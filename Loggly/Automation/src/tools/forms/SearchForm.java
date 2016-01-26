package tools.forms;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import tools.BasicFunctions;
import tools.WebDriverContainer;
import tools.fields.Field;
import tools.locators.ExitButtonLocator;
import tools.locators.RefreshedLocator;
import tools.locators.ResultLocator;
import tools.locators.SubmitButtonLocator;

public class SearchForm extends Form {

	private By refreshedLocator;
	private By resultLocator;
	
	public SearchForm(SubmitButtonLocator submit,
			Field[] things, RefreshedLocator refreshed,
			ResultLocator result) {
		super(submit, things);
		refreshedLocator = refreshed.getLocator();
		resultLocator = result.getLocator();
	}

	public SearchForm(SubmitButtonLocator submit,
			ExitButtonLocator exit, Field[] things,
			RefreshedLocator refreshed,
			ResultLocator result) {
		super(submit, exit, things);
		refreshedLocator = refreshed.getLocator();
		resultLocator = result.getLocator();
	}
	
	public boolean submit(WebDriverContainer container) {
		WebDriver driver = container.getDriver();
		WebElement refresher = container.findElement(
			refreshedLocator);
		super.submit(container);
		boolean sameResults = true;
		try {
			refresher.getTagName();
		}
		catch (Exception e) {
			sameResults = false;
		}
		while (sameResults) {
			BasicFunctions.pause(1000);
			try {
				refresher.getTagName();
			}
			catch (Exception e) {
				sameResults = false;
			}
		}
		List<WebElement> elements = driver.findElements(
			resultLocator);
		if (elements.size() > 0) {
			try {
				elements.get(0).click();
				while (elements.get(0).getAttribute("class")
						.toLowerCase()
						.indexOf("selected") < 0) {
					BasicFunctions.pause(1000);
					elements.get(0).click();
				}
			}
			catch (Throwable t) {
				BasicFunctions.pause(1000);
				WebElement element = driver.findElement(
					resultLocator);
				element.click();
			}
			return true;
		}
		return false;
	}
	
	protected boolean submitWithoutInitialResultContainer(
			WebDriverContainer container) {
		WebDriver driver = container.getDriver();
		super.submit(container);
		boolean sameResults = true;
		try {
			container.findElement(refreshedLocator, 5);
			sameResults = false;
		}
		catch (Exception e) {
		}
		for (int i = 0; sameResults; i++) {
			BasicFunctions.pause(1000);
			try {
				container.findElement(
					refreshedLocator);
				sameResults = false;
			}
			catch (Exception e) {
				if (i >= 2) {
					System.exit(1);
				}
			}
		}
		List<WebElement> elements = driver.findElements(
			resultLocator);
		if (elements.size() > 0) {
			try {
				elements.get(0).click();
				while (elements.get(0).getAttribute("class")
						.toLowerCase()
						.indexOf("selected") < 0) {
					BasicFunctions.pause(1000);
					elements.get(0).click();
				}
			}
			catch (Throwable t) {
				BasicFunctions.pause(1000);
				WebElement element = driver.findElement(
					resultLocator);
				element.click();
			}
			return true;
		}
		return false;
	}

}
