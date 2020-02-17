package com.friendly.aqa.pageobject;

import com.friendly.aqa.test.BaseTestCase;
import com.friendly.aqa.utils.Table;
import org.apache.log4j.Logger;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.io.File;

import static com.friendly.aqa.pageobject.BasePage.FrameSwitch.*;

public class MonitoringPage extends BasePage {
    private static Logger logger = Logger.getLogger(MonitoringPage.class);

    @Override
    protected String getLeftMenuCssSelector() {
        return "tr[topmenu='Monitoring']";
    }

    public MonitoringPage topMenu(TopMenu value) {
        return (MonitoringPage) super.topMenu(value);
    }

    public MonitoringPage globalButtons(GlobalButtons button) {
        clickGlobalButtons(button);
        return this;
    }

    @FindBy(id = "ddlView")
    private WebElement filterViewComboBox;

    @FindBy(id = "ddlManuf")
    private WebElement filterManufacturerComboBox;

    @FindBy(id = "ddlModel")
    private WebElement filterModelNameComboBox;

    @FindBy(id = "btnEditView_btn")
    private WebElement editViewButton;

    @FindBy(id = "btnAddModel_btn")
    private WebElement addModelButton;

    @FindBy(id = "btnNewView_btn")
    private WebElement newViewButton;

    @FindBy(id = "btnDefaultView_btn")
    private WebElement resetViewButton;

    @FindBy(id = "btnSelectDevices_btn")
    private WebElement selectButton;

    @FindBy(id = "btnCancel_btn")
    private WebElement cancelIndividualButton;

    @FindBy(id = "IsDefaultViewForPublic")
    private WebElement forPublicCheckbox;

    @FindBy(id = "IsDefaultViewForUser")
    private WebElement forUserCheckbox;

    @FindBy(id = "tbName")
    private WebElement nameField;


    public MonitoringPage newViewButton() {
        newViewButton.click();
        return this;
    }

    public MonitoringPage newGroupButton() {
        return newViewButton();
    }

    public MonitoringPage fillName() {
        nameField.sendKeys(BaseTestCase.getTestName());
        return this;
    }

    public MonitoringPage fillGroupName() {
        return (MonitoringPage) super.fillName();
    }

    public MonitoringPage fillGroupName(String name) {
        return (MonitoringPage) super.fillName(name);
    }

    public MonitoringPage forPublicCheckbox() {
        forPublicCheckbox.click();
        return this;
    }

    public MonitoringPage forUserCheckbox() {
        forUserCheckbox.click();
        return this;
    }

    public MonitoringPage editViewButton() {
        editViewButton.click();
        waitForUpdate();
        return this;
    }

    public Table getMainTable() {
        return new Table("tbl");
    }

    public MonitoringPage addModel() {
        addModelButton.click();
        waitForUpdate();
        return this;
    }

    public MonitoringPage immediately() {
        return (MonitoringPage) super.immediately();
    }

    public MonitoringPage assertMainPageDisplayed() {
        try {
            boolean viewComboBox = filterViewComboBox.isDisplayed() && filterViewComboBox.isEnabled();
            boolean manufacturerComboBox = filterManufacturerComboBox.isDisplayed() && filterManufacturerComboBox.isEnabled();
            boolean modelNameComboBox = filterModelNameComboBox.isDisplayed() && filterModelNameComboBox.isEnabled();
            boolean editViewBtn = editViewButton.isDisplayed() && editViewButton.isEnabled();
            boolean newViewBtn = newViewButton.isDisplayed() && newViewButton.isEnabled();
            boolean resetViewBtn = resetViewButton.isDisplayed() && resetViewButton.isEnabled();
            if (viewComboBox && manufacturerComboBox && modelNameComboBox && editViewBtn && newViewBtn && resetViewBtn) {
                return this;
            }
        } catch (NoSuchElementException e) {
            logger.warn(e.getMessage());
        }
        throw new AssertionError("One or more elements not found on Monitoring tab main page");
    }

