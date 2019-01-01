package com.minhao.nov.common;

import com.minhao.nov.util.PropertiesUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisPool {

    private static JedisPool pool;
    private static String host=PropertiesUtil.getProperty("redis.host");
    private static int port= Integer.parseInt(PropertiesUtil.getProperty("redis.port"));







    private static Integer maxTotal= Integer.parseInt(PropertiesUtil.getProperty("spring.redis.jedis.pool.max-active","20"));
    private static Integer maxIdle=Integer.parseInt(PropertiesUtil.getProperty("spring.redis.jedis.pool.max-idle","10"));
    private static Integer minIdle=Integer.parseInt(PropertiesUtil.getProperty("spring.redis.jedis.pool.min-idle","2"));
    private static Boolean testBorrow=Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.borrow"));
    private static Boolean testReturn=Boolean.parseBoolean(PropertiesUtil.getProperty("redis.test.return"));



    private static void init(){
        JedisPoolConfig config=new JedisPoolConfig();

        config.setMaxTotal(maxTotal);
        config.setMaxIdle(maxIdle);
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(testBorrow);
        config.setTestOnReturn(testReturn);


        pool=new JedisPool(config,host,port,1000*2);


    }

    static {
        init();
    }


    public static Jedis getJedis(){
        return pool.getResource();
    }


    public static void returnRecource(Jedis jedis){
        if (jedis!=null){
            pool.returnResource(jedis);
        }
    }


    public static void returnBrokenResource(Jedis jedis){
        if (jedis!=null){
            pool.returnBrokenResource(jedis);
        }
    }


    public static void main(String[] args) {
        Jedis jedis=pool.getResource();
        jedis.set("minhao","hahaha");
        returnRecource(jedis);
        System.out.println("done");
    }
















}
