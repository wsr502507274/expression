/**
 * ==================================================
 * <p>
 * FileName: ResultCodeEnum
 *
 * @author : shihongwei
 * @create 2018/10/20
 * @since 1.0.0
 * 〈功能〉：返回码定义枚举
 * ==================================================
 */
package com.zds.boot.exp.comm.enums;

public enum ExpTypeEnum {


    SYS_EXP("1", "系统表达式"),
    USR_EXP("2","自定义表达式");


    /**
     * ========================end=========================
     */


    private String code;
    private String desc;

    private ExpTypeEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

    public String getCode() {
        return code;
    }

    public String getDesc() {
        return desc;
    }
}
