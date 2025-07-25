package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.property;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.hpsf.ClassID;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.common.POIFSConstants;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.ByteField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.IntegerField;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.LittleEndianConsts;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util.ShortField;

public abstract class Property implements Child {
    static final protected int _NO_INDEX = -1;

    static final protected byte _NODE_BLACK = 1;
    static final protected byte _NODE_RED = 0;
    static final private byte _default_fill = (byte) 0x00;
    static final private int _name_size_offset = 0x40;
    static final private int _max_name_length = (_name_size_offset / LittleEndianConsts.SHORT_SIZE) - 1;

    static final private int _node_color_offset = 0x43;
    static final private int _previous_property_offset = 0x44;
    static final private int _next_property_offset = 0x48;
    static final private int _child_property_offset = 0x4C;
    static final private int _storage_clsid_offset = 0x50;
    static final private int _user_flags_offset = 0x60;
    static final private int _seconds_1_offset = 0x64;
    static final private int _days_1_offset = 0x68;
    static final private int _seconds_2_offset = 0x6C;
    static final private int _days_2_offset = 0x70;
    static final private int _start_block_offset = 0x74;
    static final private int _size_offset = 0x78;

    static final private int _big_block_minimum_bytes = POIFSConstants.BIG_BLOCK_MINIMUM_DOCUMENT_SIZE;
    private final ShortField _name_size;
    private final ByteField _property_type;
    private final ByteField _node_color;
    private final IntegerField _previous_property;
    private final IntegerField _next_property;
    private final IntegerField _child_property;
    private final IntegerField _user_flags;
    private final IntegerField _seconds_1;
    private final IntegerField _days_1;
    private final IntegerField _seconds_2;
    private final IntegerField _days_2;
    private final IntegerField _start_block;
    private final IntegerField _size;
    private final byte[] _raw_data;
    private String _name;
    private ClassID _storage_clsid;
    private int _index;
    private Child _next_child;
    private Child _previous_child;

    protected Property() {
        _raw_data = new byte[POIFSConstants.PROPERTY_SIZE];
        Arrays.fill(_raw_data, _default_fill);
        _name_size = new ShortField(_name_size_offset);
        _property_type = new ByteField(PropertyConstants.PROPERTY_TYPE_OFFSET);
        _node_color = new ByteField(_node_color_offset);
        _previous_property = new IntegerField(_previous_property_offset, _NO_INDEX, _raw_data);
        _next_property = new IntegerField(_next_property_offset, _NO_INDEX, _raw_data);
        _child_property = new IntegerField(_child_property_offset, _NO_INDEX, _raw_data);
        _storage_clsid = new ClassID(_raw_data, _storage_clsid_offset);
        _user_flags = new IntegerField(_user_flags_offset, 0, _raw_data);
        _seconds_1 = new IntegerField(_seconds_1_offset, 0, _raw_data);
        _days_1 = new IntegerField(_days_1_offset, 0, _raw_data);
        _seconds_2 = new IntegerField(_seconds_2_offset, 0, _raw_data);
        _days_2 = new IntegerField(_days_2_offset, 0, _raw_data);
        _start_block = new IntegerField(_start_block_offset);
        _size = new IntegerField(_size_offset, 0, _raw_data);
        _index = _NO_INDEX;
        setName("");
        setNextChild(null);
        setPreviousChild(null);
    }

    protected Property(int index, byte[] array, int offset) {
        _raw_data = new byte[POIFSConstants.PROPERTY_SIZE];
        System.arraycopy(array, offset, _raw_data, 0, POIFSConstants.PROPERTY_SIZE);
        _name_size = new ShortField(_name_size_offset, _raw_data);
        _property_type = new ByteField(PropertyConstants.PROPERTY_TYPE_OFFSET, _raw_data);
        _node_color = new ByteField(_node_color_offset, _raw_data);
        _previous_property = new IntegerField(_previous_property_offset, _raw_data);
        _next_property = new IntegerField(_next_property_offset, _raw_data);
        _child_property = new IntegerField(_child_property_offset, _raw_data);
        _storage_clsid = new ClassID(_raw_data, _storage_clsid_offset);
        _user_flags = new IntegerField(_user_flags_offset, 0, _raw_data);
        _seconds_1 = new IntegerField(_seconds_1_offset, _raw_data);
        _days_1 = new IntegerField(_days_1_offset, _raw_data);
        _seconds_2 = new IntegerField(_seconds_2_offset, _raw_data);
        _days_2 = new IntegerField(_days_2_offset, _raw_data);
        _start_block = new IntegerField(_start_block_offset, _raw_data);
        _size = new IntegerField(_size_offset, _raw_data);
        _index = index;
        int name_length = (_name_size.get() / LittleEndianConsts.SHORT_SIZE) - 1;

        if (name_length < 1) {
            _name = "";
        } else {
            char[] char_array = new char[name_length];
            int name_offset = 0;

            for (int j = 0; j < name_length; j++) {
                char_array[j] = (char) new ShortField(name_offset, _raw_data).get();
                name_offset += LittleEndianConsts.SHORT_SIZE;
            }
            _name = new String(char_array, 0, name_length);
        }
        _next_child = null;
        _previous_child = null;
    }

