package values;

import org.openqa.selenium.By;

import tools.fields.Field;
import tools.fields.InputField;
import tools.forms.Form;
import tools.locators.SubmitButtonLocator;

public class GeneralValues {
	public static String link = "https://cmdcloudprod.loggly.com/";
	
	public static Form loginForm(String username, String password) {
		SubmitButtonLocator submitButton = new SubmitButtonLocator(
			By.xpath("//form[@id=\'login-form\']/div["
				+ "@class=\'action-btns\']/button[@class"
				+ "=\'submit-login btn btn-primary\']"));
		Field[] fields = new Field[] {
			new InputField(By.id("id_username"), username),
			new InputField(By.id("id_password"), password)
		};
		return new Form(submitButton, fields);
	}
	
	private static String logglyUsername = "motani";
	private static String logglyPassword = "password#2015";
	
	public static Form loginForm = loginForm(logglyUsername,
		logglyPassword);
}
