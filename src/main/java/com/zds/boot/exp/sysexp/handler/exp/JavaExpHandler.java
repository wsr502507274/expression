/**
 * ==================================================
 * <p>
 * FileName: JavaExpHandler
 *
 * @author : shihongwei
 * @create 2018/11/16
 * @since 1.0.0
 * 〈功能〉：Java表达式解析器
 * ==================================================
 */
package com.zds.boot.exp.sysexp.handler.exp;

import com.zds.boot.common.exception.BusinessException;
import com.zds.boot.exp.comm.constant.ValidateConstans;
import com.zds.boot.exp.comm.context.ValidateContext;
import com.zds.boot.exp.sysexp.entity.ValidateJavaInvokeResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JavaExpHandler extends AbsExpressionHandler{
    private static final String SPLIT="#";
    private String handingClass;
    @Override
    public boolean runExp(String param,Object obj) {
        String[] sp = handingClass.split(JavaExpHandler.SPLIT);
        try {
            Class<?> aClass = Class.forName(sp[0]);
            ValidateJavaInvokeResult result = (ValidateJavaInvokeResult) aClass.getMethod(sp[1], obj.getClass()).invoke(aClass.newInstance(), obj);
            if (!result.isSuccess()){
                ValidateContext.getContext().getTemp().put(param,result.getDesc());
            }
            return result.isSuccess();
        } catch (Exception e) {
            log.error("Java表达式解析器执行错误，param:{},obj:{}",param,obj,e);
            throw new BusinessException(ValidateConstans.ERR_CODE,"Java表达式解析器执行错误");
        }
    }

    public JavaExpHandler(String handingClass){
        this.handingClass=handingClass;
    }


}
