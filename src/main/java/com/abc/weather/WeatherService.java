package com.abc.weather;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import net.sf.json.JSONObject;

@Service
public class WeatherService {

	private RestTemplate restTemplate;

	@Value("${weather.api.host}")
	private String weatherApiHost;

	@Value("${weather.api.appid}")
	private String weatherApiId;

	public WeatherModel getWeather(final String city) {

		WeatherModel weatherModel = null;

		try {
			if (city != null) {
				final String url = this.getWeatherUri(city);
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(url);
				doc.getDocumentElement().normalize();
				
				System.out.println(doc.getDocumentElement().getNodeName());
			}
		} catch (Exception e) {
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
	/*
	 * private WeatherModel getWeatherModel(final String responseJson) {
	 * WeatherModel weatherModel = null; if(responseJson != null) { weatherModel =
	 * new WeatherModel(); JSONObject jsonObject =
	 * JSONObject.fromObject(responseJson); final String city =
	 * jsonObject.getString("name"); System.out.println(responseJson);
	 * weatherModel.setUpdatedTime(null); } return weatherModel; }
	 */

	/**
	 * Gets weather uri.
	 * 
	 * @param city
	 * @return
	 * @throws URISyntaxException
	 */
	private String getWeatherUri(final String city) throws URISyntaxException {
		String weatherUri = null;

		if (city != null) {
			StringBuilder sbuff = new StringBuilder();
			sbuff.append(this.weatherApiHost);
			sbuff.append("?q=");
			sbuff.append(city);
			sbuff.append("&mode=xml&appid=");
			sbuff.append(this.weatherApiId);
			weatherUri = sbuff.toString();
		}
		System.out.println(weatherUri);
		return weatherUri;
	}
}
