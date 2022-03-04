package com.easybroker.demo.services;

import com.easybroker.demo.beans.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

@RestController
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

	@RequestMapping(value = "/properties")
	public Properties getPropertiesEndpoint(@RequestParam(required = false) String page) {
		String url = "https://api.stagingeb.com/v1/properties";
		Properties properties = getPropertiesList(ObjectUtils.isEmpty(page) ?
				url : new StringBuilder().append(url).append("?page=").append(page).toString(), getHeaders());
		properties.getContent().forEach(property -> System.out.println(property.getTitle()));
		properties.getPagination().setNextPage(
				StringUtils.replace(properties.getPagination().getNextPage(),
						"https://api.stagingeb.com/v1", "http://localhost:8080"));

		return properties;
	}

}
