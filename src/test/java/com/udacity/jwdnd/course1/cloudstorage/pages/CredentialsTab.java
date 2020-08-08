package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class CredentialsTab {

    @FindBy(className = "credential-row")
    private List<WebElement> credentialElements;

    @FindBy(id = "credential-url")
    private WebElement credentialUrlField;

    @FindBy(id = "credential-username")
    private WebElement credentialUsernameField;

    @FindBy(id = "credential-password")
    private WebElement credentialPasswordField;

    @FindBy(id = "add-new-credential-btn")
    private WebElement addNewCredentialBtn;

    @FindBy(id = "credential-save")
    private WebElement credentialSaveBtn;

    private WebDriver driver;

    public CredentialsTab(WebDriver driver) {
        PageFactory.initElements(driver, this);
        this.driver = driver;
    }

    public WebElement getCredentialRow(String url, String username){

        for(WebElement credentialElement : credentialElements){
            WebElement urlElement = credentialElement.findElement(By.className("credential-url"));
            WebElement usernameElement = credentialElement.findElement(By.className("credential-username"));

            if(urlElement.getText().equals(url) && usernameElement.getText().equals(username)) return credentialElement;
        }

        return null;
    }

    public String getCredentialPassword(WebElement credentialElement) {
        WebElement passwordElement = credentialElement.findElement(By.className("credential-password"));
        if(passwordElement != null)
            return passwordElement.getText();

        return null;
    }

    public void addCredential(String url, String username, String decryptedPassword){

        new WebDriverWait(driver, 1).until(ExpectedConditions.elementToBeClickable(addNewCredentialBtn)).click();

        new WebDriverWait(driver, 1).until(ExpectedConditions.elementToBeClickable(credentialSaveBtn));

        credentialUrlField.sendKeys(url);
        credentialUsernameField.sendKeys(username);
        credentialPasswordField.sendKeys(decryptedPassword);

        credentialSaveBtn.click();
    }

    public String openEditCredentialDialog(String url, String username) {
        WebElement credentialRow = getCredentialRow(url, username);

        if(credentialRow == null)
            return null;

        credentialRow.findElement(By.className("credential-edit")).click();

        new WebDriverWait(driver, 1).until(ExpectedConditions.elementToBeClickable(credentialSaveBtn));

        new WebDriverWait(driver, 1);
        String password = credentialPasswordField.getAttribute("value");
        return password;
    }

    public boolean editCredential(String oldUrl, String oldUsername, String newUrl, String newUsername, String newDecryptedPassword){

        WebElement credentialRow = getCredentialRow(oldUrl, oldUsername);

        if(credentialRow == null)
            return false;


        credentialRow.findElement(By.className("credential-edit")).click();

        new WebDriverWait(driver, 1).until(ExpectedConditions.elementToBeClickable(credentialSaveBtn));

        credentialUrlField.clear();
        credentialUsernameField.clear();
        credentialPasswordField.clear();

        credentialUrlField.sendKeys(newUrl);
        credentialUsernameField.sendKeys(newUsername);
        credentialPasswordField.sendKeys(newDecryptedPassword);

        credentialSaveBtn.click();

        return true;
    }

    public boolean deleteCredential(String url, String username){
        WebElement credentialRow = getCredentialRow(url,username);
        if(credentialRow == null) return false;

        credentialRow.findElement(By.className("credential-delete")).click();

        try{
            WebDriverWait wait = new WebDriverWait(driver, 1);
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());

            alert.accept();
        }catch(Throwable e){
            System.err.println("Error came while waiting for the alert popup. "+e.getMessage());
            return false;
        }

        return true;
    }

}
