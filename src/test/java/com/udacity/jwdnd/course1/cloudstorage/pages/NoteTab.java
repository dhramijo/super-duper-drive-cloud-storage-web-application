package com.udacity.jwdnd.course1.cloudstorage.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NoteTab {

    @FindBy(css="#inputUsername")
    private WebElement titleField;

    @FindBy(css="#inputPassword")
    private WebElement descriptionField;

    @FindBy(css="#inputPassword")
    private WebElement addNewNoteButton;

    @FindBy(css="#submit-button")
    private WebElement saveNoteButton;

    public NoteTab(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
    }

    public void addNote() {
        this.addNewNoteButton.click();
    }

    public void saveNote(String title, String description) {
        this.titleField.sendKeys(title);
        this.descriptionField.sendKeys(description);
        this.saveNoteButton.click();
    }
}
