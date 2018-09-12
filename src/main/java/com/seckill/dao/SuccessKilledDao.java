package com.seckill.dao;

import com.seckill.entity.SuccessKilled;

/**
 *
 * @author linklmm
 * */
public interface SuccessKilledDao {
    /**
     * 插入购买明细，可过滤重复
     * @param seckillId
     * @param userPhone
     * @return
     * 插入的行数
     *
     * */
    int insertSuccessKilled(long seckillId,long userPhone);

    /**
     * 更具id查询successKilled并携带秒杀产品对象
     * @param seckillId
     * @return
     * */
    SuccessKilled qureByIdWithSeckill(long seckillId);
}
