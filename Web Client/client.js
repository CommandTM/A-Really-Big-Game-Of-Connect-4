let webSocket = null;
let username;

function connect() {
    webSocket = new WebSocket('ws://localhost:81')

    webSocket.onopen = function() {
        document.getElementById("messageSend").hidden = false
        webSocket.send(JSON.stringify({type: "login", username: document.getElementById("user").value}))
    }

    webSocket.onmessage = event => {
        console.log(event.data)
    }
}

function sendMessage() {
    webSocket.send(document.getElementById('text').value)
}