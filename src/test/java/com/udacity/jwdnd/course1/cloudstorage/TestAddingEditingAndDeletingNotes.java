package com.udacity.jwdnd.course1.cloudstorage;

import com.udacity.jwdnd.course1.cloudstorage.pages.HomePage;
import com.udacity.jwdnd.course1.cloudstorage.pages.LoginPage;
import com.udacity.jwdnd.course1.cloudstorage.pages.NoteTab;
import com.udacity.jwdnd.course1.cloudstorage.pages.SignupPage;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

/**
 *
 * This is a test class for testing the following:
 *
 * 1. Test that logs in an existing user, creates a note
 *    and verifies that the note details are visible in the note list.
 *
 * 2. Test that logs in an existing user with existing notes,
 *    clicks the edit note button on an existing note,
 *    changes the note data, saves the changes,
 *    and verifies that the changes appear in the note list.
 *
 * 3. Test that logs in an existing user with existing notes,
 *    clicks the delete note button on an existing note,
 *    and verifies that the note no longer appears in the note list.
 *
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class TestAddingEditingAndDeletingNotes {

    @LocalServerPort
    private int port;

    private static WebDriver driver;

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
    @Order(2)
    public void testAddNewNote() throws InterruptedException {

        signUpAndLogin();

        HomePage homePage = new HomePage(driver);
        Thread.sleep(1000);
        homePage.openNotesTab();

        NoteTab noteTab = new NoteTab(driver);
        noteTab.addNote(NOTE_TITLE,NOTE_DESCRIPTION);

        Thread.sleep(1000);
        driver.get("http://localhost:" + this.port + "/home");

        Thread.sleep(1000);
        homePage.openNotesTab();

        Thread.sleep(1000);
        WebElement noteRow = noteTab.getNoteRow(NOTE_TITLE, NOTE_DESCRIPTION);

        assertNotNull(noteRow);
        assertEquals(NOTE_TITLE,noteRow.findElement(By.className("note-title")).getText());
        assertEquals(NOTE_DESCRIPTION,noteRow.findElement(By.className("note-description")).getText());
    }


    @Test
    @Order(3)
    public void testEditNote() throws InterruptedException {

        String NOTE_TITLE_EDITED = "Edited To Do";
        String NOTE_DESCRIPTION_EDITED = "Edited Note";

        signUpAndLogin();

        HomePage homePage = new HomePage(driver);
        Thread.sleep(1000);
        homePage.openNotesTab();

        NoteTab noteTab = new NoteTab(driver);
        noteTab.addNote(NOTE_TITLE,NOTE_DESCRIPTION);

        Thread.sleep(1000);
        driver.get("http://localhost:" + this.port + "/home");

        Thread.sleep(1000);
        homePage.openNotesTab();

        Thread.sleep(1000);
        noteTab.editNote(NOTE_TITLE,NOTE_DESCRIPTION,NOTE_TITLE_EDITED,NOTE_DESCRIPTION_EDITED);

        Thread.sleep(1000);
        driver.get("http://localhost:" + this.port + "/home");

        Thread.sleep(1000);
        homePage.openNotesTab();

        Thread.sleep(1000);
        WebElement noteRow = noteTab.getNoteRow(NOTE_TITLE_EDITED, NOTE_DESCRIPTION_EDITED);

        assertNotNull(noteRow);
        assertEquals(NOTE_TITLE_EDITED,noteRow.findElement(By.className("note-title")).getText());
        assertEquals(NOTE_DESCRIPTION_EDITED,noteRow.findElement(By.className("note-description")).getText());
    }


    @Test
    @Order(1)
    public void testDeleteNote() throws InterruptedException {
        signUpAndLogin();

        HomePage homePage = new HomePage(driver);
        Thread.sleep(1000);
        homePage.openNotesTab();

        NoteTab noteTab = new NoteTab(driver);
        noteTab.addNote(NOTE_TITLE,NOTE_DESCRIPTION);

        Thread.sleep(1000);
        driver.get("http://localhost:" + this.port + "/home");

        Thread.sleep(1000);
        homePage.openNotesTab();

        Thread.sleep(1000);
        noteTab.deleteNote(NOTE_TITLE,NOTE_DESCRIPTION);

        Thread.sleep(1000);
        driver.get("http://localhost:" + this.port + "/home");

        Thread.sleep(1000);
        homePage.openNotesTab();

        Thread.sleep(1000);
        assertNull(noteTab.getNoteRow(NOTE_TITLE, NOTE_DESCRIPTION));
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
