package com.telesens.rozetka;

import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class RozetkaTests {

    private String rozetkaUrl;
        private WebDriver driver;

        private String priceCSS = "#block_with_goods  div.g-price.g-price-cheaper > div";

        @BeforeClass(alwaysRun = true)
        public void setUp(@Optional("chrome")String browser) throws Exception {
            rozetkaUrl = "https://rozetka.com.ua/";
                System.setProperty( "webdriver.chrome.driver", "d:/selenium/chromedriver.exe" );

                driver = new ChromeDriver();

                driver.manage().window().setSize(new Dimension(1000, 1000));
                driver.manage().timeouts().implicitlyWait( 30, TimeUnit.SECONDS );
            }


        @Test
        public void testSort() {
            driver.get( rozetkaUrl );

            driver.findElement( By.xpath( "(.//*[normalize-space(text()) and normalize-space(.)='Каталог товаров'])[3]/following::a[1]" ) ).click();

            driver.findElement( By.partialLinkText( "Мониторы" ) ).click();

            List<String> prices =
                    driver.findElements( By.cssSelector( priceCSS ) ).stream()
                            .map( WebElement::getText )
                            .map( s -> s.replaceAll( "[^\\d]", "" ) )
                            .collect( Collectors.toList() );
            System.out.println( prices );

            try {
                Thread.sleep( 5000 );
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            driver.findElement( By.partialLinkText( "по рейтингу" ) ).click();
            driver.findElement( By.partialLinkText( "от дешевых к дорогим" ) ).click();

            }

        @AfterClass
        public void tearDown() {
            driver.quit();
        }
    }
