package com.example.dictionaryapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main extends Application {

    class Word {
        private String word;
        private String def;

        public Word(String word, String def) {
            this.word = word;
            this.def = def;
        }

        public String getWord() {
            return word;
        }

        public void setWord(String word) {
            this.word = word;
        }

        public String getDef() {
            return def;
        }

        public void setDef(String def) {
            this.def = def;
        }
    }

    private static final String FILE_PATH = "data/eng_vie.txt";
    public static Map<String, String> data = new HashMap<>();
    public void readData() throws IOException {
        FileReader fis = new FileReader(FILE_PATH);
        BufferedReader br = new BufferedReader(fis);
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split("<html>");
            String word = parts[0];
            String definition = "<html>" + parts[1];
            data.put(word, definition);
        }

    }

    public static Map<String, String> getData() {
        return data;
    }

    @Override
    public void start(Stage stage) throws IOException {
        readData();
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1300, 700);
        stage.setTitle("DictionaryApp");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}