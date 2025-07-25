package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.io;

import java.io.IOException;

public class IncompleteActionException extends IOException {

    private static final long serialVersionUID = -6817511986951461967L;

    private final Action action;

    private final byte[] rest;

    public IncompleteActionException(Action action, byte[] rest) {
        super("Action " + action + " contains " + rest.length + " unread bytes");
        this.action = action;
        this.rest = rest;
    }

    public Action getAction() {
        return action;
    }

    public byte[] getBytes() {
        return rest;
    }
}
