package org.lemanoman.interpreters;

public interface ArgsListener {
    void onHelp();
    void onSetParameter(String key, String value);
    void onOperation(String operation);
    void onNoArgs();
}
