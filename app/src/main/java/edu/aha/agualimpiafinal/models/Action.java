package edu.aha.agualimpiafinal.models;

public class Action {

    private String id_token;
    private String id;
    private String token;
    private boolean status;
    private String type;

    public Action() {
    }

    public Action(String id_token, String id, String token, boolean status, String type) {
        this.id_token = id_token;
        this.id = id;
        this.token = token;
        this.status = status;
        this.type = type;
    }

    public String getId_token() {
        return id_token;
    }

    public void setId_token(String id_token) {
        this.id_token = id_token;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
