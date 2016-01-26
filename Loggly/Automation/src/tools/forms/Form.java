package tools.forms;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import tools.WebDriverContainer;
import tools.fields.Field;
import tools.locators.*;

import com.google.common.util.concurrent.SimpleTimeLimiter;
import com.google.common.util.concurrent.UncheckedTimeoutException;

import exceptions.IECrashException;

public class Form implements Submitter {
	private By submitButtonLocator;
	private boolean hasExitButton;
	private By exitButtonLocator;
	private Field[] fields;
	
	/*
	 * A form has a list of fields and a submit button.
	 * There are also optional attributes, such as exit
	 * button (like close or cancel) and result (if the
	 * form is a search form). We also have a boolean
	 * called resultExpected, which is set to false
	 * if there's no result. If there is a result,
	 * the default value of resultExpected is true
	 * but we might set it to false if we don't
	 * expect a result (so we don't have to wait as long
	 * for the result).
	 */
	
	public Form(SubmitButtonLocator submit,
			Field[] things) {
		submitButtonLocator = submit.getLocator();
		hasExitButton = false;
		fields = things;
	}
	
	public Form(SubmitButtonLocator submit,
			ExitButtonLocator exit, Field[] things) {
		submitButtonLocator = submit.getLocator();
		hasExitButton = true;
		exitButtonLocator = exit.getLocator();
		fields = things;
	}
	// Returns a deep copy
	public Form clone() {
		Field[] fieldlist = getFields();
		if (hasExitButton) {
			return new Form(
				new SubmitButtonLocator(
					submitButtonLocator),
				new ExitButtonLocator(exitButtonLocator),
				fieldlist);
		} 
		else {
			return new Form(
				new SubmitButtonLocator(
					submitButtonLocator),
				fieldlist);
		}
	}
	
	public Field[] getFields() {
		Field[] copy = new Field[fields.length];
		for (int i = 0; i < fields.length; i++) {
			copy[i] = fields[i];
		}
		return copy;
	}
	/* Fills each field, clicks the submit button, and
	 * clicks on the result if a result exists.
	 */
	public boolean submit(WebDriverContainer container) {
		for (int i = 0; i < fields.length; i++) {
			try {
			    fields[i].fill(container);
			}
			catch (WebDriverException e) {
				System.out.println(e.getStackTrace());
			}
		}
		WebElement element = container.findElement(
			submitButtonLocator);
		element.click();
		try {
			container.switchTo().alert().accept();
			container.switchTo().defaultContent();
		}
		catch (WebDriverException e) {}
		return false;
	}
	public void exit(WebDriverContainer container) {
		if (hasExitButton) {
			try {
			    container.findElement(
			    	exitButtonLocator).click();
			}
			catch (Throwable t) {
				System.out.println(t.getMessage());
			}
		}
	}
	// Same as submit but exits out if exit button exists.
	public void submitAndExit(WebDriverContainer container) {
		submit(container);
		exit(container);
	}
	
	/* 
	 * Similar to submit functions above but accounts for
	 * the possibility of a crash.
	 */
	public static void submit(Form form,
			WebDriverContainer container) throws IECrashException {
		if (form.fields.length > 0) {
			container.findElement(form
				.fields[0].getLocator());
		}
		else {
			container.findElement(form
				.submitButtonLocator);
		}
		SimpleTimeLimiter limiter = new SimpleTimeLimiter();
	    Submitter proxy = limiter.newProxy(form,
	        Submitter.class, form.fields.length * 4 + 40,
	        TimeUnit.SECONDS);
	    try {
		    proxy.submit(container);
	    }
	    catch (UncheckedTimeoutException e) {
	    	throw new IECrashException("Internet Explorer "
	    		+ "has crashed.");
	    }
	}
	public static void submit(Form form,
			WebDriverContainer container,
			int seconds) throws IECrashException  {
		if (form.fields.length > 0) {
			container.findElement(form
				.fields[0].getLocator());
		}
		else {
			container.findElement(form
				.submitButtonLocator);
		}
		SimpleTimeLimiter limiter = new SimpleTimeLimiter();
	    Submitter proxy = limiter.newProxy(form,
	        Submitter.class, seconds, TimeUnit.SECONDS);
	    try {
		    proxy.submit(container);
	    }
	    catch (UncheckedTimeoutException e) {
	    	throw new IECrashException("Internet Explorer "
	    		+ "has crashed.");
	    }
	}
	public static void submitAndExit(Form form,
			WebDriverContainer container) throws IECrashException  {
		submit(form, container);
		form.exit(container);
	}
	public static void submitAndExit(Form form,
			WebDriverContainer container,
			int seconds) throws IECrashException {
		submit(form, container, seconds);
		form.exit(container);
	}
	public boolean matches(WebDriverContainer container) {
		for (int i = 0; i < fields.length; i++) {
			if (!fields[i].matches(container)) {
				return false;
			}
		}
		return true;
	}
}
