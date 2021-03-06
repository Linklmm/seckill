package com.seckill.service;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.entity.Seckill;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillExcption;

import java.util.List;

/**
 *方法定义粒度，参数，返回类型(return 类型/异常)
 * @author myfloweryourgrass
 * */
public interface SeckillService {
    /**
     * 查询所有的秒杀记录
     * @return
     *
     * */
    List<Seckill> getSeckillList();

    /**
     * 查询单个秒杀记录
     * @param seckillId
     * @return
     * */
    Seckill getById(long seckillId);

    /**
     *
     * 秒杀开启是输出秒杀接口地址，
     * 否则输出系统时间和秒杀时间
     * @param seckillId
     * */
    Exposer exportSeckillUrl(long seckillId);
    /**
     * 执行秒杀操作
     * @param seckillId
     * @param md5
     * @param userPhone
     * */

    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillExcption,RepeatKillException,SeckillExcption;

    /**
     * 执行秒杀操作by 存储过程
     * @param seckillId
     * @param md5
     * @param userPhone
     * */
    SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5)
            throws SeckillExcption,RepeatKillException,SeckillExcption;
}
