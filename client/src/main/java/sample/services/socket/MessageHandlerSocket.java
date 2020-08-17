package sample.services.socket;

import javafx.application.Platform;
import sample.app.principal.PrincipalController;
import sample.models.Contato;
import sample.models.Mensagem;
import sample.models.MensagemSocket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;

public class MessageHandlerSocket {

    private PrincipalController principalController;
    private Socket clientSocket;
    private ObjectInputStream clientInput;
    private ObjectOutputStream clientOutput;
    private Boolean run  = true;
    public MessageHandlerSocket(Socket clientSocket, PrincipalController pc){
        principalController = pc;
        this.clientSocket = clientSocket;
        try {
            clientOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            clientInput = new ObjectInputStream(clientSocket.getInputStream());

            listenSocket();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void listenSocket(){
        new Thread(() -> {
            while (this.run){
                try{
                    MensagemSocket msg = (MensagemSocket)clientInput.readObject();
                    if(msg != null){
                        if(msg.getTipo().equalsIgnoreCase("lista_contato")){
                            System.out.println("Obtendo status Usuários");
                            principalController.getListContactsController().setMeusContatos((ArrayList<Contato>) msg.getConteudo());
                            Platform.runLater(() -> {
                                principalController.getListContactsController().montaContatosPadroes();
                            });
                        }


                    }
                } catch (Exception e) {
                    principalController.ficarOffline();
                    e.printStackTrace();

                }
            }
        }).start();
    }


    private void sendMessageThroughSocket(MensagemSocket msg){
        try {
            clientOutput.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void obtemStatusContatos(){
        MensagemSocket mensagemSocket = new MensagemSocket();
        mensagemSocket.setTipo("obter_status_contatos");
        sendMessageThroughSocket(mensagemSocket);
    }


    public void ficarOnline(Contato contato){
        MensagemSocket mensagemSocket = new MensagemSocket();
        mensagemSocket.setTipo("ficar_online");
        mensagemSocket.setConteudo(contato);
        sendMessageThroughSocket(mensagemSocket);
    }

    public void ficarOffline(Contato contato){
        MensagemSocket mensagemSocket = new MensagemSocket();
        mensagemSocket.setTipo("ficar_offline");
        mensagemSocket.setConteudo(contato);
        sendMessageThroughSocket(mensagemSocket);
    }
    public void setRun(Boolean bool){
        this.run = bool;
    }
}
