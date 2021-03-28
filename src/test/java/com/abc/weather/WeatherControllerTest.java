package com.abc.weather;

import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@SpringBootTest
@AutoConfigureMockMvc
public class WeatherControllerTest {

	@Autowired
	private MockMvc mvc;

	@Test
	public void getWeater() throws Exception {
		mvc.perform(MockMvcRequestBuilders.get("/weather").accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk());
	}
}
