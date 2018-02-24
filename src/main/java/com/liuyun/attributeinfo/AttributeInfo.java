package com.liuyun.attributeinfo;

import com.liuyun.basictype.U1;
import com.liuyun.constantpool.ConstantPool;
import java.io.InputStream;

public class AttributeInfo extends BasicAttributeInfo {

    private byte[] info;

    public AttributeInfo(ConstantPool constantPool) {
        super(constantPool);
    }

    @Override
    public void read(InputStream inputStream) {
        info = new byte[(int) getAttributeLength()];
        for (int i = 0; i < getAttributeLength(); i++) {
            U1 byteU1 = U1.read(inputStream);
            info[i] = byteU1.getValue();
        }
    }

}
