$(document).ready(function() {
    var websocket = null;
    var stompClient = null;

    function initialState() {
        $("#message").prop("disabled", true);
        $("#sendMessage").prop("disabled", true);
        $("#userList").prop("disabled", true);
        $("#newMessages").prop("disabled", true);
    }

    function populateParticipants(participants) {
        $('#userList ul').empty();
        $.each(participants, function (i, participant) {
            var li = ('<span>' + participant + '</span><br>');
            $('#userList ul').append(li);
        })
    }

    function setupSubscriptions() {
        stompClient.subscribe('/topic/chat.message', function(message) {
            $("#newMessages ul").append('<li class="list-group-item">' + message.body + '</li>');
        })
        stompClient.subscribe('/topic/chat.participants', function(message) {
                populateParticipants(JSON.parse(message.body));
        })
        stompClient.subscribe('/app/chat.participants', function(message) {
        })
    }

    function handleError(error) {
        alert(error);
    }

    function connect() {
        if (stompClient == null) {
            websocket = new SockJS('/ws');
            stompClient = Stomp.over(websocket);
            stompClient.connect({username: $('#username').val()}, setupSubscriptions, handleError)
            connected();
        }
    }

    function connected() {
        $("#message").prop("disabled", false);
        $("#sendMessage").prop("disabled", false);
        $("#username").prop("disabled", true);
        $("#connectButton").prop("textContent", "Disconnect");
        $("#userList").prop("disabled", false);
        $("#newMessages").prop("disabled", false);
    }

    function disconnect() {
		if (stompClient != null) {
			stompClient.disconnect("", {username: $('#username').val()});
			stompClient = null;
		}
		disconnected();
    }

    function disconnected() {
        $("#message").prop("disabled", true);
        $("#sendMessage").prop("disabled", true);
        $("#username").prop("disabled", false);
        $("#connectButton").prop("textContent", "Connect");
        $("#userList").prop("disabled", true);
        $("#newMessages").prop("disabled", true);
        $("#userList ul").empty();
        $("#newMessages ul").empty();
    }

    function sendMessage() {
        var message = $('#message').val();
        stompClient.send("/app/chat.message", {username: $('#username').val()}, message);
    }

    $("#connectButton").on("click", function() {
        if ($("#connectButton").text() == "Connect") {
            connect();
        } else {
            disconnect();
        }
    })

    $("#sendMessage").on("click", function() {
        sendMessage();
    })

    initialState();
});

