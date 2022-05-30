package edu.aha.agualimpiafinal.models;

public class MoldeComentarios {

    private String authorEmail;
    private String authorFirstname;
    private String authorLastname;
    private String authorAlias;

    private long sugerenciaFechaUnixtime;
    private String sugerenciaMensaje;

    public MoldeComentarios() {
    }

    public MoldeComentarios(String authorEmail, String authorFirstname, String authorLastname, String authorAlias, long sugerenciaFechaUnixtime, String sugerenciaMensaje) {
        this.authorEmail = authorEmail;
        this.authorFirstname = authorFirstname;
        this.authorLastname = authorLastname;
        this.authorAlias = authorAlias;
        this.sugerenciaFechaUnixtime = sugerenciaFechaUnixtime;
        this.sugerenciaMensaje = sugerenciaMensaje;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public String getAuthorFirstname() {
        return authorFirstname;
    }

    public void setAuthorFirstname(String authorFirstname) {
        this.authorFirstname = authorFirstname;
    }

    public String getAuthorLastname() {
        return authorLastname;
    }

    public void setAuthorLastname(String authorLastname) {
        this.authorLastname = authorLastname;
    }

    public String getAuthorAlias() {
        return authorAlias;
    }

    public void setAuthorAlias(String authorAlias) {
        this.authorAlias = authorAlias;
    }

    public long getSugerenciaFechaUnixtime() {
        return sugerenciaFechaUnixtime;
    }

    public void setSugerenciaFechaUnixtime(long sugerenciaFechaUnixtime) {
        this.sugerenciaFechaUnixtime = sugerenciaFechaUnixtime;
    }

    public String getSugerenciaMensaje() {
        return sugerenciaMensaje;
    }

    public void setSugerenciaMensaje(String sugerenciaMensaje) {
        this.sugerenciaMensaje = sugerenciaMensaje;
    }
}
