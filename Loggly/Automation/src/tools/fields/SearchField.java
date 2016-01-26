package tools.fields;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import tools.WebDriverContainer;
import tools.locators.ActivatorLocator;
import tools.locators.ResultLocator;
import tools.locators.TextboxLocator;
/**
 * SearchField is a unique field, since we have to click
 * on an activator for the input field to show, then
 * we have to type in a sequence of characters, and
 * we finally have to choose one of the available results
 * the show up.
 * @author anli
 *
 */
public class SearchField extends Field {
	InputField inputField; // e.g. By.xpath("//form[@id=\'theform\']/div[@control_pointer=\'item\']/input[@class=\'query\']")
	By activatorLocator; // e.g. By.id("tab_(item)_id_activator")
	By resultLocator; // e.g. By.xpath("//div[@id=\'tab_(item)_id_results\']/div")
	String value;

	public SearchField(By locator,
			TextboxLocator inputLocator,
			String value, ActivatorLocator activator,
			ResultLocator result) {
		super(locator);
		inputField = new InputField(inputLocator
			.getLocator(), value);
		activatorLocator = activator.getLocator();
		resultLocator = result.getLocator();
	}
	public SearchField(By locator, InputField input,
			ActivatorLocator activator,
			ResultLocator result) {
		super(locator);
		inputField = input.clone();
		activatorLocator = activator.getLocator();
		resultLocator = result.getLocator();
	}
	@Override
	public SearchField clone() {
		return new SearchField(getLocator(),
			inputField.clone(),
			new ActivatorLocator(activatorLocator),
			new ResultLocator(resultLocator));
	}
	@Override
	public boolean matches(WebDriverContainer container) {
		WebElement element = container.findElement(
			getLocator());
		return value.equals(element.getText());
	}
    /*
     * These methods return an SearchField with the same
     * locators but with the set value.
     */
    public SearchField setValue(int value) {
    	SearchField newSearchField = clone();
    	String val = "" + value;
    	newSearchField.inputField = new InputField(
    		getLocator(), val);
    	return newSearchField;
    }
    public SearchField setValue(String val) {
    	SearchField newSearchField = clone();
    	newSearchField.inputField = new InputField(
    		getLocator(), val);
    	return newSearchField;
    }
    public SearchField setValue(String[] val) {
    	SearchField newSearchField = clone();
    	newSearchField.inputField = new InputField(
    		getLocator(), val);
    	return newSearchField;
    }
	@Override
	public void fill(WebDriverContainer container) {
		WebElement element;
		// Clicks on the activator
		element = container.findElement(activatorLocator);
		element.click();

		// Fills the input box
		inputField.fill(container);

		// Presses enter after result shows up.
		element = container.findElement(resultLocator, 15);
		element = container.findElement(inputField
			.getLocator());
		element.sendKeys(Keys.RETURN);
		try {
			container.switchTo().alert().accept();
			container.switchTo().defaultContent();
		}
		catch (WebDriverException e) {}
		element = container.findElement(getLocator());
		
		// Saves the text
		value = element.getText();
	}
}
