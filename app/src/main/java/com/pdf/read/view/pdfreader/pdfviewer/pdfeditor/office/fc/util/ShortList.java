package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.util;

public class ShortList {
    private static final int _default_size = 128;
    private short[] _array;
    private int _limit;

    public ShortList() {
        this(_default_size);
    }

    public ShortList(final ShortList list) {
        this(list._array.length);
        System.arraycopy(list._array, 0, _array, 0, _array.length);
        _limit = list._limit;
    }

    public ShortList(final int initialCapacity) {
        _array = new short[initialCapacity];
        _limit = 0;
    }

    public void add(final int index, final short value) {
        if (index > _limit) {
            throw new IndexOutOfBoundsException();
        } else if (index == _limit) {
            add(value);
        } else {

            if (_limit == _array.length) {
                growArray(_limit * 2);
            }
            System.arraycopy(_array, index, _array, index + 1,
                    _limit - index);
            _array[index] = value;
            _limit++;
        }
    }

    public boolean add(final short value) {
        if (_limit == _array.length) {
            growArray(_limit * 2);
        }
        _array[_limit++] = value;
        return true;
    }

    public boolean addAll(final ShortList c) {
        if (c._limit != 0) {
            if ((_limit + c._limit) > _array.length) {
                growArray(_limit + c._limit);
            }
            System.arraycopy(c._array, 0, _array, _limit, c._limit);
            _limit += c._limit;
        }
        return true;
    }

    public boolean addAll(final int index, final ShortList c) {
        if (index > _limit) {
            throw new IndexOutOfBoundsException();
        }
        if (c._limit != 0) {
            if ((_limit + c._limit) > _array.length) {
                growArray(_limit + c._limit);
            }

            System.arraycopy(_array, index, _array, index + c._limit,
                    _limit - index);

            System.arraycopy(c._array, 0, _array, index, c._limit);
            _limit += c._limit;
        }
        return true;
    }

    public void clear() {
        _limit = 0;
    }

    public boolean contains(final short o) {
        boolean rval = false;

        for (int j = 0; !rval && (j < _limit); j++) {
            if (_array[j] == o) {
                rval = true;
                break;
            }
        }
        return rval;
    }

    public boolean containsAll(final ShortList c) {
        boolean rval = true;

        if (this != c) {
            for (int j = 0; rval && (j < c._limit); j++) {
                if (!contains(c._array[j])) {
                    rval = false;
                }
            }
        }
        return rval;
    }

    public boolean equals(final Object o) {
        boolean rval = this == o;

        if (!rval && (o != null) && (o.getClass() == this.getClass())) {
            ShortList other = (ShortList) o;

            if (other._limit == _limit) {

                rval = true;
                for (int j = 0; rval && (j < _limit); j++) {
                    rval = _array[j] == other._array[j];
                }
            }
        }
        return rval;
    }

    public short get(final int index) {
        if (index >= _limit) {
            throw new IndexOutOfBoundsException();
        }
        return _array[index];
    }

    public int hashCode() {
        int hash = 0;

        for (int j = 0; j < _limit; j++) {
            hash = (31 * hash) + _array[j];
        }
        return hash;
    }

    public int indexOf(final short o) {
        int rval = 0;

        for (; rval < _limit; rval++) {
            if (o == _array[rval]) {
                break;
            }
        }
        if (rval == _limit) {
            rval = -1;
        }
        return rval;
    }

    public boolean isEmpty() {
        return _limit == 0;
    }

    public int lastIndexOf(final short o) {
        int rval = _limit - 1;

        for (; rval >= 0; rval--) {
            if (o == _array[rval]) {
                break;
            }
        }
        return rval;
    }

    public short remove(final int index) {
        if (index >= _limit) {
            throw new IndexOutOfBoundsException();
        }
        short rval = _array[index];

        System.arraycopy(_array, index + 1, _array, index, _limit - index);
        _limit--;
        return rval;
    }

    public boolean removeValue(final short o) {
        boolean rval = false;

        for (int j = 0; !rval && (j < _limit); j++) {
            if (o == _array[j]) {
                System.arraycopy(_array, j + 1, _array, j, _limit - j);
                _limit--;
                rval = true;
            }
        }
        return rval;
    }

    public boolean removeAll(final ShortList c) {
        boolean rval = false;

        for (int j = 0; j < c._limit; j++) {
            if (removeValue(c._array[j])) {
                rval = true;
            }
        }
        return rval;
    }

    public boolean retainAll(final ShortList c) {
        boolean rval = false;

        for (int j = 0; j < _limit; ) {
            if (!c.contains(_array[j])) {
                remove(j);
                rval = true;
            } else {
                j++;
            }
        }
        return rval;
    }

    public short set(final int index, final short element) {
        if (index >= _limit) {
            throw new IndexOutOfBoundsException();
        }
        short rval = _array[index];

        _array[index] = element;
        return rval;
    }

    public int size() {
        return _limit;
    }

    public short[] toArray() {
        short[] rval = new short[_limit];

        System.arraycopy(_array, 0, rval, 0, _limit);
        return rval;
    }

    public short[] toArray(final short[] a) {
        short[] rval;

        if (a.length == _limit) {
            System.arraycopy(_array, 0, a, 0, _limit);
            rval = a;
        } else {
            rval = toArray();
        }
        return rval;
    }

    private void growArray(final int new_size) {
        int size = (new_size == _array.length) ? new_size + 1
                : new_size;
        short[] new_array = new short[size];

        System.arraycopy(_array, 0, new_array, 0, _limit);
        _array = new_array;
    }
}   

