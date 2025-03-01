package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.emf.io;

import java.util.HashMap;
import java.util.Map;

public class ActionSet {

    protected Map actions;

    protected Action defaultAction;

    public ActionSet() {
        actions = new HashMap();
        defaultAction = new Action.Unknown();
    }

    public void addAction(Action action) {
        actions.put(Integer.valueOf(action.getCode()), action);
    }

    public Action get(int actionCode) {
        Action action = (Action) actions.get(Integer.valueOf(actionCode));
        if (action == null) {
            action = defaultAction;
        }
        return action;
    }

    public boolean exists(int actionCode) {
        return (actions.get(Integer.valueOf(actionCode)) != null);
    }
}
