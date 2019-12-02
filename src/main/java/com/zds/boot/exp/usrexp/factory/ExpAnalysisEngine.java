package com.zds.boot.exp.usrexp.factory;

import com.zds.boot.common.boot.ApplicationContextHolder;
import com.zds.boot.common.exception.BusinessException;
import com.zds.boot.exp.comm.constant.ValidateConstans;
import com.zds.boot.exp.comm.dto.LoadDriverInfo;
import com.zds.boot.exp.comm.util.EmptyChecker;
import com.zds.boot.exp.comm.util.JSONUtil;
import com.zds.boot.exp.usrexp.entity.ExpressionNode;
import com.zds.boot.exp.usrexp.handler.ExpressionAnalysisHandler;
import com.zds.boot.exp.usrexp.handler.ExpressionCalculateHandler;
import com.zds.boot.exp.usrexp.service.ExpEngineLoadDriver;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;

/**
 * ==================================================
 * <p>
 * FileName: ExpAnalysisEngine
 *
 * @author : shihongwei
 * @create 2019/4/10
 * @since 1.0.0
 * 〈功能〉：表达式解析引擎主类
 * ==================================================
 */
@Slf4j
public class ExpAnalysisEngine {
    private static final String EXP_CACHE_KEY="cms_common_exp_cache_key->";
    private ExpAnalysisEngine(){}

    /* *
     *========================================
     * @方法说明 ： 分析表达式合法性
     * @author : shihongwei
     * @param expression
     * @return      java.lang.Object
     * @exception
     * @创建时间：     2019/4/10 21:04
     *========================================
    */
    public static List<ExpressionNode> analyzeExp(String expression) {
        log.info("开始分析表达式，当前表达式为  ：{}", expression);
        return analysis(expression,true);
    }

    /* *
     *========================================
     * @方法说明 ： 解析表达式
     * @author : shihongwei
     * @param expression
     * @param tag
     * @return      java.util.List<com.zds.credit.common.exp.usrexp.entity.ExpressionNode>
     * @exception
     * @创建时间：     2019/4/18 17:42
     *========================================
    */
    public static List<ExpressionNode> analysis(String expression,boolean tag) {
        log.info("开始执行表达式解析，当前表达式为  ：{}", expression);
        if (EmptyChecker.isEmpty(expression)) {
            return new ArrayList<>();
        }
        List<ExpressionNode> expressionNodes = ExpressionAnalysisHandler.parseExpression(expression);
        log.info("原始表达式解析完成 解析后表达式运算节点信息为：{}",expressionNodes);
        if (tag){
            return expressionNodes;
        }
        try {
            RedisTemplate redisTemplate = ApplicationContextHolder.get().getBean("redisTemplate",RedisTemplate.class);
            redisTemplate.opsForValue().set(EXP_CACHE_KEY.concat(expression),expressionNodes);
        } catch (Exception e) {
            log.warn("redis缓存表达式执行节点集合失败",e);
        }
        return expressionNodes;
    }

    /* *
     *========================================
     * @方法说明 ： 运算表达式 逻辑运算
     * @author : shihongwei
     * @param expression
     * @param data
     * @return      boolean
     * @exception
     * @创建时间：     2019/4/18 17:36
     *========================================
    */
    public static <T> boolean calculateLogical(String expression,T data) {

        return assessResult(calculate(expression,data));
    }
    /* *
     *========================================
     * @方法说明 ： 运算表达式 可支持算数运算
     * @author : shihongwei
     * @param expression
     * @return      java.lang.Object
     * @exception   
     * @创建时间：     2019/4/10 21:06
     *========================================
    */
    public static <T> Object calculate(String expression,T data) {
        if (EmptyChecker.isEmpty(expression)) {
            return true;
        }

        //1 获取表达式执行节点集合
        List<ExpressionNode> expressionNodes =new ArrayList<>();
        try {
            RedisTemplate redisTemplate = ApplicationContextHolder.get().getBean("redisTemplate",RedisTemplate.class);
            expressionNodes = (List<ExpressionNode>) redisTemplate.opsForValue().get(EXP_CACHE_KEY.concat(expression));
        } catch (Exception e) {
            log.warn("从redis获取表达式执行节点集合失败，将重新解析表达式！！",e);
        }
        if (EmptyChecker.isEmpty(expressionNodes)){
            expressionNodes = analysis(expression,false);
        }
        //2 参数装载
        log.info("表达式解析引擎--【计算】，开始装载-计算节点数据，待装载列表expressionNodes：{}",expressionNodes);
        loadCalculateData(data,expressionNodes);
        log.info("表达式解析引擎--【计算】，完成装载-计算节点数据，结果列表 expressionNodes：{}",expressionNodes);
        //3 执行运算
        return ExpressionCalculateHandler.calcExpression(expressionNodes);
    }

