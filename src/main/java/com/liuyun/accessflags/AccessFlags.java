package com.liuyun.accessflags;

public abstract class AccessFlags {

    public abstract String getFormattedAccessFlags(short accessFlags);

    public static String getFormattedAccessFlags(AccessFlags accessFlags, short accessFlagsIndex) {
        return accessFlags.getFormattedAccessFlags(accessFlagsIndex);
    }

}
