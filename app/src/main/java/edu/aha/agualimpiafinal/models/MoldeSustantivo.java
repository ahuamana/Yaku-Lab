package edu.aha.agualimpiafinal.models;

public class MoldeSustantivo {

    private String id;
    private String name;
    private String tipo;
    private String url;
    private String author_email;
    private String author_name;
    private String author_lastname;
    private long timestamp;

    public MoldeSustantivo() {
    }

    public MoldeSustantivo(String id, String name, String tipo, String url, String author_email, String author_name, String author_lastname, long timestamp) {
        this.id = id;
        this.name = name;
        this.tipo = tipo;
        this.url = url;
        this.author_email = author_email;
        this.author_name = author_name;
        this.author_lastname = author_lastname;
        this.timestamp = timestamp;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getAuthor_email() {
        return author_email;
    }

    public void setAuthor_email(String author_email) {
        this.author_email = author_email;
    }

    public String getAuthor_name() {
        return author_name;
    }

    public void setAuthor_name(String author_name) {
        this.author_name = author_name;
    }

    public String getAuthor_lastname() {
        return author_lastname;
    }

    public void setAuthor_lastname(String author_lastname) {
        this.author_lastname = author_lastname;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
