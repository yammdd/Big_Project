package com.example.dictionaryapp;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.HTMLEditor;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Add_Delete_Word {

    @FXML
    private HTMLEditor editor;
    @FXML
    private TextField addWord;
    @FXML
    private ListView<String> listWord;

    public static List<String> html_word = new ArrayList<>();
    public void createWord() {
        editor.setHtmlText("<html><i>" + addWord.getText() +
                "</i><br/><ul><li><b><i> Loại từ: " +
                "</i></b><ul><li><font color='#cc0000'><b> Nghĩa của từ: " +
                "</b></font></li></ul></li></ul></html>");
    }

    public void keyPressed(KeyEvent e) throws Exception {
        if(e.getCode().equals(KeyCode.ENTER)) {
            createWord();
            addNewWord();
        }
    }


    public void addNewWord() {
        String word = addWord.getText();
        String definition = editor.getHtmlText().replace(" dir=\"ltr\"", "");
        String text = word + definition + "\n";
        listWord.getItems().add(word);
        html_word.add(text);
        //System.exit(0);
    }

    public void saveWord() throws IOException {
        FileWriter writer = new FileWriter("data/test.txt");
        for (String word : html_word) {
            writer.write(word);
        }
        writer.close();
    }

    public void deleteWord() throws IOException {
        String selected = listWord.getSelectionModel().getSelectedItem();
        FileReader reader = new FileReader("data/test.txt");
        BufferedReader br = new BufferedReader(reader);
        String line;
        if (selected != null) {
            listWord.getItems().remove(selected);
            while((line = br.readLine()) != null) {
                String[] parts = line.split("<html>");
                String word = parts[0];
                //String definition = "<html>" + parts[1];
                if(selected.equals(word)) {
                    html_word.remove(line);
                }
            }
        }
        saveWord();
    }

}
