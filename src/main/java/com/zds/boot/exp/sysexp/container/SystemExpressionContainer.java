/**
 * ==================================================
 * <p>
 * FileName: SystemExpressionContainer
 *
 * @author : shihongwei
 * @create 2018/11/15
 * @since 1.0.0
 * 〈功能〉：系统表达式容器
 * ==================================================
 */
package com.zds.boot.exp.sysexp.container;




import com.zds.boot.exp.sysexp.handler.exp.AbsExpressionHandler;

import java.util.HashMap;
import java.util.Map;

public class SystemExpressionContainer {

    private static final Map<String,AbsExpressionHandler> EXP_CONTAINER=new HashMap<>(512);

    protected SystemExpressionContainer(){}

    /* *
     *========================================
     * @方法说明 ： 获取表达式解析器
     * @author : shihongwei
     * @param exp
     * @return      AbsExpressionHandler
     * @exception   
     * @创建时间：     2018/11/15 20:49
     *========================================
    */
    protected static AbsExpressionHandler getExpressionHandler(String exp){

        return EXP_CONTAINER.get(exp);
    }

    /* *
     *========================================
     * @方法说明 ： 新增表达式解析器
     * @author : shihongwei
     * @param exp
     * @param expressionHandler
     * @return      AbsExpressionHandler
     * @exception   
     * @创建时间：     2018/11/15 20:49
     *========================================
    */
    protected static void setExpressionHandler(String exp, AbsExpressionHandler expressionHandler){

        EXP_CONTAINER.put(exp,expressionHandler);
    }

    /* *
     *========================================
     * @方法说明 ： 情空容器
     * @author : shihongwei
     * @param exp
     * @param expressionHandler
     * @return      void
     * @exception
     * @创建时间：     2018/11/26 14:25
     *========================================
    */
    protected static void clearContainer(){
        EXP_CONTAINER.clear();
    }

    protected static Map<String,AbsExpressionHandler> getContainer(){
        return EXP_CONTAINER;
    }

}
