package objects;

public class User {
    private long userId = -1;
    private String userName = "";
    private String password = "";
    private String emailId = "";

    public User(String userName, String password, String email){
        this.userName = userName;
        this.password = password;
        this.emailId = email;
    }

    public long getUserId(){return this.userId;}

    public String getUserName() {
        return this.userName;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailId() {
        return this.emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
