package ua.cn.stu.univer03.debt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AccessConfig {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GlobalVariable globalVariable;

    // Маршрут для відображення сторінки з формою
    @GetMapping("/login")
    public String showSqlPage() {
        
        return "/login"; // Повертає ім'я шаблону для відображення форми
    }

    // Маршрут для обробки SQL-запиту
    @PostMapping("/login")
    public String processLoginForm(@RequestParam String username, @RequestParam String password, Model model) {
        // Приклад SQL-запиту для перевірки користувача
        String query = "SELECT password FROM users WHERE username = ?";
        
        try {
            String storedPassword = jdbcTemplate.queryForObject(query, String.class, username);
            if (storedPassword != null && storedPassword.equals(password)) {
                // Отримуємо роль користувача
                String roleQuery = "SELECT role FROM users WHERE username = ?";
                String role = jdbcTemplate.queryForObject(roleQuery, String.class, username);
                
                // Встановлюємо значення ролі у глобальну змінну
                globalVariable.setGlobalVar(role);
                // Успішний вхід
                return "redirect:/sql"; // Перенаправление на другую страницу
            } else {
                // Невірний пароль
                model.addAttribute("error", "Invalid username or password");
                String invat = "Initial Value";
                globalVariable.setGlobalVar(invat);
                return "/error-401"; // Повернення до сторінки входу з повідомленням про помилку
            }
        } catch (EmptyResultDataAccessException e) {
            // Користувач не знайдено
            model.addAttribute("error", "User not found");
            String invat = "Initial Value";
            globalVariable.setGlobalVar(invat);
            return "/error-401"; // Повертаємо ім'я шаблону для відображення результату
        }
    }
}
