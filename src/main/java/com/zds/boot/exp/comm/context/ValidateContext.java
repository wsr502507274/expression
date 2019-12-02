/**
 * ==================================================
 * <p>
 * FileName: ValidateContext
 *
 * @author : shihongwei
 * @create 2018/11/15
 * @since 1.0.0
 * 〈功能〉：校验引擎上下文
 * ==================================================
 */
package com.zds.boot.exp.comm.context;


import com.zds.boot.exp.sysexp.entity.ValidateEngineGlobalVar;

public class ValidateContext {
    private ValidateContext(){}

    //校验引擎上下文
    private static final ThreadLocal<ValidateEngineGlobalVar> CONTEXT = ThreadLocal.withInitial(ValidateEngineGlobalVar::new);

    /* *
     *========================================
     * @方法说明 ： 获取校验引擎上下文
     * @author : shihongwei
     * @return      ValidateContext
     * @exception   
     * @创建时间：     2018/11/15 20:08
     *========================================
    */
    public static ValidateEngineGlobalVar getContext() {
        return CONTEXT.get();
    }

    /* *
     *========================================
     * @方法说明 ： 移除校验引擎上下文
     * @author : shihongwei
     * @return      void
     * @exception
     * @创建时间：     2018/11/15 20:09
     *========================================
    */
    public static void removeContext() {
        CONTEXT.remove();
    }
}
