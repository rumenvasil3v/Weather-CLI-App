package cli; // grouping related classes
// accessing each other methods and fields without the need of import

import java.io.IOException;
import java.util.Scanner;

import service.WeatherService;

public class Main {
	public static void main(String[] args) {	
		WeatherService service = new WeatherService();
		
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter city name: ");
		String userCity = scanner.nextLine();
		System.out.print("Enter for how many days to display weather ahead (1 - 14): ");
		String days = scanner.nextLine();
		
		System.out.println("Making http get request...");
		
		System.out.println("================Weather Info================");
		
		try {
			System.out.println(service.getWeather(userCity, Integer.parseInt(days)).toString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
