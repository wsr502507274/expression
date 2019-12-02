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

/** 表达式样例  isCertNo()*/
public class IsCertNoHandler extends AbsExpressionHandler{



    @Override
    public boolean runExp(String param,Object obj) {
        if (EmptyChecker.isEmpty(param)){
            return true;
        }
        if(param.length()!=18) {
            return false;
        }
        char [] id =param.toCharArray();
        int i;
        int sum;
        int n;
        for (sum = i = 0; i < 17; i++){
            sum += ((1 << (17 - i)) % 11) * (id[i] - '0');
        }
        n = (12 - (sum % 11)) % 11;
        if (n < 10) {
            return (n == id[17] - '0');
        } else {
            return (id[17] == 'X');
        }
    }
}
