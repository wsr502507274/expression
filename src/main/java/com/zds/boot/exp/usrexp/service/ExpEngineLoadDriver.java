package com.zds.boot.exp.usrexp.service;

/**
 * ==================================================
 * <p>
 * FileName: ExpEngineLoadDriver
 *
 * @author : shihongwei
 * @create 2019/4/11
 * @since 1.0.0
 * 〈功能〉：表达式解析引擎 辅助数据装载驱动接口
 * ==================================================
 */
public interface ExpEngineLoadDriver<T> {

    /*  *
     *========================================
     * @方法说明 ： 执行加载
     * @author : shihongwei
     * @param params    参数json
     * @return      T
     * @exception   
     * @创建时间：     2019/4/11 13:26
     *========================================
    */
    T doExecute(String params);


    /* *
     *========================================
     * @方法说明 ： 类初始化
     * @author : shihongwei
     * @return      void
     * @exception
     * @创建时间：     2019/4/11 13:29
     *========================================
     *
    */
    void init();
}
