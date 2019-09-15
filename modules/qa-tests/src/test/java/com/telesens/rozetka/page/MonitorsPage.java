package com.telesens.rozetka.page;
import com.telesens.framework.page.BasePage;
import com.telesens.rozetka.RozetkaTests;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class MonitorsPage extends BasePage {
    private static final Logger LOG = LogManager.getLogger( RozetkaTests.class.getName());

    @FindBy(css = "#sort_view > a")
    private WebElement sortView;

    @FindBy(xpath = "//*[@id='sort_view']/div/ul/li/a[contains(text(), 'от дешевых')]")
    private WebElement fromСheapToExpensive;

    @FindBy(css = "div> img.left-slider")
    private WebElement leftSlider;

    @FindBy(css = "div> img.right-slider")
    private WebElement rightSlider;

    @FindBy(id = "submitprice")
    private WebElement submitPrice;

    @FindBy(xpath = "//*[@id='reset_filterprice']/a")
    private WebElement infoAboutFiltering;


    public MonitorsPage(WebDriver driver) {
        super( driver );
        this.driver = driver;
    }

    public MonitorsPage sortedMonitorsAscending() {
        sortView.click();
        fromСheapToExpensive.click();
        return this;
    }

    public List<Integer> getListWithPrices() {
        List<Integer> prices =
                driver.findElements( By.cssSelector( "div[name='goods_list_container']  div.g-price > div.g-price-uah" ) ).stream()
                        .map( WebElement::getText )
                        .map( s -> s.replaceAll( "[^\\d]", "" ) )
                        .map( Integer::parseInt )
                        .collect( Collectors.toList());
        return prices;

    }

    public MonitorsPage sortingCheck() {
        new WebDriverWait( driver, Duration.ofSeconds( 10 ) )
                .until( ExpectedConditions.presenceOfElementLocated(
                        By.xpath( "//div[contains(@class, 'sort-popup') and contains(@style, 'visibility: hidden')]" )
                ));

        List<Integer> pricesSortedActual = getListWithPrices();
        LOG.info( "Sorted prices: "+ pricesSortedActual );

        List<Integer> pricesSortedExpected = new ArrayList<>(pricesSortedActual);
        Collections.sort( pricesSortedExpected );

        Assert.assertEquals( getListWithPrices(), pricesSortedExpected );
        LOG.info( "Sort check succeeded ");
        return this;
    }

    public MonitorsPage filterMonitorsByPrice(){
        Actions actions = new Actions( driver );

        int xLeft = (int) (Math.random() * 125);
        int xRight = (int) -(Math.random() * (125 - xLeft));

        actions.dragAndDropBy(leftSlider, xLeft, 0 )
                .build()
                .perform();

        actions.dragAndDropBy(rightSlider, xRight, 0 )
                .build()
                .perform();

        submitPrice.click();
        return this;
    }
    public MonitorsPage filteringCheck() {
        new WebDriverWait( driver, Duration.ofSeconds( 10 ) )
                .until( ExpectedConditions.elementToBeClickable( By.xpath( ".//*[@id=\"reset_filterprice\"]/a" ) ) );

        List<Integer> pricesFilterActual = getListWithPrices();
        Collections.sort(pricesFilterActual);

        LOG.info( "Filtered prices: " + pricesFilterActual );

        String price = String.valueOf(infoAboutFiltering.getText() );

        String priceMin = price.substring( 0,10 );
        Integer priceMinExtended = Integer.valueOf( priceMin.replaceAll( "[^\\d]", "" ));

        String priceMax = price.substring( 10 );
        Integer priceMaxExtended = Integer.valueOf( priceMax.replaceAll( "[^\\d]", "" ));

        Integer first = pricesFilterActual.get( 0 );
        Integer last = pricesFilterActual.get( pricesFilterActual.size() - 1 );

        Assert.assertTrue( first >= priceMinExtended );
        Assert.assertTrue(last <= priceMaxExtended);

        LOG.info("Items have been filtered from " + priceMinExtended + " UAH. to " + priceMaxExtended + " UAH. ");

        return this;
    }
}
