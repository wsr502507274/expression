package com.zds.boot.exp;

import com.zds.boot.common.boot.ApplicationContextHolder;
import com.zds.boot.exp.comm.constant.ValidateConstans;
import com.zds.boot.exp.comm.util.EmptyChecker;
import com.zds.boot.exp.sysexp.entity.ValidateDto;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * ==================================================
 * <p>
 * FileName: ValidateChainBuilder
 *
 * @author : shihongwei
 * @create 2019/4/28
 * @since 1.0.0
 * 〈功能〉：校验链构造器
 * ==================================================
 */
public class ValidateChainBuilder {

    private ValidateChainBuilder(){}
    private static final Map<String,String> VERSION_CACHE = new ConcurrentHashMap<>();
    private static final Map<String,ValidateChain> VALIDATE_CHAIN_CACHE = new ConcurrentHashMap<>();

    public static final String DEFAULT ="default";
    /* *
     *========================================
     * @方法说明 ： 构建校验链
     * @author : shihongwei
     * @param data  需要校验的实体（ps：必须单层级）
     * @param validateList  校验列表
     * @param pid  主标识
     * @param stage  副标识
     * @return      com.zds.credit.business.common.util.ValidateChain
     * @exception
     * @创建时间：     2019/4/28 10:46
     *========================================
    */
    public static ValidateChain buildChain( String pid, String stage){

        RedisTemplate redisTemplate = ApplicationContextHolder.get().getBean("redisTemplate",RedisTemplate.class);

        String key = getKey(pid, stage);

        String versionKey = key.concat(ValidateConstans.MIDDLE).concat(ValidateConstans.VALIDATE_REDIS_VERSION_KEY);

        String nativeVersion = VERSION_CACHE.get(versionKey);
        String redisVersion = (String)redisTemplate.opsForValue().get(versionKey);
        redisVersion = EmptyChecker.isEmpty(redisVersion) ? DEFAULT : redisVersion;
        if (EmptyChecker.notEmpty(nativeVersion)
                && nativeVersion.equals(redisVersion)
                && VALIDATE_CHAIN_CACHE.containsKey(key)){

            return VALIDATE_CHAIN_CACHE.get(key);
        }
        // 从redis 获取校验列表
        List<ValidateDto> list = (List<ValidateDto>) redisTemplate.opsForHash().get(key, ValidateConstans.VALIDATE_REDIS_HASH_KEY);
        if (EmptyChecker.isEmpty(list)){
            return null;
        }
        Deque<ValidateChain> chains = new ArrayDeque<>();
        Map<String,List<ValidateDto>> classMap = classifyList2Map(list);
        classMap.forEach((className,classList) ->{
            // 将validateList 转换为便于定位的map<field,list>
            Map<String,List<ValidateDto>> map= covertList2Map(classList);
            // 构建校验链
            map.forEach((k,v) ->{
                ValidateChain chain = new ValidateChain();
                chain.setField(k);
                chain.setValidates(v);
                chain.setClassName(className);
                chains.add(chain);
            });
        });

        ValidateChain chain = build(chains);
        if (!DEFAULT.equals(redisVersion)){
            VERSION_CACHE.put(versionKey,redisVersion);
        }
        if (chain!=null){
            VALIDATE_CHAIN_CACHE.put(key,chain);
        }
        return chain;
    }

    /* *
     *========================================
     * @方法说明 ： classname 和字段的映射
     * @author : shihongwei
     * @param list
     * @return      java.util.Map<java.lang.String,java.util.List<com.zds.boot.exp.sysexp.entity.ValidateDto>>
     * @exception
     * @创建时间：     2019/7/17 14:30
     *========================================
    */
    private static Map<String,List<ValidateDto>> classifyList2Map(List<ValidateDto> list) {

        Map<String,List<ValidateDto>> map = new HashMap<>();
        list.forEach(validate ->{
            String className = validate.getClassName() == null ? DEFAULT : validate.getClassName();
            if (map.containsKey(className)){
                map.get(className).add(validate);
                return;
            }

            List<ValidateDto> li = new ArrayList<>();
            li.add(validate);
            map.put(className,li);

        });


        return map;

    }

    private static String getKey(String pid, String stage) {
        return ValidateConstans.SYSTEM_NO.concat(ValidateConstans.MIDDLE).
                    concat(ValidateConstans.VALIDATE_TAG).concat(ValidateConstans.MIDDLE).
                    concat(pid).concat(ValidateConstans.MIDDLE).concat(stage);
    }

    private static ValidateChain build(Deque<ValidateChain> chains) {
        // first
        ValidateChain first = chains.poll();

        ValidateChain temp = chains.poll();
        if (temp==null){
            return first;
        }
        first.setNext(temp);

        ValidateChain poll;
        while ((poll = chains.poll())!=null){
            temp.setNext(poll);
            temp=poll;
        }

        return first;
    }


    /* *
     *========================================
     * @方法说明 ： 将validateList 转换为便于定位的map<field,list>
     * @author : shihongwei
     * @param validateList
     * @return      java.util.Map<java.lang.String,java.util.List<com.zds.credit.component.facade.module.dto.resp.ValidateDto>>
     * @exception   
     * @创建时间：     2019/4/28 11:22
     *========================================
    */
    private static Map<String,List<ValidateDto>> covertList2Map(List<ValidateDto> validateList) {
        Map<String,List<ValidateDto>> map = new HashMap<>();
        if (EmptyChecker.isEmpty(validateList)){
            return map;
        }
        validateList.forEach(validate ->{
            String fieldName = validate.getFieldName();
            if (!map.containsKey(fieldName)){
                List<ValidateDto> deque = new ArrayList<>();
                deque.add(validate);
                map.put(fieldName,deque);
                return ;
            }

            map.get(fieldName).add(validate);
        });


        return map;
    }
}
