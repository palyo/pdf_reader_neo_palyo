package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.eval;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Address;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.AggregateFunction;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Averagea;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.BooleanFunction;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.CalendarFieldFunction;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Choose;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Column;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Columns;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Count;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Counta;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Countblank;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Countif;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.DateFunc;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Days360;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Errortype;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Even;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.FinanceFunction;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Function;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.FunctionMetadata;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.FunctionMetadataRegistry;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Hlookup;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Hyperlink;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.IfFunc;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Index;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Irr;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.LogicalFunction;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Lookup;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Match;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.MinaMaxa;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Mode;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Na;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.NotImplementedFunction;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Now;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Npv;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.NumericFunction;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Odd;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Offset;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Replace;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.RowFunc;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Rows;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Substitute;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Subtotal;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Sumif;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Sumproduct;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Sumx2my2;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Sumx2py2;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Sumxmy2;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.T;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.TextFunction;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.TimeFunc;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Today;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Value;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Vlookup;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hssf.formula.function.Weekday;

public final class FunctionEval {
    private static final FunctionID ID = null;
    private static final Function[] functions = produceFunctions();

    private static Function[] produceFunctions() {
        Function[] retval = new Function[368];

        retval[0] = new Count();
        retval[FunctionID.IF] = new IfFunc();
        retval[2] = LogicalFunction.ISNA;
        retval[3] = LogicalFunction.ISERROR;
        retval[FunctionID.SUM] = AggregateFunction.SUM;
        retval[5] = AggregateFunction.AVERAGE;
        retval[6] = AggregateFunction.MIN;
        retval[7] = AggregateFunction.MAX;
        retval[8] = new RowFunc();
        retval[9] = new Column();
        retval[10] = new Na();
        retval[11] = new Npv();
        retval[12] = AggregateFunction.STDEV;
        retval[13] = NumericFunction.DOLLAR;

        retval[15] = NumericFunction.SIN;
        retval[16] = NumericFunction.COS;
        retval[17] = NumericFunction.TAN;
        retval[18] = NumericFunction.ATAN;
        retval[19] = NumericFunction.PI;
        retval[20] = NumericFunction.SQRT;
        retval[21] = NumericFunction.EXP;
        retval[22] = NumericFunction.LN;
        retval[23] = NumericFunction.LOG10;
        retval[24] = NumericFunction.ABS;
        retval[25] = NumericFunction.INT;
        retval[26] = NumericFunction.SIGN;
        retval[27] = NumericFunction.ROUND;
        retval[28] = new Lookup();
        retval[29] = new Index();

        retval[31] = TextFunction.MID;
        retval[32] = TextFunction.LEN;
        retval[33] = new Value();
        retval[34] = BooleanFunction.TRUE;
        retval[35] = BooleanFunction.FALSE;
        retval[36] = BooleanFunction.AND;
        retval[37] = BooleanFunction.OR;
        retval[38] = BooleanFunction.NOT;
        retval[39] = NumericFunction.MOD;

        retval[46] = AggregateFunction.VAR;
        retval[48] = TextFunction.TEXT;

        retval[56] = FinanceFunction.PV;
        retval[57] = FinanceFunction.FV;
        retval[58] = FinanceFunction.NPER;
        retval[59] = FinanceFunction.PMT;

        retval[62] = new Irr();
        retval[63] = NumericFunction.RAND;
        retval[64] = new Match();
        retval[65] = DateFunc.instance;
        retval[66] = new TimeFunc();
        retval[67] = CalendarFieldFunction.DAY;
        retval[68] = CalendarFieldFunction.MONTH;
        retval[69] = CalendarFieldFunction.YEAR;
        retval[70] = new Weekday();

        retval[71] = CalendarFieldFunction.HOUR;
        retval[72] = CalendarFieldFunction.MINUTE;
        retval[73] = CalendarFieldFunction.SECOND;
        retval[74] = new Now();

        retval[76] = new Rows();
        retval[77] = new Columns();
        retval[82] = TextFunction.SEARCH;
        retval[FunctionID.OFFSET] = new Offset();
        retval[82] = TextFunction.SEARCH;

        retval[97] = NumericFunction.ATAN2;
        retval[98] = NumericFunction.ASIN;
        retval[99] = NumericFunction.ACOS;
        retval[FunctionID.CHOOSE] = new Choose();
        retval[101] = new Hlookup();
        retval[102] = new Vlookup();

        retval[105] = LogicalFunction.ISREF;

        retval[109] = NumericFunction.LOG;

        retval[111] = TextFunction.CHAR;
        retval[112] = TextFunction.LOWER;
        retval[113] = TextFunction.UPPER;

        retval[115] = TextFunction.LEFT;
        retval[116] = TextFunction.RIGHT;
        retval[117] = TextFunction.EXACT;
        retval[118] = TextFunction.TRIM;
        retval[119] = new Replace();
        retval[120] = new Substitute();
        retval[121] = TextFunction.CODE;

        retval[124] = TextFunction.FIND;

        retval[127] = LogicalFunction.ISTEXT;
        retval[128] = LogicalFunction.ISNUMBER;
        retval[129] = LogicalFunction.ISBLANK;
        retval[130] = new T();

        retval[144] = AggregateFunction.DDB;

        retval[FunctionID.INDIRECT] = null;
        retval[162] = TextFunction.CLEAN;
        retval[169] = new Counta();

        retval[183] = AggregateFunction.PRODUCT;
        retval[184] = NumericFunction.FACT;

        retval[190] = LogicalFunction.ISNONTEXT;
        retval[194] = AggregateFunction.VARP;
        retval[197] = NumericFunction.TRUNC;
        retval[198] = LogicalFunction.ISLOGICAL;

        retval[212] = NumericFunction.ROUNDUP;
        retval[213] = NumericFunction.ROUNDDOWN;
        retval[219] = new Address();
        retval[220] = new Days360();
        retval[221] = new Today();

        retval[227] = AggregateFunction.MEDIAN;
        retval[228] = new Sumproduct();
        retval[229] = NumericFunction.SINH;
        retval[230] = NumericFunction.COSH;
        retval[231] = NumericFunction.TANH;
        retval[232] = NumericFunction.ASINH;
        retval[233] = NumericFunction.ACOSH;
        retval[234] = NumericFunction.ATANH;

        retval[247] = AggregateFunction.DB;

        retval[FunctionID.EXTERNAL_FUNC] = null;

        retval[261] = new Errortype();

        retval[269] = AggregateFunction.AVEDEV;

        retval[276] = NumericFunction.COMBIN;

        retval[279] = new Even();

        retval[285] = NumericFunction.FLOOR;

        retval[288] = NumericFunction.CEILING;

        retval[298] = new Odd();

        retval[300] = NumericFunction.POISSON;

        retval[303] = new Sumxmy2();
        retval[304] = new Sumx2my2();
        retval[305] = new Sumx2py2();

        retval[318] = AggregateFunction.DEVSQ;

        retval[321] = AggregateFunction.SUMSQ;

        retval[325] = AggregateFunction.LARGE;
        retval[326] = AggregateFunction.SMALL;

        retval[330] = new Mode();

        retval[336] = TextFunction.CONCATENATE;
        retval[337] = NumericFunction.POWER;

        retval[342] = NumericFunction.RADIANS;
        retval[343] = NumericFunction.DEGREES;

        retval[344] = new Subtotal();
        retval[345] = new Sumif();
        retval[346] = new Countif();
        retval[347] = new Countblank();

        retval[359] = new Hyperlink();

        retval[361] = new Averagea();

        retval[362] = MinaMaxa.MAXA;
        retval[363] = MinaMaxa.MINA;

        for (int i = 0; i < retval.length; i++) {
            Function f = retval[i];
            if (f == null) {
                FunctionMetadata fm = FunctionMetadataRegistry.getFunctionByIndex(i);
                if (fm == null) {
                    continue;
                }
                retval[i] = new NotImplementedFunction(fm.getName());
            }
        }
        return retval;
    }

    public static Function getBasicFunction(int functionIndex) {

        switch (functionIndex) {
            case FunctionID.INDIRECT:
            case FunctionID.EXTERNAL_FUNC:
                return null;
        }

        Function result = functions[functionIndex];
        if (result == null) {
            throw new NotImplementedException("FuncIx=" + functionIndex);
        }
        return result;
    }

    private static final class FunctionID {

        public static final int IF = FunctionMetadataRegistry.FUNCTION_INDEX_IF;

        public static final int SUM = FunctionMetadataRegistry.FUNCTION_INDEX_SUM;

        public static final int OFFSET = 78;

        public static final int CHOOSE = FunctionMetadataRegistry.FUNCTION_INDEX_CHOOSE;

        public static final int INDIRECT = FunctionMetadataRegistry.FUNCTION_INDEX_INDIRECT;

        public static final int EXTERNAL_FUNC = FunctionMetadataRegistry.FUNCTION_INDEX_EXTERNAL;
    }
}
