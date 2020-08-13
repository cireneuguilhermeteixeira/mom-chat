package sample.app.inicio;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import sample.Main;
import sample.models.Contato;

import java.util.HashMap;
import java.util.logging.Logger;


public class InicioController {

    Logger logger = Logger.getLogger(InicioController.class.getName());

    @FXML private GridPane inicio;
    @FXML private TextField myName;
    @FXML private TextField myFone;

    @FXML private Label infoLabel;



    public void startChat(ActionEvent event){

        if (myName.getText().isEmpty() || myFone.getText().isEmpty()) {
            infoLabel.setText("Informe todos campos.");
        } else {
            infoLabel.setText(null);
            System.out.println("App vai começar.");
            Contato contato = new Contato();
            contato.setNome(myName.getText());
            contato.setCelular(myFone.getText());
            Main.changeScreen("chat", contato);
        }
    }
}
