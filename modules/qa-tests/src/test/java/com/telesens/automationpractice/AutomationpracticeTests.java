package com.telesens.automationpractice;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import com.telesens.framework.page.BasePage;
import com.telesens.framework.test.BaseTest;
import com.telesens.automationpractice.page.AuthPage;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.annotations.*;

import static com.telesens.automationpractice.page.HomePage.startFromHome;


public class AutomationpracticeTests extends BaseTest {
    private static final String DEFAULT_PATH = "src/main/resources/automationpractice.properties";
    private String baseUrl;
    private String login;
    private String password;
    private String wrongLogin;
    private String wrongPassword;
    private File file;
    private String setAddress;
    private String setCity;
    private String setPostcode;
    private String setIdStage;
    private String setPhone;
    private String setAlias;


    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
        String automationPracticePath = System.getProperty( "automationpractice" );
        if (automationPracticePath == null)
            automationPracticePath = DEFAULT_PATH;

        Properties prop = new Properties();
        prop.load( new FileReader( automationPracticePath ) );

        baseUrl = prop.getProperty( "baseUrl" );
        login = prop.getProperty( "login" );
        password = prop.getProperty( "password" );
        wrongLogin = prop.getProperty( "wrongLogin" );
        wrongPassword = prop.getProperty( "wrongPassword" );

        File file = new File( prop.getProperty( "address.exc" ) );

        try {
            XSSFWorkbook workbook = new XSSFWorkbook( new FileInputStream( file ) );
            XSSFSheet sheet = workbook.getSheet( "Лист1" );

            setAddress = sheet.getRow( 0 ).getCell( 0 ).getStringCellValue();
            setCity = sheet.getRow( 0 ).getCell( 1 ).getStringCellValue();
            setIdStage = sheet.getRow( 0 ).getCell( 2 ).getStringCellValue();
            setPostcode = sheet.getRow( 0 ).getCell( 3 ).getStringCellValue();
            setPhone = sheet.getRow( 0 ).getCell( 4 ).getStringCellValue();
            setAlias = sheet.getRow( 0 ).getCell( 5 ).getStringCellValue();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAuthSuccess() throws Exception {
        BasePage basePage = startFromHome(driver, baseUrl)
                .clickSignIn()
                .enterEmail(login)
                .enterPassword(password)
                .pressSubmit();

        ((AuthPage) basePage).pressSignOut();
    }

    @Test(dataProvider = "authErrorMessageProvider")
    public void testWrongPasswordAndLogin(String login, String passw, String expectedError) throws Exception {
        BasePage basePage = startFromHome(driver, baseUrl)
                .clickSignIn()
                .enterEmail(login)
                .enterPassword(passw)
                .pressSubmit();

        String actualError = ((AuthPage) basePage).getErrorMessage();

        Assert.assertEquals(actualError, expectedError);
    }

    @Test
    public void setAddress() throws Exception {
        BasePage basePage = startFromHome(driver, baseUrl)
                .clickSignIn()
                .enterEmail(login)
                .enterPassword(password)
                .goToMyAccountPage()
                .goToMyAdressesPage()
                .addNewAdress()
                .inputAddress( setAddress )
                .inputCity( setCity )
                .inputIdStage( setIdStage )
                .inputPostcode( setPostcode )
                .inputPhone( setPhone )
                .inputAlias(setAlias  )
                .saveAddress();
    }

    @DataProvider(name = "authErrorMessageProvider")
    public Object[][] authErrorMessageProvider() {
        return new Object[][]{
                {wrongLogin, wrongPassword, "Invalid email address."},
                {wrongLogin, password, "Invalid email address."},
                {login,wrongPassword,"Authentication failed."},
                {login,"","Password is required."},
                {"",password,"An email address required."}
        };
    }
}

