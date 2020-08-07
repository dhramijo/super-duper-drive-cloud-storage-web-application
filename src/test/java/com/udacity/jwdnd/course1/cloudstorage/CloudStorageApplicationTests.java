package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CloudStorageApplicationTests {

	@LocalServerPort
	private int port;

	private WebDriver driver;

	private static final String FIRSTNAME = "Mario";
	private static final String LASTNAME = "Rossi";
	private static final String USERNAME = "mrossi";
	private static final String PASSWORD = "Mross1!";

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
	public void getLoginPage() {
		driver.get("http://localhost:" + this.port + "/login");
		assertEquals("Login", driver.getTitle());
	}

	@Test
	public void testUnAuthorizedUserCannotAccessHomePageButOnlyLoginAndSignup() {
		driver.get("http://localhost:" + this.port + "/home");
		assertFalse(driver.getTitle() == "Home");

		driver.get("http://localhost:" + this.port + "/login");
		assertEquals("Login", driver.getTitle());

		driver.get("http://localhost:" + this.port + "/signup");
		assertEquals("Sign Up", driver.getTitle());
	}

	@Test
	public void testUserSignupLogin() throws InterruptedException {

		driver.get("http://localhost:" + this.port + "/signup");

		SignupPage signupPage = new SignupPage(driver);
		signupPage.signup(FIRSTNAME, LASTNAME, USERNAME, PASSWORD);

		driver.get("http://localhost:" + this.port + "/login");

		LoginPage loginPage = new LoginPage(driver);
		loginPage.login(USERNAME, PASSWORD);

		assertEquals("Home", driver.getTitle());

		HomePage homePage = new HomePage(driver);
		Thread.sleep(1000);
		homePage.logout();

		driver.get("http://localhost:" + this.port + "/home");

		assertFalse(driver.getTitle() == "Home");
		assertEquals("Login", driver.getTitle());

	}

}
