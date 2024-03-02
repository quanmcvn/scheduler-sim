package com.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application {
    
    private static final int WINDOW_WIDTH = 600;
    private static final int WINDOW_HEIGHT = 400;
    private static final int BALL_RADIUS = 20;
    private static int BALL_SPEED = 5;
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Simple Game");
        
        Pane root = new Pane();
        Scene scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        primaryStage.setScene(scene);
        
        Circle ball = new Circle(BALL_RADIUS, Color.BLUE);
        ball.relocate(WINDOW_WIDTH / 2 - BALL_RADIUS, WINDOW_HEIGHT / 2 - BALL_RADIUS);
        
        root.getChildren().add(ball);
        
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), event -> {
            moveBall(ball);
        }));
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        
        primaryStage.show();
    }
    
    private void moveBall(Circle ball) {
        double newX = ball.getLayoutX() + BALL_SPEED;
        double newY = ball.getLayoutY() + BALL_SPEED;
        
        // Check boundaries
        if (newX >= WINDOW_WIDTH - BALL_RADIUS || newX <= 0) {
            BALL_SPEED *= -1; // Change direction
        }
        if (newY >= WINDOW_HEIGHT - BALL_RADIUS || newY <= 0) {
            BALL_SPEED *= -1; // Change direction
        }
        
        ball.setLayoutX(newX);
        ball.setLayoutY(newY);
    }
}
