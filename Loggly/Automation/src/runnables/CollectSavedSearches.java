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

import logglysearch.SavedSearch;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import tools.*;
import values.*;

public class CollectSavedSearches {
	public static void main2(String[] args) {
        File theDir = new File("searches");

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
	    	By.cssSelector("a[href=\"/search\"]"));
	    
	    WebElement event = container.findElement(By.xpath(
	    	"//div[@class=\'search-events content\']/div"));
	    
	    // Clicks on the star button.
	    WebElement element = container.findElement(
	    	By.xpath("//div[@data-title=\'Saved Searches\']"
	    		+ "/button[@class=\'btn btn-dropdown "
	    		+ "dropdown-toggle\']"));
	    element.click();
	    
	    
	    // Clicks on the Manage Saved Searches
	    element = element.findElement(By.xpath(".."));
	    element = element.findElement(By.xpath("//div["
	    	+ "@data-target=\'#manage-saved-search-modal\']"
	    	+ "/a[@class=\'saved-search-menu-text\']"));
	    container.clickInvisibleElement(element);

	    // Obtains the table body (which contains each row
	    // that represents a search)
	    WebElement searchModal = container.findElement(
	    	By.id("manage-saved-search-modal"));
	    WebElement tableContainer = container.findElement(
	    	searchModal, By.cssSelector("span[class="
	    		+ "\"table-container\"]"));
	    WebElement table = container.findElement(
	    	tableContainer, By.cssSelector("table[class=\""
	    		+ "table saved-search-table dataTable\"]"));
	    WebElement tableBody = container.findElement(table,
	    	By.cssSelector("tbody[role=\"alert\"]"));

	    
	    /*
	     * Captures the Search Name, Time Range, Owner, and
	     * Alerts info for rows from all pages.
	     */
	    ArrayList<SavedSearch> searches; // Row storage
	    searches = new ArrayList<SavedSearch>();
	    boolean notLastPage = true;
	    // Goes through each page.
	    while (notLastPage) {
	    	/*
	    	 * Captures the Search Name, Time Range, Owner,
	    	 * and Alerts info on the current page.
	    	 */
	        List<WebElement> rowsOdd = tableBody.findElements(
	    	    By.cssSelector("tr[class=\"table-row "
		    	    + "truncate-row odd\"]"));
	        List<WebElement> rowsEven = tableBody.findElements(
		        By.cssSelector("tr[class=\"table-row "
			        + "truncate-row even\"]"));
	        int totalSize = rowsOdd.size() + rowsEven.size();
	        ArrayList<WebElement> rows = new ArrayList<WebElement>(
	    	    totalSize);
	        for (int i = 0; i < totalSize; i++) {
	    	    if (i % 2 == 0) {
	    		    rows.add(rowsOdd.get(0));
	    		    rowsOdd.remove(0);
	    	    }
	    	    else {
	    		    rows.add(rowsEven.get(0));
	    		    rowsEven.remove(0);
	    	    }
	        }
	        
	        /* Stores the current page's rows onto the storage
	         * for all rows
	         */
	        for (int i = 0; i < rows.size(); i++) {
	        	searches.add(SavedSearch.parse(container,
	        		rows.get(i)));
	        }
	        
	        // Clicks on the next page button.
	        try {
	            WebElement nextButton = element.findElement(
	            	    By.xpath("//li[@class=\'next\']/a"));
	            nextButton.click();
	        }
	        catch (WebDriverException e) {
	            notLastPage = false;
	        }
	        
	        // Waits for the next page
	        boolean waitingForNewPage = notLastPage;
	        while (waitingForNewPage) {
	            try {
	                rows.get(0).getAttribute("class");
	                BasicFunctions.pause(1000);
	    	    }
	            catch (WebDriverException e) {
	            	waitingForNewPage = false;
	    	    }
	            catch (Exception e) {}
	        }
	    }
	    // Exits out of the search modal.
	    List<WebElement> links = table.findElements(
	    	By.xpath(".//a"));
	    for (int i = 0; i < links.size(); i++) {
	    	if (links.get(i).getText().equals(searches
	    			.get(searches.size() - 1)
	    			.getSearchName())) {
	    		links.get(i).click();
	    		i = links.size();
	    	}
	    }
	    
