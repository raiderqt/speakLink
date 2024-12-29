const createGroupBtn = document.getElementById('group');
const popup = document.getElementById('popup');
const overlay = document.getElementById('overlay');
const cancelBtn = document.getElementById('cancelBtn');
const submitBtn = document.getElementById('submitBtn');
const groupNameInput = document.getElementById('groupName');

// Открытие окна
createGroupBtn.addEventListener('click', () => {
    popup.style.display = 'block';
    overlay.style.display = 'block';
});

// Закрытие окна
cancelBtn.addEventListener('click', () => {
    popup.style.display = 'none';
    overlay.style.display = 'none';
});

overlay.addEventListener('click', () => {
    popup.style.display = 'none';
    overlay.style.display = 'none';
});

// Отправка данных
submitBtn.addEventListener('click', () => {
    const name = groupNameInput.value.trim();

    if (!name) {
        alert('Введите имя группы!');
        return;
    }

    const data = {name};

    // Отправка данных на сервер через fetch
    fetch('/chat/group', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(data),
    })
        .then(response => {
            if (response.ok) {
                alert('Группа успешно создана!');
            } else {
                alert('Ошибка при создании группы');
            }
            return response.json();
        })
        .then(data => {
            console.log('Ответ сервера:', data);
        })
        .catch(error => {
            console.error('Ошибка:', error);
        });

    // Закрыть окно после отправки
    popup.style.display = 'none';
    overlay.style.display = 'none';
});

const openPopupBtn = document.getElementById('userAddGroup_openPopup');
const userAddGroup_popup = document.getElementById('userAddGroup_popup');
const userAddGroup_overlay = document.getElementById('userAddGroup_overlay');
const userAddGroup_cancelBtn = document.getElementById('userAddGroup_cancelBtn');
const addBtn = document.getElementById('userAddGroup_addBtn');
const userList = document.getElementById('userList');

// Открытие окна и загрузка пользователей
openPopupBtn.addEventListener('click', () => {
    userAddGroup_popup.style.display = 'block';
    userAddGroup_overlay.style.display = 'block';

    // Отправка запроса на сервер для получения списка пользователей
    fetch('/chat/users')
        .then(response => response.json())
        .then(data => {
            // Очистка списка
            userList.innerHTML = '';

            // Добавление пользователей в список
            data.forEach(user => {
                const listItem = document.createElement('li');
                console.log('user id =' + user.firstName + user.lastName + user.id);
                listItem.innerHTML = `
                    <label>
                        <input type="checkbox" value="${user.id}">
                        <span>${user.id} ${user.firstName} ${user.lastName} </span>
                    </label>
                `;
                userList.appendChild(listItem);
            });
        })
        .catch(error => {
            console.error('Ошибка при загрузке списка пользователей:', error);
        });
});

// Закрытие окна
userAddGroup_cancelBtn.addEventListener('click', () => {
    userAddGroup_popup.style.display = 'none';
    userAddGroup_overlay.style.display = 'none';
});

userAddGroup_overlay.addEventListener('click', () => {
    userAddGroup_popup.style.display = 'none';
    userAddGroup_overlay.style.display = 'none';
});

let currentRoomId = null;

// При клике по элементу списка чатов
$('.chat-list').on('click', '.chat-item', function (e) {
    $('.chat-list div.active').removeClass('active'); // Убираем класс active
    $(this).addClass('active'); // Добавляем класс active к выбранному элементу
    currentRoomId = $(this).data('room-id'); // Сохраняем ID комнаты в глобальной переменной
    changeRoom(currentRoomId);
});

// В обработчике addBtn
addBtn.addEventListener('click', () => {
    const selectedUsers = [];
    const checkboxes = document.querySelectorAll('.user-list input[type="checkbox"]:checked');

    checkboxes.forEach(checkbox => {
        selectedUsers.push(Number(checkbox.value)); // Собираем значения ID
    });

    const roomId = currentRoomId; // Используем глобальную переменную

    console.log(selectedUsers);
    console.log(JSON.stringify({ roomId: roomId, users: selectedUsers }));

    // Отправка данных на сервер
    fetch('/chat/usersList', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({ roomId: roomId, users: selectedUsers }), // Передаем ID комнаты и массив ID пользователей
    })
        .then(response => {
            if (response.ok) {
                alert('Пользователи успешно добавлены!');
            } else {
                alert('Ошибка при добавлении пользователей');
            }
            return response.json();
        })
        .then(data => {
            console.log('Ответ сервера:', data);
        })
        .catch(error => {
            console.error('Ошибка:', error);
        });

    // Закрыть окно
    userAddGroup_popup.style.display = 'none';
    userAddGroup_overlay.style.display = 'none';
});