package sample.app.principal.listcontacts;


import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import sample.app.principal.PrincipalController;
import sample.models.Contato;

import java.util.ArrayList;


public class ListContactsController {

    private PrincipalController main;
    @FXML public AnchorPane anchorPaneListContacts;
    private ArrayList<Contato> meusContatos = new ArrayList<>();


    public void init(PrincipalController principalController) {

        main = principalController;
        montaContatosPadroes();
    }


    public void montaContatosPadroes(){
        int i = 0;
        meusContatos = getContatos();
        for (Contato contato: meusContatos) {
            adicionaContatoNaLista(contato, i);
            i++;
        }
        mudaJanelaDaConversa(meusContatos.get(0));


    }



    public void adicionaContatoNaLista(Contato contato, int index){

        AnchorPane anchorPane = new AnchorPane();
        anchorPane.setLayoutX(10);
        anchorPane.setLayoutY(40 + (60 * (index)));
        anchorPane.setMinHeight(0);
        anchorPane.setMinWidth(0);
        anchorPane.setPrefHeight(52);
        anchorPane.setPrefWidth(309);
        anchorPane.setStyle("-fx-background-color: #3da457;");

        anchorPane.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<javafx.scene.input.MouseEvent>() {
            @Override public void handle(javafx.scene.input.MouseEvent e) {
                mudaJanelaDaConversa(contato);
            }
        });


        Text text = new Text();
        text.setFill(javafx.scene.paint.Color.valueOf("#ffff"));
        text.setLayoutX(7);
        text.setLayoutY(31);
        text.setStrokeType(javafx.scene.shape.StrokeType.valueOf("OUTSIDE"));
        text.setStrokeWidth(0);
        text.setWrappingWidth(264);
        text.setText(contato.getNome()+ " (Celular: "+contato.getCelular()+") ");

        anchorPane.getChildren().add(text);
        anchorPaneListContacts.getChildren().add(anchorPane);
    }


    private ArrayList<Contato> getContatos(){
        ArrayList<Contato> contatos = new ArrayList<>();
        for(int i = 0; i < 10; i++){
            Contato contato = new Contato();
            contato.setNome("Usuário "+i);
            contato.setCelular("000000"+i);
            contatos.add(contato);
        }
        return contatos;
    }



    private void mudaJanelaDaConversa(Contato contato){
        main.getChatController().iniciaConversaComContato(contato);
    }
}

