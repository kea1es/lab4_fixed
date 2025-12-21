<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Лаба 4</title>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/2.2.0/jquery.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/vue@2.6.14/dist/vue.min.js"></script>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>

    <link rel="stylesheet" href="styles/body.css"/>
    <link rel="stylesheet" href="styles/header.css"/>
    <link rel="stylesheet" href="styles/user-input.css"/>
    <link rel="stylesheet" href="styles/table.css"/>
    <link rel="stylesheet" href="styles/other.css"/>.
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
            integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
            crossorigin="anonymous"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
            integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
            crossorigin="anonymous"></script>

    <script type="text/javascript" src="scripts/main.js"></script>
</head>
<body>
<div id="custom-alert-container"></div>

<header>
    <div id="info">
        <div id="info-lab-name">
            <h1><strong>Лабораторная работа №4</strong></h1>
        </div>
        <div class="horizontal-container">
            <div id="info-name" class="form-element">
                <span><strong>Студент: </strong>Прокопенко Сергей Петрович</span>
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

<div class="top-content-row" style="display: flex; overflow: auto; margin-bottom: 20px;">
    <div class="controller" style="margin-right: 30px; width: 520px;">
        <div id="user-request">

            <div class="input-group">
                <label class="input-label">Выберите X:</label>
                <div class="input-options x-checkbox-block">
                    <div class="radio-option">
                        <input type="radio" name="coordX" value="-2" v-model="coord_x" id="x-2">
                        <label for="x-2">-2</label>
                    </div>
                    <div class="radio-option">
                        <input type="radio" name="coordX" value="-1.5" v-model="coord_x" id="x-1.5">
                        <label for="x-1.5">-1.5</label>
                    </div>
                    <div class="radio-option">
                        <input type="radio" name="coordX" value="-1" v-model="coord_x" id="x-1">
                        <label for="x-1">-1</label>
                    </div>
                    <div class="radio-option">
                        <input type="radio" name="coordX" value="-0.5" v-model="coord_x" id="x-0.5">
                        <label for="x-0.5">-0.5</label>
                    </div>
                    <div class="radio-option">
                        <input type="radio" name="coordX" value="0" v-model="coord_x" id="x0">
                        <label for="x0">0</label>
                    </div>
                    <div class="radio-option">
                        <input type="radio" name="coordX" value="0.5" v-model="coord_x" id="x0.5">
                        <label for="x0.5">0.5</label>
                    </div>
                    <div class="radio-option">
                        <input type="radio" name="coordX" value="1" v-model="coord_x" id="x1">
                        <label for="x1">1</label>
                    </div>
                    <div class="radio-option">
                        <input type="radio" name="coordX" value="1.5" v-model="coord_x" id="x1.5">
                        <label for="x1.5">1.5</label>
                    </div>
                    <div class="radio-option">
                        <input type="radio" name="coordX" value="2" v-model="coord_x" id="x2">
                        <label for="x2">2</label>
                    </div>
                </div>
            </div>

            <div class="input-group">
                <label class="input-label">Введите Y:</label>
                <input type="text" id="y-value" class="y-value" v-model="coord_y" placeholder="от -5 до 3"/>
            </div>

            <div class="input-group">
                <label class="input-label">Выберите R:</label>
                <div class="input-options r-checkbox-block">
                    <div class="radio-option">
                        <input type="radio" class="rCheckbox" name="coordR" value="0" v-model="coord_r" id="r0">
                        <label for="r0">0</label>
                    </div>
                    <div class="radio-option">
                        <input type="radio" class="rCheckbox" name="coordR" value="0.5" v-model="coord_r" id="r0.5">
                        <label for="r0.5">0.5</label>
                    </div>
                    <div class="radio-option">
                        <input type="radio" class="rCheckbox" name="coordR" value="1" v-model="coord_r" id="r1">
                        <label for="r1">1</label>
                    </div>
                    <div class="radio-option">
                        <input type="radio" class="rCheckbox" name="coordR" value="1.5" v-model="coord_r" id="r1.5">
                        <label for="r1.5">1.5</label>
                    </div>
                    <div class="radio-option">
                        <input type="radio" class="rCheckbox" name="coordR" value="2" v-model="coord_r" id="r2">
                        <label for="r2">2</label>
                    </div>
                </div>
            </div>

            <div class="submit-button-block">
                <button class="action-button-styled submit-button" v-on:click="sdc">Отправить</button>
            </div>
        </div>
    </div>

    <div style="height: 300px; width: 300px; margin: 0 5% 8% 5%; text-align: center;">
        <svg id="image-coordinates" style="height:300px; width:300px">
        </svg>
        <div id="current-r-display" style="text-align: center; margin-top: 5px; font-weight: bold;">
            Текущее R: <span id="current-r-value">не выбрано</span>
        </div>
    </div>

    <div class="results" style="margin-top: 10px; flex-grow: 1; max-width: 40%;">
        <table id="results-table">
            <thead>
            <tr>
                <th><center>X</center></th>
                <th><center>Y</center></th>
                <th><center>R</center></th>
                <th><center>Результат</center></th>
                <th><center>Время отправки</center></th>
                <th><center>Время выполнения</center></th>
            </tr>
            </thead>
            <tbody id="tableBody">
            </tbody>
        </table>
    </div>
