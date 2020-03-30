var keymap = {};
document.addEventListener('keydown', function(event) {
    keymap[event.key] = true;
});
document.addEventListener('keyup', function(event) {
    keymap[event.key] = false;
});

function isKeyDown(key) {
    return keymap[key] === true;
}

export { isKeyDown };
