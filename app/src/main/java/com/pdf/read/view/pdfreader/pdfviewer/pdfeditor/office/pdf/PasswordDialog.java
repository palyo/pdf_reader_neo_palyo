package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.pdf;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.common.ICustomDialog;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.constant.EventConstant;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.pdf.PDFLib;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.res.ResKit;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.system.IControl;

public class PasswordDialog {

    private final IControl control;

    private final PDFLib lib;

    private AlertDialog.Builder alertBuilder;

    private EditText pwView;

    public PasswordDialog(IControl control, PDFLib lib) {
        this.control = control;
        this.lib = lib;
    }

    public void show() {
        if (control.getMainFrame().isShowPasswordDlg()) {
            alertBuilder = new AlertDialog.Builder(control.getActivity());
            requestPassword(lib);
        } else {
            ICustomDialog dlgListener = control.getCustomDialog();
            if (dlgListener != null) {
                dlgListener.showDialog(ICustomDialog.DIALOGTYPE_PASSWORD);
            }
        }
    }

    private void requestPassword(final PDFLib lib) {
        pwView = new EditText(control.getActivity());
        pwView.setInputType(EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
        pwView.setTransformationMethod(new PasswordTransformationMethod());

        AlertDialog alert = alertBuilder.create();
        alert.setTitle(ResKit.instance().getLocalString("DIALOG_ENTER_PASSWORD"));
        alert.setView(pwView);
        alert.setButton(AlertDialog.BUTTON_POSITIVE,
                ResKit.instance().getLocalString("BUTTON_OK"),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if (lib.authenticatePasswordSync(pwView.getText().toString())) {
                            control.actionEvent(EventConstant.APP_PASSWORD_OK_INIT, null);
                        } else {
                            requestPassword(lib);
                        }
                    }
                });
        alert.setButton(AlertDialog.BUTTON_NEGATIVE,
                ResKit.instance().getLocalString("BUTTON_CANCEL"),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        control.getActivity().onBackPressed();

                    }
                });

        alert.setOnKeyListener(new DialogInterface.OnKeyListener() {
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                    control.getActivity().onBackPressed();

                    return true;
                }
                return false;
            }
        });
        alert.show();
    }
}
