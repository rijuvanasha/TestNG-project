package pages;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.relevantcodes.extentreports.LogStatus;

public class HomePage extends BasePage {

	By translationLink = By.id("translation-btn");
	By litePlanTitle = By.id("name-lite");
	By classicPlanTitle = By.id("name-classic");
	By premiumPlanTitle = By.id("name-premium");
	By subscriptionPageTitle = By.xpath("//h2[text()='Choose Your Plan']");
	By packageTypes = By
			.xpath("//*[@id='main']/div/div[@class='plan-section']/div[@class='plan-row plan-header sticky']/div/div/strong");
	By countryNavigationLink = By.xpath("//*[@id='arrow']/img");
	By countryName = By.id("country-name");
	By countryPopupTitle = By.id("country-title");
	By countryList = By.xpath("//div[@id='country-selct']/a");
	By currencyValue = By.xpath("//*[@id='currency-lite']/i");

	String COUNTRY_SELECTION = "//div[@id='country-selct']/a['%s']";
	String COUNTRY_NAME = "//div[@id='country-selct']/a['%s']/span[contains(@id,'contry-lable')]";
	String COUNTRY_SELECTION_LINK = "//span[@id='country-name'][contains(text(),'%s')]";
	String PACKAGE_SELECTION_PRICELIST = "//*[@id='currency-%s']/b";
	

	WebDriver driver;
	WebDriverWait wait;

	public HomePage(WebDriver driver) {
		this.driver = driver;
		wait = new WebDriverWait(driver, 5);
	}

	public String getTitle() {
		return driver.getTitle();
	}

	public boolean getTranslationLink() {
		wait.until(ExpectedConditions.visibilityOfElementLocated(translationLink));
		return driver.findElement(translationLink).isDisplayed();
	}

	public void clickTranslationLink() {
		wait.until(ExpectedConditions.elementToBeClickable(translationLink));
		driver.findElement(translationLink).click();
	}

	public List<String> getSubscriptionPackageType() {
		wait.until(ExpectedConditions.visibilityOfElementLocated(subscriptionPageTitle));
		List<WebElement> subscriptionTypes = driver.findElements(packageTypes);
		List<String> subscriptionPackageTypes = new ArrayList<>();
		for (WebElement packageResult : subscriptionTypes) {
			subscriptionPackageTypes.add(packageResult.getText());
		}
		return subscriptionPackageTypes;
	}

	public void clickCountryNaviagtionLink() {
		wait.until(ExpectedConditions.elementToBeClickable(countryNavigationLink));
		driver.findElement(countryNavigationLink).click();
	}

	public boolean validateCurrencyAndPricingForAllCountries() {
		boolean validationCompleted = false;
		wait.until(ExpectedConditions.visibilityOfElementLocated(countryPopupTitle));
		int countrySize = driver.findElements(countryList).size();
		for (int i = 1; i <= countrySize; i++) {
			if(i>1) {
				clickCountryNaviagtionLink();	
			}
			WebElement country = driver.findElement(By.xpath(String.format((COUNTRY_SELECTION), i)));
			WebElement countryNames = driver.findElement(By.xpath(String.format((COUNTRY_NAME), i)));
			String countryName = countryNames.getText();
			country.click();
			logger.log(LogStatus.INFO, "Clicked on " + countryName +" country");
			String currencyType = getCurrencyValue(countryName);
			getPriceForAllPackages(currencyType);
			validationCompleted = true;
		}
		return validationCompleted;

	}

	public String getCurrencyValue(String countryName) {
		wait.until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath(String.format((COUNTRY_SELECTION_LINK), countryName))));
		String countryCurrency = driver.findElement(currencyValue).getText();
		logger.log(LogStatus.INFO, "Currency name : " + countryCurrency);
		return countryCurrency;
	}

	public void getPriceForAllPackages(String currency) {
		List<String> subscription = getSubscriptionPackageType();
		String packageType;
		String packagePriceList;
		for (int i = 0; i < subscription.size(); i++) {
			packageType = subscription.get(i).toLowerCase();
			packagePriceList=driver.findElement(By.xpath(String.format(PACKAGE_SELECTION_PRICELIST, packageType))).getText();
			logger.log(LogStatus.INFO,String.format("Price Value for '%s' package is '%s' '%s' : ",packageType,
					packagePriceList,currency ));
		}
	}

}
