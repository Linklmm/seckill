package com.seckill.dao;

import com.seckill.entity.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface SeckillDao {
    /**
     *减库存
     * @param seckillId
     * @param killTime
     * @return 如果影响行数>1,表示更新记录的行数
     */
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);

    /**
     *根据id查询秒杀对象
     * @param seckillId
     * @return
     * */
    Seckill queryById(long seckillId);
/**
 * 根据偏移量查询秒杀商品列表
 * @param limit 偏移量
 * @param offet 记录条数
 * @return
 * */
    List<Seckill> queryAll(@Param("offset") int offet, @Param("limit") int limit);

}
