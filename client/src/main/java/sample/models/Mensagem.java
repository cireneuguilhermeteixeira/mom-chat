package sample.models;

import sample.models.Contato;

import java.io.Serializable;
import java.util.Timer;

public class Mensagem implements Serializable {

    private String conteudo;
    private String celularReceber;
    private Contato contato;
    private Long currentTimeMillis;


    public Long getCurrentTimeMillis() {
        return currentTimeMillis;
    }

    public String getCelularReceber() {
        return celularReceber;
    }

    public String getConteudo() {
        return conteudo;
    }


    public Contato getContato() {
        return contato;
    }

    public void setConteudo(String conteudo) {
        this.conteudo = conteudo;
    }

    public void setCelularReceber(String celularReceber) {
        this.celularReceber = celularReceber;
    }

    public void setContato(Contato contato) {
        this.contato = contato;
    }

    public void setCurrentTimeMillis(Long currentTimeMillis) {
        this.currentTimeMillis = currentTimeMillis;
    }

    public String convertToString(){
        return  conteudo+"/"+celularReceber+"/"+contato.getNome()+"/"+contato.getCelular()+"/"+currentTimeMillis;
    }

    public Mensagem convertStringToObject(String string){
        String[] strings = string.split("/");
        Mensagem mensagem = new Mensagem();
        Contato contato = new Contato();


        mensagem.setConteudo(strings[0]);
        mensagem.setCelularReceber(strings[1]);
        contato.setNome(strings[2]);
        contato.setCelular(strings[3]);
        mensagem.setContato(contato);
        mensagem.setCurrentTimeMillis(Long.parseLong(strings[4]));

        return mensagem;
    }
}
