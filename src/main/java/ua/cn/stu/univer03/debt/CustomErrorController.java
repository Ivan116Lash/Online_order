package ua.cn.stu.univer03.debt;

import javax.servlet.http.HttpServletRequest;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request) {
        // Отримуємо статус помилки із запиту
        Object status = request.getAttribute("javax.servlet.error.status_code");

        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());

            // Обробка різних кодів помилок
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error-404";  // Сторінка помилки 404
            } else if (statusCode == HttpStatus.FORBIDDEN.value()) {
                return "error-403";  // Сторінка помилки 403
            } else if (statusCode == HttpStatus.UNAUTHORIZED.value()) {
                return "error-401";  // Сторінка помилки 401
            }
        }
        return "error";  // Сторінка за промовчанням для всіх інших помилок
    }
}
