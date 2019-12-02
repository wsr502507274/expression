/**
 * ==================================================
 * <p>
 * FileName: SystemExpAnalysisFactory
 *
 * @author : shihongwei
 * @create 2018/11/15
 * @since 1.0.0
 * 〈功能〉：表达式解析工厂
 * ==================================================
 */
package com.zds.boot.exp.sysexp.factory;

import com.zds.boot.common.exception.BusinessException;
import com.zds.boot.exp.comm.constant.ValidateConstans;
import com.zds.boot.exp.comm.util.EmptyChecker;
import com.zds.boot.exp.sysexp.container.SystemExpressionContainer;
import com.zds.boot.exp.sysexp.handler.exp.*;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/** *
 *========================================
 *表达式解析工厂：
 * 1 逻辑符 只支持 & |
 * 2 暂不支持 复杂逻辑运算关系解析   如:使用括号提高优先级等  (a&b)|c
 *========================================
*/


@Slf4j
public class SystemExpAnalysisFactory extends SystemExpressionContainer {

    private static final String AND="&";
    private static final String OR="|";
    private static final String SPLIT="[&|]";
    private static final String SYSTEM_EXP_TYPE="3";
    private static final String JAVA_EXP_TYPE="4";
    private static final String NOT_EMPTY ="notEmpty";
    private static final String EQUALS="equals";
    private static final String LENGTH="length";
    private static final String MAX="max";
    private static final String MIN="min";
    private static final String DATE="date";
    private static final String DECIMAL="decimal";
    private static final String IS_NUMBER="isNumber";
    private static final String IS_CERT_NO ="isCertNo";
    private static final String JAVA_VALIDATE="${java_validate}*";

    private static final String EMPTY_STRING="";
    private static final String LEFT="\\(";
    private static final String RIGHT="\\)";

    private SystemExpAnalysisFactory(){}

    /* *
     *========================================
     * @方法说明 ： 获取表达式解析器集
     * @author : shihongwei

     * @param exp 表达式
     * @return      java.util.List<AbsExpressionHandler>
     * @exception
     * @创建时间：     2018/11/16 11:36
     *========================================
    */
    public static List<AbsExpressionHandler> analysisExp(String exp ){
        List<AbsExpressionHandler> expList = new ArrayList<>();

        //如果包含逻辑运算符
        if (exp.contains(AND)|| exp.contains(OR)){
            String[] split = exp.split(SPLIT);
            buildHandlerForList(split,expList);
            return expList;
        }

        buildHandler(exp,expList);

        return expList;
    }

    /* *
     *========================================
     * @方法说明 ： 组装集合表达式解析器
     * @author : shihongwei

     * @param split 表达式切割后数组
     * @param expList 表达式解析器集合
     * @return      void
     * @exception
     * @创建时间：     2018/11/16 11:13
     *========================================
    */
    private static void buildHandlerForList(String[] split, List<AbsExpressionHandler> expList ) {
        Arrays.stream(split).forEach(ex ->buildHandler(ex,expList));
    }

    /* *
     *========================================
     * @方法说明 ： 组装表达式解析器
     * @author : shihongwei
     * @param exp 表达式
     * @param expList 表达式解析器集合
     * @return      void
     * @exception
     * @创建时间：     2018/11/16 11:13
     *========================================
    */
    private static void buildHandler(String exp, List<AbsExpressionHandler> expList ) {
        AbsExpressionHandler expressionHandler = getExpressionHandler(exp);
        if (EmptyChecker.isEmpty(expressionHandler)){
            expressionHandler=initHandlerForExp(exp);
            setExpressionHandler(exp,expressionHandler);
        }
        expList.add(expressionHandler);
    }

    /* *
     *========================================
     * @方法说明 ： 初始化对应表达式解析器到容器
     * @author : shihongwei

     * @param exp 表达式
     * @return      AbsExpressionHandler
     * @exception
     * @创建时间：     2018/11/16 11:44
     *========================================
    */
    private static AbsExpressionHandler initHandlerForExp(String exp ) {
        log.info("[表达式解析工厂]--初始化表达式解析器到容器，exp :{}",exp);
        if (exp.startsWith(NOT_EMPTY)){
            return new NotEmptyExpHandler();
        }
        if (exp.startsWith(EQUALS)){
            return new EqualsExpHandler(exp.substring(EQUALS.length()).replaceAll(LEFT,EMPTY_STRING).replaceAll(RIGHT,EMPTY_STRING));
        }
        if (exp.startsWith(MAX)){
            return new MaxExpHandler(new BigDecimal(exp.substring(MAX.length()).replaceAll(LEFT,EMPTY_STRING).replaceAll(RIGHT,EMPTY_STRING)));
        }
        if (exp.startsWith(MIN)){
            return new MinExpHandler(new BigDecimal(exp.substring(MIN.length()).replaceAll(LEFT,EMPTY_STRING).replaceAll(RIGHT,EMPTY_STRING)));
        }
        if (exp.startsWith(LENGTH)){
            return new LengthExpHandler(Integer.valueOf(exp.substring(LENGTH.length()).replaceAll(LEFT,EMPTY_STRING).replaceAll(RIGHT,EMPTY_STRING)));
        }

        if (exp.startsWith(DATE)){
            return new DateExpHandler(exp.substring(DATE.length()).replaceAll(LEFT,EMPTY_STRING).replaceAll(RIGHT,EMPTY_STRING));
        }

        if (exp.startsWith(DECIMAL)){
            return new DecimalPlaceExpHandler(exp.substring(DECIMAL.length()).replaceAll(LEFT,EMPTY_STRING).replaceAll(RIGHT,EMPTY_STRING));
        }
        if (exp.startsWith(IS_CERT_NO)){
            return new IsCertNoHandler();
        }

        if (exp.startsWith(IS_NUMBER)){
            return new IsNumberExpHandler();
        }

        if (exp.startsWith(JAVA_VALIDATE)){
            return new JavaExpHandler(exp.substring(JAVA_VALIDATE.length()));
        }

        log.error("[表达式解析工厂]--未匹配到对应的系统表达式类型 传入表达式={}",exp);
        throw new BusinessException(ValidateConstans.ERR_CODE,"[表达式解析工厂]--未匹配到对应的系统表达式类型");
    }


    
    /* *
     *========================================
     * @方法说明 ： 清空表达式解析器容器
     * @author : shihongwei
     * @param exp
     * @param expressionHandler
     * @return      void
     * @exception   
     * @创建时间：     2018/11/26 14:26
     *========================================
    */
    public static void clearParentContainer(){
        clearContainer();
    }

    /* *
     *========================================
     * @方法说明 ： 获取父容器
     * @author : shihongwei
     * @return      java.util.Map<java.lang.String,AbsExpressionHandler>
     * @exception
     * @创建时间：     2018/11/26 14:46
     *========================================
    */
    public static Map<String,AbsExpressionHandler> getParentContainer() {
        return getContainer();
    }

    /* *
     *========================================
     * @方法说明 ： 设置表达式解析器
     * @author : shihongwei
     * @param exp
     * @param expressionHandler
     * @return      void
     * @exception   
     * @创建时间：     2018/11/26 15:28
     *========================================
    */
    public static void setHandler(String exp, AbsExpressionHandler expressionHandler) {
        setExpressionHandler(exp,expressionHandler);
    }
}
