package com.easybroker.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class PropertiesEndpointTest {
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	public void testPropertiesEndpoint() throws Exception {
		File file = ResourceUtils.getFile("classpath:test.json");
		String content = new String(Files.readAllBytes(file.toPath()));
		assertThat(restTemplate.getForObject("http://localhost:" + port + "/properties",
				String.class)).isEqualTo(content);
	}
}
