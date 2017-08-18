package services;

import javafx.util.Pair;
import objects.Store;
import objects.ToDo;
import objects.User;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;
import constants.Constants;

import static services.Utility.getUser;

public class ToDoServices {
    public static void assigntodo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter(Constants.ID.getValue()));
        String toDoStatus = Constants.UNASSIGNED.getValue();

        Pair<String, String> p = getUser(request);
        String tmpName = p.getKey(), tmpToken = p.getValue();


        if(!Store.getInstance().validateCookie(tmpName, tmpToken)){
            //User not recognised
            response.sendRedirect(Constants.LOGINPAGE.getValue());
            return;
        }

        if(Store.getInstance().assignToDo(id, toDoStatus, tmpName)){
            response.sendRedirect(Constants.MAINPAGE.getValue());
            return;
        }else {
            response.sendRedirect(Constants.MAINPAGE.getValue());
            return;
        }
    }
    public static void deletetodo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String id = request.getParameter(Constants.ID.getValue());
        String[] arr = id.split(":");
        if(arr.length < 2) response.sendRedirect(Constants.MAINPAGE.getValue());
        //long toDoId = Long.parseLong(request.getParameter("toDoId"));
        Pair<String, String> p = getUser(request);
        String tmpName = p.getKey(), tmpToken = p.getValue();
        if(!Store.getInstance().validateCookie(tmpName, tmpToken)){
            //User not recogized
            response.sendRedirect(Constants.LOGINPAGE.getValue());
        }

        if(Store.getInstance().deletetodo(Long.parseLong(arr[0]), arr[1], tmpName)){
            //Deletion succeded
            response.sendRedirect(Constants.MAINPAGE.getValue());
        }else{
            //Deletion failed
            response.sendRedirect(Constants.MAINPAGE.getValue());
        }
    }

    public static void completetodo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter(Constants.ID.getValue()));
        Pair<String, String> p = getUser(request);
        String tmpName = p.getKey(), tmpToken = p.getValue();

        if(!Store.getInstance().validateCookie(tmpName, tmpToken)){
            //User not recognised
            response.sendRedirect(Constants.LOGINPAGE.getValue());
            return;
        }

        if(Store.getInstance().completeToDo(id, Constants.ASSIGNED.getValue(), tmpName)){
            ///toDo assigned
            //redirect to main
            response.sendRedirect(Constants.MAINPAGE.getValue());
            return;
        }else {
            //failed to assign
            response.sendRedirect(Constants.MAINPAGE.getValue());
            return;
        }
    }
    public static void createtodo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String name = request.getParameter(Constants.TODONAME.getValue());
        String msg = request.getParameter(Constants.TODOMSG.getValue());

        Pair<String, String> p = getUser(request);
        String tmpName = p.getKey(), tmpToken = p.getValue();


        if(!Store.getInstance().validateCookie(tmpName, tmpToken)){
            //User not recognised
            response.sendRedirect(Constants.LOGINPAGE.getValue());
            return;
        }
        ToDo toDo = new ToDo(name, msg);
        long toDoId = Store.getInstance().createToDo(toDo);

        if(toDoId < 0){
            ///toDo creation failed todo name already exists
            response.sendRedirect(Constants.CREATETODOPAGE.getValue());
            return;
        }else {
            //succesfully created todo
            response.sendRedirect(Constants.MAINPAGE.getValue());
            return;
        }
    }
    public static void displaytodo(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String start = request.getParameter(Constants.START.getValue());
        Pair<String, String> p = getUser(request);
        String tmpName = p.getKey(), tmpToken = p.getValue();
        if(!Store.getInstance().validateCookie(tmpName, tmpToken)){
            //User not recognised
            response.sendRedirect(Constants.LOGINPAGE.getValue());
        }

        response.setContentType(Constants.JSON.getValue());
        PrintWriter out = response.getWriter();
        out.write(Store.getInstance().getToDos(start));
        Store.getInstance().clearTmpData();
        out.flush();
    }
}
