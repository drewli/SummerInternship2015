package tools.locators;

import org.openqa.selenium.By;

public class Locator {
	private By locator;
	
	protected Locator(By by) {
		locator = by;
	}

	public By getLocator() {
		return locator;
	}

}
