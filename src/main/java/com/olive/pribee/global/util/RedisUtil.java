package com.olive.pribee.global.util;

import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


@Component
@RequiredArgsConstructor
@Slf4j
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;

    // 레디스에 유효 시간에 맞춰 값 저장
    public void setOpsForValue(String key, String value, int expire_h) {
        ValueOperations<String, Object> stringValueOperations = redisTemplate.opsForValue();
        stringValueOperations.set(key, value);
        redisTemplate.expire(key, expire_h, TimeUnit.HOURS);

        log.info("[Redis] Data saved successfully. -- " + key);
    }

    // 레디스에 저장된 값 가져오기
    public String getOpsForValue(String key) {
        return (String) redisTemplate.opsForValue().get(key);
    }

    // 레디스에 저장된 값 삭제
    public void delete(String key) {
        redisTemplate.delete(key);

        log.info("[Redis] Data deleted successfully. -- " + key);
    }
}
