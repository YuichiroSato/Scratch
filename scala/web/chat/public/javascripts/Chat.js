let webSocket = null

function connectWebSocket() {
    const apiUrl = 'ws://' + window.location.host + window.location.pathname + '/connect'
    webSocket = new WebSocket(apiUrl)

    webSocket.onopen = function(event) {
        console.log("接続しました。")
    }

    webSocket.onmessage = function(event) {
        const data = JSON.parse(event.data)
        const chatList = document.getElementById("postList")
        const newPost = document.createElement("li")
        newPost.appendChild(document.createTextNode(data.text))
        chatList.appendChild(newPost)
        document.getElementById("submitText").value = ''
    }
}

function sendMessage() {
    const submitText = document.getElementById("submitText").value
    webSocket.send(JSON.stringify({ text: submitText }))
}

window.addEventListener('DOMContentLoaded', function() {
    connectWebSocket()
    document.getElementById("submitButton").addEventListener("click", sendMessage)
})
