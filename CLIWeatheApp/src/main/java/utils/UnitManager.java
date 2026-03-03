package utils;

import java.util.Scanner;

public final class UnitManager {
	private static final Scanner myScanner = new Scanner(System.in);
	
	public static String chooseUnit(String property, String firstChoice, String secondChoice) {
		System.out.println("Choose unit for property -> " + property);
		System.out.print("You can choose between \'" + firstChoice + "\'" + " or " + "\'"
		+ secondChoice + "\'" + ": ");
		String unit = myScanner.nextLine();
		System.out.println();
		
		return unit;
	}
}
