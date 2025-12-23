let currentR = null;
let code = 0;
let allPoints = {}; // Единственное хранилище: { "1": [...], "2": [...], ... }

function getRKey(r) {
    const numR = parseFloat(r);
    if (isNaN(numR)) return r.toString();
    return numR.toString();
}

function showCustomAlert(message, type = 'error') {
    const container = document.getElementById('custom-alert-container');
    if (!container) {
        const body = document.body;
        const newContainer = document.createElement('div');
        newContainer.id = 'custom-alert-container';
        newContainer.style.cssText = `
            position: fixed;
            top: 20px;
            right: 20px;
            z-index: 9999;
            max-width: 400px;
        `;
        body.appendChild(newContainer);
    }

    const currentContainer = document.getElementById('custom-alert-container');
    const alertId = 'alert-' + Date.now();
    const alertDiv = document.createElement('div');

    const bgColor = type === 'error' ? '#f8d7da' : '#d4edda';
    const textColor = type === 'error' ? '#721c24' : '#155724';
    const borderColor = type === 'error' ? '#f5c6cb' : '#c3e6cb';

    alertDiv.style.cssText = `
        background-color: ${bgColor};
        color: ${textColor};
        border: 1px solid ${borderColor};
        border-radius: 5px;
        padding: 15px;
        margin-bottom: 10px;
        position: relative;
        opacity: 0;
        transform: translateX(100%);
        transition: opacity 0.3s, transform 0.3s;
    `;

    alertDiv.id = alertId;
    alertDiv.innerHTML = `
        ${message}
        <button style="
            position: absolute;
            top: 5px;
            right: 5px;
            background: none;
            border: none;
            font-size: 20px;
            cursor: pointer;
            color: ${textColor};
        " onclick="closeAlert('${alertId}')">&times;</button>
    `;

    currentContainer.appendChild(alertDiv);

    setTimeout(() => {
        alertDiv.style.opacity = '1';
        alertDiv.style.transform = 'translateX(0)';
    }, 10);

    setTimeout(() => {
        closeAlert(alertId);
    }, 5000);
}

function closeAlert(alertId) {
    const alert = document.getElementById(alertId);
    if (alert) {
        alert.style.opacity = '0';
        alert.style.transform = 'translateX(100%)';
        setTimeout(() => {
            if (alert.parentNode) {
                alert.parentNode.removeChild(alert);
            }
        }, 300);
    }
}

function parseYValue(y) {
    if (typeof y !== 'string') return y;
    return y.replace(',', '.');
}

function validateFormData(x, y, r) {
    if (x === undefined || x === '' || x === null) {
        return "Пожалуйста, выберите значение X";
    }

    if (r === undefined || r === '' || r === null) {
        return "Пожалуйста, выберите значение R";
    }

    if (y === undefined || y === '' || y === null) {
        return "Пожалуйста, введите значение Y";
    }

    const numX = parseFloat(x);
    const rawYString = String(y).trim();
    const cleanYString = parseYValue(rawYString);
    const numY = parseFloat(cleanYString);
    const numR = parseFloat(r);

    if (!/^-?\d*([,\.]\d*)?$/.test(rawYString)) {
        return "Значение Y содержит недопустимые символы. Разрешены только числа, минус и запятая/точка.";
    }

    if (isNaN(numX)) {
        return "Значение X должно быть числом";
    }

    if (isNaN(numY)) {
        return "Некорректный формат числа Y. Проверьте, что введено корректное число.";
    }

    if (isNaN(numR)) {
        return "Значение R должно быть числом";
    }

    if (numX < -3 || numX > 5) {
        return "Значение X должно быть в диапазоне [-3, 5]";
    }

    if (numY < -5 || numY > 3) {
        return "Значение Y должно быть в диапазоне [-5, 3]";
    }

    if (numR < -3 || numR > 5) {
        return "Значение R должно быть в диапазоне [-3, 5]";
    }

    return null;
}

