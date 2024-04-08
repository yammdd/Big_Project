package com.example.dictionaryapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class MainController implements Initializable {
    
    public static Map<String, String> data_eng2vie = new HashMap<>();
    public static Map<String, String> data_vie2eng = new HashMap<>();
    public static Map<String, String> searchCode = new HashMap<>();
    public static Map<String, String> speakCode = new HashMap<>();
    public static List<String> list_EV = new ArrayList<>();
    public static List<String> list_VE = new ArrayList<>();
    public static Set<String> set_eng2vie = data_eng2vie.keySet();
    public static Set<String> set_vie2eng = data_vie2eng.keySet();
    public static Map<String, List<String>> Map_eng2vie = new HashMap<>();
    public static Map<String, List<String>> Map_vie2eng = new HashMap<>();

    public static boolean setSavedData = false;
    @FXML
    private BorderPane borderPane;

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

    public void readKeyCode() throws IOException {
        FileReader fis = new FileReader("data/code.txt");
        BufferedReader br = new BufferedReader(fis);
        String line;
        while ((line = br.readLine()) != null) {
            String[] parts = line.split(", ");
            searchCode.put(parts[0], parts[1]);
            speakCode.put(parts[0], parts[2]);
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
        //setSavedData = false;
        AnchorPane view = FXMLLoader.load(getClass().getResource("Search.fxml"));
        borderPane.setCenter(view);
    }

    public void button_google() throws IOException {
        AnchorPane view = FXMLLoader.load(getClass().getResource("GoogleTranslateAPI.fxml"));
        borderPane.setCenter(view);
    }

    public void button_setting() throws IOException {
        //setSavedData = false;
        AnchorPane view = FXMLLoader.load(getClass().getResource("Setting.fxml"));
        borderPane.setCenter(view);
    }

    /*public void button_saved() throws IOException {
        //setSavedData = true;
        AnchorPane view = FXMLLoader.load(getClass().getResource("SavedWord.fxml"));
        borderPane.setCenter(view);
    }*/
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        try {
            readData();
            readKeyCode();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        sort();
        insertToMap_eng2vie(set_eng2vie);
        insertToMap_vie2eng(set_vie2eng);
    }
}
