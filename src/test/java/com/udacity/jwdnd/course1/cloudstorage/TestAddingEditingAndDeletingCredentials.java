package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.pages.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * This is a test class for testing the following:
 *
 * 1. Test that logs in an existing user, creates a credential
 *    and verifies that the credential details are visible in the credential list.
 *
 * 2. Test that logs in an existing user with existing credentials,
 *    clicks the edit credential button on an existing credential,
 *    changes the credential data, saves the changes,
 *    and verifies that the changes appear in the credential list.
 *
 * 3. Test that logs in an existing user with existing credentials,
 *    clicks the delete credential button on an existing credential,
 *    and verifies that the credential no longer appears in the credential list.
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TestAddingEditingAndDeletingCredentials {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    private static final String FIRSTNAME = "Mario";
    private static final String LASTNAME = "Rossi";
    private static final String USERNAME = "mrossi";
    private static final String PASSWORD = "Mross1!";

    private static final String CREDENTIAL_URL = "http://credential-test.co.uk";
    private static final String CREDENTIAL_USERNAME = "jdreamer";
    private static final String CREDENTIAL_PASSWORD = "Jdr3amer1!";

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() {
        this.driver = new ChromeDriver();
    }

    @AfterEach
    public void afterEach() {
        if (this.driver != null) {
            driver.quit();
        }
    }


    @Test
    @Order(2)
    public void testAddNewCredentials() throws InterruptedException {

        signUpAndLogin();

        HomePage homePage = new HomePage(driver);
        Thread.sleep(1000);
        homePage.openCredentialsTab();

        CredentialsTab credentialsTab = new CredentialsTab(driver);
        credentialsTab.addCredential(CREDENTIAL_URL,CREDENTIAL_USERNAME,CREDENTIAL_PASSWORD);

        Thread.sleep(1000);
        driver.get("http://localhost:" + this.port + "/home");

        Thread.sleep(1000);
        homePage.openCredentialsTab();

        Thread.sleep(1000);
        WebElement credentialsRow = credentialsTab.getCredentialRow(CREDENTIAL_URL,CREDENTIAL_USERNAME);

        assertNotNull(credentialsRow);
        assertNotEquals(CREDENTIAL_PASSWORD, credentialsTab.getCredentialPassword(credentialsRow));
    }


    @Test
    @Order(3)
    public void testEditCredentials() throws InterruptedException {

        String NEW_CREDENTIAL_URL = "http://new-credential-test.co.uk";
        String NEW_CREDENTIAL_USERNAME = "new_username";
        String NEW_CREDENTIAL_PASSWORD = "New_p4ssword";

        signUpAndLogin();

        HomePage homePage = new HomePage(driver);
        Thread.sleep(1000);
        homePage.openCredentialsTab();

        CredentialsTab credentialsTab = new CredentialsTab(driver);
        credentialsTab.addCredential(CREDENTIAL_URL,CREDENTIAL_USERNAME,CREDENTIAL_PASSWORD);

        Thread.sleep(1000);
        driver.get("http://localhost:" + this.port + "/home");

        Thread.sleep(1000);
        homePage.openCredentialsTab();

        Thread.sleep(1000);
        credentialsTab.editCredential(CREDENTIAL_URL,CREDENTIAL_USERNAME,NEW_CREDENTIAL_URL,NEW_CREDENTIAL_USERNAME,NEW_CREDENTIAL_PASSWORD);

        Thread.sleep(1000);
        driver.get("http://localhost:" + this.port + "/home");

        Thread.sleep(1000);
        homePage.openCredentialsTab();

        Thread.sleep(1000);
        WebElement credentialsRow = credentialsTab.getCredentialRow(NEW_CREDENTIAL_URL,NEW_CREDENTIAL_USERNAME);

        assertNotNull(credentialsRow);
        assertEquals(NEW_CREDENTIAL_URL,credentialsRow.findElement(By.className("credential-url")).getText());
        assertEquals(NEW_CREDENTIAL_USERNAME,credentialsRow.findElement(By.className("credential-username")).getText());

        String editablePassword = credentialsTab.openEditCredentialDialog(NEW_CREDENTIAL_URL, NEW_CREDENTIAL_USERNAME);

        assertEquals(NEW_CREDENTIAL_PASSWORD, editablePassword);
    }


    @Test
    @Order(1)
    public void testDeleteCredentials() throws InterruptedException {
        signUpAndLogin();

        HomePage homePage = new HomePage(driver);
        Thread.sleep(1000);
        homePage.openCredentialsTab();

        CredentialsTab credentialsTab = new CredentialsTab(driver);
        credentialsTab.addCredential(CREDENTIAL_URL,CREDENTIAL_USERNAME,CREDENTIAL_PASSWORD);

        Thread.sleep(1000);
        driver.get("http://localhost:" + this.port + "/home");

        Thread.sleep(1000);
        homePage.openCredentialsTab();

        Thread.sleep(1000);
        credentialsTab.deleteCredential(CREDENTIAL_URL,CREDENTIAL_USERNAME);

        Thread.sleep(1000);
        driver.get("http://localhost:" + this.port + "/home");

        Thread.sleep(1000);
        homePage.openCredentialsTab();

        Thread.sleep(1000);
        assertNull(credentialsTab.getCredentialRow(CREDENTIAL_URL, CREDENTIAL_USERNAME));
    }


    private void signUpAndLogin() {
        driver.get("http://localhost:" + this.port + "/signup");

        SignupPage signupPage = new SignupPage(driver);
        signupPage.signup(FIRSTNAME, LASTNAME, USERNAME, PASSWORD);

        driver.get("http://localhost:" + this.port + "/login");

        LoginPage loginPage = new LoginPage(driver);
        loginPage.login(USERNAME, PASSWORD);
    }

}
