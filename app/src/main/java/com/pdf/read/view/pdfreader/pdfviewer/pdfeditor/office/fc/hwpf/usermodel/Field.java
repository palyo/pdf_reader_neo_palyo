package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hwpf.usermodel;

public interface Field {

    byte REF = 0x03;
    byte FTNREF = 0x05;
    byte SET = 0x06;
    byte IF = 0x07;
    byte INDEX = 0x08;
    byte STYLEREF = 0x0A;
    byte SEQ = 0x0C;
    byte TOC = 0x0D;
    byte INFO = 0x0E;
    byte TITLE = 0x0F;
    byte SUBJECT = 0x10;
    byte AUTHOR = 0x11;
    byte KEYWORDS = 0x12;
    byte COMMENTS = 0x13;
    byte LASTSAVEDBY = 0x14;
    byte CREATEDATE = 0x15;
    byte SAVEDATE = 0x16;
    byte PRINTDATE = 0x17;
    byte REVNUM = 0x18;
    byte EDITTIME = 0x19;
    byte NUMPAGES = 0x1A;
    byte NUMWORDS = 0x1B;
    byte NUMCHARS = 0x1C;
    byte FILENAME = 0x1D;
    byte TEMPLATE = 0x1E;
    byte DATE = 0x1F;
    byte TIME = 0x20;
    byte PAGE = 0x21;
    byte Equals = 0x22;
    byte QUOTE = 0x23;
    byte INCLUDE = 0x24;
    byte PAGEREF = 0x25;
    byte ASK = 0x26;
    byte FILLIN = 0x27;
    byte DATA = 0x28;
    byte NEXT = 0x29;
    byte NEXTIF = 0x2A;
    byte SKIPIF = 0x2B;
    byte MERGEREC = 0x2C;
    byte DDE = 0x2D;
    byte DDEAUTO = 0x2E;
    byte GLOSSARY = 0x2F;
    byte PRINT = 0x30;
    byte EQ = 0x31;
    byte GOTOBUTTON = 0x32;
    byte MACROBUTTON = 0x33;
    byte AUTONUMOUT = 0x34;
    byte AUTONUMLGL = 0x35;
    byte AUTONUM = 0x36;
    byte IMPORT = 0x37;
    byte LINK = 0x38;
    byte SYMBOL = 0x39;
    byte EMBED = 0x3A;
    byte MERGEFIELD = 0x3B;
    byte USERNAME = 0x3C;
    byte USERINITIALS = 0x3D;
    byte USERADDRESS = 0x3E;
    byte BARCODE = 0x3F;
    byte DOCVARIABLE = 0x40;
    byte SECTION = 0x41;
    byte SECTIONPAGES = 0x42;
    byte INCLUDEPICTURE = 0x43;
    byte INCLUDETEXT = 0x44;
    byte FILESIZE = 0x45;
    byte FORMTEXT = 0x46;
    byte FORMCHECKBOX = 0x47;
    byte NOTEREF = 0x48;
    byte TOA = 0x49;
    byte MERGESEQ = 0x4B;
    byte AUTOTEXT = 0x4F;
    byte COMPARE = 0x50;
    byte ADDIN = 0x51;
    byte FORMDROPDOWN = 0x53;
    byte ADVANCE = 0x54;
    byte DOCPROPERTY = 0x55;
    byte CONTROL = 0x57;
    byte HYPERLINK = 0x58;
    byte AUTOTEXTLIST = 0x59;
    byte LISTNUM = 0x5A;
    byte HTMLCONTROL = 0x5B;
    byte BIDIOUTLINE = 0x5C;
    byte ADDRESSBLOCK = 0x5D;
    byte GREETINGLINE = 0x5E;
    byte SHAPE = 0x5F;

    Range firstSubrange(Range parent);

    int getFieldEndOffset();

    int getFieldStartOffset();

    CharacterRun getMarkEndCharacterRun(Range parent);

    int getMarkEndOffset();

    CharacterRun getMarkSeparatorCharacterRun(Range parent);

    int getMarkSeparatorOffset();

    CharacterRun getMarkStartCharacterRun(Range parent);

    int getMarkStartOffset();

    int getType();

    boolean hasSeparator();

    boolean isHasSep();

    boolean isLocked();

    boolean isNested();

    boolean isPrivateResult();

    boolean isResultDirty();

    boolean isResultEdited();

    boolean isZombieEmbed();

    Range secondSubrange(Range parent);
}
