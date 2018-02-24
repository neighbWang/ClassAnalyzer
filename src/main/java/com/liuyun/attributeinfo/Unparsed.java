package com.liuyun.attributeinfo;

import com.liuyun.basictype.U1;
import com.liuyun.basictype.U4;
import com.liuyun.constantpool.ConstantPool;
import java.io.InputStream;

public class Unparsed extends BasicAttributeInfo {

    public Unparsed(ConstantPool constantPool, short attributeNameIndex) {
        super(constantPool);
        setAttributeNameIndex(attributeNameIndex);
    }

    @Override
    public void read(InputStream inputStream) {
        U4 attributeLengthU4 = U4.read(inputStream);
        setAttributeLength(attributeLengthU4.getValue());
        for (int i = 0; i < getAttributeLength(); i++) {
            U1.read(inputStream);
        }
    }
}
