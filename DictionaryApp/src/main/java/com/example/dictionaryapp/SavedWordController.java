package com.example.dictionaryapp;

import com.voicerss.tts.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class SavedWordController extends SearchController implements Initializable {

    @FXML
    private ListView<String> listSaved = new ListView<>();
    @FXML
    private TextField textField;
    @FXML
    private ImageView view;
    @FXML
    private ImageView star;
    @FXML
    private Label label;
    public static boolean setLang = false;
    public static boolean setUSUK = false;
    private static final String myKey = "40ccd1f320c549f3afc53b26046c49a4";
    private String accent;
    private String Path;

    public void changeLang() {
        if (setLang) {
            view.setImage(pic2);
            listSaved.getItems().removeAll(list_EV_saved);
            listSaved.getItems().addAll(list_VE_saved);
            setLang = false;
            label.setText("VIE");
        } else {
            view.setImage(pic1);
            listSaved.getItems().removeAll(list_VE_saved);
            listSaved.getItems().addAll(list_EV_saved);
            setLang = true;
            label.setText("US");
        }
    }

    public void USUK() throws Exception {
        if (!label.getText().equals("VIE")) {
            if (setUSUK) {
                label.setText("UK");
                accent = Languages.English_GreatBritain;
                setUSUK = false;
            } else {
                label.setText("US");
                accent = Languages.English_UnitedStates;
                setUSUK = true;
            }
            if (!textField.getText().equals("")) requestDownloadSaved(textField.getText());
        }
    }


    public void chooseWord() throws Exception {
        String selected = listSaved.getSelectionModel().getSelectedItem();
        textField.setText(selected);
        if (!setLang) {
            definitionView.getEngine().loadContent(data_vie2eng.get(selected), "text/html");
            accent = Languages.Vietnamese;
            Path = "data/Voice1.mp3";
        } else {
            definitionView.getEngine().loadContent(data_eng2vie.get(selected), "text/html");
            accent = Languages.English_UnitedStates;
            Path = "data/Voice2.mp3";
        }
        requestDownloadSaved(selected);
    }

    public void requestDownloadSaved(String text) throws Exception {
        VoiceProvider tts = new VoiceProvider(myKey);
        VoiceParameters params = new VoiceParameters(text, accent);
        params.setCodec(AudioCodec.MP3);
        params.setFormat(AudioFormat.Format_44KHZ.AF_44khz_16bit_stereo);
        params.setBase64(false);
        params.setSSML(false);
        params.setRate(0);
        byte[] voice = tts.speech(params);
        FileOutputStream fos = new FileOutputStream(Path);
        fos.write(voice, 0, voice.length);
        fos.flush();
        fos.close();
    }

    public void spellingSaved() {
        Media sound = new Media(new File(Path).toURI().toString());
        MediaPlayer mediaPlayer = new MediaPlayer(sound);
        mediaPlayer.play();
    }

    public void unsaved() throws IOException {

        String text = textField.getText();
        if (label.getText().equals("VIE")) {
            listSaved.getItems().removeAll(list_VE_saved);
            list_VE_saved.remove(text);
            listSaved.getItems().addAll(list_VE_saved);
            writeVESavedWord();
        } else {
            listSaved.getItems().removeAll(list_EV_saved);
            list_EV_saved.remove(text);
            listSaved.getItems().addAll(list_EV_saved);
            writeEVSavedWord();
        }
        textField.setText("");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        changeLang();
        try {
            USUK();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
