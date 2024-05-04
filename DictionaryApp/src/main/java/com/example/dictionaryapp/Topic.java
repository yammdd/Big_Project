package com.example.dictionaryapp;

import java.util.List;

public class Topic {
    private String topic;
    private List<String> words;

    public Topic(String topic, List<String> words) {
        this.topic = topic;
        this.words = words;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public List<String> getWords() {
        return words;
    }

    public void setWords(List<String> words) {
        this.words = words;
    }
}
