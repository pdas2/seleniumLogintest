package org.csanchez.selenium.example;

import java.net.URL;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ExampleTest {

    private final static String SELENIUM_URL = System.getProperty("selenium.url", "http://localhost:4444/wd/hub");
    private final static String SELENIUM_BROWSER = System.getProperty("selenium.browser", "chrome");
    private final static int SLEEP = Integer.parseInt(System.getProperty("sleep", "10000"));

    protected WebDriver driver;

    @Before
    public void setUp() throws Exception {
        DesiredCapabilities capabilities = new DesiredCapabilities(SELENIUM_BROWSER, "", Platform.ANY);
        // Retry connecting
        WebDriverException ex = null;
        for (int i = 0; i < 10; i++) {
            try {
                this.driver = new RemoteWebDriver(new URL(SELENIUM_URL), capabilities);
                return;
            } catch (WebDriverException e) {
                ex = e;
                System.out.println(String.format("Error connecting to %s: %s. Retrying", SELENIUM_URL, e));
                Thread.sleep(1000);
            }
        }
        throw ex;
    }

    @Test
    public void test() throws Exception {
        // And now use this to visit Google
        driver.get("http://168.1.141.221:30451/login.html");
        // Alternatively the same thing can be done like this
        // driver.navigate().to("http://www.google.com");

        Thread.sleep(SLEEP);

        Thread.sleep(2000);
		if(driver.findElement(By.name("userid")).isDisplayed()) {
			System.out.println("Login page is launched -- PASS");
			driver.findElement(By.name("userid")).clear();
			driver.findElement(By.name("userid")).sendKeys("9830553821");
			driver.findElement(By.name("password")).clear();
			driver.findElement(By.name("password")).sendKeys("password1");
			driver.findElement(By.xpath("//input[@value='Login']")).click();
			Thread.sleep(2000);
			if(driver.findElement(By.id("btnBooking")).isDisplayed()) {
				System.out.println("Select Plan page is launched -- PASS");
				Select selectPlan = new Select(driver.findElement(By.id("offerid")));
				selectPlan.selectByIndex(0);
				driver.findElement(By.id("btnBooking")).click();
				Thread.sleep(2000);
				if(driver.findElement(By.xpath("//a[@href='/login.html']")).isDisplayed()) {
					System.out.println("Plan submitted -- PASS");
					driver.quit();
				}
				else {
					System.out.println("Plan not submitted -- FAIL");
					driver.quit();
				}
			}
			else {
				System.out.println("Select Plan page is not launched -- FAIL");
				driver.quit();
			}
			
		}
		else {
			System.out.println("Login page is not launched -- FAIL");
			driver.quit();
		}
		
    }

    @After
    public void tearDown() throws Exception {
        if (driver != null)
            driver.quit();
    }
}