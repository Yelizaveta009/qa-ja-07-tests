package com.telesens.automationpractice;

import java.io.FileReader;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.chrome.ChromeDriver;
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
    private String chromeWebDriver;
    private String seleniumChromeDriver;


    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
        String automationPracticePath = System.getProperty("properties");
        if (automationPracticePath == null)
            automationPracticePath = DEFAULT_PATH;

        Properties prop = new Properties();
        prop.load(new FileReader(automationPracticePath));
        baseUrl = prop.getProperty("baseUrl");
        login = prop.getProperty( "login" );
        password = prop.getProperty( "password" );
        wrongLogin = prop.getProperty( "wrongLogin" );
        wrongPassword = prop.getProperty( "wrongPassword" );
        chromeWebDriver = prop.getProperty( "chromeWebDriver" );
        seleniumChromeDriver= prop.getProperty( "seleniumChromeDriver" );

        System.setProperty(chromeWebDriver, seleniumChromeDriver);
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
   }

    @Test
    public void testAuthSuccess() throws Exception {
        driver.get(baseUrl);
        driver.findElement(By.linkText("Sign in")).click();
        WebElement email = driver.findElement(By.id("email"));
        email.click();
        email.clear();
        email.sendKeys( login );
        WebElement passwd = driver.findElement(By.id("passwd"));
        passwd.click();
        passwd.clear();
        passwd.sendKeys( password );
        driver.findElement(By.id("SubmitLogin")).click();
        driver.findElement(By.linkText("Sign out")).click();
    }

    @Test
    public void testWrongPasswordAndLogin() throws Exception {
        driver.get(baseUrl);
        driver.findElement(By.linkText("Sign in")).click();
        WebElement email = driver.findElement(By.id("email"));
        email.click();
        email.clear();
        email.sendKeys( wrongLogin );
        WebElement passwd = driver.findElement(By.id("passwd"));
        passwd.click();
        passwd.clear();
        passwd.sendKeys( wrongPassword );
        driver.findElement(By.id("SubmitLogin")).click();
        WebElement errorMsg = driver.findElement(By.xpath("//div[contains(@class, 'alert alert-danger')]/ol/li"));
        String actualError = errorMsg.getText();
        Assert.assertEquals(actualError, "Invalid email address.");
    }


    @Test
    public void testWrongLogin() throws Exception {
        driver.get(baseUrl);
        driver.findElement( By.linkText("Sign in")).click();
        WebElement email = driver.findElement(By.id("email"));
        email.click();
        email.clear();
        email.sendKeys(wrongLogin);
        WebElement passwd = driver.findElement(By.id("passwd"));
        passwd.click();
        passwd.clear();
        passwd.sendKeys( password );
        driver.findElement(By.id("SubmitLogin")).click();
        WebElement errorMsg = driver.findElement(By.xpath("//div[contains(@class, 'alert alert-danger')]/ol/li"));
        String actualError = errorMsg.getText();
        Assert.assertEquals(actualError, "Invalid email address.");
    }

    @Test
    public void wrongPassword() throws Exception {
        driver.get(baseUrl);
        driver.findElement( By.linkText("Sign in")).click();
        WebElement email = driver.findElement(By.id("email"));
        email.click();
        email.clear();
        email.sendKeys( login );
        WebElement passwd = driver.findElement(By.id("passwd"));
        passwd.click();
        passwd.clear();
        passwd.sendKeys( wrongPassword );
        driver.findElement(By.id("SubmitLogin")).click();
        WebElement errorMsg = driver.findElement(By.xpath("//div[contains(@class, 'alert alert-danger')]/ol/li"));
        String actualError = errorMsg.getText();
        Assert.assertEquals(actualError, "Authentication failed.");
    }

    @Test
    public void testBlankPassword() throws Exception {
        driver.get(baseUrl);
        driver.findElement( By.linkText("Sign in")).click();
        WebElement email = driver.findElement(By.id("email"));
        email.click();
        email.clear();
        email.sendKeys( login );
        WebElement passwd = driver.findElement(By.id("passwd"));
        passwd.click();
        passwd.clear();
        passwd.sendKeys( "" );
        driver.findElement(By.id("SubmitLogin")).click();
        WebElement errorMsg = driver.findElement(By.xpath("//div[contains(@class, 'alert alert-danger')]/ol/li"));
        String actualError = errorMsg.getText();
        Assert.assertEquals(actualError, "Password is required.");
    }

    @Test
    public void testBlankLogin() throws Exception {
        driver.get(baseUrl);
        driver.findElement( By.linkText("Sign in")).click();
        WebElement email = driver.findElement(By.id("email"));
        email.click();
        email.clear();
        email.sendKeys( "" );
        WebElement passwd = driver.findElement(By.id("passwd"));
        passwd.click();
        passwd.clear();
        passwd.sendKeys( password );
        driver.findElement(By.id("SubmitLogin")).click();
        WebElement errorMsg = driver.findElement(By.xpath("//div[contains(@class, 'alert alert-danger')]/ol/li"));
        String actualError = errorMsg.getText();
        Assert.assertEquals(actualError, "An email address required.");
    }

        @AfterClass(alwaysRun = true)
        public void tearDown() throws Exception {
            driver.quit();
        }
}

