package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.objectpool;

import java.util.Vector;

public class TokenManage {

    private static final int TOKEN_SIZE = 10;

    public static TokenManage kit = new TokenManage();

    public Vector<ParaToken> paraTokens = new Vector<ParaToken>(TOKEN_SIZE);

    public static TokenManage instance() {
        return kit;
    }

    public synchronized ParaToken allocToken(IMemObj obj) {
        ParaToken token = null;
        if (paraTokens.size() >= TOKEN_SIZE) {
            for (int i = 0; i < TOKEN_SIZE; i++) {
                if (paraTokens.get(i).isFree()) {
                    token = paraTokens.remove(i);
                    break;
                }
            }

            paraTokens.add(token);
        } else {
            token = new ParaToken(obj);
            paraTokens.add(token);

        }
        return token;
    }
}
