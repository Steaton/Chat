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
        $("#userList ul").empty();
        $("#newMessages ul").empty();
    }

    function sendMessage() {
        var message = $('#message').val();
        stompClient.send("/app/chat.message", {username: $('#username').val()}, message);
        $("#message").val("");
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

    <!-- Allow only alphanumeric -->
    $('#username').keydown(function (e) {
        var k = e.which;
        var ok = k >= 65 && k <= 90 || // A-Z
            k >= 96 && k <= 105 || // a-z
            k >= 35 && k <= 40 || // arrows
            k == 8 || // Backspaces
            (!e.shiftKey && k >= 48 && k <= 57); // 0-9

        if (!ok){
            e.preventDefault();
        }
    });

    <!-- Enter key for sending message -->
    $(document).keypress(function(e){
        if (e.which == 13){
            $("#sendMessage").click();
        }
    });

    initialState();
});

