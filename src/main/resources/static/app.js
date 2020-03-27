$(document).ready(function() {
    var ws = new WebSocket("ws://localhost:8080/test");
    ws.onmessage = function (evt) {
        alert(evt.data);
    };

    $('#test').on('click', function() {
        ws.send('hello world');
    });
});
