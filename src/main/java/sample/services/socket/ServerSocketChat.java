package sample.services.socket;

import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class ServerSocketChat implements Runnable{

    private static Integer PORTA;
    private boolean run;


    public ServerSocketChat(String fimDigitoPorta){
        PORTA = Integer.parseInt("1234"+fimDigitoPorta);
        run = true;
    }

    @Override
    public void run() {
        try {
            // Instancia o ServerSocket ouvindo a porta 12345
            ServerSocket servidor = new ServerSocket(PORTA);
            System.out.println("Servidor ouvindo a porta "+ PORTA);

            while(this.run) {
                // o método accept() bloqueia a execução até que
                // o servidor receba um pedido de conexão
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
                ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
                saida.flush();
                saida.writeObject(new Date());
                saida.close();
                cliente.close();
            }
        }
        catch(Exception e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }


    public void setRunValue(boolean value){
        this.run = value;
    }
}