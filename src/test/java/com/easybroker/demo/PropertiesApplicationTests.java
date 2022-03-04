package com.easybroker.demo;

import com.easybroker.demo.beans.Pagination;
import com.easybroker.demo.beans.Properties;
import com.easybroker.demo.beans.Property;
import com.easybroker.demo.services.PropertiesService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@ActiveProfiles("test")
class PropertiesApplicationTests {

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private PropertiesService propertiesService;

	@Test
	public void testGetProperties() throws Exception {
		Property property = new Property();
		property.setTitle("title");

		List<Property> propertiesList = new ArrayList<>();
		propertiesList.add(property);
		Properties properties = new Properties();

		Pagination pagination = new Pagination();
		pagination.setNextPage("nextpage");

		properties.setContent(propertiesList);
		properties.setPagination(pagination);
		ResponseEntity<Properties> myEntity = new ResponseEntity<>(properties, HttpStatus.ACCEPTED);
		when(restTemplate.exchange(
				ArgumentMatchers.anyString(),
				ArgumentMatchers.any(HttpMethod.class),
				ArgumentMatchers.any(),
				ArgumentMatchers.<Class<Properties>>any()))
				.thenReturn(myEntity);

		Properties res = propertiesService.getPropertiesList("", propertiesService.getHeaders());
		assertEquals(properties.getContent().get(0).getTitle(), res.getContent().get(0).getTitle());
		assertEquals(properties.getPagination().getNextPage(), res.getPagination().getNextPage());
	}

}
