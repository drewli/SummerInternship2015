package runnables;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import logglysearch.Alert;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import tools.BasicFunctions;
import tools.FirefoxDriverContainer;
import tools.WebDriverContainer;
import values.GeneralValues;

/**
 * Created by andrewli on 7/29/15.
 */
public class CollectAlerts {
    public static void main(String[] args) {
        File theDir = new File("alerts");

        // if the directory does not exist, create it
        if (!theDir.exists() || !theDir.isDirectory()) {
            try {
                theDir.mkdir();
            } 
            catch (SecurityException se) {}      
        }
        
        // Open Firefox Driver and navigates to Loggly
        WebDriverContainer container = new FirefoxDriverContainer(
                GeneralValues.link);

        // Logs into the page.
        GeneralValues.loginForm.submit(container);

        // Clicks on the search tab.
        container.click(
                By.cssSelector("a[href=\"/alerts/\"]"));

        // Obtains the table (which contains each row
        // that represents a search)
        WebElement table = container.findElement(
            By.xpath("//div[@class=\'table-container\']"
                + "/div/table"));
        ArrayList<Alert> alerts = new ArrayList<Alert>();
        container.findElement(table, By.xpath(
                "//tbody[@role=\'alert\']/tr"));
        boolean notLastPage = true;
        while (notLastPage) {
            List<WebElement> rows = table.findElements(
                By.xpath("//tbody[@role=\'alert\']/tr"));
            for (int i = 0; i < rows.size(); i++) {
                alerts.add(Alert.parse(container,
                    rows.get(i)));
            }
            WebElement nextPage = null;
            try {
                nextPage = container.findElement(By.xpath("//li["
                        + "@class=\'next\']/a"), 10);
                nextPage.click();
            } catch (WebDriverException e) {
                notLastPage = false;
            }
            boolean waitingForNextPage = notLastPage;
            while (waitingForNextPage) {
                try {
                    rows.get(0).getAttribute("class");
                    BasicFunctions.pause(1500);
                } catch (WebDriverException e) {
                    waitingForNextPage = false;
                }
            }
        }

		DateFormat dateFormat = new SimpleDateFormat(
			"MM_dd_yyyy_HH_mm_ss");
		Date date = new Date();
		String now = dateFormat.format(date);
		String fileName = "alerts/alert_" + now + ".txt";
		try {
		    PrintWriter writer = new PrintWriter(
			    new FileWriter(fileName));
		    for (int i = 0; i < alerts.size(); i++) {
		    	writer.write(alerts.get(i).toString());
		    	writer.println();
		    	writer.println();
		    }
		    writer.flush();
		    writer.close();
		}
		catch (IOException e) {
			System.out.println("IO Exception");
		}
	    
	    container.quitDriver();
    }
}
