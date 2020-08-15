package sample.app.principal;

import javafx.fxml.FXML;
import javafx.stage.Stage;
import sample.Main;
import sample.app.principal.chat.ChatController;
import sample.app.principal.listcontacts.ListContactsController;

import sample.app.principal.sendtext.SendTextController;
import sample.models.Contato;
import sample.models.Mensagem;
import sample.services.ChecaNovasMensagensThread;
import sample.services.socket.ServerSocketChat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Logger;

public class PrincipalController {

    Logger logger = Logger.getLogger(PrincipalController.class.getName());
    private Contato usuarioAtual = new Contato();
    @FXML ChatController chatController;
    @FXML SendTextController sendTextController;
    @FXML ListContactsController listContactsController;
    private HashMap<String, ArrayList<Mensagem>> telefonesComListaMensagens = new HashMap<>();
    private ChecaNovasMensagensThread threadActiveMq;
    private ServerSocketChat threadSocket;
    public ChatController getChatController() {
        return chatController;
    }
    public SendTextController getSendTextController() {
        return sendTextController;
    }
    public ListContactsController getListContactsController(){
        return listContactsController;
    }
    private Stage principalStage;


    @FXML public void initialize(){
        System.out.println("Application started");
        Main.addOnChangeScreenListener(new Main.OnChangeSceen() {
            @Override
            public void onScreenChanged(String newScreen, Object data, Stage stage) {
                principalStage = stage;
                System.out.println("nova tela :"+newScreen+", "+ data);
                usuarioAtual = (Contato) data;
                iniciarControllers();
                //iniciaSocketServer();
                iniciaThreadVerificaNovasMensagensActiveMq();

            }
        });
    }




//    public void stopThreadSocket(){
//        threadActiveMq.setRunValue(false);
//    }


//    public void restartThreadSocket(){
//        iniciaSocketServer();
//    }


//    private void iniciaSocketServer(){
//        threadSocket = new ServerSocketChat("1");
//        Thread serviceSocket = new Thread(threadSocket);
//        serviceSocket.start();
//
//    }


    public void stopThreadActiveMq(){
        threadActiveMq.setRunValue(false);
    }


    public void restartThreadActiveMq(){
        iniciaThreadVerificaNovasMensagensActiveMq();
    }


    private void iniciaThreadVerificaNovasMensagensActiveMq(){
        threadActiveMq = new ChecaNovasMensagensThread(getChatController());
        Thread serviceChecaFila = new Thread(threadActiveMq);
        serviceChecaFila.start();
    }



    public void iniciarControllers(){
        sendTextController.init(this);
        chatController.init(this);
        listContactsController.init(this);
    }


    public Contato getUsuarioAtual() {
        return usuarioAtual;
    }


    public void  tratarMensagem(Mensagem mensagem, String indentificadorFila){

        if(telefonesComListaMensagens.containsKey(indentificadorFila)){
            telefonesComListaMensagens.get(indentificadorFila).add(mensagem);
        }else {
            ArrayList<Mensagem> mensagens = new ArrayList<>();
            mensagens.add(mensagem);
            telefonesComListaMensagens.put(indentificadorFila,mensagens);
        }
        getChatController().setMensagensDaConversaAtual(telefonesComListaMensagens.get(indentificadorFila));
    }


    public HashMap<String, ArrayList<Mensagem>> getTelefonesComListaMensagens() {
        return telefonesComListaMensagens;
    }

    public void tratarMensagemRecebidaENotificarUsuario(Mensagem mensagem){
        tratarMensagem(mensagem, mensagem.getContato().getCelular());
        //getChatController().getMensagensDaConversaAtual().add(mensagem);
        getChatController().montarMensagemRecebidaNaView(mensagem, getChatController().getMensagensDaConversaAtual().size()-1);

    }
}



