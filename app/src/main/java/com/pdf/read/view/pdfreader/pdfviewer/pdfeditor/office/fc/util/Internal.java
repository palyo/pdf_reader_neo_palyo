package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Documented
@Retention(RetentionPolicy.RUNTIME)
public @interface Internal {
    String value() default "";
}
