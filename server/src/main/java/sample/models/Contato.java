package sample.models;

import java.io.Serializable;

public class Contato implements Serializable {

    private String nome;
    private String celular;
    private String statusConexao;

    public String getStatusConexao() {
        return statusConexao;
    }

    public String getNome() {
        return nome;
    }

    public String getCelular() {
        return celular;
    }


    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public void setStatusConexao(String statusConexao) {
        this.statusConexao = statusConexao;
    }
}
