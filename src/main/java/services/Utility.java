package services;

import javafx.util.Pair;
import objects.*;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.UUID;
import constants.Constants;

public class Utility {
    private static AbstractDataStore getContainer(){
        if(Constants.USEDB.getValue().equals("true")){
            return DB.getInstance();
        }else{
            return Store.getInstance();
        }
    }
    public  static Pair<String, String> getUser(HttpServletRequest request){
        String tmpName = "", tmpToken = "";
        Cookie cookies[] = request.getCookies();
        for(Cookie i : cookies){
            if(i.getName().equals(Constants.USERNAME.getValue())){
                tmpName = i.getValue();
            }else if(i.getName().equals(Constants.TOKEN.getValue())){
                tmpToken = i.getValue();
            }
        }
        return new Pair<String, String>(tmpName, tmpToken);
    }

    public static void main(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Pair<String, String> p = getUser(request);
        String tmpName = p.getKey(), tmpToken = p.getValue();
        if(!getContainer().validateCookie(tmpName, tmpToken)){
            response.sendRedirect(Constants.LOGINPAGE.getValue());
            return;
        }
    }

}
