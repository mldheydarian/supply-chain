package com.gts.supplychain;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cache.CacheManager;
import org.springframework.cache.support.NoOpCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@ActiveProfiles({ "test" })
@TestPropertySource(locations = { "classpath:application-test.properties" })
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AbstractIntegrationTest {

	@LocalServerPort
	protected Integer port;

	@Configuration
	public static class TestConfig {

		@Bean("inMemoryCacheManager")
		public CacheManager inMemoryCacheManager() {
			return new NoOpCacheManager();
		}

	}

}
