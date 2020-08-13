package sample.services.socket;

import sample.models.Mensagem;

import javax.swing.*;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Date;

public class ClientSocketChat {

    public static void enviaMensagemSocket(Mensagem mensagem) throws Exception{

        Socket cliente = new Socket("localhost",12341);
        ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
        Date data_atual = (Date)entrada.readObject();


        //JOptionPane.showMessageDialog(null,"Data recebida do servidor:" + data_atual.toString());
        entrada.close();
        System.out.println("Conexão encerrada");

    }
}
