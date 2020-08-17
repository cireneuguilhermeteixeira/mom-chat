package sample;

import sample.models.Contato;
import sample.models.MensagemSocket;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClientSideServer implements Runnable{
    private Socket clientSocket;
    private ObjectInputStream clientInput;
    private ObjectOutputStream clientOutput;
    private Server server;


    public ClientSideServer(Server server, Socket clientSocket){
        this.server = server;
        this.clientSocket = clientSocket;
        try {
            clientOutput = new ObjectOutputStream(clientSocket.getOutputStream());
            clientInput = new ObjectInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    Socket getClientSocket() {
        return clientSocket;
    }

    @Override
    public void run() {
        MensagemSocket msg;
        boolean clientConnected = true;
        while(clientConnected) {
            try {
                msg = (MensagemSocket) clientInput.readObject();
                if (msg != null) {
                    gerenciaMensagem(msg);
                }
            } catch (Exception e) {
                System.out.println("Cliente " + clientSocket.getInetAddress().getHostAddress() +
                        " desconectou, parando tarefa");
                clientConnected = false;
                server.pararLadoCliente(this);
            }
        }
    }

    private void gerenciaMensagem(MensagemSocket msg){
        Contato contato = new Contato();
        switch (msg.getTipo()){
            case "ficar_online":
                contato = (Contato)msg.getConteudo();
                System.out.println("Informando a o server que o usuário "+ contato.getCelular()+ " está online." );
                server.ficarOnline(this,contato);
                //server.onConnectionSolicitation(handshake.getUserName(), handshake.getAvatarImageName(), this);
                break;
            case "ficar_offline":
                contato = (Contato)msg.getConteudo();
                System.out.println("Informando a o server que o usuário "+ contato.getCelular()+ "está offline." );
                server.ficarOffline(this,contato);
                //server.onConnectionSolicitation(handshake.getUserName(), handshake.getAvatarImageName(), this);
                break;
            case "obter_status_contatos":
                System.out.println("Obtendo contatos pré existentes");
                server.obterStatusContatos(this);
                break;
            default:
                break;
        }
    }



    void sendContactList(List<Contato> contactList){
        MensagemSocket mensagemSocket = new MensagemSocket();
        mensagemSocket.setTipo("lista_contato");
        mensagemSocket.setConteudo(contactList);
        mandaMensagemPeloSocket(mensagemSocket);
    }






    private void mandaMensagemPeloSocket(Object msg){
        try {
            clientOutput.writeObject(msg);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}