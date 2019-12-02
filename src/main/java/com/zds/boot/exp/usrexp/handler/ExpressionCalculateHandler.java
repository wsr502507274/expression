package com.zds.boot.exp.usrexp.handler;

import com.zds.boot.common.exception.BusinessException;
import com.zds.boot.exp.comm.constant.ValidateConstans;
import com.zds.boot.exp.comm.enums.ExpressionNodeType;
import com.zds.boot.exp.comm.util.DateUtils;
import com.zds.boot.exp.comm.util.EmptyChecker;
import com.zds.boot.exp.usrexp.entity.ExpressionNode;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.math.NumberUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * ==================================================
 * <p>
 * FileName: ExpressionCalculateHandler
 *
 * @author : shihongwei
 * @create 2019/4/10
 * @since 1.0.0
 * 〈功能〉：表达式计算执行器
 * ==================================================
 */

@Slf4j
public class ExpressionCalculateHandler {
    private static final String ERR1 = "表达式运算错误";

    private ExpressionCalculateHandler() {
    }

    /* *
     *========================================
     * @方法说明 ： 根据运算节点集合 执行节点运算
     * @author : shihongwei
     * @param nodes
     * @return      java.lang.Object
     * @exception
     * @创建时间：     2019/4/10 20:18
     *========================================
     */
    public static Object calcExpression(List<ExpressionNode> nodes) {
        if (EmptyChecker.isEmpty(nodes)) {
            return null;
        }

        if (nodes.size() > 1) {
            int index = 0;
            //储存数据
            ArrayList<Object> values = new ArrayList();
            while (index < nodes.size()) {
                //循环计算节点
                index = calculateAndSave(nodes, index,values);
            }
        }

        if (nodes.size() != 1) {
            throw new BusinessException("缺少操作符或操作数","999999");
        }
        switch (nodes.get(0).getType()) {
            case NUMERIC:
                return nodes.get(0).getNumeric();

            case STRING:
            case DATE:
                return nodes.get(0).getNumeric().toString().replace("\"", "");
        }
        throw new BusinessException(ValidateConstans.ERR_CODE,"缺少操作数");
    }


    private static int calculateAndSave(List<ExpressionNode> nodes, int index,ArrayList<Object> values) {

        ExpressionNode node = nodes.get(index);
        switch (node.getType()) {
            //如果是数字，则将值存入 values 中
            case NUMERIC:
            case STRING:
            case DATE:
                values.add(node.getNumeric());
                index++;
                break;
            default:
                //二元表达式，需要二个参数， 如果是Not的话，则只要一个参数
                int paramCount = 2;
                if (node.getType() == ExpressionNodeType.NOT) {
                    paramCount = 1;
                }
                //计算操作数的值
                if (values.size() < paramCount) {
                    log.debug("缺少操作数:{},,paramCount{}",node,paramCount);
                    throw new BusinessException(ValidateConstans.ERR_CODE,"缺少操作数");
                }
                //传入参数
                Object[] data = new Object[paramCount];
                for (int i = 0; i < paramCount; i++) {
                    data[i] = values.get(index - paramCount + i);
                }
                //将计算结果再存入当前节点
                node.setNumeric(calculate(node.getType(), data));
                node.setType(ExpressionNodeType.NUMERIC);
                //将操作数节点删除
                for (int i = 0; i < paramCount; i++) {
                    nodes.remove(index - i - 1);
                    values.remove(index - i - 1);
                }
                index -= paramCount;
                break;
        }
        return index;
    }

    /* *
     *========================================
     * @方法说明 ： 计算节点的值
     * @author : shihongwei
     * @param nodeType
     * @param data
     * @return      java.lang.Object
     * @exception
     * @创建时间：     2019/4/10 20:51
     *========================================
     */
    private static Object calculate(ExpressionNodeType nodeType, Object[] data) {
        Object obj1 = data[0];
        Object obj2 = data[1];
        String str1 = obj1 == null ? null : obj1.toString();
        String str2 = obj2 == null ? null : obj2.toString();

        boolean dateFlag = ExpressionNode.isDatetime(str1) || ExpressionNode.isDatetime(str2);
        boolean str1Contains = str1 != null && str1.contains("\"");
        boolean str2Contains = str2 != null && str2.contains("\"");
        boolean strFlag = str1Contains || str2Contains;

        if (str1 != null) {
            str1 = str1.replace("\"", "");
        }

        if (str2 != null) {
            str2 = str2.replace("\"", "");
        }
        log.debug("str1={},str2={}", str1, str2);

        Object temp = compare(nodeType, obj1, obj2, str1, str2, dateFlag, strFlag);

        if (temp != null) return temp;
        return 0;
    }


