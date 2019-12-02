package com.zds.boot.exp;

import com.zds.boot.common.exception.BusinessException;
import com.zds.boot.exp.comm.context.ValidateContext;
import com.zds.boot.exp.comm.enums.ExpTypeEnum;
import com.zds.boot.exp.comm.enums.SeriousLevelEnum;
import com.zds.boot.exp.sysexp.entity.ValidateDto;
import com.zds.boot.exp.sysexp.factory.SystemExpAnalysisFactory;
import com.zds.boot.exp.sysexp.factory.SystemValidatorFactory;
import com.zds.boot.exp.sysexp.handler.exp.AbsExpressionHandler;
import com.zds.boot.exp.sysexp.handler.validator.ValidateBaseHandler;
import com.zds.boot.exp.usrexp.factory.ExpAnalysisEngine;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.*;

/**
 * ==================================================
 * <p>
 * FileName: ValidateChain
 *
 * @author : shihongwei
 * @create 2019/4/28
 * @since 1.0.0
 * 〈功能〉：校验链
 * ==================================================
 */
@Data
@Slf4j
public class ValidateChain {
    /** 下一跳*/
    private ValidateChain next;

    /** 字段名*/
    private String className;
    /** 字段名*/
    private String field;
    /** 字段值*/
    private Object value;

    /** 表达式信息*/
    private List<ValidateDto> validates = new ArrayList<>();


    /* *
     *========================================
     * @方法说明 ： 执行校验
     * @author : shihongwei
     * @return      void
     * @exception
     * @创建时间：     2019/4/29 15:14
     *========================================
    */
    public <T> void  doValidate(T data){
        Object thisData = getClassData(data);
        //赋值value
        if (thisData !=null){
            setValue(thisData,thisData.getClass());
        }

        // 阻断容器
        Map<String, List<String>> interdiction = ValidateContext.getContext().getInterdiction();
        // 警告容器
        Map<String, List<String>> warn = ValidateContext.getContext().getWarn();

        validates.stream().filter(Objects::nonNull).forEach(poll ->{
            String validateExpType = poll.getValidateExpType();
            // 系统表达式
            if (ExpTypeEnum.SYS_EXP.getCode().equals(validateExpType)){
                sysValidate(interdiction, warn, poll,thisData);
                // 自定义表达式
            }else if (ExpTypeEnum.USR_EXP.getCode().equals(validateExpType)){
                usrValidate(interdiction, poll,thisData);
            }
        });
        if (next !=null){
            next.doValidate(data);
        }
    }

    /* *
     *========================================
     * @方法说明 ： 根据className获取data实体
     * @author : shihongwei
     * @param data
     * @return      java.lang.Object
     * @exception
     * @创建时间：     2019/7/17 14:57
     *========================================
    */
    private <T> Object getClassData(T data) {

        if (ValidateChainBuilder.DEFAULT.equals(className)){
            return data;
        }

        Field declaredField = null;
        try {
            declaredField = data.getClass().getDeclaredField(className);
        }catch (Exception e){
            log.debug("获取当前对象字段失败");
            return null;
        }

        declaredField.setAccessible(true);
        try {
            return declaredField.get(data);
        } catch (Exception e) {
            log.error("反射获取当前值失败》field：{}，data：{}",field,data,e);
            return null;
        }
    }

    private <T> void setValue(T data,Class clazz) {
        Field declaredField = null;
        try {
            declaredField = clazz.getDeclaredField(field);
        }catch (Exception e){
            log.debug("获取当前对象字段失败，递归父级获取");
        }

        if (declaredField ==null && clazz != Object.class){
            setValue(data,clazz.getSuperclass());
            return ;
        }



        if (declaredField ==null){
            value =null;
            return;
        }

        declaredField.setAccessible(true);
        try {
            value=declaredField.get(data);
        } catch (Exception e) {
            log.error("反射获取当前值失败》field：{}，data：{}",field,data,e);
        }
    }

