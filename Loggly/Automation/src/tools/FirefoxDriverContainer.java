package tools;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class FirefoxDriverContainer extends WebDriverContainer {

	public FirefoxDriverContainer() {
		super(browse());
	}
	
	public FirefoxDriverContainer(String website) {
		super(browse(website));
	}

	public FirefoxDriverContainer(FirefoxDriver newDriver) {
		super(newDriver);
	}

	@Override
	public WebDriverContainer restart() {
		getDriver().quit();
		setDriver(browse());
		return this;
	}

	@Override
	public WebDriverContainer restart(String link) {
		// TODO Auto-generated method stub
		return null;
	}
	
	protected static FirefoxDriver browse() {
		FirefoxDriver driver = new FirefoxDriver();
		driver.manage().window().maximize();
		return driver;
	}
	
	protected static FirefoxDriver browse(String website) {
		FirefoxDriver driver = new FirefoxDriver();
		driver.navigate().to(website);
		driver.manage().window().maximize();
		return driver;
	}

}
