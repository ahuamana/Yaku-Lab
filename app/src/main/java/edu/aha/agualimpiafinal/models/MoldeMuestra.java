package edu.aha.agualimpiafinal.models;

public class MoldeMuestra {

    private String AuthorEmail;
    private String AuthorFirstname;
    private String AuthorLastname;
    private String AuthorAlias;

    private String MuestraCantidad;
    private String MuestraDepartamento;

    private String MuestraFotoPATH;

    private double MuestraLatitud;
    private double MuestraLongitud;

    private String MuestraProvincia;
    private String MuestraResultado;
    private long MuestraTimeStamp;


    public MoldeMuestra (){

    }

    public MoldeMuestra(String authorEmail, String authorFirstname, String authorLastname, String authorAlias, String muestraCantidad, String muestraDepartamento, String muestraFotoPATH, double muestraLatitud, double muestraLongitud, String muestraProvincia, String muestraResultado, long muestraTimeStamp) {
        AuthorEmail = authorEmail;
        AuthorFirstname = authorFirstname;
        AuthorLastname = authorLastname;
        AuthorAlias = authorAlias;
        MuestraCantidad = muestraCantidad;
        MuestraDepartamento = muestraDepartamento;
        MuestraFotoPATH = muestraFotoPATH;
        MuestraLatitud = muestraLatitud;
        MuestraLongitud = muestraLongitud;
        MuestraProvincia = muestraProvincia;
        MuestraResultado = muestraResultado;
        MuestraTimeStamp = muestraTimeStamp;
    }

    public String getAuthorEmail() {
        return AuthorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        AuthorEmail = authorEmail;
    }

    public String getAuthorFirstname() {
        return AuthorFirstname;
    }

    public void setAuthorFirstname(String authorFirstname) {
        AuthorFirstname = authorFirstname;
    }

    public String getAuthorLastname() {
        return AuthorLastname;
    }

    public void setAuthorLastname(String authorLastname) {
        AuthorLastname = authorLastname;
    }

    public String getAuthorAlias() {
        return AuthorAlias;
    }

    public void setAuthorAlias(String authorAlias) {
        AuthorAlias = authorAlias;
    }

    public String getMuestraCantidad() {
        return MuestraCantidad;
    }

    public void setMuestraCantidad(String muestraCantidad) {
        MuestraCantidad = muestraCantidad;
    }

    public String getMuestraDepartamento() {
        return MuestraDepartamento;
    }

    public void setMuestraDepartamento(String muestraDepartamento) {
        MuestraDepartamento = muestraDepartamento;
    }

    public String getMuestraFotoPATH() {
        return MuestraFotoPATH;
    }

    public void setMuestraFotoPATH(String muestraFotoPATH) {
        MuestraFotoPATH = muestraFotoPATH;
    }

    public double getMuestraLatitud() {
        return MuestraLatitud;
    }

    public void setMuestraLatitud(double muestraLatitud) {
        MuestraLatitud = muestraLatitud;
    }

    public double getMuestraLongitud() {
        return MuestraLongitud;
    }

    public void setMuestraLongitud(double muestraLongitud) {
        MuestraLongitud = muestraLongitud;
    }

    public String getMuestraProvincia() {
        return MuestraProvincia;
    }

    public void setMuestraProvincia(String muestraProvincia) {
        MuestraProvincia = muestraProvincia;
    }

    public String getMuestraResultado() {
        return MuestraResultado;
    }

    public void setMuestraResultado(String muestraResultado) {
        MuestraResultado = muestraResultado;
    }

    public long getMuestraTimeStamp() {
        return MuestraTimeStamp;
    }

    public void setMuestraTimeStamp(long muestraTimeStamp) {
        MuestraTimeStamp = muestraTimeStamp;
    }
}
