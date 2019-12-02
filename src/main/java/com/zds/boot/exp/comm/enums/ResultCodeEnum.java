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

public enum ResultCodeEnum {


    /** =====================validate_engine异常信息======================*/
    /** ========================start=========================*/
    PARAMS_VALIDATE_FAIL("700001", "参数校验失败"),
    SYS_ERR("999999", "系统异常");

    /**
     * ========================end=========================
     */


    private String code;
    private String desc;

    private ResultCodeEnum(String code, String desc) {
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
