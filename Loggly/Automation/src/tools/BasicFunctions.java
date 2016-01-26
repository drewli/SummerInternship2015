package tools;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Scanner;
import java.util.Set;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class BasicFunctions {

    /**
     * Creates a file or adds a line to a file if the file
     * has already been created.
     * @param fileName Name of the file we want to write to
     * @param message Message that we want to add to the file.
     */
    public static void writeToFile(String fileName,
    		String message) {
    	/*
    	 * Reads in all the lines of a file and stores them.
    	 * If the file does not exist, we don't store any
    	 * lines of text.
    	 */
    	ArrayList<String> lines = new ArrayList<String>();
    	File file = new File(fileName);
    	Scanner fileReader;
    	try {
			fileReader = new Scanner(file);
			while (fileReader.hasNextLine()) {
				lines.add(fileReader.nextLine());
			}
		} catch (FileNotFoundException e) {}
    	/*
    	 * Rewrites what was stored (so the text is not
    	 * overwritten) and writes the new line of text.
    	 */
    	try {
    		if (fileName.lastIndexOf(".") < 0) {
    			fileName += ".txt";
    		}
			PrintWriter writer = new PrintWriter(
    			new FileWriter(fileName));
    		for (int i = 0; i < lines.size(); i++) {
    			writer.write(lines.get(i));
    			writer.println();
    		}
    		DateFormat dateFormat = new SimpleDateFormat(
    			"MM/dd/yyyy HH:mm:ss");
    		Date date = new Date();
    		String now = dateFormat.format(date);
    		writer.write(now + "\t\t" + message);
    		writer.flush();
    		writer.close();
    	}
    	catch (IOException e) {}
    	
    }

    // Logs out if we're currently signed in.
    /**
     * @param The WebDriver that represents the browser which we attempt a logout.
     */
    /**
     * @param milliseconds number of milliseconds that we will pause.
     */
    public static void pause(int milliseconds) {
    	try {
    		Thread.sleep(milliseconds);
    	}
    	catch (Throwable t) {}
    }
    
    /**
     * @param driver The browser in which we will do the right-clicking
     * @param element The element that we will right-click.
     * @param index The number of times we click the down key.
     */
    public static void rightClick(WebDriver driver,
    		WebElement element, int index) {
    	Actions actions = new Actions(driver);
    	actions.contextClick(element).build().perform();
    	for (int i = 0; i < index + 1; i++) {
        	element.sendKeys(Keys.ARROW_DOWN);
    	}
    	element.sendKeys(Keys.RETURN);
    }
    
    public static void subWindow(WebDriver driver,
    		Set<String> handlesParam) {
    	final Set<String> handles = handlesParam;
        String subWindowHandler = (new WebDriverWait(driver, 10)).until(
        	new ExpectedCondition<String>() {
                public String apply(WebDriver input) {
                    Set<String> newHandles = input.getWindowHandles();
                    newHandles.removeAll(handles);
                    if (newHandles.size() > 0) {
                        return newHandles.iterator().next();
                    }
                    return null;
                }
            }
        );
        driver.switchTo().window(subWindowHandler);
    }
    
}
