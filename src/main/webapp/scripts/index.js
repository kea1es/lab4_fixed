function sendDatas(login, password) {
    password = sha256(password);

    document.getElementById("email-text").classList.remove("is-invalid");
    document.getElementById("password-text").classList.remove("is-invalid");
    const alertBox = document.getElementById("alert");
    alertBox.classList.remove("show");

    $.ajax({
        method: 'POST',
        url: "/Lab4/api/auth/login",
        data: {login: login, password: password},
        success: function (data, status, jqXHR) {
            location.href = '/Lab4/main';
        },
        error: function (jqXHR) {
            const alertBox = document.getElementById("alert");
            const statusLabel = document.getElementById("login-status");

            let state = jqXHR.getResponseHeader("StatusOfLogIn");

            if (jqXHR.status === 401) {
                if (state == 1) {
                    document.getElementById("email-text").classList.add("is-invalid");
                    statusLabel.innerText = "Пользователь не найден";
                } else if (state == 2) {
                    document.getElementById("password-text").classList.add("is-invalid");
                    statusLabel.innerText = "Неверный пароль";
                } else {
                    statusLabel.innerText = "Ошибка авторизации";
                }
            } else {
                statusLabel.innerText = "Ошибка сервера: " + jqXHR.status;
            }

            alertBox.classList.add("show");
        }
    });
}

function sendDataRegistration(login, password) {
    password = sha256(password);

    // Очищаем старые стили
    document.getElementById("login-text").classList.remove("is-invalid");
    const statusLabel = document.getElementById("register-status");

    $.ajax({
        method: 'POST',
        url: "/Lab4/api/auth/register",
        data: {login: login, password: password},
        success: function (data, status, jqXHR) {
            statusLabel.innerText = "Успешно! Переход на главную...";
            statusLabel.style.color = "green";
            setTimeout(() => { location.href = '/Lab4/'; }, 1000);
        },
        error: function (jqXHR) {
            let state = jqXHR.getResponseHeader("StatusOfRegistration");

            if (state == 1 || jqXHR.status === 401 || jqXHR.status === 409) {
                document.getElementById("login-text").classList.add("is-invalid");
                statusLabel.innerText = "Этот логин уже занят!";
                statusLabel.style.color = "red";
            } else {
                statusLabel.innerText = "Ошибка регистрации: " + jqXHR.status;
                statusLabel.style.color = "red";
            }
        }
    });
}

var sha256 = function sha256(ascii) {
    function rightRotate(value, amount) {
        return (value >>> amount) | (value << (32 - amount));
    };

    var mathPow = Math.pow;
    var maxWord = mathPow(2, 32);
    var lengthProperty = 'length'
    var i, j;
    var result = ''

    var words = [];
    var asciiBitLength = ascii[lengthProperty] * 8;

    var hash = sha256.h = sha256.h || [];
    var k = sha256.k = sha256.k || [];
    var primeCounter = k[lengthProperty];

    var isComposite = {};
    for (var candidate = 2; primeCounter < 64; candidate++) {
        if (!isComposite[candidate]) {
            for (i = 0; i < 313; i += candidate) {
                isComposite[i] = candidate;
            }
            hash[primeCounter] = (mathPow(candidate, .5) * maxWord) | 0;
            k[primeCounter++] = (mathPow(candidate, 1 / 3) * maxWord) | 0;
        }
    }

    ascii += '\x80'
    while (ascii[lengthProperty] % 64 - 56) ascii += '\x00'
    for (i = 0; i < ascii[lengthProperty]; i++) {
        j = ascii.charCodeAt(i);
        if (j >> 8) return;
        words[i >> 2] |= j << ((3 - i) % 4) * 8;
    }
    words[words[lengthProperty]] = ((asciiBitLength / maxWord) | 0);
    words[words[lengthProperty]] = (asciiBitLength)

    for (j = 0; j < words[lengthProperty];) {
        var w = words.slice(j, j += 16);
        var oldHash = hash;
        hash = hash.slice(0, 8);

        for (i = 0; i < 64; i++) {
            var i2 = i + j;
            var w15 = w[i - 15], w2 = w[i - 2];

            var a = hash[0], e = hash[4];
            var temp1 = hash[7]
                + (rightRotate(e, 6) ^ rightRotate(e, 11) ^ rightRotate(e, 25))
                + ((e & hash[5]) ^ ((~e) & hash[6]))
                + k[i]
                + (w[i] = (i < 16) ? w[i] : (
                        w[i - 16]
                        + (rightRotate(w15, 7) ^ rightRotate(w15, 18) ^ (w15 >>> 3))
                        + w[i - 7]
                        + (rightRotate(w2, 17) ^ rightRotate(w2, 19) ^ (w2 >>> 10))
                    ) | 0
                );
            var temp2 = (rightRotate(a, 2) ^ rightRotate(a, 13) ^ rightRotate(a, 22))
                + ((a & hash[1]) ^ (a & hash[2]) ^ (hash[1] & hash[2]));

            hash = [(temp1 + temp2) | 0].concat(hash);
            hash[4] = (hash[4] + temp1) | 0;
        }

        for (i = 0; i < 8; i++) {
            hash[i] = (hash[i] + oldHash[i]) | 0;
        }
    }

    for (i = 0; i < 8; i++) {
        for (j = 3; j + 1; j--) {
            var b = (hash[i] >> (j * 8)) & 255;
            result += ((b < 16) ? 0 : '') + b.toString(16);
        }
    }
    return result;
};