package de.ixsen.streamlinkvodhelper.custom.components;

import javafx.scene.control.TextField;

public class NumericTextField extends TextField {

    private Integer lowerBound;
    private Integer upperBound;

    @Override
    public void replaceText(int start, int end, String text) {
        if (this.isValid(text)) {
            super.replaceText(start, end, text);
        }
    }

    @Override
    public void replaceSelection(String text) {
        if (this.isValid(text)) {
            super.replaceSelection(text);
        }
    }

    private boolean isValid(String inserted) {
        String newText = this.getText() + inserted;
        if (this.hasBounds()) {
            if (newText.matches("\\d+")) {
                boolean valid = true;
                if (this.upperBound != null) {
                    valid = Integer.parseInt(newText) <= this.upperBound;
                }
                if (this.lowerBound != null) {
                    valid = valid && Integer.parseInt(newText) >= this.lowerBound;
                }
                return valid;
            } else {
                return false;
            }
        }
        return newText.matches("\\d*");
    }

    private boolean hasBounds() {
        return this.upperBound != null || this.lowerBound != null;
    }

    public void setRange(Integer lowerBound, Integer upperBound) {
        this.lowerBound = lowerBound;
        this.upperBound = upperBound;
    }

    public void addUpperBound(int upperBound) {
        this.setRange(this.lowerBound, upperBound);
    }

    public void addLowerBound(int lowerBound) {
        this.setRange(lowerBound, this.upperBound);
    }
}
