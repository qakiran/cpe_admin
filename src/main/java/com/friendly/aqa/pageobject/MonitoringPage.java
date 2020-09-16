package com.friendly.aqa.pageobject;

import com.friendly.aqa.entities.GlobalButtons;
import com.friendly.aqa.entities.IGlobalButtons;
import com.friendly.aqa.entities.TopMenu;
import com.friendly.aqa.test.BaseTestCase;
import com.friendly.aqa.utils.CalendarUtil;
import com.friendly.aqa.entities.Table;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.io.File;
import java.util.*;

import static com.friendly.aqa.pageobject.BasePage.FrameSwitch.*;
import static com.friendly.aqa.entities.GlobalButtons.ADVANCED_VIEW;
import static com.friendly.aqa.entities.GlobalButtons.SAVE;
import static com.friendly.aqa.utils.DataBaseConnector.getMonitorNameSetByManufacturer;
import static com.friendly.aqa.utils.DataBaseConnector.getMonitorNameSetByModelName;

public class MonitoringPage extends BasePage {
    private static Logger logger = Logger.getLogger(MonitoringPage.class);

    @Override
    protected String getLeftMenuCssSelector() {
        return "tr[topmenu='Monitoring']";
    }

    @Override
    public MonitoringPage topMenu(TopMenu value) {
        return (MonitoringPage) super.topMenu(value);
    }

    public MonitoringPage bottomMenu(GlobalButtons button) {
        clickGlobalButtons(button);
        return this;
    }

    @FindBy(id = "btnAddModel_btn")
    private WebElement addModelButton;

    @FindBy(id = "btnSelectDevices_btn")
    private WebElement selectButton;

    @FindBy(id = "btnCancel_btn")
    private WebElement cancelIndividualButton;

    @FindBy(id = "IsDefaultViewForPublic")
    private WebElement forPublicCheckbox;

    @FindBy(id = "IsDefaultViewForUser")
    private WebElement forUserCheckbox;

    @FindBy(id = "cbDateTo")
    private WebElement endDateCheckbox;

//    @FindBy(id = "tbName")
//    private WebElement nameField;

    @FindBy(id = "txtName")
    private WebElement customViewNameField;

    @FindBy(id = "tblDataParams")
    private WebElement paramTable;

    @FindBy(id = "tbTimeToHour")
    private WebElement endDateHours;

    @FindBy(id = "tbTimeToMinute")
    private WebElement endDateMinutes;

    @FindBy(id = "tbTimeHour")
    private WebElement scheduledHours;

    @FindBy(id = "tbTimeMinute")
    private WebElement scheduledMinutes;

    @FindBy(id = "fuImport")
    private WebElement importMonField;

    @Override
    public MonitoringPage newViewButton() {
        return (MonitoringPage) super.newViewButton();
    }

    public MonitoringPage newGroupButton() {
        return newViewButton();
    }

