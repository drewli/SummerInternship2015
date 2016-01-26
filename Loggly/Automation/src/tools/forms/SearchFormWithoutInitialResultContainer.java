package tools.forms;

import tools.WebDriverContainer;
import tools.fields.Field;
import tools.locators.ExitButtonLocator;
import tools.locators.RefreshedLocator;
import tools.locators.ResultLocator;
import tools.locators.SubmitButtonLocator;

public class SearchFormWithoutInitialResultContainer extends SearchForm {
	
	public SearchFormWithoutInitialResultContainer(
			SubmitButtonLocator submit, Field[] things,
			RefreshedLocator refreshed,
			ResultLocator result) {
		super(submit, things, refreshed, result);
	}

	public SearchFormWithoutInitialResultContainer(
			SubmitButtonLocator submit,
			ExitButtonLocator exit, Field[] things,
			RefreshedLocator refreshed,
			ResultLocator result) {
		super(submit, exit, things, refreshed, result);
	}

	public boolean submit(WebDriverContainer container) {
		return submitWithoutInitialResultContainer(
			container);
	}
}
