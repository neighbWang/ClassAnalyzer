package com.liuyun;

import com.liuyun.accessflags.AccessFlags;
import com.liuyun.accessflags.ClassAccessFlags;
import com.liuyun.attributeinfo.BasicAttributeInfo;
import com.liuyun.basictype.U1;
import com.liuyun.basictype.U2;
import com.liuyun.basictype.U4;
import com.liuyun.constantpool.*;
import java.io.InputStream;
import java.util.ArrayList;

public class ClassReader {

    public static void analyze(InputStream inputStream) {

        ClassFile classFile = read(inputStream);
        ConstantPool constantPool = new ConstantPool(classFile.constantPoolCount.getValue());
        constantPool.setCpInfo(classFile.cpInfo);

        System.out.println("magic = " + classFile.magic.getHexValue());
        System.out.println("minorVersion = " + classFile.minorVersion.getValue());
        System.out.println("majorVersion = " + classFile.majorVersion.getValue());
        System.out.println("constantPoolCount = " + classFile.constantPoolCount.getValue());
        for (int i = 0; classFile.cpInfo != null && i < classFile.cpInfo.length; i++) {
            System.out.println("cpInfo[" + (i + 1) + "] = " + classFile.cpInfo[i]);
        }
        System.out.println("accessFlags = " + classFile.accessFlags.getHexValue() +
                ", " + AccessFlags.getFormattedAccessFlags(new ClassAccessFlags(), classFile.accessFlags.getValue()));
        System.out.println("thisClass = " + classFile.thisClass.getValue() +
                ", this class name = " + getConstantClassInfoValue(constantPool, classFile.thisClass.getValue()));
        System.out.println("superClass = " + classFile.superClass.getValue() +
                ", super class name = " + getConstantClassInfoValue(constantPool, classFile.superClass.getValue()));
        System.out.println("interfacesCount = " + classFile.interfacesCount.getValue());
        for (int i = 0; i < classFile.interfacesCount.getValue(); i++) {
            System.out.println("interfaces[" + i + "] = " + classFile.interfaces[i].getValue() +
                    ", interface name = " + getConstantClassInfoValue(constantPool, classFile.interfaces[i].getValue()));
        }
        System.out.println("fieldsCount = " + classFile.fieldsCount.getValue());
        for (int i = 0; i < classFile.fieldsCount.getValue(); i++) {
            System.out.println("fields[" + i + "] = " + classFile.fields[i]);
        }
        System.out.println("methodsCount = " + classFile.methodsCount.getValue());
        for (int i = 0; i < classFile.methodsCount.getValue(); i++) {
            System.out.println("methods[" + i + "] = " + classFile.methods[i]);
        }
        System.out.println("attributesCount = " + classFile.attributesCount.getValue());
        for (int i = 0; i < classFile.attributesCount.getValue(); i++) {
            System.out.println("attributes[" + i + "] = " + classFile.attributes[i]);
        }
    }

    //读取字节流封装成ClassFile实例
    public static ClassFile read(InputStream inputStream) {
        ClassFile classFile = new ClassFile();
        // 1.读取魔数
        classFile.magic = U4.read(inputStream);
        // 2.读取次版本号
        classFile.minorVersion = U2.read(inputStream);
        // 3.读取主版本号
        classFile.majorVersion = U2.read(inputStream);
        // 4.读取常量池计数值
        classFile.constantPoolCount = U2.read(inputStream);
        // 5.读取常量池
        ConstantPool constantPool = readConstantPool(inputStream, (short) (classFile.constantPoolCount.getValue() - 1));
        classFile.cpInfo = constantPool == null ? null : constantPool.getCpInfo();
        // 6.读取访问标志
        classFile.accessFlags = U2.read(inputStream);
        // 7.读取类索引
        classFile.thisClass = U2.read(inputStream);
        // 8.读取父类索引
        classFile.superClass = U2.read(inputStream);
        // 9.读取接口计数值
        classFile.interfacesCount = U2.read(inputStream);
        //10.读取接口集合
        classFile.interfaces = new U2[classFile.interfacesCount.getValue()];
        readInterfaces(inputStream, classFile, classFile.interfacesCount.getValue());
        //11.读取字段计数值
        classFile.fieldsCount = U2.read(inputStream);
        //12.读取字段集合
        readFieldsInfo(constantPool, inputStream, classFile, classFile.fieldsCount.getValue());
        //13.读取方法计数值
        classFile.methodsCount = U2.read(inputStream);
        //14.读取方法集合
        readMethodsInfo(constantPool, inputStream, classFile, classFile.methodsCount.getValue());
        //15.读取属性计数值
        classFile.attributesCount = U2.read(inputStream);
        //16.读取属性集合
        readClassAttributesInfo(constantPool, inputStream, classFile, classFile.attributesCount.getValue());

        return classFile;
    }

