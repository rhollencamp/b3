$(document).ready(function() {
    var ws = new WebSocket("ws://localhost:8080/test");
    ws.onmessage = function (evt) {
        var pos = JSON.parse(evt.data);
        $("#player").css("top", pos.y);
        $("#player").css("left", pos.x);
    };

    window.setInterval(function() {
        var inputUpdate = [];
        if (keymap[87]) inputUpdate.push('UP');
        if (keymap[83]) inputUpdate.push('DOWN');
        if (keymap[65]) inputUpdate.push('LEFT');
        if (keymap[68]) inputUpdate.push('RIGHT');

        ws.send(JSON.stringify(inputUpdate));
    }, 15);

    var keymap = {};
    document.addEventListener('keydown', function(event) {
        keymap[event.keyCode] = true;
    });
    document.addEventListener('keyup', function(event) {
        keymap[event.keyCode] = false;
    });
});
