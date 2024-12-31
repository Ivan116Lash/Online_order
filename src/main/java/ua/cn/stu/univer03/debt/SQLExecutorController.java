package ua.cn.stu.univer03.debt;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
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
        List<String> allowedTables = List.of("users", "orders", "products", "sellers");
        if (!allowedTables.contains(table)) {
            return "/error-403"; // Если таблица не разрешена, возвращаем ошибку
        }

        if ("users".equals(table) && !"ADMIN".equals(globalVariable.getGlobalVar())) {
            return "/error-403";
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
                return handleInsertOnl(table, data, model);
            case "products":
                return handleInsertOnl(table, data, model);
            case "sales":
                return handleInsertOnl(table, data, model);
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

    private String handleInsertOnl(String table, String data, Model model) {
        try {
            
            String[] fields = data.split(",");  // Розділяємо рядок на окремі пари 'ключ=значення'
            String[] columns = new String[fields.length];  // Масив для зберігання назв стовпців
            Object[] values = new Object[fields.length];  // Масив для значень, що будуть вставлені
            
            for (int i = 0; i < fields.length; i++) {
                String[] field = fields[i].split("=");  // Розділяємо кожен елемент на стовпець і значення
                columns[i] = field[0].trim();  // Записуємо назву стовпця
                
                String value = field[1].trim();  // Значення, яке потрібно вставити в таблицю
                
                // Перевірка, чи є це поле 'id', 'seller_id', 'product_id', або 'quantity'
                if (columns[i].equals("id") || columns[i].equals("seller_id") || columns[i].equals("product_id") || columns[i].equals("quantity")) {
                    values[i] = Integer.parseInt(value);  // Перетворюємо значення в ціле число для полів
                } 
                // Додаємо перевірку для дати
                else if (columns[i].equals("sale_date")) {
                    // Перетворюємо дату у формат 'yyyy-MM-dd' в java.sql.Date
                    values[i] = java.sql.Date.valueOf(value);  
                }
                // Перевірка для поля 'price' - перетворення на число з плаваючою точкою
                else if (columns[i].equals("price")) {
                    values[i] = Double.parseDouble(value);  // Перетворюємо значення в число з плаваючою точкою для 'price'
                } 
                else {
                    values[i] = value;  // Для інших полів зберігаємо значення як рядок
                }
            }
            
            // Створюємо частину SQL запиту зі списком стовпців
            String columnList = String.join(", ", columns);
            
            // Створюємо заповнювачі для значень ('?' для кожного значення)
            String placeholders = String.join(", ", Collections.nCopies(columns.length, "?"));
    
            String sql;

            // Використовуємо параметризований запит для безпеки
            switch (table.toLowerCase()) {
                case "sellers":
                    sql = "INSERT INTO sellers (" + columnList + ") VALUES (" + placeholders + ")";
                    break;
                case "products":
                    sql = "INSERT INTO products (" + columnList + ") VALUES (" + placeholders + ")";
                    break;
                case "sales":
                    sql = "INSERT INTO sales (" + columnList + ") VALUES (" + placeholders + ")";
                    break;
                default:
                    model.addAttribute("error", "Unsupported table for delete.");
                    return "sql";
            }
            
            jdbcTemplate.update(sql, values);  // Передаємо значення як параметри запиту
    
            model.addAttribute("message", "Record inserted successfully.");
        } catch (Exception e) {
            model.addAttribute("error", "Error executing insert SQL: " + e.getMessage());
        }
        return "sql";  // Повертаємо представлення
    }
    
    private String handleDelete(String table, String data, Model model) {

        if("ADMIN".equals(globalVariable.getGlobalVar())){
        switch (table.toLowerCase()) {
            case "sellers":
                return handleDeleteOnl(table, data, model);
            case "products":
                return handleDeleteOnl(table, data, model);
            case "sales":
                return handleDeleteOnl(table, data, model);
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

    private String handleDeleteOnl(String table, String data, Model model) {
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
            String sql;

            // Використовуємо параметризований запит для безпеки
            switch (table.toLowerCase()) {
                case "sellers":
                    sql = "DELETE FROM sellers WHERE id = ?";
                    break;
                case "products":
                    sql = "DELETE FROM products WHERE id = ?";
                    break;
                case "sales":
                    sql = "DELETE FROM sales WHERE id = ?";
                    break;
                default:
                    model.addAttribute("error", "Unsupported table for delete.");
                    return "sql";
            }

            jdbcTemplate.update(sql, Integer.parseInt(id));  // Передаємо ID як параметр

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
                return handleUpdateOnl(table, data, model);
            case "products":
                return handleUpdateOnl(table, data, model);
            case "sales":
                return handleUpdateOnl(table, data, model);
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

    private String handleUpdateOnl(String table, String data, Model model) {
        try {
            // Розбираємо дані для оновлення у таблиці sellers
            String[] fields = data.split(",");
            String id = null;
            List<String> assignments = new ArrayList<>();
            List<Object> values = new ArrayList<>();  // Список для значень (параметрів)
    
            for (String field : fields) {
                String[] pair = field.split("=");
                String column = pair[0].trim();
                String value = pair[1].trim().replace("'", "");
    
                if (column.equalsIgnoreCase("id")) {
                    id = value; // Зберігаємо ID для умови
                } else {
                    // Перевіряємо, чи є значення числом
                    if (column.equals("id") || column.equals("seller_id") || column.equals("product_id") || column.equals("quantity")) {
                        values.add(Integer.parseInt(value)); // Якщо число, додаємо в список як ціле
                    } 
                    // Перевірка для дати
                    else if (column.equals("sale_date")) {
                        values.add(java.sql.Date.valueOf(value)); // Якщо дата, додаємо в список як java.sql.Date
                    }
                    // Перевірка для ціни
                    else if (column.equals("price")) {
                        values.add(Double.parseDouble(value)); // Якщо ціна, додаємо в список як число з плаваючою точкою
                    } 
                    else {
                        values.add(value); // Для інших значень додаємо як рядок
                    }
                    assignments.add(column + " = ?"); // Додаємо поле та місце для параметра
                }
            }
    
            // Перевірка: ID має бути вказано і бути числом
            if (id == null || !id.matches("\\d+")) {
                model.addAttribute("error", "Invalid or missing ID in the update command.");
                return "sql";
            }
    
            // Формуємо SQL-запит з параметризованими значеннями
            String setClause = String.join(", ", assignments);
            String sql;
    
            switch (table.toLowerCase()) {
                case "sellers":
                    sql = "UPDATE sellers SET " + setClause + " WHERE id = ?";
                    break;
                case "products":
                    sql = "UPDATE products SET " + setClause + " WHERE id = ?";
                    break;
                case "sales":
                    sql = "UPDATE sales SET " + setClause + " WHERE id = ?";
                    break;
                default:
                    model.addAttribute("error", "Unsupported table for update.");
                    return "sql";
            }
    
            // Додаємо ID до параметрів
            values.add(Integer.parseInt(id));
    
            // Виконуємо запит з параметрами
            jdbcTemplate.update(sql, values.toArray());
    
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
        List<Object> values = new ArrayList<>();  // Список для значень (параметрів)

        for (int i = 0; i < fields.length; i++) {
            String[] field = fields[i].split("=");
            String column = field[0].trim();
            String value = field[1].trim().replace("'", "");

            // Зберігаємо стовпець
            columns[i] = column;

            // Перевірка для поля 'id'
            if (column.equals("id")) {
                values.add(Integer.parseInt(value));  // Перетворюємо значення в ціле число для полів 'id'
            } 
            else if (column.equals("password")) {
                values.add(encodeBase64(value));  // Кодуємо пароль в base64
            } 
            else {
                values.add(value);  // Для інших полів додаємо значення як рядок
            }
        }

        // Створюємо частину SQL запиту зі списком стовпців
        String columnList = String.join(", ", columns);

        // Створюємо заповнювачі для значень ('?' для кожного значення)
        String placeholders = String.join(", ", Collections.nCopies(columns.length, "?"));

        // Формуємо SQL-запит
        String sql = "INSERT INTO users (" + columnList + ") VALUES (" + placeholders + ")";

        // Використовуємо параметризований запит для безпеки
        jdbcTemplate.update(sql, values.toArray());

        model.addAttribute("message", "User record inserted successfully.");
    } catch (Exception e) {
        model.addAttribute("error", "Error executing insert SQL: " + e.getMessage());
    }
    return "sql";
}

    // Функція для кодування в base64
    private String encodeBase64(String value) {
        return Base64.getEncoder().encodeToString(value.getBytes(StandardCharsets.UTF_8));
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

        // Перевірка: ID має бути вказано і бути числом
        if (id == null || !id.matches("\\d+")) {
            model.addAttribute("error", "Invalid ID format: must be a number.");
            return "sql";
        }

        // Використовуємо параметризований запит для безпеки
        String sql = "DELETE FROM users WHERE id = ?";
        jdbcTemplate.update(sql, Integer.parseInt(id));

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
        List<Object> values = new ArrayList<>();  // Список для значень (параметрів)

        for (String field : fields) {
            String[] pair = field.split("=");
            String column = pair[0].trim();
            String value = pair[1].trim().replace("'", "");

            if (column.equalsIgnoreCase("id")) {
                id = value; // Зберігаємо ID для умови
            } else {
                // Перевіряємо, чи є значення числом
                if (column.equals("id")) {
                    values.add(Integer.parseInt(value)); // Якщо число, додаємо в список як ціле
                }
                else if (column.equals("password")) {
                    values.add(encodeBase64(value));  // Кодуємо пароль в base64
                } 
                else {
                    values.add(value); // Для інших значень додаємо як рядок
                }
                assignments.add(column + " = ?"); // Додаємо поле та місце для параметра
            }
        }

        // Перевірка: ID має бути вказано і бути числом
        if (id == null || !id.matches("\\d+")) {
            model.addAttribute("error", "Invalid or missing ID in the update command.");
            return "sql";
        }

        // Формуємо частину SQL запиту зі списком полів
        String setClause = String.join(", ", assignments);
        
        // Створюємо SQL запит
        String sql = "UPDATE users SET " + setClause + " WHERE id = ?";
        
        // Додаємо ID до значень
        values.add(Integer.parseInt(id));
        
        // Виконуємо параметризований запит
        jdbcTemplate.update(sql, values.toArray());

        model.addAttribute("message", "User record updated successfully.");
    } catch (Exception e) {
        model.addAttribute("error", "Error executing update SQL: " + e.getMessage());
    }
    return "sql";
}


    
}
