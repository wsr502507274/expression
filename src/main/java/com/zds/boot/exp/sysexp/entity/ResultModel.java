package com.zds.boot.exp.sysexp.entity;

import com.zds.boot.common.facade.DtoBase;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaoneng
 * @Title: ResultModel
 * @date 2018/11/19  16:51
 */
@Getter
@Setter
public class ResultModel extends DtoBase {

    private static final long serialVersionUID = -7895647836117079948L;

    /**返回警告容器*/
    private List waringList =new ArrayList<>();

    /**返回阻断容器*/
    private List errorList =new ArrayList<>();

    /**通用校验返回信息*/
    private String comResult;
}
