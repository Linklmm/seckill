package com.seckill.service.Impl;

import com.seckill.dto.Exposer;
import com.seckill.dto.SeckillExecution;
import com.seckill.entity.Seckill;
import com.seckill.exception.RepeatKillException;
import com.seckill.exception.SeckillCloseException;
import com.seckill.exception.SeckillExcption;
import com.seckill.service.SeckillService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

import static org.junit.Assert.*;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring/spring-dao.xml",
        "classpath:spring/spring-service.xml"})
public class SeckillServiceImplTest {

    private final Logger logger=LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;
    @Test
    public void getSeckillList()throws Exception {
        List<Seckill> list=seckillService.getSeckillList();
        logger.info("list={}",list);
    }

    @Test
    public void getById() throws Exception{
        long id=1000;
        Seckill seckill=seckillService.getById(id);
        logger.info("seckill={}",seckill);
    }

    //Exposer=Exposer{ exposed=true,md5=9c85c9983d54a7c864ff30282e36c03b,seckillId=1000,now=0,start=0,end=0}
    @Test
    public void exportSeckillUrl() throws Exception{
        long id=1000;
        Exposer exposer=seckillService.exportSeckillUrl(id);
        logger.info("Exposer={}",exposer);
    }
    //测试代码完整逻辑，注意可重复执行
    //exposer=Exposer{ exposed=false,md5=null,seckillId=1001,now=1536826329271,start=1536710400000,end=1451692800000}
    @Test
    public void testSeckillLogic()throws Exception{
        long id=1001;
        Exposer exposer=seckillService.exportSeckillUrl(id);
        if (exposer.isExposed()){
            logger.info("Exposer={}",exposer);
            //long id=1000;
            long phone=15060335533l;
            String md5=exposer.getMd5();
            try {
                SeckillExecution execution =seckillService.executeSeckill(id,phone,md5);
                logger.info("result={}",execution);
            } catch (RepeatKillException e) {
                logger.info(e.getMessage());
            }catch (SeckillCloseException e) {
                logger.info(e.getMessage());
            }
        }else {
            //秒杀未开启
            logger.warn("exposer={}",exposer);
        }
    }

    @Test
    public void executeSeckill() throws Exception {
        long id=1000;
        long phone=15060335533l;
        String md5="9c85c9983d54a7c864ff30282e36c03b";
        try {
            SeckillExecution execution =seckillService.executeSeckill(id,phone,md5);
            logger.info("result={}",execution);
        } catch (RepeatKillException e) {
            logger.info(e.getMessage());
        }catch (SeckillCloseException e) {
            logger.info(e.getMessage());
        }
    }
}