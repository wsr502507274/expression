package com.zds.boot.exp;


import com.zds.boot.exp.usrexp.factory.ExpAnalysisEngine;

import java.util.HashMap;
import java.util.Map;

/**
 * ==================================================
 * <p>
 * FileName: TestExp
 *
 * @author : shihongwei
 * @create 2019/4/18
 * @since 1.0.0
 * 〈功能〉：TODO
 * ==================================================
 */
public class TestExp {

    public static void main(String[] args) {
        String xxx = "1=1 && "+"aaa"+"="+"a1aa";
        String vvv=new StringBuilder(xxx).append("\"").insert(7,"\"").insert(11,"\"").insert(13,"\"").toString();
        String exp1 = "2=2 && (3+2-5/5=4 && ((1+1)/2-(1*(2+1)))<1) && ((1+1>2 || 3-2=1) || 3+(1+1)=1) ";
        // true      true             true                   false    true      true

        Map<String,Object> map = new HashMap<>();
        map.put("aaa",111.0001);
        map.put("bbb","111.000");
        System.out.println(map);
        System.out.println(ExpAnalysisEngine.calculate("\"${aaa}\"≠\"${bbb}\"",map));



    }
}
