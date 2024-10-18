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
        //document.getElementById("messageBox").style.display = "flex";
        webSocket.send(JSON.stringify({type: "login", username: username}))
    }

    webSocket.onmessage = event => {
        console.log(event.data)
        var message = JSON.parse(event.data)
        if (message.type === "update") {
            refresh(message)
        }
        if (message.type === "message"){
            document.getElementById("messageDisplay").innerHTML = message.message
        }
        if (message.type === "error"){
            document.getElementById("messageDisplay").innerHTML = message.message
        }
        if (message.type === "login"){
            document.getElementById("boardsStuff").hidden = !message.gameExists
            document.getElementById("newGameInputs").hidden = message.gameExists
            if (message.gameExists){
                document.getElementById("newGameInputs").style.display = "none";
                renderBoard(message)
            } else {
                document.getElementById("newGameInputs").style.display = "flex";
            }
        }
    }

    document.getElementById("move").addEventListener("keypress", function(e){
        if (e.key === "Enter"){
            e.preventDefault()
            playMove()
        }
    })
}

function refresh(message){
    document.getElementById("userList").innerHTML = "|   "
    for (let i = 0; i < message.users.length; i++) {
        document.getElementById("userList").innerHTML += (message.users[i].name + "   |   ")
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
    document.getElementById("turnIndicator").innerHTML = "Current Turn: " + message.players.charAt(message.turn)
    width = message.width
    height = message.height
}

function playMove(){
    let column = document.getElementById("move").value
    if (column > 0 && column < width+1){
        webSocket.send(JSON.stringify({type: "input", column: column, username: username}))
    }
    document.getElementById("move").value = ""
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