package sample.app.principal.sendtext;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import sample.app.principal.PrincipalController;
import sample.models.Contato;
import sample.models.Mensagem;

import java.util.logging.Logger;

public class SendTextController {

    Logger logger = Logger.getLogger(SendTextController.class.getName());
    private PrincipalController main;
    @FXML public AnchorPane anchorPaneFooter;
    @FXML private TextArea textAreaToSend;
    @FXML private Label statusConexao;



    public void init(PrincipalController principalController) {
        main = principalController;
        statusConexao.setText("Você está Online");

    }


    public void setStatusConexao(String statusConexao) {
        this.statusConexao.setText(statusConexao);
    }

    public void mudarStatusConexao(ActionEvent event){
        if(main.getChatController().temSocketConectado()){
            main.getChatController().desconectar();
            setStatusConexao("Você está Offline");
        }else{
            main.getChatController().conectar();
            setStatusConexao("Você está Online");
        }
    }


    public void enviarMensagem(ActionEvent event){

        if(textAreaToSend != null && textAreaToSend.getText()!=null){
            Contato usuarioAtual = main.getUsuarioAtual();
            Mensagem mensagem = new Mensagem();
            mensagem.setContato(usuarioAtual);
            mensagem.setCurrentTimeMillis(System.currentTimeMillis());
            mensagem.setCelularReceber(main.getChatController().getCelularAtualDaConversa());
            mensagem.setConteudo(textAreaToSend.getText());
            enviaMensagemParaChat(mensagem);
            textAreaToSend.setText(null);

        }
    }



    private void enviaMensagemParaChat(Mensagem mensagem){
        this.main.tratarMensagem(mensagem, mensagem.getCelularReceber());
        this.main.getChatController().enviarMensagemParaChat(mensagem);
    }


    public AnchorPane getAnchorPane(){
        return this.anchorPaneFooter;
    }

}