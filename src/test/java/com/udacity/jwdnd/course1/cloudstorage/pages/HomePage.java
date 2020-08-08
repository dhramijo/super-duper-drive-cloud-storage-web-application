package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class HomePage {

    @FindBy(css="#home-logout")
    private WebElement logoutButton;

    @FindBy(id = "nav-files-tab")
    private WebElement filesTabButton;

    @FindBy(id = "nav-notes-tab")
    private WebElement notesTabButton;

    @FindBy(id = "nav-credentials-tab")
    private WebElement credentialsTabButton;

    public HomePage(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void openFilesTab(){
        filesTabButton.click();
    }

    public void openNotesTab(){
        notesTabButton.click();
    }

    public void openCredentialsTab(){
        credentialsTabButton.click();
    }

    public void logout() {
        this.logoutButton.click();
    }

}
