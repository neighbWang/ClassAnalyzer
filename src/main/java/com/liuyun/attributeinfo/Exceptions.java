package com.liuyun.attributeinfo;

import com.liuyun.basictype.U2;
import com.liuyun.basictype.U4;
import com.liuyun.constantpool.ConstantPool;
import com.liuyun.constantpool.ConstantUtf8Info;
import java.io.InputStream;
import java.util.Arrays;

public class Exceptions extends BasicAttributeInfo {

    private short numberOfExceptions;
    private short[] exceptionIndexTable;

    public Exceptions(ConstantPool constantPool, short attributeNameIndex) {
        super(constantPool);
        setAttributeNameIndex(attributeNameIndex);
    }

    @Override
    public void read(InputStream inputStream) {
        U4 attributeLengthU4 = U4.read(inputStream);
        U2 numberOfExceptionsU2 = U2.read(inputStream);
        setAttributeLength(attributeLengthU4.getValue());
        numberOfExceptions = numberOfExceptionsU2.getValue();
        exceptionIndexTable = new short[numberOfExceptions];
        for (int i = 0; i < numberOfExceptions; i++) {
            U2 exceptionIndexU2 = U2.read(inputStream);
            exceptionIndexTable[i] = exceptionIndexU2.getValue();
        }
    }

    @Override
    public String toString() {
        return "Exceptions{" +
                "attributeNameIndex=" + getAttributeNameIndex() +
                " [attribute name = " + ((ConstantUtf8Info) (constantPool.getCpInfo()[getAttributeNameIndex() - 1])).getValue() + "]" +
                ", attributeLength=" + getAttributeLength() +
                ", numberOfExceptions=" + numberOfExceptions +
                ", exceptionIndexTable=" + Arrays.toString(exceptionIndexTable) +
                '}';
    }

}