    public static ConstantPool readConstantPool(InputStream inputStream, short constantPoolCount) {
        ConstantPool constantPool = new ConstantPool(constantPoolCount);
        ArrayList<ConstantPoolInfo> infoList = new ArrayList<ConstantPoolInfo>();
        for (short i = 0; i < constantPoolCount; i++) {
            U1 tag = U1.read(inputStream);
            ConstantPoolInfo info = newConstantPoolInfo(tag, inputStream);
            infoList.add(info);
			if (tag.getValue() == ConstantPoolInfo.CONSTANT_LONG_INFO ||
                    tag.getValue() == ConstantPoolInfo.CONSTANT_DOUBLE_INFO) {
                i++;
                infoList.add(null);
            }
        }
        constantPool.setCpInfo(infoList.toArray(new ConstantPoolInfo[0]));
        return constantPool;
    }

    private static ConstantPoolInfo newConstantPoolInfo(U1 tag, InputStream inputStream) {
        int tagValue = tag.getValue();
        ConstantPoolInfo constantPoolInfo = null;
        switch (tagValue) {
            case ConstantPoolInfo.CONSTANT_UTF8_INFO:
                constantPoolInfo = new ConstantUtf8Info(ConstantPoolInfo.CONSTANT_UTF8_INFO);
                constantPoolInfo.read(inputStream);
                break;
            case ConstantPoolInfo.CONSTANT_INTEGER_INFO:
                constantPoolInfo = new ConstantIntegerInfo(ConstantPoolInfo.CONSTANT_INTEGER_INFO);
                constantPoolInfo.read(inputStream);
                break;
            case ConstantPoolInfo.CONSTANT_FLOAT_INFO:
                constantPoolInfo = new ConstantFloatInfo(ConstantPoolInfo.CONSTANT_FLOAT_INFO);
                constantPoolInfo.read(inputStream);
                break;
            case ConstantPoolInfo.CONSTANT_LONG_INFO:
                constantPoolInfo = new ConstantLongInfo(ConstantPoolInfo.CONSTANT_LONG_INFO);
                constantPoolInfo.read(inputStream);
                break;
            case ConstantPoolInfo.CONSTANT_DOUBLE_INFO:
                constantPoolInfo = new ConstantDoubleInfo(ConstantPoolInfo.CONSTANT_DOUBLE_INFO);
                constantPoolInfo.read(inputStream);
                break;
            case ConstantPoolInfo.CONSTANT_CLASS_INFO:
                constantPoolInfo = new ConstantClassInfo(ConstantPoolInfo.CONSTANT_CLASS_INFO);
                constantPoolInfo.read(inputStream);
                break;
            case ConstantPoolInfo.CONSTANT_STRING_INFO:
                constantPoolInfo = new ConstantClassInfo(ConstantPoolInfo.CONSTANT_STRING_INFO);
                constantPoolInfo.read(inputStream);
                break;
            case ConstantPoolInfo.CONSTANT_FIELDREF_INFO:
                constantPoolInfo = new ConstantFieldRefInfo(ConstantPoolInfo.CONSTANT_FIELDREF_INFO);
                constantPoolInfo.read(inputStream);
                break;
            case ConstantPoolInfo.CONSTANT_METHODREF_INFO:
                constantPoolInfo = new ConstantMethodRefInfo(ConstantPoolInfo.CONSTANT_METHODREF_INFO);
                constantPoolInfo.read(inputStream);
                break;
            case ConstantPoolInfo.CONSTANT_INTERFACEMETHODREF_INFO:
                constantPoolInfo = new ConstantInterfaceMethodRefInfo(ConstantPoolInfo.CONSTANT_INTERFACEMETHODREF_INFO);
                constantPoolInfo.read(inputStream);
                break;
            case ConstantPoolInfo.CONSTANT_NAMEANDTYPE_INFO:
                constantPoolInfo = new ConstantNameAndTypeInfo(ConstantPoolInfo.CONSTANT_NAMEANDTYPE_INFO);
                constantPoolInfo.read(inputStream);
                break;
            case ConstantPoolInfo.CONSTANT_METHODHANDLE_INFO:
                constantPoolInfo = new ConstantMethodHandleInfo(ConstantPoolInfo.CONSTANT_METHODHANDLE_INFO);
                constantPoolInfo.read(inputStream);
                break;
            case ConstantPoolInfo.CONSTANT_METHODTYPE_INFO:
                constantPoolInfo = new ConstantMethodTypeInfo(ConstantPoolInfo.CONSTANT_METHODTYPE_INFO);
                constantPoolInfo.read(inputStream);
                break;
            case ConstantPoolInfo.CONSTANT_INVOKEDYNAMIC_INFO:
                constantPoolInfo = new ConstantInvokeDynamicInfo(ConstantPoolInfo.CONSTANT_INVOKEDYNAMIC_INFO);
                constantPoolInfo.read(inputStream);
                break;
        }
        return constantPoolInfo;
    }

