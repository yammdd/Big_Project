package com.example.dictionaryapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main extends Application {
    public static Map<String, String> data_eng2vie = new HashMap<>();
    public static Map<String, String> data_vie2eng = new HashMap<>();
    public void readData() throws IOException {
        FileReader fis1 = new FileReader("data/eng_vie.txt");
        FileReader fis2 = new FileReader("data/vie_eng.txt");
        BufferedReader br1 = new BufferedReader(fis1);
        BufferedReader br2 = new BufferedReader(fis2);
        String line1;
        while ((line1 = br1.readLine()) != null) {
            String[] parts = line1.split("<html>");
            String word = parts[0];
            String definition = "<html>" + parts[1];
            data_eng2vie.put(word, definition);
        }
        String line2;
        while ((line2 = br2.readLine()) != null) {
            String[] parts = line2.split("<html>");
            String word = parts[0];
            String definition = "<html>" + parts[1];
            data_vie2eng.put(word, definition);
        }
    }

    public static Map<String, String> getE2V(){
        return data_eng2vie;
    }
    public static Map<String, String> getV2E(){
        return data_vie2eng;
    }
    public static Set<String> set_eng2vie = data_eng2vie.keySet();
    public static Set<String> set_vie2eng = data_vie2eng.keySet();
    public void sort() {
        List<String> list1 = new ArrayList<>(set_eng2vie);
        List<String> list2 = new ArrayList<>(set_vie2eng);
        // Sorting a List
        Collections.sort(list1);
        Collections.sort(list2);
        // Convert List to Set
        set_eng2vie = new LinkedHashSet<>(list1);
        set_vie2eng = new LinkedHashSet<>(list2);
    }
    public static Set<String> getSetE2V() {
        return set_eng2vie;
    }
    public static Set<String> getSetV2E() {
        return set_vie2eng;
    }
    public static Map<String, List<String>> Map_eng2vie = new HashMap<>();
    public static Map<String, List<String>> Map_vie2eng = new HashMap<>();

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

    public static Map<String, List<String>> getPrefixMap_E2V() {
        return Map_eng2vie;
    }
    public static Map<String, List<String>> getPrefixMap_V2E() {
        return Map_vie2eng;
    }
    @Override
    public void start(Stage stage) throws IOException {
        readData();
        sort();
        insertToMap_eng2vie(set_eng2vie);
        insertToMap_vie2eng(set_vie2eng);
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("MainApp.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 500);
        stage.setTitle("DictionaryApp");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}