    private static Object compare(ExpressionNodeType nodeType, Object obj1, Object obj2, String str1, String str2, boolean dateFlag, boolean strFlag) {
        switch (nodeType) {
            case PLUS:
                return plusCalcu(obj1, obj2);
            case SUBTRACT:
                return convertToDecimal(obj1) - convertToDecimal(obj2);
            case MULTI_PLY:
                return convertToDecimal(obj1) * convertToDecimal(obj2);
            case DIVIDE:
                return divideCalcu(obj1, obj2);
            case POWER:
                return Math.pow(convertToDecimal(obj1), convertToDecimal(obj2));
            case MOD:
                return modCalcu(obj1, obj2);
            case BITWISE_AND:
                return convertToDecimal(obj1).intValue() & convertToDecimal(obj2).intValue();
            case BITWISE_OR:
                return convertToDecimal(obj1).intValue() | convertToDecimal(obj2).intValue();
            case AND:
                return convertToBool(obj1) && convertToBool(obj2);
            case OR:
                return convertToBool(obj1) || convertToBool(obj2);
            case NOT:
                return !convertToBool(obj1);
            case EQUAL:
                return equalCalcu(obj1, obj2, str1, str2, dateFlag);
            case EQ:
                return eqCalcu(str1, str2 );
            case NEQ:
                return neqCalcu(str1, str2 );
            case UNEQUAL:
                return unequalCalcu(obj1, obj2, str1, str2, dateFlag, strFlag);
            case GT:
                return gtCalcu(obj1, obj2, str1, str2, dateFlag);
            case LT:
                return ltcalcu(obj1, obj2, str1, str2, dateFlag);
            case GT_OR_EQUAL:
                return gtOrEqualCalcu(obj1, obj2, str1, str2, dateFlag);
            case LT_OR_EQUAL:
                return ltOrEqualCalcu(obj1, obj2, str1, str2, dateFlag);
            case L_SHIFT:
                return convertToDecimal(obj1).longValue() << convertToDecimal(obj2).longValue();
            case R_SHIFT:
                return convertToDecimal(obj1).longValue() >> convertToDecimal(obj2).longValue();
            case LIKE:
                return likeCalcu(str1, str2, strFlag);
            case NOT_LIKE:
                return notLikeCalcu(str1, str2, strFlag);
            case START_WITH:
                return startWithCalcu(str1, str2, strFlag);
            case END_WITH:
                return endWithCalcu(str1, str2, strFlag);

            default:
                return null;
        }

    }

    private static Object neqCalcu(String str1, String str2) {
        if (str1 == null && str2 == null) {
            return false;
        }
        if (str1 == null) {
            return true;
        }
        return (!str1.equals(str2));
    }

