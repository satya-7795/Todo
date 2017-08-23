package objects;

import com.google.gson.Gson;
import constants.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractDataStore {
    List<ToDo> unassigned;
    List<ToDo> assigned;
    List<ToDo> completed;
    List<ToDo> added;
    Map<String, ToDo> changedU;
    Map<String, ToDo> changedA;
    List<Long> deleted;

    AbstractDataStore(){
        this.unassigned = new ArrayList<ToDo>();
        this.assigned = new ArrayList<ToDo>();
        this.completed = new ArrayList<ToDo>();
        this.added = new ArrayList<ToDo>();
        this.deleted = new ArrayList<Long>();
        this.changedA = new HashMap<String, ToDo>();
        this.changedU = new HashMap<String, ToDo>();
    }

    public static AbstractDataStore getInstance(){
        return null;
    }


    abstract public long createUser(User u);
    abstract public long createToDo(ToDo todo);
    abstract public User findUser(String username);
    abstract public boolean validateCookie(String userName, String token);
    abstract public boolean deletetodo(long id,String status,String tmpName);
    abstract public boolean assignToDo(long id, String status, String userName);
    abstract public boolean completeToDo(long id, String status, String userName);
    abstract public boolean clearUserData(String userName, String token);
    abstract public void putCookie(String token, String userName);

    public void clearTmpData(){
        synchronized(Store.class) {
            if (deleted.size() > 100)
                deleted.subList(0, 80).clear();
            if (added.size() > 100)
                added.subList(0, 80).clear();
            if (changedU.size() > 100) {
                String[] keys = changedU.keySet().toArray(new String[changedU.size()]);
                for (int i = 0; i < 80; i++) {
                    changedU.remove(keys[i]);
                }
            }
            if (changedA.size() > 100) {
                String[] keys = changedA.keySet().toArray(new String[changedA.size()]);
                for (int i = 0; i < 80; i++) {
                    changedA.remove(keys[i]);
                }
            }
        }
    }

    public String getToDos(String start){
        Gson gson = new Gson();
        String tmp ="";
        Map<String, Object> map = new HashMap<String, Object>();
        if(start.equals("1")){
            map.put(Constants.DEL.getValue(), deleted);
            map.put(Constants.ADD.getValue(), added);
            map.put(Constants.CU.getValue(), changedU);
            map.put(Constants.CA.getValue(), changedA);
        }else{
            map.put(Constants.UNASSIGNED.getValue(), unassigned);
            map.put(Constants.ASSIGNED.getValue(), assigned);
            map.put(Constants.COMPLETED.getValue(), completed);
        }
        tmp = gson.toJson(map);
        return  tmp;
    }
}
