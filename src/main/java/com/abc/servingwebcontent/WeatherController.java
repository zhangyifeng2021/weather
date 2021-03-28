package com.abc.servingwebcontent;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.abc.weather.WeatherModel;
import com.abc.weather.WeatherService;

@Controller
public class WeatherController {

	@Autowired
	private WeatherService service;

	@GetMapping("/weather")
	public String greeting(@RequestParam(name = "q", required = true, defaultValue = "Melbourne") String q,
			Model model) {

		// get weather info for the three cities
		WeatherModel sydney = this.service.getWeather("Sydney");
		WeatherModel melbourne = this.service.getWeather("Melbourne");
		WeatherModel wollongong = this.service.getWeather("Wollongong");

		// set date to the mode for UI
		model.addAttribute("sydney", sydney);
		model.addAttribute("melbourne", melbourne);
		model.addAttribute("wollongong", wollongong);

		return "weather";
	}
}
