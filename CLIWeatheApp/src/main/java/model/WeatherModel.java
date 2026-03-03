package model;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class WeatherModel {
	StringBuilder sb = new StringBuilder();

	private String date;
	private String text;
	private double temperature;
	private double windSpeed;
	private double gusting;
	private String windDirection;
	private double windChill;
	private double feelsLike;
	private String humidity;
	private double visibility;

	private final Map<Integer, String> futureDays = new HashMap<Integer, String>();
	private final Map<String, String> displayUnits = new HashMap<String, String>();

	public WeatherModel(String text, double temperature, double windSpeed, double gusting, String windDirection,
			double windChill, double feelsLike, String humidity, double visibility, String date) {
		this.text = text;
		this.temperature = temperature;
		this.windSpeed = windSpeed;
		this.gusting = gusting;
		this.windDirection = windDirection;
		this.windChill = windChill;
		this.feelsLike = feelsLike;
		this.humidity = humidity;
		this.visibility = visibility;
		this.date = date;
		
		sb.append("------------------------- Current Day -------------------------\n");
		sb.append("Date: " + this.date + "\n");
		sb.append("Weather State: " + this.getText() + "\n");
		sb.append("Wind Direction: " + this.getWindDirection() + "\n");
		sb.append("Humidity: " + this.getHumidity() + "\n");
	}

	public Map<String, String> getDisplayUnits() {
		return this.displayUnits;
	}

	public String getText() {
		return this.text;
	}

	public double getTemperature() {
		return this.temperature;
	}

	public double getWindSpeed() {
		return this.windSpeed;
	}

	public double getGusting() {
		return this.gusting;
	}

	public String getWindDirection() {
		return this.windDirection;
	}

	public double getWindChill() {
		return this.windChill;
	}

	public double getFeelsLike() {
		return this.feelsLike;
	}

	public String getHumidity() {
		return this.humidity;
	}

	public double getVisibility() {
		return this.visibility;
	}

	public void displayInImperialUnit(String property, double value) {

		switch (property) {
		case "temperature":
			sb.append("Temperature (Fahrenheit): " + this.convertToFahrenheit(this.getTemperature()) + " °F\n");
			break;
		case "wind_speed":
			sb.append("Wind Speed (Miles Per Hour): " + this.convertToMph(this.getWindSpeed()) + " mph\n");
			break;
		case "gusting":
			sb.append("Gusting (Miles Per Hour): " + this.convertToMph(this.getGusting()) + " mph\n");
			break;
		case "wind_chill":
			sb.append("Wind Chill (Fahrenheit): " + this.convertToFahrenheit(this.getWindChill()) + " °F\n");
			break;
		case "feels_like":
			sb.append("Feels Like (Fahrenheit): " + this.convertToFahrenheit(this.getFeelsLike()) + " °F\n");
			break;
		case "visibility":
			sb.append("Visibility (Miles): " + this.convertToMph(this.getVisibility()) + " miles\n");
			break;
		}
	}

	public void displayInMetricUnit(String property, double value) {

		switch (property) {
		case "temperature":
			sb.append("Temperature (Celsius): " + this.temperature + " °C\n");
			break;
		case "wind_speed":
			sb.append("Wind Speed (Kilometers Per Hour): " + this.windSpeed + " kph\n");
			break;
		case "gusting":
			sb.append("Gusting (Kilometers Per Hour): " + this.getGusting() + " kph\n");
			break;
		case "wind_chill":
			sb.append("Winh Chill (Celsius): " + this.getWindChill() + " °C\n");
			break;
		case "feels_like":
			sb.append("Feels Like (Celsius): " + this.getFeelsLike() + " °C\n");
			break;
		case "visibility":
			sb.append("Visibility (Kilometers): " + this.getVisibility() + " km\n");
			break;
		}
	}

	public void addWeahterDayInfo(int day, String dayInfo) {
		this.futureDays.put(day, dayInfo);
	}

	private double convertToFahrenheit(double value) {
		return (double) Math.round((value * 1.8 + 32) * 10) / 10;
	}

	private double convertToMph(double value) {
		return (double) Math.round((value * 0.621371) * 10) / 10;
	}

	private void addFutureDaysInfoToCurrentDay() {

		for (Integer key : this.futureDays.keySet()) {
			JsonObject object = JsonParser.parseString(this.futureDays.get(key)).getAsJsonObject();

			int day = key + 1;
			sb.append("------------------------- Day " + day + " -------------------------\n");
			sb.append("Date: " + object.get("date").getAsString() + "\n");

			JsonObject dayData = object.get("day").getAsJsonObject();
			double currentDayHumidity = dayData.get("avghumidity").getAsDouble();
			String dayWeatherState = dayData.get("condition").getAsJsonObject().get("text").getAsString();
			double chanceOfRaining = dayData.get("daily_chance_of_rain").getAsDouble();
			
			sb.append("Average Humidity: " + currentDayHumidity + "\n");
			sb.append("Weather State: " + dayWeatherState + "\n");
			sb.append("Chance of Raining: " + chanceOfRaining + " %\n");
			
			for (String unitKey : this.displayUnits.keySet()) {
				String unit = this.displayUnits.get(unitKey);

				switch (unitKey) {
				case "temperature":
					sb.append("Average ");
					if (unit.equals("Fahrenheit")) {
						this.displayInImperialUnit(unitKey, dayData.get("avgtemp_f").getAsDouble());
					} else {
						this.displayInMetricUnit(unitKey, dayData.get("avgtemp_c").getAsDouble());
					}
					break;
				case "wind_speed":
					sb.append("Average ");
					if (unit.equals("Miles Per Hour")) {
						this.displayInImperialUnit(unitKey, dayData.get("maxwind_mph").getAsDouble());
					} else {
						this.displayInMetricUnit(unitKey, dayData.get("maxwind_kph").getAsDouble());
					}
					break;
				case "visibility":
					sb.append("Average ");
					if (unit.equals("Miles")) {
						this.displayInImperialUnit(unitKey, dayData.get("avgvis_miles").getAsDouble());
					} else {
						this.displayInMetricUnit(unitKey, dayData.get("avgvis_km").getAsDouble());
					}
				}
			}
		}
	}

	@Override
	public String toString() {
		this.addFutureDaysInfoToCurrentDay();

		return sb.toString();
	}
}
