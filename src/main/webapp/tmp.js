var arrAssigned = [];
var arrUnassigned = [];
var arrCompleted = [];

function getLists() {
    $.getJSON('/displaytodos?start=0', function(data) {
        arrAssigned = data["assigned"];
        arrUnassigned = data["unassigned"];
        arrCompleted = data["completed"];
        displayTodos();
    });
}

function updateTodo(array1, array2, array3){
    array1 = array1.filter( function( el ) {
        var tmp = array3.hasOwnProperty(el.id);
        if(tmp){
            array2.push(array3[el.id]);
        }
        return !tmp;
    } );
    return array1;
}

function updateLists() {
    $.getJSON('/displaytodos?start=1', function(data) {
        //console.log(data);
        arrchangedU = data["changedU"];
        arrchangedA = data["changedA"];
        arrDeleted = data["deleted"];
        arrAdded = data["added"];

        arrAssigned = deleteElements(arrAssigned, arrDeleted);
        arrUnassigned = deleteElements(arrUnassigned, arrDeleted);
        arrCompleted = deleteElements(arrCompleted, arrDeleted);

        arrUnassigned = updateTodo(arrUnassigned, arrAssigned, arrchangedU);
        arrAssigned = updateTodo(arrAssigned, arrCompleted, arrchangedA);

        /*console.log("-----------------");
        console.log(arrUnassigned);
        console.log(arrAssigned);
        console.log("-----------------");*/

        displayTodos();
    });

}


function deleteTodo(button){
    //console.log(button.id);
    var http = new XMLHttpRequest();
    var url =  "deletetodo?";
    var params = "id=" + button.id;
    http.open("GET", url+params, true);
    http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    http.onreadystatechange = function() {
    }
    http.send(params);
}

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

function complete(button) {
    var http = new XMLHttpRequest();
    var url =  "completetodo?";
    var params = "id=" + (button.id).toString();
    http.open("GET", url+params, true);
    http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
    http.onreadystatechange = function() {
    }
    http.send(params);
}
function deleteElements(array1, array2){
    array1 = array1.filter( function( el ) {
        return !array2.includes(el.id);
    } );
    return array1;
}
function take(button) {

    var http = new XMLHttpRequest();
    var url =  "assigntodo?";
    var params = "id=" + (button.id).toString();
    http.open("GET", url+params, true);

    //Send the proper header information along with the request
    http.setRequestHeader("Content-type", "application/x-www-form-urlencoded");

    http.onreadystatechange = function() {
    }
    http.send(params);
}



getLists();
setInterval(updateLists, 3000);
