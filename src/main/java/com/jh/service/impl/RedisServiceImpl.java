package com.jh.service.impl;

import com.jh.service.RedisService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
public class RedisServiceImpl implements RedisService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public void setString(String key, String value) {
        this.setObject(key, value, null);
    }

    public void setString(String key, String value, Long time) {
        this.setObject(key, value, time);
    }

    public void setList(String key, List<String> value) {
        this.setObject(key, value, null);
    }

    public void setList(String key, List<String> value, Long time) {
        this.setObject(key, value, time);
    }

    public void setObject(String key, Object value, Long time) {

        if (StringUtils.isEmpty(key) || value == null) {
            return;
        }
        //判断类型 存放String类型
        if (value instanceof String) {
            String strValue = (String) value;
            stringRedisTemplate.opsForValue().set(key, strValue);
            if (time != null) {
                stringRedisTemplate.opsForValue().set(key, strValue, time, TimeUnit.SECONDS);
            }
            return;
        }
        //存放list
        if (value instanceof List) {
            List<String> listValue = (List<String>) value;
            for (String str : listValue) {
                stringRedisTemplate.opsForList().leftPush(key, str);
                if (time != null) {
                    stringRedisTemplate.opsForList().set(key, time, str);
                }
            }
            return;
        }
    }

    public String getStringKey(String key) {
        return stringRedisTemplate.opsForValue().get(key);
    }

}
