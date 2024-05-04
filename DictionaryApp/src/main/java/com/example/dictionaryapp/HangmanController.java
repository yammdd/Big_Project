package com.example.dictionaryapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import java.net.URL;
import java.util.Random;
import java.util.ResourceBundle;

public class HangmanController extends MainController implements Initializable {

    @FXML
    private ComboBox<String> showTopics;
    @FXML
    private Label showCurrentWord;
    @FXML
    private Label showAttempts;
    @FXML
    private Label wordLength;
    @FXML
    private ImageView HangmanImage;
    @FXML private Button aBut;
    @FXML private Button bBut;
    @FXML private Button cBut;
    @FXML private Button dBut;
    @FXML private Button eBut;
    @FXML private Button fBut;
    @FXML private Button gBut;
    @FXML private Button hBut;
    @FXML private Button iBut;
    @FXML private Button jBut;
    @FXML private Button kBut;
    @FXML private Button lBut;
    @FXML private Button mBut;
    @FXML private Button nBut;
    @FXML private Button oBut;
    @FXML private Button pBut;
    @FXML private Button qBut;
    @FXML private Button rBut;
    @FXML private Button sBut;
    @FXML private Button tBut;
    @FXML private Button uBut;
    @FXML private Button vBut;
    @FXML private Button wBut;
    @FXML private Button xBut;
    @FXML private Button yBut;
    @FXML private Button zBut;

    private static final int MAX_ATTEMPTS = 7;
    private String secretWord;
    private String currentWord;
    private int attemptsLeft;
    private Character guessSelected;

    ObservableList<String> topics = FXCollections.observableArrayList("Careers", "Fruits", "Family",
            "Animals", "Colors", "Body parts", "Schools", "Personality", "Study materials",
            "Nature", "Entertainment", "Housing", "Vegetables", "Beverages", "Food",
            "Actions", "Clothing", "Emotions", "Cooking", "Transportation", "Travel",
            "Subjects", "Shapes", "Sports", "Time", "Flowers", "Weather");

    Image image1 = new Image(getClass().getResourceAsStream("images/hangman1.png"));
    Image image2 = new Image(getClass().getResourceAsStream("images/hangman2.png"));
    Image image3 = new Image(getClass().getResourceAsStream("images/hangman3.png"));
    Image image4 = new Image(getClass().getResourceAsStream("images/hangman4.png"));
    Image image5 = new Image(getClass().getResourceAsStream("images/hangman5.png"));
    Image image6 = new Image(getClass().getResourceAsStream("images/hangman6.png"));
    Image image7 = new Image(getClass().getResourceAsStream("images/hangman7.png"));

    public void HangmanGameDefault() {
        Topic topic = getTopic();
        Random random = new Random();
        int randomIndex = 0;
        if (topic != null) {
            randomIndex = random.nextInt(topic.getWords().size());
            secretWord = topic.getWords().get(randomIndex);
            currentWord = "_".repeat(secretWord.length());
            wordLength.setText("Word Length:\t" + secretWord.length());
        }
        attemptsLeft = MAX_ATTEMPTS;
        displayGameInfo();
        HangmanImage.setImage(null);
        aBut.setVisible(true);
        bBut.setVisible(true);
        cBut.setVisible(true);
        dBut.setVisible(true);
        eBut.setVisible(true);
        fBut.setVisible(true);
        gBut.setVisible(true);
        hBut.setVisible(true);
        iBut.setVisible(true);
        jBut.setVisible(true);
        kBut.setVisible(true);
        lBut.setVisible(true);
        mBut.setVisible(true);
        nBut.setVisible(true);
        oBut.setVisible(true);
        pBut.setVisible(true);
        qBut.setVisible(true);
        rBut.setVisible(true);
        sBut.setVisible(true);
        tBut.setVisible(true);
        uBut.setVisible(true);
        vBut.setVisible(true);
        wBut.setVisible(true);
        xBut.setVisible(true);
        yBut.setVisible(true);
        zBut.setVisible(true);
    }

    public Topic getTopic() {
        String selected = showTopics.getValue();
        if (selected != null) {
            for (Topic topic : topicList) {
                if (selected.equals(topic.getTopic())) {
                    return topic;
                }
            }
        }
        return null;
    }

    private void displayGameInfo() {
        showCurrentWord.setText(currentWord);
        showAttempts.setText("Attempts left:\t" + attemptsLeft);
    }

