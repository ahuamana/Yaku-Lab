package edu.aha.agualimpiafinal.models;

public class Comment {

    private String id;
    private String id_photo;
    private String token;
    private String type;
    private String message;
    private boolean status;
    private long timestamp;

    public Comment() {
    }

    public Comment(String id, String id_photo, String token, String type, String message, boolean status, long timestamp) {
        this.id = id;
        this.id_photo = id_photo;
        this.token = token;
        this.type = type;
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId_photo() {
        return id_photo;
    }

    public void setId_photo(String id_photo) {
        this.id_photo = id_photo;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
