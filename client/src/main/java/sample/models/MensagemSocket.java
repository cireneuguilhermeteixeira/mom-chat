package sample.models;

import java.io.Serializable;

public class MensagemSocket implements Serializable {

    private String tipo;
    private Mensagem conteudo;

    public Mensagem getConteudo() {
        return conteudo;
    }

    public void setConteudo(Mensagem conteudo) {
        this.conteudo = conteudo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
