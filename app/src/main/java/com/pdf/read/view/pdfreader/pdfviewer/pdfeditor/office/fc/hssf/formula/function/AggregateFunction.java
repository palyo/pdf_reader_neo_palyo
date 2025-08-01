package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.ErrorEval;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.EvaluationException;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.NumberEval;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.OperandResolver;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval.ValueEval;

public abstract class AggregateFunction extends MultiOperandNumericFunction {

    public static final Function AVEDEV = new AggregateFunction() {
        protected double evaluate(double[] values) {
            return StatsLib.avedev(values);
        }
    };
    public static final Function AVERAGE = new AggregateFunction() {
        protected double evaluate(double[] values) throws EvaluationException {
            if (values.length < 1) {
                throw new EvaluationException(ErrorEval.DIV_ZERO);
            }
            return MathX.average(values);
        }
    };
    public static final Function DEVSQ = new AggregateFunction() {
        protected double evaluate(double[] values) {
            return StatsLib.devsq(values);
        }
    };
    public static final Function LARGE = new LargeSmall(true);
    public static final Function MAX = new AggregateFunction() {
        protected double evaluate(double[] values) {
            return values.length > 0 ? MathX.max(values) : 0;
        }
    };
    public static final Function MEDIAN = new AggregateFunction() {
        protected double evaluate(double[] values) {
            return StatsLib.median(values);
        }
    };
    public static final Function MIN = new AggregateFunction() {
        protected double evaluate(double[] values) {
            return values.length > 0 ? MathX.min(values) : 0;
        }
    };
    public static final Function PRODUCT = new AggregateFunction() {
        protected double evaluate(double[] values) {
            return MathX.product(values);
        }
    };
    public static final Function SMALL = new LargeSmall(false);
    public static final Function STDEV = new AggregateFunction() {
        protected double evaluate(double[] values) throws EvaluationException {
            if (values.length < 1) {
                throw new EvaluationException(ErrorEval.DIV_ZERO);
            }
            return StatsLib.stdev(values);
        }
    };
    public static final Function SUM = new AggregateFunction() {
        protected double evaluate(double[] values) {
            return MathX.sum(values);
        }
    };
    public static final Function SUMSQ = new AggregateFunction() {
        protected double evaluate(double[] values) {
            return MathX.sumsq(values);
        }
    };
    public static final Function VAR = new AggregateFunction() {
        protected double evaluate(double[] values) throws EvaluationException {
            if (values.length < 1) {
                throw new EvaluationException(ErrorEval.DIV_ZERO);
            }
            return StatsLib.var(values);
        }
    };
    public static final Function VARP = new AggregateFunction() {
        protected double evaluate(double[] values) throws EvaluationException {
            if (values.length < 1) {
                throw new EvaluationException(ErrorEval.DIV_ZERO);
            }
            return StatsLib.varp(values);
        }
    };
    public static final Function DB = new AggregateFunction() {
        protected double evaluate(double[] values) throws EvaluationException {
            checkParas(values);

            int length = values.length;

            if (length == 4) {
                return db(values[0], values[1], values[2], values[3], 12);
            } else if (length == 5) {
                return db(values[0], values[1], values[2], values[3], values[4]);
            } else {
                throw new EvaluationException(ErrorEval.NA);
            }
        }

        private void checkParas(double[] values) throws EvaluationException {
            int length = values.length;

            if (length == 4 || length == 5) {
                if (values[2] <= 0 || values[3] <= 0 || values[3] - values[2] > 1) {
                    throw new EvaluationException(ErrorEval.NA);
                }

                if (length == 5 && (values[4] > 12 || values[4] <= 0)) {
                    throw new EvaluationException(ErrorEval.NA);
                }
            } else {
                throw new EvaluationException(ErrorEval.NA);
            }
        }

        private double db(double cost, double salvage, double life, double period, double month) throws EvaluationException {
            if (Math.abs(period - (int) period) > 0.001) {
                throw new EvaluationException(ErrorEval.NA);
            }

            double rate = 1 - Math.pow(salvage / cost, 1 / life);
            rate = Math.round((float) rate * 1000) / 1000.0;

            if (Math.abs(period - 1) < 0.001) {

                return cost * rate * month / 12;
            } else {

                double d = cost * rate * month / 12;
                cost -= d;

                if (period <= life) {
                    int i = 2;
                    while (i <= period) {
                        d = cost * rate;
                        cost -= d;
                        i++;
                    }
                    return d;
                } else if (period - life <= 1) {

                    if (Math.abs(month - 12) < 0.001) {
                        return 0;
                    } else {
                        int i = 2;
                        while (i <= life) {
                            d = cost * rate;
                            cost -= d;
                            i++;
                        }
                        return (cost * rate * (12 - month)) / 12;
                    }
                }
            }

            throw new EvaluationException(ErrorEval.NA);
        }
    };
    public static final Function DDB = new AggregateFunction() {
        protected double evaluate(double[] values) throws EvaluationException {
            checkParas(values);

            int length = values.length;

            if (length == 4) {
                return ddb(values[0], values[1], values[2], values[3], 2);
            } else if (length == 5) {
                return ddb(values[0], values[1], values[2], values[3], values[4]);
            } else {
                throw new EvaluationException(ErrorEval.NA);
            }
        }

        private void checkParas(double[] values) throws EvaluationException {
            int length = values.length;

            if (length == 4 || length == 5) {
                if (values[2] <= 0 || values[3] <= 0 || values[3] > values[2]) {
                    throw new EvaluationException(ErrorEval.NA);
                }

            } else {
                throw new EvaluationException(ErrorEval.NA);
            }
        }

        private double ddb(double cost, double salvage, double life, double period, double factor) throws EvaluationException {
            if (Math.abs(period - (int) period) > 0.001) {
                throw new EvaluationException(ErrorEval.NA);
            }

            double rate = factor / life;
            rate = Math.round((float) rate * 1000) / 1000.0;

            int i = 2;
            double d = Math.min(cost * rate, cost - salvage);
            while (i <= period) {
                d = Math.min(cost * rate, cost - salvage);
                cost -= d;
                i++;
            }
            return d;
        }
    };