    private void drawHangman(int attemptsLeft) {
        if (attemptsLeft == 6) {
            HangmanImage.setImage(image1);
        } else if (attemptsLeft == 5) {
            HangmanImage.setImage(image2);
        } else if (attemptsLeft == 4) {
            HangmanImage.setImage(image3);
        } else if (attemptsLeft == 3) {
            HangmanImage.setImage(image4);
        } else if (attemptsLeft == 2) {
            HangmanImage.setImage(image5);
        } else if (attemptsLeft == 1) {
            HangmanImage.setImage(image6);
        } else if (attemptsLeft == 0) {
            HangmanImage.setImage(image7);
        }
    }

    private boolean updateCurrentWord(char letter) {
        boolean correctGuess = false;
        StringBuilder updatedWord = new StringBuilder(currentWord);
        for (int i = 0; i < secretWord.length(); i++) {
            if (secretWord.charAt(i) == letter) {
                updatedWord.setCharAt(i, letter);
                correctGuess = true;
            }
        }
        currentWord = updatedWord.toString();
        return correctGuess;
    }

    public void play() {
        char guess = guessSelected;
        boolean correctGuess = updateCurrentWord(guess);
        if (correctGuess) {
            displayGameInfo();
            if (currentWord.equals(secretWord)) {
                showAttempts.setText("Congratulations!");
                showCurrentWord.setText(secretWord);
            }
        } else {
            attemptsLeft--;
            drawHangman(attemptsLeft);
            displayGameInfo();
        }
        if (attemptsLeft == 0) {
            showAttempts.setText("Game Over!");
            showCurrentWord.setText(secretWord);
        }
    }

    public void aButSelected() {
        guessSelected = 'a';
        aBut.setVisible(false);
        play();
    }

    public void bButSelected() {
        guessSelected = 'b';
        bBut.setVisible(false);
        play();
    }

    public void cButSelected() {
        guessSelected = 'c';
        cBut.setVisible(false);
        play();
    }

    public void dButSelected() {
        guessSelected = 'd';
        dBut.setVisible(false);
        play();
    }

    public void eButSelected() {
        guessSelected = 'e';
        eBut.setVisible(false);
        play();
    }

    public void fButSelected() {
        guessSelected = 'f';
        fBut.setVisible(false);
        play();
    }

    public void gButSelected() {
        guessSelected = 'g';
        gBut.setVisible(false);
        play();
    }

    public void hButSelected() {
        guessSelected = 'h';
        hBut.setVisible(false);
        play();
    }

    public void iButSelected() {
        guessSelected = 'i';
        iBut.setVisible(false);
        play();
    }

    public void jButSelected() {
        guessSelected = 'j';
        jBut.setVisible(false);
        play();
    }

    public void kButSelected() {
        guessSelected = 'k';
        kBut.setVisible(false);
        play();
    }

    public void lButSelected() {
        guessSelected = 'l';
        lBut.setVisible(false);
        play();
    }

    public void mButSelected() {
        guessSelected = 'm';
        mBut.setVisible(false);
        play();
    }

    public void nButSelected() {
        guessSelected = 'n';
        nBut.setVisible(false);
        play();
    }

    public void oButSelected() {
        guessSelected = 'o';
        oBut.setVisible(false);
        play();
    }

    public void pButSelected() {
        guessSelected = 'p';
        pBut.setVisible(false);
        play();
    }

    public void qButSelected() {
        guessSelected = 'q';
        qBut.setVisible(false);
        play();
    }

    public void rButSelected() {
        guessSelected = 'r';
        rBut.setVisible(false);
        play();
    }

    public void sButSelected() {
        guessSelected = 's';
        sBut.setVisible(false);
        play();
    }

    public void tButSelected() {
        guessSelected = 't';
        tBut.setVisible(false);
        play();
    }

    public void uButSelected() {
        guessSelected = 'u';
        uBut.setVisible(false);
        play();
    }

    public void vButSelected() {
        guessSelected = 'v';
        vBut.setVisible(false);
        play();
    }

    public void wButSelected() {
        guessSelected = 'w';
        wBut.setVisible(false);
        play();
    }

    public void xButSelected() {
        guessSelected = 'x';
        xBut.setVisible(false);
        play();
    }

    public void yButSelected() {
        guessSelected = 'y';
        yBut.setVisible(false);
        play();
    }

    public void zButSelected() {
        guessSelected = 'z';
        zBut.setVisible(false);
        play();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        showTopics.setItems(topics);
    }
}
