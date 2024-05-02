package com.example.dictionaryapp;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.web.HTMLEditor;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Date;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SettingController extends SearchController implements Initializable {

    @FXML
    private HTMLEditor htmlEditor;
    @FXML
    private RadioButton english;
    @FXML
    private RadioButton vietnamese;
    @FXML
    private TextField email;
    @FXML
    private TextField getSubject;
    @FXML
    private TextArea getContent;

    private  String pathRes;
    public static List<String> listWords;
    private static final String from = "java.application.from@gmail.com";
    private static final String to = "java.application.to@gmail.com";
    private static final String password = "rnrraicahugtfbbw";

    public void search() throws Exception {
        String text = searchWord.getText();
        if (set.contains(text)) {
            htmlEditor.setHtmlText(data.get(text));
        }
    }

    public void selectedWord() throws Exception {
        String selected = listView.getSelectionModel().getSelectedItem();
        if (selected != null) {
            htmlEditor.setHtmlText(data.get(selected));
            searchWord.setText(selected);

        }
    }

    public void showAddWord() {
        htmlEditor.setHtmlText("<html>" + searchWord.getText() + "<br/><ul><li><b><i>Part of speech:" +
                "</i></b><ul><li><font color='#233fdb'><b>Definition:" +
                "</b></font></li></ul></li></ul></html>");
    }

    public void setListEV() {
        vietnamese.setSelected(false);
        english.setSelected(true);
        listWords = list_EV;
        pathRes = "data/EV.txt";
        changeLanguageForSetting(false);
    }

    public void setListVE() {
        english.setSelected(false);
        vietnamese.setSelected(true);
        listWords = list_VE;
        pathRes = "data/VE.txt";
        changeLanguageForSetting(true);
    }

    public void buttonDelete() throws IOException {
        String word = searchWord.getText();
        String definition = data.get(word);
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to delete this word?", yes, no);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.showAndWait();
        if (alert.getResult() == yes) {
            FileWriter fw = new FileWriter(pathRes);
            BufferedWriter bw = new BufferedWriter(fw);
            for (String content : listWords) {
                if (!content.equals(word + definition)) {
                    bw.write(content);
                    bw.write("\n");
                }
            }
            bw.close();
            fw.close();
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Notification");
            alert1.setHeaderText(null);
            alert1.setContentText("Deleted successfully!");
            alert1.showAndWait();
        }
    }

    public void buttonSave() throws IOException {
        String word = searchWord.getText();
        if (set.contains(word)) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Notification");
            alert.setHeaderText(null);
            alert.setContentText("This word is existed!");
            alert.showAndWait();
        } else {
            String definition = htmlEditor.getHtmlText()
                    .replace(" dir=\"ltr\"", "")
                    .replace("Part of speech:", "")
                    .replace("Definition", "");
            String text = word + definition;
            listWords.addLast(text);
            FileWriter fw = new FileWriter(pathRes);
            BufferedWriter bw = new BufferedWriter(fw);
            for (String content : listWords) {
                bw.write(content);
                bw.write("\n");
            }
            bw.close();
            fw.close();
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Notification");
            alert1.setHeaderText(null);
            alert1.setContentText("Added successfully!");
            alert1.showAndWait();
        }
    }

    public void buttonModify() throws IOException {
        ButtonType yes = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType no = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to modify this word?", yes, no);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.showAndWait();
        if (alert.getResult() == yes) {
            String word = searchWord.getText();
            String beforeDefinition = data.get(word);
            String afterDefinition = htmlEditor.getHtmlText().replace(" dir=\"ltr\"", "");
            FileWriter fw = new FileWriter(pathRes);
            BufferedWriter bw = new BufferedWriter(fw);
            for (String content : listWords) {
                if (!content.equals(word + beforeDefinition)) {
                    bw.write(content);
                    bw.write("\n");
                } else {
                    bw.write(word + afterDefinition);
                    bw.write("\n");
                }
            }
            bw.close();
            fw.close();
            Alert alert1 = new Alert(Alert.AlertType.INFORMATION);
            alert1.setTitle("Notification");
            alert1.setHeaderText(null);
            alert1.setContentText("Modified successfully!");
            alert1.showAndWait();
        }
    }

    public static boolean sendEmail(String subject, String content) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587"); // TLS 587 SSL 465
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        Authenticator auth = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                // TODO Auto-generated method stub
                return new PasswordAuthentication(from, password);
            }
        };
        Session session = Session.getInstance(props, auth);
        MimeMessage msg = new MimeMessage(session);
        try {
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            //msg.setFrom(from);
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject(subject);
            msg.setSentDate(new Date());
            msg.setContent(content, "text/HTML; charset=UTF-8");
            Transport.send(msg);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public void send() {
        String subject = getSubject.getText();
        String content = email.getText() + "\n" + getContent.getText();
        SettingController.sendEmail(subject, content);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Notification");
        alert.setHeaderText(null);
        alert.setContentText("Sent successfully!");
        alert.showAndWait();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setListEV();
        x.setVisible(false);
        l.setVisible(false);
    }
}