function validateGraphData(x, y, r) {
    if (r === undefined || r === '' || r === null || isNaN(r)) {
        return "Выберите значение R для работы с графиком";
    }

    if (x === undefined || x === '' || x === null) {
        return "Ошибка: X не может быть пустым";
    }

    if (y === undefined || y === '' || y === null) {
        return "Ошибка: Y не может быть пустым";
    }

    const numX = parseFloat(x);
    const numY = parseFloat(y);
    const numR = parseFloat(r);

    if (isNaN(numX)) {
        return "Значение X должно быть числом";
    }

    if (isNaN(numY)) {
        return "Значение Y должно быть числом";
    }

    if (isNaN(numR)) {
        return "Значение R должно быть числом";
    }

    return null;
}

function updateClock() {
    let now = new Date();
    let hours = now.getHours().toString().padStart(2, '0');
    let minutes = now.getMinutes().toString().padStart(2, '0');
    let seconds = now.getSeconds().toString().padStart(2, '0');
    let time = hours + ':' + minutes + ':' + seconds;
    let variant = "№843501";
    let date = [
        now.getDate().toString().padStart(2, '0'),
        (now.getMonth() + 1).toString().padStart(2, '0'),
        now.getFullYear()
    ].join('.');

    const timeElement = document.getElementById('time');
    if (timeElement) {
        timeElement.innerHTML = [variant, time, date].join(' | ');
    }

    setTimeout(updateClock, 1000);
}

function setR(newR) {
    if (currentR !== null && Math.abs(parseFloat(newR) - parseFloat(currentR)) < 0.001) {
        return;
    }

    const oldR = currentR;
    currentR = newR;

    const rValueElement = document.getElementById('current-r-value');
    if (rValueElement) {
        if (newR === null || isNaN(newR)) {
            rValueElement.textContent = 'не выбрано';
        } else {
            rValueElement.textContent = newR;
        }
    }

    const formVue = document.querySelector('#user-request').__vue__;
    if (formVue && newR !== null && !isNaN(newR)) {
        formVue.coord_r = newR.toString();
    }

    if (newR !== null && !isNaN(newR)) {
        localStorage.setItem('last_r', newR.toString());
    }

    if (oldR !== newR) {
        printPaint();
    }
}

function savePoint(pointData) {
    if (!pointData || pointData.length < 5) {
        console.error("Некорректные данные точки:", pointData);
        return;
    }

    const r = pointData[2];
    const rKey = getRKey(r);

    console.log(`Сохранение новой точки: X=${pointData[0]}, Y=${pointData[1]}, R=${r}, ключ=${rKey}, время=${pointData[4]}`);

    if (!allPoints[rKey]) {
        allPoints[rKey] = [];
    }

    // Всегда добавляем как новую точку (даже если координаты одинаковые)
    allPoints[rKey].push(pointData);
}

function clearAllPoints() {
    allPoints = {};
    writeTable();

    if (currentR !== null && !isNaN(currentR)) {
        printPaint();
    }
}

function getAllPointsForTable() {
    const result = [];
    for (const rKey in allPoints) {
        if (allPoints.hasOwnProperty(rKey)) {
            result.push(...allPoints[rKey]);
        }
    }

    // Сортировка по времени (новые сверху)
    result.sort((a, b) => {
        const timeA = a[4] || '';
        const timeB = b[4] || '';
        return timeB.localeCompare(timeA);
    });

    return result;
}

function writeTable() {
    const pointsForTable = getAllPointsForTable();
    let inHTML = "";

    if (pointsForTable.length === 0) {
        inHTML = `<tr><td colspan="6" style="text-align: center;">Нет данных</td></tr>`;
    } else {
        for (let i = 0; i < pointsForTable.length; i++) {
            inHTML += "<tr>";
            for (let j = 0; j < 6; j++) {
                inHTML += "<td>" + pointsForTable[i][j] + "</td>";
            }
            inHTML += "</tr>";
        }
    }

    document.getElementById("tableBody").innerHTML = inHTML;
}

