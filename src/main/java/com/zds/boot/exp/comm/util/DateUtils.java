package com.zds.boot.exp.comm.util;

import com.zds.boot.common.exception.BusinessException;
import com.zds.boot.exp.comm.constant.ValidateConstans;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * ==================================================
 * <p>
 * FileName: DateUtils
 *
 * @author : shihongwei
 * @create 2019/4/10
 * @since 1.0.0
 * 〈功能〉：日期工具类
 * ==================================================
 */
public class DateUtils {

    public static final String GENERAL_DATE1="yyyy-MM-dd HH:mm:ss";
    public static final String GENERAL_DATE2="yyyy/MM/dd HH:mm:ss";
    public static final String GENERAL_DATE3="yyyy MM dd HH:mm:ss";

    private DateUtils(){}
    /* *
     *========================================
     * @方法说明 ： 按照默认日期格式转换日期【yyyy-MM-dd HH:mm:ss】
     * @author : shihongwei
     * @param date
     * @return      java.util.DATE
     * @exception   
     * @创建时间：     2019/4/17 13:18
     *========================================
    */
    public static Date parseDate(String date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return simpleDateFormat.parse(date);
        }catch (Exception e){
            throw  new BusinessException(ValidateConstans.ERR_CODE,"日期转换异常");
        }
    }

    /* *
     *========================================
     * @方法说明 ： 按照给定日期格式转换日期
     * @author : shihongwei
     * @param date
     * @param format  给定格式
     * @return      java.util.DATE
     * @exception
     * @创建时间：     2019/4/17 13:24
     *========================================
    */
    public static Date parseDate(String date,String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.parse(date);
        }catch (Exception e){
            throw  new BusinessException(ValidateConstans.ERR_CODE,"日期转换异常");
        }
    }

    /* *
     *========================================
     * @方法说明 ： 按照默认日期格式转换日期【yyyy-MM-dd HH:mm:ss】
     * @author : shihongwei
     * @param date
     * @return      java.lang.STRING
     * @exception
     * @创建时间：     2019/4/17 13:24
     *========================================
    */
    public static String formatDate(Date date ){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            return simpleDateFormat.format(date);
        }catch (Exception e){
            throw  new BusinessException(ValidateConstans.ERR_CODE,"日期转换异常");
        }
    }

    /* *
     *========================================
     * @方法说明 ： 按照给定日期格式转换日期
     * @author : shihongwei
     * @param date
     * @param format 给定格式
     * @return      java.lang.STRING
     * @exception
     * @创建时间：     2019/4/17 13:25
     *========================================
    */
    public static String formatDate(Date date,String format){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        try {
            return simpleDateFormat.format(date);
        }catch (Exception e){
            throw  new BusinessException(ValidateConstans.ERR_CODE,"日期转换异常");
        }
    }


    public static boolean isGeneralDate(String date){

        return tryCovert(GENERAL_DATE1,date) || tryCovert(GENERAL_DATE2,date) ||tryCovert(GENERAL_DATE3,date);
    }

    private static boolean tryCovert(String format,String date){
        SimpleDateFormat sp = new SimpleDateFormat(format);
        try {
            sp.parse(date);
        }catch (Exception e){
            return false;
        }
        return true;
    }

    private static Date generalParse(String format,String date){
        SimpleDateFormat sp = new SimpleDateFormat(format);
        try {
            return sp.parse(date);
        }catch (Exception e){
            return null;
        }
    }

    public static Date parseGeneralDate(String date){
        if (generalParse(GENERAL_DATE1,date) !=null){
            return generalParse(GENERAL_DATE1,date);
        }else if (generalParse(GENERAL_DATE2,date) !=null){
            return generalParse(GENERAL_DATE2,date);
        }else if (generalParse(GENERAL_DATE3,date) !=null){
            return generalParse(GENERAL_DATE3,date);
        }
        throw  new BusinessException(ValidateConstans.ERR_CODE,"日期转换异常");
    }
}
