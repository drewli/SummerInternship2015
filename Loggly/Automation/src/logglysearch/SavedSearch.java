package logglysearch;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import tools.WebDriverContainer;

/**
 * This helps us format information on saved searches.
 * @author anli
 *
 */
public class SavedSearch {
	private static By searchNameLocator = By.cssSelector(
		"td[class=\"saved-search-name-cell "
			+ "truncate-wrapper tipper \"]");
	private static By timeRangeLocator = By.cssSelector(
		"td[class=\"truncate-wrapper time-range \"]");
	private static By ownerLocator = By.cssSelector(
		"td[class=\"truncate-wrapper tipper \"]");
	private static By alertsLocator = By.cssSelector(
		"td[class=\"truncate-wrapper alerts \"]");
	
	private String searchName;
	private String timeRange;
	private String owner;
	private String alerts;

	private String searchString;
	private String[] filters;
	
	public SavedSearch(String newSearchName,
			String newTimeRange, String newOwner,
			String newAlerts) {
		searchName = newSearchName;
		timeRange = newTimeRange;
		owner = newOwner;
		alerts = newAlerts;
	}
	
	public void setSearchString(String input) {
		searchString = input;
	}
	
	public void setFilters(String[] newFilters) {
		filters = new String[newFilters.length];
		for (int i = 0; i < newFilters.length; i++) {
			filters[i] = newFilters[i];
		}
	}
	
	public String getSearchName() {
		return searchName;
	}
	
	public String toString() {
		String output = "Search Name: " + searchName + "\n"
			+ "Time Range: " + timeRange + "\n" + "Owner: "
			+ owner + "\n" + "Alerts: " + alerts + "\n"
			+ "Search String: " + searchString + "\n"
			+ "Filters: ";
		int length = 0;
		if (filters != null && filters.length > 0) {
			output += filters[0];
			length = filters.length;
		}
		for (int i = 1; i < length; i++) {
			output += " AND " + filters[i];
		}
		return output;
	}
	
	public static SavedSearch parse(WebDriverContainer container,
			WebElement tableRow) {
		String searchName = container.getElementText(
			tableRow.findElement(searchNameLocator));
		String timeRange = container.getElementText(
			tableRow.findElement(timeRangeLocator));
		String owner = container.getElementText(
			tableRow.findElement(ownerLocator));
		String alerts = container.getElementText(
			tableRow.findElement(alertsLocator));
		return new SavedSearch(searchName, timeRange,
			owner, alerts);
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
