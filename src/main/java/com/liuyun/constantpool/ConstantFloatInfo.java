package com.liuyun.constantpool;

import com.liuyun.basictype.U2;
import java.io.InputStream;

public class ConstantFloatInfo extends ConstantPoolInfo {

    private float bytesValue;

    public ConstantFloatInfo(byte tag) {
        setTag(tag);
    }

    @Override
    public void read(InputStream inputStream) {
        U2 bytesValueU2 = U2.read(inputStream);
        this.bytesValue = bytesValueU2.getValue();
    }

    @Override
    public String toString() {
        return "ConstantFloatInfo{" +
                "bytesValue=" + bytesValue +
                '}';
    }
}
