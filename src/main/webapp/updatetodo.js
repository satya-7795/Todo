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