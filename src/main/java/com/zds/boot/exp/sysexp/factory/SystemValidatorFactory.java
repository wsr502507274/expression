/**
 * ==================================================
 * <p>
 * FileName: SystemValidatorFactory
 *
 * @author : shihongwei
 * @create 2018/11/15
 * @since 1.0.0
 * 〈功能〉：校验器工厂
 * ==================================================
 */
package com.zds.boot.exp.sysexp.factory;

import com.zds.boot.exp.comm.util.EmptyChecker;
import com.zds.boot.exp.sysexp.handler.validator.ValidateBaseHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SystemValidatorFactory {

    private SystemValidatorFactory(){}


    /* *
     *========================================
     * @方法说明 ： 获取校验器
     * @author : shihongwei
     * @param systemNo 系统编号
     * @param exp 表达式
     * @return      ValidateBaseHandler
     * @exception
     * @创建时间：     2018/11/19 9:05
     *========================================
    */
    public static ValidateBaseHandler getValidateHandler(String exp){
        if ( EmptyChecker.isEmpty(exp)){
            return null;
        }
        return getHandler(exp);
    }

    /* *
     *========================================
     * @方法说明 ： 组装校验器
     * @author : shihongwei
    
     * @param exp 表达式
     * @return      void
     * @exception   
     * @创建时间：     2018/11/15 20:28
     *========================================
    */
    private static ValidateBaseHandler getHandler(String exp) {
        //组装执行器
        ValidateBaseHandler handler = new ValidateBaseHandler();
        handler.setCombo(false);
        // 解析表达式 并设置表达式执行器
        handler.setExpList(SystemExpAnalysisFactory.analysisExp(exp));
        return handler;
    }

}
