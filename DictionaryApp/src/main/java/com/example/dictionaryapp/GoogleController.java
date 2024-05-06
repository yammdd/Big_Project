package com.example.dictionaryapp;

import com.voicerss.tts.AudioCodec;
import com.voicerss.tts.AudioFormat;
import com.voicerss.tts.VoiceParameters;
import com.voicerss.tts.VoiceProvider;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;


public class GoogleController extends MainController implements Initializable {

    private static final String myKey = "40ccd1f320c549f3afc53b26046c49a4";
    private static final String path1 = "audio/tts_rss_word.mp3";
    private static final String path2 = "audio/tts_rss_text.mp3";
    @FXML
    protected ComboBox<String> langFrom;
    @FXML
    protected ComboBox<String> langTo;
    @FXML
    protected TextArea textFrom;
    @FXML
    protected TextArea textTo;
    @FXML
    protected Button chooseFile;
    @FXML
    private Label LabelLangFrom;
    @FXML
    private Label LabelLangTo;

    private Stage stage;
    public static Set<String> language = searchCode.keySet();
    public static String filePath;

    public void transfer () {
        if (!langFrom.getValue().equals("DetectLanguage")) {
            String a = langFrom.getValue();
            String b = langTo.getValue();
            String c = textFrom.getText();
            String d = textTo.getText();
            langFrom.setValue(b);
            langTo.setValue(a);
            textFrom.setText(d);
            textTo.setText(c);
        }

    }

    public void requestDownload(String text, String ACCENT, String path) throws Exception {
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

    public void spelling1() throws Exception {
        if (!textFrom.getText().isEmpty()) {
            if (langFrom.getValue().equals("DetectLanguage")) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Notification");
                alert.setHeaderText(null);
                alert.setContentText("Not Supported");
                alert.showAndWait();
            } else {
                requestDownload(textFrom.getText(), speakCode.get(langFrom.getValue()), path1);
                Media sound = new Media(new File(path1).toURI().toString());
                MediaPlayer mediaPlayer = new MediaPlayer(sound);
                mediaPlayer.play();
            }
        }
    }

    public void spelling2() throws Exception {
        if (!textTo.getText().isEmpty()) {
            requestDownload(textTo.getText(), speakCode.get(langTo.getValue()), path2);
            Media sound = new Media(new File(path2).toURI().toString());
            MediaPlayer mediaPlayer = new MediaPlayer(sound);
            mediaPlayer.play();
        }
    }

    public void sortLang() {
        List<String> list = new ArrayList<>(language);
        Collections.sort(list);
        language = new LinkedHashSet<>(list);
    }

    public static String googleTranslate(String langFrom, String langTo, String text) throws IOException {
        String urlScript = "https://script.google.com/macros/s/" +
                "AKfycbzPu6w845r212es2r8ybkizWt8GGClVT6OwNuPHXZU5lF5ttH1PAoZFpwb0jT0Pr8Ys_g/exec" +
                "?q=" + URLEncoder.encode(text, "UTF-8") +
                "&target=" + langTo +
                "&source=" + langFrom;
        URL url = new URL(urlScript);
        StringBuilder response = new StringBuilder();
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestProperty("User-Agent", "Mozilla/5.0");
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();
        return response.toString();
    }

    public void APISearch() throws Exception {
        String before = textFrom.getText();
        if (!before.isEmpty()) {
            String lang_From = "";
            if (!langFrom.getValue().equals("DetectLanguage")) {
                lang_From = searchCode.get(langFrom.getValue());
            }
            String lang_To = searchCode.get(langTo.getValue());
            textTo.setText(googleTranslate(lang_From, lang_To, before));
        }
    }

    public void setChooseFile() {
        FileChooser.ExtensionFilter ex1 = new FileChooser.ExtensionFilter("Image Files", "*.png");
        FileChooser.ExtensionFilter ex2 = new FileChooser.ExtensionFilter("all Files", "*.*");
        chooseFile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.getExtensionFilters().addAll(ex1, ex2);
                fileChooser.setTitle("Open My File");
                // change your path
                fileChooser.setInitialDirectory(new File("D:\\GitHub" +
                        "\\Big_Project\\DictionaryApp\\ImageToText"));
                File selectedFile = fileChooser.showOpenDialog(stage);
                if (selectedFile != null) {
                    filePath = selectedFile.getPath();
                    System.out.println("filePath = " + filePath);
                    try {
                        imageToText();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        });
    }

    public void imageToText() throws Exception {
        Tesseract tesseract = new Tesseract();
        try {
            //change your path
            tesseract.setDatapath("D:\\GitHub\\Big_Project\\DictionaryApp\\traineddata");
            String lang = traineddata.get(langFrom.getValue());
            tesseract.setLanguage(lang);
            String text = tesseract.doOCR(new File(filePath));
            textFrom.setText((text));
        }
        catch (TesseractException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sortLang();
        langTo.getItems().addAll(language);
        langFrom.getItems().addAll(language);
        langFrom.getItems().addFirst("DetectLanguage");
        langFrom.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    LabelLangFrom.setText(newValue);
                }
            }
        });
        langTo.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null) {
                    LabelLangTo.setText(newValue);
                }
            }
        });
    }
}
