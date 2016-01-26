package tools.forms;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import tools.BasicFunctions;
import tools.WebDriverContainer;
import tools.fields.Field;
import tools.locators.ErrorBoxLocator;
import tools.locators.ExitButtonLocator;
import tools.locators.SubmitButtonLocator;

public class SaveForm extends Form {
	private String logFile;
	private By errorBoxLocator;

	public SaveForm(SubmitButtonLocator submit,
			Field[] things, String logFileName,
			ErrorBoxLocator error) {
		super(submit, things);
		logFile = logFileName;
		errorBoxLocator = error.getLocator();
	}

	public SaveForm(SubmitButtonLocator submit,
			ExitButtonLocator exit, Field[] things,
			String logFileName, ErrorBoxLocator error) {
		super(submit, exit, things);
		logFile = logFileName;
		errorBoxLocator = error.getLocator();
	}
	
	@Override
	public boolean submit(WebDriverContainer container) {
		super.submit(container);
		String errorBoxMessage = "";
		try {
			WebElement element = container.findElement(
				errorBoxLocator, 15);
			errorBoxMessage = element.getText();
		}
		catch (WebDriverException e) {}
		if (errorBoxMessage.length() > 0) {
			BasicFunctions.writeToFile(logFile,
				errorBoxMessage);
            File scrFile = ((TakesScreenshot) container
        	    .getDriver()).getScreenshotAs(OutputType
        			.FILE);
            try {
			    FileUtils.copyFile(scrFile, new File(
			    	logFile));
		    } catch (IOException e) {
			    e.printStackTrace();
		    }
		}
		return false;
	}

}
