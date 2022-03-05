package com.easybroker.demo.services;

import com.easybroker.demo.entities.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@Service
@Profile("!test")
public class PropertiesService {

	@Autowired
	RestTemplate restTemplate;

	@Bean
	public CommandLineRunner run() throws Exception {
		return args -> {
			String url = "https://api.stagingeb.com/v1/properties";
			HttpEntity<String> headers = getHeaders();
			do {
				Properties properties = getPropertiesList(url, headers);
				properties.getContent().forEach(property -> System.out.println(property.getTitle()));
				url = properties.getPagination().getNextPage();
			} while (url != null);
		};
	}

	public HttpEntity<String> getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("x-authorization", "l7u502p8v46ba3ppgvj5y2aad50lb9");
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		return entity;
	}

	public Properties getPropertiesList(String url, HttpEntity<String> headers) {
		return restTemplate.exchange(url,
				HttpMethod.GET, headers, Properties.class).getBody();
	}

}
