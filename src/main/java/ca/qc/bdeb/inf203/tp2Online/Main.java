package ca.qc.bdeb.inf203.tp2Online;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }
    ArrayList<String> ra = new ArrayList<>();
    public static final double WIDTH = 350;
    public static final double HEIGHT = 480;
    public static final double GRAVITY = 1200;

    private static Game game = new Game();
    private static FileEditor fileEditor = new FileEditor("scores.txt");
    private static Stage stage;
    private static AnimationTimer timer;

    private static Socket s;
    private static int id;
    private static DataOutputStream dout;

    {
        try {
            s = new Socket("localhost", 6666);
            System.out.println(s.getLocalSocketAddress());
            dout = new DataOutputStream(s.getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        ServerSocket ss = new ServerSocket();
        Random rnd = new Random();
        id = (int) Math.round(rnd.nextDouble() * Math.pow(2, 50));
        try {
            dout.writeUTF("Player " + id + " joined");
            dout.flush();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        this.stage = stage;

        Scene scene = mainScene();

        stage.setTitle("PoulpeZzz");
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(e -> {
            try {
                dout.writeUTF("Stop");
                dout.flush();
                dout.close();
                s.close();
            } catch (Exception err){
                System.out.println(err.getMessage());
            }
        });
    }

    private static Scene scoreScene(boolean newScore, int score) {
        VBox root = new VBox(10);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(15));

        Scene scene =  new Scene(root, WIDTH, HEIGHT);

        Text title = new Text("Meilleurs scores");
        title.setFont(Font.font("Courier New", 30));

        ObservableList<String> obsList = null;
        try {
            obsList = FXCollections.observableArrayList(fileEditor.getScores());
        } catch (IOException e) {
            e.printStackTrace();
        }

        ListView<String> list = new ListView<>();
        list.setItems(obsList);

        root.getChildren().addAll(title, list);

        if(newScore) {
            Text scoreText = new Text("Score : " + score + "px");

            HBox addScore = new HBox(10);

            Text subtitle = new Text("Nickname:");
            TextField name = new TextField();
            Button addButton = new Button("Enregistrer");
            addButton.setOnAction(e -> {
                try {
                    fileEditor.addScore(score, name.getText());
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
                stage.setScene(mainScene());
            });

            addScore.getChildren().addAll(subtitle, name, addButton);
            root.getChildren().addAll(scoreText, addScore);
        }

        Button back = new Button("Retourner à l'accueil");

        back.setOnAction(e -> {
            stage.setScene(mainScene());
        });

        root.getChildren().addAll(back);

        return scene;
    }

    private static Scene mainScene() {
        StackPane root = new StackPane();
        Scene scene =  new Scene(root, WIDTH, HEIGHT);
        // root.setAlignment(Pos.CENTER);

        root.setBackground(new Background(new BackgroundFill(Color.DARKBLUE, CornerRadii.EMPTY, Insets.EMPTY)));

        Image img = new Image("accueil.png");
        ImageView imgView = new ImageView(img);
        root.getChildren().add(imgView);
        imgView.setTranslateY(-42);

        Button playButton = new Button("Jouer!");
        playButton.setOnAction(e -> stage.setScene(gameScene()));
        root.getChildren().add(playButton);
        playButton.setTranslateY(130);

        Button scoreButton = new Button("Meilleurs Scores");
        scoreButton.setOnAction(e -> stage.setScene(scoreScene(false, 0)));
        root.getChildren().add(scoreButton);
        scoreButton.setTranslateY(173);

        scene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ESCAPE) {
                stage.close();
            }
        });

        return scene;
    }

    private static Scene gameScene() {
        game.newGame();
        Pane root = new Pane();
        Scene scene = new Scene(root, WIDTH, HEIGHT);
        Canvas canvas = new Canvas(WIDTH, HEIGHT);
        GraphicsContext ctx = canvas.getGraphicsContext2D();

        timer = new AnimationTimer() {
            double lastTime = 0;
            @Override
            public void handle(long now) {
                if(lastTime == 0) {
                    lastTime = now;
                }
                double deltaTime = (now - lastTime) * 1e-9;

                game.update(deltaTime);
                game.draw(ctx);

                lastTime = now;
            }
        };
        timer.start();

        scene.setOnKeyPressed(e -> {
            if(e.getCode() == KeyCode.ESCAPE) {
                stage.setScene(mainScene());
                timer.stop();
            } else if (e.getCode() == KeyCode.T) {
                game.toggleDebug();
            }
            Input.setKeyPressed(e.getCode(), true);
        });

        scene.setOnKeyReleased(e -> Input.setKeyPressed(e.getCode(), false));

        root.getChildren().add(canvas);

        return scene;
    }

    public static void endGame(int score) {
        timer.stop();
        stage.setScene(scoreScene(true, score));
    }
}
