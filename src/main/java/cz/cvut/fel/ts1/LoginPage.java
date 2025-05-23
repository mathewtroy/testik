package cz.cvut.fel.ts1;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {

    private static final String ACCOUNT_URL = "https://www.babymarkt.com/cz/account/";

    private final WebDriver driver;
    private final JavascriptExecutor js;

    @FindBy(xpath = "/html/body/aside//div[2]/div/footer/div[2]/div/button[3]")
    private WebElement acceptCookieButton;

    @FindBy(xpath = "/html/body/div[1]/div/main/div/div[2]/div[2]/div/div/div[1]/div/form/section[1]/div[1]/div/input")
    private WebElement emailField;

    @FindBy(xpath = "/html/body/div[1]/div/main/div/div[2]/div[2]/div/div/div[1]/div/form/section[2]/div[1]/div/input")
    private WebElement passwordField;

    @FindBy(xpath = "/html/body/div[1]/div/main/div/div[2]/div[2]/div/div/div[1]/div/form/button")
    private WebElement loginButton;

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.js = (JavascriptExecutor) driver;
        PageFactory.initElements(driver, this);
    }

    /** Clicks the “Accept” button inside the shadow DOM of the cookie banner. */
    public void acceptCookie() {
        WebElement accept = (WebElement) js.executeScript(
                "return document"
                        + ".querySelector('#usercentrics-cmp-ui')"
                        + ".shadowRoot"
                        + ".querySelector('#accept');"
        );
        accept.click();
    }

    /** Enters the given email into the email input field. */
    public void setEmail(String email) {
        emailField.sendKeys(email);
    }

    /** Enters the given password into the password input field. */
    public void setPassword(String password) {
        passwordField.sendKeys(password);
    }

    /** Clicks the login button to submit the form. */
    public void clickLogin() {
        loginButton.click();
    }

    /** Navigates directly to the account page by URL. */
    public void navigateToAccountPage() {
        driver.get(ACCOUNT_URL);
    }
}
