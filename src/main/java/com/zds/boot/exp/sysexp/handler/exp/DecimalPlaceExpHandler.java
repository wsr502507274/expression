/**
 * ==================================================
 * <p>
 * FileName: MaxExpHandler
 *
 * @author : shihongwei
 * @create 2018/11/16
 * @since 1.0.0
 * 〈功能〉：数字最大表达式解析器
 * ==================================================
 */
package com.zds.boot.exp.sysexp.handler.exp;

import com.zds.boot.common.exception.BusinessException;
import com.zds.boot.exp.comm.enums.ResultCodeEnum;
import com.zds.boot.exp.comm.util.EmptyChecker;
import org.apache.commons.lang3.math.NumberUtils;

/** 表达式样例  decimal(16,2)*/
public class DecimalPlaceExpHandler extends AbsExpressionHandler{
    // 小数位
    private int decimal=0;
    // 整数部分
    private int lon=0;

    public DecimalPlaceExpHandler(String decimal) {

        if (decimal.contains(",")){
            String[] split = decimal.split(",");
            if (split.length>2
                    || !NumberUtils.isNumber(split[0])
                    || !NumberUtils.isNumber(split[1])
                    ||Integer.valueOf(split[0])<Integer.valueOf(split[1])){

                throw new BusinessException(String.format("表达式初始化失败,当前（\"%s\"），格式错误，{eg：decimal(16,2)}",decimal),ResultCodeEnum.SYS_ERR.getCode());
            }


            this.decimal=Integer.valueOf(split[1]);
            this.lon=Integer.valueOf(split[0])-this.decimal;
            return ;
        }

        this.lon=Integer.valueOf(decimal);
    }
    @Override
    public boolean runExp(String param,Object obj) {
        if (EmptyChecker.isEmpty(param)){
            return true;
        }
        if (!NumberUtils.isNumber(param)){
            return false;
        }
        String[] split = param.split("\\.");
        if (split.length==1){
            return split[0].length() <=lon;
        }

        return split[1].length() <= decimal || split[0].length() <=lon;
    }


    /* *
     *========================================
     * @方法说明 ： 修正小数长度 末尾截取
     * @author : shihongwei
     * @return      java.lang.Object
     * @exception
     * @创建时间：     2019/4/28 19:07
     *========================================
    */
    public Object amendment(String params){

        if (EmptyChecker.notEmpty(params) && NumberUtils.isNumber(params)){
            String[] split = params.split("\\.");
            if (split[0].length()>lon){
                throw new BusinessException("整数部分超长，修复失败",ResultCodeEnum.SYS_ERR.getCode());
            }
            String dec=split[1].substring(0,decimal);
            return split[0].concat("\\.").concat(dec);
        }
        throw new BusinessException("数字修复失败",ResultCodeEnum.SYS_ERR.getCode());
    }
}
