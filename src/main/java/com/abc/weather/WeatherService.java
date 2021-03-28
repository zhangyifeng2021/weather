package com.abc.weather;

import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

@Service
public class WeatherService {

	@Value("${weather.api.host}")
	private String weatherApiHost;

	@Value("${weather.api.appid}")
	private String weatherApiId;
	
	/**
	 * Gets the weather information of the city.
	 * 
	 * @param city
	 * @return
	 */
	public WeatherModel getWeather(final String city) {
		WeatherModel weatherModel = null;
		try {
			if (city != null) {
				final String url = this.getWeatherUri(city);
				DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
				DocumentBuilder db = dbf.newDocumentBuilder();
				Document doc = db.parse(url);
				doc.getDocumentElement().normalize();
				weatherModel = this.getWeatherModel(city, doc.getDocumentElement());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return weatherModel;
	}

	/**
	 *  Gets weather model from api response
	 *  
	 * @param city
	 * @param docElement
	 * @return
	 */
	private WeatherModel getWeatherModel(final String city, final Element docElement) {
		WeatherModel weatherModel = new WeatherModel();
		if (docElement != null) {
			weatherModel.setCity(city);
			weatherModel.setUpdatedTime(this.getUpdatedTime(docElement));
			weatherModel.setWeather(this.getNodeAttrValue(docElement, Constants.WEATHER));
			weatherModel.setTemperature(this.getNodeAttrValue(docElement, Constants.TEMPERATURE));
			weatherModel.setWind(this.getWindInfo(docElement));
		}
		return weatherModel;
	}
	
	/**
	 * Gets last updated time.
	 * 
	 * @param docElement
	 * @return
	 */
	private String getUpdatedTime(final Element docElement) {
		String dateInString = this.getNodeAttrValue(docElement, Constants.LAST_UPDATE);
		SimpleDateFormat sdf = new SimpleDateFormat(Constants.DATE_FORMAT_INPUT);
		SimpleDateFormat sdf2 = new SimpleDateFormat(Constants.DATE_FORMAT_OUTPUT, Locale.US);
		String newTime = Constants.EMPTY;
		
		try {
			Date start = sdf.parse(dateInString);
			newTime = sdf2.format(start);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return newTime;
	}

	/**
	 * Gets the attribute value from xml response.
	 * 
	 * @param docElement
	 * @param nodeName
	 * @return
	 */
	private String getNodeAttrValue(final Element docElement, final String nodeName) {
		String attrValue = Constants.EMPTY;
		NodeList nodeList = docElement.getElementsByTagName(nodeName);
		if(nodeList!= null) {
			Node node = nodeList.item(0);
			if (node != null && node.hasAttributes()) {
				NamedNodeMap attrs = node.getAttributes();
				for (int i = 0; i < attrs.getLength(); i++) {
					Attr attribute = (Attr) attrs.item(i);
					if (attribute != null && Constants.VALUE.equals(attribute.getName())) {
						attrValue = attribute.getValue();
					}
				}
			}
		}
		return attrValue;
	}

	/**
	 * Gets wind info.
	 * 
	 * @param docElement
	 * @return
	 */
	private String getWindInfo(final Element docElement) {
		StringBuffer sbuff = new StringBuffer();
		// only wind node has the speed attribute
		NodeList nodeList = docElement.getElementsByTagName(Constants.SPEED);
		if(nodeList!= null) {
			Node node = nodeList.item(0);
			if (node != null && node.hasAttributes()) {
				String value = Constants.EMPTY;
				String unit = Constants.EMPTY;
				NamedNodeMap attrs = node.getAttributes();
				for (int i = 0; i < attrs.getLength(); i++) {
					Attr attribute = (Attr) attrs.item(i);
					if (attribute != null) {
						if(Constants.VALUE.equals(attribute.getName())) {
							value = attribute.getValue();
						} else if(Constants.UNIT.equals(attribute.getName())) {
							unit = attribute.getValue();
						}
					}
				}
				sbuff.append(value);
				sbuff.append(unit);
			}
		}
		return sbuff.toString();
	}

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
			StringBuffer sbuff = new StringBuffer();
			sbuff.append(this.weatherApiHost);
			sbuff.append("?q=");
			sbuff.append(city);
			sbuff.append("&mode=xml&appid=");
			sbuff.append(this.weatherApiId);
			weatherUri = sbuff.toString();
		}
		return weatherUri;
	}
}
