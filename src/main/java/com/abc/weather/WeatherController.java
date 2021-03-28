package com.abc.weather;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

	@Autowired
	private WeatherService service;
		
	@RequestMapping("/weather")
	@ResponseBody
    public WeatherModel getWeather(@RequestParam String q) {
        return this.service.getWeather(q);
    }
}
