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

public enum SeriousLevelEnum {


    IGNORED("0", "忽略"),
    INTERDICTION("1", "阻断"),
    WARING("2", "警告");



    private String code;
    private String desc;

    private SeriousLevelEnum(String code, String desc) {
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
