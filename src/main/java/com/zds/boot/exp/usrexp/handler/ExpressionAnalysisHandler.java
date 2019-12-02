package com.zds.boot.exp.usrexp.handler;

import com.zds.boot.common.exception.BusinessException;
import com.zds.boot.exp.comm.constant.ValidateConstans;
import com.zds.boot.exp.comm.enums.ExpressionNodeType;
import com.zds.boot.exp.comm.util.EmptyChecker;
import com.zds.boot.exp.usrexp.entity.ExpressionNode;
import com.zds.boot.exp.usrexp.entity.ExpressionParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/**
 * ==================================================
 * <p>
 * FileName: ExpressionAnalysisHandler
 *
 * @author : shihongwei
 * @create 2019/4/10
 * @since 1.0.0
 * 〈功能〉：表达式解析处理器
 * ==================================================
 */

@Slf4j
public class ExpressionAnalysisHandler {
    private ExpressionAnalysisHandler() {

    }

    /* *
     *========================================
     * @方法说明 ： 将算术表达式转换为逆波兰表达式
     * @author : shihongwei
     * @param expression
     * @return      java.util.List<com.zds.credit.common.exp.usrexp.entity.ExpressionNode>
     * @exception
     * @创建时间：     2019/4/10 16:40
     *========================================
     */
    public static List<ExpressionNode> parseExpression(String expression) {

        // 定义运算节点集合
        List<ExpressionNode> listOperator = new ArrayList<>(10);
        //运算符栈
        Deque<ExpressionNode> deque = new ArrayDeque<>();
        //初始化表达式装载引擎
        ExpressionParser expParser = new ExpressionParser(expression);

        ExpressionNode [] unitaryNode = new ExpressionNode [1];
        ExpressionNode expNode;

        //是否需要操作数
        boolean requireOperand = false;


        while ((expNode = expParser.readNode()) != null) {

            log.debug("当前deque  {}",deque);

            if ((expNode.getType() == ExpressionNodeType.NUMERIC) ||
                    (expNode.getType() == ExpressionNodeType.STRING) ||
                    (expNode.getType() == ExpressionNodeType.DATE)) {
                //操作数， 直接加入后缀表达式中
                if (EmptyChecker.notEmpty(unitaryNode[0])) {
                    //设置一元操作符节点
                    expNode.setUnitaryNode(unitaryNode[0]);
                    unitaryNode[0] = null;
                }
                listOperator.add(expNode);
                requireOperand = false;
            } else if (expNode.getType() == ExpressionNodeType.L_PARENTHESES) {
                //左括号， 直接加入操作符栈
                deque.push(expNode);
            } else if (expNode.getType() == ExpressionNodeType.R_PARENTHESES) {
                //匹配右括号，如果匹配失败则异常
                matchRParentheses(listOperator, deque, expParser);
            } else {
                requireOperand = assessOperation(requireOperand,expNode,unitaryNode,expParser, deque, listOperator);
            }
        }

        if (requireOperand) {
            //丢失操作数
            throw new BusinessException(ValidateConstans.ERR_CODE,String.format("表达式\"%s\"在位置\"%s\"上缺少操作数!", expParser.getExpression(), expParser.getPosition()));
        }
        //将运算符加入运算节点集合
        setOperationAndClearDequn(listOperator, deque, expParser);
        return listOperator;
    }

    /* *
     *========================================
     * @方法说明 ： 判断操作符
     * @author : shihongwei
     * @param requireOperand
     * @param expNode
     * @param unitaryNode
     * @param expParser
     * @param deque
     * @param listOperator
     * @return      boolean
     * @exception
     * @创建时间：     2019/4/18 15:58
     *========================================
    */
    private static boolean assessOperation(boolean requireOperand,ExpressionNode expNode,ExpressionNode [] unitaryNode,
                                       ExpressionParser expParser,Deque<ExpressionNode> deque,List<ExpressionNode> listOperator){

        if (deque.isEmpty()) {
            //第一个节点则判断此节点是否是 !,(
            log.debug("判断是否为 !( {}",isUnitary(listOperator, expNode));
            if (isUnitary(listOperator, expNode)) {
                // 如果不是左括号或！
                //后缀表达式没有任何数据则判断是否是一元操作数  + -
                log.debug("判断是否为 + - {}",ExpressionNode.isUnitaryNode(expNode.getType()));
                if (ExpressionNode.isUnitaryNode(expNode.getType())) {

                    ExpressionNode exprTemp = new ExpressionNode();
                    BeanUtils.copyProperties(expNode,exprTemp);
                    unitaryNode[0] = expNode;
                }else {
                    //丢失操作数
                    throw new BusinessException(ValidateConstans.ERR_CODE,String.format("表达式\"%s\"在位置(%s)上缺少操作数!", expParser.getExpression(), expParser.getPosition()));
                }
            } else {
                //如果是（或! 则直接压入操作符栈
                deque.push(expNode);
            }
            //下一个节点需要操作数
            requireOperand = true;
        } else {
            //判定其他
            requireOperand=assessOther(requireOperand,expNode,unitaryNode,expParser, deque, listOperator);
        }
        return requireOperand;
    }

