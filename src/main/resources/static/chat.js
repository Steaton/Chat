$(document).ready(function() {
    var username = prompt('Enter Username:', username) || '';

    var ws = new SockJS('/chat');
    var stompClient = Stomp.over(ws);

    stompClient.connect({}, function(frame) {
        stompClient.subscribe('/topic/chat', function(message) {
            alert(message);
        })
    });

    function sendForm() {
        stompClient.send("/topic/chat", {}, $('#message').val());
    };
});
