package com.zds.boot.exp.usrexp.entity;

import com.zds.boot.common.exception.BusinessException;
import com.zds.boot.exp.comm.constant.ValidateConstans;
import com.zds.boot.exp.comm.enums.ExpressionNodeType;
import lombok.extern.slf4j.Slf4j;

/**
 * ==================================================
 * <p>
 * FileName: ExpressionParser
 *
 * @author : shihongwei
 * @create 2019/4/10
 * @since 1.0.0
 * 〈功能〉：表达式装载引擎模型
 * ==================================================
 */
@Slf4j
public class ExpressionParser {

    //当前分析的表达式
    private String expression;

    //当前读取的位置
    private int position;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ExpressionParser(String expression) {
        this.expression = expression;
        this.position = 0;
    }

    /* *
     *========================================
     * @方法说明 ： 读取下一个表达式节点,如果读取失败则返回null
     * @author : shihongwei
     * @return      com.zds.credit.common.exp.usrexp.entity.ExpressionNode
     * @exception
     * @创建时间：     2019/4/10 20:59
     *========================================
    */
    public ExpressionNode readNode() {
        //空格的位置
        int whileSpacePos = -1;
        boolean flag = false;
        StringBuilder buffer = new StringBuilder(10);
        while (this.position < this.expression.length()) {
            char c = this.expression.charAt(this.position);
            if (c == '"') {
                flag = !flag;
                if (!flag) {
                    this.position++;
                    buffer.append(c);
                    break;
                }
                if (buffer.length() != 0) {
                    break;
                }
            }
            if (flag) {
                this.position++;
                buffer.append(c);
            } else {
                if (ExpressionNode.isWhileSpace(c)) {
                    if ((whileSpacePos >= 0) && ((this.position - whileSpacePos) > 1)) {
                        throw new BusinessException(ValidateConstans.ERR_CODE,String.format("表达式\"%s\"在位置(%s)上的字符非法!", this.getExpression(), this.getPosition()));
                    }
                    if (buffer.length() == 0) {
                        whileSpacePos = -1;
                    } else {
                        whileSpacePos = this.position;
                    }
                    this.position++;
                    continue;
                }
                if ((buffer.length() == 0) || ExpressionNode.isCongener(c, buffer.charAt(buffer.length() - 1))) {
                    this.position++;
                    buffer.append(c);
                } else {
                    break;
                }
                if (!ExpressionNode.needMoreOperator(c)) {
                    break;
                }
            }
        }
        if (buffer.length() == 0) {
            return null;
        }
        ExpressionNode node = new ExpressionNode(buffer.toString());
        if (node.getType() == ExpressionNodeType.UNKNOWN) {
            throw new BusinessException(ValidateConstans.ERR_CODE,String.format("表达式\"%s\"在位置%s上的字符\"%s\"非法!", this.getExpression(), this.getPosition() - node.getValue().length(), node.getValue()));
        }

        log.info("================读取表达式====================");
        log.info("=================node  {}",node);
        log.info("====================================");
        return node;
    }
}
