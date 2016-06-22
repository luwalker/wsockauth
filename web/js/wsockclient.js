var sessionWebSocket = null;
var apiToken = "";
var apiExpiration = "";

function init() {
    //alert("Init entrance");
}

function startAuth(){
    // check session instance
    if(sessionWebSocket !== undefined && sessionWebSocket !== null){
        document.getElementById("messagewnd").innerHTML += "JS: Session exists already<br>";
        return;
    }
    
    // start new session
    sessionWebSocket = new WebSocket("ws://localhost:8080/WsockAuth/wsockauth");
    sessionWebSocket.onopen = sessionOnOpen;
    sessionWebSocket.onmessage = sessionOnMessage;
    sessionWebSocket.onclose = sessionOnClose;

    // send athentication
    setTimeout(sendAuth, 1000 );
}

function userRegistration() {
    var authObject = new Object();
    authObject.email = document.getElementById("authemail").value;
    authObject.password = document.getElementById("authpassw").value;
    
    var authString = JSON.stringify(authObject);
    sendMsg(authString, "REGISTRATION_CUSTOMER");
}

function sendAuth() {
    var authObject = new Object();
    authObject.email = document.getElementById("authemail").value;
    authObject.password = document.getElementById("authpassw").value;
    
    var authString = JSON.stringify(authObject);
    sendMsg(authString, "LOGIN_CUSTOMER");
}

function sessionOnOpen(event){
    //alert("JS: Session opened.");
    document.getElementById("messagewnd").innerHTML += "JS: Session started.<br>";
}
function sessionOnMessage(event){
    //alert(event.data);
    //document.getElementById("messagewnd").innerHTML += event.data + "<br>";
    
    var messageObject = JSON.parse(event.data);
    messageObject.dataObject = JSON.parse(messageObject.data);
    
    // sequence_id
    switch (messageObject.type) {
        case "USER_MESSAGE":
            document.getElementById("messagewnd").innerHTML += messageObject.dataObject.text + "<br>";
            break;
        case "REGISTRATION_CUSTOMER":
            document.getElementById("messagewnd").innerHTML += messageObject.data + "<br>";
            break;
        case "LOGIN_CUSTOMER":
            document.getElementById("messagewnd").innerHTML += messageObject.data + "<br>";
            break;
        case "CUSTOMER_API_TOKEN":
            apiToken = messageObject.dataObject.api_token;
            apiExpiration = messageObject.dataObject.api_token_expiration_date;
            document.getElementById("messagewnd").innerHTML += "JS: Token: <b>" + apiToken + "</b><br>";
            document.getElementById("messagewnd").innerHTML += "JS: Expiration: <b>" + apiExpiration + "</b><br>";
            break;
        default:
    }
}
function sessionOnClose(event){
    //alert("JS: Session closed.");
    document.getElementById("messagewnd").innerHTML += "JS: Session closed.<br>===<br><br>";
    sessionWebSocket = null;
    apiToken = "";
    apiExpiration = "";
}


function closeSession(){
    if(sessionWebSocket === undefined || sessionWebSocket === null){
        document.getElementById("messagewnd").innerHTML += "JS: Session is not active.<br><br>";
        return;
    }
    sessionWebSocket.close();
}

function sendMsg(messageString, messageType, sequenceId){
    sequenceId = sequenceId || "";
    if (sessionWebSocket !== null) {
    //if (apiToken !== "" || messageType === "LOGIN_CUSTOMER") {

        if (messageString === undefined){
            messageString = document.getElementById("msginput").value;
            messageType = "USER_MESSAGE";
        }
        sessionWebSocket.send(makeMessage(messageString, messageType, sequenceId));
    }
    else {
        document.getElementById("messagewnd").innerHTML += "JS: Session is not active.<br><br>";
    }
}

function makeMessage(messageString, messageType, sequenceId) {
    if ( messageType === "USER_MESSAGE") {
        var textObject = new Object();
        textObject.text = messageString;
        messageString = JSON.stringify(textObject);
    }
    
    var messageObject = new Object();
    messageObject.type = messageType;
    messageObject.data = messageString;
    if (sequenceId === "") {
        sequenceId = createGuid();
    }
    messageObject.sequence_id = sequenceId;
    return(JSON.stringify(messageObject));
}

/*
 * used as example of
 * http://byronsalau.com/blog/how-to-create-a-guid-uuid-in-javascript/
 */
function createGuid()
{
    return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, function(c) {
        var r = Math.random()*16|0, v = c === 'x' ? r : (r&0x3|0x8);
        return v.toString(16);
    });
}

window.onload = init;