    private static Object eqCalcu( String str1, String str2) {
        if (str1 == null && str2 == null) {
            return true;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        return (str1.equals(str2));
    }

    private static Object endWithCalcu(String str1, String str2, boolean strFlag) {
        if (!strFlag) {
            return false;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.endsWith(str2);
    }

    private static Object startWithCalcu(String str1, String str2, boolean strFlag) {
        if (!strFlag) {
            return false;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.startsWith(str2);
    }

    private static Object notLikeCalcu(String str1, String str2, boolean strFlag) {
        if (!strFlag) {
            return false;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        return !str1.contains(str2);
    }

    private static Object likeCalcu(String str1, String str2, boolean strFlag) {
        if (!strFlag) {
            return false;
        }
        if (str1 == null || str2 == null) {
            return false;
        }
        return str1.contains(str2);
    }

    private static Object ltOrEqualCalcu(Object obj1, Object obj2, String str1, String str2, boolean dateFlag) {
        try {
            if (!dateFlag) {
                return (convertToDecimal(obj1) <= convertToDecimal(obj2));
            }
            return (DateUtils.parseGeneralDate(str1).getTime() <= DateUtils.parseGeneralDate(str2).getTime());
        } catch (Exception e) {
            throw new BusinessException(ValidateConstans.ERR_CODE,"进行 '<=' 操作时 只能判断 数字或日期类型");
        }
    }

    private static Object gtOrEqualCalcu(Object obj1, Object obj2, String str1, String str2, boolean dateFlag) {
        try {
            if (!dateFlag) {
                return (convertToDecimal(obj1) >= convertToDecimal(obj2));
            }
            return (DateUtils.parseGeneralDate(str1).getTime() >= DateUtils.parseGeneralDate(str2).getTime());
        } catch (Exception e) {
            throw new BusinessException(ValidateConstans.ERR_CODE,"进行 '>=' 操作时 只能判断 数字或日期类型");
        }
    }

    private static Object ltcalcu(Object obj1, Object obj2, String str1, String str2, boolean dateFlag) {
        try {
            if (!dateFlag) {
                return (convertToDecimal(obj1) < convertToDecimal(obj2));
            }
            return (DateUtils.parseGeneralDate(str1).getTime() < DateUtils.parseGeneralDate(str2).getTime());
        } catch (Exception e) {
            throw new BusinessException(ValidateConstans.ERR_CODE,"进行 '<' 操作时 只能判断 数字或日期类型");
        }
    }

    private static Object gtCalcu(Object obj1, Object obj2, String str1, String str2, boolean dateFlag) {
        try {
            if (!dateFlag) {
                return (convertToDecimal(obj1) > convertToDecimal(obj2));
            }
            return (DateUtils.parseGeneralDate(str1).getTime() > DateUtils.parseGeneralDate(str2).getTime());
        } catch (Exception e) {
            throw new BusinessException(ValidateConstans.ERR_CODE,"进行 '>' 操作时 只能判断 数字或日期类型");
        }
    }

    private static Object unequalCalcu(Object obj1, Object obj2, String str1, String str2, boolean dateFlag, boolean strFlag) {
        try {
            if (!dateFlag && strFlag) {
                    if (str1 == null && str2 == null) {
                        return true;
                    }
                    if (str1 == null || str2 == null) {
                        return false;
                    }
                return !(convertToDecimal(obj1).equals(convertToDecimal(obj2)));
            }
            return (DateUtils.parseGeneralDate(str1).getTime() != DateUtils.parseGeneralDate(str2).getTime());
        } catch (Exception e) {
            throw new BusinessException(ValidateConstans.ERR_CODE,"进行 '!=' 操作时 只能判断 数字或日期类型");
        }
    }

    private static Object equalCalcu(Object obj1, Object obj2, String str1, String str2, boolean dateFlag) {
        try {
            if (!dateFlag ) {
                if (str1 == null && str2 == null) {
                    return true;
                }
                if (str1 == null || str2 == null) {
                    return false;
                }
                return (convertToDecimal(obj1).equals(convertToDecimal(obj2)));
            }
            return (DateUtils.parseGeneralDate(str1).getTime() == DateUtils.parseGeneralDate(str2).getTime());
        } catch (Exception e) {
            e.printStackTrace();
            throw new BusinessException(ValidateConstans.ERR_CODE,"进行 '=' 操作时 只能判断 数字或日期类型");
        }
    }

    private static Object modCalcu(Object obj1, Object obj2) {
        double temp1 = convertToDecimal(obj2);
        if (temp1 == 0) {
            throw new BusinessException(ValidateConstans.ERR_CODE,ERR1.concat("取模时，摸与值不能为0"));
        }
        return convertToDecimal(obj1) % temp1;
    }

    private static Object divideCalcu(Object obj1, Object obj2) {
        double temp = convertToDecimal(obj2);
        if (temp == 0) {
            throw new BusinessException(ValidateConstans.ERR_CODE,ERR1.concat("除法运算时，除数不能为0"));
        }
        return convertToDecimal(obj1) / temp;
    }

    private static Object plusCalcu(Object obj1, Object obj2) {

        return (convertToDecimal(obj1) + convertToDecimal(obj2));
    }


    /* *
     *========================================
     * @方法说明 ： 转换为boolean
     * @author : shihongwei
     * @param value
     * @return      java.lang.Boolean
     * @exception
     * @创建时间：     2019/4/10 20:49
     *========================================
     */
    private static Boolean convertToBool(Object value) {
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else {
            return value != null;
        }
    }

    /* *
     *========================================
     * @方法说明 ： 转换为double
     * @author : shihongwei
     * @param value
     * @return      java.lang.Double
     * @exception
     * @创建时间：     2019/4/10 20:50
     *========================================
     */
    private static Double convertToDecimal(Object value) {
        if (EmptyChecker.isEmpty(value)) {
            return 0D;
        }
        value = value.toString().replaceAll("\"","");
        if (!NumberUtils.isNumber(value.toString())){
            throw  new BusinessException(ValidateConstans.ERR_CODE,String.format("表达式计算错误，使用算术运算时 存在非数字类型：\"%s\"",value));
        }
        if (value instanceof Boolean) {
            return ((Boolean) value ? 1d : 0d);
        } else {
            return Double.parseDouble(value.toString().replaceAll("\"", ""));
        }
    }
}
