package com.example.dictionaryapp;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebView;
import com.voicerss.tts.*;
import java.io.*;
import java.util.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;


public class Controller {
    @FXML
    private Label myLabel;
    @FXML
    private CheckBox choice;
    @FXML
    private ListView<String> listView;
    @FXML
    private WebView definitionView;
    @FXML
    private TextField searchWord;

    public void exitProgram() {
        System.exit(0);
    }
    public static final Map<String, String> data_eng2vie = Main.getE2V();
    public static Set<String> set_eng2vie = data_eng2vie.keySet();
    public static final Map<String, String> data_vie2eng = Main.getV2E();
    public static Set<String> set_vie2eng = data_vie2eng.keySet();
    public static Map<String, String> data;
    public static Set<String> set;

    public void search() throws Exception {
        String text = searchWord.getText();
        //String lowerCasetext = text.toLowerCase();
        if (set.contains(text)) {
            definitionView.getEngine().loadContent(data.get(text), "text/html");
            spelling();
        }
    }
    public void selectedWord() throws Exception {
        String selected = listView.getSelectionModel().getSelectedItem();
        if(selected != null) {
            definitionView.getEngine().loadContent(data.get(selected), "text/html");
            searchWord.setText(selected);
            spelling();
        }
    }
    public void keyPressed(KeyEvent e) throws Exception {
        if(e.getCode().equals(KeyCode.ENTER)) {
            search();
        }
    }
    /*private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToScene1(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Scene1.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }*/
    public static List<String> curr;
    public static Map<String, List<String>> prefixMap;
    public void showAllWords() {
        if(choice.isSelected()) {
            prefixMap = Main.getPrefixMap_V2E();
            set = set_vie2eng;
            data = data_vie2eng;
        } else {
            prefixMap = Main.getPrefixMap_E2V();
            set = set_eng2vie;
            data = data_eng2vie;
        }
        String text = searchWord.getText();
        if (text.length() == 1) {
            listView.getItems().removeAll(set);
            curr = prefixMap.getOrDefault(text, new ArrayList<>());
            listView.getItems().addAll(curr);

        } else if (text.length() > 1) {
            listView.getItems().removeAll(curr);
            curr = prefixMap.getOrDefault(text, new ArrayList<>());
            listView.getItems().addAll(curr);
        } else {
            listView.getItems().addAll(set);
        }
    }
    private static final String myKey = "40ccd1f320c549f3afc53b26046c49a4";
    public static String ACCENT = Languages.English_GreatBritain;
    public static String path = "data/tts_rss_word.mp3";
    public void requestDownload(String text) throws Exception {
        VoiceProvider tts = new VoiceProvider(myKey);
        VoiceParameters params = new VoiceParameters(text, ACCENT);
        params.setCodec(AudioCodec.MP3);
        params.setFormat(AudioFormat.Format_44KHZ.AF_44khz_16bit_stereo);
        params.setBase64(false);
        params.setSSML(false);
        params.setRate(0);
        byte[] voice = tts.speech(params);
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(voice, 0, voice.length);
        fos.flush();
        fos.close();
    }
    public void spelling() throws Exception {
        String text = searchWord.getText();
        requestDownload(text);
        Media sound = new Media(new File(path).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }
    public void change() throws IOException {
        if(choice.isSelected()) {
            myLabel.setText("Vie-Eng");
            path = "data/tts_rss_text.mp3";
            ACCENT = Languages.Vietnamese;
        } else {
            myLabel.setText("Eng-Vie");
            path = "data/tts_rss_word.mp3";
            ACCENT = Languages.English_GreatBritain;
        }
    }
}