package org.lemanoman.download;

public class StringReplace {
    private String oldValue;
    private String newValue;
    public String getOldValue() {
        return oldValue;
    }
    public void setOldValue(String oldValue) {
        this.oldValue = oldValue;
    }
    public String getNewValue() {
        return newValue;
    }
    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
    public StringReplace(String oldValue,String newValue) {
	this.newValue = newValue;
	this.oldValue = oldValue;
    }
    
}
