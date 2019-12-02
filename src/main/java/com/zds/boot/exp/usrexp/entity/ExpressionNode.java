package com.zds.boot.exp.usrexp.entity;

import com.zds.boot.common.facade.DtoBase;
import com.zds.boot.exp.comm.enums.ExpressionNodeType;
import com.zds.boot.exp.comm.util.DateUtils;
import com.zds.boot.exp.comm.util.EmptyChecker;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;

/**
 * ==================================================
 * <p>
 * FileName: ExpressionNode
 *
 * @author : shihongwei
 * @create 2019/4/10
 * @since 1.0.0
 * 〈功能〉：计算节点模型
 * ==================================================
 */
@Slf4j
@Setter
@Getter
public class ExpressionNode extends DtoBase {
    private static final long serialVersionUID = -7352014754726699986L;
    private String value;

    private ExpressionNodeType type;

    private int pri;

    private ExpressionNode unitaryNode;

    private Object numeric;

    /**
     * @param value 操作数或运算符
     */
    public ExpressionNode(String value) {
        this.value = value;
        this.type = parseNodeType(value);
        this.pri = getNodeTypePRI(this.type);
        this.numeric = null;
    }
    public ExpressionNode() {
    }


    public Object getNumeric() {
        if (this.numeric == null) {

            if ((this.type == ExpressionNodeType.STRING) || (this.type == ExpressionNodeType.DATE)) {
                return this.value;
            }

            if (this.type != ExpressionNodeType.NUMERIC) {
                return 0;
            }
            Double num = new Double(this.value);
            if (this.unitaryNode != null && this.unitaryNode.type == ExpressionNodeType.SUBTRACT) {
                num = 0 - num;
            }
            this.numeric = num;
        }
        return numeric;
    }


    public void setNumeric(Object numeric) {
        this.numeric = numeric;
        this.value = this.numeric.toString();
    }

    /**
     * 设置或返回与当前节点相关联的一元操作符节点
     *
     * @param unitaryNode
     */
    public void setUnitaryNode(ExpressionNode unitaryNode) {
        this.unitaryNode = unitaryNode;
    }

    /* *
     *========================================
     * @方法说明 ： 解析节点类型
     * @author : shihongwei
     * @param value
     * @return      com.zds.credit.common.enums.ExpressionNodeType
     * @exception
     * @创建时间：     2019/4/10 20:55
     *========================================
    */
    private static ExpressionNodeType parseNodeType(String value) {
        log.debug("value  ========{}",value);
        if (StringUtils.isEmpty(value)) {
            return ExpressionNodeType.UNKNOWN;
        }
        switch (value) {
            case "+":
                return ExpressionNodeType.PLUS;
            case "-":
                return ExpressionNodeType.SUBTRACT;
            case "*":
                return ExpressionNodeType.MULTI_PLY;
            case "/":
                return ExpressionNodeType.DIVIDE;
            case "%":
                return ExpressionNodeType.MOD;
            case "^":
                return ExpressionNodeType.POWER;
            case "(":
                return ExpressionNodeType.L_PARENTHESES;
            case ")":
                return ExpressionNodeType.R_PARENTHESES;
            case "&":
                return ExpressionNodeType.BITWISE_AND;
            case "|":
                return ExpressionNodeType.BITWISE_OR;
            case "&&":
            case "<并且>":
            case "并且":
                return ExpressionNodeType.AND;
            case "||":
            case "<或者>":
            case "或者":
                return ExpressionNodeType.OR;
            case "!":
                return ExpressionNodeType.NOT;
            case "==":
                return ExpressionNodeType.EQ;
            case "!=":
                return ExpressionNodeType.NEQ;
            case "=":
                return ExpressionNodeType.EQUAL;
            case "≠":
                return ExpressionNodeType.UNEQUAL;
            case ">":
                return ExpressionNodeType.GT;
            case "<":
                return ExpressionNodeType.LT;
            case ">=":
            case "≥":
                return ExpressionNodeType.GT_OR_EQUAL;
            case "<=":
            case "≤":
                return ExpressionNodeType.LT_OR_EQUAL;
            case "<<":
                return ExpressionNodeType.L_SHIFT;
            case ">>":
                return ExpressionNodeType.R_SHIFT;
            case "@":
            case "<包含>":
            case "包含":
            case "⊂":
                return ExpressionNodeType.LIKE;
            case "!@":
            case "<不包含>":
            case "不包含":
            case "⊄":
                return ExpressionNodeType.NOT_LIKE;
            case "!!$":
                return ExpressionNodeType.START_WITH;
            case "!!@":
                return ExpressionNodeType.END_WITH;

                default:
                    if (isNumerics(value)) {
                        return ExpressionNodeType.NUMERIC;
                    }
                    if (isDatetime(value)) {
                        return ExpressionNodeType.DATE;
                    }
                    if (value.contains("\"")) {
                        return ExpressionNodeType.STRING;
                    }
                    return ExpressionNodeType.UNKNOWN;
        }
    }

