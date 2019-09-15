package com.telesens.automationpractice.page;

import com.telesens.framework.page.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class HomePage extends BasePage {


    @FindBy(linkText = "Sign in")
    private WebElement linkSignIn;



        public HomePage(WebDriver driver) {
            super(driver);
            this.driver = driver;
        }

        public static HomePage startFromHome(WebDriver driver, String baseUrl) {
            driver.get(baseUrl);
            return new HomePage(driver);
        }

        public AuthPage clickSignIn() {
            linkSignIn.click();
            return new AuthPage(driver);
        }
}


