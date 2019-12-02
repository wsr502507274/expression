/**
 * ==================================================
 * <p>
 * FileName: NotEmptyExpHandler
 *
 * @author : shihongwei
 * @create 2018/11/16
 * @since 1.0.0
 * 〈功能〉：非空表达式解析器
 * ==================================================
 */
package com.zds.boot.exp.sysexp.handler.exp;


import com.zds.boot.exp.comm.util.EmptyChecker;

/** 表达式样例  notEmpty()*/
public class NotEmptyExpHandler extends AbsExpressionHandler{
    @Override
    public boolean runExp(String param,Object obj) {

        return EmptyChecker.notEmpty(param);
    }
}
