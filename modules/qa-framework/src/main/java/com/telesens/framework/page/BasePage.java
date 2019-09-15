package com.telesens.framework.page;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

public class BasePage {
    protected WebDriver driver;

    protected BasePage(WebDriver driver) {
            this.driver = driver;
            PageFactory.initElements(driver, this);
    }

    public static BasePage start(WebDriver driver) {

        return new BasePage(driver);
    }

    protected void inputTextField(WebElement textField, String value) {
            textField.click();
            textField.clear();
            textField.sendKeys(value);
    }
}