const clickAnswer = function (event) {
    if (currentR === null || isNaN(currentR)) {
        showCustomAlert("Сначала выберите значение R для работы с графиком", 'error');
        return;
    }

    const svg = document.getElementById("image-coordinates");
    const rect = svg.getBoundingClientRect();

    // Получаем координаты клика относительно SVG
    const clickX = event.clientX - rect.left;
    const clickY = event.clientY - rect.top;

    // Нормализуем координаты к системе SVG (0-300)
    const xCord = (clickX / rect.width) * 300;
    const yCord = (clickY / rect.height) * 300;

    // Преобразуем в математические координаты (центр в 150,150)
    const xGraph = xCord - 150;
    const yGraph = -(yCord - 150); // Ось Y инвертирована в SVG

    // Преобразуем в реальные значения с учетом R
    let x, y;
    const r = currentR;

    if (Math.abs(r) < 0.0001) { // r ≈ 0
        x = (xGraph / 120);
        y = (yGraph / 120);
    } else {
        x = (xGraph / 120) * r;
        y = (yGraph / 120) * r;
    }

    x = parseFloat(x.toFixed(4));
    y = parseFloat(y.toFixed(4));

    console.log("Клик по графику: X=" + x + ", Y=" + y + ", R=" + r);

    const validationError = validateGraphData(x, y, r);
    if (validationError) {
        showCustomAlert(validationError, 'error');
        return;
    }

    send(x, y, r);
};

function send(x, y, r) {
    console.log("Отправка точки на сервер:", {x, y, r});

    $.ajax({
        url: '/Lab4/api/shots',
        method: 'POST',
        data: {coord_x: x, coord_y: y, coord_r: r},
        success: function (data, textStatus, request) {
            console.log("Успешный ответ от сервера:", data);
            processServerResponse(data);
        },
        error: function (xhr) {
            console.error("Ошибка при отправке:", xhr);
            showCustomAlert("Ошибка при отправке: " + xhr.status, 'error');
        }
    });
}

function processServerResponse(data) {
    console.log("Обработка ответа от сервера:", data);

    if (data && data.trim().length > 0) {
        const lines = data.trim().split("\n");

        if (lines.length === 1) {
            // Новая точка - всегда добавляем как новую
            const pointData = lines[0].split(" ");
            if (pointData.length >= 5) {
                savePoint(pointData);
                writeTable();

                const pointR = parseFloat(pointData[2]);
                if (currentR !== null && Math.abs(pointR - currentR) < 0.001) {
                    printPaint();
                }
            }
        } else {
            // Загрузка всех точек с сервера
            allPoints = {};

            lines.forEach(line => {
                if (line.trim().length > 0) {
                    const pointData = line.split(" ");
                    if (pointData.length >= 5) {
                        savePoint(pointData);
                    }
                }
            });

            writeTable();

            if (currentR !== null && !isNaN(currentR)) {
                printPaint();
            }
        }
    }
}

