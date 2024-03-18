package com.example.dictionaryapp;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
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

    public Set<String> set = data.keySet();
    public static boolean first_time = true;
    public void search() throws IOException {
        if(first_time) readData();
        String text = searchWord.getText();
        //String lowerCasetext = text.toLowerCase();

            if (set.contains(text)) {
                definitionView.getEngine().loadContent(data.get(text), "text/html");
            }

    }
    public void keyPressed(KeyEvent e) throws Exception {
        if(e.getCode().equals(KeyCode.ENTER)) {
            search();
            spelling();
        }
    }
    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToScene1(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Scene1.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();

    }
    public void showAllWords() {
        List<String> listWord = new ArrayList<>(set);
        // Sorting a List
        Collections.sort(listWord);
        // Convert List to Set
        set = new LinkedHashSet<>(listWord);
        listView.getItems().addAll(set);
    }
    private static String myKey = "40ccd1f320c549f3afc53b26046c49a4";
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
    //public static boolean checkBox = true;
    public void change() throws IOException {
        if(choice.isSelected()) {
            first_time = false;
            myLabel.setText("Vie-Eng");
            path = "data/tts_rss_text.mp3";
            ACCENT = Languages.Vietnamese;
            FILE_PATH = "data/vie_eng.txt";
            data.clear();
            set.clear();
            readData();

        } else {
            myLabel.setText("Eng-Vie");
            path = "data/tts_rss_word.mp3";
            ACCENT = Languages.English_GreatBritain;
            FILE_PATH = "data/eng_vie.txt";
            readData();
        }
    }

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
    public static String FILE_PATH = "data/eng_vie.txt";
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
}