    @Override
    public MonitoringPage fillName() {
        return (MonitoringPage) super.fillName();
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

    @Override
    public MonitoringPage editButton() {
        return (MonitoringPage) super.editButton();
    }

    @Override
    public String getMainTableId() {
        return "tbl";
    }

    @Override
    public MonitoringPage clickOnTable(String id, int row, int column) {
        return (MonitoringPage) super.clickOnTable(id, row, column);
    }

    @Override
    public MonitoringPage clickOnTable(String id, int row, int column, int tagNum) {
        return (MonitoringPage) super.clickOnTable(id, row, column, tagNum);
    }

    @Override
    public MonitoringPage assertEqualsAlertMessage(String expectedMessage) {
        return (MonitoringPage) super.assertEqualsAlertMessage(expectedMessage);
    }

    @Override
    public MonitoringPage selectItem() {
        return (MonitoringPage) super.selectItem();
    }

    @Override
    public MonitoringPage selectItem(String groupName) {
        return (MonitoringPage) super.selectItem(groupName);
    }

    @Override
    public MonitoringPage enterIntoGroup(String groupName) {
        return (MonitoringPage) super.enterIntoGroup(groupName);
    }

    public MonitoringPage enterIntoGroup() {
        return enterIntoGroup(BaseTestCase.getTestName());
    }

    public MonitoringPage addModel() {
        addModelButton.click();
        waitForUpdate();
        return this;
    }

    public MonitoringPage addAnotherModel() {
        List<WebElement> optList = modelComboBox.findElements(By.tagName("option"));
        List<String> list = new ArrayList<>();
        optList.forEach(element -> list.add(element.getText()));
        list.remove("Select a model");
        list.remove(getModelName());
        setImplicitlyWait(0);
        for (String item : list) {
            selectModel(item);
            addModel();
            switchToFrame(ROOT);
            waitForUpdate();
            if (!okButtonAlertPopUp.isDisplayed()) {
                switchToFrame(DESKTOP);
                selectSendTo();
                break;
            }
            okButtonAlertPopUp.click();
            switchToFrame(DESKTOP);
        }
        setDefaultImplicitlyWait();
        return this;
    }

    public void setParametersFor2Devices(boolean theSameParameter) {
        int param1 = 0, param2 = -1;
        String hint1, hint2;
        Table devTable = getTable("tblModels");
        String[] devices = {devTable.getCellText(0, 0), devTable.getCellText(1, 0)};
        devTable.clickOn(0, 0);
        String[] dev1 = getParamTable().getColumn(0);
        waitForUpdate();
        devTable.clickOn(1, 0);
        String[] dev2 = getParamTable().getColumn(0);
        out:
        for (int i = 0; i < dev1.length; i++) {
            param1 = i + 1;
            for (int j = 0; j < dev2.length; j++) {
                if (dev1[i].equals(dev2[j]) == theSameParameter) {
                    param2 = j + 1;
                    break out;
                }
            }
        }
        if (param2 < 0) {
            throw new AssertionError("Cannot find suitable parameters!");
        }
        hint2 = getParamTable().clickOn(param2, 1, 0).getHint(param2);
        getTable("tblModels").clickOn(0, 0);
        waitForUpdate();
        hint1 = getParamTable().clickOn(param1, 1, 0).getHint(param1);
        immediately();
        bottomMenu(SAVE);
        okButtonPopUp();
        enterIntoGroup();
        getTable("tblModels").clickOn(devices[0]);
        getTabTable().clickOn("Summary");
        waitForUpdate();
        dev1 = getParamTable().getColumn(0);
        if (dev1.length != 2) {
            throw new AssertionError("Unexpected number of parameters!");
        }
        boolean success = false;
        for (int i = 0; i < 2; i++) {
            if (dev1[i].equals(hint1)) {
                success = true;
                break;
            }
        }
        if (!success) {
            throw new AssertionError("Parameter " + hint1 + " not found!");
        }
        getTable("tblModels").clickOn(devices[1]);
        waitForUpdate();
        dev2 = getParamTable().getColumn(0);
        if (dev2.length != 2) {
            throw new AssertionError("Unexpected number of parameters!");
        }
        success = false;
        for (int i = 0; i < 2; i++) {
            if (dev2[i].equals(hint2)) {
                success = true;
                break;
            }
        }
        if (!success) {
            throw new AssertionError("Parameter " + hint2 + " not found!");
        }
    }

    public MonitoringPage setParameters(String tab, int startParam, int endParam) {
        return setParameters(false, tab, startParam, endParam);
    }

    public MonitoringPage setAdvancedParameters(String branch, int startParam, int endParam) {
        return setParameters(true, branch, startParam, endParam);
    }

    private MonitoringPage setParameters(boolean advancedView, String tab, int startParam, int endParam) {
        if (parameterSet == null) {
            getTabTable().clickOn("Summary");
            waitForUpdate();
            String[] params = new String[0];
            try {
                setImplicitlyWait(1);
                params = getParamTable().getColumn(0);
            } catch (NoSuchElementException e) {
                System.out.println("There are no default parameters");
            } finally {
                setDefaultImplicitlyWait();
            }
            parameterSet = new HashSet<>(Arrays.asList(params));
        }
        if (advancedView) {
            if (isButtonActive(ADVANCED_VIEW)) {
                getTabTable().clickOn("Management");
                bottomMenu(ADVANCED_VIEW);
                waitForUpdate();
            }
            selectBranch(tab);
        } else if (tab != null) {
            getTabTable().clickOn(tab);
        }
        waitForUpdate();
        Table table = getParamTable();
        int lastParam = Math.min(endParam, table.getTableSize()[0] - 2);
        startParam = Math.min(startParam, lastParam);
        for (int i = startParam; i < lastParam + 1; i++) {
            WebElement checkBox = table.getCellWebElement(i + 1, 1).findElement(By.tagName("input"));
            checkBox.click();
            String hint = table.getHint(i + 1);
            if (hint.endsWith("DeviceInfo.UpTime")) {
                continue;
            }
            if (!parameterSet.add(hint)) {
                parameterSet.remove(hint);
            }
            waitForUpdate();
        }
        table.clickOn(0, 0);
        return this;
    }

    public MonitoringPage setSingleParameter() {
        getTabTable().clickOn(1, 1);
        waitForUpdate();
        getParamTable().clickOn(1, 1, 0);
        return this;
    }

    @Override
    public MonitoringPage setViewColumns(int startParam, int endParam) {
        return (MonitoringPage) super.setViewColumns(startParam, endParam);
    }

    public void checkAddedTasks() {
        getTabTable().clickOn("Summary");
        waitForUpdate();
        String[] params = getParamTable().getColumn(0);
        for (String param : params) {
            if (!parameterSet.remove(param)) {
                throw new AssertionError("Unexpected parameter: '" + param + "' detected on the 'Summary' tab!");
            }
        }
        if (parameterSet.size() > 0) {
            StringBuilder unexpected = new StringBuilder("Parameters:\n");
            for (String param : parameterSet) {
                unexpected.append(param).append("\n");
            }
            throw new AssertionError(unexpected.append("not found on the 'Summary' tab!").toString());
        }
    }

    @Override
    public MonitoringPage immediately() {
        if (endDateCheckbox.isSelected()) {
            endDateCheckbox.click();
        }
        return (MonitoringPage) super.immediately();
    }

    @Override
    public MonitoringPage scheduledToRadioButton() {
        return (MonitoringPage) super.scheduledToRadioButton();
    }

    @Override
    public MonitoringPage assertMainPageIsDisplayed() {
        return (MonitoringPage) super.assertMainPageIsDisplayed();
    }

    @Override
    public MonitoringPage selectView(String value) {
        return (MonitoringPage) super.selectView(value);
    }

    public void checkFilteringByManufacturer() {
        List<String> optionList = getOptionList(filterManufacturerComboBox);
        optionList.remove("All");
        optionList.forEach(option -> checkMonitoringFiltering(true, option));
    }

    public void checkFilteringByModelName() {
        List<String> optionList = getOptionList(filterModelNameComboBox);
        optionList.remove("All");
        optionList.forEach(option -> checkMonitoringFiltering(false, option));
    }

    private void checkMonitoringFiltering(boolean byManufacturer, String filter) {
        Set<String> dbNameSet;
        if (byManufacturer) {
            selectFilterManufacturer(filter);
            dbNameSet = getMonitorNameSetByManufacturer(filter);
        } else {
            selectFilterModelName(filter);
            dbNameSet = getMonitorNameSetByModelName(filter);
        }
        waitForUpdate();
        if (elementIsPresent("btnPager2")) {
            selectComboBox(itemsOnPageComboBox, "200");
        }
        waitForUpdate();
        String[] names = elementIsPresent("tblSample") ? getMainTable().getColumn("Name") : new String[0];
        Set<String> webNameSet = new HashSet<>(Arrays.asList(names));
        if (elementIsAbsent("btnPager2")) {
            dbNameSet.removeAll(webNameSet);
            if (dbNameSet.size() == 0) {
                return;
            }
        } else {
            if (webNameSet.removeAll(dbNameSet) && webNameSet.size() == 0) {
                return;
            }
        }
        throw new AssertionError("Filtering by " + (byManufacturer ? "manufacturer" : "model name") + "failed!");
    }

    @Override
    public MonitoringPage selectFilterManufacturer(String value) {
        return (MonitoringPage) super.selectFilterManufacturer(value);
    }

    @Override
    public MonitoringPage selectFilterModelName(String value) {
        return (MonitoringPage) super.selectFilterModelName(value);
    }

    public MonitoringPage selectButton() {
        selectButton.click();
        return this;
    }

    public MonitoringPage selectIndividualDevises(int amount) {
        switchToFrame(POPUP);
        Table table = getTable("tblDevices");
        for (int i = 1; i < amount + 1; i++) {
            table.clickOn(i, 0, 0);
        }
        selectButton();
        switchToFrame(DESKTOP);
        return this;
    }

    @Override
    public MonitoringPage selectImportDevicesFile() {
        return (MonitoringPage) super.selectImportDevicesFile();
    }

    public MonitoringPage selectManufacturer() {
        return selectManufacturer(getManufacturer());
    }

    @Override
    public MonitoringPage selectManufacturer(String manufacturer) {
        return (MonitoringPage) super.selectManufacturer(manufacturer);
    }

    private Table getParamTable() {
        return new Table(this.paramTable);
    }

    @Override
    public MonitoringPage selectModel() {
        return (MonitoringPage) super.selectModel();
    }

    @Override
    public MonitoringPage selectModel(String value) {
        return (MonitoringPage) super.selectModel(value);
    }

    @Override
    public MonitoringPage selectColumnFilter(String option) {
        return (MonitoringPage) super.selectColumnFilter(option);
    }

    @Override
    public MonitoringPage selectCompare(String option) {
        return (MonitoringPage) super.selectCompare(option);
    }

    public MonitoringPage waitForStatus(String status) {
        waitForStatus(status, 5);
        return this;
    }

    public MonitoringPage waitForStatus(String status, String testName) {
        waitForStatus(status, testName, 5);
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
    public MonitoringPage assertButtonsAreEnabled(boolean enabled, IGlobalButtons... buttons) {
        return (MonitoringPage) super.assertButtonsAreEnabled(enabled, buttons);
    }

    @Override
    public MonitoringPage assertButtonsArePresent(IGlobalButtons... buttons) {
        return (MonitoringPage) super.assertButtonsArePresent(buttons);
    }

    @Override
    public MonitoringPage selectShiftedDate(String id, int value) {
        return (MonitoringPage) super.selectShiftedDate(id, value);
    }

    public MonitoringPage clickOn(String id) {
        driver.findElement(By.id(id)).click();
        return this;
    }

    @Override
    public MonitoringPage assertTableIsEmpty(String id) {
        return (MonitoringPage) super.assertTableIsEmpty(id);
    }

    @Override
    public MonitoringPage assertTableHasContent(String tableId) {
        return (MonitoringPage) super.assertTableHasContent(tableId);
    }

    public MonitoringPage cancelIndividualSelection() {
        switchToFrame(POPUP);
        cancelIndividualButton.click();
        switchToPreviousFrame();
        return this;
    }

    public MonitoringPage assertButtonIsActive(String id) {
        if (isButtonActive(id)) {
            return this;
        }
        throw new AssertionError("Button id='" + id + "' is disabled");
    }

    @Override
    public MonitoringPage assertEquals(String actual, String expected) {
        return (MonitoringPage) super.assertEquals(actual, expected);
    }

    @Override
    public MonitoringPage assertEquals(String actual, String expected, String message) {
        return (MonitoringPage) super.assertEquals(actual, expected, message);
    }

    @Override
    public MonitoringPage assertTrue(boolean condition) {
        return (MonitoringPage) super.assertTrue(condition);
    }

    @Override
    public MonitoringPage assertTrue(boolean condition, String message) {
        return (MonitoringPage) super.assertTrue(condition, message);
    }

    @Override
    public MonitoringPage assertFalse(boolean condition) {
        return (MonitoringPage) super.assertFalse(condition);
    }

    @Override
    public MonitoringPage assertFalse(boolean condition, String message) {
        return (MonitoringPage) super.assertFalse(condition, message);
    }

    public MonitoringPage fillViewName() {
        return (MonitoringPage) super.fillName();
    }

    public MonitoringPage addDeviceWithoutTemplate() {
        super.addDeviceWithoutTemplate();
        addModel();
        assertEqualsAlertMessage("Template for this model doesn't exist");
        return this;
    }

    public MonitoringPage fillViewName(String name) {
        return (MonitoringPage) super.fillName(name);
    }

    @Override
    public MonitoringPage addFilter() {
        return (MonitoringPage) super.addFilter();
    }

    @Override
    public MonitoringPage filterRecordsCheckbox() {
        return (MonitoringPage) super.filterRecordsCheckbox();
    }

    @Override
    public MonitoringPage okButtonPopUp() {
        return (MonitoringPage) super.okButtonPopUp();
    }

    @Override
    public MonitoringPage inputText(String id, String text) {
        return (MonitoringPage) super.inputText(id, text);
    }

    public MonitoringPage setEndDateDelay(int minutes) {
        String[] time = CalendarUtil.getDelay(minutes);
        endDateHours.clear();
        endDateHours.sendKeys(time[0]);
        endDateMinutes.clear();
        endDateMinutes.sendKeys(time[1]);
        return this;
    }

    public MonitoringPage setScheduledDelay(int minutes) {
        String[] time = CalendarUtil.getDelay(minutes);
        scheduledHours.clear();
        scheduledHours.sendKeys(time[0]);
        scheduledMinutes.clear();
        scheduledMinutes.sendKeys(time[1]);
        return this;
    }

    @Override
    public MonitoringPage waitForStatus(String status, int timeout) {
        return (MonitoringPage) super.waitForStatus(status, timeout);
    }

    @Override
    public MonitoringPage selectDate(String date) {
        return (MonitoringPage) super.selectDate(date);
    }

    @Override
    public MonitoringPage pause(long millis) {
        return (MonitoringPage) super.pause(millis);
    }

    @Override
    public MonitoringPage assertPresenceOfElements(String... ids) {
        return (MonitoringPage) super.assertPresenceOfElements(ids);
    }

    public MonitoringPage presetFilter(String parameter, String value) {
        new DeviceUpdatePage().presetFilter(parameter, value);
        return this;
    }

    public MonitoringPage selectImportGuFile() {
        switchToFrame(DESKTOP);
        String inputText = new File(getImportMonitorFile()).getAbsolutePath();
        importMonField.sendKeys(inputText);
        ((JavascriptExecutor) getDriver()).executeScript("__doPostBack('btnSaveConfiguration','')");
        return this;
    }

    public MonitoringPage leftMenu(Left item) {
        switchToFrame(ROOT);
        leftMenuClick(item.getValue());
        waitForUpdate();
        switchToFrame(DESKTOP);
        return this;
    }

    @Override
    public MonitoringPage deleteAllCustomViews() {
        return (MonitoringPage) super.deleteAllCustomViews();
    }

    public MonitoringPage fillCustomViewName() {
        customViewNameField.sendKeys(BaseTestCase.getTestName());
        return this;
    }

    public enum Left {
        /*VIEW("View"),*/ IMPORT("Import"), NEW("New");
        private String value;

        Left(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
