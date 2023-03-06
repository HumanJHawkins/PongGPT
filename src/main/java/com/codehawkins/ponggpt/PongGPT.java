/*****
 *
 * Built with several experimental ChatGPT prompts and corrections of prompts.
 *
 * Essentially asked for a JavaFX implementation of Pong, player vs. computer
 *   with scoring, with 10 as the game winning score. And with a game over
 *   screen that includes the ability to press a key for a new game.
 *
 ******/

package com.codehawkins.ponggpt;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

public class PongGPT extends Application {
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int PADDLE_WIDTH = 10;
    private static final int PADDLE_HEIGHT = 80;
    private static final int BALL_SIZE = 10;
    private static final int PADDLE_SPEED = 5;
    private static final int BALL_SPEED = 4;

    private Canvas canvas;
    private GraphicsContext gc;

    private int playerScore;
    private int computerScore;

    private double ballX;
    private double ballY;
    private double ballSpeedX;
    private double ballSpeedY;

    private double playerPaddleY;
    private double computerPaddleY;

    @Override
    public void start(Stage primaryStage) {
        canvas = new Canvas(WIDTH, HEIGHT);
        gc = canvas.getGraphicsContext2D();
        reset();

        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(16), e -> update()));
        timeline.setCycleCount(Timeline.INDEFINITE);

        canvas.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.SPACE) {
                if (playerScore >= 10 || computerScore >= 10) {
                    gameReset();
                }
            }
        });

        canvas.setFocusTraversable(true);
        canvas.requestFocus();

        canvas.setOnMouseMoved(event -> {
            double y = event.getY() - PADDLE_HEIGHT / 2;
            playerPaddleY = Math.max(0, Math.min(y, HEIGHT - PADDLE_HEIGHT));
        });

        timeline.play();
        primaryStage.setScene(new Scene(new StackPane(canvas)));
        primaryStage.show();
    }

     private void reset() {
         ballX = WIDTH / 2;
         ballY = HEIGHT / 2;
         ballSpeedX = BALL_SPEED;
         ballSpeedY = 0;
         computerPaddleY = HEIGHT / 2 - PADDLE_HEIGHT / 2;
         playerPaddleY = HEIGHT / 2 - PADDLE_HEIGHT / 2;
     }

    private void gameReset() {
        playerScore = 0;
        computerScore = 0;
        reset();
    }

    private void update() {
        gc.setFill(Color.BLACK);
        gc.fillRect(0, 0, WIDTH, HEIGHT);

        gc.setFill(Color.WHITE);
        gc.fillRect(WIDTH / 2 - 1, 0, 2, HEIGHT);

        gc.setFill(Color.YELLOW);
        gc.fillOval(ballX - BALL_SIZE / 2, ballY - BALL_SIZE / 2, BALL_SIZE, BALL_SIZE);

        gc.setFill(Color.WHITE);
        gc.fillRect(0, playerPaddleY, PADDLE_WIDTH, PADDLE_HEIGHT);
        gc.fillRect(WIDTH - PADDLE_WIDTH, computerPaddleY, PADDLE_WIDTH, PADDLE_HEIGHT);

        ballX += ballSpeedX;
        ballY += ballSpeedY;

        if (ballX < PADDLE_WIDTH && ballY > playerPaddleY && ballY < playerPaddleY + PADDLE_HEIGHT) {
            ballSpeedX = BALL_SPEED;
            double relativeIntersectY = (playerPaddleY + PADDLE_HEIGHT / 2) - ballY;
            ballSpeedY = relativeIntersectY / (PADDLE_HEIGHT / 2) * BALL_SPEED;
        }

        if (ballX > WIDTH - PADDLE_WIDTH - BALL_SIZE && ballY > computerPaddleY
                && ballY < computerPaddleY + PADDLE_HEIGHT) {
            ballSpeedX = -BALL_SPEED;
            double relativeIntersectY = (computerPaddleY + PADDLE_HEIGHT / 2) - ballY;
            ballSpeedY = relativeIntersectY / (PADDLE_HEIGHT / 2) * BALL_SPEED;
        }

        if (ballY < BALL_SIZE / 2 || ballY > HEIGHT - BALL_SIZE / 2) {
            ballSpeedY = -ballSpeedY;
        }

        if (ballX < -BALL_SIZE / 2) {
            computerScore++;
            reset();
        }

        if (ballX > WIDTH + BALL_SIZE / 2) {
            playerScore++;
            reset();
        }

        if (computerPaddleY + PADDLE_HEIGHT / 2 < ballY) {
            computerPaddleY += PADDLE_SPEED;
        }

        if (computerPaddleY + PADDLE_HEIGHT / 2 > ballY) {
            computerPaddleY -= PADDLE_SPEED;
        }

        gc.setFill(Color.WHITE);
        gc.fillText("Player: " + playerScore, 50, 50);
        gc.fillText("Computer: " + computerScore, WIDTH - 150, 50);

        if (playerScore >= 10 || computerScore >= 10) {
            gc.setFill(Color.WHITE);
            gc.fillText("Press SPACE to start a new game", WIDTH / 2 - 100, HEIGHT / 2);
        }

        if (playerScore >= 10) {
            gc.setFill(Color.YELLOW);
            gc.fillText("YOU WIN!", WIDTH / 2 - 50, HEIGHT / 2 - 50);
            gc.setFill(Color.WHITE);
            gc.fillText("Press SPACE to start a new game", WIDTH / 2 - 100, HEIGHT / 2);
        }

        if (computerScore >= 10) {
            gc.setFill(Color.YELLOW);
            gc.fillText("YOU LOSE!", WIDTH / 2 - 50, HEIGHT / 2 - 50);
            gc.setFill(Color.WHITE);
            gc.fillText("Press SPACE to start a new game", WIDTH / 2 - 100, HEIGHT / 2);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }


}
