package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.mozilla.intl.chardet;

public class nsUTF8Verifier extends nsVerifier {

    static int[] cclass;
    static int[] states;
    static int stFactor;
    static String charset;

    public nsUTF8Verifier() {

        cclass = new int[256 / 8];

        cclass[0] = ((((((1) << 4) | (1)) << 8) | ((1) << 4) | (1)) << 16) | ((((1) << 4) | (1)) << 8) | ((1) << 4) | (1);
        cclass[1] = ((((((0) << 4) | (0)) << 8) | ((1) << 4) | (1)) << 16) | ((((1) << 4) | (1)) << 8) | ((1) << 4) | (1);
        cclass[2] = ((((((1) << 4) | (1)) << 8) | ((1) << 4) | (1)) << 16) | ((((1) << 4) | (1)) << 8) | ((1) << 4) | (1);
        cclass[3] = ((((((1) << 4) | (1)) << 8) | ((1) << 4) | (1)) << 16) | ((((0) << 4) | (1)) << 8) | ((1) << 4) | (1);
        cclass[4] = ((((((1) << 4) | (1)) << 8) | ((1) << 4) | (1)) << 16) | ((((1) << 4) | (1)) << 8) | ((1) << 4) | (1);
        cclass[5] = ((((((1) << 4) | (1)) << 8) | ((1) << 4) | (1)) << 16) | ((((1) << 4) | (1)) << 8) | ((1) << 4) | (1);
        cclass[6] = ((((((1) << 4) | (1)) << 8) | ((1) << 4) | (1)) << 16) | ((((1) << 4) | (1)) << 8) | ((1) << 4) | (1);
        cclass[7] = ((((((1) << 4) | (1)) << 8) | ((1) << 4) | (1)) << 16) | ((((1) << 4) | (1)) << 8) | ((1) << 4) | (1);
        cclass[8] = ((((((1) << 4) | (1)) << 8) | ((1) << 4) | (1)) << 16) | ((((1) << 4) | (1)) << 8) | ((1) << 4) | (1);
        cclass[9] = ((((((1) << 4) | (1)) << 8) | ((1) << 4) | (1)) << 16) | ((((1) << 4) | (1)) << 8) | ((1) << 4) | (1);
        cclass[10] = ((((((1) << 4) | (1)) << 8) | ((1) << 4) | (1)) << 16) | ((((1) << 4) | (1)) << 8) | ((1) << 4) | (1);
        cclass[11] = ((((((1) << 4) | (1)) << 8) | ((1) << 4) | (1)) << 16) | ((((1) << 4) | (1)) << 8) | ((1) << 4) | (1);
        cclass[12] = ((((((1) << 4) | (1)) << 8) | ((1) << 4) | (1)) << 16) | ((((1) << 4) | (1)) << 8) | ((1) << 4) | (1);
        cclass[13] = ((((((1) << 4) | (1)) << 8) | ((1) << 4) | (1)) << 16) | ((((1) << 4) | (1)) << 8) | ((1) << 4) | (1);
        cclass[14] = ((((((1) << 4) | (1)) << 8) | ((1) << 4) | (1)) << 16) | ((((1) << 4) | (1)) << 8) | ((1) << 4) | (1);
        cclass[15] = ((((((1) << 4) | (1)) << 8) | ((1) << 4) | (1)) << 16) | ((((1) << 4) | (1)) << 8) | ((1) << 4) | (1);
        cclass[16] = ((((((3) << 4) | (3)) << 8) | ((3) << 4) | (3)) << 16) | ((((2) << 4) | (2)) << 8) | ((2) << 4) | (2);
        cclass[17] = ((((((4) << 4) | (4)) << 8) | ((4) << 4) | (4)) << 16) | ((((4) << 4) | (4)) << 8) | ((4) << 4) | (4);
        cclass[18] = ((((((4) << 4) | (4)) << 8) | ((4) << 4) | (4)) << 16) | ((((4) << 4) | (4)) << 8) | ((4) << 4) | (4);
        cclass[19] = ((((((4) << 4) | (4)) << 8) | ((4) << 4) | (4)) << 16) | ((((4) << 4) | (4)) << 8) | ((4) << 4) | (4);
        cclass[20] = ((((((5) << 4) | (5)) << 8) | ((5) << 4) | (5)) << 16) | ((((5) << 4) | (5)) << 8) | ((5) << 4) | (5);
        cclass[21] = ((((((5) << 4) | (5)) << 8) | ((5) << 4) | (5)) << 16) | ((((5) << 4) | (5)) << 8) | ((5) << 4) | (5);
        cclass[22] = ((((((5) << 4) | (5)) << 8) | ((5) << 4) | (5)) << 16) | ((((5) << 4) | (5)) << 8) | ((5) << 4) | (5);
        cclass[23] = ((((((5) << 4) | (5)) << 8) | ((5) << 4) | (5)) << 16) | ((((5) << 4) | (5)) << 8) | ((5) << 4) | (5);
        cclass[24] = ((((((6) << 4) | (6)) << 8) | ((6) << 4) | (6)) << 16) | ((((6) << 4) | (6)) << 8) | ((0) << 4) | (0);
        cclass[25] = ((((((6) << 4) | (6)) << 8) | ((6) << 4) | (6)) << 16) | ((((6) << 4) | (6)) << 8) | ((6) << 4) | (6);
        cclass[26] = ((((((6) << 4) | (6)) << 8) | ((6) << 4) | (6)) << 16) | ((((6) << 4) | (6)) << 8) | ((6) << 4) | (6);
        cclass[27] = ((((((6) << 4) | (6)) << 8) | ((6) << 4) | (6)) << 16) | ((((6) << 4) | (6)) << 8) | ((6) << 4) | (6);
        cclass[28] = ((((((8) << 4) | (8)) << 8) | ((8) << 4) | (8)) << 16) | ((((8) << 4) | (8)) << 8) | ((8) << 4) | (7);
        cclass[29] = ((((((8) << 4) | (8)) << 8) | ((9) << 4) | (8)) << 16) | ((((8) << 4) | (8)) << 8) | ((8) << 4) | (8);
        cclass[30] = ((((((11) << 4) | (11)) << 8) | ((11) << 4) | (11)) << 16) | ((((11) << 4) | (11)) << 8) | ((11) << 4) | (10);
        cclass[31] = ((((((0) << 4) | (0)) << 8) | ((15) << 4) | (14)) << 16) | ((((13) << 4) | (13)) << 8) | ((13) << 4) | (12);

        states = new int[26];

        states[0] = ((((((10) << 4) | (12)) << 8) | ((eError) << 4) | (eError)) << 16) | ((((eError) << 4) | (eError)) << 8) | ((eStart) << 4) | (eError);
        states[1] = ((((((3) << 4) | (4)) << 8) | ((5) << 4) | (6)) << 16) | ((((7) << 4) | (8)) << 8) | ((11) << 4) | (9);
        states[2] = ((((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (eError)) << 16) | ((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (eError);
        states[3] = ((((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (eError)) << 16) | ((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (eError);
        states[4] = ((((((eItsMe) << 4) | (eItsMe)) << 8) | ((eItsMe) << 4) | (eItsMe)) << 16) | ((((eItsMe) << 4) | (eItsMe)) << 8) | ((eItsMe) << 4) | (eItsMe);
        states[5] = ((((((eItsMe) << 4) | (eItsMe)) << 8) | ((eItsMe) << 4) | (eItsMe)) << 16) | ((((eItsMe) << 4) | (eItsMe)) << 8) | ((eItsMe) << 4) | (eItsMe);
        states[6] = ((((((eError) << 4) | (eError)) << 8) | ((5) << 4) | (5)) << 16) | ((((5) << 4) | (5)) << 8) | ((eError) << 4) | (eError);
        states[7] = ((((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (eError)) << 16) | ((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (eError);
        states[8] = ((((((eError) << 4) | (eError)) << 8) | ((5) << 4) | (5)) << 16) | ((((5) << 4) | (eError)) << 8) | ((eError) << 4) | (eError);
        states[9] = ((((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (eError)) << 16) | ((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (eError);
        states[10] = ((((((eError) << 4) | (eError)) << 8) | ((7) << 4) | (7)) << 16) | ((((7) << 4) | (7)) << 8) | ((eError) << 4) | (eError);
        states[11] = ((((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (eError)) << 16) | ((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (eError);
        states[12] = ((((((eError) << 4) | (eError)) << 8) | ((7) << 4) | (7)) << 16) | ((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (eError);
        states[13] = ((((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (eError)) << 16) | ((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (eError);
        states[14] = ((((((eError) << 4) | (eError)) << 8) | ((9) << 4) | (9)) << 16) | ((((9) << 4) | (9)) << 8) | ((eError) << 4) | (eError);
        states[15] = ((((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (eError)) << 16) | ((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (eError);
        states[16] = ((((((eError) << 4) | (eError)) << 8) | ((9) << 4) | (eError)) << 16) | ((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (eError);
        states[17] = ((((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (eError)) << 16) | ((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (eError);
        states[18] = ((((((eError) << 4) | (eError)) << 8) | ((12) << 4) | (12)) << 16) | ((((12) << 4) | (12)) << 8) | ((eError) << 4) | (eError);
        states[19] = ((((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (eError)) << 16) | ((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (eError);
        states[20] = ((((((eError) << 4) | (eError)) << 8) | ((12) << 4) | (eError)) << 16) | ((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (eError);
        states[21] = ((((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (eError)) << 16) | ((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (eError);
        states[22] = ((((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (12)) << 16) | ((((12) << 4) | (12)) << 8) | ((eError) << 4) | (eError);
        states[23] = ((((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (eError)) << 16) | ((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (eError);
        states[24] = ((((((eError) << 4) | (eError)) << 8) | ((eStart) << 4) | (eStart)) << 16) | ((((eStart) << 4) | (eStart)) << 8) | ((eError) << 4) | (eError);
        states[25] = ((((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (eError)) << 16) | ((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (eError);

        charset = "UTF-8";
        stFactor = 16;

    }

    public int[] cclass() {
        return cclass;
    }

    public int[] states() {
        return states;
    }

    public int stFactor() {
        return stFactor;
    }

    public String charset() {
        return charset;
    }

    public boolean isUCS2() {
        return false;
    }

}
