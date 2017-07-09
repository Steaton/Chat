$(document).ready(function() {
    var websocket = null;
    var stompClient = null;

    websocket = new SockJS('/chat');
    stompClient = Stomp.over(websocket);
    stompClient.connect({}, function(frame) {
        stompClient.subscribe('/topic/chat', function(message) {
            alert(message);
        })
    });

    function sendForm() {
        stompClient.send("/topic/chat", {}, $('#message').val());
    };

    function connect() {
        var un = $('#username').val();
        alert(un);
        stompClient.send("/queue/connect", {}, un);
    };

    $("#connectButton").on("click", function() {
        connect();
    })
});