	    // Waits for new events to load.
    	boolean waitingForEvents = true;
    	while (waitingForEvents) {
    		try {
    			event.getAttribute("class");
    		}
    		catch (WebDriverException e) {
    			waitingForEvents = false;
    			event = container.findElement(By.xpath(
    			    "//div[@class=\'search-events "
    					+ "content\']/div"));
    		}
    	}
	    for (int i = 0; i < searches.size(); i++) {
		    // Clicks on the star button.
		    element = container.findElement(
		    	By.xpath("//div[@data-title=\'Saved "
		    		+ "Searches\']/button[@class=\'btn "
		    		+ "btn-dropdown dropdown-toggle\']"));
		    element.click();
		    
		    // Clicks on the Manage Saved Searches
		    element = element.findElement(By.xpath(".."));
		    element = element.findElement(By.xpath("//div["
		    	+ "@data-target=\'#manage-saved-search"
		    	+ "-modal\']/a[@class=\'saved-search-menu"
		    	+ "-text\']"));
		    element.findElement(By.xpath("//a"));
		    container.clickInvisibleElement(element);
		    
		    searchModal = container.findElement(
			    By.id("manage-saved-search-modal"));
			tableContainer = container.findElement(
			    searchModal, By.cssSelector("span[class="
			    	+ "\"table-container\"]"));
			boolean waitingForNewTable = true;
			while (waitingForNewTable) {
				try {
					table.getAttribute("class");
					BasicFunctions.pause(1000);
				}
				catch (WebDriverException e) {
					waitingForNewTable = false;
				}
			}
			table = container.findElement(
			    tableContainer, By.cssSelector("table[class=\""
			    	+ "table saved-search-table dataTable\"]"));
			tableBody = container.findElement(table,
			    By.cssSelector("tbody[role=\"alert\"]"));
			
			SavedSearch currentSearch = searches.get(i);
	    	
	    	boolean notFoundOnPage = true;
	    	while (notFoundOnPage) {
	    		List<WebElement> elements = table.findElements(
	    			By.xpath(".//a"));
	    		for (int j = 0; j < elements.size(); j++) {
	    			if (elements.get(j).getText().equals(
	    				currentSearch.getSearchName())) {
	    				    notFoundOnPage = false;
	    				    elements.get(j).click();
	    			}
	    		}
	    		if (notFoundOnPage) {
		            // Clicks on the next page button.
		            try {
		                WebElement nextButton;
		                nextButton = element.findElement(
		            	    By.xpath("//li[@class=\'next\']"
		            	    	+ "/a"));
		                nextButton.click();
		            }
		            catch (WebDriverException e) {
		                notLastPage = false;
		            }
		            // Waits for the next page
		            boolean waitingForNewPage = notLastPage;
		            while (waitingForNewPage) {
		                try {
		                    elements.get(0).getAttribute(
		                    	"class");
		                    BasicFunctions.pause(1000);
		    	        }
		                catch (WebDriverException e) {
		            	    waitingForNewPage = false;
		    	        }
		            }
	    		}
	    	}
	    	
	    	waitingForEvents = true;
	    	while (waitingForEvents) {
	    		try {
	    			event.getAttribute("class");
	    		}
	    		catch (WebDriverException e) {
	    			waitingForEvents = false;
	    			event = container.findElement(By.xpath(
	    			    "//div[@class=\'search-events "
	    					+ "content\']/div"));
	    		}
	    	}
	    	
	    	WebElement searchStringElement;
	    	searchStringElement = container.findElement(
	    		By.cssSelector("input[class=\"search-input "
	    			+ "main-searchbox hover-change-context "
	    			+ "feature-tour-step2\"]"));
	    	String searchString;
	    	searchString = searchStringElement.getAttribute(
	    		"value");
	    	searches.get(i).setSearchString(searchString);
	    	
	    	List<WebElement> filterElements;
	    	filterElements = container.findElements(
	    		By.cssSelector("span[class=\"filter-text "
	    			+ "ng-binding\"]"));
	    	String[] filters = new String[filterElements
	    	    .size()];
	    	for (int j = 0; j < filterElements.size(); j++) {
	    		filters[j] = container.getElementText(
	    			filterElements.get(j));
	    		if (filters[j] == null || filters[j]
	    				.length() == 0) {
	    			System.out.println("Text is null.");
	    			System.exit(1);
	    		}
	    	}
	    	
	    	searches.get(i).setFilters(filters);
	    	
	    }

