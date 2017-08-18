import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EditToDo extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long id = Long.parseLong(request.getParameter("id"));
        String name = request.getParameter("todoName");
        String msg = request.getParameter("todoMsg");
        String toDoStatus = request.getParameter("status");
        String username = request.getParameter("userName");


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

        if(Store.getInstance().editTodo(id, name, msg, username, toDoStatus)){
            ///toDo edited
            //redirect to View todo page
        }else {
            //failed to edit
            //redirect to View todo page
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }
}
