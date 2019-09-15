package com.telesens.automationpractice.page;

import com.telesens.framework.page.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MyAccountPage extends BasePage {
    @FindBy(xpath = "//i[@class='icon-building']")
    private WebElement myAdresses;

    public MyAccountPage(WebDriver driver) {
        super( driver );
        this.driver = driver;
    }
    public MyAddressesPage goToMyAdressesPage() {
        myAdresses.click();
        return new MyAddressesPage(driver);
    }
}

