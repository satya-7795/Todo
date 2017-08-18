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

getLists();
setInterval(updateLists, 3000);
