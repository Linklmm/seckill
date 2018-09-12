package com.seckill.dao;

import com.seckill.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;
//Junit启动时加载springIOC容器
@RunWith(SpringJUnit4ClassRunner.class)
//告诉Junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})

public class SuccessKilledDaoTest {
    @Resource
    private SuccessKilledDao successKilledDao;

//第一次：insertCount=1；
    //第二次：insertCount=0;
    @Test
    public void insertSuccessKilled() {
        long id=1001L;
        long phone=15060335532L;
        int insertCount=successKilledDao.insertSuccessKilled(id,phone);
        System.out.println("insertCount="+insertCount);
    }

    @Test
    public void qureByIdWithSeckill() {
        long id=1000L;
        long phone=15060335532L;
        SuccessKilled successKilled=successKilledDao.qureByIdWithSeckill(id,phone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
    }
}