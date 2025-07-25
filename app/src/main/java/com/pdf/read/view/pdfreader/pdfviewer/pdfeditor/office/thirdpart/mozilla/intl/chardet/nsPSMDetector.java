package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.thirdpart.mozilla.intl.chardet;

public abstract class nsPSMDetector {

    public static final int ALL = 0;
    public static final int JAPANESE = 1;
    public static final int CHINESE = 2;
    public static final int SIMPLIFIED_CHINESE = 3;
    public static final int TRADITIONAL_CHINESE = 4;
    public static final int KOREAN = 5;

    public static final int NO_OF_LANGUAGES = 6;
    public static final int MAX_VERIFIERS = 16;

    nsVerifier[] mVerifier;
    nsEUCStatistics[] mStatisticsData;

    nsEUCSampler mSampler = new nsEUCSampler();
    byte[] mState = new byte[MAX_VERIFIERS];
    int[] mItemIdx = new int[MAX_VERIFIERS];

    int mItems;
    int mClassItems;

    boolean mDone;
    boolean mRunSampler;
    boolean mClassRunSampler;

    public nsPSMDetector() {
        initVerifiers(nsPSMDetector.ALL);
        Reset();
    }

    public nsPSMDetector(int langFlag) {
        initVerifiers(langFlag);
        Reset();
    }

    public nsPSMDetector(int aItems, nsVerifier[] aVerifierSet, nsEUCStatistics[] aStatisticsSet) {
        mClassRunSampler = (aStatisticsSet != null);
        mStatisticsData = aStatisticsSet;
        mVerifier = aVerifierSet;

        mClassItems = aItems;
        Reset();
    }

    public void Reset() {
        mRunSampler = mClassRunSampler;
        mDone = false;
        mItems = mClassItems;

        for (int i = 0; i < mItems; i++) {
            mState[i] = 0;
            mItemIdx[i] = i;
        }

        mSampler.Reset();
    }

    protected void initVerifiers(int currVerSet) {

        int idx = 0;
        int currVerifierSet;

        if (currVerSet >= 0 && currVerSet < NO_OF_LANGUAGES) {
            currVerifierSet = currVerSet;
        } else {
            currVerifierSet = nsPSMDetector.ALL;
        }

        mVerifier = null;
        mStatisticsData = null;

        if (currVerifierSet == nsPSMDetector.TRADITIONAL_CHINESE) {

            mVerifier = new nsVerifier[]{new nsUTF8Verifier(), new nsBIG5Verifier(),
                    new nsISO2022CNVerifier(), new nsEUCTWVerifier(), new nsCP1252Verifier(),
                    new nsUCS2BEVerifier(), new nsUCS2LEVerifier()};

            mStatisticsData = new nsEUCStatistics[]{null, new Big5Statistics(), null,
                    new EUCTWStatistics(), null, null, null};
        } else if (currVerifierSet == nsPSMDetector.KOREAN) {

            mVerifier = new nsVerifier[]{new nsUTF8Verifier(), new nsEUCKRVerifier(),
                    new nsISO2022KRVerifier(), new nsCP1252Verifier(), new nsUCS2BEVerifier(),
                    new nsUCS2LEVerifier()};
        } else if (currVerifierSet == nsPSMDetector.SIMPLIFIED_CHINESE) {

            mVerifier = new nsVerifier[]{new nsUTF8Verifier(), new nsGB2312Verifier(),
                    new nsGB18030Verifier(), new nsISO2022CNVerifier(), new nsHZVerifier(),
                    new nsCP1252Verifier(), new nsUCS2BEVerifier(), new nsUCS2LEVerifier()};
        } else if (currVerifierSet == nsPSMDetector.JAPANESE) {
            mVerifier = new nsVerifier[]{new nsUTF8Verifier(), new nsSJISVerifier(),
                    new nsEUCJPVerifier(), new nsISO2022JPVerifier(), new nsCP1252Verifier(),
                    new nsUCS2BEVerifier(), new nsUCS2LEVerifier()};
        } else if (currVerifierSet == nsPSMDetector.CHINESE) {

            mVerifier = new nsVerifier[]{new nsUTF8Verifier(), new nsGB2312Verifier(),
                    new nsGB18030Verifier(), new nsBIG5Verifier(), new nsISO2022CNVerifier(),
                    new nsHZVerifier(), new nsEUCTWVerifier(), new nsCP1252Verifier(),
                    new nsUCS2BEVerifier(), new nsUCS2LEVerifier()};

            mStatisticsData = new nsEUCStatistics[]{null, new GB2312Statistics(), null,
                    new Big5Statistics(), null, null, new EUCTWStatistics(), null, null, null};
        } else if (currVerifierSet == nsPSMDetector.ALL) {

            mVerifier = new nsVerifier[]{new nsUTF8Verifier(), new nsSJISVerifier(),
                    new nsEUCJPVerifier(), new nsISO2022JPVerifier(), new nsEUCKRVerifier(),
                    new nsISO2022KRVerifier(), new nsBIG5Verifier(), new nsEUCTWVerifier(),
                    new nsGB2312Verifier(), new nsGB18030Verifier(), new nsISO2022CNVerifier(),
                    new nsHZVerifier(), new nsCP1252Verifier(), new nsUCS2BEVerifier(),
                    new nsUCS2LEVerifier()};

            mStatisticsData = new nsEUCStatistics[]{null, null, new EUCJPStatistics(), null,
                    new EUCKRStatistics(), null, new Big5Statistics(), new EUCTWStatistics(),
                    new GB2312Statistics(), null, null, null, null, null, null};
        }

        mClassRunSampler = (mStatisticsData != null);
        mClassItems = mVerifier.length;

    }

