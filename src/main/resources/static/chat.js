$(document).ready(function() {
    var ws = new SockJS('/chat', 9090);
    var stompClient = Stomp.over(ws);

    stompClient.connect({}, function(frame) {
        stompClient.subscribe('/topic/chat', function(message) {
            alert(message);
        })
    });

    function sendForm() {
        stompClient.send("/topic/chat", {}, $('#message').val());
    };

    function connect() {
        stompClient.send("/queue/connect", {}, $('username').val());
    };
});
