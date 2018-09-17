package com.seckill.service.Impl;

import com.seckill.dao.SeckillDao;
import com.seckill.dao.SuccessKilledDao;
import com.seckill.dao.cache.RedisDao;
import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.entity.Seckill;
import com.seckill.entity.SuccessKilled;
import com.seckill.enums.SeckillStatEnum;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.exception.SeckillExcption;
import com.seckill.service.SeckillService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SeckillServiceImpl implements SeckillService {
    private Logger logger=LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;
    @Autowired
    private RedisDao redisDao;

    //用于混淆md5
    private final String slat="kkjkhihwiioiNBMMHi+_)99*&^%$ou!`~!@##$%^&*(ET#TEDgs$%^*(l..jioo";

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,4);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }


    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        //缓存优化
        //1:访问redis
        Seckill seckill=redisDao.getSeckill(seckillId);
        if (seckill==null){
            //2.访问数据库
            seckill=seckillDao.queryById(seckillId);
            if (seckill==null){
                return new Exposer(false,seckillId);
            }else {
                //3：放入Redis
                redisDao.putSeckill(seckill);
            }
        }
        /**
         * get from cache
         * if null
         *
         * */
        //logger.info("seckill={}",seckill);
        Date startTime=seckill.getStartTime();
        Date endTime=seckill.getEndTime();
        Date nowTime=new Date();
        if (nowTime.getTime()<startTime.getTime()||nowTime.getTime()>endTime.getTime()){
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        //转化特定字符串的过程，不可逆
        String md5=getMD5(seckillId);//TODO
        return new Exposer(true,md5,seckillId);
    }

    /**
     *
     * 生成md5
     * */
    private String getMD5(long seckillId){
        String base=seckillId+"/"+slat;
        String md5=DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Override
    @Transactional
    /**
     * 使用注解控制事务方法的优点
     *执行秒杀
     * */
    //秒杀是否成功，成功:减库存，增加明细；失败:抛出异常，事务回滚
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillExcption, RepeatKillException, SeckillExcption {
        if (md5==null||!md5.equals(getMD5(seckillId))){
            throw new  SeckillExcption("seckill Data Rewrite");
        }
        //执行秒杀逻辑：减库存+记录购买行为
        Date nowTime=new Date();
        //减库存
        try {
            //记录购买行为
            //唯一：seckillId,userPhone
            int insertCount=successKilledDao.insertSuccessKilled(seckillId,userPhone);
            if (insertCount<=0){
                throw new RepeatKillException("seckill repeated");
            }else {
                //减库存,热点商品竞争
                int updateCount=seckillDao.reduceNumber(seckillId,nowTime);
                if (updateCount<=0){
                    //没有更新到记录，秒杀结束，rollback
                    throw new SeckillCloseException("seckill is closed!");
                }else {
                    //秒杀成功commit
                    SuccessKilled successKilled=successKilledDao.qureByIdWithSeckill(seckillId,userPhone);
                    return new SeckillExecution(seckillId,SeckillStatEnum.SUCCESS,successKilled);
                }
            }

        } catch (SeckillCloseException e1){
            throw  e1;
        }catch (RepeatKillException e2){
            throw e2;
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            //所有编译期异常转化为运行期异常
            throw new SeckillExcption("seckill inner error:"+e.getMessage());
        }
    }

    /**
     *
     * 存储过程执行秒杀
     *
     * */
    @Override
    public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
        if (md5==null||!md5.equals(getMD5(seckillId))){
            return new SeckillExecution(seckillId,SeckillStatEnum.DATA_REWRITE);
        }
        Date killTime=new Date();
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("seckillId",seckillId);
        map.put("phone",userPhone);
        map.put("killTime",killTime);
        map.put("result",null);
        //执行存储过程，result被赋值
        try {
            seckillDao.killByProcedure(map);
            //获取result
            int result=MapUtils.getInteger(map,"result",-2);
            if (result==1){
                SuccessKilled sk=successKilledDao.qureByIdWithSeckill(seckillId,userPhone);
                return new SeckillExecution(seckillId,SeckillStatEnum.SUCCESS,sk);
            }else {
                return new SeckillExecution(seckillId,SeckillStatEnum.stateOf(result));
            }
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            return new SeckillExecution(seckillId,SeckillStatEnum.INNER_ERROR);
        }
    }
}
