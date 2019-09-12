package uk.gov.ons.census.fwmt.outcomeservice.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import uk.gov.ons.census.fwmt.common.error.GatewayException;
import uk.gov.ons.census.fwmt.outcomeservice.service.JobCacheManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Service
public class JobCacheManagerImpl implements JobCacheManager {

  @Autowired
  private JedisPool jedisPool;

  @Autowired
  private RedisTemplate redisTemplate;

  private Date outcomeDate;
  private Date outcomeDateToCheck;

  @Override
  public String cacheCCSOutcome(String caseId, String outcome) {
    Jedis jedis = jedisPool.getResource();
    jedis.select(1);
    jedis.hset(String.valueOf(caseId), "DateTime", String.valueOf(LocalDateTime.now()));
    jedis.hset(String.valueOf(caseId), "Outcome", String.valueOf(outcome));
    log.info("Placed the following in cache: " +  outcome);
    return outcome;
  }

  public void resetTMOutcomes(String dateTime) throws GatewayException {
    Jedis jedis = jedisPool.getResource();
    jedis.select(1);

    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
    String checkKey;
    String checkDate;

    Set<String> redisKeys = redisTemplate.keys("*");
    Iterator<String> keyIterator = redisKeys.iterator();

    while (keyIterator.hasNext()) {
      checkKey = keyIterator.next();
      checkDate = jedis.hget(checkKey, "DateTime");

      try {
        outcomeDate = dateFormat.parse(dateTime);
        outcomeDateToCheck = dateFormat.parse(checkDate);
      } catch (ParseException e) {
        throw new GatewayException(GatewayException.Fault.SYSTEM_ERROR, "Unable to convert date");
      }

      if (outcomeDateToCheck.after(outcomeDate)) {
        jedis.del(checkKey);
        log.info("Removed key: " +  checkKey);
      }
    }

    log.info("Finished removing keys after date time: " +  outcomeDate);
  }
}