		DateFormat dateFormat = new SimpleDateFormat(
			"MM_dd_yyyy_HH_mm_ss");
		Date date = new Date();
		String now = dateFormat.format(date);
		String fileName = "searches/search_" + now + ".txt";
		try {
		    PrintWriter writer = new PrintWriter(
			    new FileWriter(fileName));
		    for (int i = 0; i < searches.size(); i++) {
		    	writer.write(searches.get(i).toString());
		    	writer.println();
		    	writer.println();
		    }
		    writer.flush();
		    writer.close();
		}
		catch (IOException e) {
			System.out.println("IO Exception");
		}

    	
	    for (int i = 0; i < searches.size(); i++) {
	    	System.out.println(searches.get(i));
	    }
	    
	    
	}
	

	public static void main(String[] args) {
        File theDir = new File("searches");

        // if the directory does not exist, create it
        if (!theDir.exists()) {
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
	    	By.cssSelector("a[href=\"/search\"]"));
	    
	    WebElement event = container.findElement(By.xpath(
	    	"//div[@class=\'search-events content\']/div"));
	    
	    // Clicks on the star button.
	    WebElement element = container.findElement(
	    	By.xpath("//div[@data-title=\'Saved Searches\']"
	    		+ "/button[@class=\'btn btn-dropdown "
	    		+ "dropdown-toggle\']"));
	    element.click();
	    
	    
	    // Clicks on the Manage Saved Searches
	    element = element.findElement(By.xpath(".."));
	    element = element.findElement(By.xpath("//div["
	    	+ "@data-target=\'#manage-saved-search-modal\']"
	    	+ "/a[@class=\'saved-search-menu-text\']"));
	    container.clickInvisibleElement(element);

	    // Obtains the table body (which contains each row
	    // that represents a search)
	    WebElement searchModal = container.findElement(
	    	By.id("manage-saved-search-modal"));
	    WebElement tableContainer = container.findElement(
	    	searchModal, By.cssSelector("span[class="
	    		+ "\"table-container\"]"));
	    WebElement table = container.findElement(
	    	tableContainer, By.cssSelector("table[class=\""
	    		+ "table saved-search-table dataTable\"]"));
	    WebElement tableBody = container.findElement(table,
	    	By.cssSelector("tbody[role=\"alert\"]"));

	    
	    /*
	     * Captures the Search Name, Time Range, Owner, and
	     * Alerts info for rows from all pages.
	     */
	    ArrayList<SavedSearch> searches; // Row storage
	    searches = new ArrayList<SavedSearch>();
	    boolean notLastPage = true;
	    // Goes through each page.
	    while (notLastPage) {
	    	/*
	    	 * Captures the Search Name, Time Range, Owner,
	    	 * and Alerts info on the current page.
	    	 */
	        List<WebElement> rowsOdd = tableBody.findElements(
	    	    By.cssSelector("tr[class=\"table-row "
		    	    + "truncate-row odd\"]"));
	        List<WebElement> rowsEven = tableBody.findElements(
		        By.cssSelector("tr[class=\"table-row "
			        + "truncate-row even\"]"));
	        int totalSize = rowsOdd.size() + rowsEven.size();
	        ArrayList<WebElement> rows = new ArrayList<WebElement>(
	    	    totalSize);
	        for (int i = 0; i < totalSize; i++) {
	    	    if (i % 2 == 0) {
	    		    rows.add(rowsOdd.get(0));
	    		    rowsOdd.remove(0);
	    	    }
	    	    else {
	    		    rows.add(rowsEven.get(0));
	    		    rowsEven.remove(0);
	    	    }
	        }
	        
	        /* Stores the current page's rows onto the storage
	         * for all rows
	         */
	        for (int i = 0; i < rows.size(); i++) {
	        	SavedSearch savedSearch = SavedSearch.parse(
	        		container, rows.get(i));
	        	searches.add(savedSearch);
	        	List<WebElement> links;
	        	links = rows.get(i).findElements(By.xpath(
	        		"//td[@class=\'saved-search-name"
	        			+ "-cell truncate-wrapper "
	        			+ "tipper \']/div[@class=\'"
	        			+ "truncate-wrapper\']/span/a"));
	        	WebElement link = null;
	        	if (container.getElementText(
	        		links.get(i)).equals(savedSearch
	        		.getSearchName())) {
	        		    link = links.get(i);
	        	}
	        	else {
	        		for (WebElement newLink : links) {
	    	        	if (container.getElementText(
	    		        	links.get(i)).equals(savedSearch
	    		    	    .getSearchName())) {
	    		        		link = newLink;
	    		        }
	        		}
	        	}
	        	
	        	container.clickInvisibleElement(link);
	        	
	        	boolean waitingForEvents = true;
		    	while (waitingForEvents) {
		    		try {
		    			event.getAttribute("class");
		    			BasicFunctions.pause(1500);
		    		}
		    		catch (WebDriverException e) {
		    			waitingForEvents = false;
		    		}
		    	}
		    	event = container.findElement(
		    		By.xpath("//div[@class=\'search"
		    			+ "-events content\']/div"));
		    	
		    	WebElement searchStringElement;
		    	searchStringElement = container.findElement(
		    		By.cssSelector("input[class=\"search"
		    			+ "-input main-searchbox hover-change"
		    			+ "-context feature-tour-step2\"]"));
		    	String searchString;
		    	searchString = searchStringElement.getAttribute(
		    		"value");
		    	savedSearch.setSearchString(searchString);
		    	
		    	List<WebElement> filterElements;
		    	filterElements = container.findElements(
		    		By.cssSelector("span[class=\"filter"
		    			+ "-text ng-binding\"]"));
		    	String[] filters = new String[filterElements
		    	    .size()];
		    	for (int j = 0; j < filterElements.size(); j++) {
		    		filters[j] = container.getElementText(
		    			filterElements.get(j));
		    		if (filters[j] == null || filters[j]
		    				.length() == 0) {
		    			System.exit(1);
		    		}
		    	}
		    	
		    	savedSearch.setFilters(filters);
	        }
	        
	        // Clicks on the next page button.
	        try {
	            WebElement nextButton = element.findElement(
	            	    By.xpath("//li[@class=\'next\']/a"));
	            container.clickInvisibleElement(nextButton);
	        }
	        catch (WebDriverException e) {
	            notLastPage = false;
	        }
	        
	        // Waits for the next page
	        boolean waitingForNewPage = notLastPage;
	        while (waitingForNewPage) {
	            try {
	                rows.get(0).getAttribute("class");
	                BasicFunctions.pause(1000);
	    	    }
	            catch (WebDriverException e) {
	            	waitingForNewPage = false;
	    	    }
	            catch (Exception e) {}
	        }
	    }

		DateFormat dateFormat = new SimpleDateFormat(
			"MM_dd_yyyy_HH_mm_ss");
		Date date = new Date();
		String now = dateFormat.format(date);
		String fileName = "searches/search_" + now + ".txt";
		try {
		    PrintWriter writer = new PrintWriter(
			    new FileWriter(fileName));
		    for (int i = 0; i < searches.size(); i++) {
		    	writer.write(searches.get(i).toString());
		    	writer.println();
		    	writer.println();
		    }
		    writer.flush();
		    writer.close();
		}
		catch (IOException e) {
			System.out.println("IO Exception");
		}

    	
	    for (int i = 0; i < searches.size(); i++) {
	    	System.out.println(searches.get(i));
	    }
	    
	    container.quitDriver();
	}
}
