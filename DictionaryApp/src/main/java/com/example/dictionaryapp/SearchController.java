package com.example.dictionaryapp;

import com.voicerss.tts.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.web.WebView;
import java.io.*;
import java.net.URL;
import java.util.*;

public class SearchController extends MainController implements Initializable {

    private static final String myKey = "40ccd1f320c549f3afc53b26046c49a4";
    @FXML
    protected ListView<String> listView = new ListView<>();
    @FXML
    protected WebView definitionView;
    @FXML
    protected TextField searchWord;
    @FXML
    protected ImageView imageView;
    @FXML
    protected ImageView favorite;
    @FXML
    protected Label label;
    Image pic1 = new Image(getClass().getResourceAsStream("images/eng-viet.png"));
    Image pic2 = new Image(getClass().getResourceAsStream("images/viet-eng.png"));
    Image pic3 = new Image(getClass().getResourceAsStream("images/icons8_Star_Filled_52px.png"));
    Image pic4 = new Image(getClass().getResourceAsStream("images/icons8_Star_52px.png"));

    public static boolean setLanguage = false;
    public static boolean setFavorite = false;
    public static Map<String, String> data = data_eng2vie;
    public static Set<String> set = set_eng2vie;
    public static List<String> curr;
    public static Map<String, List<String>> prefixMap = Map_eng2vie;
    public static String ACCENT = Languages.English_UnitedStates;
    public static String path;

   /* public void keyPressed(KeyEvent e) throws Exception {
        if (e.getCode().equals(KeyCode.ENTER)) {
            search();
        }
    }*/

    public void search() throws Exception {
        String text = searchWord.getText();
        //String lowerCase-text = text.toLowerCase();
        if (set.contains(text)) {
            definitionView.getEngine().loadContent(data.get(text), "text/html");
            requestDownload(text);
        }
    }

    public void selectedWord() throws Exception {
        String selected = listView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            definitionView.getEngine().loadContent(data.get(selected), "text/html");
            searchWord.setText(selected);
            requestDownload(selected);
        }
    }

    public void changeLanguage() {
        if (setLanguage) {
            imageView.setImage(pic2);
            setLanguage = false;
            prefixMap = Map_vie2eng;
            set = set_vie2eng;
            data = data_vie2eng;
            path = "data/Voice1.mp3";
            ACCENT = Languages.Vietnamese;
            label.setText("VIE");
            //savedPath = "data/VE_saved.txt";
        } else {
            imageView.setImage(pic1);
            setLanguage = true;
            prefixMap = Map_eng2vie;
            set = set_eng2vie;
            data = data_eng2vie;
            path = "data/Voice2.mp3";
            ACCENT = Languages.English_UnitedStates;
            label.setText("US");
            //savedPath = "data/VE_saved.txt";
        }
    }

    public void changeLanguageForSetting(boolean setLang) {
        if (setLang) {
            setLang = false;
            prefixMap = Map_vie2eng;
            set = set_vie2eng;
            data = data_vie2eng;
        } else {
            setLang = true;
            prefixMap = Map_eng2vie;
            set = set_eng2vie;
            data = data_eng2vie;
        }
    }
    public static boolean setUS_UK = false;
    public void US_UK() throws Exception {
        if (setUS_UK) {
            label.setText("UK");
            ACCENT = Languages.English_GreatBritain;
            requestDownload(searchWord.getText());
            setUS_UK = false;
        } else {
            label.setText("US");
            ACCENT = Languages.English_UnitedStates;
            requestDownload((searchWord.getText()));
            setUS_UK = true;
        }
    }

    public void showAllWords() {
        String text = searchWord.getText();

        if (text.length() == 1) {
            listView.getItems().removeAll(set);
            curr = prefixMap.getOrDefault(text, new ArrayList<>());
            listView.getItems().addAll(curr);

        } else if (text.length() > 1) {
            listView.getItems().removeAll(curr);
            curr = prefixMap.getOrDefault(text, new ArrayList<>());
            listView.getItems().addAll(curr);
        }
    }


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

    public void spelling() {
        Media sound = new Media(new File(path).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    public void favorite_click() {
        if (setFavorite) {
            favorite.setImage(pic3);
            setFavorite = false;
        } else {
            favorite.setImage(pic4);
            setFavorite = true;
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setLanguage = true;
        prefixMap = Map_eng2vie;
        set = set_eng2vie;
        data = data_eng2vie;
        path = "data/Voice2.mp3";
        ACCENT = Languages.English_UnitedStates;
        label.setText("US");

    }



}
