/**
 * ==================================================
 * <p>
 * FileName: TemplateModelContainer
 *
 * @author : shihongwei
 * @create 2018/11/15
 * @since 1.0.0
 * 〈功能〉：校验引擎单元模型容器
 * ==================================================
 */
package com.zds.boot.exp.sysexp.container;

import com.zds.boot.exp.sysexp.entity.TemplateModel;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class TemplateModelContainer {
    /*校验引擎模型容器 Map<商户号-产品基类,Map<全类名,Map<字段名,校验单元模型>>>*/
    protected static final Map<String,Map<String,Map<String,TemplateModel>>> TEMPLATE_CONTAINER = new ConcurrentHashMap<>(16);

    protected TemplateModelContainer(){}

    /* *
     *========================================
     * @方法说明 ： 获取顶级容器
     * @author : shihongwei

     * @return      java.util.Map<java.lang.String,java.util.Map<java.lang.String,java.util.Map<java.lang.String,TemplateModel>>>
     * @exception   
     * @创建时间：     2018/11/15 16:12
     *========================================
    */
    protected static Map<String,Map<String,Map<String,TemplateModel>>> getParentContainer(){
        return TEMPLATE_CONTAINER;
    }

    /* *
     *========================================
     * @方法说明 ： 往父容器插入校验场景
     * @author : shihongwei
    
     * @param key
     * @param value
     * @return      void
     * @exception   
     * @创建时间：     2018/11/15 16:20
     *========================================
    */
    protected static void putToParentContainer(String key,Map<String,Map<String,TemplateModel>> value){
        
        TEMPLATE_CONTAINER.put(key,value);
    }


}
