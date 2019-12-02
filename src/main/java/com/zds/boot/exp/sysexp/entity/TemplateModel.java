/*
 * ==================================================
 * <p>
 * FileName: TemplateModel
 *
 * @author : shihongwei
 * @create 2018/11/15
 * @since 1.0.0
 * 〈功能〉：校验引擎单元模型
 * ==================================================
 */
package com.zds.boot.exp.sysexp.entity;

import com.zds.boot.common.facade.DtoBase;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class TemplateModel extends DtoBase {
    private static final long serialVersionUID = 8699527727410870898L;

    /*字段名*/
    /*(请求实体的字段名)*/
    private String fileName;
    /*字段含义*/
    /*(字段含义解释)*/
    private String fileDesc;



    /*提示消息*/
    /*(不满足校验提示信息)*/
    private String message;
    /*表达式/解析类#法名/引入校验器*/
    /*表达式附表
     并且	&
     或者	|
     不为空	notEmpty
     比较	equals(xxxx)
     长度	length(x)
     最大	max(x)
     最小	min(x)
     */
    private String exp;
    /*表达式解析器*/
    /*(
     1.正则
     2.excel表达式
     3.系统表达
     4.java方法解析
     5.引入其他校验器)*/
    private String expType;
    /*严重级别*/
    /*(
     0，忽略提示
     1，阻断级别
     2，警告级别)*/
    private String level;
    /*关联校验id*/
    /*(关联校验编号组
     0 为默认，无关联组，直接单条校验。)*/
    private int relevancyId;
    /*关联校验优先级*/
    /*(数字类型
     根据数字大小确定校验先后顺序)*/
    private String relevancyPriority;



}