    public static boolean isSmall(int length) {
        return length < _big_block_minimum_bytes;
    }

    static boolean isValidIndex(int index) {
        return index != _NO_INDEX;
    }

    public void writeData(OutputStream stream) throws IOException {
        stream.write(_raw_data);
    }

    public int getStartBlock() {
        return _start_block.get();
    }

    public void setStartBlock(int startBlock) {
        _start_block.set(startBlock, _raw_data);
    }

    public int getSize() {
        return _size.get();
    }

    protected void setSize(int size) {
        _size.set(size, _raw_data);
    }

    public boolean shouldUseSmallBlocks() {
        return Property.isSmall(_size.get());
    }

    public String getName() {
        return _name;
    }

    protected void setName(String name) {
        char[] char_array = name.toCharArray();
        int limit = Math.min(char_array.length, _max_name_length);

        _name = new String(char_array, 0, limit);
        short offset = 0;
        int j = 0;

        for (; j < limit; j++) {
            new ShortField(offset, (short) char_array[j], _raw_data);
            offset += LittleEndianConsts.SHORT_SIZE;
        }
        for (; j < _max_name_length + 1; j++) {
            new ShortField(offset, (short) 0, _raw_data);
            offset += LittleEndianConsts.SHORT_SIZE;
        }

        _name_size.set((short) ((limit + 1) * LittleEndianConsts.SHORT_SIZE), _raw_data);
    }

    abstract public boolean isDirectory();

    public ClassID getStorageClsid() {
        return _storage_clsid;
    }

    public void setStorageClsid(ClassID clsidStorage) {
        _storage_clsid = clsidStorage;
        if (clsidStorage == null) {
            Arrays.fill(_raw_data, _storage_clsid_offset, _storage_clsid_offset + ClassID.LENGTH,
                    (byte) 0);
        } else {
            clsidStorage.write(_raw_data, _storage_clsid_offset);
        }
    }

    protected void setPropertyType(byte propertyType) {
        _property_type.set(propertyType, _raw_data);
    }

    protected void setNodeColor(byte nodeColor) {
        _node_color.set(nodeColor, _raw_data);
    }

    protected void setChildProperty(int child) {
        _child_property.set(child, _raw_data);
    }

    protected int getChildIndex() {
        return _child_property.get();
    }

    protected int getIndex() {
        return _index;
    }

    protected void setIndex(int index) {
        _index = index;
    }

    abstract protected void preWrite();

    int getNextChildIndex() {
        return _next_property.get();
    }

    int getPreviousChildIndex() {
        return _previous_property.get();
    }

    public Child getNextChild() {
        return _next_child;
    }

    public void setNextChild(Child child) {
        _next_child = child;
        _next_property.set((child == null) ? _NO_INDEX : ((Property) child).getIndex(), _raw_data);
    }

    public Child getPreviousChild() {
        return _previous_child;
    }

    public void setPreviousChild(Child child) {
        _previous_child = child;
        _previous_property.set((child == null) ? _NO_INDEX : ((Property) child).getIndex(),
                _raw_data);
    }

    public Object[] getViewableArray() {
        Object[] results = new Object[5];

        results[0] = "Name          = \"" + getName() + "\"";
        results[1] = "Property Type = " + _property_type.get();
        results[2] = "Node Color    = " + _node_color.get();
        long time = _days_1.get();

        time <<= 32;
        time += _seconds_1.get() & 0x0000FFFFL;
        results[3] = "Time 1        = " + time;
        time = _days_2.get();
        time <<= 32;
        time += _seconds_2.get() & 0x0000FFFFL;
        results[4] = "Time 2        = " + time;
        return results;
    }

    public Iterator getViewableIterator() {
        return Collections.EMPTY_LIST.iterator();
    }

    public boolean preferArray() {
        return true;
    }

    public String getShortDescription() {

        return "Property: \"" + getName() + "\"";
    }
}
