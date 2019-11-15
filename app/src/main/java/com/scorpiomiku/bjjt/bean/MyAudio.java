package com.scorpiomiku.bjjt.bean;

public class MyAudio {
    private int audio_id;
    private String audio_name;

    @Override
    public String toString() {
        return "MyAudio{" +
                "audio_id=" + audio_id +
                ", audio_name='" + audio_name + '\'' +
                '}';
    }

    public int getAudio_id() {

        return audio_id;
    }

    public void setAudio_id(int audio_id) {
        this.audio_id = audio_id;
    }

    public String getAudio_name() {
        return audio_name;
    }

    public void setAudio_name(String audio_name) {
        this.audio_name = audio_name;
    }
}
