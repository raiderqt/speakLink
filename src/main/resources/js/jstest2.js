let stompClient;
(function ($) {
    "use strict";
    $('.scrollable-chat-panel').perfectScrollbar();
    var position = $(".chat-search").last().position().top;
    $('.scrollable-chat-panel').scrollTop(position);
    $('.scrollable-chat-panel').perfectScrollbar('update');
    $('.pagination-scrool').perfectScrollbar();

    $('.chat-upload-trigger').on('click', function (e) {
        $(this).parent().find('.chat-upload').toggleClass("active");
    });
    $('.user-detail-trigger').on('click', function (e) {
        $(this).closest('.chat').find('.chat-user-detail').toggleClass("active");
    });
    $('.user-undetail-trigger').on('click', function (e) {
        $(this).closest('.chat').find('.chat-user-detail').toggleClass("active");
    });
    $('.chat-list').on('click', '.chat-item', function (e) {
        $('.chat-list div.active').removeClass('active'); // Убираем класс active с текущего активного элемента
        $(this).addClass('active'); // Добавляем класс active к выбранному элементу
        changeRoom($(this).data('room-id'));
    });


    {
        let socket = new SockJS('/ws');
        stompClient = Stomp.over(socket);
        stompClient.connect({}, function (frame) {
            console.log('Connected: ' + frame);
            $.ajax({
                url: '/chat/rooms',
                type: 'POST',
                dataType: 'json',
                success: function (data) {
                    console.log(data); //for debug
                    initRoom(data);
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log(textStatus, errorThrown); //for debug
                }
            });
            $.ajax({
                url: '/chat/user',
                type: 'POST',
                dataType: 'json',
                success: function (data) {
                    console.log(data); //for debug
                    user = data;
                },
                error: function (jqXHR, textStatus, errorThrown) {
                    console.log(textStatus, errorThrown); //for debug
                }
            });
        });
        $(document).on('keydown', function (event) {
            //if (event.which === 13 && $(event.target).is('#myButton')) {
            if (event.which === 13) { //key enter
                let element = $('#chat-input');
                stompClient.send(
                    '/app/message/' + currentRoom.id,
                    {},
                    JSON.stringify({'text': element.val()}));
                // Действия, которые нужно выполнить при нажатии на Enter на кнопке
                element.val('');
            }
        });

    }

})(jQuery);

let user;
let rooms;
let currentRoom;
function initRoom(data) {
    rooms = data;
    for (let i = 0; i < data.length; i++) {
        let name = data[i].name;
        let info = data[i].info;
        let imgSrc = '../img/defaultUser.svg';
        switch (data[i].type) {
            case 'PRIVATE':
                break;
            case 'PUBLIC':
            case 'CLOSED':

                imgSrc = '../img/defaultRoom.svg'
                break;
        }

        const block = document.createElement('div');
        block.classList.add('chat-item', 'd-flex', 'pl-3', 'pr-0', 'pt-3', 'pb-3');
        block.setAttribute('data-room-id', data[i].id);
        block.innerHTML = `
          <div class="w-100">
            <div class="d-flex pl-0">
              <img class="rounded-circle shadow avatar-sm mr-3" src="${imgSrc}">
              <div>
                <p class="margin-auto fw-400 text-dark-75">${name}</p>
                <div class="d-flex flex-row mt-1">
                  <span>
                    <div class="svg15 double-check"></div>
                  </span>
                  <span class="message-shortcut margin-auto fw-400 fs-13 ml-1 mr-4">${info}</span>
                </div>
              </div>
            </div>
          </div>
          <div class="flex-shrink-0 margin-auto pl-2 pr-3">
            <div class="d-flex flex-column">
              <p class="text-muted text-right fs-13 mb-2">08:21</p>
              <span class="round badge badge-light-success margin-auto">2</span>
            </div>
          </div>`;
        document.getElementById('chat-list').appendChild(block);
        stompClient.subscribe('/room/' + data[i].id, function (message) {
            let roomId = message.headers.destination.replace('/room/','');
            let messageList = JSON.parse(message.body);
            console.log('Message received: ' + messageList.length);
            initChat(messageList, roomId);
        });
    }
}

function changeRoom(roomId) {
    currentRoom = rooms.find(el => el.id === roomId);
    $('#chat-user-name').text(currentRoom.name);
    $.ajax({
        url: '/message',
        type: 'POST',
        data: {
            roomId: roomId
        },
        dataType: 'json',
        success: function (data) {
            $('.message').remove();
            initChat(data, roomId);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log(textStatus, errorThrown); //for debug
        }
    })
    stompClient.send(
        '/app/allRoomMessage/' + currentRoom.id,
        {},
        JSON.stringify({'id': currentRoom.id}));

}

function initChat(messageList, roomId) {
    if(currentRoom.id === roomId)
    {
        for (let i = 0; i < messageList.length; i++) {
            if (messageList[i].user.email === user.email) {
                addMyMessage(messageList[i]);
            } else {
                addMemberMessage(messageList[i]);
            }
        }
    }
}

function addMyMessage(message) {
    $('#message-list').append('<div class="message d-flex flex-row-reverse mb-2">\n' +
        '                    <div class="right-chat-message fs-13 mb-2">\n' +
        '                      <div class="mb-0 mr-3 pr-4">\n' +
        '                        <div class="d-flex flex-row">\n' +
        '                          <div class="pr-2">' + message.text + '</div>\n' +
        '                          <div class="pr-4"></div>\n' +
        '                        </div>\n' +
        '                      </div>\n' +
        '                      <div class="message-options dark">\n' +
        '                        <div class="message-time">\n' +
        '                          <div class="d-flex flex-row">\n' +
        '                            <div class="mr-2">' + timestampToDate(message.timestamp) + '</div>\n' +
        '                            <div class="svg15 double-check"></div>\n' +
        '                          </div>\n' +
        '                        </div>\n' +
        '                        <div class="message-arrow"><i class="text-muted la la-angle-down fs-17"></i></div>\n' +
        '                      </div>\n' +
        '                    </div>\n' +
        '                  </div>');
}

function addMemberMessage(message) {
    $('#message-list').append('<div class="message left-chat-message fs-13 mb-2">\n' +
        '                    <p class="mb-0 mr-3 pr-4">' + message.text + '</p>\n' +
        '                    <div class="message-options">\n' +
        '                      <div class="message-time">' + timestampToDate(message.timestamp) + '</div>\n' +
        '                      <div class="message-arrow"><i class="text-muted la la-angle-down fs-17"></i></div>\n' +
        '                    </div>\n' +
        '                  </div>');
}

function timestampToDate(timestamp) {
    let date = new Date(timestamp);
    let hour = date.getHours();
    if (hour < 10) {
        hour = '0' + hour;
    }
    let minutes = date.getMinutes();
    if (minutes < 10) {
        minutes = '0' + minutes;
    }
    return '' + hour + ':' + minutes;
}