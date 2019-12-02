/**
 * ==================================================
 * <p>
 * FileName: AbsExpressionHandler
 *
 * @author : shihongwei
 * @create 2018/11/15
 * @since 1.0.0
 * 〈功能〉：表达式解析器
 * ==================================================
 */
package com.zds.boot.exp.sysexp.handler.exp;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public abstract class AbsExpressionHandler {

    /* *
     *========================================
     * @方法说明 ： 执行表达式
     * @author : shihongwei
     * @return      boolean
     * @exception   
     * @创建时间：     2018/11/16 10:28
     *========================================
    */
    public abstract boolean runExp(String param,Object obj);

    /* *
     *========================================
     * @方法说明 ： 按照设定格式修正
     * @author : shihongwei
     * @return      boolean
     * @exception
     * @创建时间：     2018/11/16 10:28
     *========================================
     */
    public Object amendment(String params){

        return null;
    }

}
