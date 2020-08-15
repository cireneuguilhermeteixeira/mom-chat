package sample.app.principal.chat;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import sample.app.principal.PrincipalController;
import sample.models.Contato;
import sample.models.Mensagem;
import sample.services.mom.ProdutorActiveMq;
import sample.services.socket.ClientSocketChat;

import javax.jms.JMSException;
import java.util.ArrayList;


public class ChatController {

    @FXML public AnchorPane rootAnchorPane;
    private ArrayList<Mensagem> mensagensDaConversaAtual = new ArrayList<>();
    private PrincipalController main;
    private Boolean socketConectado = false;
    private String celularAtualDaConversa = "";
    public void init(PrincipalController principalController) {
        main = principalController;
    }
    public PrincipalController getPrincipalController() {
        return main;
    }



    public void enviarMensagemParaChat(Mensagem mensagem){
        enviaMensagemViaSocketOuFila(mensagem);
        montaMensagemEnviadaNaView(mensagem, mensagensDaConversaAtual.size()-1);
    }


    public void iniciaConversaComContato(Contato contato){
        celularAtualDaConversa = contato.getCelular();
        atualizaConversa(contato);
    }

    public String getCelularAtualDaConversa() {
        return celularAtualDaConversa;
    }


    private void atualizaConversa(Contato contato){
        rootAnchorPane.getChildren().clear();
        Label label = new Label();
        label.setPrefHeight(17);
        label.setPrefWidth(261);
        label.setTextFill(javafx.scene.paint.Color.valueOf("#656262"));
        label.setText("Você está falando com: "+ contato.getNome());
        rootAnchorPane.getChildren().add(label);

        mensagensDaConversaAtual = main.getTelefonesComListaMensagens().get(getCelularAtualDaConversa());
        if(mensagensDaConversaAtual!=null && !mensagensDaConversaAtual.isEmpty()){
            int i = 0;
            for (Mensagem mensagem: mensagensDaConversaAtual) {
                if(mensagem.getContato().getCelular().equals(contato.getCelular())){
                    montarMensagemRecebidaNaView(mensagem, i);
                }else if(mensagem.getContato().getCelular().equals(main.getUsuarioAtual().getCelular())){
                    montaMensagemEnviadaNaView(mensagem, i);
                }
                i++;
            }
        }else{
            mensagensDaConversaAtual = new ArrayList<>();
        }
    }


    public ArrayList<Mensagem> getMensagensDaConversaAtual() {
        return mensagensDaConversaAtual;
    }



    public void setMensagensDaConversaAtual(ArrayList<Mensagem> mensagensDaConversaAtual) {
        this.mensagensDaConversaAtual = mensagensDaConversaAtual;
    }

    private void montaMensagemDefault(Mensagem mensagem, ScrollPane scrollPane, int index) {
        scrollPane.setLayoutY(20 + (130 * index));
        scrollPane.setPrefHeight(95);
        scrollPane.setPrefWidth(350);

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setMinHeight(0);
        anchorPane.setMinWidth(0);
        anchorPane.setPrefHeight(90);
        anchorPane.setPrefHeight(345);

        Text text = new Text();
        text.setFill(javafx.scene.paint.Color.valueOf("#656262"));
        text.setLayoutX(14);
        text.setLayoutY(40);
        text.setStrokeType(javafx.scene.shape.StrokeType.valueOf("OUTSIDE"));
        text.setStrokeWidth(0);
        text.setWrappingWidth(350);
        text.setText(mensagem.getContato().getNome()+" ("+mensagem.getContato().getCelular()+") - "+ mensagem.getConteudo());


        anchorPane.getChildren().add(text);
        scrollPane.setContent(anchorPane);
        Platform.runLater(() -> {
            rootAnchorPane.getChildren().add(scrollPane);
        });
    }




    public void montarMensagemRecebidaNaView(Mensagem mensagem, int index){
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setLayoutX(20);
        montaMensagemDefault(mensagem, scrollPane, index);
    }




    private void montaMensagemEnviadaNaView(Mensagem mensagem, int index){
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setLayoutX(400);
        montaMensagemDefault(mensagem, scrollPane, index);
    }



    private void enviaMensagemViaSocketOuFila(Mensagem mensagem) {
        if(temSocketConectado()){
            enviaMensagemViaSocket(mensagem);
        }else{
            enviaMensagemFilaApacheActiveMq(mensagem);
        }
    }



    private void enviaMensagemViaSocket(Mensagem mensagem){
        System.out.println("Enviando Mensagem Via socket");
        try {
            ClientSocketChat.enviaMensagemSocket(mensagem);
        } catch(Exception e) {
            enviaMensagemFilaApacheActiveMq(mensagem);
            this.desconectar();
            main.getSendTextController().setStatusConexao("Você está Offline");
            System.out.println("Erro: " + e.getMessage());
        }
    }


    private void enviaMensagemFilaApacheActiveMq(Mensagem mensagem)  {
        try {
            System.out.println("Enviando Mensagem Via ActiveMq");
            ProdutorActiveMq.send(mensagem);
        }catch (JMSException exception){
            exception.printStackTrace();
        }
    }


    public boolean temSocketConectado(){
        return socketConectado;
    }

    public void conectar(){

        main.restartThreadActiveMq();
        //main.restartThreadSocket();
        socketConectado = true;
        //TODO conecte com socket
    }

    public void desconectar(){
        //TODO desconecte-se
        //main.stopThreadActiveMq();
        //main.stopThreadSocket();
        socketConectado = false;

    }

}