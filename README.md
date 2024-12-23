#

1. Завантажити шаблон

- Розархівуйте, відкрийте папку в терміналі та в IDE
- run -> exception
- comment openai deps in pom.xml -> run -> no exception

2. Запусти pgAdmin та завантаж туди БД з файлу sql_schema/my_data_Custom .sql

3. Налаштувати доступ до БД у програмі Java. Заповнити своїми налаштуваннями в pgAdmin.

- application.properties:
  ```
  spring.datasource.url=jdbc:postgresql://localhost:5432/my_date
  spring.datasource.username=username 
  spring.datasource.password=password
  ```

4. install db plugin to IDE OR use other client

- vscode - Database Client + Database Client JDBC

5. Запустити проект та перейти у браузер за посиланням locahttp://localhost:8080/login

6. Залогінитися під адміном і налаштовувати БД під себе

- Дані для входу:
  ```
  Login: admin
  Password: adminpass
  ```

6. Команди для роботи з БД

- Команди:
  ```
  read table nametable (id);
  insert table nametable (id='?', . . .);
  delete table nametable (id='?');
  update table nametable (id='?', . . .);
  ```