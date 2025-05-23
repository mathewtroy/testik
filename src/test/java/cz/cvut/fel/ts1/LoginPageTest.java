package cz.cvut.fel.ts1;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;

public class LoginPageTest {

    private WebDriver driver;
    private LoginPage loginPage;
    private WebDriverWait wait;

    private final String loginUrl = "https://www.babymarkt.com/cz/signin/?redirectUrl=/cz/";
    private final String expectedWelcomeText = "Dobrý den Jiri!";

    @BeforeEach
    public void setUp() {
        // point Selenium to the chromedriver.exe in project root
        String driverPath = System.getProperty("user.dir")
                + File.separator
                + "chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", driverPath);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--whitelisted-ips=\"\"");
        driver = new ChromeDriver(options);

        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        // explicit wait for up to 10 seconds
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get(loginUrl);
        loginPage = new LoginPage(driver);
    }

    @Test
    public void testSuccessLoginWithDelays() {
        loginPage.acceptCookie();

        // enter credentials, then pause briefly so UI updates
        loginPage.setEmail("qwerty12@cz.cz");
        loginPage.setPassword("qwerty12@cz.cz");
        // small pause before clicking login
        wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("/html/body/div[1]/div/main/div/div[2]/div[2]/div/div/div[1]/div/form/button")
        ));

        loginPage.clickLogin();

        // wait until login form is no longer visible (i.e. login succeeded)
        wait.until(ExpectedConditions.invisibilityOfElementLocated(
                By.xpath("/html/body/div[1]/div/main/div/div[2]/div[2]/div/div/div[1]/div/form")
        ));

        // pause a bit to ensure session is established
        wait.until(ExpectedConditions.urlContains("/cz/"));

        // now navigate explicitly to the account page
        loginPage.navigateToAccountPage();

        // wait until the welcome message appears
        String welcomeXpath = "/html/body/div[1]/div/main/div/h4";
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(welcomeXpath)));

        String actual = driver.findElement(By.xpath(welcomeXpath)).getText();
        Assertions.assertEquals(expectedWelcomeText, actual);
    }

    @AfterEach
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
