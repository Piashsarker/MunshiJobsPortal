package dcastalia.com.munshijobsportal.Model;

/**
 * Created by nusrat-pc on 10/20/16.
 */

public class UserInfo {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    private String name;
    private String phone;


    public UserInfo(String name, String phone){
        this.name=name;
        this.phone=phone;

    }

}
