package ru.sample.tabolin.itunepreview.domain;

public class PreviewModel {

    public int trackId;
    public String artistName;
    public String collectionName;
    public String trackName;
    public String collectionCensoredName;
    public String trackCensoredName;
    public String previewUrl;
    public String artworkUrl30;
    public String artworkUrl60;
    public String artworkUrl100;

    @Override
    public String toString() {
        return "PreviewModel{" +
                "trackId=" + trackId +
                ", artistName='" + artistName + '\'' +
                ", collectionName='" + collectionName + '\'' +
                ", trackName='" + trackName + '\'' +
                ", collectionCensoredName='" + collectionCensoredName + '\'' +
                ", trackCensoredName='" + trackCensoredName + '\'' +
                ", previewUrl='" + previewUrl + '\'' +
                ", artworkUrl30='" + artworkUrl30 + '\'' +
                ", artworkUrl60='" + artworkUrl60 + '\'' +
                ", artworkUrl100='" + artworkUrl100 + '\'' +
                '}';
    }
}