    protected AggregateFunction() {
        super(false, false);
    }

    static Function subtotalInstance(Function func) {
        final AggregateFunction arg = (AggregateFunction) func;
        return new AggregateFunction() {
            @Override
            protected double evaluate(double[] values) throws EvaluationException {
                return arg.evaluate(values);
            }

            @Override
            public boolean isSubtotalCounted() {
                return false;
            }

        };
    }

    private static final class LargeSmall extends Fixed2ArgFunction {
        private final boolean _isLarge;

        private LargeSmall(boolean isLarge) {
            _isLarge = isLarge;
        }

        public ValueEval evaluate(int srcRowIndex, int srcColumnIndex, ValueEval arg0,
                                  ValueEval arg1) {
            double dn;
            try {
                ValueEval ve1 = OperandResolver.getSingleValue(arg1, srcRowIndex, srcColumnIndex);
                dn = OperandResolver.coerceValueToDouble(ve1);
            } catch (EvaluationException e1) {

                return ErrorEval.VALUE_INVALID;
            }

            if (dn < 1.0) {

                return ErrorEval.NUM_ERROR;
            }

            int k = (int) Math.ceil(dn);

            double result;
            try {
                double[] ds = ValueCollector.collectValues(arg0);
                if (k > ds.length) {
                    return ErrorEval.NUM_ERROR;
                }
                result = _isLarge ? StatsLib.kthLargest(ds, k) : StatsLib.kthSmallest(ds, k);
                NumericFunction.checkValue(result);
            } catch (EvaluationException e) {
                return e.getErrorEval();
            }

            return new NumberEval(result);
        }
    }

    static final class ValueCollector extends MultiOperandNumericFunction {
        private static final ValueCollector instance = new ValueCollector();

        public ValueCollector() {
            super(false, false);
        }

        public static double[] collectValues(ValueEval... operands) throws EvaluationException {
            return instance.getNumberArray(operands);
        }

        protected double evaluate(double[] values) {
            throw new IllegalStateException("should not be called");
        }
    }
}
