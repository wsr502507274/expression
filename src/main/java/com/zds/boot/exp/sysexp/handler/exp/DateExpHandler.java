/**
 * ==================================================
 * <p>
 * FileName: NotEmptyExpHandler
 *
 * @author : shihongwei
 * @create 2018/11/16
 * @since 1.0.0
 * 〈功能〉：时间表达式解析器
 * ==================================================
 */
package com.zds.boot.exp.sysexp.handler.exp;

import com.zds.boot.exp.comm.util.EmptyChecker;

import java.text.SimpleDateFormat;


/** 表达式样例  date(yyyy-MM-dd)*/
public class DateExpHandler extends AbsExpressionHandler{

    private String dateFormat;

    public DateExpHandler(String dateFormat) {
        this.dateFormat=dateFormat;
    }
    @Override
    public boolean runExp(String param,Object obj) {
        if (EmptyChecker.isEmpty(param)){
            return true;
        }
        try {
            new SimpleDateFormat(dateFormat).parse(param);
            return true;
        } catch (Exception e){

            return false;
        }
    }
}
