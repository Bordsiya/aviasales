package com.example.aviasales.config;

import com.example.aviasales.service.messaging.FlightUpdateSubscriber;
import com.example.aviasales.service.messaging.passengers.PassengerUpdateSubscriber;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;
    @Value("${spring.data.redis.port}")
    private int port;

    @Value("${spring.data.redis.topics.flights}")
    private String flightsTopic;

    @Value("${spring.data.redis.topics.passengers}")
    private String passengersTopic;

    /**
     * Connection to redis
     */
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(host, port);
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    /**
     * Messaging
     */
    @Bean
    MessageListenerAdapter flightsListener(FlightUpdateSubscriber flightUpdateSubscriber) {
        return new MessageListenerAdapter(flightUpdateSubscriber);
    }

    @Bean
    MessageListenerAdapter passengersListener(PassengerUpdateSubscriber passengerUpdateSubscriber) {
        return new MessageListenerAdapter(passengerUpdateSubscriber);
    }

    @Bean
    ChannelTopic flightsTopic() {
        return new ChannelTopic(flightsTopic);
    }

    @Bean
    ChannelTopic passengersTopic() {
        return new ChannelTopic(passengersTopic);
    }

    @Bean
    RedisMessageListenerContainer redisContainer(
            JedisConnectionFactory jedisConnectionFactory,

            MessageListenerAdapter flightsListener,
            ChannelTopic flightsTopic,

            MessageListenerAdapter passengersListener,
            ChannelTopic passengersTopic
    ) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(jedisConnectionFactory);

        container.addMessageListener(flightsListener, flightsTopic);
        container.addMessageListener(passengersListener, passengersTopic);

        return container;
    }
}
