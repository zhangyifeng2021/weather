package com.abc.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

	@Autowired
	private WeatherService service;
		
	@RequestMapping("/weather")
    public WeatherModel getWeather() {
		final String cityName = "London";
        return this.service.getWeather(cityName);
    }
}