    public abstract void Report(String charset);

    public boolean HandleData(byte[] aBuf, int len) {

        int i, j;
        byte b, st;

        for (i = 0; i < len; i++) {
            b = aBuf[i];
            for (j = 0; j < mItems; ) {
                st = nsVerifier.getNextState(mVerifier[mItemIdx[j]], b, mState[j]);
                if (st == nsVerifier.eItsMe) {
                    Report(mVerifier[mItemIdx[j]].charset());
                    mDone = true;
                    return mDone;
                } else if (st == nsVerifier.eError) {
                    mItems--;
                    if (j < mItems) {
                        mItemIdx[j] = mItemIdx[mItems];
                        mState[j] = mState[mItems];
                    }
                } else {
                    mState[j++] = st;
                }
            }
            if (mItems <= 1) {
                if (1 == mItems) {
                    Report(mVerifier[mItemIdx[0]].charset());
                }
                mDone = true;
                return mDone;
            } else {
                int nonUCS2Num = 0;
                int nonUCS2Idx = 0;
                for (j = 0; j < mItems; j++) {
                    if ((!(mVerifier[mItemIdx[j]].isUCS2()))
                            && (!(mVerifier[mItemIdx[j]].isUCS2()))) {
                        nonUCS2Num++;
                        nonUCS2Idx = j;
                    }
                }
                if (1 == nonUCS2Num) {
                    Report(mVerifier[mItemIdx[nonUCS2Idx]].charset());
                    mDone = true;
                    return mDone;
                }
            }
        }
        if (mRunSampler) {
            Sample(aBuf, len);
        }
        return mDone;
    }

    public void DataEnd() {
        if (mDone) {
            return;
        }

        if (mItems == 2) {
            if ((mVerifier[mItemIdx[0]].charset()).equals("GB18030")) {
                Report(mVerifier[mItemIdx[1]].charset());
                mDone = true;
            } else if ((mVerifier[mItemIdx[1]].charset()).equals("GB18030")) {
                Report(mVerifier[mItemIdx[0]].charset());
                mDone = true;
            }
        }
        if (mItems == 4) {
            if ((mVerifier[mItemIdx[1]].charset()).equals("Shift_JIS")) {
                Report(mVerifier[mItemIdx[1]].charset());
                mDone = true;
            }
        }
        if (mRunSampler) {
            Sample(null, 0, true);
        }
    }

    public void Sample(byte[] aBuf, int aLen) {
        Sample(aBuf, aLen, false);
    }

    public void Sample(byte[] aBuf, int aLen, boolean aLastChance) {
        int possibleCandidateNum = 0;
        int j;
        int eucNum = 0;

        for (j = 0; j < mItems; j++) {
            if (null != mStatisticsData[mItemIdx[j]])
                eucNum++;
            if ((!mVerifier[mItemIdx[j]].isUCS2())
                    && (!(mVerifier[mItemIdx[j]].charset()).equals("GB18030")))
                possibleCandidateNum++;
        }

        mRunSampler = (eucNum > 1);

        if (mRunSampler) {
            mRunSampler = mSampler.Sample(aBuf, aLen);
            if (((aLastChance && mSampler.GetSomeData()) || mSampler.EnoughData())
                    && (eucNum == possibleCandidateNum)) {
                mSampler.CalFreq();

                int bestIdx = -1;
                int eucCnt = 0;
                float bestScore = 0.0f;
                for (j = 0; j < mItems; j++) {
                    if ((null != mStatisticsData[mItemIdx[j]])
                            && (!(mVerifier[mItemIdx[j]].charset()).equals("Big5"))) {
                        float score = mSampler.GetScore(
                                mStatisticsData[mItemIdx[j]].mFirstByteFreq(),
                                mStatisticsData[mItemIdx[j]].mFirstByteWeight(),
                                mStatisticsData[mItemIdx[j]].mSecondByteFreq(),
                                mStatisticsData[mItemIdx[j]].mSecondByteWeight());

                        if ((0 == eucCnt++) || (bestScore > score)) {
                            bestScore = score;
                            bestIdx = j;
                        }
                    }
                }
                if (bestIdx >= 0) {
                    Report(mVerifier[mItemIdx[bestIdx]].charset());
                    mDone = true;
                }
            }
        }
    }

    public String[] getProbableCharsets() {
        if (mItems <= 0) {
            String[] nomatch = new String[1];
            nomatch[0] = "nomatch";
            return nomatch;
        }
        String[] ret = new String[mItems];
        for (int i = 0; i < mItems; i++) {
            ret[i] = mVerifier[mItemIdx[i]].charset();
        }
        return ret;
    }

}
