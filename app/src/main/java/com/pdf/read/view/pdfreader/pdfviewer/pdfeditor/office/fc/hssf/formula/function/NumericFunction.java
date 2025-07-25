package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.BoolEval;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.ErrorEval;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.EvaluationException;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.NumberEval;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.OperandResolver;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.ValueEval;

public abstract class NumericFunction implements Function {

    public static final Function ABS = new OneArg() {
        protected double evaluate(double d) {
            return Math.abs(d);
        }
    };
    public static final Function ACOS = new OneArg() {
        protected double evaluate(double d) {
            return Math.acos(d);
        }
    };
    public static final Function ACOSH = new OneArg() {
        protected double evaluate(double d) {
            return MathX.acosh(d);
        }
    };
    public static final Function ASIN = new OneArg() {
        protected double evaluate(double d) {
            return Math.asin(d);
        }
    };
    public static final Function ASINH = new OneArg() {
        protected double evaluate(double d) {
            return MathX.asinh(d);
        }
    };
    public static final Function ATAN = new OneArg() {
        protected double evaluate(double d) {
            return Math.atan(d);
        }
    };
    public static final Function ATANH = new OneArg() {
        protected double evaluate(double d) {
            return MathX.atanh(d);
        }
    };

    public static final Function COS = new OneArg() {
        protected double evaluate(double d) {
            return Math.cos(d);
        }
    };
    public static final Function COSH = new OneArg() {
        protected double evaluate(double d) {
            return MathX.cosh(d);
        }
    };

