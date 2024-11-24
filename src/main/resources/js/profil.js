//меню настройки пользователя
const profileBtn = document.getElementById('profileBtn');
const profileMenu = document.getElementById('profileMenu');
const openProfileSettings = document.getElementById('openProfileSettings');
const profileSettings = document.getElementById('profileSettings')


const optionsMenu = document.getElementById('optionsMenu')
const openОptionsSettings = document.getElementById('openОptionsSettings')
const optionsSettings = document.getElementById('optionsSettings')


// Добавляем обработчик события на кнопку profileBtn для отображения меню
profileBtn.addEventListener('click', function(event) {
    event.preventDefault(); // Предотвращаем переход по ссылке

    if (profileMenu.style.display === 'none' || profileMenu.style.display === '' ||
        optionsMenu.style.display === 'none' || optionsMenu.style.display ===  '') {
        profileMenu.style.display = 'block';
        optionsMenu.style.display = 'block';  // Показываем меню профиля

        if (profileSettings.style.display === 'block' || optionsSettings.style.display === 'block') {
            profileSettings.style.display = 'none';                 // Если меню настроек открыто, закрываем его
            optionsSettings.style.display = 'none';
        }

    } else {
        profileMenu.style.display = 'none';                          // Скрываем меню профиля
        optionsMenu.style.display = 'none';
        profileSettings.style.display = 'none';                     // Скрываем меню настроек, если оно было открыто
    }
});


// Добавляем обработчик на кнопку "Настройки профиля"
 openProfileSettings.addEventListener('click', function() {
    profileMenu.style.display = 'none'; // Скрываем меню профиля
    profileSettings.style.display = 'block'; // Показываем настройки профиля
});


// Добавляем обработчик на кнопку "Опции"
openОptionsSettings.addEventListener('click', function() {
    optionsMenu.style.display = 'none'; // Скрываем меню
    optionsSettings.style.display = 'block'; // Показываем опции
});


function changProfile(event) {
    event.preventDefault(); // Останавливаем стандартную отправку формы
    console.log("Submitting form..."); // Лог для проверки

    // Собираем данные формы
    const formData = {
        firstName: document.getElementById('firstName').value,
        lastName: document.getElementById('lastName').value,
    };


    // Отправляем данные на сервер с помощью POST-запроса
    fetch('/profile', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(formData) // преобразуем данные в формат JSON
    })
        .then(response => response.json()) // конвертируем ответ в JSON
        .then(data => {
            if (data.success) {
                alert('Profile updated successfully');
                window.location.href = "http://localhost:8080/chat"; // Перенаправляем на страницу чата
            } else {
                alert('Error: ' + data.message);
            }
        })
        .catch(error => {
            console.error('There was a problem with the request:', error);
        });
}
