package edu.aha.agualimpiafinal.modulos.login.model;

public class User {

    private String author_email;
    private String author_firstname;
    private String author_lastname;
    private String author_alias;
    private int points;
    private String token;

    public User() {
    }

    public User(String author_email, String author_firstname, String author_lastname, String author_alias, int points, String token) {
        this.author_email = author_email;
        this.author_firstname = author_firstname;
        this.author_lastname = author_lastname;
        this.author_alias = author_alias;
        this.points = points;
        this.token = token;
    }

    public String getAuthor_email() {
        return author_email;
    }

    public void setAuthor_email(String author_email) {
        this.author_email = author_email;
    }

    public String getAuthor_firstname() {
        return author_firstname;
    }

    public void setAuthor_firstname(String author_firstname) {
        this.author_firstname = author_firstname;
    }

    public String getAuthor_lastname() {
        return author_lastname;
    }

    public void setAuthor_lastname(String author_lastname) {
        this.author_lastname = author_lastname;
    }

    public String getAuthor_alias() {
        return author_alias;
    }

    public void setAuthor_alias(String author_alias) {
        this.author_alias = author_alias;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
