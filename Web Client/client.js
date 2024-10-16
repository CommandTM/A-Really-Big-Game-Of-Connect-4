let webSocket = null;
let username;

function connect() {
    username = document.getElementById("user").value
    if (username.length > 30){
        return;
    }
    webSocket = new WebSocket(document.getElementById("ip").value)

    webSocket.onopen = function() {
        document.getElementById("messageBox").hidden = false
        webSocket.send(JSON.stringify({type: "login", username: username}))
    }

    webSocket.onmessage = event => {
        console.log(event.data)
        var message = JSON.parse(event.data)
        if (message.type === "update") {
            refresh(message)
        }
        if (message.type === "message"){
            receiveMessage(message)
        }
    }
}

function refresh(message){
    document.getElementById("userList").innerHTML = ""
    for (let i = 0; i < message.users.length; i++) {
        let name = document.createElement("p").innerHTML=message.users[i].name
        document.getElementById("userList").appendChild(document.createElement("p"))
        document.getElementById("userList").children[i].innerHTML=name
        document.getElementById("userList").children[i].className="noMargin"
        //document.getElementById("userList").append(<p class='noMargin'>"+message.users[i].name+"</p>)
    }
}

function receiveMessage(message){
    document.getElementById("messages").appendChild(document.createElement("p"))
    document.getElementById("messages").children[document.getElementById("messages").children.length-1].innerHTML=message.username + ": " + message.message
    document.getElementById("messages").children[document.getElementById("messages").children.length-1].className="noMargin"
}

function sendMessage() {
    webSocket.send(JSON.stringify({type: "message", username: username, message: document.getElementById('messageField').value}))
    document.getElementById("messageField").value = "";
}