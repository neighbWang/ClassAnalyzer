package com.liuyun.constantpool;

import com.liuyun.basictype.U2;
import java.io.InputStream;

public class ConstantFieldRefInfo extends ConstantPoolInfo {

    private short classIndex;
    private short nameAndTypeIndex;

    public ConstantFieldRefInfo(byte tag) {
        setTag(tag);
    }

    @Override
    public void read(InputStream inputStream) {
        U2 classIndexU2 = U2.read(inputStream);
        U2 nameAndTypeIndexU2 = U2.read(inputStream);
        this.classIndex = classIndexU2.getValue();
        this.nameAndTypeIndex = nameAndTypeIndexU2.getValue();
    }

    @Override
    public String toString() {
        return "ConstantFieldRefInfo{" +
                "classIndex=" + classIndex +
                ", nameAndTypeIndex=" + nameAndTypeIndex +
                '}';
    }
}
