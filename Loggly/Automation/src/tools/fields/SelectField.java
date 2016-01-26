package tools.fields;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import tools.WebDriverContainer;

import java.util.*;
/**
 * This kind of Field involves select options.
 * @author anli
 */
public class SelectField extends Field {
    private int index;
    private String optionName;
    private boolean byName;
    
    /*
     * Creates the SelectField. The type of constructor used
     * determines whether the value is obtained by name or
     * by index.
     */
    public SelectField(By b, int i) {
    	super(b);
        index = i;
        byName = false;
    }
    public SelectField(By b, String v) {
        super(b);
        optionName = v;
        index = -1;
        byName = true;
    }
    public SelectField setValue(int val) {
    	SelectField copy = clone();
    	copy.byName = false;
    	copy.index = val;
    	return copy;
    }
    public SelectField setValue(String val) {
    	SelectField copy = clone();
    	copy.byName = true;
    	copy.optionName = val;
    	return copy;
    }
    public SelectField setValue(String[] val) {
    	SelectField copy = clone();
    	copy.byName = true;
    	copy.optionName = "";
    	for (int i = 0; i < val.length; i++) {
    		copy.optionName += val[i];
    	}
    	return copy;
    }
    @Override
    public SelectField clone() {
    	if (byName) {
    		return new SelectField(getLocator(), optionName); 
    	}
    	else {
    		return new SelectField(getLocator(), index);
    	}
    }
    
    public boolean isByName() {
    	return byName;
    }
    
    @Override
    public boolean matches(WebDriverContainer container) {
    	Select select = new Select(container
    		.findElement(getLocator()));
    	return optionName.equals(select
    		.getFirstSelectedOption()
    		.getText());
    }
    @Override
    public void fill(WebDriverContainer container) {
    	WebElement element = container.findElement(
    		getLocator());
    	Select select = new Select(element);
    	List<WebElement> options = select.getOptions();
    	/*
    	 * In case the index is out of range.
    	 */
    	index += options.size();
    	index %= options.size();
    	/*
    	 * Searches for the index of the option with the
    	 * given name if we created the Select Field by
    	 * name
    	 */
    	if (byName) {
        	index = options.size() - 1;
        	for (int i = 0; i < options.size(); i++) {
        		if (options.get(i).getText().equalsIgnoreCase(
        				optionName)) {
        			index = i;
        			i = options.size();
        		}
        		else if (options.get(i).getText()
        				.toLowerCase().indexOf(optionName
        				.toLowerCase()) >= 0 && index >= i) {
        			index = i;
        		}
        	}
    	}
    	options.get(index).click();
    	element.findElement(By.xpath("..")).click();
    	optionName = select.getFirstSelectedOption().getText();
    }
}