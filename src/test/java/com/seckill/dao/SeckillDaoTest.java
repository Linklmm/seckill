package com.seckill.dao;

import com.seckill.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;
/**
 *
 * 配置spring和Junit整合，Junit启动时加载springIOC容器
 *spring-test,junit
 * */
//Junit启动时加载springIOC容器
@RunWith(SpringJUnit4ClassRunner.class)
//告诉Junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    //注入Dao实现类依赖
    @Resource
    private SeckillDao seckillDao;
    @Test
    /**
     *  DEBUG c.s.dao.SeckillDao.reduceNumber - ==> Parameters: 1000(Long), 2018-09-12 16:35:29.713(Timestamp), 2018-09-12 16:35:29.713(Timestamp)
     * 16:35:30.565 [main] DEBUG c.s.dao.SeckillDao.reduceNumber - <==    Updates: 0
     * 16:35:30.566 [main] DEBUG org.mybatis.spring.SqlSessionUtils - Closing non transactional SqlSession [org.apache.ibatis.session.defaults.DefaultSqlSession@21ca139c]
     * 0
     * */
    public void reduceNumber() throws Exception{
        Date killTime=new Date();
        int updateCount=seckillDao.reduceNumber(1000L,killTime);
        System.out.println(updateCount);
    }

    @Test
    public void queryById() throws Exception{
        long id=1000;
        Seckill seckill=seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
    }

    @Test
    public void queryAll() throws Exception{
        List<Seckill> seckills=seckillDao.queryAll(0,100);
        //List<Seckill> queryAll(int offet,int limit);
        //java没有保存形参的记录：List<Seckill> queryAll(int offet,int limit);=queryAll(int arg0,int arg1)
        for (Seckill seckill: seckills) {
            System.out.println(seckill);
        }
    }
}