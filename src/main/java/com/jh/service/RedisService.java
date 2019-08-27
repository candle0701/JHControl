package com.jh.service;

import java.util.List;

public interface RedisService {

    void setString(String key, String value);

    void setString(String key, String value, Long time);

    void setList(String key, List<String> value);

    void setList(String key, List<String> value, Long time);
}
