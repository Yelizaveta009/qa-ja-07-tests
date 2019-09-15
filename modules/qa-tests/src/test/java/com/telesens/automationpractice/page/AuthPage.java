package com.telesens.automationpractice.page;

import com.telesens.framework.page.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class AuthPage extends BasePage {
    @FindBy(id = "email")
    private WebElement emailField;

    @FindBy(id = "passwd")
    private WebElement passwField;

    @FindBy(id = "SubmitLogin")
    private WebElement submitButton;

    @FindBy(linkText = "Sign out")
    private WebElement signOutButton;

    @FindBy(xpath = "//*[@id='center_column']/div[1]/ol/li")
    private WebElement errorMessageBlock;


    public AuthPage(WebDriver driver) {
        super( driver );
        this.driver = driver;
    }

    public AuthPage enterEmail(String email) {
        inputTextField( emailField, email );
        return this;
    }

    public AuthPage enterPassword(String passw) {
        inputTextField( passwField, passw );
        return this;
    }

    public BasePage pressSubmit() {
        submitButton.click();
        return this;
    }

        public MyAccountPage goToMyAccountPage(){
        submitButton.click();
        return new MyAccountPage(driver);
    }

     public BasePage pressSignOut(){
            signOutButton.click();
        return this;
    }

    public String getErrorMessage(){
        return errorMessageBlock.getText();
    }
}