function printPaint() {
    let inHTML = `<rect width="300" height="300" fill="rgb(255,255,255)" stroke-width="0" stroke="rgb(0,0,0)"></rect>
    
    <defs>
        <pattern id="grid" width="30" height="30" patternUnits="userSpaceOnUse">
            <path d="M 30 0 L 0 0 0 30" fill="none" stroke="rgba(147, 112, 219, 1)" stroke-width="0.5" stroke-opacity="0.3"/>
        </pattern>
        <marker id="arrowhead-x" markerWidth="10" markerHeight="7" refX="10" refY="3.5" orient="auto">
            <polygon points="0 0, 10 3.5, 0 7" fill="black"/>
        </marker>
        <marker id="arrowhead-y" markerWidth="7" markerHeight="10" refX="3.5" refY="10" orient="auto">
            <polygon points="0 0, 7 10, 0 10" fill="black"/>
        </marker>
    </defs>
    <rect width="320" height="320" fill="url(#grid)" stroke-width="0"></rect>
    
    <rect x="150" y="30" width="120" height="120" fill="#9370DB" fill-opacity="0.3" stroke-width="1" stroke="rgb(50,50,50)"></rect>
    <path d="M 150 150 L 150 210 A 60 60 0 0 1 90 150 Z" fill="#9370DB" fill-opacity="0.3" stroke-width="1" stroke="rgb(50,50,50)"></path>
    <polyline points="150,150 270,150 150,210 150,150" fill="#9370DB" fill-opacity="0.3" stroke-width="1" stroke="rgb(50,50,50)"></polyline>
   
    <line x1="150" y1="300" x2="150" y2="0" stroke-width="1.5" stroke="rgb(0,0,0)" 
          marker-end="url(#arrowhead-y)"></line>
    
    <line x1="0" y1="150" x2="300" y2="150" stroke-width="1.5" stroke="rgb(0,0,0)" 
          marker-end="url(#arrowhead-x)"></line>
    
    <line x1="145" y1="30" x2="155" y2="30" stroke-width="1" stroke="rgb(0,0,0)"></line>
    <line x1="270" y1="145" x2="270" y2="155" stroke-width="1" stroke="rgb(0,0,0)"></line>
    <line x1="30" y1="145" x2="30" y2="155" stroke-width="1" stroke="rgb(0,0,0)"></line>
    <line x1="145" y1="270" x2="155" y2="270" stroke-width="1" stroke="rgb(0,0,0)"></line>
    <line x1="145" y1="90" x2="155" y2="90" stroke-width="1" stroke="rgb(0,0,0)"></line>
    <line x1="210" y1="145" x2="210" y2="155" stroke-width="1" stroke="rgb(0,0,0)"></line>
    <line x1="90" y1="145" x2="90" y2="155" stroke-width="1" stroke="rgb(0,0,0)"></line>
    <line x1="145" y1="210" x2="155" y2="210" stroke-width="1" stroke="rgb(0,0,0)"></line>
    
    <text x="290" y="140" font-family="Arial" font-size="12" fill="rgb(0,0,0)">X</text>
    <text x="160" y="10" font-family="Arial" font-size="12" fill="rgb(0,0,0)">Y</text>`;

    inHTML += `<text x="270" y="170" font-family="Arial" font-size="10" text-anchor="middle" fill="rgb(0,0,0)">`;
    if (currentR !== null && !isNaN(currentR)) {
        inHTML += `${currentR}`;
    } else {
        inHTML += `R`;
    }
    inHTML += `</text>`;

    inHTML += `<text x="210" y="170" font-family="Arial" font-size="10" text-anchor="middle" fill="rgb(0,0,0)">`;
    if (currentR !== null && !isNaN(currentR)) {
        inHTML += `${(currentR/2).toFixed(1)}`;
    } else {
        inHTML += `R/2`;
    }
    inHTML += `</text>`;

    inHTML += `<text x="90" y="170" font-family="Arial" font-size="10" text-anchor="middle" fill="rgb(0,0,0)">`;
    if (currentR !== null && !isNaN(currentR)) {
        inHTML += `-${(currentR/2).toFixed(1)}`;
    } else {
        inHTML += `-R/2`;
    }
    inHTML += `</text>`;

    inHTML += `<text x="30" y="170" font-family="Arial" font-size="10" text-anchor="middle" fill="rgb(0,0,0)">`;
    if (currentR !== null && !isNaN(currentR)) {
        inHTML += `-${currentR}`;
    } else {
        inHTML += `-R`;
    }
    inHTML += `</text>`;

    inHTML += `<text x="150" y="170" font-family="Arial" font-size="10" text-anchor="middle" fill="rgb(0,0,0)">0</text>`;

    inHTML += `<text x="135" y="30" font-family="Arial" font-size="10" text-anchor="end" fill="rgb(0,0,0)">`;
    if (currentR !== null && !isNaN(currentR)) {
        inHTML += `${currentR}`;
    } else {
        inHTML += `R`;
    }
    inHTML += `</text>`;

    inHTML += `<text x="135" y="90" font-family="Arial" font-size="10" text-anchor="end" fill="rgb(0,0,0)">`;
    if (currentR !== null && !isNaN(currentR)) {
        inHTML += `${(currentR/2).toFixed(1)}`;
    } else {
        inHTML += `R/2`;
    }
    inHTML += `</text>`;

    inHTML += `<text x="135" y="210" font-family="Arial" font-size="10" text-anchor="end" fill="rgb(0,0,0)">`;
    if (currentR !== null && !isNaN(currentR)) {
        inHTML += `-${(currentR/2).toFixed(1)}`;
    } else {
        inHTML += `-R/2`;
    }
    inHTML += `</text>`;

    inHTML += `<text x="135" y="270" font-family="Arial" font-size="10" text-anchor="end" fill="rgb(0,0,0)">`;
    if (currentR !== null && !isNaN(currentR)) {
        inHTML += `-${currentR}`;
    } else {
        inHTML += `-R`;
    }
    inHTML += `</text>`;

    inHTML += `<text x="135" y="150" font-family="Arial" font-size="10" text-anchor="end" fill="rgb(0,0,0)">0</text>`;

    if (currentR !== null && !isNaN(currentR)) {
        const rKey = getRKey(currentR);
        const pointsForCurrentR = allPoints[rKey] || [];

        console.log(`Отрисовка графика для R=${currentR}, ключ=${rKey}, точек: ${pointsForCurrentR.length}`);

        for (let i = 0; i < pointsForCurrentR.length; i++) {
            let x = parseFloat(pointsForCurrentR[i][0]);
            let y = parseFloat(pointsForCurrentR[i][1]);
            let r = currentR;

            let cx, cy;
            if (Math.abs(r) < 0.0001) { // r ≈ 0
                cx = 150 + 120 * x;
                cy = 150 - 120 * y; // Ось Y инвертирована
            } else {
                cx = 150 + 120 * (x / r);
                cy = 150 - 120 * (y / r); // Ось Y инвертирована
            }

            // Ограничиваем координаты внутри SVG
            cx = Math.max(0, Math.min(300, cx));
            cy = Math.max(0, Math.min(300, cy));

            inHTML += `<circle cx="${cx}" cy="${cy}" r="5" `;

            if (pointsForCurrentR[i][3] === "false") {
                inHTML += `fill="#f23d3d" `;
            } else {
                inHTML += `fill="#5ee667" `;
            }

            inHTML += `stroke-width="1" stroke="rgb(0,0,0)" style="pointer-events: none;"/>`;
        }
    }

    document.getElementById("image-coordinates").innerHTML = inHTML;
}