    /* *
     *========================================
     * @方法说明 ： 自定义表达式校验
     * @author : shihongwei
     * @param interdiction
     * @param poll
     * @return      void
     * @exception
     * @创建时间：     2019/4/29 15:13
     *========================================
    */
    private <T> void usrValidate(Map<String, List<String>> interdiction, ValidateDto poll, T data) {
        boolean usrTag = ExpAnalysisEngine.calculateLogical(poll.getValidateExp(), data);
        // 异常项  加入阻断列表
        if (!usrTag){
            addMsg(interdiction, poll);
        }
    }

    /* *
     *========================================
     * @方法说明 ： 系统表达式校验
     * @author : shihongwei
     * @param interdiction
     * @param warn
     * @param poll
     * @return      void
     * @exception
     * @创建时间：     2019/4/29 15:13
     *========================================
    */
    private <T> void sysValidate(Map<String, List<String>> interdiction, Map<String, List<String>> warn, ValidateDto poll, T data) {
        ValidateBaseHandler validateHandler = SystemValidatorFactory.getValidateHandler(poll.getValidateExp());
        if (validateHandler ==null){
            log.error("根据表达式，未能获取到校验handler,exp:{}",poll.getValidateExp());
            throw new BusinessException("999999","根据系统编码和表达式，未能获取到校验handler");
        }
        boolean sysTag = validateHandler.doValidate(String.valueOf(value), null);
        // 校验不通过
        if (!sysTag){
            sysHanding(interdiction, warn, poll,data);

        }
    }
    /* *
     *========================================
     * @方法说明 ： 系统表达式异常处理
     * @author : shihongwei
     * @param interdiction
     * @param warn
     * @param poll
     * @return      void
     * @exception
     * @创建时间：     2019/4/29 15:13
     *========================================
    */
    private <T> void sysHanding(Map<String, List<String>> interdiction, Map<String, List<String>> warn, ValidateDto poll, T data) {
        // 忽略提示 并修正的
        if (SeriousLevelEnum.IGNORED.getCode().equals(poll.getValidateLevel())){
            List<AbsExpressionHandler> absExpressionHandlers = SystemExpAnalysisFactory.analysisExp(poll.getCorrectRule());
            // 则尝试修正数据
            try {
                absExpressionHandlers.forEach(handler -> amendmentData(handler,data));
            } catch (Exception e) {
                addMsg(interdiction, poll);
            }
            // 异常项  加入阻断列表
        }else if (SeriousLevelEnum.INTERDICTION.getCode().equals(poll.getValidateLevel())){
            addMsg(interdiction, poll);
        //警告项  加入警告列表
        }else if (SeriousLevelEnum.WARING.getCode().equals(poll.getValidateLevel())){
            addMsg(warn, poll);
        }
    }
    /* *
     *========================================
     * @方法说明 ： 提示信息处理
     * @author : shihongwei
     * @param lis
     * @param poll
     * @return      void
     * @exception
     * @创建时间：     2019/4/29 15:14
     *========================================
    */
    private void addMsg(Map<String, List<String>> lis, ValidateDto poll) {
        if (!lis.containsKey(field)){
            List<String> li= new ArrayList<>();
            li.add(poll.getValidateMsg());
            lis.put(field,li);
            return;
        }
        lis.get(field).add(poll.getValidateMsg());
    }

    /* *
     *========================================
     * @方法说明 ： 修正处理
     * @author : shihongwei
     * @param validate
     * @return      void
     * @exception
     * @创建时间：     2019/4/29 15:14
     *========================================
    */
    private <T> void amendmentData(AbsExpressionHandler validate,T data) {
        // 如果校验不通过 则尝试修正数据
        Object amendment = validate.amendment(String.valueOf(value));
        try {
            Field declaredField = data.getClass().getDeclaredField(field);
            declaredField.setAccessible(true);
            declaredField.set(data,amendment);
        } catch (Exception e) {
            log.error("反射设置值失败");
            throw new BusinessException("999999","反射设置值失败");
        }
    }
}
