package org.example.pojo.courierlogin;

import org.example.pojo.RequestBody;

public class CourierLoginRequest implements RequestBody {
    private String login;
    private String password;

    public CourierLoginRequest() {
    }

    public CourierLoginRequest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public String getPassword() {
        return password;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
