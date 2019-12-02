/**
 * ==================================================
 * <p>
 * FileName: LengthExpHandler
 *
 * @author : shihongwei
 * @create 2018/11/16
 * @since 1.0.0
 * 〈功能〉：长度表达式解析器
 * ==================================================
 */
package com.zds.boot.exp.sysexp.handler.exp;

import com.zds.boot.exp.comm.util.EmptyChecker;

/** 表达式样例  length(18)*/
public class LengthExpHandler extends AbsExpressionHandler{

    private int length;

    public LengthExpHandler(int length) {
        this.length=length;
    }

    @Override
    public boolean runExp(String param,Object obj) {
        if (EmptyChecker.isEmpty(param)){
            return true;
        }
        return (length - param.length())>=0;
    }


}
