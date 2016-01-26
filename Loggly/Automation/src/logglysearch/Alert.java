package logglysearch;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import tools.WebDriverContainer;

import java.util.Set;
import java.util.List;

/**
 * Created by andrewli on 7/29/15.
 */
public class Alert {
    private static By alertNameLocator = By.cssSelector(
        "td[class=\"alert-name-cell truncate-wrapper "
            + "tipper  sorting_1\"]");
    private static By ownerLocator = By.cssSelector(
        "td[class=\"alert-owner truncate-wrapper tipper "
            + "\"]");
    private static By intervalLocator = By.cssSelector(
        "td[class=\"alert-interval truncate-wrapper "
            + "tipper \"]");
    private static By lastRunLocator = By.cssSelector(
        "td[class=\"truncate-wrapper tipper \"]");


    private static By alertLinkLocator = By.xpath(
            "td[@class=\'alert-name-cell truncate-wrapper "
                    + "tipper  sorting_1\']/div[@class="
                    + "\'truncate-wrapper\']/a");

    private String alertName;
    private String owner;
    private String interval;
    private String lastRun;

    private String description;
    private String savedSearch;
    private String alertIf;
    private boolean sendEmail;
    private String[] recipients;
    private boolean sendToAnEndpoint;
    private String includeUpTo10Events;
    private boolean enable10Events;
    private String checkForThisConditionInterval;
    private boolean enableAlert;

    private Alert(String newAlertName,
                       String newOwner, String newInterval,
                       String newLastRun) {
        alertName = newAlertName;
        owner = newOwner;
        interval = newInterval;
        lastRun = newLastRun;
    }

    public String toString() {
        String line1 = "Alert Name: " + alertName + "\n";
        String line2 = "Owner: " + owner + "\n";
        String line3 = "Run Every: " + interval + "\n";
        String line4 = "Last Run: " + lastRun + "\n";
        String line5 = "Description: " + description + "\n";
        String line6 = "Saved Search: " + savedSearch + "\n";
        String line7 = "Alert if: " + alertIf + "\n";
        String line8 = "Send an Email checkbox: ";
        if (sendEmail) {
            line8 += "checked\n";
        }
        else {
            line8 += "unchecked\n";
        }
        String line9 = "Email addresses: ";
        if (recipients != null && recipients.length > 0) {
            line9 += recipients[0];
        }
        for (int i = 1; i < recipients.length; i++) {
            line9 += ", " + recipients[i];
        }
        line9 += "\n";
        String line10 = "Send to an Endpoint checkbox: ";
        if (sendToAnEndpoint) {
            line10 += "checked\n";
        }
        else {
            line10 += "unchecked\n";
        }
        String line11 = "Endpoint Name: ";
        line11 += includeUpTo10Events + "\n";
        String line12 = "Include up to 10 recent events checkbox: ";
        if (enable10Events) {
        	line12 += "checked\n";
        }
        else {
        	line12 += "unchecked\n";
        }
        String line13 = "Check for this condition every: ";
        line13 += checkForThisConditionInterval + "\n";
        String line14 = "Enable this alert checkbox: ";
        if (enableAlert) {
            line14 += "checked\n";
        }
        else {
            line14 += "unchecked\n";
        }
        return line1 + line2 + line3 + line4 + line5
            + line6 + line7 + line8 + line9 + line10
            + line11 + line12 + line13 + line14;
    }

