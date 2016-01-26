package runnables;

import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import tools.FirefoxDriverContainer;
import tools.WebDriverContainer;

public class Test {
	public static void main(String[] args) {
		WebDriverContainer container = new FirefoxDriverContainer("http://www.google.com");
		WebElement inputBox = container.findElement(By.id("lst-ib"));
		inputBox.sendKeys("George Washington");
		WebElement button = container.findElement(By.name("btnG"));
		button.click();
		Set<String> currentHandles = container.getWindowHandles();
		Actions act = new Actions(container.getDriver());
		WebElement link = container.findElement(By.id("vs0p1"));
		act.contextClick(link);
		act.sendKeys("w").perform();
		container.subWindow(currentHandles);
		Set<String> newHandles = container.getWindowHandles();
		for (String handle : currentHandles) {
			System.out.println(handle);
		}
		System.out.println("-");
		System.out.println("-");
		for (String handle : newHandles) {
			System.out.println(handle);
		}
		System.out.println(container.findElement(By.xpath("//a")).getAttribute("href"));
	}
}
