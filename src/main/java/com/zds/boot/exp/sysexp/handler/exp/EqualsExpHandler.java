/**
 * ==================================================
 * <p>
 * FileName: EqualsExpHandler
 *
 * @author : shihongwei
 * @create 2018/11/16
 * @since 1.0.0
 * 〈功能〉：字符串比较表达式解析器
 * ==================================================
 */
package com.zds.boot.exp.sysexp.handler.exp;

/** 表达式样例  equals(aaa)*/
public class EqualsExpHandler extends AbsExpressionHandler {

    private String equals;

    public EqualsExpHandler(String equals) {
        this.equals = equals;
    }

    @Override
    public boolean runExp(String param,Object obj) {
        if (param ==null){
            return false;
        }

        return equals.equals(param);
    }
}
