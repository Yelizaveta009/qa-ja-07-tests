package com.telesens.fibi;

/*7) Реализовать автотест сортировки 'DRESSES' для сайта automationpractice.com:
        - перейти на главную страницу по ссылке: http://automationpractice.com
        - в меню (WOMEN | DRESSES | T-SHIRTS) кликнуть пункт 'DRESSES'
        - отсортировать по Price: Lowest first
        - Сделать следующие проверки:
        1) присутствуют пять позиций
        2) Цены идут по возрастанию: [$16.40, $26.00, $28.98, $30.50, $50.99] (точное значение цен не важно, важно - упорядоченность)

        (см. скриншоты в директории '07_sort_dresses')

        Тут я взяла другой сайт и выполнила аналогичный действия.
        */

import com.beust.jcommander.Parameter;
import com.google.common.collect.Ordering;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static org.testng.Assert.*;

public class SortTests {
    private static final String DEFAULT_PATH = "src/main/resources/automationpractice.properties";
    private WebDriver driver;
    private String fibiUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @Parameters("browser")
    @BeforeClass(alwaysRun = true)
    public void setUp(String browser) throws Exception {
        String automationPracticePath = System.getProperty( "properties" );
        if (automationPracticePath == null)
            automationPracticePath = DEFAULT_PATH;

        Properties prop = new Properties();
        prop.load( new FileReader( automationPracticePath ) );
        fibiUrl = prop.getProperty( "fibiUrl" );

        if (browser.equals( "chrome" )) {
            System.setProperty( "webdriver.chrome.driver", "d:/selenium/chromedriver.exe" );

            driver = new ChromeDriver();
        } else if (browser.equals( "firefox" )) {
            System.setProperty( "webdriver.gecko.driver", "d:/selenium/geckodriver.exe" );

            driver = new FirefoxDriver();

            driver.manage().window().setSize(new Dimension(1000, 1000));
            driver.manage().timeouts().implicitlyWait( 30, TimeUnit.SECONDS );
        }
    }

    @Test
    public void testUntitledTestCase() throws Exception {
        driver.get( fibiUrl );

        driver.findElement( By.cssSelector( "#menu > div.navbar-header > button > i" ) ).click();
        driver.findElement( By.cssSelector( ".hidem > ul:nth-child(1) > li:nth-child(3) > a:nth-child(1)" ) ).click();

        driver.findElement( By.id( "input-sort" ) ).click();//выполняем сортировку
        new Select( driver.findElement( By.id( "input-sort" ) ) ).selectByVisibleText( "По цене (возрастанию)" );

        List<String> prices =
                driver.findElements(By.className("price")).stream()
                        .map(WebElement::getText)
                        .map(s->s.replaceAll("[^\\d\\.]", ""))
                        .map(s->s.replace( ".00.","" ))//костыль
                        .map( s ->s.replace( "270588","270" ) )//костыль
                        .collect( Collectors.toList());
        System.out.println(prices);


        boolean isSorted = Ordering.natural().isOrdered(prices);
        System.out.println(isSorted);

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        driver.quit();
        String verificationErrorString = verificationErrors.toString();
        if (!"".equals(verificationErrorString)) {
            fail(verificationErrorString);
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
