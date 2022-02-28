package com.easybroker.demo.beans;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Pagination {
	private String limit;
	private String page;
	private String total;
	@JsonProperty("next_page")
	private String nextPage;
}
