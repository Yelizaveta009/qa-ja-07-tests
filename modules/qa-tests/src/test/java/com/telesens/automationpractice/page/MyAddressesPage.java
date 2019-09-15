package com.telesens.automationpractice.page;

import com.telesens.framework.page.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class MyAddressesPage extends BasePage {
    @FindBy(linkText = "Add a new address")
    private WebElement addAdress;


    public MyAddressesPage(WebDriver driver) {
        super( driver );
        this.driver = driver;
    }

    public CreateAddressPage addNewAdress() {
        addAdress.click();
        return new CreateAddressPage(driver);
    }
}
