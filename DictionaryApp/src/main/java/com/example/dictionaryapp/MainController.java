package com.example.dictionaryapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import java.io.*;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    @FXML
    private BorderPane borderPane;

    public static Map<String, String> data_eng2vie = new HashMap<>();
    public static Map<String, String> data_vie2eng = new HashMap<>();
    public static Map<String, String> searchCode = new HashMap<>();
    public static Map<String, String> speakCode = new HashMap<>();
    public static Map<String, String> traineddata = new HashMap<>();
    public static List<String> list_EV = new ArrayList<>();
    public static List<String> list_VE = new ArrayList<>();
    public static List<String> list_EV_saved = new ArrayList<>();
    public static List<String> list_VE_saved = new ArrayList<>();
    public static Set<String> set_eng2vie = data_eng2vie.keySet();
    public static Set<String> set_vie2eng = data_vie2eng.keySet();
    public static Map<String, List<String>> Map_eng2vie = new HashMap<>();
    public static Map<String, List<String>> Map_vie2eng = new HashMap<>();
    public static List<Quiz> quizList = new ArrayList<>();
    public static List<Topic> topicList = new ArrayList<>();
    public static int getHighestScores;

    public void loadQuiz() throws IOException {
        FileReader fr = new FileReader("data/Quiz.txt");
        BufferedReader br = new BufferedReader(fr);
        String line;
        while ((line = br.readLine()) != null) {
            String[] split = line.split(";");
            List<String> answers = new ArrayList<>();
            answers.add(split[2]);
            answers.add(split[3]);
            answers.add(split[4]);
            answers.add(split[5]);
            Quiz quiz = new Quiz(Integer.parseInt(split[0]), split[1], answers, split[6]);
            quizList.add(quiz);
        }
    }

    public void readWordHangman() throws IOException {
        FileReader fis = new FileReader("data/Word.txt");
        BufferedReader br = new BufferedReader(fis);
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(";");
            List<String> words = new ArrayList<>(Arrays.asList(parts).subList(1, parts.length));
            Topic topic = new Topic(parts[0], words);
            topicList.add(topic);
        }
    }

    public void loadScore() throws IOException {
        FileReader fr = new FileReader("data/savedScores.txt");
        BufferedReader br = new BufferedReader(fr);
        getHighestScores = Integer.parseInt(br.readLine());
    }

    public void sort() {
        List<String> list1 = new ArrayList<>(set_eng2vie);
        List<String> list2 = new ArrayList<>(set_vie2eng);
        Collections.sort(list1);
        Collections.sort(list2);
        set_eng2vie = new LinkedHashSet<>(list1);
        set_vie2eng = new LinkedHashSet<>(list2);
    }

    public void readData() throws IOException {
        FileReader fis1 = new FileReader("data/EV.txt");
        FileReader fis2 = new FileReader("data/VE.txt");
        BufferedReader br1 = new BufferedReader(fis1);
        BufferedReader br2 = new BufferedReader(fis2);
        String line1;
        while ((line1 = br1.readLine()) != null) {
            list_EV.addLast(line1);
            String[] parts = line1.split("<html>");
            String word = parts[0];
            String definition = "<html>" + parts[1];
            data_eng2vie.put(word, definition);
        }
        String line2;
        while ((line2 = br2.readLine()) != null) {
            list_VE.addLast(line2);
            String[] parts = line2.split("<html>");
            String word = parts[0];
            String definition = "<html>" + parts[1];
            data_vie2eng.put(word, definition);
        }
    }

    public void readDataSaved() throws IOException {
        FileReader fis1 = new FileReader("data/EV_saved.txt");
        FileReader fis2 = new FileReader("data/VE_saved.txt");
        BufferedReader br1 = new BufferedReader(fis1);
        BufferedReader br2 = new BufferedReader(fis2);
        String line1;
        while ((line1 = br1.readLine()) != null) {
            list_EV_saved.addLast(line1);
        }
        String line2;
        while ((line2 = br2.readLine()) != null) {
            list_VE_saved.addLast(line2);
        }
    }

    public void writeEVSavedWord() throws IOException {
        FileWriter fr = new FileWriter("data/EV_saved.txt");
        BufferedWriter bw = new BufferedWriter(fr);
        for (String word : list_EV_saved) {
            bw.write(word);
            bw.write("\n");
        }
        bw.close();
        fr.close();
    }

    public void writeVESavedWord() throws IOException {
        FileWriter fr = new FileWriter("data/VE_saved.txt");
        BufferedWriter bw = new BufferedWriter(fr);
        for (String word : list_VE_saved) {
            bw.write(word);
            bw.write("\n");
        }
        bw.close();
        fr.close();
    }

    public void readKeyCode() throws IOException {
        FileReader fis = new FileReader("data/code.txt");
        BufferedReader br = new BufferedReader(fis);
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(", ");
            searchCode.put(parts[0], parts[1]);
            speakCode.put(parts[0], parts[2]);
            traineddata.put(parts[0], parts[3]);
        }
    }

    public void insertToMap_eng2vie(Set<String> set) {
        for (String word : set) {
            for (int i = 1; i <= word.length(); i++) {
                String prefix = word.substring(0, i);
                Map_eng2vie.putIfAbsent(prefix, new ArrayList<>());
                Map_eng2vie.get(prefix).add(word);
            }
        }
    }

    public void insertToMap_vie2eng(Set<String> set) {
        for (String word : set) {
            for (int i = 1; i <= word.length(); i++) {
                String prefix = word.substring(0, i);
                Map_vie2eng.putIfAbsent(prefix, new ArrayList<>());
                Map_vie2eng.get(prefix).add(word);
            }
        }
    }

    public void button_search() throws IOException {
        AnchorPane view = FXMLLoader.load(getClass().getResource("Search.fxml"));
        borderPane.setCenter(view);
    }

    public void button_google() throws IOException {
        AnchorPane view = FXMLLoader.load(getClass().getResource("GoogleTranslateAPI.fxml"));
        borderPane.setCenter(view);
    }

    public void button_setting() throws IOException {
        AnchorPane view = FXMLLoader.load(getClass().getResource("Setting.fxml"));
        borderPane.setCenter(view);
    }

    public void button_saved() throws IOException {
        AnchorPane view = FXMLLoader.load(getClass().getResource("SavedWord.fxml"));
        borderPane.setCenter(view);
    }

    public void button_game() throws IOException {
        ButtonType yes = new ButtonType("QuizGame", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("Hangman", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Choose your game you want to play.", yes, no);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.showAndWait();
        if (alert.getResult() == yes) {
            quiz_game();
        } else {
            hangman_game();
        }
    }

    public void quiz_game() throws IOException {
        AnchorPane view = FXMLLoader.load(getClass().getResource("Quiz.fxml"));
        borderPane.setCenter(view);
    }

    public void hangman_game() throws IOException {
        AnchorPane view = FXMLLoader.load(getClass().getResource("Hangman.fxml"));
        borderPane.setCenter(view);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            readData();
            readKeyCode();
            readDataSaved();
            loadQuiz();
            loadScore();
            readWordHangman();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sort();
        insertToMap_eng2vie(set_eng2vie);
        insertToMap_vie2eng(set_vie2eng);
    }
}
