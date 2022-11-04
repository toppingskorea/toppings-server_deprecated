// package com.toppings.server.domain_global.config;
//
// import java.time.Duration;
//
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.cache.CacheManager;
// import org.springframework.cache.annotation.EnableCaching;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.data.redis.cache.RedisCacheConfiguration;
// import org.springframework.data.redis.cache.RedisCacheManager;
// import org.springframework.data.redis.connection.RedisConnectionFactory;
// import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
// import org.springframework.data.redis.core.RedisTemplate;
// import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
// import org.springframework.data.redis.serializer.RedisSerializationContext;
// import org.springframework.data.redis.serializer.RedisSerializer;
// import org.springframework.data.redis.serializer.StringRedisSerializer;
//
// @Configuration
// @EnableCaching
// public class CacheConfig {
//
// 	@Value("${spring.redis.host}")
// 	private String host;
//
// 	@Value("${spring.redis.port}")
// 	private int port;
//
// 	@Bean
// 	public RedisConnectionFactory redisConnectionFactory() {
// 		return new LettuceConnectionFactory(host, port);
// 	}
//
// 	@Bean
// 	public CacheManager userCacheManager(RedisConnectionFactory connectionFactory) {
// 		RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
// 			.serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
// 			.serializeValuesWith(
// 				RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
// 			.entryTtl(Duration.ofMinutes(3L));
//
// 		return RedisCacheManager.RedisCacheManagerBuilder.fromConnectionFactory(connectionFactory)
// 			.cacheDefaults(redisCacheConfiguration)
// 			.build();
// 	}
//
// 	@Bean
// 	public RedisTemplate<?, ?> redisTemplate() {
// 		RedisTemplate<?, ?> redisTemplate = new RedisTemplate<>();
// 		redisTemplate.setDefaultSerializer(RedisSerializer.json());
// 		redisTemplate.setKeySerializer(RedisSerializer.string());
// 		redisTemplate.setHashKeySerializer(RedisSerializer.string());
// 		redisTemplate.setConnectionFactory(redisConnectionFactory());
// 		return redisTemplate;
// 	}
// }
