package sample;


import sample.models.Contato;

import java.io.IOException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

public class Server {
    private static final int SERVER_PORT = 5000;
    private ServerSocket serverSocket;
    private ExecutorService pool;
    private HashMap<ClientSideServer, Future> clientHandlerTaskMap;
    private List<Contato> listaContatos;
    private HashMap<Contato, ClientSideServer> clientHandlerMap;


    private void initServer() {
        try {
            System.out.println("Servidor rodando na porta " + SERVER_PORT);
            serverSocket = new ServerSocket(SERVER_PORT);
            pool = Executors.newFixedThreadPool(10);
            clientHandlerTaskMap = new HashMap<>();
            listaContatos = new ArrayList<>();
            clientHandlerMap = new HashMap<>();
        } catch (IOException e) {
            System.out.println("Erro ao tentar iniciar server.");
            e.printStackTrace();
        }
    }

    Server(){};

    void run(){
        initServer();
        criaContatosPreexistentes();
        new Thread(() -> {
            while (true) {
                try {
                    System.out.println("Aguardando conexoes");
                    Socket client = serverSocket.accept();
                    System.out.println("Nova conexao recebida: " + client.getInetAddress().getHostAddress());
                    ClientSideServer clientSideServer = new ClientSideServer(this, client);
                    Future task = pool.submit(clientSideServer);
                    clientHandlerTaskMap.put(clientSideServer, task);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    void pararLadoCliente(ClientSideServer clientSideServer){
        AtomicReference<Contato> c = new AtomicReference<>(new Contato());
                clientHandlerMap.forEach((contato, css) -> {
            if(css.equals(clientSideServer)){
                c.set(contato);
            }
        });
        Contato contato = c.get();
        System.out.println("Parando tarefa para o socket do usuario " + contato.getNome());
        Future clientHandlerTask = clientHandlerTaskMap.get(clientSideServer);
        clientHandlerTask.cancel(true);
        System.out.println("Tarefa para o cliente " + contato.getNome() + " finalizada com sucesso!");

    }










    public void obterStatusContatos(ClientSideServer clientSideServer){

        AtomicReference<Contato> c = new AtomicReference<>(new Contato());
        clientHandlerMap.forEach((contato, css) -> {
            if(css.equals(clientSideServer)){
                c.set(contato);
            }
        });
        Contato requestingClient = c.get();

        System.out.println("Retornando contatos para usuario " + requestingClient.getNome());

        List<Contato> contactList = listaContatos.stream()
                .filter(client -> !client.getCelular().equals(requestingClient.getCelular())).collect(Collectors.toList());
        clientSideServer.sendContactList(contactList);

    }


    public void ficarOnline(ClientSideServer clientSideServer, Contato contato){
        Boolean novoContato = true;
        for (Contato cont: listaContatos ) {
            if  (cont.getCelular().equalsIgnoreCase(contato.getCelular())){
                cont.setStatusConexao("Online");
                novoContato = false;
            }
        }
        if(novoContato){
            contato.setStatusConexao("Online");
            listaContatos.add(contato);
        }
        obterStatusContatos(clientSideServer);
    }

    public void ficarOffline(ClientSideServer clientSideServer, Contato contato){
        for (Contato cont: listaContatos) {
            if  (cont.getCelular().equalsIgnoreCase(contato.getCelular())){
                cont.setStatusConexao("Offline");
            }
        }

        obterStatusContatos(clientSideServer);
    }



    private void criaContatosPreexistentes(){
        listaContatos = new ArrayList<>();
            for(int i = 0; i < 10; i++){
                Contato contato = new Contato();
                contato.setNome("Usuário "+i);
                contato.setCelular("000000"+i);
                contato.setStatusConexao("Offline");
                listaContatos.add(contato);
            }
        }

}

