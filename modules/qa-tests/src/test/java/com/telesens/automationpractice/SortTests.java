package com.telesens.automationpractice;

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

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.fail;

public class SortTests {
    private static final String DEFAULT_PATH = "src/main/resources/automationpractice.properties";
    private WebDriver driver;
    private String additionalUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
        String automationPracticePath = System.getProperty( "properties" );
        if (automationPracticePath == null)
            automationPracticePath = DEFAULT_PATH;

        Properties prop = new Properties();
        prop.load( new FileReader( automationPracticePath ) );
        additionalUrl = prop.getProperty( "additionalUrl" );

        System.setProperty( "webdriver.chrome.driver", "d:/selenium/chromedriver.exe" );
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait( 30, TimeUnit.SECONDS );
    }

    @Test
    public void testUntitledTestCase() throws Exception {
        driver.get( additionalUrl );
        driver.findElement( By.xpath( "(.//*[normalize-space(text()) and normalize-space(.)='Категории'])[1]/following::i[1]" ) ).click();

        driver.get( "http://www.fibi.com.ua/swimsuit-anabel-arto/" );    //тут костыль
        /*Когда записываю сценарий с помощью Каtalon он автоматически записывает driver.findElement(By.By.LinkText("Купальники")).click(). Но здесь это не работает.
        Я пробовала через className, cssSelector. Но в итоге ничего не получилось и пока написала так*/


        List<WebElement> swimsuit = driver.findElements( By.className( "product-layout2" ) );//создаем лист который находит все позиции до сортировки.

        driver.findElement( By.id( "input-sort" ) ).click();//выполняем сортировку
        new Select( driver.findElement( By.id( "input-sort" ) ) ).selectByVisibleText( "По цене (возрастанию)" );
        driver.findElement( By.id( "input-sort" ) ).click();

        List<WebElement> sortedSwimsuit = driver.findElements( By.className( "product-layout2" ) );//создаем лист который находит все позиции после сортировки.

        List<WebElement> newPrice = driver.findElements( By.className( "price" ) );

        for (WebElement element : newPrice) {
            System.out.println( element.getText() );

            int swim = swimsuit.size();//определяем длину листа до и после сортировки
            int sortedSwim = sortedSwimsuit.size();

            Assert.assertEquals( 8, sortedSwim );// проверяем, что присутствуют 8 позиций.
            Assert.assertEquals( swim, sortedSwim );// проверка, что количество позиций до и после сортировки не изменилась.
       
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
