package constants;

public enum  Constants {
    DEL("deleted"),
    ADD("added"),
    CU("changedU"),
    CA("changedA"),
    UNASSIGNED("unassigned"),
    ASSIGNED("assigned"),
    COMPLETED("completed"),
    USERNAME("userName"),
    TOKEN("token"),
    LOGINPAGE("login.html"),
    MAINPAGE("main.html"),
    ID("id"),
    TODONAME("todoName"),
    TODOMSG("todoMsg"),
    CREATETODOPAGE("createtodo.html"),
    START("start"),
    JSON("application/json"),
    REGISTERPAGE("register.html"),
    PASSWORD("password"),
    EMAIL("email"),
    USEDB("true");


    private String value;
    Constants(String value) {
        this.value = value;
    }

    public String getValue(){
        return this.value;
    }
}
