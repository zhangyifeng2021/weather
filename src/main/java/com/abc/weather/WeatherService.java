package com.abc.weather;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import net.sf.json.JSONObject;

@Service
public class WeatherService {
	
	private RestTemplate restTemplate;
	
	@Value("${weather.api.host}")
	private String weatherApiHost;
	
	@Value("${weather.api.appid}")
	private String weatherApiId;

	public WeatherModel getWeather(final String city) {
		
		WeatherModel weatherModel;
		
		try {
			this.restTemplate = new RestTemplate();
			URI weatherUri = this.getWeatherUri(city);
			if(weatherUri != null) {
				String responseJson = this.restTemplate.getForObject(weatherUri, String.class);
				this.getWeatherModel(responseJson);
			}
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		
		
		
		
		weatherModel = new WeatherModel();
		weatherModel.setCity("Melbourne");
		weatherModel.setUpdatedTime(new Date());
		weatherModel.setWeather("Mostly Cloudy");
		weatherModel.setTemperature("9°C");
		weatherModel.setWind("32km/h");
		
		return weatherModel;
	}
	
	/**
	 * Gets weather information from api response
	 * 
	 * @param responseJson
	 * @return
	 */
	private WeatherModel getWeatherModel(final String responseJson) {
		WeatherModel weatherModel = null;
		if(responseJson != null) {
			weatherModel = new WeatherModel();
			JSONObject jsonObject = JSONObject.fromObject(responseJson);
			final String city = jsonObject.getString("name");
			System.out.println(city);
			weatherModel.setUpdatedTime(null);
		}
		return weatherModel;
	}
	
	/**
	 * Gets weather URI
	 * 
	 * @param cityName
	 * @return
	 * @throws URISyntaxException
	 */
	private URI getWeatherUri(final String cityName) throws URISyntaxException {
		URI weatherUri = null;
		
		if(cityName != null) {StringBuilder sbuff = new StringBuilder();
			sbuff.append(this.weatherApiHost);
			sbuff.append("?q=");
			sbuff.append(cityName);
			sbuff.append("&appid=");
			sbuff.append(this.weatherApiId);
			weatherUri = new URI(sbuff.toString());
		}
		System.out.println(weatherUri);
		return weatherUri;
	}
}