</div>


<div class="go-back-wrapper" style="clear: both; width: 100%; text-align: center; margin-top: 30px;">
    <button onclick="exit()" class="action-button" style="width: 150px; height: 40px; border-radius: 10px;">Обратно</button>
</div>

<style>
    .input-group {
        display: flex;
        align-items: center;
        margin-bottom: 20px;
        padding-left: 20px;
    }

    .input-label {
        margin-right: 15px;
        font-size: 18px;
        font-weight: 500;
        min-width: 110px;
        color: #333;
    }

    .x-checkbox-block {
        display: flex;
        flex-wrap: nowrap;
        gap: 8px;
        align-items: center;
        width: 100%;
        max-width: 420px;
        overflow-x: auto;
        padding-bottom: 5px;
    }

    .r-checkbox-block {
        display: flex;
        flex-wrap: nowrap;
        gap: 10px;
        align-items: center;
        width: 100%;
        max-width: 280px;
    }

    .input-options {
        display: flex;
        align-items: center;
    }

    .radio-option {
        display: flex;
        align-items: center;
        gap: 4px;
        white-space: nowrap;
        flex-shrink: 0;
    }

    .input-options input[type="radio"] {
        width: 15px;
        height: 15px;
        cursor: pointer;
        margin: 0;
    }

    .input-options label {
        font-size: 14px;
        cursor: pointer;
        user-select: none;
        margin: 0;
    }

    #y-value {
        width: 130px;
        border: 1px solid #ccc;
        border-radius: 5px;
        color: #333;
        padding: 6px 10px;
        font-size: 15px;
        background-color: white;
    }

    #y-value:focus {
        border-color: #aaa;
        outline: none;
        box-shadow: 0 0 3px rgba(0, 0, 0, 0.1);
    }

    .controller {
        padding-top: 1%;
    }

    .submit-button-block {
        padding-left: 20px;
        margin-top: 20px;
        display: flex;
        align-items: center;
    }

    .submit-button {
        padding: 8px 20px;
        font-size: 16px;
        background-color: #4b61cf;
        color: white;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        transition: background-color 0.2s;
    }

    .submit-button:hover {
        background-color: #3a50b5;
    }

    .clear-button {
        padding: 8px 20px;
        font-size: 16px;
        background-color: #dc3545;
        color: white;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        transition: background-color 0.2s;
    }

    .clear-button:hover {
        background-color: #c82333;
    }

    .x-checkbox-block::-webkit-scrollbar {
        height: 6px;
    }

    .x-checkbox-block::-webkit-scrollbar-track {
        background: #f1f1f1;
        border-radius: 3px;
    }

    .x-checkbox-block::-webkit-scrollbar-thumb {
        background: #888;
        border-radius: 3px;
    }

    .x-checkbox-block::-webkit-scrollbar-thumb:hover {
        background: #555;
    }

    .action-button {
        padding: 8px 20px;
        font-size: 16px;
        background-color: #6c757d;
        color: white;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        transition: background-color 0.2s;
    }

    .action-button:hover {
        background-color: #5a6268;
    }
</style>

<script>
    new Vue({
        el: '#user-request',
        data: {
            coord_r: localStorage.getItem('last_r') || "",
            coord_x: localStorage.getItem('last_x') || "",
            coord_y: localStorage.getItem('last_y') || ""
        },
        methods: {
            sdc: function (event) {
                event.preventDefault();
                event.stopPropagation();

                const selectedXValue = this.coord_x;
                const selectedRValue = this.coord_r;

                const validationError = validateFormData(selectedXValue, this.coord_y, selectedRValue);
                if (validationError) {
                    showCustomAlert(validationError, 'error');
                    return;
                }

                const x = parseFloat(selectedXValue);
                const y = parseFloat(parseYValue(this.coord_y));
                const r = parseFloat(selectedRValue);

                console.log(`Отправка формы: X=${x}, Y=${y}, R=${r}`);


                localStorage.setItem('last_r', r.toString());
                localStorage.setItem('last_x', x.toString());
                localStorage.setItem('last_y', y.toString());

                if (currentR !== r) {
                    setR(r);
                } else {
                    printPaint();
                }

                send(x, y, r);
            }
        },
        watch: {
            coord_r: function(newR) {
                if (newR && newR !== "") {
                    const rNum = parseFloat(newR);
                    if (!isNaN(rNum) && (currentR === null || Math.abs(rNum - currentR) > 0.001)) {
                        setR(rNum);
                        localStorage.setItem('last_r', newR);
                    }
                }
            },
            coord_x: function(newX) {
                if (newX && newX !== "") {
                    localStorage.setItem('last_x', newX);
                }
            },
            coord_y: function(newY) {
                if (newY && newY !== "") {
                    localStorage.setItem('last_y', newY);
                }
            }
        }
    });
</script>
</body>
</html>