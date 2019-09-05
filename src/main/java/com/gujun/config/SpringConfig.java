package com.gujun.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPoolConfig;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@ImportResource({"classpath:spring-config.xml"})  //引入xml配置文件
@ComponentScan(basePackages={"com.gujun"})
@EnableCaching
public class SpringConfig {

    @Bean
    public RedisConnectionFactory redisConnectionFactory(@Autowired JedisPoolConfig jedisPoolConfig,
                                                         @Value("${redis.host}")  String host, @Value("${redis.port}") int port, @Value("${redis.database}") int database
    ){
        RedisStandaloneConfiguration redisStandaloneConfiguration=new RedisStandaloneConfiguration();
        ////设置redis服务器的host,ip地址,数据库，此处没设置密码
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
//        redisStandaloneConfiguration.setPassword("gujun");
        redisStandaloneConfiguration.setDatabase(database);
        JedisClientConfiguration.JedisPoolingClientConfigurationBuilder jpcf = (JedisClientConfiguration.JedisPoolingClientConfigurationBuilder) JedisClientConfiguration.builder();
        jpcf.poolConfig(jedisPoolConfig);
        JedisClientConfiguration jedisClientConfiguration = jpcf.build();
        return new JedisConnectionFactory(redisStandaloneConfiguration, jedisClientConfiguration);
    }


    @Bean
    public StringRedisSerializer stringRedisSerializer(){
        return new StringRedisSerializer();
    }

//    @Bean
//    public JdkSerializationRedisSerializer jdkSerializationRedisSerializer(){
//        return new JdkSerializationRedisSerializer();
//    }

    @Bean
    public GenericJackson2JsonRedisSerializer genericJackson2JsonRedisSerializer(){
        return new GenericJackson2JsonRedisSerializer();
    }

    @Bean("redisTemplate")
    public RedisTemplate<String, Object> redisTemplate(@Autowired RedisConnectionFactory redisConnectionFactory){
        RedisTemplate<String,Object> redisTemplate=new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(stringRedisSerializer());
//        redisTemplate.setValueSerializer(genericJackson2JsonRedisSerializer()); //用这个,值不会乱码,但是数值型字符串增减操作会报错；
        redisTemplate.setHashKeySerializer(genericJackson2JsonRedisSerializer());
        redisTemplate.setHashValueSerializer(stringRedisSerializer());
        redisTemplate.setValueSerializer(stringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean("redisCacheManage")
    public CacheManager cacheManager(@Autowired RedisConnectionFactory redisConnectionFactory){
        Map<String, RedisCacheConfiguration> configurationMap = new HashMap<>();
        configurationMap.put("cacheNoLimit",RedisCacheConfiguration.defaultCacheConfig());
        configurationMap.put("cacheLimit",RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(30))); //设置缓存期限为30分钟
        return RedisCacheManager.builder(RedisCacheWriter.nonLockingRedisCacheWriter(redisConnectionFactory))
                .initialCacheNames(configurationMap.keySet())
                .withInitialCacheConfigurations(configurationMap)
                .build();
    }
}
