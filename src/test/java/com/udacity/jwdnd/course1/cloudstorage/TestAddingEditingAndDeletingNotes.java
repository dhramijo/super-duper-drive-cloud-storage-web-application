package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.pages.*;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestAddingEditingAndDeletingNotes {

    @LocalServerPort
    private int port;

    private WebDriver driver;

    private static final String FIRSTNAME = "Mario";
    private static final String LASTNAME = "Rossi";
    private static final String USERNAME = "mrossi";
    private static final String PASSWORD = "Mross1!";

    private static final String NOTE_TITLE = "To Do";
    private static final String NOTE_DESCRIPTION = "Note for a to do list";

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
    public void testAddNewNote() throws InterruptedException {

        signUpAndLogin();

        HomePage homePage = new HomePage(driver);
        Thread.sleep(1000);
        homePage.openNotesTab();

        NoteTab noteTab = new NoteTab(driver);
        noteTab.saveNote(NOTE_TITLE,NOTE_DESCRIPTION);

        Thread.sleep(1000);

        driver.get("http://localhost:" + this.port + "/home");
        Thread.sleep(2000);
        homePage.openNotesTab();

        WebElement noteRow = noteTab.getNoteRow(NOTE_TITLE, NOTE_DESCRIPTION);
        //noteRow.findElement(By.id(""));
        assertNotNull(noteRow);

    }


    @Test
    public void testEditNote() {
        signUpAndLogin();
    }


    @Test
    public void testDeleteNote() {
        signUpAndLogin();
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
