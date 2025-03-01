package com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.property;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.common.POIFSConstants;
import com.pdf.read.view.pdfreader.pdfviewer.pdfeditor.office.fc.poifs.storage.ListManagedBlock;

class PropertyFactory {

    private PropertyFactory() {
    }

    static List<Property> convertToProperties(ListManagedBlock[] blocks)
            throws IOException {
        List<Property> properties = new ArrayList<Property>();

        for (int j = 0; j < blocks.length; j++) {
            byte[] data = blocks[j].getData();
            convertToProperties(data, properties);
        }
        return properties;
    }

    static void convertToProperties(byte[] data, List<Property> properties)
            throws IOException {
        int property_count = data.length / POIFSConstants.PROPERTY_SIZE;
        int offset = 0;

        for (int k = 0; k < property_count; k++) {
            switch (data[offset + PropertyConstants.PROPERTY_TYPE_OFFSET]) {
                case PropertyConstants.DIRECTORY_TYPE:
                    properties.add(
                            new DirectoryProperty(properties.size(), data, offset)
                    );
                    break;

                case PropertyConstants.DOCUMENT_TYPE:
                    properties.add(
                            new DocumentProperty(properties.size(), data, offset)
                    );
                    break;

                case PropertyConstants.ROOT_TYPE:
                    properties.add(
                            new RootProperty(properties.size(), data, offset)
                    );
                    break;

                default:
                    properties.add(null);
                    break;
            }

            offset += POIFSConstants.PROPERTY_SIZE;
        }
    }

}   

