package tools.fields;

import org.openqa.selenium.By;

import tools.WebDriverContainer;

/**
 * @author anli
 * This class is to be extended. We do know that all fields
 * include some kind of element. We also want all fields
 * to be able to be checked, cloned, and filled.
 */
public abstract class Field implements Cloneable {
	// Locator for the primary element in the field.
	private By locator;

	// General Constructor.
	protected Field(By loc) {
		locator = loc;
	}
	
	// Allows the child classes to access.
	public By getLocator() {
		return locator;
	}
	
	/*
	 * Creates a copy that does not modify current Field.
	 */
	public abstract Field clone();
	
	public abstract Field setValue(int val);
	public abstract Field setValue(String val);
	public abstract Field setValue(String[] val);
	
	/*
	 * When we're at a form, we might want to make sure that
	 * the element (matching our locator) contains our same
	 * value.
	 */
	public abstract boolean matches(
			WebDriverContainer container);
	
	/*
	 * Fills the field based on the values passed into our
	 * field.
	 */
	public abstract void fill(WebDriverContainer container);

}
