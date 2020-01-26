package stepDefinations;

import static org.junit.Assert.assertEquals;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;

import cucumber.api.java.Before;
import cucumber.api.java.en.*;
import junit.framework.Assert;
import pageObjects.AddcustomerPage;
import pageObjects.LoginPage;
import pageObjects.SearchCustomerPage;

public class Steps extends BaseClass
{
	@Before
	public void setup() throws IOException
	{
		//Logger
				logger=Logger.getLogger("nopComemrce"); //Added logger
				PropertyConfigurator.configure("Log4j.properties");//Added logger
				
				//Reading properties
				configProp=new Properties();
				FileInputStream configPropfile=new FileInputStream("config.properties");
				configProp.load(configPropfile);
				
				String br=configProp.getProperty("browser");
				
				if(br.equals("chrome"))
				{
				System.setProperty("webdriver.chrome.driver",configProp.getProperty("chromepath"));
				driver=new ChromeDriver();
				}
				else if (br.equals("firefox")) {
					System.setProperty("webdriver.gecko.driver",configProp.getProperty("firefoxpath"));
					driver = new FirefoxDriver();
				}
				else if (br.equals("ie")) {
					System.setProperty("webdriver.ie.driver",configProp.getProperty("iepath"));
					driver = new InternetExplorerDriver();
				}
				
				logger.info("******** Launching browser*********");
		
	}
	
	@Given("User Launch Chrome browser")
	public void user_Launch_Chrome_browser() {

		lp = new LoginPage(driver);
	}

	@When("User opens URL {string}")
	public void user_opens_URL(String url) {
		logger.info("********Opening URL********");
		driver.get(url);
	}

	@When("User enters Email as {string} and Password as {string}")
	public void user_enters_Email_as_and_Password_as(String email, String password) {
		logger.info("********Providing Login Details********");
		lp.setUserName(email);
		lp.setPassword(password);
	}

	@When("Click on Login")
	public void click_on_Login() {
		logger.info("********Started Login********");
		lp.clickLogin();
	}

	@Then("Page Title should be {string}")
	public void page_Title_should_be(String title) {
		
		if (driver.getPageSource().contains("Login was unsuccessful")) {			
			driver.close();
			Assert.assertTrue(false);
			logger.info("********Login Failed********");
			}
		else
		{
			Assert.assertEquals(title, driver.getTitle());
			logger.info("********Login Passed********");
		}
	}

	@When("User click on Log out link")
	public void user_click_on_Log_out_link() throws InterruptedException {
		logger.info("********Click on LogOut********");
		Thread.sleep(3000);
		lp.clickLogout();
	}

	@Then("close browser")
	public void close_browser() {
		logger.info("********Close the Browser********");
		driver.close();

	}
	
	//Customer -------------------------------------------------------
	
	@Then("User can view Dashboad")
	public void user_can_view_Dashboad() {
		logger.info("********Dashboard********");
		addCust = new AddcustomerPage(driver);
		Assert.assertEquals("Dashboard / nopCommerce administration", addCust.getPageTitle());

	}

	@When("User click on customers Menu")
	public void user_click_on_customers_Menu() throws InterruptedException {
		Thread.sleep(3000);
		addCust.clickOnCustomersMenu();

	}

	@When("click on customers Menu Item")
	public void click_on_customers_Menu_Item() throws InterruptedException {
		Thread.sleep(3000);
		addCust.clickOnCustomersMenuItem();
}

	@When("click on Add new button")
	public void click_on_Add_new_button() throws InterruptedException  {
		logger.info("********Adding new Customer********");
		Thread.sleep(3000);
		addCust.clickOnAddnew();


	}

	@Then("User can view Add new customer page")
	public void user_can_view_Add_new_customer_page() throws InterruptedException {
		logger.info("********Adding new Customer********");
		Thread.sleep(3000);
		Assert.assertEquals("Add a new customer / nopCommerce administration", addCust.getPageTitle());

		
		
	}

	@When("User enter customer info")
	public void user_enter_customer_info() throws InterruptedException {
		logger.info("********Providing new Customer details********");
		Thread.sleep(3000);
		String email = randomestring()+"@gmail.com";
		addCust.setEmail(email);
		addCust.setPassword("test123");
		addCust.setCustomerRoles("Gusest");
		Thread.sleep(3000);
		addCust.setManagerOfVendor("Vendor 2");
		addCust.setFirstName("Male");
		addCust.setLastName("Kumar");
		addCust.setDob("07/08/1993");
		addCust.setCompanyName("busyQA");
		addCust.setAdminContent("This is for testing..............");
	}

	@When("click on Save button")
	public void click_on_Save_button() throws InterruptedException {
		logger.info("********Saving new Customer********");
		addCust.clickOnSave();
		Thread.sleep(3000);
		

	}

	@Then("User can view confirmation message {string}")
	public void user_can_view_confirmation_message(String msg) {
		Assert.assertTrue(driver.findElement(By.tagName("body")).getText().contains(msg));

	}
	
//search customer
@When("Enter customer EMail")
public void enter_customer_EMail() throws InterruptedException {
	logger.info("********Entering Customer email********");
	searchCust = new SearchCustomerPage(driver);
	Thread.sleep(3000);
	searchCust.setEmail("victoria_victoria@nopCommerce.com");

}

@When("Click on search button")
public void click_on_search_button() throws InterruptedException {
	searchCust.clickSearch();
	Thread.sleep(3000);
}

@Then("User should found Email in the Search table")
public void user_should_found_Email_in_the_Search_table() {
	
	boolean status = searchCust.searchCustomerByEmail("victoria_victoria@nopCommerce.com");
	Assert.assertEquals(true, status);
}

//steps for searching a customer by using First Name & Lastname
@When("Enter customer FirstName")
public void enter_customer_FirstName() {
	logger.info("********Entering new Customer first Name********");
	searchCust=new SearchCustomerPage(driver);
	searchCust.setFirstName("Victoria");
}

@When("Enter customer LastName")
public void enter_customer_LastName() {
	logger.info("********Entering new Customer last Name********");
	searchCust.setLastName("Terces");
}

@Then("User should found Name in the Search table")
public void user_should_found_Name_in_the_Search_table() {
	boolean status=searchCust.searchCustomerByName("Victoria Terces");
	Assert.assertEquals(true, status);
}




}
