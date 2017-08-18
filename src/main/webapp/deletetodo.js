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