package tools.fields;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import tools.WebDriverContainer;
/**
 * The most basic Field. All we need to do is click on
 * the element.
 * @author anli
 *
 */
public class ClickField extends Field {

	public ClickField(By loc) {
		super(loc);
	}

	@Override
	public Field clone() {
		return new ClickField(getLocator());
	}
    public ClickField setValue(int value) {
    	return (ClickField) clone();
    }
    public ClickField setValue(String val) {
    	return (ClickField) clone();
    }
    public ClickField setValue(String[] val) {
    	return (ClickField) clone();
    }

	/**
	 * Returns true because there isn't a specific way
	 * to test a checkbox.
	 */
	@Override
	public boolean matches(WebDriverContainer container) {
		return true;
	}

	@Override
	public void fill(WebDriverContainer container) {
		WebElement element = container.findElement(
			getLocator());
		element.click();
	}

}
