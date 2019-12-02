/**
 * ==================================================
 * <p>
 * FileName: ValidateJavaInvokeResult
 *
 * @author : shihongwei
 * @create 2018/11/16
 * @since 1.0.0
 * 〈功能〉：java校验类标准结果输出
 * ==================================================
 */
package com.zds.boot.exp.sysexp.entity;

import com.zds.boot.common.facade.DtoBase;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ValidateJavaInvokeResult extends DtoBase {
    private static final long serialVersionUID = -408634633669284559L;

    /**结果*/
    private boolean isSuccess;
    /**结果详情*/
    private String desc;
}
