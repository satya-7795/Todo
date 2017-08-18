import java.util.ArrayList;
import java.util.List;

public class ToDo {
    private String name;
    private String msg;
    private List<User> users;
    private String status;
    private long id=-1;

    ToDo(String name, String msg){
        this.name = name;
        this.msg = msg;
        this.status = "unassigned";
        this.users = new ArrayList<User>();
    }

    public void changeStatus(String newStatus){
        this.status = newStatus;
    }

    public void setToDoId(long toDoId){
        this.id = toDoId;
    }

    public boolean hasUser(User user){
        return users.contains(user);
    }

    public void update(String name,String msg){
        this.name = name;
        this.msg = msg;
    }

    public void addUser(User user){
        this.users.add(user);
    }

    public long getId(){ return this.id;}

    public String getName(){return this.name;}
}
