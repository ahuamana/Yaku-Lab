package edu.aha.agualimpiafinal.modulos.login.model;

public class User2 {

    private String author_nombres;
    private String author_apellidos;
    private String author_alias;
    private String author_email;
    private int points;

    public User2() {
    }

    public User2(String author_nombres, String author_apellidos, String author_alias, String author_email, int points, String token) {
        this.author_nombres = author_nombres;
        this.author_apellidos = author_apellidos;
        this.author_alias = author_alias;
        this.author_email = author_email;
        this.points = points;
    }

    public String getAuthor_nombres() {
        return author_nombres;
    }

    public void setAuthor_nombres(String author_nombres) {
        this.author_nombres = author_nombres;
    }

    public String getAuthor_apellidos() {
        return author_apellidos;
    }

    public void setAuthor_apellidos(String author_apellidos) {
        this.author_apellidos = author_apellidos;
    }

    public String getAuthor_alias() {
        return author_alias;
    }

    public void setAuthor_alias(String author_alias) {
        this.author_alias = author_alias;
    }

    public String getAuthor_email() {
        return author_email;
    }

    public void setAuthor_email(String author_email) {
        this.author_email = author_email;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
