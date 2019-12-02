/**
 * ==================================================
 * <p>
 * FileName: ValidateBaseHandler
 *
 * @author : shihongwei
 * @create 2018/11/15
 * @since 1.0.0
 * 〈功能〉：基本的统一的校验器
 * ==================================================
 */
package com.zds.boot.exp.sysexp.handler.validator;

import com.zds.boot.exp.sysexp.handler.exp.AbsExpressionHandler;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Setter
@Getter
@Slf4j
public class ValidateBaseHandler {

    /**是否组合校验*/
    private boolean isCombo;

    /** 表达式解析器集*/
    private List<AbsExpressionHandler> expList;

    /* *
     *========================================
     * @方法说明 ： 执行校验
     * @author : shihongwei
     * @param value  1 如果是非Java校验-》只传本参数 代表具体值   2 如果是Java类校验时 唯一标示 UUID
     * @param obj    2 如果是非Java校验-》本参数传null  2 如果是Java类校验时 传具体校验类参数的引用
     * @return      void
     * @exception   
     * @创建时间：     2018/11/15 17:27
     *========================================
    */
    public boolean doValidate(String value,Object obj){
        //分别执行表达式解析器 完成校验
        for (AbsExpressionHandler handler : expList) {
            //任意解析器 返回不满足 则直接校验不通过
            if (!handler.runExp(value,obj)){
                return false;
            }
        }
        return true;
    }
}
