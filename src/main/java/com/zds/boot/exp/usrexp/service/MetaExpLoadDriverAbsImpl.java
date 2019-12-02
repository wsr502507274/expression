package com.zds.boot.exp.usrexp.service;

import com.zds.boot.exp.comm.util.EmptyChecker;
import com.zds.boot.exp.usrexp.factory.ExpAnalysisEngine;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * ==================================================
 * <p>
 * FileName: MetaExpLoadDriverAbsImpl
 *
 * @author : shihongwei
 * @create 2019/4/12
 * @since 1.0.0
 * 〈功能〉：表达式预解析类
 * ==================================================
 */
@Slf4j
public abstract class MetaExpLoadDriverAbsImpl<T> implements ExpEngineLoadDriver{
    @Override
    public Object doExecute(String params) {
        return null;
    }

    /* *
     *========================================
     * @方法说明 ： 原表达式 预解析
     * @author : shihongwei
     * @param expList
     * @return      void
     * @exception
     * @创建时间：     2019/4/12 9:27
     *========================================
    */
    public void loadMetaExp(List<String> expList) {
        if (EmptyChecker.notEmpty(expList)){
            log.info("表达式解析引擎--预解析表达式列表..initLoad==>expList:{}，参数为：{}",expList);
            expList.forEach( temp ->ExpAnalysisEngine.analysis(temp,false));
        }

    }
}
