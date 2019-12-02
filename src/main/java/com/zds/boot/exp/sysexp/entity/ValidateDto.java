package com.zds.boot.exp.sysexp.entity;

import com.zds.boot.common.facade.DtoBase;
import com.zds.boot.exp.ValidateChainBuilder;
import lombok.Getter;
import lombok.Setter;

/**
 * ==================================================
 * FileName: ValidateInfoRespDto
 *
 * @author: 信贷核心
 * @create: 2019-04-17 20:09
 * @since: 1.0.0
 * @description: 校验规则信息表
 * ==================================================
 */
@Getter
@Setter
public class ValidateDto extends DtoBase {
    /**
     * 表单节点编码
     */
    private String nodeCode;

    /**实体名称*/
    private String className = ValidateChainBuilder.DEFAULT;

    /**字段名称*/
    private String fieldName;

    /**
     * 校验规则编码
     */
    private String validateCode;

    /**
     * 规则名称
     */
    private String validateName;

    /**
     * 校验规则表达式
     */
    private String validateExp;

    /**
     * 是否邮箱字段
     */
    private Boolean isEmail;

    /**
     * 是否身份证号
     */
    private Boolean isIdNo;

    /**
     * 是否手机号
     */
    private Boolean isMobile;

    /**
     * 校验提示信息
     */
    private String validateMsg;

    /**
     * 校验表达式类型
     *
     * 1 正则
     * 2 excel表达式
     * 3 系统表达
     * 4.java方法解析
     * 5，引入其他校验器
     */
    private String validateExpType;

    /**
     * 校验级别（0，忽略提示;1，阻断级别;2，警告级别）
     */
    private String validateLevel;

    /**修正规则*/
    private String correctRule;

    /**
     * 启用状态
     */
    private Boolean isStart;

    /**校验表达式原值*/
    private String validateValue;
}