    /* *
     *========================================
     * @方法说明 ： 判定其他
     * @author : shihongwei
     * @param requireOperand
     * @param expNode
     * @param unitaryNode
     * @param expParser
     * @param deque
     * @param listOperator
     * @return      boolean
     * @exception
     * @创建时间：     2019/4/18 14:49
     *========================================
    */
    private static boolean assessOther(boolean requireOperand,ExpressionNode expNode,ExpressionNode [] unitaryNode,
                                ExpressionParser expParser,Deque<ExpressionNode> deque,List<ExpressionNode> listOperator){


        if (requireOperand) {
            //如果需要操作数则判断当前的是否是"+","-"号(一元操作符),如果是则继续
            if (ExpressionNode.isUnitaryNode(expNode.getType()) && unitaryNode[0] == null) {
                unitaryNode[0] = expNode;
            }else {
                //丢失操作数
                throw new BusinessException(ValidateConstans.ERR_CODE,String.format("表达式\"%s\"在位置\"%s\"上缺少操作数!", expParser.getExpression(), expParser.getPosition()));
            }
        } else {
            operSort(listOperator, deque, expNode);
            //将操作符压入操作符栈
            deque.push(expNode);
            return true;
        }

        return requireOperand;
    }

    /* *
     *========================================
     * @方法说明 ： 判断是否是 ！ （
     * @author : shihongwei
     * @param listOperator
     * @param expNode
     * @return      boolean
     * @exception
     * @创建时间：     2019/4/18 14:03
     *========================================
    */
    private static boolean isUnitary(List<ExpressionNode> listOperator, ExpressionNode expNode) {
        return listOperator.isEmpty()
                &&!(expNode.getType() == ExpressionNodeType.L_PARENTHESES
                || expNode.getType() == ExpressionNodeType.NOT);
    }


    /* *
     *========================================
     * @方法说明 ： 对前面的所有操作符进行优先级比较
     * @author : shihongwei
     * @param listOperator
     * @param deque
     * @param expNode
     * @return      void
     * @exception
     * @创建时间：     2019/4/10 19:57
     *========================================
     */
    private static void operSort(List<ExpressionNode> listOperator, Deque<ExpressionNode> deque, ExpressionNode expNode) {
        //对前面的所有操作符进行优先级比较
        ExpressionNode beforeExpNode;
        do {
            //取得上一次的操作符
            beforeExpNode = deque.peek();
            //如果前一个操作符优先级较高，则将前一个操作符加入后缀表达式中
            if (beforeExpNode != null && beforeExpNode.getType() != ExpressionNodeType.L_PARENTHESES
                    && (beforeExpNode.getPri() - expNode.getPri()) >= 0) {
                listOperator.add(deque.pop());
            } else {
                break;
            }
        } while (!deque.isEmpty());
    }

    /* *
     *========================================
     * @方法说明 ： 将运算符加入运算节点集合
     * @author : shihongwei
     * @param listOperator
     * @param deque
     * @param expParser
     * @return      void
     * @exception
     * @创建时间：     2019/4/10 19:50
     *========================================
     */
    private static void setOperationAndClearDequn(List<ExpressionNode> listOperator, Deque<ExpressionNode> deque, ExpressionParser expParser) {
        ExpressionNode beforeExpNode;//清空堆栈
        while (!deque.isEmpty()) {
            //取得操作符
            beforeExpNode = deque.pop();
            if (beforeExpNode.getType() == ExpressionNodeType.L_PARENTHESES) {
                throw new BusinessException(ValidateConstans.ERR_CODE,String.format("表达式\"%s\"中括号不匹配,丢失右括号!", expParser.getExpression()));
            }
            listOperator.add(beforeExpNode);
        }
    }

    /* *
     *========================================
     * @方法说明 ： 匹配右括号，如果匹配失败则异常
     * @author : shihongwei
     * @param listOperator
     * @param deque
     * @param expParser
     * @return      void
     * @exception
     * @创建时间：     2019/4/10 19:33
     *========================================
     */
    private static void matchRParentheses(List<ExpressionNode> listOperator, Deque<ExpressionNode> deque, ExpressionParser expParser) {
        //右括号则在操作符栈中反向搜索，直到遇到匹配的左括号为止，将中间的操作符依次加到后缀表达式中。
        ExpressionNode lpNode = null;
        while (!deque.isEmpty()) {
            lpNode = deque.pop();
            if (lpNode.getType() == ExpressionNodeType.L_PARENTHESES) {
                break;
            }
            listOperator.add(lpNode);
        }
        if (lpNode == null || lpNode.getType() != ExpressionNodeType.L_PARENTHESES) {
            throw new BusinessException(ValidateConstans.ERR_CODE,String.format("在表达式\"%s\"中没有与在位置(%s)上\")\"匹配的\"(%s)\"字符!", expParser.getExpression(), expParser.getPosition(), ExpressionNodeType.L_PARENTHESES));
        }
    }


}
