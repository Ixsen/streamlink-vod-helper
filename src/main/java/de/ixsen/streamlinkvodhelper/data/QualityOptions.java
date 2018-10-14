package de.ixsen.streamlinkvodhelper.data;

public enum QualityOptions {
    BEST("best"), VHIGH("1080p60"), HIGHER("720p60"), HIGH("720p"), MID("480p"), LOW("360p"), VLOW("120p"), WORST("worst");

    private String value;

    QualityOptions(String value) {
        this.value = value;
    }

    public String getValue() {
        return this.value;
    }
}
