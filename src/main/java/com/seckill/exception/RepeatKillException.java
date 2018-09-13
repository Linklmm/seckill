package com.seckill.exception;
/**
 * 重发秒杀异常(运行期异常)
 *
 *
 * */
public class RepeatKillException extends SeckillExcption{
    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}
