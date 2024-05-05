package com.example.dictionaryapp;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class QuizGameController extends MainController implements Initializable {

    @FXML
    private Label question_text;
    @FXML
    private Button option_a;
    @FXML
    private Button option_b;
    @FXML
    private Button option_c;
    @FXML
    private Button option_d;
    @FXML
    private Label highest_score;
    @FXML
    private Label current_score;
    @FXML
    private Label labelTimer;
    @FXML
    private ImageView restart;
    @FXML
    private ImageView A;
    @FXML
    private ImageView B;
    @FXML
    private ImageView C;
    @FXML
    private ImageView D;

    private final String correctStyle = "-fx-background-color: #90D26D;";
    private final String incorrectStyle = "-fx-background-color: #FAA0A0;";
    Image Apic = new Image(getClass().getResourceAsStream("images/A.png"));
    Image Bpic = new Image(getClass().getResourceAsStream("images/B.png"));
    Image Cpic = new Image(getClass().getResourceAsStream("images/C.png"));
    Image Dpic = new Image(getClass().getResourceAsStream("images/D.png"));
    Image A_pic = new Image(getClass().getResourceAsStream("images/A_.png"));
    Image B_pic = new Image(getClass().getResourceAsStream("images/B_.png"));
    Image C_pic = new Image(getClass().getResourceAsStream("images/C_.png"));
    Image D_pic = new Image(getClass().getResourceAsStream("images/D_.png"));
    public static int score = 0;
    public static int highestScore = getHighestScores;
    public static String getClickedOption;
    public static String correctOption;
    private Timeline timeline;
    private int timeSeconds = 120;

    public Quiz getQuiz(int questionId) {
        for (Quiz quiz : quizList) {
            if (quiz.getQuestion_id() == questionId) {
                return quiz;
            }
        }
        return null;
    }

    public void setDefault() {
        Random random = new Random();
        int questionId = random.nextInt(180) + 1;
        Quiz randomQuiz = getQuiz(questionId);
        if (randomQuiz != null) {
            question_text.setText(randomQuiz.getQuestion());
            List<String> answers = randomQuiz.getAnswers();
            option_a.setText(answers.get(0));
            option_b.setText(answers.get(1));
            option_c.setText(answers.get(2));
            option_d.setText(answers.get(3));
            correctOption = randomQuiz.getCorrectAnswer();
        }
        option_a.setStyle(null);
        option_b.setStyle(null);
        option_c.setStyle(null);
        option_d.setStyle(null);
        A.setImage(Apic);
        B.setImage(Bpic);
        C.setImage(Cpic);
        D.setImage(Dpic);
    }

    public void showAnswer() throws IOException {
        if (correctOption.equals("A")) {
            option_a.setStyle(correctStyle);
            option_b.setStyle(incorrectStyle);
            option_c.setStyle(incorrectStyle);
            option_d.setStyle(incorrectStyle);
            if (getClickedOption.equals("A")) {
                correctSound();
                score += 10;
                current_score.setText("Score:\t" + score);
            } else {
                incorrectSound();
                gameFinished();
            }
        } else if (correctOption.equals("B")) {
            option_a.setStyle(incorrectStyle);
            option_b.setStyle(correctStyle);
            option_c.setStyle(incorrectStyle);
            option_d.setStyle(incorrectStyle);
            if (getClickedOption.equals("B")) {
                correctSound();
                score += 10;
                current_score.setText("Score:\t" + score);
            } else {
                incorrectSound();
                gameFinished();
            }
        } else if (correctOption.equals("C")) {
            option_a.setStyle(incorrectStyle);
            option_b.setStyle(incorrectStyle);
            option_c.setStyle(correctStyle);
            option_d.setStyle(incorrectStyle);
            if (getClickedOption.equals("C")) {
                correctSound();
                score += 10;
                current_score.setText("Score:\t" + score);
            } else {
                incorrectSound();
                gameFinished();
            }
        } else {
            option_a.setStyle(incorrectStyle);
            option_b.setStyle(incorrectStyle);
            option_c.setStyle(incorrectStyle);
            option_d.setStyle(correctStyle);
            if (getClickedOption.equals("D")) {
                correctSound();
                score += 10;
                current_score.setText("Score:\t" + score);
            } else {
                incorrectSound();
                gameFinished();
            }
        }
    }

    public void ClickedA() throws IOException {
        getClickedOption = "A";
        A.setImage(A_pic);
        showAnswer();
    }

    public void ClickedB() throws IOException {
        getClickedOption = "B";
        B.setImage(B_pic);
        showAnswer();
    }

    public void ClickedC() throws IOException {
        getClickedOption = "C";
        C.setImage(C_pic);
        showAnswer();
    }

    public void ClickedD() throws IOException {
        getClickedOption = "D";
        D.setImage(D_pic);
        showAnswer();
    }

    public void correctSound() {
        Media sound = new Media(new File("audio/correctAnswer.mp3").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    public void incorrectSound() {
        Media sound = new Media(new File("audio/loseGame.mp3").toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }


    boolean isFinished = false;
    public void gameFinished() throws IOException {
        if (score > highestScore) {
            highestScore = score;
        }
        score = 0;
        FileWriter fw = new FileWriter("data/savedScores.txt");
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(highestScore + "");
        bw.newLine();
        bw.close();
        highest_score.setText("Highest score:\t" + highestScore);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText("Game Over!");
        alert.showAndWait();
        restart.setVisible(true);
        isFinished = true;
    }

    public void startTimer() {
        if (timeline != null) {
            timeline.stop();
        }

        timeline = new Timeline();
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.getKeyFrames().add(
                new KeyFrame(Duration.seconds(1), new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        timeSeconds--;
                        labelTimer.setText(String.valueOf(timeSeconds));
                        if (isFinished) {
                            timeline.stop();
                            isFinished = false;
                        }
                        if (timeSeconds <= 0) {
                            timeline.stop();
                            labelTimer.setText("Time's up!");
                        }
                    }
                })
        );
        timeline.play();
    }

    public void restartTimer() {
        timeSeconds = 120;
        labelTimer.setText(String.valueOf(timeSeconds));
        setDefault();
        startTimer();
        restart.setVisible(false);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setDefault();
        highest_score.setText("Highest score:\t" + highestScore);
        current_score.setText("Score:\t" + score);
        /*Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Rules");
        alert.setHeaderText(null);
        alert.setContentText("Choose A, B, C or D for each gap in the following sentences");
        alert.showAndWait();*/
        startTimer();
    }
}
