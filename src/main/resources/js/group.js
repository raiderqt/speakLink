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
    const groupName = groupNameInput.value.trim();

    if (!groupName) {
        alert('Введите имя группы!');
        return;
    }

    const data = { groupName };

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