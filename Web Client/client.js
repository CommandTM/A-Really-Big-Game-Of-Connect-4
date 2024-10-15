let webSocket = null;

function connect() {
    webSocket = new WebSocket('ws://localhost:81')

    webSocket.onopen = function() {
        document.getElementById("messageSend").hidden = false
    }

    webSocket.onmessage = event => {
        console.log(event.data)
    }
}

function sendMessage() {
    webSocket.send(document.getElementById('text').value)
}