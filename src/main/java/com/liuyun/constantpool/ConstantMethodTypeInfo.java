package com.liuyun.constantpool;

import com.liuyun.basictype.U2;
import java.io.InputStream;

public class ConstantMethodTypeInfo extends ConstantPoolInfo {

    private short descriptorIndex;

    public ConstantMethodTypeInfo(byte tag) {
        setTag(tag);
    }

    @Override
    public void read(InputStream inputStream) {
        U2 descriptorIndexU2 = U2.read(inputStream);
        this.descriptorIndex = descriptorIndexU2.getValue();
    }

    @Override
    public String toString() {
        return "ConstantMethodTypeInfo{" +
                "descriptorIndex=" + descriptorIndex +
                '}';
    }
}