    private static boolean assessResult(Object result) {

        return result instanceof Boolean && (boolean) result;
    }

    /* *
     *========================================
     * @方法说明 ： 参数装载
     * @author : shihongwei
     * @param data
     * @param expressionNodes
     * @return      void
     * @exception
     * @创建时间：     2019/4/11 9:56
     *========================================
    */
    private static <T> void loadCalculateData(T data, List<ExpressionNode> expressionNodes) {
        expressionNodes.forEach(node ->expTypeDispatcher(data, node));
    }

    private static <T> void expTypeDispatcher(T data, ExpressionNode node) {
        String value = node.getValue();
        // 如果是el表达式
        if (value.startsWith("\"${") && value.endsWith("}\"")){
            setMetaData(node,data,value);
        } else if (value.startsWith("\"#")){// 如果是扩展方法获取
            runModelForValue(node,data,value);
        }
    }


    /* *
     *========================================
     * @方法说明 ： 通过方法获取数据    TODO 需要增加详细参数校验
     * @author : shihongwei
     * @param node
     * @param data
     * @param placeholder
     * @return      void
     * @exception
     * @创建时间：     2019/4/11 11:23
     *========================================
    */
    private static <T> void runModelForValue(ExpressionNode node, T data, String placeholder) {
        if (placeholder.length()<2){
            throw new BusinessException(ValidateConstans.ERR_CODE,"通过方法获取数据时，方法不能为空");
        }
        String replaceAll = placeholder.replaceAll("\"", "");
        String exp = replaceAll.substring(1, replaceAll.length());
        //1 切割出  class  method
        String[] clazz = exp.split("\\.");
        //2 切割出 方法和参数列表
        String[] method = clazz[1].split("\\(");
        //3 去除反括号后 通过， 切割具体参数列表
        String[] params = method[1].replaceAll("\\)", "").split(","); //TODO 参数为方法时 不支持
        //4 通过ioc实例名 获取 实例信息
        LoadDriverInfo driver = ExpLoadDriverFactory.getDriver(clazz[0]);
        //5 获取执行实例
        ExpEngineLoadDriver expEngineLoadDriver = driver.getExpEngineLoadDriver();
        //6 获取实例配置参数信息
        List<String> paramsField = driver.getParams();

        Map<String,Object> confirmParams = new HashMap<>(paramsField.size());
        if (paramsField.size() != params.length){
            throw  new BusinessException(ValidateConstans.ERR_CODE,"表达式解析引擎--错误的表达式，通过方法获取数据时，参数列表和方法参数配置不一致");
        }
        //7 递归解析每个参数
        for (int i=0; i< params.length ; i++) {
            ExpressionNode nd = new ExpressionNode();
            nd.setValue(params[i]);
            expTypeDispatcher(data,nd);
            confirmParams.put(paramsField.get(i),nd.getValue());
        }
        //8  执行方法
        Object resp = expEngineLoadDriver.doExecute(JSONUtil.toJson(confirmParams));
        //9 设置具体属性值
        node.setValue(resp==null?null:"\"".concat(resp.toString()).concat("\""));
    }

    /*  *
     *========================================
     * @方法说明 ： 将元数据值赋值到el占位符中
     * @author : shihongwei
     * @param node
     * @param data
     * @param placeholder
     * @return      void
     * @exception
     * @创建时间：     2019/4/11 10:24
     *========================================
    */
    private static <T> void setMetaData(ExpressionNode node, T data,String placeholder) {
        if (placeholder.length()<4){

            throw new BusinessException(ValidateConstans.ERR_CODE,"元数据占位符字段不能为空");
        }
        String replaceAll = placeholder.replaceAll("\"", "");
        String filed = replaceAll.substring(2, replaceAll.length() - 1);
        //设置初始值
        node.setValue(null);


        // 获取数据实体字段
        log.debug("当前填充属性的字段为={} ",filed);
        if (data instanceof Map){
            Object val = ((Map)data).get(filed);
            node.setValue(val==null?null:"\"".concat(val.toString()).concat("\""));
            return;
        }

        Arrays.stream(data.getClass().getDeclaredFields()).filter(fi ->filed.equals(fi.getName())).forEach(fi ->{
            try {
                fi.setAccessible(true);
                Object o = fi.get(data);
                node.setValue(o==null?null:"\"".concat(o.toString()).concat("\""));

            } catch (Exception e) {
                log.error("反射获取数据值失败  反射字段：{}",fi,e);
                throw new BusinessException(ValidateConstans.ERR_CODE,"反射获取数据值失败");
            }
        });

    }

}
