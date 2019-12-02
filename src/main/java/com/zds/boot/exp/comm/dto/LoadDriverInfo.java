package com.zds.boot.exp.comm.dto;

import com.zds.boot.common.facade.DtoBase;
import com.zds.boot.exp.usrexp.service.ExpEngineLoadDriver;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * ==================================================
 * <p>
 * FileName: LoadDriverInfo
 *
 * @author : shihongwei
 * @create 2019/4/11
 * @since 1.0.0
 * 〈功能〉：TODO
 * ==================================================
 */
@Getter
@Setter
public class LoadDriverInfo extends DtoBase {
    private static final long serialVersionUID = 2746650782911595850L;

    private ExpEngineLoadDriver expEngineLoadDriver;
    private List<String> params;
}
