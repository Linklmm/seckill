package com.seckill.exception;
/**
 * 秒杀关闭异常
 * @author linklmm
 * */
public class SeckillCloseException extends SeckillExcption{
    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
