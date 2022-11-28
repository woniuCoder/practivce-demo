package com.imooc.socialecom.common;

public interface MqCommon<T> {

    void send(String key, String value);

    void send(String key, T t);

    String sendAsync(String key, T t, boolean isAsync);
}
