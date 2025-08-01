package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.ss.usermodel;

public interface DataValidationConstraint {

    int getValidationType();

    int getOperator();

    void setOperator(int operator);

    String[] getExplicitListValues();

    void setExplicitListValues(String[] explicitListValues);

    String getFormula1();

    void setFormula1(String formula1);

    String getFormula2();

    void setFormula2(String formula2);

    final class ValidationType {

        public static final int ANY = 0x00;

        public static final int INTEGER = 0x01;

        public static final int DECIMAL = 0x02;

        public static final int LIST = 0x03;

        public static final int DATE = 0x04;

        public static final int TIME = 0x05;

        public static final int TEXT_LENGTH = 0x06;

        public static final int FORMULA = 0x07;

        private ValidationType() {

        }
    }

    final class OperatorType {
        public static final int BETWEEN = 0x00;
        public static final int NOT_BETWEEN = 0x01;
        public static final int EQUAL = 0x02;
        public static final int NOT_EQUAL = 0x03;
        public static final int GREATER_THAN = 0x04;
        public static final int LESS_THAN = 0x05;
        public static final int GREATER_OR_EQUAL = 0x06;
        public static final int LESS_OR_EQUAL = 0x07;

        public static final int IGNORED = BETWEEN;

        private OperatorType() {

        }

        public static void validateSecondArg(int comparisonOperator, String paramValue) {
            switch (comparisonOperator) {
                case BETWEEN:
                case NOT_BETWEEN:
                    if (paramValue == null) {
                        throw new IllegalArgumentException("expr2 must be supplied for 'between' comparisons");
                    }

            }
        }
    }
}
