package org.example.pojo.acceptorder;

public class AcceptOrderResponse {
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
