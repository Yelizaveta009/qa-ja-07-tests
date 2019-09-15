package com.telesens.framework.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.util.concurrent.TimeUnit;

public class BaseTest {
    protected WebDriver driver;

    @Parameters({"browser"})
    @BeforeClass(alwaysRun = true)
    public void setUp(@Optional("chrome") String browser) throws Exception {
        System.setProperty( "webdriver.chrome.driver", "d:/selenium/chromedriver.exe" );
        System.setProperty( "webdriver.gecko.driver", "d:/selenium/geckodriver.exe" );
        driver = new ChromeDriver();
//            driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait( 10, TimeUnit.SECONDS );
        driver.manage().window().maximize();
    }

        @AfterClass(alwaysRun = true)
        public void tearDown() throws Exception {
            driver.quit();
        }
}



