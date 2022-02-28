package com.easybroker.demo.services;

import com.easybroker.demo.beans.Properties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
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
public class PropertiesService {
	@Autowired
	RestTemplate restTemplate;

	@Bean
	public CommandLineRunner run(RestTemplate restTemplate) throws Exception {
		return args -> {
			String url = "https://api.stagingeb.com/v1/properties";
			do {
				Properties properties = getPropertiesList(url);
				getPropertiesList(url).getContent().forEach(property -> System.out.println(property.getTitle()));
				url = properties.getPagination().getNextPage();
			} while (url != null);
		};
	}

	public Properties getPropertiesList(String url) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("x-authorization", "l7u502p8v46ba3ppgvj5y2aad50lb9");
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		return restTemplate.exchange(url,
				HttpMethod.GET, entity, Properties.class).getBody();
	}

	@RequestMapping(value = "/properties")
	public Properties getPropertiesEndpoint(
			@RequestParam(defaultValue = "https://api.stagingeb.com/v1/properties") String url,
			@RequestParam(required = false) String page) {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.add("x-authorization", "l7u502p8v46ba3ppgvj5y2aad50lb9");
		HttpEntity<String> entity = new HttpEntity<String>(headers);

		Properties properties = restTemplate.exchange(ObjectUtils.isEmpty(page) ?
						url : new StringBuilder().append(url).append("?page=").append(page).toString(),
				HttpMethod.GET, entity, Properties.class).getBody();

		properties.getPagination().setNextPage(
				StringUtils.replace(properties.getPagination().getNextPage(),
						"https://api.stagingeb.com/v1", "http://localhost:8080"));

		return properties;
	}

}
