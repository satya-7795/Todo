import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Store {
    private static Store instance = null;

    private long userId = 0;
    private long toDoId = 0;
    private Map<String, User> accounts = null;
    private Map<String, String> userToken = null;
    private List<ToDo> unassigned;
    private List<ToDo> assigned;
    private List<ToDo> completed;
    private List<ToDo> added;
    private Map<String, ToDo> changedU;
    private Map<String, ToDo> changedA;
    private List<Long> deleted;

    private Store(){
        this.accounts = new HashMap<String, User>();
        this.userToken = new HashMap<String, String>();
        this.unassigned = new ArrayList<ToDo>();
        this.assigned = new ArrayList<ToDo>();
        this.completed = new ArrayList<ToDo>();
        this.added = new ArrayList<ToDo>();
        this.deleted = new ArrayList<Long>();
        this.changedA = new HashMap<String, ToDo>();
        this.changedU = new HashMap<String, ToDo>();
    }

    public static Store getInstance(){
        synchronized(Store.class) {
            if (instance == null) {
                instance = new Store();
            }
        }
        return instance;
    }

    public long createUser(User u){

        long id = -1;
        synchronized(Store.class) {
            if (!accounts.containsKey(u.getUserName())) {
                u.setUserId(++userId);
                accounts.put(u.getUserName(), u);
                id = userId;
            }
        }
        return id;
    }

    public boolean checkTodo(ToDo todo){
        for(int i=0;i < unassigned.size() ;i++){
            if (unassigned.get(i).getName().equals(todo.getName())){
                return false;
            }
        }
        for(int i=0;i < assigned.size() ;i++){
            if (assigned.get(i).getName().equals(todo.getName())){
                return false;
            }
        }
        for(int i=0;i < completed.size() ;i++){
            if (completed.get(i).getName().equals(todo.getName())){
                return false;
            }
        }
        return true;
    }

    public long createToDo(ToDo todo){
        long id = -1;
        synchronized(Store.class) {
            if (checkTodo(todo)) {
                todo.setToDoId(++toDoId);
                unassigned.add(todo);
                id = toDoId;
                added.add(todo);
            }
        }
        return id;
    }

    public User findUser(String username){
        if(!accounts.containsKey(username)) return null;
        return accounts.get(username);
    }

    public Map<String, String> getUserTokenMap(){
        return userToken;
    }

    public boolean validateCookie(String userName, String token){
        if(token.equals(null) || userName.equals(null)) return false;
        if(!userToken.containsKey(token)) return false;
        return userToken.get(token).equals(userName);
    }

    public String getToDos(String start){
        Gson gson = new Gson();
        String tmp ="";
        Map<String, Object> map = new HashMap<String, Object>();
        if(start.equals("1")){
            map.put("deleted", deleted);
            map.put("added", added);
            map.put("changedU", changedU);
            map.put("changedA", changedA);
        }else{
            map.put("unassigned", unassigned);
            map.put("assigned", assigned);
            map.put("completed", completed);
        }
        tmp = gson.toJson(map);
        return  tmp;
    }

    public void clearTmpData(){
        if(deleted.size() > 100)
            deleted.subList(0, 80).clear();
        if(added.size() > 100)
            added.subList(0, 80).clear();
        if(changedU.size() > 100){
            String[] keys = changedU.keySet().toArray(new String[changedU.size()]);
            for(int i = 0; i < 80; i++) {
                changedU.remove(keys[i]);
            }
        }
        if(changedA.size() > 100){
            String[] keys = changedA.keySet().toArray(new String[changedA.size()]);
            for(int i = 0; i < 80; i++) {
                changedA.remove(keys[i]);
            }
        }
    }

    public String getToDo(Long id, String status){
        Gson gson = new Gson();
        ToDo tmp = new ToDo("","");
        if(status.equals("unassigned")){
            for(ToDo i: unassigned){
                if(i.getId() == id){
                    tmp = i;
                }
            }
        }else if (status.equals("assigned")){
            for(ToDo i: assigned){
                if(i.getId() == id){
                    tmp = i;
                }
            }
        }else{
            for(ToDo i: completed){
                if(i.getId() == id){
                    tmp = i;
                }
            }
        }
        return  gson.toJson(tmp);
    }

    public boolean deletetodo(long id,String status,String tmpName){
        User user = null;
        if(accounts.containsKey(tmpName))
            user = accounts.get(tmpName);
        if(user == null) return false;
        if(status.equals("unassigned")){
            for(ToDo i: unassigned){
                if(i.getId() == id){
                    deleted.add(i.getId());
                    unassigned.remove(i);
                    return true;
                }
            }
        }else if (status.equals("assigned")){
            for(ToDo i: assigned){
                if(i.getId() == id && i.hasUser(user)){
                    deleted.add(i.getId());
                    assigned.remove(i);
                    return true;
                }
            }
        }else{
            for(ToDo i: completed){
                if(i.getId() == id && i.hasUser(user)){
                    deleted.add(i.getId());
                    completed.remove(i);
                    return true;
                }
            }
        }
        return false;
    }

    public  boolean editTodo(long id,String name, String msg, String username, String status){
        User user = accounts.get(username);
        if(user == null) return false;
        if(status.equals("unassigned")){
            for(ToDo i: unassigned){
                if(i.getId() == id){
                    i.update(name, msg);
                    return true;
                }
            }
        }else if (status.equals("assigned")){
            for(ToDo i: assigned){
                if(i.getId() == id && i.hasUser(user)){
                    i.update(name, msg);
                    return true;
                }
            }
        }else{
            for(ToDo i: completed){
                if(i.getId() == id && i.hasUser(user)){
                    i.update(name, msg);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean assignToDo(long id, String status, String userName){
        User user = null;
        if(accounts.containsKey(userName))
            user = accounts.get(userName);
        if(user == null) return false;
        System.out.println(user.getUserName());
        if(status.equals("unassigned")){
            for(ToDo i: unassigned){
                if(i.getId() == id){
                    i.changeStatus("assigned");
                    i.addUser(user);
                    this.unassigned.remove(i);
                    this.assigned.add(i);
                    changedU.put(Long.toString(i.getId()), i);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean completeToDo(long id, String status, String userName){
        User user = null;
        if(accounts.containsKey(userName))
            user = accounts.get(userName);
        if(user == null) return false;
        if(status.equals("assigned")){
            for(ToDo i: assigned){
                if(i.getId() == id && i.hasUser(user)){
                    i.changeStatus("completed");
                    this.assigned.remove(i);
                    this.completed.add(i);
                    changedA.put(Long.toString(i.getId()), i);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean clearUserData(String userName, String token){
        if(!userToken.containsKey(token)) return false;
        if(!userToken.get(token).equals(userName)) return false;
        userToken.remove(token);
        return true;
    }

    public  boolean getTrue(){
        return true;
    }

}