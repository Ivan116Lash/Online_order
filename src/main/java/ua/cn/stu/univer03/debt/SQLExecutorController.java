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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SQLExecutorController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @PostMapping("/sql")
    public String executeSql(@RequestParam("sqlQuery") String sqlQuery, Model model) {
        try {
            // Парсим команду
            Pattern pattern = Pattern.compile("(?i)(insert|update|delete|read)\\s+table\\s+(\\w+)\\s*\\(([^)]+)\\)\\s*;");
            Matcher matcher = pattern.matcher(sqlQuery.trim());

            if (matcher.matches()) {
                String action = matcher.group(1).toLowerCase();
                String table = matcher.group(2);
                String data = matcher.group(3); // Данные для вставки/обновления и т.д.

                // В зависимости от действия, выполняем SQL-запрос
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
        // Проверяем тип таблицы и выполняем соответствующий SQL-запрос
        String sql = "SELECT * FROM " + table;
        List<Map<String, Object>> results = jdbcTemplate.queryForList(sql);

        // Заголовки для таблицы
        List<String> headers = new ArrayList<>();
        if (!results.isEmpty()) {
            headers.addAll(results.get(0).keySet());
        }

        // Строки данных
        List<List<Object>> rows = new ArrayList<>();
        for (Map<String, Object> result : results) {
            rows.add(new ArrayList<>(result.values()));
        }

        // Добавляем в модель
        model.addAttribute("headers", headers);
        model.addAttribute("results", rows);
        return "sql";  // Возвращаем ту же страницу
    }

    private String handleInsert(String table, String data, Model model) {
        switch (table.toLowerCase()) {
            case "sellers":
                return handleInsertSellers(data, model);
            case "products":
                return handleInsertProducts(data, model);
            case "sales":
                return handleInsertSales(data, model);
            default:
                model.addAttribute("error", "Unsupported table for insert.");
                return "sql";
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
            // Разбираем данные для вставки в таблицу products
            String[] fields = data.split(",");
            String[] columns = new String[fields.length];
            String[] values = new String[fields.length];
    
            for (int i = 0; i < fields.length; i++) {
                String[] field = fields[i].split("=");
                columns[i] = field[0].trim();
                String value = field[1].trim();
    
                // Проверяем, является ли значение строкой (нужно добавить кавычки)
                if (value.matches("[A-Za-z]+")) { // Если это строка
                    values[i] = "'" + value + "'"; // Добавляем кавычки
                } else {
                    values[i] = value; // Если это не строка, оставляем как есть
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
            // Разбираем данные для вставки в таблицу sales
            String[] fields = data.split(",");
            String[] columns = new String[fields.length];
            String[] values = new String[fields.length];
    
            for (int i = 0; i < fields.length; i++) {
                String[] field = fields[i].split("=");
                columns[i] = field[0].trim();
                String value = field[1].trim().replace("'", "");
    
                // Проверяем, является ли значение строкой (нужно добавить кавычки)
                // Регулярное выражение позволяет буквы, цифры и символы, такие как @ и точка для email
                if (value.matches("[A-Za-zА-Яа-яЁё0-9@._,\\- ]+")) { // Строки (email, имена и т.д.)
                    value = "'" + value + "'"; // Добавляем кавычки для строковых значений
                } else if (value.matches("[0-9]+")) { // Цифры
                    // Оставляем как есть
                } else {
                    // Для других типов данных можно добавить дополнительную проверку или исключение
                    throw new IllegalArgumentException("Invalid value format: " + value);
                }
    
                values[i] = value; // Присваиваем обработанное значение
            }
    
            // Собираем строку с именами столбцов и значениями
            String columnList = String.join(", ", columns);
            String valueList = String.join(", ", values);
    
            // Строим SQL-запрос
            String sql = "INSERT INTO sales (" + columnList + ") VALUES (" + valueList + ")";
            jdbcTemplate.update(sql); // Выполняем запрос
    
            model.addAttribute("message", "Record inserted successfully.");
        } catch (Exception e) {
            model.addAttribute("error", "Error executing insert SQL: " + e.getMessage());
        }
        return "sql";
    }
    

    private String handleDelete(String table, String data, Model model) {
        switch (table.toLowerCase()) {
            case "sellers":
                return handleDeleteSellers(data, model);
            case "products":
                return handleDeleteProducts(data, model);
            case "sales":
                return handleDeleteSales(data, model);
            default:
                model.addAttribute("error", "Unsupported table for delete.");
                return "sql";
        }
    }

    private String handleDeleteSellers(String data, Model model) {
        try {
            // Разбираем данные для удаления из таблицы sellers
            String[] fields = data.split(",");
            String id = null;
            for (String field : fields) {
                String[] pair = field.split("=");
                if (pair[0].trim().equalsIgnoreCase("id")) {
                    id = pair[1].trim().replace("'", "");
                    break;
                }
            }

            // Проверяем, что ID - это число
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
            // Разбираем данные для удаления из таблицы products
            String[] fields = data.split(",");
            String id = null;
            for (String field : fields) {
                String[] pair = field.split("=");
                if (pair[0].trim().equalsIgnoreCase("id")) {
                    id = pair[1].trim().replace("'", "");
                    break;
                }
            }
    
            // Проверяем, что ID - это число
            if (id == null || !id.matches("\\d+")) {
                model.addAttribute("error", "Invalid ID format: must be a number.");
                return "sql";
            }
    
            // Формируем запрос для удаления
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
            // Разбираем данные для удаления из таблицы sales
            String[] fields = data.split(",");
            String id = null;
            for (String field : fields) {
                String[] pair = field.split("=");
                if (pair[0].trim().equalsIgnoreCase("id")) {
                    id = pair[1].trim().replace("'", "");
                    break;
                }
            }

            // Проверяем, что ID - это число
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
        switch (table.toLowerCase()) {
            case "sellers":
                return handleUpdateSellers(data, model);
            case "products":
                return handleUpdateProducts(data, model);
            case "sales":
                return handleUpdateSales(data, model);
            default:
                model.addAttribute("error", "Unsupported table for update.");
                return "sql";
        }
    }

    private String handleUpdateSellers(String data, Model model) {
        try {
            // Разбираем данные для обновления в таблице sellers
            String[] fields = data.split(",");
            String id = null;
            List<String> assignments = new ArrayList<>();
    
            for (String field : fields) {
                String[] pair = field.split("=");
                String column = pair[0].trim();
                String value = pair[1].trim().replace("'", "");
    
                if (column.equalsIgnoreCase("id")) {
                    id = value; // Сохраняем ID для условия
                } else {
                    // Проверяем, является ли значение строкой (нужно добавить кавычки)
                    if (value.matches("[A-Za-zА-Яа-яЁё0-9@._,\\- ]+")) { // Строки (например, email)
                        value = "'" + value + "'"; // Добавляем кавычки для строковых значений
                    }
                    assignments.add(column + " = " + value); // Добавляем поле и значение
                }
            }
    
            // Проверка: ID должен быть указан и являться числом
            if (id == null || !id.matches("\\d+")) {
                model.addAttribute("error", "Invalid or missing ID in the update command.");
                return "sql";
            }
    
            // Формируем SQL-запрос
            String setClause = String.join(", ", assignments);
            String sql = "UPDATE sellers SET " + setClause + " WHERE id = " + id;
    
            // Выполняем запрос
            jdbcTemplate.update(sql);
    
            model.addAttribute("message", "Record updated successfully.");
        } catch (Exception e) {
            model.addAttribute("error", "Error executing update SQL: " + e.getMessage());
        }
        return "sql";
    }
    

    private String handleUpdateProducts(String data, Model model) {
        try {
            // Разбираем данные для обновления
            String[] fields = data.split(",");
            String id = null;
            List<String> assignments = new ArrayList<>();
    
            for (String field : fields) {
                String[] pair = field.split("=");
                String column = pair[0].trim();
                String value = pair[1].trim();
    
                if (column.equalsIgnoreCase("id")) {
                    id = value.replace("'", ""); // Сохраняем ID для условия
                } else {
                    assignments.add(column + " = " + value); // Добавляем поле и значение
                }
            }
    
            // Проверка: ID должен быть указан и являться числом
            if (id == null || !id.matches("\\d+")) {
                model.addAttribute("error", "Invalid or missing ID in the update command.");
                return "sql";
            }
    
            // Формируем SQL-запрос
            String setClause = String.join(", ", assignments);
            String sql = "UPDATE products SET " + setClause + " WHERE id = " + id;
    
            // Выполняем запрос
            jdbcTemplate.update(sql);
    
            model.addAttribute("message", "Record updated successfully.");
        } catch (Exception e) {
            model.addAttribute("error", "Error executing update SQL: " + e.getMessage());
        }
        return "sql";
    }
    
    
    

    private String handleUpdateSales(String data, Model model) {
        try {
            // Разбираем данные для обновления в таблице sales
            String[] fields = data.split(",");
            String id = null;
            List<String> assignments = new ArrayList<>();
    
            for (String field : fields) {
                String[] pair = field.split("=");
                String column = pair[0].trim();
                String value = pair[1].trim().replace("'", "");
    
                if (column.equalsIgnoreCase("id")) {
                    id = value; // Сохраняем ID для условия
                } else {
                    // Проверяем, является ли значение строкой (нужно добавить кавычки)
                    if (value.matches("[A-Za-zА-Яа-яЁё0-9@._,\\- ]+")) { // Строки (например, email)
                        value = "'" + value + "'"; // Добавляем кавычки для строковых значений
                    }
                    assignments.add(column + " = " + value); // Добавляем поле и значение
                }
            }
    
            // Проверка: ID должен быть указан и являться числом
            if (id == null || !id.matches("\\d+")) {
                model.addAttribute("error", "Invalid or missing ID in the update command.");
                return "sql";
            }
    
            // Формируем SQL-запрос
            String setClause = String.join(", ", assignments);
            String sql = "UPDATE sales SET " + setClause + " WHERE id = " + id;
    
            // Выполняем запрос
            jdbcTemplate.update(sql);
    
            model.addAttribute("message", "Record updated successfully.");
        } catch (Exception e) {
            model.addAttribute("error", "Error executing update SQL: " + e.getMessage());
        }
        return "sql";
    }
    
}
/* 
@Controller
public class SQLExecutorController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    // Маршрут для отображения страницы с формой
    @GetMapping("/sql")
    public String showSqlPage() {
        return "sql"; // Возвращает имя шаблона для отображения формы
    }

    // Маршрут для обработки SQL-запроса
    @PostMapping("/sql")
    public String executeSql(@RequestParam("sqlQuery") String sqlQuery, Model model) {
        try {
            if (sqlQuery.trim().toUpperCase().startsWith("SELECT")) {
                // Выполняем SQL-запрос и получаем результаты для SELECT
                List<Map<String, Object>> results = jdbcTemplate.queryForList(sqlQuery);

                // Получаем заголовки (названия столбцов)
                List<String> headers = new ArrayList<>();
                if (!results.isEmpty()) {
                    headers.addAll(results.get(0).keySet());
                }

                // Получаем строки данных
                List<List<Object>> rows = new ArrayList<>();
                for (Map<String, Object> result : results) {
                    rows.add(new ArrayList<>(result.values()));
                }

                // Добавляем данные в модель
                model.addAttribute("headers", headers);
                model.addAttribute("rows", rows);
            } else {
                // Для запросов типа INSERT, UPDATE, DELETE используем update()
                int rowsAffected = jdbcTemplate.update(sqlQuery);

                // Добавляем информацию о том, сколько строк было изменено
                model.addAttribute("message", rowsAffected + " row(s) affected.");
            }
        } catch (Exception e) {
            // Обрабатываем ошибки выполнения SQL-запроса
            model.addAttribute("error", "Error executing SQL: " + e.getMessage());
        }

        return "sql"; // Возвращаем имя шаблона для отображения результата
    }
}*/