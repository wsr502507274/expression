/**
 * ==================================================
 * <p>
 * FileName: MaxExpHandler
 *
 * @author : shihongwei
 * @create 2018/11/16
 * @since 1.0.0
 * 〈功能〉：是否数字表达式解析器
 * ==================================================
 */
package com.zds.boot.exp.sysexp.handler.exp;

import com.zds.boot.exp.comm.util.EmptyChecker;
import org.apache.commons.lang3.math.NumberUtils;

/** 表达式样例  isNumber*/
public class IsNumberExpHandler extends AbsExpressionHandler{

    @Override
    public boolean runExp(String param,Object obj) {

        if (EmptyChecker.isEmpty(param)){
            return true;
        }

        return NumberUtils.isNumber(param);
    }

}
