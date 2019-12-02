
package com.zds.boot.exp.comm.util;

import java.util.Collection;
import java.util.Map;

/**
 * ==================================================
 * <p>
 * FileName: EmptyCheck
 *
 * @author : shihongwei
 * @create 2018/8/30
 * @since 1.0.0
 * 〈功能〉：空校验辅助类
 * ==================================================
 */
public class EmptyChecker {

    private EmptyChecker() {
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null || "null".equals(obj.toString()) || "".equals(obj.toString())) {
            return true;
        }

        if (obj instanceof String) {
            return ((String) obj).trim().length() == 0;
        }

        if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        }

        if (obj instanceof Map) {
            return ((Map) obj).isEmpty();
        }

        return false;
    }

    public static boolean notEmpty(Object obj) {
        return !isEmpty(obj);
    }

    public static boolean isEmpty(Object[] array) {
        if (array == null || array.length == 0) {
            return true;
        }

        return false;
    }

    /**
     *========================================

     * @方法说明 ： 如果任意一个参数为空 返回true
     * @author : shihongwei

     * @param obj
     * @return      boolean
     * @exception
     * @创建时间：     2018/9/10 11:14

     *========================================
     */
    public static boolean isAnyOneEmpty(Object ... obj) {
        for (int i = 0; i <obj.length ; i++) {
            boolean temp = isEmpty(obj[i]);
            if (temp){
                return true;
            }
        }

        return false;
    }

    /**
     *========================================

     * @方法说明 ： 如果所有参数为空 返回true
     * @author : shihongwei

     * @param obj
     * @return      boolean
     * @exception
     * @创建时间：     2018/9/10 11:14

     *========================================
     */
    public static boolean isAllEmpty(Object ... obj) {
        for (int i = 0; i <obj.length ; i++) {
            boolean temp = notEmpty(obj[i]);
            if (temp){
                return true;
            }
        }

        return false;
    }
}