    public static void readInterfaces(InputStream inputStream, ClassFile classFile, short interfacesCount) {
        for (int i = 0; i < interfacesCount; i++) {
            classFile.interfaces[i] = U2.read(inputStream);
        }
    }

    public static void readFieldsInfo(ConstantPool constantPool, InputStream inputStream, ClassFile classFile, short fieldsCount) {
        ArrayList<FieldInfo> fieldInfoList = new ArrayList<FieldInfo>();
        for (int i = 0; i < fieldsCount; i++) {
            FieldInfo fieldInfo = new FieldInfo(constantPool);
            fieldInfo.read(inputStream);
            fieldInfoList.add(fieldInfo);
        }
        classFile.fields = fieldInfoList.toArray(new FieldInfo[0]);
    }

    public static void readMethodsInfo(ConstantPool constantPool, InputStream inputStream,
                                       ClassFile classFile, short methodsCount) {
        classFile.methods = new MethodInfo[methodsCount];
        for (int i = 0; i < methodsCount; i++) {
            MethodInfo methodInfo = new MethodInfo(constantPool);
            methodInfo.read(inputStream);
            classFile.methods[i] = methodInfo;
        }
    }

    public static void readClassAttributesInfo(ConstantPool constantPool, InputStream inputStream,
                                               ClassFile classFile, short classAttributesCount) {
        classFile.attributes = new BasicAttributeInfo[classAttributesCount];
        for (int i = 0; i < classAttributesCount; i++) {
            short attributeNameIndex = U2.read(inputStream).getValue();
            BasicAttributeInfo attributeInfo = BasicAttributeInfo.newAttributeInfo(constantPool, attributeNameIndex);
            attributeInfo.read(inputStream);
            classFile.attributes[i] = attributeInfo;
        }
    }

    private static String getConstantClassInfoValue(ConstantPool constantPool, short constantClassInfoIndex) {
        if (constantClassInfoIndex == 0) { return "No super class"; }
        ConstantClassInfo constantClassInfo = (ConstantClassInfo) constantPool.getCpInfo()[constantClassInfoIndex - 1];
        short index = constantClassInfo.getIndex();
        ConstantUtf8Info constantUtf8Info = ((ConstantUtf8Info) (constantPool.getCpInfo()[index - 1]));
        return constantUtf8Info.getValue();
    }

}
