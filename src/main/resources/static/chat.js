$(document).ready(function() {
    var websocket = null;
    var stompClient = null;

    function initialState() {
        $("#message").prop("disabled", true);
        $("#sendMessage").prop("disabled", true);
    }

    function sendForm() {
        stompClient.send("/topic/chat", {}, $('#message').val());
    }

    function connect() {
        var username = $('#username').val();
        websocket = new SockJS('/ws');
        stompClient = Stomp.over(websocket);
        stompClient.connect({username: username}, function(frame) {
            stompClient.subscribe('/topic/chat', function(message) {
                $("#newMessages ul").append('<li class="list-group-item">' + message.body + '</li>');
            })
        });
        connected();
    }

    function connected() {
        $("#message").prop("disabled", false);
        $("#sendMessage").prop("disabled", false);
        $("#username").prop("disabled", true);
        $("#connectButton").prop("value", "Disconnect");
    }

    function sendMessage() {
        var message = $('#message').val();
        stompClient.send("/topic/chat", {}, message);
    }

    $("#connectButton").on("click", function() {
        connect();
    })

    $("#sendMessage").on("click", function() {
        sendMessage();
    })

    initialState();
});