    /* *
     *========================================
     * @方法说明 ： 获取各节点类型的优先级
     * @author : shihongwei
     * @param nodeType
     * @return      int
     * @exception
     * @创建时间：     2019/4/10 16:57
     *========================================
    */
    private static int getNodeTypePRI(ExpressionNodeType nodeType) {
        switch (nodeType) {
            case L_PARENTHESES:
            case R_PARENTHESES:
                return 9;
            //逻辑非是一元操作符,所以其优先级较高
            case NOT:
                return 8;
            case MOD:
                return 7;
            case MULTI_PLY:
            case DIVIDE:
            case POWER:
                return 6;
            case PLUS:
            case SUBTRACT:
                return 5;
            case L_SHIFT:
            case R_SHIFT:
                return 4;
            case BITWISE_AND:
            case BITWISE_OR:
                return 3;
            case EQ:
            case NEQ:
            case EQUAL:
            case UNEQUAL:
            case GT:
            case LT:
            case GT_OR_EQUAL:
            case LT_OR_EQUAL:
            case LIKE:
            case NOT_LIKE:
            case START_WITH:
            case END_WITH:
            case CONTAINS:
            case NOT_CONTAINS:
                return 2;
            case AND:
            case OR:
                return 1;
            default:
                return 0;
        }

    }

    /* *
     *========================================
     * @方法说明 ： 判断是否为数值
     * @author : shihongwei
     * @param op
     * @return      boolean
     * @exception
     * @创建时间：     2019/4/10 17:00
     *========================================
    */
    private static boolean isNumerics(String op) {
        return NumberUtils.isNumber(op);
    }

    /* *
     *========================================
     * @方法说明 ： 判断是否为日期
     * @author : shihongwei
     * @param op
     * @return      boolean
     * @exception
     * @创建时间：     2019/4/10 17:02
     *========================================
    */
    public static boolean isDatetime(String op) {
        if (EmptyChecker.isEmpty(op)){
            return false;
        }
        op = op.replace("\"", "").trim();
        log.debug("判断是否为日期  {}",DateUtils.isGeneralDate(op));
        return DateUtils.isGeneralDate(op);
    }


    /* *
     *========================================
     * @方法说明 ： 判断某个字符后是否需要更多的操作符
     * @author : shihongwei
     * @param c
     * @return      boolean
     * @exception
     * @创建时间：     2019/4/10 20:57
     *========================================
    */
    public static boolean needMoreOperator(char c) {
        switch (c) {
            case '&':
            case '|':
            case '=':
            case '!':
            case '>':
            case '<':
            case '.':   //小数点
                return true;
                default:
                    //数字则需要更多
                    return Character.isDigit(c);
        }
    }

    /* *
     *========================================
     * @方法说明 ： 判断两个字符是否是同一类  特指类型
     * @author : shihongwei
     * @param c1
     * @param c2
     * @return      boolean
     * @exception
     * @创建时间：     2019/4/10 20:57
     *========================================
    */
    public static boolean isCongener(char c1, char c2) {
        if ((c1 == '(') || (c2 == '(')) {
            return false;
        }
        if ((c1 == ')') || (c2 == ')')) {
            return false;
        }
        if ((c1 == '"') || (c2 == '"')) {
            return false;
        }
        if (Character.isDigit(c1) || (c1 == '.')) {
            //c1为数字,则c2也为数字
            return (Character.isDigit(c2) || (c2 == '.'));
        }
        return !Character.isDigit(c2) && (c2 != '.');
    }

    /* *
     *========================================
     * @方法说明 ： 判断某个字符是否是空白字符
     * @author : shihongwei
     * @param c
     * @return      boolean
     * @exception
     * @创建时间：     2019/4/10 20:58
     *========================================
    */
    public static boolean isWhileSpace(char c) {
        return c == ' ' || c == '\t';
    }

    /* *
     *========================================
     * @方法说明 ： 判断是否是一元操作符节点
     * @author : shihongwei
     * @param nodeType
     * @return      boolean
     * @exception
     * @创建时间：     2019/4/10 20:58
     *========================================
    */
    public static boolean isUnitaryNode(ExpressionNodeType nodeType) {
        return (nodeType == ExpressionNodeType.PLUS || nodeType == ExpressionNodeType.SUBTRACT);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public ExpressionNodeType getType() {
        return type;
    }

    public void setType(ExpressionNodeType type) {
        this.type = type;
    }

    public int getPri() {
        return pri;
    }

    public void setPri(int pri) {
        this.pri = pri;
    }

    public ExpressionNode getUnitaryNode() {
        return unitaryNode;
    }
}
