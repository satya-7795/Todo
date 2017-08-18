package servlets;

import objects.Store;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class DisplayToDo extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        /*String toDoStatus = request.getParameter("status");
        //String toDoStatus = "unassigned";

        long toDoId = Long.parseLong(request.getParameter("toDoId"));
        String tmpName = "", tmpToken = "";
        Cookie cookies[] = request.getCookies();
        for(Cookie i : cookies){
            if(i.getName().equals("userName")){
                tmpName = i.getValue();
            }else if(i.getName().equals("token")){
                tmpToken = i.getValue();
            }
        }
        if(!Store.getInstance().validateCookie(tmpName, tmpToken)){
            //User not recognised
            response.sendRedirect("login.html");
        }

        response.setContentType("application/json");
        PrintWriter out = response.getWriter();
        out.print(Store.getInstance().getToDo(toDoId, toDoStatus));
        out.flush();*/
    }
}