    public static final Function DEGREES = new OneArg() {
        protected double evaluate(double d) {
            return Math.toDegrees(d);
        }
    };
    public static final Function EXP = new OneArg() {
        protected double evaluate(double d) {
            return Math.pow(Math.E, d);
        }
    };
    public static final Function FACT = new OneArg() {
        protected double evaluate(double d) {
            return MathX.factorial((int) d);
        }
    };
    public static final Function INT = new OneArg() {
        protected double evaluate(double d) {
            return Math.round(d - 0.5);
        }
    };
    public static final Function LN = new OneArg() {
        protected double evaluate(double d) {
            return Math.log(d);
        }
    };
    public static final Function RADIANS = new OneArg() {
        protected double evaluate(double d) {
            return Math.toRadians(d);
        }
    };
    public static final Function SIGN = new OneArg() {
        protected double evaluate(double d) {
            return MathX.sign(d);
        }
    };
    public static final Function SIN = new OneArg() {
        protected double evaluate(double d) {
            return Math.sin(d);
        }
    };
    public static final Function SINH = new OneArg() {
        protected double evaluate(double d) {
            return MathX.sinh(d);
        }
    };
    public static final Function SQRT = new OneArg() {
        protected double evaluate(double d) {
            return Math.sqrt(d);
        }
    };
    public static final Function TAN = new OneArg() {
        protected double evaluate(double d) {
            return Math.tan(d);
        }
    };
    public static final Function TANH = new OneArg() {
        protected double evaluate(double d) {
            return MathX.tanh(d);
        }
    };
    public static final Function CEILING = new TwoArg() {
        protected double evaluate(double d0, double d1) {
            return MathX.ceiling(d0, d1);
        }
    };
    public static final Function COMBIN = new TwoArg() {
        protected double evaluate(double d0, double d1) throws EvaluationException {
            if (d0 > Integer.MAX_VALUE || d1 > Integer.MAX_VALUE) {
                throw new EvaluationException(ErrorEval.NUM_ERROR);
            }
            return MathX.nChooseK((int) d0, (int) d1);
        }
    };
    public static final Function POWER = new TwoArg() {
        protected double evaluate(double d0, double d1) {
            return Math.pow(d0, d1);
        }
    };
    public static final Function ROUND = new TwoArg() {
        protected double evaluate(double d0, double d1) {
            return MathX.round(d0, (int) d1);
        }
    };
    public static final Function ROUNDDOWN = new TwoArg() {
        protected double evaluate(double d0, double d1) {
            return MathX.roundDown(d0, (int) d1);
        }
    };
    public static final Function ROUNDUP = new TwoArg() {
        protected double evaluate(double d0, double d1) {
            return MathX.roundUp(d0, (int) d1);
        }
    };
    public static final Function LOG = new Log();
    public static final Function RAND = new Fixed0ArgFunction() {
        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex) {
            return new NumberEval(Math.random());
        }
    };
    public static final Function POISSON = new Fixed3ArgFunction() {

        private final static double DEFAULT_RETURN_RESULT = 1;
        private final long[] FACTORIALS = new long[]{
                1L, 1L, 2L,
                6L, 24L, 120L,
                720L, 5040L, 40320L,
                362880L, 3628800L, 39916800L,
                479001600L, 6227020800L, 87178291200L,
                1307674368000L, 20922789888000L, 355687428096000L,
                6402373705728000L, 121645100408832000L, 2432902008176640000L};

        private boolean isDefaultResult(double x, double mean) {

            return x == 0 && mean == 0;
        }

        private boolean checkArgument(double aDouble) throws EvaluationException {

            NumericFunction.checkValue(aDouble);

            if (aDouble < 0) {
                throw new EvaluationException(ErrorEval.NUM_ERROR);
            }

            return true;
        }

        private double probability(int k, double lambda) {
            return Math.pow(lambda, k) * Math.exp(-lambda) / factorial(k);
        }

        private double cumulativeProbability(int x, double lambda) {
            double result = 0;
            for (int k = 0; k <= x; k++) {
                result += probability(k, lambda);
            }
            return result;
        }

        public long factorial(final int n) {
            if (n < 0 || n > 20) {
                throw new IllegalArgumentException("Valid argument should be in the range [0..20]");
            }
            return FACTORIALS[n];
        }

        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1, ValueEval arg2) {

            double mean = 0;
            double x = 0;
            boolean cumulative = ((BoolEval) arg2).getBooleanValue();
            double result = 0;

            try {
                x = NumericFunction.singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex);
                mean = NumericFunction.singleOperandEvaluate(arg1, srcRowIndex, srcColumnIndex);

                if (isDefaultResult(x, mean)) {
                    return new NumberEval(DEFAULT_RETURN_RESULT);
                }
                checkArgument(x);
                checkArgument(mean);

                if (cumulative) {
                    result = cumulativeProbability((int) x, mean);
                } else {
                    result = probability((int) x, mean);
                }

                NumericFunction.checkValue(result);

            } catch (EvaluationException e) {
                return e.getErrorEval();
            }

            return new NumberEval(result);

        }
    };
    static final double ZERO = 0.0;
    public static final Function ATAN2 = new TwoArg() {
        protected double evaluate(double d0, double d1) throws EvaluationException {
            if (d0 == ZERO && d1 == ZERO) {
                throw new EvaluationException(ErrorEval.DIV_ZERO);
            }
            return Math.atan2(d1, d0);
        }
    };
    public static final Function FLOOR = new TwoArg() {
        protected double evaluate(double d0, double d1) throws EvaluationException {
            if (d1 == ZERO) {
                if (d0 == ZERO) {
                    return ZERO;
                }
                throw new EvaluationException(ErrorEval.DIV_ZERO);
            }
            return MathX.floor(d0, d1);
        }
    };

    public static final Function MOD = new TwoArg() {
        protected double evaluate(double d0, double d1) throws EvaluationException {
            if (d1 == ZERO) {
                throw new EvaluationException(ErrorEval.DIV_ZERO);
            }
            return MathX.mod(d0, d1);
        }
    };
    static final double TEN = 10.0;
    static final double LOG_10_TO_BASE_e = Math.log(TEN);
    public static final Function LOG10 = new OneArg() {
        protected double evaluate(double d) {
            return Math.log(d) / LOG_10_TO_BASE_e;
        }
    };
    static final NumberEval DOLLAR_ARG2_DEFAULT = new NumberEval(2.0);
    public static final Function DOLLAR = new Var1or2ArgFunction() {
        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
            return evaluate(srcRowIndex, srcColumnIndex, arg0, DOLLAR_ARG2_DEFAULT);
        }

        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0,
                                  ValueEval arg1) {
            double val;
            double d1;
            try {
                val = singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex);
                d1 = singleOperandEvaluate(arg1, srcRowIndex, srcColumnIndex);
            } catch (EvaluationException e) {
                return e.getErrorEval();
            }
            int nPlaces = (int) d1;

            if (nPlaces > 127) {
                return ErrorEval.VALUE_INVALID;
            }

            return new NumberEval(val);
        }
    };
    static final NumberEval TRUNC_ARG2_DEFAULT = new NumberEval(0);
    public static final Function TRUNC = new Var1or2ArgFunction() {

        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
            return evaluate(srcRowIndex, srcColumnIndex, arg0, TRUNC_ARG2_DEFAULT);
        }

        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
            double result;
            try {
                double d0 = singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex);
                double d1 = singleOperandEvaluate(arg1, srcRowIndex, srcColumnIndex);
                double multi = Math.pow(10d, d1);
                result = Math.floor(d0 * multi) / multi;
                checkValue(result);
            } catch (EvaluationException e) {
                return e.getErrorEval();
            }
            return new NumberEval(result);
        }
    };
    static final NumberEval PI_EVAL = new NumberEval(Math.PI);
    public static final Function PI = new Fixed0ArgFunction() {
        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex) {
            return PI_EVAL;
        }
    };

    protected static final double singleOperandEvaluate(ValueEval arg, int srcRowIndex, int srcColumnIndex) throws EvaluationException {
        if (arg == null) {
            throw new IllegalArgumentException("arg must not be null");
        }
        ValueEval ve = OperandResolver.getSingleValue(arg, srcRowIndex, srcColumnIndex);
        double result = OperandResolver.coerceValueToDouble(ve);
        checkValue(result);
        return result;
    }

    public static final void checkValue(double result) throws EvaluationException {
        if (Double.isNaN(result) || Double.isInfinite(result)) {
            throw new EvaluationException(ErrorEval.NUM_ERROR);
        }
    }

    public final ValueEval evaluate(ValueEval[] args, int srcCellRow, int srcCellCol) {
        double result;
        try {
            result = eval(args, srcCellRow, srcCellCol);
            checkValue(result);
        } catch (EvaluationException e) {
            return e.getErrorEval();
        }
        return new NumberEval(result);
    }

    protected abstract double eval(ValueEval[] args, int srcCellRow, int srcCellCol) throws EvaluationException;

    public static abstract class OneArg extends Fixed1ArgFunction {
        protected OneArg() {
        }

        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
            double result;
            try {
                double d = singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex);
                result = evaluate(d);
                checkValue(result);
            } catch (EvaluationException e) {
                return e.getErrorEval();
            }
            return new NumberEval(result);
        }

        protected final double eval(ValueEval[] args, int srcCellRow, int srcCellCol) throws EvaluationException {
            if (args.length != 1) {
                throw new EvaluationException(ErrorEval.VALUE_INVALID);
            }
            double d = singleOperandEvaluate(args[0], srcCellRow, srcCellCol);
            return evaluate(d);
        }

        protected abstract double evaluate(double d) throws EvaluationException;
    }

    public static abstract class TwoArg extends Fixed2ArgFunction {
        protected TwoArg() {
        }

        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0, ValueEval arg1) {
            double result;
            try {
                double d0 = singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex);
                double d1 = singleOperandEvaluate(arg1, srcRowIndex, srcColumnIndex);
                result = evaluate(d0, d1);
                checkValue(result);
            } catch (EvaluationException e) {
                return e.getErrorEval();
            }
            return new NumberEval(result);
        }

        protected abstract double evaluate(double d0, double d1) throws EvaluationException;
    }

    private static final class Log extends Var1or2ArgFunction {
        public Log() {
        }

        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0) {
            double result;
            try {
                double d0 = NumericFunction.singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex);
                result = Math.log(d0) / LOG_10_TO_BASE_e;
                NumericFunction.checkValue(result);
            } catch (EvaluationException e) {
                return e.getErrorEval();
            }
            return new NumberEval(result);
        }

        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0,
                                  ValueEval arg1) {
            double result;
            try {
                double d0 = NumericFunction.singleOperandEvaluate(arg0, srcRowIndex, srcColumnIndex);
                double d1 = NumericFunction.singleOperandEvaluate(arg1, srcRowIndex, srcColumnIndex);
                double logE = Math.log(d0);
                double base = d1;
                if (base == Math.E) {
                    result = logE;
                } else {
                    result = logE / Math.log(base);
                }
                NumericFunction.checkValue(result);
            } catch (EvaluationException e) {
                return e.getErrorEval();
            }
            return new NumberEval(result);
        }
    }
}
