package com.easybroker.demo.controllers;

import com.easybroker.demo.entities.Properties;
import com.easybroker.demo.services.PropertiesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Profile("!test")
public class PropertiesControllers {
	@Autowired
	PropertiesService propertiesService;

	@RequestMapping(value = "/properties")
	public Properties getPropertiesEndpoint(@RequestParam(required = false) String page) {
		String url = "https://api.stagingeb.com/v1/properties";
		Properties properties = propertiesService.getPropertiesList(ObjectUtils.isEmpty(page) ?
				url : new StringBuilder().append(url).append("?page=").append(page).toString(), propertiesService.getHeaders());
		properties.getContent().forEach(property -> System.out.println(property.getTitle()));
		properties.getPagination().setNextPage(
				StringUtils.replace(properties.getPagination().getNextPage(),
						"https://api.stagingeb.com/v1", "http://localhost:8080"));

		return properties;
	}
}
