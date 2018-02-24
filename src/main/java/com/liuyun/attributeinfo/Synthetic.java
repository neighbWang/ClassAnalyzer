package com.liuyun.attributeinfo;

import com.liuyun.basictype.U2;
import com.liuyun.basictype.U4;
import com.liuyun.constantpool.ConstantPool;
import com.liuyun.constantpool.ConstantUtf8Info;
import java.io.InputStream;

public class Synthetic extends BasicAttributeInfo {

    public Synthetic(ConstantPool constantPool, short attributeNameIndex) {
        super(constantPool);
        setAttributeNameIndex(attributeNameIndex);
    }

    @Override
    public void read(InputStream inputStream) {
        U2 attributeNameIndexU2 = U2.read(inputStream);
        U4 attributeLengthU4 = U4.read(inputStream);
        setAttributeNameIndex(attributeNameIndexU2.getValue());
        setAttributeLength(attributeLengthU4.getValue());
    }

    @Override
    public String toString() {
        return "Synthetic{" +
                "attributeNameIndex=" + getAttributeNameIndex() +
                " [attribute name = " + ((ConstantUtf8Info) (constantPool.getCpInfo()[getAttributeNameIndex() - 1])).getValue() + "]" +
                ", attributeLength=" + getAttributeLength() +
                '}';
    }

}
