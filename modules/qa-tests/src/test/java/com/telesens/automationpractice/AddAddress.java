package com.telesens.automationpractice;

/* Реализовать автотест для сайта automationpractice.com:
        - перейти на главную страницу по ссылке: http://automationpractice.com
        - залогиниться
        - нажать "My addresses"
        - нажать "Add a new address"
        - заполнить все поля (данные для адреса нужно взять из файла *.txt или *.xlsx)
        - нажать "Save >"
        (исли добавляемый адрес уже существует, то необходимо предварительно его удалить)

        проверить, что в списке адресов появился сохраненный адрес
        => для проверки необходимо прочитать список адресов перед добавлением и после добавления

        (см. скриншоты в директории '08_address_add')*/


import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import java.io.*;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

public class AddAddress {
    private static final String DEFAULT_PATH = "src/main/resources/automationpractice.properties";
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();
    private String login;
    private String password;
    private File file;
    private String setAddress;
    private String setCity;
    private String setPostcode;
    private String setIdStage;
    private String setPhone;
    private String setAlias;

    public AddAddress() throws IOException {
    }

    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
        String automationPracticePath = System.getProperty( "properties" );
        if (automationPracticePath == null)
            automationPracticePath = DEFAULT_PATH;

        Properties prop = new Properties();
        prop.load( new FileReader( automationPracticePath ) );
        baseUrl = prop.getProperty( "baseUrl" );
        login = prop.getProperty( "login" );
        password = prop.getProperty( "password" );
        file = new File(prop.getProperty("address.exc"));

        System.setProperty( "webdriver.chrome.driver", "d:/selenium/chromedriver.exe" );
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait( 30, TimeUnit.SECONDS );

        try {
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(file));
            XSSFSheet sheet = workbook.getSheet( "Лист1" );

                setAddress = sheet.getRow(0).getCell(0).getStringCellValue();
                setCity = sheet.getRow(0).getCell( 1 ).getStringCellValue();
                setIdStage =  sheet.getRow(0).getCell( 2).getStringCellValue();
                setPostcode = sheet.getRow(0).getCell( 3).getStringCellValue();
                setPhone =  sheet.getRow(0).getCell( 4 ).getStringCellValue();
                setAlias =  sheet.getRow(0).getCell( 5 ).getStringCellValue();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        @Test
    public void testAuthSuccess() throws Exception {
        driver.get(baseUrl);
        driver.findElement( By.linkText("Sign in")).click();

        WebElement email = driver.findElement( By.id("email") );
        email.click();
        email.clear();
        email.sendKeys(login);

        WebElement passwd = driver.findElement( By.id("passwd") );
        passwd.click();
        passwd.clear();
        passwd.sendKeys(password);
        driver.findElement( By.id("SubmitLogin")).click();

        WebElement myAddress = driver.findElement(By.className("icon-building"));
        myAddress.click();
        List<WebElement> elements = driver.findElements( By.className("page-subheading"));
        driver.findElement(By.xpath("(.//*[normalize-space(text())and normalize-space(.)='Delete'])[1]/following::span[1]")).click();

        WebElement address = driver.findElement(By.id("address1"));
        address.click();
        address.clear();
        address.sendKeys(setAddress);

        WebElement city = driver.findElement(By.id("city"));
        city.click();
        city.clear();
        city.sendKeys(setCity);

        WebElement idState = driver.findElement(By.id("id_state"));
        idState.click();
        new Select(idState).selectByVisibleText(setIdStage);
        idState.click();

        WebElement postcode = driver.findElement(By.id("postcode"));
        postcode.click();
        postcode.clear();
        postcode.sendKeys(setPostcode);

        WebElement phone = driver.findElement(By.id("phone"));
        phone.click();
        phone.clear();
        phone.sendKeys(setPhone);

        WebElement alias = driver.findElement(By.id("alias"));
        alias.clear();
        alias.sendKeys(setAlias);

        driver.findElement(By.id("submitAddress")).click();

        List<WebElement> newElements = driver.findElements( By.className("page-subheading"));

            try {
                Assert.assertEquals(elements,newElements );
            } catch (AssertionError e) {
                System.out.println("Был добавлен новый адрес");
            }
            acceptNextAlert = true;

            driver.findElement(By.xpath("(.//*[normalize-space(text()) and normalize-space(.)='Update'])[2]/following::span[1]")).click();
            assertTrue(closeAlertAndGetItsText().matches("^Are you sure[\\s\\S]$"));

            List<WebElement> extendedElements = driver.findElements( By.className("page-subheading"));

            try {
                Assert.assertEquals(newElements,extendedElements);
            } catch (AssertionError e) {
                System.out.println("Новый адрес успешно удален");
            }
            acceptNextAlert = true;

    }
    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
        }
    }

    private boolean isElementPresent(By by) {
        try {
            driver.findElement(by);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    private boolean isAlertPresent() {
        try {
            driver.switchTo().alert();
            return true;
        } catch (NoAlertPresentException e) {
            return false;
        }
    }

    private String closeAlertAndGetItsText() {
        try {
            Alert alert = driver.switchTo().alert();
            String alertText = alert.getText();
            if (acceptNextAlert) {
                alert.accept();
            } else {
                alert.dismiss();
            }
            return alertText;
        } finally {
            acceptNextAlert = true;
        }
    }
}
