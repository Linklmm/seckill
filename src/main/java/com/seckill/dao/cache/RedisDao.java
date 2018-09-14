package com.seckill.dao.cache;

import com.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

public class RedisDao {
    private final Logger logger=LoggerFactory.getLogger(this.getClass());

    private JedisPool jedisPool;

    public RedisDao(String ip,int port){
        jedisPool=new JedisPool(ip,port);
    }
    public Seckill getSeckill(long seckillId){
        //redis操作逻辑
        try {
            Jedis jedis=jedisPool.getResource();
            try {
                String key="seckill"+seckillId;
                //并没有实现内部序列化操作
                //get->byte[]->反序列化->object(seckill)
                //才用自定义序列化

            }finally {
                jedis.close();
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }
    public String putSeckill(Seckill seckill){
        return null;
    }
}
