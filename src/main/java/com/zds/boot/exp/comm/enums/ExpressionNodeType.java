package com.zds.boot.exp.comm.enums;

/**
 * ==================================================
 * <p>
 * FileName: ExpressionNodeType
 *
 * @author : shihongwei
 * @create 2019/4/10
 * @since 1.0.0
 * 〈功能〉：节点枚举
 * ==================================================
 */
public enum ExpressionNodeType {

    UNKNOWN,
    PLUS,// +
    SUBTRACT, /// -
    MULTI_PLY,// *
    DIVIDE,// /
    L_PARENTHESES,//(
    R_PARENTHESES, /// )
    MOD,//% (求模,取余)
    POWER,// ^ (次幂)
    BITWISE_AND, /// & (按位与)
    BITWISE_OR,/// | (按位或)
    AND,// && (逻辑与)
    OR, /// || (逻辑或)
    NOT,/// ! (逻辑非)
    EQUAL,/// = (算数相等)
    UNEQUAL,/// != 或 <> (算数不等于)
    GT, /// > (大于)
    LT, /// < (小于)
    GT_OR_EQUAL,/// >= (大于等于)
    LT_OR_EQUAL, /// <= (小于等于)
    L_SHIFT,  /// << (左移位)
    R_SHIFT,/// >> (右移位)
    NUMERIC, /// 数值,
    STRING,
    DATE,
    LIKE,//包含
    NOT_LIKE,//不包含
    START_WITH,//已什么开始
    END_WITH,//已什么结尾
    CONTAINS,//包含
    NOT_CONTAINS,//不包含
    EQ,//字符串相等比较 ==
    NEQ;//字符串不相等比较!==
}
