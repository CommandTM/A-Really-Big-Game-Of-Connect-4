let webSocket = null;
let username;

function connect() {
    webSocket = new WebSocket(document.getElementById("ip").value)
    username = document.getElementById("user").value

    webSocket.onopen = function() {
        document.getElementById("messageBox").hidden = false
        webSocket.send(JSON.stringify({type: "login", username: username}))
    }

    webSocket.onmessage = event => {
        console.log(event.data)
    }
}

function sendMessage() {
    webSocket.send(document.getElementById('text').value)
}