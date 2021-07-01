package com.example.common.util;


import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 包内可见的类和方法
 *
 * @author zdh
 * @date 2019/5/18
 */
public class ValueUtil {

    public static String parseString(Object v) {
        if (v == null) {
            return null;
        }
        if (v instanceof String) {
            return (String) v;
        }
        return v.toString();
    }

    public static int parseInt(Number v) {
        return v == null ? 0 : v.intValue();
    }

    public static long parseLong(Number v) {
        return v == null ? 0 : v.longValue();
    }

    public static double parseDouble(Number v) {
        return v == null ? 0 : v.doubleValue();
    }

    public static boolean parseBoolean(Boolean v) {
        return v != null && v;
    }

    /**
     * 不为0即为真
     *
     * @param v
     * @return
     */
    public static boolean parseBoolean(Number v) {
        return v != null && v.doubleValue() != 0;
    }

    public static int parseInt(Object v) {
        if (v == null) {
            return 0;
        }
        try {
            return Integer.parseInt(v.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public static long parseLong(Object v) {
        if (v == null) {
            return 0;
        }
        try {
            return Long.parseLong(v.toString());
        } catch (Exception e) {
            return 0L;
        }
    }

    public static double parseDouble(Object v) {
        if (v == null) {
            return 0;
        }
        try {
            return Double.parseDouble(v.toString());
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean parseBoolean(Object v) {
        if (v == null) {
            return false;
        }
        try {
            return Boolean.parseBoolean(v.toString());
        } catch (Exception e) {
            return false;
        }
    }

    public static <T> T parse(Object obj, Class<T> clazz) {
        if (clazz.isInstance(obj)) {
            T t = (T) obj;
            return t;
        }
        return null;
    }

    public static int getValue(Integer value) {
        return value == null ? 0 : value;
    }

    public static long getValue(Long value) {
        return value == null ? 0 : value;
    }

    public static double getValue(Double value) {
        return value == null ? 0 : value;
    }

    public static boolean getValue(Boolean value) {
        return value == null ? false : value;
    }

    public static Set<String> parseSet(Set<Object> objList) {
        Set<String> sets = new TreeSet<>();
        if (!canAndParse(sets, objList, String.class)) {
            return null;
        }
        return sets;
    }

    public static <T> List<T> parseList(List<Object> objList, Class<T> clazz) {
        List<T> list = new ArrayList<>(objList.size());
        if (!canAndParse(list, objList, clazz)) {
            return null;
        }
        return list;
    }

    public static <T> Set<T> parseSet(Set<Object> objList, Class<T> clazz) {
        Set<T> sets = new TreeSet<>();
        if (!canAndParse(sets, objList, clazz)) {
            return null;
        }
        return sets;
    }

    public static <T> Map<String, T> parseMap(Map<String, Object> objMap, Class<T> clazz) {
        if (objMap == null || objMap.size() <= 0) {
            return null;
        }
        Map<String, T> map = new HashMap<>(objMap.size());
        for (Map.Entry<String, Object> entry : objMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value == null) {
                map.put(key, null);
            } else if (clazz.isInstance(value)) {
                T t = (T) value;
                map.put(key, t);
            } else {
                //元素类型已经发生变化，返回空列表
                return null;
            }

        }
        return map;
    }
    /*
    public static List<IZsetTuple> parseTupleList(Set<ZSetOperations.TypedTuple<Object>> source){
        if (source == null || source.size() <= 0) {
            return null;
        }
        List<IZsetTuple> target = new ArrayList<>(source.size());
        for (ZSetOperations.TypedTuple<Object> t: source) {
            if (t == null) {
                continue;
            }
            String member = parseString(t.getValue());
            long score = parseLong(t.getScore());
            IZsetTuple tuple = new ZsetTuple(member, score);
            target.add(tuple);
        }
        return target;
    }*/

    /**
     * 把source的元素全部转为T类型后，添加到target当中。
     * 全部转化成功，返回true
     * 如果存在非T类型的数据，导致转化失败，则返回false
     *
     * @param target
     * @param source
     * @param clazz
     * @param <T>
     * @return 转化成功，返回true，转化失败，返回false
     */
    private static <T> boolean canAndParse(Collection<T> target, Collection<Object> source, Class<T> clazz) {
        if (source == null || source.size() <= 0) {
            return false;
        }
        for (Object obj : source) {
            if (obj == null) {
                target.add(null);
            } else if (clazz.isInstance(obj)) {
                T t = (T) obj;
                target.add(t);
            } else {
                //元素类型已经发生变化，返回空列表
                return false;
            }
        }
        return true;
    }

    public static boolean isMobileNO(String mobiles) {
        if (mobiles==null) {
            return false;
        }
        String s2 = "^[1](([3|5|8][\\d])|([4][5,6,7,8,9])|([6][5,6])|([7][3,4,5,6,7,8])|([9][8,9]))[\\d]{8}$";
        Pattern p = Pattern.compile(s2);
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }
}