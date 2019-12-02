/**
 * ==================================================
 * <p>
 * FileName: ValidateEngineGlobalVar
 *
 * @author : shihongwei
 * @create 2018/11/15
 * @since 1.0.0
 * 〈功能〉：校验引擎全局参数
 * ==================================================
 */
package com.zds.boot.exp.sysexp.entity;

import com.zds.boot.common.facade.DtoBase;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Setter
@Getter
public class ValidateEngineGlobalVar extends DtoBase {
    private static final long serialVersionUID = -6690303284365653926L;


    /**警告信息集合*/
    private Map<String,List<String>> warn =new HashMap<>();

    /**阻断信息集合*/
    private Map<String,List<String>> interdiction=new HashMap<>();

    /**全局临时缓冲集合*/
    private Map<String,String> temp=new HashMap<>();

}
