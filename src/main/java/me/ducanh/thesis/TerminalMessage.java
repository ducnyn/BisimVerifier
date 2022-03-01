package me.ducanh.thesis;

public enum TerminalMessage {
    CLEAR("All vertices have been removed.");


    private final String message;

    TerminalMessage(String fileName) {
        this.message = fileName;
    }

    public String getMessage() {
        return message;
    }
}
