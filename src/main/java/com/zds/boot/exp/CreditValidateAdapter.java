package com.zds.boot.exp;

import com.zds.boot.common.exception.BusinessException;
import com.zds.boot.common.utils.ToString;
import com.zds.boot.exp.comm.context.ValidateContext;
import com.zds.boot.exp.comm.enums.ResultCodeEnum;
import com.zds.boot.exp.comm.util.EmptyChecker;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;

/**
 * ==================================================
 * <p>
 * FileName: CreditValidateAdapter
 *
 * @author : shihongwei
 * @create 2019/4/26
 * @since 1.0.0
 * 〈功能〉：信贷表单校验工具
 * ==================================================
 */
@Slf4j
public class CreditValidateAdapter {

    /*  *
     *========================================
     * @方法说明 ： 执行主表单校验
     * @author : shihongwei
     * @param data  参数域
     * @param pid   主编号  （产品编号）
     * @param stage 副编号  （阶段编码）
     * @return      void
     * @exception
     * @创建时间：     2019/4/29 20:37
     *========================================
    */
    public static <T> void doValidate(T data  ,String pid,String stage){
        ValidateChain validateChain = ValidateChainBuilder.buildChain(pid,stage);

        if (EmptyChecker.isEmpty(validateChain)){
            return ;
        }
        try {
            // ##执行校验
            validateChain.doValidate(data);
            // 阻断容器
            Map<String, List<String>> interdiction = ValidateContext.getContext().getInterdiction();
            // 警告容器
            Map<String, List<String>> warn = ValidateContext.getContext().getWarn();

            if (EmptyChecker.notEmpty(warn)){
                log.warn(ResultCodeEnum.PARAMS_VALIDATE_FAIL.getCode(),"本次校验存在警告项，内容如下：{}",warn);
            }

            if (EmptyChecker.notEmpty(interdiction)){
                log.error(ResultCodeEnum.PARAMS_VALIDATE_FAIL.getCode(),"本次校验存在异常项，内容如下：{}",interdiction);
                throw new BusinessException(ToString.toString(interdiction),ResultCodeEnum.PARAMS_VALIDATE_FAIL.getCode());
            }
        } finally {
            ValidateContext.removeContext();
        }

    }

}
