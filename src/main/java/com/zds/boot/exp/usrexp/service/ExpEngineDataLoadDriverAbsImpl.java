package com.zds.boot.exp.usrexp.service;

import com.zds.boot.common.exception.BusinessException;
import com.zds.boot.exp.comm.util.EmptyChecker;
import com.zds.boot.exp.usrexp.factory.ExpLoadDriverFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * ==================================================
 * FileName: ExpEngineDataLoadDriverAbsImpl
 *
 * @author : shihongwei
 * @create 2019/4/11
 * @since 1.0.0
 * 〈功能〉：扩展表达式解析引擎自定义数据加载方法
 * ==================================================
 * 如若需要表达式解析引擎可以执行自定义数据加载方法需要如下几步：
 *      * 1、你需要继承 ExpEngineDataLoadDriverAbsImpl 并将自己交由ioc管理，并手动指定一个bean—name，这个抽象类并重写2个方法，
 *      * 1.1、init 方法：
 *              1）首先你需要将这个方法上添加@PostConstruct注解
 *              2）调用super.initLoad()方法 将自定义的 spring-bean-name 和本类加载需要的参数列表传入
 *      * 1、
 */
@Slf4j
public  abstract class ExpEngineDataLoadDriverAbsImpl<T> implements ExpEngineLoadDriver {


    /* *
     *========================================
     * @方法说明 ： 初始化加载驱动类和参数列表
     * @author : shihongwei
     * @param beanName      手动指定一个bean—name
     * @param params        本类加载需要的参数列表传入（特指 doExecute 方法参数列表，一般指元素库字段名）
     * @return      void
     * @exception   
     * @创建时间：     2019/4/11 13:58
     *========================================
    */
    protected void initLoad(String beanName, List<String> params){
        log.info("表达式解析引擎--初始化..initLoad==>beanName:{}，参数为：{}",beanName,params);
        if (EmptyChecker.isEmpty(beanName)){
            throw  new BusinessException("999999","表达式解析引擎--初始化失败{自定义数据加载类容器bean-name不能为空}");
        }
        ExpLoadDriverFactory.putDriver(beanName,params);
    }

}
