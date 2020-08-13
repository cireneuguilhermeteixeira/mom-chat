package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Objects;

public class Main extends Application {

    private static Stage stage;
    private static  Scene initialScene;
    private static  Scene principalScene;


    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;

        stage.setTitle("Fake Whatsapp");
        Parent fxmlmain = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("view/inicio/inicio.fxml")));
        initialScene = new Scene(fxmlmain, 700, 300);
        Parent fxmlChat = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("view/principal/principal.fxml")));
        principalScene = new Scene(fxmlChat, getMainStageWidth(), getMainStageHeight());
        primaryStage.setTitle("Fake Whatsapp");
        primaryStage.setScene(initialScene);
        primaryStage.show();
    }



    public static void changeScreen(String scr, Object data){
        switch (scr){
            case "main":
                stage.setScene(initialScene);
                notifyAllListeners("main",data, stage);
                break;

            case "chat":
                notifyAllListeners("chat",data, stage);
                stage.setScene(principalScene);
                stage.setMaximized(true);
                //stage.setResizable(false);
                break;
        }
    }


    @Override
    public void stop(){
        if(stage.getScene().equals(principalScene)){
            System.out.println("Parando aplicação");
            Platform.exit();
            System.exit(0);
        }
    }


    public static void main(String[] args) {
        launch(args);
    }

    private static ArrayList<OnChangeSceen> listeners = new ArrayList<>();

    public static interface OnChangeSceen{
        void onScreenChanged(String newScreen, Object data, Stage stage);
    }

    public static void addOnChangeScreenListener(OnChangeSceen newListener){
        listeners.add(newListener);
    }

    private static void notifyAllListeners(String newScreen, Object data, Stage stage){
        for(OnChangeSceen l : listeners)
            l.onScreenChanged(newScreen,data, stage);
    }

    public static double getMainStageWidth(){
        return stage.getWidth();
    }

    public static double getMainStageHeight(){
        return stage.getHeight();
    }
}
