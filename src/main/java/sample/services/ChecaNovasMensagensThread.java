package sample.services;

import sample.app.principal.chat.ChatController;
import sample.models.Contato;
import sample.models.Mensagem;
import sample.services.mom.ConsumidorActiveMq;

import javax.jms.*;

public class ChecaNovasMensagensThread implements Runnable {

    private ChatController chatController;
    private boolean run;

    public ChecaNovasMensagensThread(ChatController controller){
        this.chatController = controller;
        run = true;
    }


    @Override
    synchronized public void run() {
        while(this.run){
            try {

                System.out.println("Thread rodando");
                TextMessage resposta = (TextMessage) ConsumidorActiveMq.receive(chatController.getPrincipalController().getUsuarioAtual().getCelular());
                Mensagem mensagem = new Mensagem();
                mensagem = mensagem.convertStringToObject(resposta.getText());
                System.out.println("Messagem: "+ mensagem.getConteudo()+", recebida na Fila");
                Thread.sleep(2000);
                Contato contato = new Contato();
                contato.setCelular("0000000");
                contato.setNome("Usuário 0");
                mensagem.setContato(contato);

                chatController.getPrincipalController().tratarMensagemRecebidaENotificarUsuario(mensagem);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setRunValue(boolean value){
        this.run = value;
    }
}
