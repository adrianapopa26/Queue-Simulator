package sample;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UserInterface extends Application{
    private final Button startSimulation=new Button("Start Simulation");
    private final Text title=new Text("Setup data");
    private final Text t1=new Text("Nr. services:");
    private final Text t2=new Text("Nr. clients:");
    private final Text t3=new Text("Min. arrival time:");
    private final Text t4=new Text("Max. arrival time:");
    private final Text t5=new Text("Min. service time:");
    private final Text t6=new Text("Max. service time:");
    private final Text t7=new Text("Time limit:");
    private final TextField Q=new TextField();
    private final TextField N=new TextField();
    private final TextField tMinArrival=new TextField();
    private final TextField tMaxArrival=new TextField();
    private final TextField tMinService=new TextField();
    private final TextField tMaxService=new TextField();
    private final TextField tLimit=new TextField();
    private final TextArea updates=new TextArea();
    private final UserInterface thisInterface=this;

    public void update(String s){
        updates.appendText(s);
        if(updates.getLength()>1000)
            updates.deleteText(0,updates.getLength()-1);
    }

    EventHandler<ActionEvent> event1 = e -> {
        String s=Q.getText();
        int q =Integer.parseInt(s);
        s=N.getText();
        int n =Integer.parseInt(s);
        s=tMinArrival.getText();
        int minArrival =Integer.parseInt(s);
        s=tMaxArrival.getText();
        int maxArrival =Integer.parseInt(s);
        s=tMinService.getText();
        int minService =Integer.parseInt(s);
        s=tMaxService.getText();
        int maxService =Integer.parseInt(s);
        s=tLimit.getText();
        int t =Integer.parseInt(s);
        updates.clear();
        SimulationManager sim=new SimulationManager(q,n,t,minArrival,maxArrival,minService,maxService,thisInterface);
        new Thread(sim).start();
    };

    public void initializeUI(){
        startSimulation.setTranslateX(-230);
        startSimulation.setTranslateY(120);
        startSimulation.setMaxSize(120,30);
        startSimulation.setFont(Font.font("Times New Roman", FontWeight.BOLD, 14));
        startSimulation.setOnAction(event1);

        title.setTranslateX(-230);
        title.setTranslateY(-180);
        title.setFont(Font.font("Times New Roman", FontWeight.BOLD, 16));

        t1.setTranslateX(-250);
        t1.setTranslateY(-140);
        t1.setFont(Font.font("Times New Roman",  14));

        t2.setTranslateX(-250);
        t2.setTranslateY(-110);
        t2.setFont(Font.font("Times New Roman",  14));

        t3.setTranslateX(-250);
        t3.setTranslateY(-80);
        t3.setFont(Font.font("Times New Roman",  14));

        t4.setTranslateX(-250);
        t4.setTranslateY(-50);
        t4.setFont(Font.font("Times New Roman",  14));

        t5.setTranslateX(-250);
        t5.setTranslateY(-20);
        t5.setFont(Font.font("Times New Roman",  14));

        t6.setTranslateX(-250);
        t6.setTranslateY(10);
        t6.setFont(Font.font("Times New Roman",  14));

        t7.setTranslateX(-250);
        t7.setTranslateY(40);
        t7.setFont(Font.font("Times New Roman",  14));

        Q.setTranslateX(-150);
        Q.setTranslateY(-140);
        Q.setMaxSize(50,20);

        N.setTranslateX(-150);
        N.setTranslateY(-110);
        N.setMaxSize(50,20);

        tMinArrival.setTranslateX(-150);
        tMinArrival.setTranslateY(-80);
        tMinArrival.setMaxSize(50,20);

        tMaxArrival.setTranslateX(-150);
        tMaxArrival.setTranslateY(-50);
        tMaxArrival.setMaxSize(50,20);

        tMinService.setTranslateX(-150);
        tMinService.setTranslateY(-20);
        tMinService.setMaxSize(50,20);

        tMaxService.setTranslateX(-150);
        tMaxService.setTranslateY(10);
        tMaxService.setMaxSize(50,20);

        tLimit.setTranslateX(-150);
        tLimit.setTranslateY(40);
        tLimit.setMaxSize(50,20);

        updates.setTranslateX(100);
        updates.setTranslateY(-40);
        updates.setMaxSize(400,320);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Queue Simulator");
        StackPane root=new StackPane();
        initializeUI();
        root.getChildren().add(startSimulation);
        root.getChildren().add(title);
        root.getChildren().add(t1);
        root.getChildren().add(t2);
        root.getChildren().add(t3);
        root.getChildren().add(t4);
        root.getChildren().add(t5);
        root.getChildren().add(t6);
        root.getChildren().add(t7);
        root.getChildren().add(Q);
        root.getChildren().add(N);
        root.getChildren().add(tMinArrival);
        root.getChildren().add(tMaxArrival);
        root.getChildren().add(tMinService);
        root.getChildren().add(tMaxService);
        root.getChildren().add(tLimit);
        root.getChildren().add(updates);
        primaryStage.setScene(new Scene(root, 750, 450));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

