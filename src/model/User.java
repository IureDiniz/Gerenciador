package model;

public class User {
    private int id;
    private String login;
    private String senha;
    private String email;
    private String tipo;
    private boolean ativo;


    public int getId() {
        return id;
    }

    public String getLogin() {
        return login;
    }

    public String getSenha() {
        return senha;
    }

    public String getEmail() {
        return email;
    }

    public String getTipo() {
        return tipo;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public User(int id, String login, String senha, String email, String tipo, boolean ativo){
        this.id = id;
        this.login = login;
        this.senha = senha;
        this.email = email;
        this.tipo = tipo;
        this.ativo = ativo;
    }


    @Override
    public String toString(){
        return  "ID - " + this.id + "\n" +
                "Login - " + this.login + "\n" +
                "Senha - " + this.senha + "\n" +
                "Email - " + this.email + "\n" +
                "Tipo - " + this.tipo + "\n" +
                "Ativo - " + this.ativo;
    }
}
