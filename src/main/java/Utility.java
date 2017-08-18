import javafx.util.Pair;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;

public class Utility {
    public  static Pair<String, String> getUser(HttpServletRequest request){
        String tmpName = "", tmpToken = "";
        Cookie cookies[] = request.getCookies();
        for(Cookie i : cookies){
            if(i.getName().equals("userName")){
                tmpName = i.getValue();
            }else if(i.getName().equals("token")){
                tmpToken = i.getValue();
            }
        }
        return new Pair<String, String>(tmpName, tmpToken);
    }
    public static void assigntodo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        String toDoStatus = "unassigned";

        Pair<String, String> p = getUser(request);
        String tmpName = p.getKey(), tmpToken = p.getValue();


        if(!Store.getInstance().validateCookie(tmpName, tmpToken)){
            //User not recognised
            response.sendRedirect("login.html");
            return;
        }

        if(Store.getInstance().assignToDo(id, toDoStatus, tmpName)){
            response.sendRedirect("/main.html");
            return;
        }else {
            response.sendRedirect("/main.html");
            return;
        }
    }
    public static void deletetodo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter("id");
        String[] arr = id.split(":");
        if(arr.length < 2) response.sendRedirect("main.html");
        //long toDoId = Long.parseLong(request.getParameter("toDoId"));
        Pair<String, String> p = getUser(request);
        String tmpName = p.getKey(), tmpToken = p.getValue();
        if(!Store.getInstance().validateCookie(tmpName, tmpToken)){
            //User not recogized
            response.sendRedirect("login.html");
        }

        if(Store.getInstance().deletetodo(Long.parseLong(arr[0]), arr[1], tmpName)){
            //Deletion succeded
            response.sendRedirect("main.html");
        }else{
            //Deletion failed
            response.sendRedirect("main.html");
        }
    }
    public static void completetodo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        Pair<String, String> p = getUser(request);
        String tmpName = p.getKey(), tmpToken = p.getValue();

        if(!Store.getInstance().validateCookie(tmpName, tmpToken)){
            //User not recognised
            response.sendRedirect("login.html");
            return;
        }

        if(Store.getInstance().completeToDo(id, "assigned", tmpName)){
            ///toDo assigned
            //redirect to main
            response.sendRedirect("main.html");
            return;
        }else {
            //failed to assign
            response.sendRedirect("main.html");
            return;
        }
    }
    public static void createtodo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter("todoName");
        String msg = request.getParameter("todoMsg");

        Pair<String, String> p = getUser(request);
        String tmpName = p.getKey(), tmpToken = p.getValue();


        if(!Store.getInstance().validateCookie(tmpName, tmpToken)){
            //User not recognised
            response.sendRedirect("login.html");
            return;
        }
        ToDo toDo = new ToDo(name, msg);
        long toDoId = Store.getInstance().createToDo(toDo);

        if(toDoId < 0){
            ///toDo creation failed todo name already exists
            response.sendRedirect("createtodo.html");
            return;
        }else {
            //succesfully created todo
            response.sendRedirect("main.html");
            return;
        }
    }
    public static void displaytodo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String start = request.getParameter("start");
        Pair<String, String> p = getUser(request);
        String tmpName = p.getKey(), tmpToken = p.getValue();
        if(!Store.getInstance().validateCookie(tmpName, tmpToken)){
            //User not recognised
            response.sendRedirect("login.html");
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.write(Store.getInstance().getToDos(start));
        Store.getInstance().clearTmpData();
        out.flush();
    }
    public static void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        User user = Store.getInstance().findUser(userName);
        if(user == null){
            ///Redirect to login page
            //No user
            response.sendRedirect("login.html");
        }
        else if(user!=null && user.getPassword().equals(password)){
            String token = UUID.randomUUID().toString();
            Store.getInstance().getUserTokenMap().put(token,userName);
            response.addCookie(new Cookie("userName", userName));
            response.addCookie(new Cookie("token", token));
            response.sendRedirect("main.html");
        }else{
            ///Redirect to login page
            //Userpassword wrong
            response.sendRedirect("login.html");
        }
    }

    public static void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Pair<String, String> p = getUser(request);
        String tmpName = p.getKey(), tmpToken = p.getValue();
        if(!Store.getInstance().validateCookie(tmpName, tmpToken)){
            response.sendRedirect("login.html");
        }

        if(Store.getInstance().clearUserData(tmpName, tmpToken)){
            //Deletion succeded
            response.sendRedirect("login.html");
        }else{
            //Deletion failed
            response.sendRedirect("main.html");
        }
    }
    public static void main(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Pair<String, String> p = getUser(request);
        String tmpName = p.getKey(), tmpToken = p.getValue();
        if(!Store.getInstance().validateCookie(tmpName, tmpToken)){
            response.sendRedirect("/login.html");
            return;
        }
    }
    public static void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        String emailId = request.getParameter("email");


        System.out.println(userName);
        User user = new User(userName, password, emailId);
        long userId = Store.getInstance().createUser(user);
        if(userId < 0){
            //User already exits
            //Redirect to register page
            response.sendRedirect("register.html");
        }else{
            /// User succesfully created
            ///Redirect to main servlet
            System.out.println(user.getUserName());
            response.sendRedirect("login.html");

        }
    }



}
