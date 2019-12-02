package com.zds.boot.exp.usrexp.factory;

import com.zds.boot.common.boot.ApplicationContextHolder;
import com.zds.boot.exp.comm.dto.LoadDriverInfo;
import com.zds.boot.exp.usrexp.service.ExpEngineLoadDriver;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ==================================================
 * <p>
 * FileName: ExpLoadDriverFactory
 *
 * @author : shihongwei
 * @create 2019/4/11
 * @since 1.0.0
 * 〈功能〉：表达式解析引擎辅助数据加载类工厂
 * ==================================================
 */
@Slf4j
public class ExpLoadDriverFactory {

    /** 用于缓存用于表达式解析引擎辅助数据加载类 容器*/
    private static final Map<String,LoadDriverInfo> DRIVER_MAP = new ConcurrentHashMap<>(16);


    /* *
     *========================================
     * @方法说明 ： 缓存辅助类
     * @author : shihongwei
     * @param key
     * @return      void
     * @exception
     * @创建时间：     2019/4/11 13:50
     *========================================
    */
    public static void putDriver(String key,List<String> params){
        LoadDriverInfo loadDriverInfo = new LoadDriverInfo();
        loadDriverInfo.setParams(params);
        DRIVER_MAP.put(key,loadDriverInfo);
    }


    /* *
     *========================================
     * @方法说明 ： 获取数据加载类和参数信息
     * @author : shihongwei
     * @param key
     * @return      com.zds.credit.common.data.LoadDriverInfo
     * @exception   
     * @创建时间：     2019/4/11 14:53
     *========================================
    */
    public static LoadDriverInfo getDriver(String key){
        LoadDriverInfo loadDriverInfo = DRIVER_MAP.get(key);
        if (loadDriverInfo.getExpEngineLoadDriver() == null){
            loadDriverInfo.setExpEngineLoadDriver(ApplicationContextHolder.get().getBean(key,ExpEngineLoadDriver.class));
        }
        return  loadDriverInfo;

    }
}
