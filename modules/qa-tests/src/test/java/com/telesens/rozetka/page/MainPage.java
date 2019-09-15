package com.telesens.rozetka.page;

import com.telesens.framework.page.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

public class MainPage extends BasePage {
    @FindBy(xpath = "//a[@class='header-topline__user-link link-dashed']")
    private WebElement enterLink;

    @FindBy(xpath = "//input[@id='auth_email']")
    private WebElement loginField;

    @FindBy(xpath = "//input[@id='auth_pass']")
    private WebElement passwordField;

    @FindBy(xpath = "//span[@class='button-inner']")
    private WebElement loginButton;

    @FindBy(xpath = "//a[@class='header-topline__user-link link-dashed']")
    private WebElement userName;

    @FindBy(partialLinkText = "Выход")
    private WebElement exitButton;

    @FindBy(xpath = "/html/body/app-root/div/div[1]/app-rz-main-page/div/aside/main-page-sidebar/sidebar-fat-menu/div/ul/li[1]")
    private WebElement mainMenuCss;

    @FindBy(partialLinkText = "Мониторы")
    private WebElement monitors;

    @FindBy(xpath = "//p[@class='error-message_color_red']")
    private WebElement errorMessageWrongPassword;

    @FindBy(xpath = "//span[@class='auth-modal__form-email']")
    private WebElement errorMessageWrongLogin;


    public MainPage(WebDriver driver) {
        super( driver );
        this.driver = driver;
    }

    public static MainPage startFromMain(WebDriver driver, String baseUrl) {
        driver.get( baseUrl );
        return new MainPage( driver );
    }

    public MainPage clickSignIn() {
        enterLink.click();
        return this;
    }

    public MainPage inputLogin(String login) {
        inputTextField( loginField, login );
        return this;
    }

    public MainPage inputPassword(String password) {
        inputTextField( passwordField, password );
        return this;
    }

    public MainPage pressSubmit() {
        loginButton.click();
        return this;
    }

    public MainPage signOut() {
        Actions actions = new Actions( driver );
        actions.moveToElement( userName )
                .build()
                .perform();

        exitButton.click();
        return this;
    }

    public MonitorsPage goToMonitorsPage() {
        Actions actions = new Actions( driver );
        actions.moveToElement( mainMenuCss )
                .build()
                .perform();
        monitors.click();
        return new MonitorsPage( driver );
    }
}



