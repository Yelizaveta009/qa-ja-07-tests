package com.telesens.automationpractice;

/*3) Реализовать автотест для сайта automationpractice.com:
        Сценарий:
        - перейти на главную страницу по ссылке: http://automationpractice.com
        - в меню (WOMEN | DRESSES | T-SHIRTS) кликнуть пункт 'WOMEN'
        - Сделать следующие проверки:
        1) страница title содержит слово "Women"
        2) навигатор страниц содержит пункт "women"
        3) слева над каталогом расположен раздел "WOMEN"
        4) по середение содержится текст с заголовком "Women" и содержимое текста начинается
        "You will find here all woman fashion..."
        5) наличие под текстом в середине категории "WOMEN" "There are 7 products". (Число 7 может быть другим)
        6) в футоре внизу наличие "Women" в пункте "Categories"*/

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.FileReader;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static org.testng.Assert.assertTrue;

public class CategoryWomenTests {
    private static final String DEFAULT_PATH = "src/main/resources/automationpractice.properties";
    private WebDriver driver;
    private String baseUrl;
    private boolean acceptNextAlert = true;
    private StringBuffer verificationErrors = new StringBuffer();

    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
        String automationPracticePath = System.getProperty( "properties" );
        if (automationPracticePath == null)
            automationPracticePath = DEFAULT_PATH;

        Properties prop = new Properties();
        prop.load( new FileReader( automationPracticePath ) );
        baseUrl = prop.getProperty( "baseUrl" );

        System.setProperty("webdriver.chrome.driver", "d:/selenium/chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait( 30, TimeUnit.SECONDS );
    }

        @Test
        public void testCategoryWomen () throws Exception {
            driver.get(baseUrl);
            driver.findElement(By.className("sf-with-ul")).click();
            String title = driver.getTitle();
            WebElement navigator = driver.findElement(By.className("sfHoverForce"));
            WebElement section = driver.findElement(By.className("title_block"));
            WebElement futor = driver.findElement(By.className("category-name"));

            String navigatorPage = navigator.getText();
            String sectionWomen = section.getText();
            String categoriesFutor = futor.getText();

            assertTrue(title.contains("Women"));// страница title содержит слово "Women"
            Assert.assertEquals(navigatorPage, "WOMEN");//навигатор страниц содержит пункт "women"
            Assert.assertEquals(sectionWomen, "WOMEN");//слева над каталогом расположен раздел "WOMEN"

            assertTrue(driver.findElement(By.cssSelector("div.cat_desc")).getText().contains("Women"));//  4) по середение содержится текст с заголовком "Women" и содержимое текста начинается "You will find here all woman fashion..."
            assertTrue(driver.findElement(By.cssSelector("div.cat_desc")).getText().contains("You will find here all woman fashion collections."));

            assertTrue(driver.findElement(By.cssSelector("span.heading-counter")).getText().contains("There are")); //5) наличие под текстом в середине категории "WOMEN" "There are 7 products". (Число 7 может быть другим)
            assertTrue(driver.findElement(By.cssSelector("span.heading-counter")).getText().contains("products")); //

            Assert.assertEquals( categoriesFutor, "Women" );// 6) в футоре внизу наличие "Women" в пункте "Categories"*/

        }

        @AfterClass(alwaysRun = true)
        public void tearDown() throws Exception {
            driver.quit();
    }
}


