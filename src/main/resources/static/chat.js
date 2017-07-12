$(document).ready(function() {
    var websocket = null;
    var stompClient = null;

    function initialState() {
        $("#message").prop("disabled", true);
        $("#sendMessage").prop("disabled", true);
    }

    function populateParticipants(participants) {
        $('#userList ul').empty();
        $.each(participants, function (i, participant) {
            var li = ('<span>' + participant + '</span><br>');
            $('#userList ul').append(li);
        })
    }

    function setupSubscriptions(stompClient) {
        stompClient.subscribe('/topic/chat.message', function(message) {
            $("#newMessages ul").append('<li class="list-group-item">' + message.body + '</li>');
        })
        stompClient.subscribe('/app/chat.participants', function(message) {
        })
        stompClient.subscribe('/topic/chat.participants', function(message) {
                populateParticipants(JSON.parse(message.body));
        })
    }

    function connect() {
        var username = $('#username').val();
        websocket = new SockJS('/ws');
        stompClient = Stomp.over(websocket);
        stompClient.connect({username: username}, function(frame) {
            setupSubscriptions(stompClient);
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
        stompClient.send("/app/chat.message", {}, message);
    }

    $("#connectButton").on("click", function() {
        connect();
    })

    $("#sendMessage").on("click", function() {
        sendMessage();
    })

    initialState();
});

