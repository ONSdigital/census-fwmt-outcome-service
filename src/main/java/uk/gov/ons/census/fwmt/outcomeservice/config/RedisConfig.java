package uk.gov.ons.census.fwmt.outcomeservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import redis.clients.jedis.JedisPool;

@Configuration
public class RedisConfig {

  private JedisPool pool = null;
  private String redisHostname;
  private int redisPort;
  private int redisDB;

  public RedisConfig(@Value("${redis.host}") String redisHostname,
                     @Value("${redis.port}") int redisPort,
                     @Value("${redis.database}") int redisDB) {
    this.redisHostname = redisHostname;
    this.redisPort = redisPort;
    this.redisDB = redisDB;
  }

  @Bean
  protected JedisConnectionFactory jedisConnectionFactory() {
    RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHostname, redisPort);
    configuration.setDatabase(redisDB);
    JedisClientConfiguration jedisClientConfiguration = JedisClientConfiguration.builder().usePooling().build();
    JedisConnectionFactory factory = new JedisConnectionFactory(configuration, jedisClientConfiguration);
    factory.afterPropertiesSet();
    return factory;
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate(JedisConnectionFactory jedisConnectionFactory) {
    final RedisTemplate<String, Object> redisTemplate = new RedisTemplate<String, Object>();
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashKeySerializer(new GenericToStringSerializer<Object>(Object.class));
    redisTemplate.setHashValueSerializer(new JdkSerializationRedisSerializer());
    redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
    redisTemplate.setConnectionFactory(jedisConnectionFactory);
    return redisTemplate;
  }

  @Bean
  public JedisPool jedisPool() {
    pool = new JedisPool(redisHostname, redisPort);
    return pool;
  }

}

