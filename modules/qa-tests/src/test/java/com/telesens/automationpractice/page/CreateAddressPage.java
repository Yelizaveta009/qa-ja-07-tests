package com.telesens.automationpractice.page;

import com.telesens.framework.page.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;


public class CreateAddressPage extends BasePage {

    @FindBy(id = "address1")
    private WebElement inputAdress;

    @FindBy(id = "city")
    private WebElement inputCity;

    @FindBy(id = "id_state")
    private WebElement inputIdState;

    @FindBy(id = "postcode")
    private WebElement inputPostcode;

    @FindBy(id = "phone")
    private WebElement inputPhone;

    @FindBy(id = "alias")
    private WebElement inputAlias;

    @FindBy(id = "submitAddress")
    private WebElement submitAdress;

    public CreateAddressPage(WebDriver driver) {
        super( driver );
        this.driver = driver;
    }


    public CreateAddressPage inputAddress(String address) {
        inputTextField( inputAdress, address );
        return this;
    }

    public CreateAddressPage inputCity(String city) {
        inputTextField( inputCity, city );
        return this;
    }

    public CreateAddressPage inputIdStage(String idStage) {
        inputIdState.click();
        new Select(inputIdState).selectByVisibleText(idStage);
        inputIdState.click();
        return this;
    }

    public CreateAddressPage inputPostcode(String postCode) {
        inputTextField( inputPostcode, postCode );
        return this;
    }

    public CreateAddressPage inputPhone(String phone) {
        inputTextField( inputPhone, phone );
        return this;
    }

    public CreateAddressPage inputAlias(String alias) {
        inputTextField( inputAlias, alias );
        return this;
    }

    public CreateAddressPage saveAddress() {
        submitAdress.click();
        return new CreateAddressPage( driver );
    }
}

