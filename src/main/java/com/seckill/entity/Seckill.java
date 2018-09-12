package com.seckill.entity;

import java.util.Date;

public class Seckill {

    private long seckillId;
    private String name;
    private int number;
    private Date startTime;
    private Date endTme;
    private Date createTime;

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTme() {
        return endTme;
    }

    public void setEndTme(Date endTme) {
        this.endTme = endTme;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Seckill{"+"seckillId="+seckillId+
                ",name="+name+",number="+number+
                ",startTime="+startTime+",endTime="+endTme+
                ",createTime="+createTime+"}";
    }
}
