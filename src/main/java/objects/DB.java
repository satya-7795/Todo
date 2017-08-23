package objects;
import com.google.gson.Gson;
import constants.Constants;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DB extends AbstractDataStore {
    static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    static final String DB_URL = "jdbc:mysql://localhost/2do";
    static final String USER = "root";
    static final String PASS = "";
    Connection conn = null;
    private static DB instance = null;

    DB(){
        super();
        try{
            Class.forName(JDBC_DRIVER);
            System.out.println("Connecting to a selected database...");
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            System.out.println("Connected database successfully...");
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static DB getInstance(){
        synchronized(Store.class) {
            if (instance == null) {
                instance = new DB();
            }
        }
        return instance;
    }

    public long createUser(User u) {
        long id = -1;
        try {
            PreparedStatement stmt = null;
            //conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.prepareStatement("SELECT * FROM users WHERE user_name = ? OR user_email = ?");
            stmt.setString(1, u.getUserName());
            stmt.setString(2, u.getEmailId());
            ResultSet rs = stmt.executeQuery();
            if (rs.next() == false) {
                stmt = conn.prepareStatement("INSERT INTO users (user_name, user_email, password) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, u.getUserName());
                stmt.setString(2, u.getEmailId());
                stmt.setString(3, u.getPassword());
                int num = stmt.executeUpdate();
                rs = stmt.getGeneratedKeys();
                if (num == 1 && rs.next()){
                    id=rs.getInt(1);
                }else{
                    id = -1;
                }
            }
        }catch (SQLException se){
            id = -1;
        }
        return id;
    }

    public long createToDo(ToDo todo) {
        long id = -1;
        try {
            PreparedStatement stmt = null;
            stmt = conn.prepareStatement("SELECT todo_id FROM todos WHERE todo_name = ?");
            stmt.setString(1, todo.getName());
            ResultSet rs = stmt.executeQuery();
            if (rs.next() == false) {
                stmt = conn.prepareStatement("INSERT INTO todos (todo_name, todo_msg, todo_status, user_name) VALUES (?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, todo.getName());
                stmt.setString(2, todo.getMsg());
                stmt.setString(3, "unassigned");
                stmt.setString(4, todo.getUserName());
                int num = stmt.executeUpdate();
                rs = stmt.getGeneratedKeys();
                if (num == 1 && rs.next()){
                    id=rs.getInt(1);
                }else{
                    id = -1;
                }
            }
        }catch (SQLException se){
            id = -1;
        }
        return id;
    }

    public User findUser(String username)  {
        try {
            PreparedStatement stmt = null;
            stmt = conn.prepareStatement("SELECT * FROM users WHERE user_name = ?");
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() == false)
                return null;
            //System.out.println(rs.getString(2));
            User user = new User(rs.getString(2), rs.getString(4), rs.getString(3));
            //user.setUserId(rs.getLong(1));
            return user;
        }catch (SQLException se){
        }
        return null;
    }

    public boolean validateCookie(String userName, String token) {
        try {
            if (token.equals(null) || userName.equals(null)) return false;
            PreparedStatement stmt = null;
            stmt = conn.prepareStatement("SELECT * FROM cookies WHERE user_name = ? AND token = ?");
            stmt.setString(1, userName);
            stmt.setString(2, token);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() == false) {
                return false;
            }
            return true;
        }catch (SQLException se){
            return false;
        }
    }


    public void getLists(String start) throws SQLException {
        if(start.equals("1")) return;
        unassigned.clear();
        assigned.clear();
        completed.clear();
        PreparedStatement stmt = null;
        stmt = conn.prepareStatement("SELECT * FROM todos");
        ResultSet rs = stmt.executeQuery();
        while(rs.next()){
            Long id = rs.getLong(1);
            String name = rs.getString(2);
            String msg = rs.getString(3);
            String status = rs.getString(4);
            String userName = rs.getString(5);
            ToDo todo = new ToDo(name, msg);
            todo.setToDoId(id);
            todo.addUser(findUser(userName));
            todo.changeStatus(status);
            if(status.equals(Constants.UNASSIGNED.getValue())){
                unassigned.add(todo);
            }else if(status.equals(Constants.ASSIGNED.getValue())){
                assigned.add(todo);
            }else{
                completed.add(todo);
            }
        }
    }

    public String getToDos(String start){
        try {
            getLists(start);
        }catch (SQLException se){
            return "";
        }
        return super.getToDos(start);
    }

    public boolean deletetodo(long id,String status,String tmpName){
        try {
            User user = findUser(tmpName);
            if (user == null) return false;
            PreparedStatement stmt = null;
            stmt = conn.prepareStatement("SELECT * FROM todos WHERE todo_id = ?");
            stmt.setString(1, String.valueOf(id));
            ResultSet rs = stmt.executeQuery();
            if (rs.next() == false) {
                return false;
            }
            String userName = rs.getString(5);
            if (user.getUserName().equals(userName)) {
                stmt = conn.prepareStatement("DELETE FROM todos WHERE todo_id = ?");
                stmt.setString(1, String.valueOf(id));
                int num = stmt.executeUpdate();
                if(num==1){
                    deleted.add(id);
                    return true;
                }
                return false;
            }
            return false;
        }catch (SQLException se){
            return false;
        }
    }

    public boolean changeToDo(long id, String status, String userName, boolean check)throws SQLException {
        User user = findUser(userName);
        if(user == null) return false;
        String tmpStatus = !check ? Constants.UNASSIGNED.getValue() : Constants.ASSIGNED.getValue();
        if(status.equals(tmpStatus)) {
            PreparedStatement stmt = null;
            stmt = conn.prepareStatement("SELECT * FROM todos WHERE todo_id = ? AND todo_status = ?");
            stmt.setString(1, String.valueOf(id));
            stmt.setString(2, status);
            ResultSet rs = stmt.executeQuery();
            if(rs.next() == false) return false;
            ToDo toDo = new ToDo(rs.getString(2), rs.getString(3));
            toDo.setToDoId(Long.parseLong(rs.getString(1)));
            toDo.addUser(user);
            toDo.changeStatus(!check ? Constants.ASSIGNED.getValue() : Constants.COMPLETED.getValue());
            if(check && !rs.getString(5).equals(user.getUserName()))
                return false;
            stmt = conn.prepareStatement("UPDATE todos SET todo_status = ? , user_name = ? WHERE todo_id = ?");
            stmt.setString(3, String.valueOf(id));
            stmt.setString(1, !check ? Constants.ASSIGNED.getValue() : Constants.COMPLETED.getValue());
            stmt.setString(2, user.getUserName());
            int num = stmt.executeUpdate();
            if(num == 0) return false;
            if(!check)
                changedU.put(Long.toString(toDo.getId()), toDo);
            else
                changedA.put(Long.toString(toDo.getId()), toDo);
            return true;
        }
        return false;
    }

    public boolean assignToDo(long id, String status, String userName) {
        try {
            return changeToDo(id, status, userName, false);
        }catch (SQLException se){
            return false;
        }
    }

    public boolean completeToDo(long id, String status, String userName) {
        try {
            return changeToDo(id, status, userName, true);
        }catch (SQLException se){
            return false;
        }
    }

    public boolean clearUserData(String userName, String token) {
        try {
            PreparedStatement stmt = null;
            stmt = conn.prepareStatement("SELECT * FROM cookies WHERE user_name = ? AND token = ?");
            stmt.setString(1, userName);
            stmt.setString(2, token);
            ResultSet rs = stmt.executeQuery();
            if (rs.next() == false) return false;
            stmt = conn.prepareStatement("DELETE  FROM cookies WHERE user_name = ? AND token = ?");
            stmt.setString(1, userName);
            stmt.setString(2, token);
            int num = stmt.executeUpdate();
            return num == 1 ? true : false;
        }catch (SQLException se){
            return false;
        }
    }

    public void putCookie(String token, String userName) {
        try {
            PreparedStatement stmt = null;
            stmt = conn.prepareStatement("INSERT INTO cookies(user_name, token)  VALUES (?, ?)");
            stmt.setString(1, userName);
            stmt.setString(2, token);
            int num = stmt.executeUpdate();
        }catch (SQLException se){
        }
    }

}
