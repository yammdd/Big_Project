package com.example.dictionaryapp;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebView;
import com.voicerss.tts.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.stage.Stage;


public class Controller implements Initializable {
    @FXML
    private Label myLabel;
    @FXML
    private CheckBox choice;
    @FXML
    private ListView<String> listView;
    @FXML
    private ListView<String> historyList = new ListView<>();
    @FXML
    private WebView definitionView;
    @FXML
    private TextField searchWord;
    @FXML
    private HTMLEditor htmlEditor;
    @FXML
    private ChoiceBox<String> langFrom = new ChoiceBox<>();
    @FXML
    private ChoiceBox<String> langTo = new ChoiceBox<>();
    @FXML
    private TextArea textFrom;
    @FXML
    private TextArea textTo;

    public void exitProgram() {
        System.exit(0);
    }

    public static final Map<String, String> data_eng2vie = Main.getE2V();
    public static Set<String> set_eng2vie = data_eng2vie.keySet();
    public static final Map<String, String> data_vie2eng = Main.getV2E();
    public static Set<String> set_vie2eng = data_vie2eng.keySet();
    public static Map<String, String> data;
    public static Set<String> set;
    public static Map<String, String> searchCode = Main.getSearchCode();
    public static Map<String, String> speakCode = Main.getSpeakCode();
    public static Set<String> languages = searchCode.keySet();

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
        if (selected != null) {
            definitionView.getEngine().loadContent(data.get(selected), "text/html");
            searchWord.setText(selected);
            spelling();
        }
    }

    public void keyPressed(KeyEvent e) throws Exception {
        if (e.getCode().equals(KeyCode.ENTER)) {
            search();
        }
    }

    public static List<String> curr;
    public static Map<String, List<String>> prefixMap;

    public void showAllWords() {
        if (choice.isSelected()) {
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
        if (choice.isSelected()) {
            myLabel.setText("Vie-Eng");
            path = "data/tts_rss_text.mp3";
            ACCENT = Languages.Vietnamese;
            listWords = listWordsVE;
        } else {
            myLabel.setText("Eng-Vie");
            path = "data/tts_rss_word.mp3";
            ACCENT = Languages.English_GreatBritain;
            listWords = listWordsEV;

        }
    }

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToAdd_Delete_Word_scene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("Test.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 870, 600);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToHistory_scene(ActionEvent event) throws IOException {

        root = FXMLLoader.load(getClass().getResource("History.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 870, 600);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToAPI_scene(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("GoogleTranslateAPI.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root, 870, 600);
        stage.setScene(scene);
        stage.show();
    }


    public void modifyWord() {
        htmlEditor.setHtmlText("<html>" + searchWord.getText() +
                "<br/><ul><li><b><i> Loại từ: " +
                "</i></b><ul><li><font color='#cc0000'><b> Nghĩa của từ: " +
                "</b></font></li></ul></li></ul></html>");
    }

    public void show() {
        modifyWord();
        showAllWords();
    }

    public static List<String> listWordsEV = Main.getList_EV();
    public static List<String> listWordsVE = Main.getList_VE();
    public static List<String> listWords;

    public void add() throws IOException {
        String word = searchWord.getText();
        String definition = htmlEditor.getHtmlText().replace(" dir=\"ltr\"", "");
        String text = word + definition;
        listWords.addLast(text);
        FileWriter fw = new FileWriter(path);
        BufferedWriter bw = new BufferedWriter(fw);
        for (String content : listWords) {
            bw.write(content);
            bw.write("\n");
        }
        bw.close();
        fw.close();
    }

    public void remove() throws IOException {
        String selected = listView.getSelectionModel().getSelectedItem();
        String definition = data.get(selected);
        FileWriter fw = new FileWriter(path);
        BufferedWriter bw = new BufferedWriter(fw);
        for (String content : listWords) {
            if (!content.equals(selected + definition)) {
                bw.write(content);
                bw.write("\n");
            }
        }
        bw.close();
        fw.close();
    }

    public static List<String> saveWord = new ArrayList<>();
    public static String pathHistory;

    public void saveWordToList() throws IOException {
        if (choice.isSelected()) {
            pathHistory = "data/VE_history.txt";
        } else {
            pathHistory = "data/EV_history.txt";
        }

        FileReader fis = new FileReader(pathHistory);
        BufferedReader br = new BufferedReader(fis);
        String line;
        while ((line = br.readLine()) != null) {
            saveWord.addLast(line);
        }

        String selected = searchWord.getText();
        saveWord.addLast(selected);
        historyList.getItems().addAll(saveWord);

        FileWriter fw = new FileWriter(pathHistory);
        BufferedWriter bw = new BufferedWriter(fw);
        for (String content : saveWord) {
            bw.write(content);
            bw.write("\n");
        }
        bw.close();
        fw.close();
    }

    public void selectedsaveWord() throws Exception {

        String selected = historyList.getSelectionModel().getSelectedItem();
        if (selected != null) {
            definitionView.getEngine().loadContent(data.get(selected), "text/html");
            searchWord.setText(selected);
        }
    }

    public void showHistory() throws IOException {
        if (choice.isSelected()) {
            pathHistory = "data/VE_history.txt";
        } else {
            pathHistory = "data/EV_history.txt";
        }

        FileReader fis = new FileReader(pathHistory);
        BufferedReader br = new BufferedReader(fis);
        String line;
        while ((line = br.readLine()) != null) {
            historyList.getItems().add(line);
        }
    }

    public static String googleTranslate(String langFrom, String langTo, String text) throws IOException {
        String urlScript = "https://script.google.com/macros/s/AKfycbzPu6w845r212es2r8ybkizWt8GGClVT6OwNuPHXZU5lF5ttH1PAoZFpwb0jT0Pr8Ys_g/exec" +
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


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        langFrom.getItems().addAll(languages);
        langTo.getItems().addAll(languages);
    }

    public void APISearch() throws IOException {
        String from = textFrom.getText();
        String lang1 = searchCode.get(langFrom.getValue());
        String lang2 = searchCode.get(langTo.getValue());
        textTo.setText(googleTranslate(lang1, lang2, from));
        if(from.equals("")) {
            textTo.setText("");
        }
    }

}
