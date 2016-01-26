package tools;

import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.TargetLocator;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class WebDriverContainer {
	private WebDriver driver;
	
	protected WebDriverContainer(WebDriver newDriver) {
		driver = newDriver;
	}
	
	public WebDriver getDriver() {
		return driver;
	}
	
	protected void setDriver(WebDriver newDriver) {
		driver = newDriver;
	}
	
	public abstract WebDriverContainer restart();
	
	public abstract WebDriverContainer restart(String link);
	
	public void closeDriver() {
		driver.close();
	}
	
	public void quitDriver() {
		driver.quit();
	}
	
	public void click(By locator) {
		findElement(locator).click();
	}
	
	public void clickInvisibleElement(WebElement element) {
	    JavascriptExecutor js;
	    js = (JavascriptExecutor) this.getDriver();
	    js.executeScript("arguments[0].click();", element);
	}
	
	public String getElementText(WebElement element) {
	    JavascriptExecutor js;
	    js = (JavascriptExecutor) this.getDriver();
	    return ((String) js.executeScript("return jQuery("
	    	+ "arguments[0]).text();", element)).trim();
	}
	
    public WebElement findElement(By by) {
    	return (new WebDriverWait(driver, 60)).until(
                new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver input) {
                return input.findElement(by);
            }
        });
    }
	
    public WebElement findElement(By by, int seconds) {
    	return (new WebDriverWait(driver, seconds)).until(
                new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver input) {
                return input.findElement(by);
            }
        });
    }
	
    public WebElement findElement(WebElement element,
    		By by) {
    	final WebElement host = element;
    	return (new WebDriverWait(driver, 60)).until(
                new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver input) {
                return host.findElement(by);
            }
        });
    }
	
    public WebElement findElement(WebElement element,
    		By by, int seconds) {
    	final WebElement host = element;
    	return (new WebDriverWait(driver, seconds)).until(
                new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver input) {
                return host.findElement(by);
            }
        });
    }
	
    public List<WebElement> findElements(By by) {
    	return driver.findElements(by);
    }
    
    public void subWindow(Set<String> handlesParam) {
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

    public void perform(WebElement element,
            CharSequence command) {
        Actions actions = new Actions(driver);
        actions.contextClick(element);
        actions.sendKeys(command).perform();
    }
    
    public TargetLocator switchTo() {
    	return driver.switchTo();
    }
    
    public String getWindowHandle() {
    	return driver.getWindowHandle();
    }
    
    public Set<String> getWindowHandles() {
    	return driver.getWindowHandles();
    }
    

    
    
}
