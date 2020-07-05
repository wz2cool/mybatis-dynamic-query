package com.github.wz2cool.dynamic.mybatis;

import java.math.BigDecimal;
import java.util.Date;

/**
 * @author Frank
 **/
public class TypeHelper {
    private TypeHelper() {
    }

    public static BigDecimal getBigDecimal(Object value) {
        BigDecimal ret = null;
        if (value != null) {
            if (value instanceof BigDecimal) {
                ret = (BigDecimal)value;
            } else {
                ret = new BigDecimal(value.toString());
            }
        }

        return ret;
    }

    public static Byte getByte(Object value) {
        Byte ret = null;
        if (value != null) {
            if (value instanceof Byte) {
                ret = (Byte)value;
            } else {
                ret = new Byte(value.toString());
            }
        }

        return ret;
    }

    public static Date getDate(Object value) {
        Date ret = null;
        if (value != null) {
            if (!(value instanceof Date)) {
                throw new ClassCastException("Not possible to coerce [" + value + "] from class " + value.getClass() + " into a Date.");
            }

            ret = (Date)value;
        }

        return ret;
    }

    public static Double getDouble(Object value) {
        Double ret = null;
        if (value != null) {
            if (value instanceof Double) {
                ret = (Double)value;
            } else {
                ret = new Double(value.toString());
            }
        }

        return ret;
    }

    public static Float getFloat(Object value) {
        Float ret = null;
        if (value != null) {
            if (value instanceof Float) {
                ret = (Float)value;
            } else {
                ret = new Float(value.toString());
            }
        }

        return ret;
    }

    public static Integer getInteger(Object value) {
        Integer ret = null;
        if (value != null) {
            if (value instanceof Integer) {
                ret = (Integer)value;
            } else {
                ret = new Integer(value.toString());
            }
        }

        return ret;
    }

    public static Long getLong(Object value) {
        Long ret = null;
        if (value != null) {
            if (value instanceof Long) {
                ret = (Long)value;
            } else {
                ret = new Long(value.toString());
            }
        }

        return ret;
    }

    public static Short getShort(Object value) {
        Short ret = null;
        if (value != null) {
            if (value instanceof Short) {
                ret = (Short)value;
            } else {
                ret = new Short(value.toString());
            }
        }

        return ret;
    }

    public static String getString(Object value) {
        String ret = null;
        if (value != null) {
            if (value instanceof String) {
                ret = (String)value;
            } else {
                ret = value.toString();
            }
        }

        return ret;
    }
}
