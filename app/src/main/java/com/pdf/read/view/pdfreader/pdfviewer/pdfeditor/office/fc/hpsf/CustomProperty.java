package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hpsf;

public class CustomProperty extends MutableProperty {

    private String name;

    public CustomProperty() {
        this.name = null;
    }

    public CustomProperty(final Property property) {
        this(property, null);
    }

    public CustomProperty(final Property property, final String name) {
        super(property);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public boolean equalsContents(final Object o) {
        final CustomProperty c = (CustomProperty) o;
        final String name1 = c.getName();
        final String name2 = this.getName();
        boolean equalNames = true;
        if (name1 == null)
            equalNames = name2 == null;
        else
            equalNames = name1.equals(name2);
        return equalNames && c.getID() == this.getID()
                && c.getType() == this.getType()
                && c.getValue().equals(this.getValue());
    }

    public int hashCode() {
        return (int) this.getID();
    }

}
