function displayTodos() {
    console.log(arrAssigned);
    console.log(arrUnassigned);
    console.log(arrCompleted);

    var l = arrAssigned.length;
    var str = "<div>";
    for(var i=0;i<l;i++){
        str += "<div>User:" + arrAssigned[i]["users"][0]["userName"] + "</div>"
        str += "<div>" + arrAssigned[i]["msg"] + "</div>"
        str += "<button id="+ arrAssigned[i]["id"] +" onclick='complete(this)'>Complete</button>"
        str += "<button id="+ arrAssigned[i]["id"] +":assigned onclick='deleteTodo(this)'>Delete</button>"
    }
    str += "</div>";
    document.getElementById("assigned").innerHTML = str;
    var l = arrUnassigned.length;
    var str = "<div>";
    for(var i=0;i<l;i++){
        str += "<div>" + arrUnassigned[i]["msg"] + "</div>"
        str += "<button id="+ arrUnassigned[i]["id"] +" onclick = 'take(this)'>Take</button>"
        str += "<button id="+ arrUnassigned[i]["id"] +":unassigned onclick='deleteTodo(this)'>Delete</button>"
    }
    str += "</div>";
    document.getElementById("unassigned").innerHTML = str;
    var l = arrCompleted.length;
    var str = "<div>";
    for(var i=0;i<l;i++){
        str += "<div>User:" + arrCompleted[i]["users"][0]["userName"] + "</div>"
        str += "<div>" + arrCompleted[i]["msg"] + "</div>"
        str += "<button id="+ arrCompleted[i]["id"] +":completed onclick='deleteTodo(this)'>Delete</button>"
    }
    str += "</div>";
    document.getElementById("completed").innerHTML = str;

}
