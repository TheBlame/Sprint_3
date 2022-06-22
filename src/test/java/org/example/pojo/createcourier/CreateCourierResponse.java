package org.example.pojo.createcourier;

public class CreateCourierResponse {
    private boolean ok;
    private String message;

    public boolean isOk() {
        return ok;
    }

    public String getMessage() {
        return message;
    }

    public void setOk(boolean ok) {
        this.ok = ok;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
