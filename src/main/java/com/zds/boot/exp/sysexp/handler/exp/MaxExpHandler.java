/**
 * ==================================================
 * <p>
 * FileName: MaxExpHandler
 *
 * @author : shihongwei
 * @create 2018/11/16
 * @since 1.0.0
 * 〈功能〉：数字最大表达式解析器
 * ==================================================
 */
package com.zds.boot.exp.sysexp.handler.exp;

import com.zds.boot.exp.comm.util.EmptyChecker;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;

/** 表达式样例  max(18)*/
public class MaxExpHandler extends AbsExpressionHandler{

    private BigDecimal max;

    public MaxExpHandler(BigDecimal max) {
        this.max=max;
    }
    @Override
    public boolean runExp(String param,Object obj) {
        if (EmptyChecker.isEmpty(param)){
            return true;
        }
        if (!NumberUtils.isNumber(param)){
            return false;
        }
        return max.compareTo(new BigDecimal(param))>=0;
    }
}
