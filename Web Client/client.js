let webSocket = null;
let username;
let width;
let height;

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
        if (message.type === "login" && message.gameExists === true){
            renderBoard(message)
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

function newGame(){
    webSocket.send(JSON.stringify({type: "newgame", username: username, width: document.getElementById("width").value, height: document.getElementById("height").value, players: document.getElementById("players").value}))
}

function renderBoard(message){
    let board = document.getElementById("board")
    board.innerHTML = ""
    for (let i = 0; i < message.board.length; i++) {
        board.innerHTML += (message.board[i] + "<br/>")
    }
    width = message.width
    height = message.height
}

function playMove(){
    let column = document.getElementById("move").value
    if (column > 0 && column < width+1){
        webSocket.send(JSON.stringify({type: "input", column: column, username: username}))
    }
}

/*
function receiveMessage(message){
    document.getElementById("messages").appendChild(document.createElement("p"))
    document.getElementById("messages").children[document.getElementById("messages").children.length-1].innerHTML=message.username + ": " + message.message
    document.getElementById("messages").children[document.getElementById("messages").children.length-1].className="noMargin"
}

function sendMessage() {
    webSocket.send(JSON.stringify({type: "message", username: username, message: document.getElementById('messageField').value}))
    document.getElementById("messageField").value = "";
}
*/