package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.mozilla.intl.chardet;

public class nsBIG5Verifier extends nsVerifier {

    static int[] cclass;
    static int[] states;
    static int stFactor;
    static String charset;

    public nsBIG5Verifier() {

        cclass = new int[256 / 8];

        cclass[0] = ((((((1) << 4) | (1)) << 8) | ((1) << 4) | (1)) << 16) | ((((1) << 4) | (1)) << 8) | ((1) << 4) | (1);
        cclass[1] = ((((((0) << 4) | (0)) << 8) | ((1) << 4) | (1)) << 16) | ((((1) << 4) | (1)) << 8) | ((1) << 4) | (1);
        cclass[2] = ((((((1) << 4) | (1)) << 8) | ((1) << 4) | (1)) << 16) | ((((1) << 4) | (1)) << 8) | ((1) << 4) | (1);
        cclass[3] = ((((((1) << 4) | (1)) << 8) | ((1) << 4) | (1)) << 16) | ((((0) << 4) | (1)) << 8) | ((1) << 4) | (1);
        cclass[4] = ((((((1) << 4) | (1)) << 8) | ((1) << 4) | (1)) << 16) | ((((1) << 4) | (1)) << 8) | ((1) << 4) | (1);
        cclass[5] = ((((((1) << 4) | (1)) << 8) | ((1) << 4) | (1)) << 16) | ((((1) << 4) | (1)) << 8) | ((1) << 4) | (1);
        cclass[6] = ((((((1) << 4) | (1)) << 8) | ((1) << 4) | (1)) << 16) | ((((1) << 4) | (1)) << 8) | ((1) << 4) | (1);
        cclass[7] = ((((((1) << 4) | (1)) << 8) | ((1) << 4) | (1)) << 16) | ((((1) << 4) | (1)) << 8) | ((1) << 4) | (1);
        cclass[8] = ((((((2) << 4) | (2)) << 8) | ((2) << 4) | (2)) << 16) | ((((2) << 4) | (2)) << 8) | ((2) << 4) | (2);
        cclass[9] = ((((((2) << 4) | (2)) << 8) | ((2) << 4) | (2)) << 16) | ((((2) << 4) | (2)) << 8) | ((2) << 4) | (2);
        cclass[10] = ((((((2) << 4) | (2)) << 8) | ((2) << 4) | (2)) << 16) | ((((2) << 4) | (2)) << 8) | ((2) << 4) | (2);
        cclass[11] = ((((((2) << 4) | (2)) << 8) | ((2) << 4) | (2)) << 16) | ((((2) << 4) | (2)) << 8) | ((2) << 4) | (2);
        cclass[12] = ((((((2) << 4) | (2)) << 8) | ((2) << 4) | (2)) << 16) | ((((2) << 4) | (2)) << 8) | ((2) << 4) | (2);
        cclass[13] = ((((((2) << 4) | (2)) << 8) | ((2) << 4) | (2)) << 16) | ((((2) << 4) | (2)) << 8) | ((2) << 4) | (2);
        cclass[14] = ((((((2) << 4) | (2)) << 8) | ((2) << 4) | (2)) << 16) | ((((2) << 4) | (2)) << 8) | ((2) << 4) | (2);
        cclass[15] = ((((((1) << 4) | (2)) << 8) | ((2) << 4) | (2)) << 16) | ((((2) << 4) | (2)) << 8) | ((2) << 4) | (2);
        cclass[16] = ((((((4) << 4) | (4)) << 8) | ((4) << 4) | (4)) << 16) | ((((4) << 4) | (4)) << 8) | ((4) << 4) | (4);
        cclass[17] = ((((((4) << 4) | (4)) << 8) | ((4) << 4) | (4)) << 16) | ((((4) << 4) | (4)) << 8) | ((4) << 4) | (4);
        cclass[18] = ((((((4) << 4) | (4)) << 8) | ((4) << 4) | (4)) << 16) | ((((4) << 4) | (4)) << 8) | ((4) << 4) | (4);
        cclass[19] = ((((((4) << 4) | (4)) << 8) | ((4) << 4) | (4)) << 16) | ((((4) << 4) | (4)) << 8) | ((4) << 4) | (4);
        cclass[20] = ((((((3) << 4) | (3)) << 8) | ((3) << 4) | (3)) << 16) | ((((3) << 4) | (3)) << 8) | ((3) << 4) | (4);
        cclass[21] = ((((((3) << 4) | (3)) << 8) | ((3) << 4) | (3)) << 16) | ((((3) << 4) | (3)) << 8) | ((3) << 4) | (3);
        cclass[22] = ((((((3) << 4) | (3)) << 8) | ((3) << 4) | (3)) << 16) | ((((3) << 4) | (3)) << 8) | ((3) << 4) | (3);
        cclass[23] = ((((((3) << 4) | (3)) << 8) | ((3) << 4) | (3)) << 16) | ((((3) << 4) | (3)) << 8) | ((3) << 4) | (3);
        cclass[24] = ((((((3) << 4) | (3)) << 8) | ((3) << 4) | (3)) << 16) | ((((3) << 4) | (3)) << 8) | ((3) << 4) | (3);
        cclass[25] = ((((((3) << 4) | (3)) << 8) | ((3) << 4) | (3)) << 16) | ((((3) << 4) | (3)) << 8) | ((3) << 4) | (3);
        cclass[26] = ((((((3) << 4) | (3)) << 8) | ((3) << 4) | (3)) << 16) | ((((3) << 4) | (3)) << 8) | ((3) << 4) | (3);
        cclass[27] = ((((((3) << 4) | (3)) << 8) | ((3) << 4) | (3)) << 16) | ((((3) << 4) | (3)) << 8) | ((3) << 4) | (3);
        cclass[28] = ((((((3) << 4) | (3)) << 8) | ((3) << 4) | (3)) << 16) | ((((3) << 4) | (3)) << 8) | ((3) << 4) | (3);
        cclass[29] = ((((((3) << 4) | (3)) << 8) | ((3) << 4) | (3)) << 16) | ((((3) << 4) | (3)) << 8) | ((3) << 4) | (3);
        cclass[30] = ((((((3) << 4) | (3)) << 8) | ((3) << 4) | (3)) << 16) | ((((3) << 4) | (3)) << 8) | ((3) << 4) | (3);
        cclass[31] = ((((((0) << 4) | (3)) << 8) | ((3) << 4) | (3)) << 16) | ((((3) << 4) | (3)) << 8) | ((3) << 4) | (3);

        states = new int[3];

        states[0] = ((((((eError) << 4) | (eError)) << 8) | ((eError) << 4) | (eError)) << 16) | ((((3) << 4) | (eStart)) << 8) | ((eStart) << 4) | (eError);
        states[1] = ((((((eError) << 4) | (eItsMe)) << 8) | ((eItsMe) << 4) | (eItsMe)) << 16) | ((((eItsMe) << 4) | (eItsMe)) << 8) | ((eError) << 4) | (eError);
        states[2] = ((((((eStart) << 4) | (eStart)) << 8) | ((eStart) << 4) | (eStart)) << 16) | ((((eStart) << 4) | (eStart)) << 8) | ((eStart) << 4) | (eError);

        charset = "Big5";
        stFactor = 5;

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
