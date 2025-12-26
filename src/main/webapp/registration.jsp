<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <title>Lab 4 - Регистрация</title>
    <script src="https://cdn.jsdelivr.net/npm/vue@2.6.14/dist/vue.min.js"></script>
    <link rel="stylesheet" href="styles/header.css"/>
    <link rel="stylesheet" href="styles/registartion-page.css"/>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.4.0/jquery.min.js"></script>
</head>

<body>
<header>
    <div id="info">
        <div id="info-lab-name">
            <h1><strong>Лабораторная работа №4</strong></h1>
        </div>
        <div class="horizontal-container">
            <div id="info-name" class="form-element">
                <span><strong>Студенты:</strong> Прокопенко Сергей Петрович</span>
            </div>
            <div id="info-group" class="form-element">
                <span><strong>Группа:</strong> P3207</span>
            </div>
            <div id="info-variant" class="form-element">
                <span><strong>Вариант:</strong> №843501</span>
            </div>
        </div>
    </div>
</header>

<div id="custom-alert-container"></div>

<div id="app">
    <center>
        <h3 style="margin-top: 20px; margin-bottom: 30px;">Регистрация нового пользователя</h3>
    </center>
</div>

<div class="form-row">
    <div class="col-4"></div>
    <div class="col-4" id="form">
        <br>

        <div class="form-row">
            <div class="pole">
                <h6>Логин (минимум 4 символа):</h6>
                <input v-model="login" id="login-text" placeholder="Введите логин" type="text" class="form-control is-valid"
                       style="min-width:200px" required>
            </div>
        </div>
        <br>

        <div class="form-row">
            <div class="pole">
                <h6>Пароль (минимум 4 символа):</h6>
                <input v-model="pass" id="password-text" placeholder="Введите пароль" class="form-control is-valid" type="password"
                       style="min-width:200px" required>
            </div>
        </div>
        <br>

        <div class="form-row">
            <div class="pole">
                <input v-model="passR" id="password-r-text" placeholder="Введите пароль повторно" class="form-control is-valid" type="password"
                       style="min-width:200px" required>
            </div>
        </div>
        <br>

        <div class="form-row d-flex align-items-center registration-button-block">
            <div class="col-4">
                <button type="submit" class="action-button-styled" v-on:click="greet">Зарегистрироваться</button>
            </div>
            <div class="col-1"></div>
            <div class="col-7">
                <h6><strong>Статус: </strong> <span id="register-status"></span></h6>
            </div>
        </div>
    </div>
    <div class="col-4"></div>
</div>

<div class="go-back-wrapper bottom-margin" style="clear: both; width: 100%; text-align: center;">
    <button onclick="location.href = '../Lab4/';" class="action-button" style="width: 150px; height: 40px; border-radius: 10px;">Обратно</button>
</div>

<script src="scripts/index.js"></script>
<script>
    new Vue({
        el: '#form',
        data: {
            login: "",
            pass: "",
            passR: ""
        },
        methods: {
            greet: function (event) {

                if (this.login.length < 4) {
                    document.getElementById("login-text").classList.replace("is-valid", "is-invalid");
                    document.getElementById("register-status").innerText = "Логин должен быть минимум 4 символа!";
                    return;
                }
                if (this.pass.length < 4) {
                    document.getElementById("password-text").classList.replace("is-valid", "is-invalid");
                    document.getElementById("register-status").innerText = "Пароль должен быть минимум 4 символа!";
                    return;
                }


                if (this.pass === this.passR) {
                    document.getElementById("password-text").classList.replace("is-invalid", "is-valid");
                    document.getElementById("password-r-text").classList.replace("is-invalid", "is-valid");
                    sendDataRegistration(this.login, this.pass);
                } else {
                    document.getElementById("password-text").classList.replace("is-valid", "is-invalid");
                    document.getElementById("password-r-text").classList.replace("is-valid", "is-invalid");
                    document.getElementById("register-status").innerText = "Пароли не совпадают!";
                }
            }
        }
    })
    new Vue({
        el: '#app',
        data: { message: '' }
    })
</script>
</body>
</html>