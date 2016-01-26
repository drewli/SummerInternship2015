package tools.fields;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import tools.WebDriverContainer;
/**
 * @author anli
 * Represents a Field that we insert text into.
 */
public class InputField extends Field {
    private String[] value;
    private String entered;
    private boolean hasUnfocuser;
    private ClickField unfocuser;
    public InputField(By b, String v) {
        super(b);
        value = new String[] {v};
        entered = v;
        hasUnfocuser = false;
    }
    public InputField(By b, String[] v) {
        super(b);
    	value = new String[v.length];
    	entered = "";
    	for (int i = 0; i < value.length; i++) {
    		value[i] = v[i];
    		entered += v[i];
    	}
        hasUnfocuser = false;
    }
    public InputField(By b, String v, ClickField c) {
        super(b);
        value = new String[] {v};
        entered = v;
        hasUnfocuser = true;
        unfocuser = c;
    }
    public InputField(By b, String[] v, ClickField c) {
        super(b);
    	value = new String[v.length];
    	entered = "";
    	for (int i = 0; i < value.length; i++) {
    		value[i] = v[i];
    		entered += v[i];
    	}
        hasUnfocuser = true;
        unfocuser = c;
    }
    /**
     * A copy includes only the locator and value.
     */
    @Override
    public InputField clone() {
    	if (hasUnfocuser) {
    		return new InputField(getLocator(), value,
    			(ClickField) unfocuser.clone());
    	}
    	return new InputField(getLocator(), value);
    }
    /**
     * Creates a copy of the value and returns it.
     * Reason we need a copy is that we don't want
     * outside classes to modify the value of the object.
     * @return Copy of the value
     */
    public String[] getValue() {
    	String[] output = new String[value.length];
    	for (int i = 0; i < output.length; i++) {
    		output[i] = value[i];
    	}
    	return output;
    }
    /*
     * These methods return an InputField with the same
     * locator but with the set value.
     */
    public InputField setValue(int val) {
    	String valString = "" + val;
    	return new InputField(getLocator(), valString);
    }
    public InputField setValue(String val) {
    	return new InputField(getLocator(), val);
    }
    public InputField setValue(String[] val) {
    	return new InputField(getLocator(), val);
    }
    /*
     * For subclasses (anonymous or defined) to use the
     * expected value of the input box.
     */
    protected String getEntered() {
    	return entered;
    }
    /*
     * Changes the expected value of the input box.
     */
    protected void setEntered(String enteredInput) {
    	entered = enteredInput;
    }
    /**
     * Compares the value of the input box to the expected
     * value of the input box.
     */
    @Override
    public boolean matches(WebDriverContainer container) {
    	if (value.length == 0) {
    		return false;
    	}
    	return entered.equals(container.findElement(
    		getLocator()).getAttribute("value"));
    }
    /**
     * Inserts text into the input box, unfocuses the input
     * box, and sets the expected value of the input box to
     * whatever value is in the input box (if we want to check
     * again). We have to unfocus to see if the text changes
     * (since 2/2/12 might change to 02/02/2012 and 5000
     * might change to 5,000.00)
     */
    @Override
    public void fill(WebDriverContainer container) {
    	WebElement element = container.findElement(
    		getLocator());
        element.clear();
        element.sendKeys(getValue());
        if (hasUnfocuser) {
        	unfocuser.fill(container);
        }
        else {
            element.findElement(By.xpath("..")).click();
        }
        setEntered(element.getAttribute("value"));
    }
}
