package tests;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;
import com.relevantcodes.extentreports.LogStatus;
import pages.BasePage;
import pages.HomePage;

public class HomePageTest extends BasePage {

	List<String> packageTypes;

	@Test(priority = 0)
	public void verifyTitle() {
		HomePage homePage = new HomePage(driver);
		logger.log(LogStatus.INFO, "Checking Title Web Page");
		Assert.assertEquals(homePage.getTitle(), "STC TV | موقع الأفلام والمسلسلات المفضل للجميع",
				"Title does not match");
		logger.log(LogStatus.PASS, "Title Page Matched");
	}

	@Test(priority = 1)
	public void verifySubscriptionPackageTypes() {
		HomePage homePage = new HomePage(driver);
		logger.log(LogStatus.INFO, "Check english translation link is present");
		Assert.assertTrue(homePage.getTranslationLink());
		homePage.clickTranslationLink();
		packageTypes = homePage.getSubscriptionPackageType();
		Assert.assertTrue(getPackageTypes());
	}

	@Test(priority = 2)
	public void verifyPriceAndCurrency() {
		HomePage homePage = new HomePage(driver);
		homePage.clickCountryNaviagtionLink();
		Assert.assertTrue(homePage.validateCurrencyAndPricingForAllCountries());
	}

	private boolean getPackageTypes() {
		boolean isAllPackageTypesVerified = false;
		int totalPackages = packageTypes.size();
		for (int packageType = 0; packageType < totalPackages; packageType++) {
			logger.log(LogStatus.INFO,
					"Subscription Package Type: " + packageTypes.get(packageType) + " section is present");
			isAllPackageTypesVerified = true;
		}
		return isAllPackageTypesVerified;
	}
}
