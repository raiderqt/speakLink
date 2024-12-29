// Получаем элементы input и div, которые нужно переключать

function toggleDivs() {
    const contactsList = document.getElementById('contactsList');
    const findUser = document.getElementById('findUser');
    contactsList.style.display = 'none';
    findUser.style.display = 'block';
    friendList();
    let formData = new FormData(document.getElementById('change').form);

}

function toggleBack() {
    const contactsList = document.getElementById('contactsList');
    const findUser = document.getElementById('findUser');

    contactsList.style.display = 'block';
    findUser.style.display = 'none';
}

function friendList() {
    // Пример ввода для имени пользователя
    const firstName = prompt("Введите Имя:");

    if (!firstName) {
        alert("Требуется указать имя: ");
        return;
    }

    // Подготовка данных для запроса
    const payload = JSON.stringify({ firstName: firstName });

    // Запрос на серверный эндпоинт
    fetch('/chat/find', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: payload
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            // Отобразить список пользователей в HTML
            displayUserList(data);
        })
        .catch(error => {
            console.error('Произошла ошибка с операцией fetch:', error);
        });
}

function displayUserList(users) {
    const userListContainer = document.getElementById('userListContainer');
    userListContainer.innerHTML = ''; // Очистить предыдущий список, если есть

    if (users.length === 0) {
        userListContainer.innerHTML = '<p>Пользователи не найдены</p>';
        return;
    }

    // Для каждого пользователя создаем элемент div
    users.forEach(user => {
        const userDiv = document.createElement('div');
        userDiv.classList.add('chat-item', 'd-flex', 'pl-3', 'pr-0', 'pt-3', 'pb-3');

        userDiv.innerHTML = `
            <div class="d-flex pl-0">
                <p>${user.firstName} ${user.lastName}</p>
                <div >
                <button onclick="sendUserId(${user.id})">Отправить ID</button>
                </div >
            </div>
        `;

        userListContainer.appendChild(userDiv);
    });
}

function sendUserId(userId) {
    // Подготовка данных для запроса
    const payload = JSON.stringify({ id: userId });

    // Запрос на серверный эндпоинт для отправки userId
    fetch('/chat/addFriend', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: payload
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            // Обработка успешного ответа от сервера
            alert('ID пользователя успешно отправлен');
        })
        .catch(error => {
            console.error('Произошла ошибка с отправкой userId:', error);
        });
}

