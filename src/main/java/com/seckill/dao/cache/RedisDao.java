package com.seckill.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.seckill.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

//region Description
public class RedisDao {
    private final Logger logger=LoggerFactory.getLogger(this.getClass());

    private final JedisPool jedisPool;

    private RuntimeSchema<Seckill> sechema=RuntimeSchema.createFrom(Seckill.class);

    public RedisDao(String ip,int port){
        jedisPool=new JedisPool(ip,port);
    }
    public Seckill getSeckill(long seckillId){
        //redis操作逻辑
        try {
            Jedis jedis=jedisPool.getResource();
            try {
                String key="seckill:"+seckillId;
                //并没有实现内部序列化操作
                //get->byte[]->反序列化->object(seckill)
                //才用自定义序列化
                byte[] bytes=jedis.get(key.getBytes());
                if (bytes!=null){
                    //空对象
                    Seckill seckill=sechema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes,seckill,sechema);
                    //seckill被反序列化
                    return seckill;
                }
            }finally {
                jedis.close();
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
        }
        return null;
    }
    public String putSeckill(Seckill seckill){
        //set object(seckill)->序列化->byte[]->
        try {
            Jedis jedis=jedisPool.getResource();
            try {
                String key="seckill:"+seckill.getSeckillId();
                byte[] bytes=ProtostuffIOUtil.toByteArray(seckill,sechema,LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                //超时缓存
                int timeout=60*60;//1小时
                String result=jedis.setex(key.getBytes(),timeout,bytes);
                return result;
            }finally {
                jedis.close();
            }
        }catch (Exception e){
            logger.info(e.getMessage(),e);
        }
        return null;
    }
}
//endregion