    public MonitoringPage selectView(String value) {
        selectComboBox(filterViewComboBox, value);
        waitForUpdate();
        return this;
    }

    public MonitoringPage selectFilterManufacturer(String value) {
        selectComboBox(filterManufacturerComboBox, value);
        return this;
    }

    public MonitoringPage selectFilterModelName(String value) {
        selectComboBox(filterModelNameComboBox, value);
        return this;
    }

    public MonitoringPage selectButton() {
        selectButton.click();
        return this;
    }

    public MonitoringPage selectIndividualDevises(int amount) {
        System.out.println(frame);
        switchToFrame(POPUP);
        Table table = getTable("tblDevices");
        for (int i = 1; i < amount + 1; i++) {
            table.clickOn(i, 0, 0);
        }
        selectButton();
        switchToFrame(DESKTOP);
        return this;
    }

    public MonitoringPage selectImportDevicesFile() {
        return (MonitoringPage) super.selectImportDevicesFile();
    }

    public MonitoringPage selectManufacturer() {
        selectComboBox(manufacturerComboBox, getManufacturer());
        return this;
    }

    @Override
    public MonitoringPage selectModel() {
        return (MonitoringPage) super.selectModel();
    }

    @Override
    public MonitoringPage selectModel(String value) {
        return (MonitoringPage) super.selectModel(value);
    }

    public MonitoringPage selectColumnFilter(String option) {
        selectComboBox(selectColumnFilter, option);
        return this;
    }

    public MonitoringPage compareSelect(String option) {
        selectComboBox(compareSelect, option);
        return this;
    }

    @Override
    public MonitoringPage selectSendTo() {
        return (MonitoringPage) super.selectSendTo();
    }

    @Override
    public MonitoringPage selectSendTo(String sendTo) {
        return (MonitoringPage) super.selectSendTo(sendTo);
    }

    @Override
    public MonitoringPage assertButtonsAreEnabled(boolean enabled, GlobalButtons... buttons) {
        return (MonitoringPage) super.assertButtonsAreEnabled(enabled, buttons);
    }

    @Override
    public MonitoringPage assertButtonsArePresent(GlobalButtons... buttons) {
        return (MonitoringPage) super.assertButtonsArePresent(buttons);
    }

    public MonitoringPage assertTableIsEmpty(String id) {
        Table table = getTable(id);
        if (table.getTableSize()[0] > 0) {
            throw new AssertionError("Unexpected table content (expected: empty table)");
        }
        return this;
    }

    public MonitoringPage assertTableHasContent(String id) {
        Table table = getTable(id);
        if (table.getTableSize()[0] == 0) {
            throw new AssertionError("Unexpected table content (expected: not empty table)");
        }
        return this;
    }

    public MonitoringPage cancelIndividualSelection() {
        switchToFrame(POPUP);
        cancelIndividualButton.click();
        switchToPreviousFrame();
        return this;
    }

    public MonitoringPage assertButtonAreActive(String id) {
        if (isButtonActive(id)) {
            return this;
        }
        throw new AssertionError("Button ID='" + id + "' is disabled");
    }

    public MonitoringPage fillViewName() {
        return (MonitoringPage) super.fillName();
    }

    public MonitoringPage fillViewName(String name) {
        return (MonitoringPage) super.fillName(name);
    }

    @Override
    public MonitoringPage addFilter() {
        return (MonitoringPage) super.addFilter();
    }

    @Override
    public MonitoringPage okButtonPopUp() {
        return (MonitoringPage) super.okButtonPopUp();
    }

    public MonitoringPage assertElementIsPresent(String id) {
        return (MonitoringPage) super.assertElementIsPresent(id);
    }

    public MonitoringPage leftMenu(Left item) {
        switchToFrame(ROOT);
        leftMenuClick(item.getValue());
        waitForUpdate();
        switchToFrame(DESKTOP);
        return this;
    }

    public enum Left {
        VIEW("View"), IMPORT("Import"), NEW("New");
        private String value;

        Left(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
