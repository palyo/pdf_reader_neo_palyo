package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pg.animate;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.IOfficeToPicture;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.EventConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IControl;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.ITimerListener;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.beans.ATimer;

public class AnimationManager implements ITimerListener {
    private IAnimation animation;
    private ATimer timer;
    private int actionIndex;
    private IControl control;

    public AnimationManager(IControl control) {
        this.control = control;
    }

    public void setAnimation(IAnimation animation) {

        if (this.animation != null && timer != null && timer.isRunning()) {
            timer.stop();
            this.animation.stop();
        }

        this.animation = animation;
    }

    public void beginAnimation(int delay) {
        if (timer == null) {
            timer = new ATimer(delay, this);
        }

        if (animation != null) {
            actionIndex = 0;
            animation.start();

            timer.start();
            if (control.getOfficeToPicture() != null) {
                control.getOfficeToPicture().setModeType(IOfficeToPicture.VIEW_CHANGING);
            }
        }
    }

    public void restartAnimationTimer() {
        if (timer != null) {
            timer.restart();
        }
    }

    public void killAnimationTimer() {
        if (timer != null) {
            timer.stop();
        }
    }

    public void stopAnimation() {
        if (animation != null) {
            if (timer != null) {
                timer.stop();
            }
            if (animation != null) {
                animation.stop();
            }

            if (control.getOfficeToPicture() != null) {
                control.getOfficeToPicture().setModeType(IOfficeToPicture.VIEW_CHANGE_END);
            }
            control.actionEvent(EventConstant.PG_REPAINT_ID, null);
        }
    }

    public void actionPerformed() {
        if (animation != null && animation.getAnimationStatus() != Animation.AnimStatus_End) {
            animation.animation(++actionIndex);

            control.actionEvent(EventConstant.PG_REPAINT_ID, null);

            if (timer != null) {
                timer.restart();
            }
        } else {
            if (timer != null) {
                timer.stop();
            }
            if (control.getOfficeToPicture() != null) {
                control.getOfficeToPicture().setModeType(IOfficeToPicture.VIEW_CHANGE_END);
            }
            control.actionEvent(EventConstant.APP_GENERATED_PICTURE_ID, null);
        }

    }

    public boolean hasStoped() {
        if (animation != null) {
            return animation.getAnimationStatus() == Animation.AnimStatus_End;
        }

        return true;
    }

    public void dispose() {
        control = null;
        animation = null;
        if (timer != null) {
            timer.dispose();
            timer = null;
        }
    }
}