function exit() {
    $.ajax({
        url: '/Lab4/api/auth/exit',
        method: 'POST',
        success: function () {
            location.href = '/Lab4/';
        }
    });
}

function clear() {
    $.ajax({
        url: '/Lab4/api/shots',
        method: 'DELETE',
        success: function (data) {
            clearAllPoints();
            showCustomAlert("Данные очищены", 'success');
        },
        error: function (xhr) {
            showCustomAlert("Ошибка очистки", 'error');
        }
    });
}

function loadAllPointsFromServer() {
    $.ajax({
        url: '/Lab4/api/shots',
        method: 'GET',
        success: function (data, textStatus, request) {
            if (data && data.trim().length > 0) {
                processServerResponse(data);
                console.log("Загружены все точки с сервера");
            }
        },
        error: function (xhr) {
            console.error("Ошибка загрузки точек:", xhr);
        }
    });
}

document.addEventListener('DOMContentLoaded', function () {
    document.getElementById('image-coordinates').addEventListener('click', clickAnswer);

    updateClock();

    // Восстанавливаем сохраненные значения
    const savedR = localStorage.getItem('last_r');
    const savedX = localStorage.getItem('last_x');
    const savedY = localStorage.getItem('last_y');

    if (savedR && !isNaN(parseFloat(savedR))) {
        setTimeout(() => {
            setR(parseFloat(savedR));

            const rRadio = document.querySelector(`.rCheckbox[value="${savedR}"]`);
            if (rRadio) {
                rRadio.checked = true;
            }
        }, 100);
    }

    if (savedX) {
        setTimeout(() => {
            const xRadio = document.querySelector(`input[name="coordX"][value="${savedX}"]`);
            if (xRadio) {
                xRadio.checked = true;
            }
        }, 100);
    }

    // Загружаем точки с сервера
    loadAllPointsFromServer();

    // Навешиваем обработчики на радио-кнопки R
    const rRadios = document.querySelectorAll('.rCheckbox');
    rRadios.forEach(radio => {
        radio.addEventListener('change', function(e) {
            if (e.target.checked) {
                const rValue = parseFloat(e.target.value);
                setR(rValue);
            }
        });
    });
});