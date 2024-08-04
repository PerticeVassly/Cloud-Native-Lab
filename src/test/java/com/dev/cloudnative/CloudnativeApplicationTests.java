package com.dev.cloudnative;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")  // 使用测试配置文件
class CloudnativeApplicationTests {

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	void testRateLimiter() {
		String url = "/api/hello";

		// 发送在限制内的请求
		for (int i = 0; i < 100; i++) {
			ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
			assertThat(response.getStatusCodeValue()).isEqualTo(200);
			assertThat(response.getBody()).contains("{\"msg\":\"hello\"}");
		}

		// 发送超过限制的请求
		ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
		assertThat(response.getStatusCodeValue()).isEqualTo(429);
		assertThat(response.getBody()).contains("{\"msg\":\"Too many requests\"}");
	}
}
