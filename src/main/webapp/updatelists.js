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

function deleteElements(array1, array2){
    array1 = array1.filter( function( el ) {
        return !array2.includes(el.id);
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