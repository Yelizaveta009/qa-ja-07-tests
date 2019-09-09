package com.telesens.automationpractice;

import java.io.FileReader;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.*;
import org.openqa.selenium.*;


public class AuthTests {
    private static final String DEFAULT_PATH = "src/main/resources/automationpractice.properties";
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    private String login;
    private String password;
    private String wrongLogin;
    private String wrongPassword;

    @Parameters("browser")
    @BeforeClass(alwaysRun = true)
    public void setUp(String browser) throws Exception {
        String automationPracticePath = System.getProperty( "properties" );
        if (automationPracticePath == null)
            automationPracticePath = DEFAULT_PATH;

        Properties prop = new Properties();
        prop.load( new FileReader( automationPracticePath ) );
        baseUrl = prop.getProperty( "baseUrl" );
        login = prop.getProperty( "login" );
        password = prop.getProperty( "password" );
        wrongLogin = prop.getProperty( "wrongLogin" );
        wrongPassword = prop.getProperty( "wrongPassword" );


        if (browser.equals( "chrome" )) {
            System.setProperty( "webdriver.chrome.driver", "d:/selenium/chromedriver.exe" );

            driver = new ChromeDriver();
        } else if (browser.equals( "firefox" )) {
            System.setProperty( "webdriver.gecko.driver", "d:/selenium/geckodriver.exe" );

            driver = new FirefoxDriver();

        driver.manage().timeouts().implicitlyWait( 30, TimeUnit.SECONDS );
        }
    }

    @Test
    public void testAuthSuccess() throws Exception {
        driver.get(baseUrl);
        driver.findElement( By.linkText( "Sign in" ) ).click();
        WebElement email = driver.findElement( By.id( "email" ) );
        email.click();
        email.clear();
        email.sendKeys( login );
        WebElement passwd = driver.findElement( By.id( "passwd" ) );
        passwd.click();
        passwd.clear();
        passwd.sendKeys( password );
        driver.findElement( By.id( "SubmitLogin" ) ).click();
        driver.findElement( By.linkText( "Sign out" ) ).click();
    }

    @Test(dataProvider = "authErrorMessageProvider")
    public void testWrongPasswordAndLogin(String login, String passw, String errMsg) throws Exception {
        driver.get(baseUrl);
        driver.findElement( By.linkText( "Sign in" ) ).click();
        WebElement email = driver.findElement( By.id( "email" ) );
        email.click();
        email.clear();
        email.sendKeys(login);
        WebElement passwd = driver.findElement( By.id( "passwd" ) );
        passwd.click();
        passwd.clear();
        passwd.sendKeys(passw);
        driver.findElement( By.id( "SubmitLogin" ) ).click();
        WebElement errorMsg = driver.findElement( By.xpath( "//div[contains(@class, 'alert alert-danger')]/ol/li" ) );
        String actualError = errorMsg.getText();

        Assert.assertEquals( actualError, errMsg );
    }


    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        driver.quit();

    }

    @DataProvider(name = "authErrorMessageProvider")
    public Object[][] authErrorMessageProvider() {
        return new Object[][]{
                {wrongLogin, wrongPassword, "Invalid email address."},
                {wrongLogin, password, "Invalid email address."},
                {login,wrongPassword,"Authentication failed."},
                {login,"","Password is required."},
                {"",password,"An email address required."}
        };
    }
}

