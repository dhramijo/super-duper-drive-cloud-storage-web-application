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

public class NoteTab {

    @FindBy(id="note-title")
    private WebElement titleField;

    @FindBy(id="note-description")
    private WebElement descriptionField;

    @FindBy(className="note-row")
    private List<WebElement> noteElements;

    @FindBy(id="add-new-note-btn")
    private WebElement addNewNoteButton;

    @FindBy(id="note-save")
    private WebElement saveNoteButton;

    private WebDriver webDriver;

    public NoteTab(WebDriver webDriver) {
        PageFactory.initElements(webDriver, this);
        this.webDriver = webDriver;
    }


    public void addNote(String title, String description) {

        new WebDriverWait(webDriver, 1).until(ExpectedConditions.elementToBeClickable(addNewNoteButton)).click();
        new WebDriverWait(webDriver, 1).until(ExpectedConditions.elementToBeClickable(saveNoteButton));

        this.titleField.sendKeys(title);
        this.descriptionField.sendKeys(description);
        this.saveNoteButton.click();
    }


    public WebElement getNoteRow(String noteTitle, String noteDescription)  {

        for(WebElement noteElement : noteElements){
            WebElement titleElement = noteElement.findElement(By.className("note-title"));
            WebElement descriptionElement = noteElement.findElement(By.className("note-description"));

            if(titleElement.getText().equals(noteTitle) && descriptionElement.getText().equals(noteDescription)) return noteElement;
        }

        return null;
    }

    public boolean editNote(String oldTitle, String oldDescription, String newTitle, String newDescription){

        WebElement noteRow = getNoteRow(oldTitle, oldDescription);

        if(noteRow == null)
            return false;


        noteRow.findElement(By.className("note-edit")).click();

        new WebDriverWait(webDriver, 1).until(ExpectedConditions.elementToBeClickable(saveNoteButton));

        titleField.clear();
        descriptionField.clear();

        titleField.sendKeys(newTitle);
        descriptionField.sendKeys(newDescription);
        saveNoteButton.click();

        return true;
    }

    public boolean deleteNote(String title, String description){
        WebElement noteRow = getNoteRow(title,description);
        if(noteRow == null) return false;

        noteRow.findElement(By.className("note-delete")).click();

        try{
            WebDriverWait wait = new WebDriverWait(webDriver, 1);
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());

            alert.accept();
        }catch(Throwable e){
            System.err.println("Error came while waiting for the alert popup. "+e.getMessage());
            return false;
        }

        return true;
    }

}
