package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.beans;

import java.util.Timer;
import java.util.TimerTask;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.ITimerListener;

public class ATimer {

    private final int delay;

    private boolean isRunning;

    private Timer timer;

    private ITimerListener listener;

    public ATimer(int delay, ITimerListener listener) {
        super();
        this.delay = delay;
        this.listener = listener;
    }

    public void start() {
        if (isRunning) {
            return;
        }
        timer = new Timer();
        timer.schedule(new ATimerTask(), delay);
        isRunning = true;
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    public void stop() {
        if (isRunning) {
            timer.cancel();
            timer.purge();
            isRunning = false;
        }
    }

    public void restart() {
        stop();
        start();
    }

    public void dispose() {
        if (isRunning) {
            timer.cancel();
            timer.purge();
            isRunning = false;
        }
        timer = null;
        listener = null;
    }

    class ATimerTask extends TimerTask {
        public ATimerTask() {
        }

        public void run() {
            try {
                timer.schedule(new ATimerTask(), delay);
                listener.actionPerformed();
            } catch (Exception e) {
            }
        }

    }
}
