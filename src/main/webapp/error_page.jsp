<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ru">
<head>
    <meta charset="UTF-8">
    <title>Ошибка 401</title>
    <meta name="description" content="Ошибка доступа - требуется авторизация">
    <style>
        body {
            font-family: Arial, sans-serif;
            text-align: center;
            padding: 50px;
            background-color: #f4f4f4;
            color: #333;
        }
        h1 {
            font-size: 100px;
            margin: 0;
            color: dimgrey;
        }
        h2 {
            font-size: 30px;
            margin: 20px 0;
        }
        p {
            font-size: 18px;
            line-height: 1.6;
            margin: 20px 0;
        }
        a {
            color: #337ab7;
            text-decoration: none;
            font-weight: bold;
        }
        a:hover {
            text-decoration: underline;
        }
        .container {
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
            background-color: white;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>401</h1>
    <h2>Доступ запрещен</h2>
    <p>Данная страница доступна только авторизованным пользователям.</p>
    <p>
        <a href="/Lab4/">Вернуться на главную страницу</a>
    </p>
</div>
</body>
</html>