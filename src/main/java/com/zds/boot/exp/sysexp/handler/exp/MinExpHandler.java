/**
 * ==================================================
 * <p>
 * FileName: MinExpHandler
 *
 * @author : shihongwei
 * @create 2018/11/16
 * @since 1.0.0
 * 〈功能〉：数字最小表达式解析器
 * ==================================================
 */
package com.zds.boot.exp.sysexp.handler.exp;

import com.zds.boot.exp.comm.util.EmptyChecker;
import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigDecimal;

/** 表达式样例  min(18)*/
public class MinExpHandler extends AbsExpressionHandler{

    private BigDecimal min;

    public MinExpHandler(BigDecimal min) {
        this.min=min;
    }
    @Override
    public boolean runExp(String param,Object obj) {
        if (EmptyChecker.isEmpty(param)){
            return true;
        }
        if (!NumberUtils.isNumber(param)){
            return false;
        }
        return min.compareTo(new BigDecimal(param))<=0;
    }
}
