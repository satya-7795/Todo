package services;

import constants.Constants;
import javafx.util.Pair;
import objects.Store;
import objects.User;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

import static services.Utility.getUser;

public class UserAuthenticationServices {
    public static void login(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter(Constants.USERNAME.getValue());
        String password = request.getParameter(Constants.PASSWORD.getValue());
        User user = Store.getInstance().findUser(userName);
        if(user == null){
            ///Redirect to login page
            //No user
            response.sendRedirect(Constants.LOGINPAGE.getValue());
        }
        else if(user!=null && user.getPassword().equals(password)){
            String token = UUID.randomUUID().toString();
            Store.getInstance().getUserTokenMap().put(token,userName);
            response.addCookie(new Cookie(Constants.USERNAME.getValue(), userName));
            response.addCookie(new Cookie(Constants.TOKEN.getValue(), token));
            response.sendRedirect(Constants.MAINPAGE.getValue());
        }else{
            ///Redirect to login page
            //Userpassword wrong
            response.sendRedirect(Constants.LOGINPAGE.getValue());
        }
    }

    public static void logout(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Pair<String, String> p = getUser(request);
        String tmpName = p.getKey(), tmpToken = p.getValue();
        if(!Store.getInstance().validateCookie(tmpName, tmpToken)){
            response.sendRedirect(Constants.LOGINPAGE.getValue());
        }

        if(Store.getInstance().clearUserData(tmpName, tmpToken)){
            //Deletion succeded
            response.sendRedirect(Constants.LOGINPAGE.getValue());
        }else{
            //Deletion failed
            response.sendRedirect(Constants.MAINPAGE.getValue());
        }
    }
    public static void register(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String userName = request.getParameter(Constants.USERNAME.getValue());
        String password = request.getParameter(Constants.PASSWORD.getValue());
        String emailId = request.getParameter(Constants.EMAIL.getValue());


        System.out.println(userName);
        User user = new User(userName, password, emailId);
        long userId = Store.getInstance().createUser(user);
        if(userId < 0){
            //User already exits
            //Redirect to register page
            response.sendRedirect(Constants.REGISTERPAGE.getValue());
        }else{
            /// User succesfully created
            ///Redirect to main servlet
            System.out.println(user.getUserName());
            response.sendRedirect(Constants.LOGINPAGE.getValue());

        }
    }
}
