package com.telesens.rozetka;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class RozetkaTests {

    private String rozetkaUrl;
    private WebDriver driver;

    private int minPrice = (int) (Math.random() * 74329);//генерирует случайную минимальную цену до 74329 грн.
    private int maxPrice = (int) (74329 + (Math.random() * 74329));//генерирует случайную максимальную цену от 74329 грн.

    private String mainMenuCSS = "body > app-root > div > div:nth-child(2) > app-rz-main-page > div > aside > main-page-sidebar > sidebar-fat-menu > div > ul > li:nth-child(1) > a";
    private String priceCSS = "div[name='goods_list_container']  div.g-price > div.g-price-uah";

    @BeforeClass(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser) throws Exception {
        rozetkaUrl = "https://rozetka.com.ua/";
        System.setProperty( "webdriver.chrome.driver", "d:/selenium/chromedriver.exe" );

        driver = new ChromeDriver();

        driver.manage().window().setSize( new Dimension( 1500, 1800 ) );
        driver.manage().timeouts().implicitlyWait( 30, TimeUnit.SECONDS );

    }


    @Test
    @Ignore
    public void testSort() {
        driver.get( rozetkaUrl );
        Actions actions = new Actions( driver );
        actions.moveToElement( driver.findElement( By.cssSelector( mainMenuCSS ) ) )
                .perform();
        driver.findElement( By.partialLinkText( "Мониторы" ) ).click();

        driver.findElement( By.cssSelector( "#sort_view > a" ) ).click();
        driver.findElement( By.xpath( "//*[@id='sort_view']/div/ul/li/a[contains(text(), 'от дешевых')]" ) ).click();

        new WebDriverWait( driver, Duration.ofSeconds( 10 ) )
                .until( ExpectedConditions.presenceOfElementLocated(
                        By.xpath( "//div[contains(@class, 'sort-popup') and contains(@style, 'visibility: hidden')]" )
                ) );
        List<Integer> pricesSortedActual =
                driver.findElements( By.cssSelector( priceCSS ) ).stream()
                        .map( WebElement::getText )
                        .map( s -> s.replaceAll( "[^\\d]", "" ) )
                        .map( Integer::parseInt )
                        .collect( Collectors.toList() );

        List<Integer> pricesSortedExpected = new ArrayList<>( pricesSortedActual );
        Collections.sort( pricesSortedExpected );


//            System.out.println("pricesSortedActual: " + pricesSortedActual);
//            System.out.println("pricesSortedExpected: " + pricesSortedExpected);

        Assert.assertEquals( pricesSortedActual, pricesSortedExpected );
    }

    @Test
    @Ignore
    public void testFilter() {
        driver.get( rozetkaUrl );
        Actions actions = new Actions( driver );
        actions.moveToElement( driver.findElement( By.cssSelector( mainMenuCSS ) ) )
                .perform();
        driver.findElement( By.partialLinkText( "Мониторы" ) ).click();

        driver.findElement( By.id( "price[min]" ) ).click();
        driver.findElement( By.id( "price[min]" ) ).clear();
        driver.findElement( By.id( "price[min]" ) ).sendKeys( Integer.toString( minPrice ) );//вписываем случайную цену
        driver.findElement( By.id( "price[max]" ) ).click();
        driver.findElement( By.id( "price[max]" ) ).clear();
        driver.findElement( By.id( "price[max]" ) ).sendKeys( Integer.toString( maxPrice ) );
        driver.findElement( By.id( "submitprice" ) ).click();

        new WebDriverWait( driver, Duration.ofSeconds( 10 ) )
                .until( ExpectedConditions.elementToBeClickable( By.xpath( ".//*[@id=\"reset_filterprice\"]/a" ) ) );

        List<Integer> pricesFilterActual =
                driver.findElements( By.cssSelector( priceCSS ) ).stream()
                        .map( WebElement::getText )
                        .map( s -> s.replaceAll( "[^\\d]", "" ) )
                        .map( Integer::parseInt )
                        .collect( Collectors.toList() );//сортируем список от минимального к максимальному.
        Collections.sort( pricesFilterActual );

        System.out.println( pricesFilterActual );

        Integer first = pricesFilterActual.get( 0 ); //берем 1 значение из списка то есть минимальную цену
        Integer last = pricesFilterActual.get( pricesFilterActual.size() - 1 );//берем последнее значение из списка то есть максимальную цену

        if (first >= minPrice && last <= maxPrice) {
            System.out.println( "Товары были отфильтрованы стоимостью от " + minPrice + " грн. до " + maxPrice + " грн. " );
        } else {
            System.out.println( "Товары не были отфильтрованы." );
        }
    }

    @Test
    public void testFilterSecondVersion() {
        driver.get( rozetkaUrl );
        Actions actions = new Actions( driver );
        actions.moveToElement( driver.findElement( By.cssSelector( mainMenuCSS ) ) )
                .perform();
        driver.findElement( By.partialLinkText( "Мониторы" ) ).click();

        int xLeft = (int) (Math.random() * 125);//значение для xOffset, чтоб курсором левый слайдер перемещался на рандомную длину вправо.

        int xRight = (int) -(Math.random() * (125 - xLeft));//тут высчитывания xOffset для действий с правым слайдером.
        // Я постралась реализовать формулу при которой слайдер будет перемещаться влево на рандомную длину.
        // Но при этом будет оставаться небольшое расстояние между слайдерами, чтоб создавался диапазон.

        actions.dragAndDropBy( driver.findElement( By.cssSelector( "div> img.left-slider" ) ), xLeft, 0 )
                .build()
                .perform();

        actions.dragAndDropBy( driver.findElement( By.cssSelector( "div> img.right-slider" ) ), xRight, 0 )
                .build()
                .perform();

        driver.findElement( By.id( "submitprice" ) ).click();

        new WebDriverWait( driver, Duration.ofSeconds( 10 ) )
                .until( ExpectedConditions.elementToBeClickable( By.xpath( ".//*[@id=\"reset_filterprice\"]/a" ) ) );

        List<Integer> pricesFilterActual =
                driver.findElements( By.cssSelector( priceCSS ) ).stream()
                        .map( WebElement::getText )
                        .map( s -> s.replaceAll( "[^\\d]", "" ) )
                        .map( Integer::parseInt )
                        .collect( Collectors.toList() );
        Collections.sort( pricesFilterActual );

        System.out.println( pricesFilterActual );


        String price = String.valueOf( driver.findElement( By.xpath( "//*[@id=\"reset_filterprice\"]/a" ) ).getText() );

        String priceMin = price.substring( 0,10 );
        Integer priceMinExtended = Integer.valueOf( priceMin.replaceAll( "[^\\d]", "" ));

        String priceMax = price.substring( 10 );
        Integer priceMaxExtended = Integer.valueOf( priceMax.replaceAll( "[^\\d]", "" ));

            Integer first = pricesFilterActual.get( 0 );
            Integer last = pricesFilterActual.get( pricesFilterActual.size() - 1 );

            if (first >= priceMinExtended && last <= priceMaxExtended) {
            System.out.println( "Товары были отфильтрованы стоимостью от " + priceMinExtended + " грн. до " + priceMaxExtended + " грн. " );
        } else {
            System.out.println( "Товары не были отфильтрованы." );
        }
  }
        @AfterClass
        public void tearDown () {
            driver.quit();
        }
}