    public static Alert parse(WebDriverContainer container,
            WebElement tableRow) {
        WebElement alertNameLink = tableRow.findElement(
                alertNameLocator);
        String alertName = alertNameLink.getText();
        String owner = tableRow.findElement(
                ownerLocator).getText();
        String interval = tableRow.findElement(
                intervalLocator).getText();
        String lastRun = tableRow.findElement(
                lastRunLocator).getText();
        Alert output = new Alert(alertName, owner, interval,
            lastRun);

        alertNameLink = tableRow.findElement(
            alertLinkLocator);

        String parentHandle = container.getWindowHandle();
        Set<String> handles = container.getWindowHandles();
        
        boolean notPassed = true;
        
        for (int i = 0; notPassed; i++) {
        	try {
                container.perform(alertNameLink, "w");
                container.subWindow(handles);

                output.description = container.findElement(
                	By.id("id_description"), 30)
                		.getAttribute("value");
                notPassed = false;
        	}
        	catch (WebDriverException e) {
        		container.switchTo().window(parentHandle);
        		System.out.println("Error in subwindow.");
        		if (i > 50) {
        			throw new RuntimeException(
        				"The format is incompatible.");
        		}
        	}
        }
        WebElement selectField = container.findElement(
            By.id("id_savedsearch"));
        Select select = new Select(selectField);
        output.savedSearch = select.getFirstSelectedOption().getText();

        String count;
        selectField = container.findElement(
                By.id("id_trigger_type"));
        select = new Select(selectField);
        count = select.getFirstSelectedOption().getText();
        String relative;
        selectField = container.findElement(
                By.id("id_check"));
        select = new Select(selectField);
        relative = select.getFirstSelectedOption().getText();
        String firstNumber = container.findElement(By.id(
                "id_value")).getAttribute("value");
        String lastNumber = container.findElement(By.id(
                "id_window")).getAttribute("value");
        String units;
        selectField = container.findElement(
                By.id("id_window_interval"));
        select = new Select(selectField);
        units = select.getFirstSelectedOption().getText();
        output.alertIf = count + " is " + relative + " "
            + firstNumber + " within " + lastNumber + " "
            + units;

        String checked = container.findElement(By.id(
            "id_enable_email")).getAttribute("checked");
        output.sendEmail = checked != null && (
            checked.equalsIgnoreCase("checked")
            || checked.equalsIgnoreCase("true"));

        List<WebElement> elements = container.findElements(
            By.xpath("//ul[@class=\'select2-choices\']"
                + "/li[@class=\'select2-search-choice\']"
                + "/div"));
        output.recipients = new String[elements.size()];
        for (int i = 0; i < elements.size(); i++) {
            output.recipients[i] = elements.get(i).getText();
        }

        checked = container.findElement(By.id("id_enable"
            + "_endpoint")).getAttribute("checked");
        output.sendToAnEndpoint = checked != null && (
        	checked.equalsIgnoreCase("checked")
        	|| checked.equalsIgnoreCase("true"));
        
        selectField = container.findElement(
                By.id("id_alert_endpoints"));
        select = new Select(selectField);
        output.includeUpTo10Events = select
            .getFirstSelectedOption().getText();

        checked = container.findElement(By.id("id_event"
            + "_display")).getAttribute("checked");
        output.enable10Events = checked != null && (
            checked.equalsIgnoreCase("checked")
            || checked.equalsIgnoreCase("true"));
        
        String numberCheckCondition;
        numberCheckCondition = container.findElement(
                By.id("id_interval")).getAttribute("value");
        String unitsCheckCondition;
        selectField = container.findElement(
                By.id("id_interval_scale"));
        select = new Select(selectField);
        unitsCheckCondition = select.getFirstSelectedOption().getText();
        output.checkForThisConditionInterval = numberCheckCondition
            + " " + unitsCheckCondition;

        checked = container.findElement(By.id("id_enable"
        	+ "_alert")).getAttribute("checked");
        output.enableAlert = checked != null && (
            checked.equalsIgnoreCase("checked")
            || checked.equalsIgnoreCase("true"));

        container.closeDriver();
        container.switchTo().window(parentHandle);
        return output;
    }
/*
Search Name: CRASHPFI-CHARGE-ERROR-AM
Time Range:  Last 30 minutes
Owner:  tpconnxtSW
Alerts: 1
Search String:   json.Hostname:*PFI01 json.Severity:ERROR ("key err_processor value charge-cleanup-processor" or "key err_processor value charge-correlation-processor" or "key err_processor value charge-assemble-processor")
Filters:   json.process : crash AND json.Severity : ERROR
*/
}
