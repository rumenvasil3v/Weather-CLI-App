package service;

import java.awt.List;
import java.awt.Window.Type;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.HashMap;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import model.WeatherModel;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import utils.UnitManager;

public class WeatherService {
	private static final String API_KEY = "774ce3b2d6274b00acb121947261902";
	private static final OkHttpClient client = new OkHttpClient();
	private static final String BASE_URL = "https://api.weatherapi.com/v1/forecast.json?key=" + API_KEY + "&q=";
	private static final Hashtable<String, String> cities = new Hashtable<String, String>();
	private final Map<String, WeatherModel> weatherDays = new HashMap<String, WeatherModel>();
	
	public WeatherService() {
		this.mapCitiesToISO();
	}

	private void mapCitiesToISO() {
		cities.put("NY", "New York, US");
		cities.put("Guildford", "Guildford, GB");
		cities.put("BrightonUk", "Brighton, GB");
		cities.put("BrightonUs", "Brighton, US");
		cities.put("Paris", "Paris, FR");
		cities.put("Sofia", "Sofia, BG");
		cities.put("Sydney", "Sydney, AUS");
	}

	public WeatherModel getWeather(String city, int numOfDays) throws IOException {
		if (!cities.containsKey(city)) {
			throw new IllegalArgumentException("City not found. Try agains");
		}

		Request request = new Request.Builder().url(BASE_URL + city + "&days=" + numOfDays).build();

		Response response = client.newCall(request).execute();

		JsonObject weatherJson = JsonParser.parseString(response.body().string()).getAsJsonObject();
		JsonObject current = weatherJson.getAsJsonObject("current");
		JsonObject condition = current.getAsJsonObject("condition");
		JsonObject forecast = weatherJson.getAsJsonObject("forecast");
		JsonArray forecastArray = forecast.getAsJsonArray("forecastday");
		
		String date = forecastArray.get(0).getAsJsonObject().get("date").getAsString();
		String text = condition.get("text").getAsString();
		double temperature = current.get("temp_c").getAsDouble();
		double windSpeed = current.get("wind_kph").getAsDouble();
		double gusting = current.get("gust_kph").getAsDouble();
		String windDirection = current.get("wind_dir").getAsString();
		double windChill = current.get("windchill_c").getAsDouble();
		double feelsLike = current.get("feelslike_c").getAsDouble();
		String humidity = current.get("humidity").getAsString();
		double visibility = current.get("vis_km").getAsDouble();
		
		WeatherModel model = new WeatherModel(text, temperature, windSpeed, gusting, windDirection, windChill,
				feelsLike, humidity, visibility, date);
		
		this.handleUnits(model, UnitManager.chooseUnit("Temperature", "Fahrenheit", "Celsius"),
				"temperature", "Celsius", "Fahrenheit", model.getTemperature());
		this.handleUnits(model, UnitManager.chooseUnit("Wind Speed", "Miles Per Hour", "Kilometers Per Hour"),
				"wind_speed", "Kilometers Per Hour", "Miles Per Hour", model.getWindSpeed());
		this.handleUnits(model, UnitManager.chooseUnit("Gusting", "Miles Per Hour", "Kilometers Per Hour"),
				"gusting", "Kilometers Per Hour", "Miles Per Hour", model.getGusting());
		this.handleUnits(model, UnitManager.chooseUnit("Wind Chill", "Fahrenheit", "Celsius"), "wind_chill",
				"Celsius", "Fahrenheit", model.getWindChill());
		this.handleUnits(model, UnitManager.chooseUnit("Feels Like", "Fahrenheit", "Celsius"),
				"feels_like", "Celsius", "Fahrenheit", model.getFeelsLike());
		this.handleUnits(model, UnitManager.chooseUnit("Visibility", "Miles",
				"Kilometers"), "visibility", "Kilometers", "Miles", model.getVisibility());
		
		for (int i = 1; i < forecastArray.size(); i++) {
			JsonObject currentDay = forecastArray.get(i).getAsJsonObject();
			
			model.addWeahterDayInfo(i, currentDay.toString());
		}
		
		return model;
	}

	private void handleUnits(WeatherModel model, String chosenUnit, String property, String metricUnit,
			String imperialUnit, double value) {
		if (chosenUnit.equals(metricUnit)) {
			model.displayInMetricUnit(property, value);
		} else if (chosenUnit.equals(imperialUnit)) {
			model.displayInImperialUnit(property, value);
		}
		
		model.getDisplayUnits().put(property, chosenUnit);
	}
	
}
