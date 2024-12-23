package ua.cn.stu.univer03.debt;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class SQLExecutorController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private GlobalVariable globalVariable;

    // Обробка GET-запиту для відображення сторінки з формою
    @GetMapping("/sql")
    public String showForm() {

        // Використовуйте equals() для порівняння рядків
        if ("Initial Value".equals(globalVariable.getGlobalVar())) {
            return "redirect:/error-401"; // Це повертає сторінку із формою входу
        }

        return "sql"; // Це повертає сторінку з формою
    }

    @PostMapping("/sql")
    public String executeSql(@RequestParam("sqlQuery") String sqlQuery, Model model) {
        try {
            // Парс команду
            Pattern pattern = Pattern.compile("(?i)(insert|update|delete|read)\\s+table\\s+(\\w+)\\s*\\(([^)]+)\\)\\s*;");
            Matcher matcher = pattern.matcher(sqlQuery.trim());

            if (matcher.matches()) {
                String action = matcher.group(1).toLowerCase();
                String table = matcher.group(2);
                String data = matcher.group(3); // Дані для вставки/оновлення та ін.

                // Залежно від дії виконуємо SQL-запит
                switch (action) {
                    case "read":
                        return handleRead(table, model);
                    case "insert":
                        return handleInsert(table, data, model);
                    case "delete":
                        return handleDelete(table, data, model);
                    case "update":
                        return handleUpdate(table, data, model);
                    default:
                        model.addAttribute("error", "Unsupported SQL action.");
                        return "sql";
                }
            } else {
                model.addAttribute("error", "Invalid SQL command format.");
                return "sql";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error executing SQL: " + e.getMessage());
            return "sql";
        }
    }

    private String handleRead(String table, Model model) {
        if("users".equals(table)){
            if("ADMIN".equals(globalVariable.getGlobalVar())){

            }else{
                return "/error-403";
            }
        }
        // Перевіряємо тип таблиці та виконуємо відповідний SQL-запит
        String sql = "SELECT * FROM " + table;
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

        // Заголовки для таблиці
        List<String> headers = new ArrayList<>();
        if (!results.isEmpty()) {
            headers.addAll(results.get(0).keySet()); // Вилучаємо ключі (назви стовпців)
        }

        // Рядки даних
        List<List<Object>> rows = new ArrayList<>();
        for (Map<String, Object> result : results) {
            rows.add(new ArrayList<>(result.values())); // Витягуємо значення для кожного рядка
        }

        // Додаємо у модель
        model.addAttribute("headers", headers);
        model.addAttribute("rows", rows); // Перейменовуємо змінну із results на rows
        return "sql";  // Повертаємо ту саму сторінку
    }

    private String handleInsert(String table, String data, Model model) {

        if("ADMIN".equals(globalVariable.getGlobalVar())){
        switch (table.toLowerCase()) {
            case "sellers":
                return handleInsertSellers(data, model);
            case "products":
                return handleInsertProducts(data, model);
            case "sales":
                return handleInsertSales(data, model);
            case "users":
                return handleInsertUsers(data, model);
            default:
                model.addAttribute("error", "Unsupported table for insert.");
                return "sql";
        }
        }else{
            return "/error-403";
        }
    }

    private String handleInsertSellers(String data, Model model) {
        try {
            // Разбираем данные для вставки в таблицу sellers
            String[] fields = data.split(",");
            String[] columns = new String[fields.length];
            String[] values = new String[fields.length];
    
            for (int i = 0; i < fields.length; i++) {
                String[] field = fields[i].split("=");
                columns[i] = field[0].trim();
                String value = field[1].trim().replace("'", "");
    
                // Проверяем, является ли значение строкой (нужно добавить кавычки)
                // Регулярное выражение позволяет буквы, цифры и символы, такие как @ и точка для email
                if (value.matches("[A-Za-zА-Яа-яЁё0-9@._,\\- ]+")) { // Теперь разрешены буквы, цифры, @, точка, запятая и дефис
                    value = "'" + value + "'"; // Добавляем кавычки для строковых значений
                } else if (value.matches("[0-9]+")) { // Если это цифра
                    // Оставляем как есть
                } else {
                    // Для других типов данных можно добавить дополнительную проверку или исключение
                    throw new IllegalArgumentException("Invalid value format: " + value);
                }
    
                values[i] = value; // Присваиваем обработанное значение
            }
    
            String columnList = String.join(", ", columns);
            String valueList = String.join(", ", values);
    
            String sql = "INSERT INTO sellers (" + columnList + ") VALUES (" + valueList + ")";
            jdbcTemplate.update(sql); // Выполняем запрос
    
            model.addAttribute("message", "Record inserted successfully.");
        } catch (Exception e) {
            model.addAttribute("error", "Error executing insert SQL: " + e.getMessage());
        }
        return "sql";
    }
    
    

    private String handleInsertProducts(String data, Model model) {
        try {
            // Розбираємо дані для вставки в таблицю sellers
            String[] fields = data.split(",");
            String[] columns = new String[fields.length];
            String[] values = new String[fields.length];
    
            for (int i = 0; i < fields.length; i++) {
                String[] field = fields[i].split("=");
                columns[i] = field[0].trim();
                String value = field[1].trim();
    
                // Перевіряємо, чи є значення рядком (потрібно додати лапки)
                if (value.matches("[A-Za-z]+")) { // Якщо це рядок
                    values[i] = "'" + value + "'"; // Додаємо лапки
                } else {
                    values[i] = value; // Якщо це не рядок, залишаємо як є
                }
            }
    
            String columnList = String.join(", ", columns);
            String valueList = String.join(", ", values);
    
            String sql = "INSERT INTO products (" + columnList + ") VALUES (" + valueList + ")";
            jdbcTemplate.update(sql);
    
            model.addAttribute("message", "Record inserted successfully.");
        } catch (Exception e) {
            model.addAttribute("error", "Error executing insert SQL: " + e.getMessage());
        }
        return "sql";
    }
    

    private String handleInsertSales(String data, Model model) {
        try {
            // Розбираємо дані для вставки в таблицю sales
            String[] fields = data.split(",");
            String[] columns = new String[fields.length];
            String[] values = new String[fields.length];
    
            for (int i = 0; i < fields.length; i++) {
                String[] field = fields[i].split("=");
                columns[i] = field[0].trim();
                String value = field[1].trim().replace("'", "");
    
                // Перевіряємо, чи є значення рядком (потрібно додати лапки)
                // Регулярний вираз дозволяє букви, цифри та символи, такі як @ та точка для email
                if (value.matches("[A-Za-zА-Яа-яЁё0-9@._,\\- ]+")) { // Рядки (email, імена тощо)
                    value = "'" + value + "'"; // Додаємо лапки для рядкових значень
                } else if (value.matches("[0-9]+")) { // Цифри
                    // Залишаємо як є
                } else {
                    // Для інших типів даних можна додати додаткову перевірку або виключення
                    throw new IllegalArgumentException("Invalid value format: " + value);
                }
    
                values[i] = value; // Привласнюємо оброблене значення
            }
    
            // Збираємо рядок з іменами стовпців та значеннями
            String columnList = String.join(", ", columns);
            String valueList = String.join(", ", values);
    
            // Будуємо SQL-запит
            String sql = "INSERT INTO sales (" + columnList + ") VALUES (" + valueList + ")";
            jdbcTemplate.update(sql); // Виконуємо запит
    
            model.addAttribute("message", "Record inserted successfully.");
        } catch (Exception e) {
            model.addAttribute("error", "Error executing insert SQL: " + e.getMessage());
        }
        return "sql";
    }
    

    private String handleDelete(String table, String data, Model model) {

        if("ADMIN".equals(globalVariable.getGlobalVar())){
        switch (table.toLowerCase()) {
            case "sellers":
                return handleDeleteSellers(data, model);
            case "products":
                return handleDeleteProducts(data, model);
            case "sales":
                return handleDeleteSales(data, model);
            case "users":
                return handleDeleteUsers(data, model);
            default:
                model.addAttribute("error", "Unsupported table for delete.");
                return "sql";
        }
        }else{
            return "/error-403";
        }
    }

    private String handleDeleteSellers(String data, Model model) {
        try {
            // Розбираємо дані для видалення з таблиці sellers
            String[] fields = data.split(",");
            String id = null;
            for (String field : fields) {
                String[] pair = field.split("=");
                if (pair[0].trim().equalsIgnoreCase("id")) {
                    id = pair[1].trim().replace("'", "");
                    break;
                }
            }

            // Перевіряємо, що ID - це число
            if (id == null || !id.matches("\\d+")) {
                model.addAttribute("error", "Invalid ID format: must be a number.");
                return "sql";
            }

            String sql = "DELETE FROM sellers WHERE id = " + id;
            jdbcTemplate.update(sql);

            model.addAttribute("message", "Record deleted successfully.");
        } catch (Exception e) {
            model.addAttribute("error", "Error executing delete SQL: " + e.getMessage());
        }
        return "sql";
    }

    private String handleDeleteProducts(String data, Model model) {
        try {
            // Розбираємо дані для видалення з таблиці products
            String[] fields = data.split(",");
            String id = null;
            for (String field : fields) {
                String[] pair = field.split("=");
                if (pair[0].trim().equalsIgnoreCase("id")) {
                    id = pair[1].trim().replace("'", "");
                    break;
                }
            }
    
            // Перевіряємо, що ID - це число
            if (id == null || !id.matches("\\d+")) {
                model.addAttribute("error", "Invalid ID format: must be a number.");
                return "sql";
            }
    
            // Формуємо запит для видалення
            String sql = "DELETE FROM products WHERE id = " + id;
            jdbcTemplate.update(sql);
    
            model.addAttribute("message", "Record deleted successfully.");
        } catch (Exception e) {
            model.addAttribute("error", "Error executing delete SQL: " + e.getMessage());
        }
        return "sql";
    }

    private String handleDeleteSales(String data, Model model) {
        try {
            // Розбираємо дані для видалення з таблиці sales
            String[] fields = data.split(",");
            String id = null;
            for (String field : fields) {
                String[] pair = field.split("=");
                if (pair[0].trim().equalsIgnoreCase("id")) {
                    id = pair[1].trim().replace("'", "");
                    break;
                }
            }

            // Перевіряємо, що ID - це число
            if (id == null || !id.matches("\\d+")) {
                model.addAttribute("error", "Invalid ID format: must be a number.");
                return "sql";
            }

            String sql = "DELETE FROM sales WHERE id = " + id;
            jdbcTemplate.update(sql);

            model.addAttribute("message", "Record deleted successfully.");
        } catch (Exception e) {
            model.addAttribute("error", "Error executing delete SQL: " + e.getMessage());
        }
        return "sql";
    }

    private String handleUpdate(String table, String data, Model model) {

        if("ADMIN".equals(globalVariable.getGlobalVar())){
        switch (table.toLowerCase()) {
            case "sellers":
                return handleUpdateSellers(data, model);
            case "products":
                return handleUpdateProducts(data, model);
            case "sales":
                return handleUpdateSales(data, model);
            case "users":
                return handleUpdateUsers(data, model);
            default:
                model.addAttribute("error", "Unsupported table for update.");
                return "sql";
        }
        }else{
            return "/error-403";
        }
    }

    private String handleUpdateSellers(String data, Model model) {
        try {
            // Розбираємо дані для оновлення у таблиці sellers
            String[] fields = data.split(",");
            String id = null;
            List<String> assignments = new ArrayList<>();
    
            for (String field : fields) {
                String[] pair = field.split("=");
                String column = pair[0].trim();
                String value = pair[1].trim().replace("'", "");
    
                if (column.equalsIgnoreCase("id")) {
                    id = value; // Зберігаємо ID для умови
                } else {
                    // Перевіряємо, чи є значення рядком (потрібно додати лапки)
                    if (value.matches("[A-Za-zА-Яа-яЁё0-9@._,\\- ]+")) { // Рядки (наприклад, email)
                        value = "'" + value + "'"; // Додаємо лапки для рядкових значень
                    }
                    assignments.add(column + " = " + value); // Додаємо поле та значення
                }
            }
    
            // Перевірка: ID має бути вказано і бути числом
            if (id == null || !id.matches("\\d+")) {
                model.addAttribute("error", "Invalid or missing ID in the update command.");
                return "sql";
            }
    
            // Формуємо SQL-запит
            String setClause = String.join(", ", assignments);
            String sql = "UPDATE sellers SET " + setClause + " WHERE id = " + id;
    
            // Виконуємо запит
            jdbcTemplate.update(sql);
    
            model.addAttribute("message", "Record updated successfully.");
        } catch (Exception e) {
            model.addAttribute("error", "Error executing update SQL: " + e.getMessage());
        }
        return "sql";
    }
    

    private String handleUpdateProducts(String data, Model model) {
        try {
            // Розбираємо дані для оновлення у таблиці
            String[] fields = data.split(",");
            String id = null;
            List<String> assignments = new ArrayList<>();
    
            for (String field : fields) {
                String[] pair = field.split("=");
                String column = pair[0].trim();
                String value = pair[1].trim();
    
                if (column.equalsIgnoreCase("id")) {
                    id = value.replace("'", ""); // Зберігаємо ID для умови
                } else {
                    assignments.add(column + " = " + value); // Додаємо поле та значення
                }
            }
    
            // Перевірка: ID має бути вказано і бути числом
            if (id == null || !id.matches("\\d+")) {
                model.addAttribute("error", "Invalid or missing ID in the update command.");
                return "sql";
            }
    
            // Формуємо SQL-запит
            String setClause = String.join(", ", assignments);
            String sql = "UPDATE products SET " + setClause + " WHERE id = " + id;
    
            // Виконуємо запит
            jdbcTemplate.update(sql);
    
            model.addAttribute("message", "Record updated successfully.");
        } catch (Exception e) {
            model.addAttribute("error", "Error executing update SQL: " + e.getMessage());
        }
        return "sql";
    }
    
    private String handleUpdateSales(String data, Model model) {
        try {
            // Розбираємо дані для оновлення у таблиці sales
            String[] fields = data.split(",");
            String id = null;
            List<String> assignments = new ArrayList<>();
    
            for (String field : fields) {
                String[] pair = field.split("=");
                String column = pair[0].trim();
                String value = pair[1].trim().replace("'", "");
    
                if (column.equalsIgnoreCase("id")) {
                    id = value; // Зберігаємо ID для умови
                } else {
                    // Перевіряємо, чи є значення рядком (потрібно додати лапки)
                    if (value.matches("[A-Za-zА-Яа-яЁё0-9@._,\\- ]+")) { // Рядки (наприклад, email)
                        value = "'" + value + "'"; // Додаємо лапки для рядкових значень
                    }
                    assignments.add(column + " = " + value); // Додаємо поле та значення
                }
            }
    
            // Перевірка: ID має бути вказано і бути числом
            if (id == null || !id.matches("\\d+")) {
                model.addAttribute("error", "Invalid or missing ID in the update command.");
                return "sql";
            }
    
            // Формуємо SQL-запит
            String setClause = String.join(", ", assignments);
            String sql = "UPDATE sales SET " + setClause + " WHERE id = " + id;
    
            // Виконуємо запит
            jdbcTemplate.update(sql);
    
            model.addAttribute("message", "Record updated successfully.");
        } catch (Exception e) {
            model.addAttribute("error", "Error executing update SQL: " + e.getMessage());
        }
        return "sql";
    }

    // Додавання запису до таблиці users
private String handleInsertUsers(String data, Model model) {
    try {
        // Розбираємо дані для вставки в таблицю users
        String[] fields = data.split(",");
        String[] columns = new String[fields.length];
        String[] values = new String[fields.length];

        for (int i = 0; i < fields.length; i++) {
            String[] field = fields[i].split("=");
            columns[i] = field[0].trim();
            String value = field[1].trim().replace("'", "");

            // Перевіряємо, чи є значення рядком (потрібно додати лапки)
            if (value.matches("[A-Za-zА-Яа-яЁё0-9@._,\\- ]+")) { // Рядки (наприклад, email, імена)
                value = "'" + value + "'"; // Додаємо лапки для рядкових значень
            } else if (value.matches("[0-9]+")) { // Якщо це цифра
                // Залишаємо як є
            } else {
                throw new IllegalArgumentException("Invalid value format: " + value);
            }

            values[i] = value; // Привласнюємо оброблене значення
        }

        String columnList = String.join(", ", columns);
        String valueList = String.join(", ", values);

        String sql = "INSERT INTO users (" + columnList + ") VALUES (" + valueList + ")";
        jdbcTemplate.update(sql); // Виконуємо запит

        model.addAttribute("message", "User record inserted successfully.");
    } catch (Exception e) {
        model.addAttribute("error", "Error executing insert SQL: " + e.getMessage());
    }
    return "sql";
}

// Видалення запису з таблиці users
private String handleDeleteUsers(String data, Model model) {
    try {
        // Розбираємо дані для видалення з таблиці users
        String[] fields = data.split(",");
        String id = null;
        for (String field : fields) {
            String[] pair = field.split("=");
            if (pair[0].trim().equalsIgnoreCase("id")) {
                id = pair[1].trim().replace("'", "");
                break;
            }
        }

        if (id == null || !id.matches("\\d+")) {
            model.addAttribute("error", "Invalid ID format: must be a number.");
            return "sql";
        }

        String sql = "DELETE FROM users WHERE id = " + id;
        jdbcTemplate.update(sql);

        model.addAttribute("message", "User record deleted successfully.");
    } catch (Exception e) {
        model.addAttribute("error", "Error executing delete SQL: " + e.getMessage());
    }
    return "sql";
}

// Оновлення запису в таблиці users
private String handleUpdateUsers(String data, Model model) {
    try {
        // Розбираємо дані для оновлення у таблиці users
        String[] fields = data.split(",");
        String id = null;
        List<String> assignments = new ArrayList<>();

        for (String field : fields) {
            String[] pair = field.split("=");
            String column = pair[0].trim();
            String value = pair[1].trim().replace("'", "");

            if (column.equalsIgnoreCase("id")) {
                id = value; // Зберігаємо ID для умови
            } else {
                // Перевіряємо, чи є значення рядком (потрібно додати лапки)
                if (value.matches("[A-Za-zА-Яа-яЁё0-9@._,\\- ]+")) { // Рядки (наприклад, email)
                    value = "'" + value + "'"; // Додаємо лапки для рядкових значень
                }
                assignments.add(column + " = " + value); // Додаємо поле та значення
            }
        }

        // Перевірка: ID має бути вказано і бути числом
        if (id == null || !id.matches("\\d+")) {
            model.addAttribute("error", "Invalid or missing ID in the update command.");
            return "sql";
        }

        String setClause = String.join(", ", assignments);
        String sql = "UPDATE users SET " + setClause + " WHERE id = " + id;

        jdbcTemplate.update(sql);

        model.addAttribute("message", "User record updated successfully.");
    } catch (Exception e) {
        model.addAttribute("error", "Error executing update SQL: " + e.getMessage());
    }
    return "sql";
}

    
}
