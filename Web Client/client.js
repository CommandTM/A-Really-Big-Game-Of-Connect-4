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
        var message = JSON.parse(event.data)
        if (message.type === "update") {
            refresh(message)
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

function sendMessage() {
    webSocket.send(document.getElementById('text').value)
}