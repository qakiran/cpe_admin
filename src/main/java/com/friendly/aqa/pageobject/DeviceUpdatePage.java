package com.friendly.aqa.pageobject;

import com.friendly.aqa.entities.IGlobalButtons;
import com.friendly.aqa.entities.Table;
import com.friendly.aqa.entities.TopMenu;
import com.friendly.aqa.test.BaseTestCase;
import com.friendly.aqa.utils.CalendarUtil;
import com.friendly.aqa.utils.DataBaseConnector;
import com.friendly.aqa.utils.HttpConnector;
import com.friendly.aqa.utils.Timer;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.ParseException;
import java.time.Duration;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.friendly.aqa.entities.TopMenu.DEVICE_UPDATE;
import static com.friendly.aqa.pageobject.BasePage.FrameSwitch.*;
import static com.friendly.aqa.pageobject.DeviceUpdatePage.GlobalButtons.*;
import static com.friendly.aqa.pageobject.DeviceUpdatePage.Left.*;

public class DeviceUpdatePage extends BasePage {
    private static final Logger logger = Logger.getLogger(DeviceUpdatePage.class);
    private Date xmlFileTime;
    private Date csvFileTime;

    @FindBy(id = "btnSaveUsr_btn")
    private WebElement saveButton;

    @FindBy(id = "btnCancel_btn")
    private WebElement cancelButton;

    @FindBy(id = "btnDel_btn")
    private WebElement deleteButton;

    @FindBy(id = "btnRefresh_btn")
    private WebElement refreshButton;

    @FindBy(id = "btnClose_btn")
    private WebElement closeButton;

    @FindBy(id = "IsDefaultViewForUser")
    private WebElement defaultViewCheckbox;

    @FindBy(id = "btnReCheck_lnk")
    private WebElement recheckStatus;

    @FindBy(id = "btnReCheck_img")
    private WebElement recheckIcon;

    @FindBy(id = "txtSerial")
    private WebElement inputSerial;

    @FindBy(id = "Description")
    private WebElement portDescription;

    @FindBy(id = "RemoteIp")
    private WebElement remoteHost;

    @FindBy(id = "InternalIp")
    private WebElement computerIp;

    @FindBy(id = "RemotePort")
    private WebElement extPort;

    @FindBy(id = "InternalPort")
    private WebElement intPort;

    @FindBy(id = "Protocol")
    private WebElement protocol;

    @FindBy(id = "ddlSearchOption")
    private WebElement searchByCombobox;

    @FindBy(id = "ddlColumns1")
    private WebElement sortByCombobox;

    @FindBy(id = "ddlSort1")
    private WebElement sortingOrderCombobox;

    @FindBy(id = "ddlDiagType")
    private WebElement diagnosticCombobox;

    @FindBy(id = "btnSearch_btn")
    private WebElement searchButton;

    @FindBy(id = "rdDelAll")
    private WebElement deleteAllRButton;

    @FindBy(id = "btnClearAll_btn")
    private WebElement clearAllRButton;

    @FindBy(id = "rdTarget")
    private WebElement defaultUploadRButton;

    @FindBy(id = "rdUrl")
    private WebElement manualUrlRButton;

    @FindBy(id = "lblReplaceCpeHeader")
    private WebElement replaceHeader;

    @FindBy(id = "btnEditUserInfo_lnk")
    private WebElement editAccountInfoLink;

    @FindBy(id = "hlCollapseAll")
    private WebElement collapseLink;

    @FindBy(id = "hlExpandAll")
    private WebElement expandLink;

    @FindBy(id = "pager2_lblPagerTotal")
    private WebElement pager;

    @FindBy(id = "lblNoReprovisionFound")
    private WebElement noProvision;


    @Override
    protected String getLeftMenuCssSelector() {
        return null;
    }

    @Override
    public Table getMainTable() {
        return getTable("tbl");
    }

    @Override
    public DeviceUpdatePage topMenu(TopMenu value) {
        return (DeviceUpdatePage) super.topMenu(value);
    }

    @Override
    public DeviceUpdatePage assertMainPageIsDisplayed() {
        assertPresenceOfElements("tbl");
        return (DeviceUpdatePage) super.assertMainPageIsDisplayed();
    }

    @Override
    public DeviceUpdatePage pause(int millis) {
        return (DeviceUpdatePage) super.pause(millis);
    }

    @Override
    public DeviceUpdatePage selectItem(Table table, String text, int startFromRow) {
        List<Integer> list = table.getRowsWithText(text);
        for (int i : list) {
            if (i >= startFromRow) {
                table.clickOn(i, 0);
                break;
            }
        }
        waitForUpdate();
        return this;
    }

    public DeviceUpdatePage selectPort() {
        Table table = getTable("tblParameters");
        int row = table.textCellMatches("^tr069_du.+");
        if (row < 0) {
            throw new AssertionError("There's no suitable port to select!");
        }
        table.clickOn(row, 0, 0);
        if (parameterMap == null) {
            parameterMap = new HashMap<>();
        }
        parameterMap.put(table.getCellText(row, "Internal port"), table.getCellText(row, "Protocol"));
        return this;
    }

    @Override
    public DeviceUpdatePage newViewButton() {
        return (DeviceUpdatePage) super.newViewButton();
    }

    @Override
    public DeviceUpdatePage assertPresenceOfOptions(String comboBoxId, String... options) {
        return (DeviceUpdatePage) super.assertPresenceOfOptions(comboBoxId, options);
    }

    @Override
    public DeviceUpdatePage fillName() {
        return (DeviceUpdatePage) super.fillName();
    }

    @Override
    public DeviceUpdatePage fillName(String name) {
        return (DeviceUpdatePage) super.fillName(name);
    }

    @Override
    public DeviceUpdatePage assertInputHasText(String id, String text) {
        return (DeviceUpdatePage) super.assertInputHasText(id, text);
    }

    @Override
    public DeviceUpdatePage setViewColumns(int startParam, int endParam) {
        return (DeviceUpdatePage) super.setViewColumns(startParam, endParam);
    }

    @Override
    public DeviceUpdatePage selectColumnFilter(String option) {
        return (DeviceUpdatePage) super.selectColumnFilter(option);
    }

    @Override
    public DeviceUpdatePage selectCompare(String option) {
        return (DeviceUpdatePage) super.selectCompare(option);
    }

    @Override
    public DeviceUpdatePage selectFilterModelName(String value) {
        return (DeviceUpdatePage) super.selectFilterModelName(value);
    }

    @Override
    public DeviceUpdatePage filterRecordsCheckbox() {
        return (DeviceUpdatePage) super.filterRecordsCheckbox();
    }

    @Override
    public DeviceUpdatePage selectView(String value) {
        return (DeviceUpdatePage) super.selectView(value);
    }

    @Override
    public DeviceUpdatePage editButton() {
        return (DeviceUpdatePage) super.editButton();
    }

    @Override
    public DeviceUpdatePage clickOnTable(String id, int row, int column, int tagNum) {
        return (DeviceUpdatePage) super.clickOnTable(id, row, column, tagNum);
    }

    @Override
    public DeviceUpdatePage addSubFilter() {
        return (DeviceUpdatePage) super.addSubFilter();
    }

    @Override
    public DeviceUpdatePage andRadioButton() {
        return (DeviceUpdatePage) super.andRadioButton();
    }

    @Override
    public DeviceUpdatePage deleteFilter() {
        return (DeviceUpdatePage) super.deleteFilter();
    }

    public DeviceUpdatePage defaultViewForCurrentUserCheckbox() {
        defaultViewCheckbox.click();
        return this;
    }

    @Override
    public DeviceUpdatePage resetView() {
        return (DeviceUpdatePage) super.resetView();
    }

    @Override
    public DeviceUpdatePage clickOnTable(String id, int row, int column) {
        return (DeviceUpdatePage) super.clickOnTable(id, row, column);
    }

    @Override
    public DeviceUpdatePage assertButtonsAreEnabled(boolean enabled, IGlobalButtons... buttons) {
        return (DeviceUpdatePage) super.assertButtonsAreEnabled(enabled, buttons);
    }

    @Override
    public DeviceUpdatePage downButton() {
        return (DeviceUpdatePage) super.downButton();
    }

    @Override
    public DeviceUpdatePage upButton() {
        return (DeviceUpdatePage) super.upButton();
    }

    @Override
    public DeviceUpdatePage topButton() {
        return (DeviceUpdatePage) super.topButton();
    }

    @Override
    public DeviceUpdatePage bottomButton() {
        return (DeviceUpdatePage) super.bottomButton();
    }

    @Override
    public DeviceUpdatePage selectDownloadFileType(String type) {
        setImplicitlyWait(0);
        List<WebElement> list = findElements("UcFirmware1_ddlFileType");
        if (!list.isEmpty() && !list.get(0).isDisplayed()) {
            bottomMenu(ADD);
        }
        return (DeviceUpdatePage) super.selectDownloadFileType(type);
    }

    @Override
    public DeviceUpdatePage selectUploadFileType(String type) {
        waitForUpdate();
        setImplicitlyWait(0);
        List<WebElement> list = findElements("ddlFileType");
        if (list.isEmpty() || !list.get(0).isDisplayed()) {
            bottomMenu(ADD);
        }
        return (DeviceUpdatePage) super.selectUploadFileType(type);
    }

    @Override
    public DeviceUpdatePage selectFromListRadioButton() {
        return (DeviceUpdatePage) super.selectFromListRadioButton();
    }

//    @Override
//    public DeviceUpdatePage manualRadioButton() {
//        return (DeviceUpdatePage) super.manualRadioButton();
//    }

    public DeviceUpdatePage searchButton() {
        waitUntilElementIsEnabled("btnSearch_btn");
        searchButton.click();
        waitForUpdate();
        return this;
    }

    public DeviceUpdatePage lookFor(String value) {
        inputText("tbDeviceID", value);
        return this;
    }

    public DeviceUpdatePage inputSerial() {
        inputSerial.sendKeys(getSerial());
        waitUntilButtonIsEnabled(START);
        return this;
    }

    public DeviceUpdatePage refreshButton() {
        switchToFrame(POPUP);
        refreshButton.click();
        waitForUpdate();
        return this;
    }

    public void assertDuplicateNameErrorIsDisplayed() {
        setImplicitlyWait(0);
        List<WebElement> list = driver.findElements(By.id("lblNameInvalid"));
        setDefaultImplicitlyWait();
        if (list.size() == 1) {
            return;
        }
        String warn = "Error message 'This name is already in use' not found on current page!";
        logger.warn(warn);
        throw new AssertionError(warn);
    }

    public void checkFiltering(String filter) {
        WebElement comboBox = filter.equals("Manufacturer") ? filterManufacturerComboBox : filterModelNameComboBox;
        List<String> optionList = getOptionList(comboBox);
        optionList.remove("All");
        for (String option : optionList) {
            selectComboBox(comboBox, option);
            waitForUpdate();
            Set<String> itemSet = new HashSet<>(Arrays.asList(getMainTable().getColumn(filter)));
            if (itemSet.size() > 1 || (itemSet.size() == 1 && !itemSet.iterator().next().equals(option))) {
                throw new AssertionError("Column '" + filter + "' has unexpected content!\n"
                        + "Expected: " + option + ", but found: " + itemSet);
            }
        }
    }

    public DeviceUpdatePage searchBy(String value) {
        selectComboBox(searchByCombobox, value);
        searchByCombobox.click();
        return this;
    }

    public DeviceUpdatePage sortByColumn(String value) {
        selectComboBox(sortByCombobox, value);
        sortByCombobox.click();
        return this;
    }

    public DeviceUpdatePage sortingOrder(String value) {
        selectComboBox(sortingOrderCombobox, value);
        sortingOrderCombobox.click();
        return this;
    }

    public void assertSortingPerformedBy(String column, Boolean isAscending) {
        Table table = getMainTable();
        WebElement cell = table.getCellWebElement(0, table.getColumnNumber(0, column));
        setImplicitlyWait(0);
        List<WebElement> img = cell.findElements(By.tagName("img"));
        setDefaultImplicitlyWait();
        if (isAscending == null) {
            if (img.size() != 0) {
                throw new AssertionError("Unexpected sorting found by column '" + column + "' (Expected: unsorted).");
            }
        } else if (img.size() == 0) {
            throw new AssertionError("No sorting found by column '" + column + "' (Expected: " + (isAscending ? "ascending" : "descending") + ").");
        } else if (img.get(0).getAttribute("src").endsWith("down.png") == isAscending) {
            throw new AssertionError("Unexpected sorting type found by column '" + column + "' (Expected: " + (isAscending ? "ascending" : "descending") + ").");
        }
    }

    public void assertChangingView() {
        selectView("Default");
        String[] defColumns = getMainTable().getRow(0);
        List<String> optList = getOptionList(filterViewComboBox);
        for (String option : optList) {
            if (option.equals("Default")) {
                continue;
            }
            selectView(option);
            String[] newColumns = getMainTable().getRow(0);
            if (!Arrays.deepEquals(newColumns, defColumns)) {
//                System.out.println(option + ": OK\n" + Arrays.deepToString(defColumns) + "\n" + Arrays.deepToString(newColumns));
                return;
            }
//            System.out.println(option + ": Fail\n" + Arrays.deepToString(defColumns) + "\n" + Arrays.deepToString(newColumns));
        }
        throw new AssertionError("View has not been changed! Make sure that you have enough different view for this test case!");
    }

    public DeviceUpdatePage assertSelectedViewIs(String expectedView) {
        if (getSelectedOption(filterViewComboBox).toLowerCase().equals(expectedView.toLowerCase())) {
            return this;
        }
        throw new AssertionError("Actual and expected view don't match! Expected: " + expectedView
                + "; actual: " + getSelectedOption(filterViewComboBox));
    }

    public DeviceUpdatePage presetFilter(String parameter, String value) {
        topMenu(DEVICE_UPDATE);
        enterToDevice();
        Table table = new Table("tblUserInfo");
        if (!table.isEmpty()) {
            try {
                int rowNum = table.getRowNumberByText(parameter);
                if (rowNum >= 0 && table.getCellText(rowNum, 1).equals(value)) {
                    return this;
                }
            } catch (AssertionError e) {
                System.out.println("filter does not exist, go to create new one");
            }
        }
        editAccountInfoLink.click();
        switchToFrame(POPUP);
        WebElement saveButton = driver.findElement(By.id("btnSaveUsr_btn"));
        setUserInfo(parameter, value);
        waitUntilElementIsEnabled("btnSaveUsr_btn");
        saveButton.click();
        okButtonPopUp();
        pause(500);
        return this;
    }

    public void createPreconditionsForSorting() {
        waitForUpdate();
        Table mainTable = getMainTable();
        String[] serials = mainTable.getColumn("Serial");
        for (String serial : serials) {
            mainTable.clickOn(serial);
            setAllUserInfo();
            topMenu(DEVICE_UPDATE);
            mainTable = getMainTable();
        }
    }

    public DeviceUpdatePage setAllUserInfo() {
        editAccountInfoLink.click();
        switchToFrame(POPUP);
        Table userInfoTable = getTable("tblMain", 18, true);
        String[] items = userInfoTable.getWholeColumn(0);
        for (int i = 0; i < items.length; i++) {
            String item = items[i];
            if (userInfoTable.getInputText(i, 1).isEmpty()) {
                setUserInfo(userInfoTable, item, getRandomStringValue(10));
            }
        }
        try {
            waitUntilElementIsEnabled("btnSaveUsr_btn");
            saveButton.click();
            okButtonPopUp();
            pause(500);
            waitForUpdate();
        } catch (AssertionError e) {
            cancelButton.click(); // in case no one item has been changed
        } finally {
            switchToFrame(DESKTOP);
        }
        return this;
    }

    public DeviceUpdatePage enterToDevice() {
        return enterToDevice(getSerial(), getMainTable());
    }

    public DeviceUpdatePage editAccountInfoLink() {
        editAccountInfoLink.click();
        return this;
    }

    public DeviceUpdatePage clearUserInfo() {
        switchToFrame(POPUP);
        clearAllRButton.click();
        okButtonPopUp();
        return this;
    }

    public void assertAccountInfoIsClear() {
        Timer timer = new Timer();
        while (!timer.timeout()) {
            Table table = getTable("tblUserInfo");
            if (table.getTableSize()[0] == 0) {
                return;
            }
        }
        throw new AssertionError("Account info is not empty!");
    }

    public DeviceUpdatePage enterToAnyDevice() {
        Table table = getMainTable();
        String[] serials = table.getColumn("Serial");
        for (String serial : serials) {
            if (!getSerial().equals(serial)) {
                parameterSet = new HashSet<>();
                parameterSet.add(serial);
                return enterToDevice(serial, getMainTable());
            }
        }
        throw new AssertionError("there are no suitable devices to be selected!");
    }

    public DeviceUpdatePage enterToDevice(String serial, Table table) {
        try {
            table.clickOn(serial);
            pause(500);
        } catch (AssertionError e) {
            selectComboBox(itemsOnPageComboBox, "200");
            waitForUpdate();
            getMainTable().clickOn(serial);
        }
        waitForUpdate();
        return this;
    }

    public DeviceUpdatePage deselectCheckbox(String id) {
        WebElement checkbox = findElement(id);
        if (checkbox.isSelected()) {
            checkbox.click();
        }
        return this;
    }

    public DeviceUpdatePage selectCheckbox(String id) {
        WebElement checkbox = findElement(id);
        if (!checkbox.isSelected()) {
            checkbox.click();
        }
        return this;
    }

    public void setUserInfo(String paramName, String value) {
        setUserInfo(new Table("tblMain"), paramName, value);
    }

    public void setUserInfo(Table table, String paramName, String value) {
        int rowNum = table.getRowNumberByText(0, paramName);
        if (rowNum < 0) {
            throw new AssertionError("Parameter name '" + paramName + "' not found");
        }
        WebElement paramCell = table.getCellWebElement(rowNum, 1);
        if (props.getProperty("browser").equals("edge")) {
            BasePage.scrollToElement(paramCell);
        }
        WebElement input = paramCell.findElement(By.tagName("input"));
        input.clear();
        pause(300);
        input.sendKeys(value);
    }

    public DeviceUpdatePage bottomMenu(GlobalButtons button) {
        clickGlobalButtons(button);
        return this;
    }

    public DeviceUpdatePage setParameter(String parameter, String value) {
        waitForUpdate();
        Table table = getTable("tblParamsTable");
        int rowNum = table.getRowNumberByText(parameter);
        if (parameterMap == null) {
            parameterMap = new HashMap<>();
        }
        String hint = table.getHint(rowNum);
        WebElement paramCell = table.getCellWebElement(rowNum, 1);
//        if (props.getProperty("browser").equals("edge")) {
//            scrollToElement(paramCell);
//        }
        if (value != null) {
            waitForUpdate();
            WebElement input = paramCell.findElement(By.tagName("input"));
            input.clear();
            input.sendKeys(value);
        }
        parameterMap.put(hint, value);
        if (!BROWSER.equals("edge")) {
            table.clickOn(0, 0);
        }
        return this;
    }

    @Override
    public DeviceUpdatePage okButtonPopUp() {
        return (DeviceUpdatePage) super.okButtonPopUp();
    }

    public DeviceUpdatePage selectTab(String tab) {
        return (DeviceUpdatePage) super.selectTab(tab);
    }

    @Override
    public DeviceUpdatePage cancelButtonPopUp() {
        return (DeviceUpdatePage) super.cancelButtonPopUp();
    }

    public DeviceUpdatePage leftMenu(Left item) {
        switchToFrame(ROOT);
        getTable("tblLeftMenu").clickOn(item.value);
        waitForUpdate();
        switchToFrame(DESKTOP);
        return this;
    }

    public DeviceUpdatePage selectAnyDevice() { //except target device
        Table table = getMainTable();
        String[] serials = table.getColumn("Serial");
        for (int i = 0; i < serials.length; i++) {
            String serial = serials[i];
            if (!getSerial().equals(serial)) {
                table.clickOn(i + 1, 0);
                parameterSet = new HashSet<>();
                parameterSet.add(serial);
                return this;
            }
        }
        throw new AssertionError("there are no suitable devices to be selected!");
    }

    public void assertAbsenceOfValue() {
        String value = parameterSet.iterator().next();
        waitForUpdate();
        verifySinglePage();
        if (getMainTable().contains(value)) {
            throw new AssertionError("Value '" + value + "' still present in the table!");
        }
    }

    public DeviceUpdatePage assertTraceWindowIsOpened() {
        switchToNewWindow();
        assertEquals(findElement("lblTitle").getText(), "Trace of: Serial = " + getSerial()
                + " / ID = " + DataBaseConnector.getDeviceId(getSerial()));
        closeNewWindow();
        return this;
    }

    public DeviceUpdatePage clearDeviceActivity() {
        leftMenu(DEVICE_ACTIVITY);
        waitForUpdate();
        if (pager.getText().equals("No records found")) {
            return this;
        }
        waitUntilButtonIsDisplayed(REFRESH);
        deleteAllRButton.click();
        bottomMenu(DELETE);
        okButtonPopUp();
        return this;
    }

    public DeviceUpdatePage clearProvisionManager() {
        leftMenu(PROVISION_MANAGER);
        waitForUpdate();
        if (noProvision.getText().equals("There is no provision data to display")) {
            return this;
        }
        bottomMenu(EDIT);
        waitUntilButtonIsDisplayed(CANCEL);
        Table tabTable = null;
        while (!(tabTable = getTable("tabsMain_tblTabs")).isEmpty()) {
            tabTable.clickOn(tabTable.getNotEmptyContentList().get(0));
            waitForUpdate();
            Table table = getTable("tblItems");
            table.clickOn(0, 0, 0);
            bottomMenu(DELETE);
            okButtonPopUp();
            waitForUpdate();
            if (noProvision.getText().equals("There is no provision data to display")) {
                return this;
            }
        }
        throw new AssertionError("Unexpected Provision Manager behavior");
    }

    public DeviceUpdatePage saveFileName() {
        switchToFrame(ROOT);
        String message = findElement("spnAlert").getText();
        Pattern datePattern = Pattern.compile("Inventory_.+_(.+)\\)\\.");
        Pattern extPattern = Pattern.compile("\\)\\.([xmlcsv]{3})'");
        Matcher m = datePattern.matcher(message);
        Matcher extM = extPattern.matcher(message);
        if (m.find()) {
            try {
                Date date = CalendarUtil.getDate(m.group(1));
                if (extM.find()) {
                    if (extM.group(1).equals("csv")) {
                        csvFileTime = date;
                    } else if (extM.group(1).equals("xml")) {
                        xmlFileTime = date;
                    } else {
                        throw new AssertionError("File extension parsing error!");
                    }
                }
            } catch (ParseException e) {
                System.out.println("Date parsing error! \n" + message);
            }
        }
        return this;
    }

    public void checkSavedExport(String... extensions) {
        switchToFrame(POPUP);
        Table table = getTable("tbl");
        for (String ext : extensions) {
            if (!ext.equalsIgnoreCase("csv") && !ext.equalsIgnoreCase("xml")) {
                throw new AssertionError("Unsupported file type!");
            }
            if (ext.equalsIgnoreCase("csv")) {
                table.assertPresenceOfValue(1, "Report(Inventory_Default_" + CalendarUtil.getCsvFileFormat(csvFileTime) + ").csv");
            }
            if (ext.equalsIgnoreCase("xml")) {
                table.assertPresenceOfValue(1, "Report(Inventory_Default_" + CalendarUtil.getCsvFileFormat(xmlFileTime) + ").xml");
            }
        }
    }

    public DeviceUpdatePage deleteExportEntry() {
        switchToFrame(POPUP);
        Table table = getTable("tbl");
        table.clickOn(1, 0);
        parameterSet = new HashSet<>(1);
        parameterSet.add(table.getCellText(1, 1));
        deleteButton.click();
        return this;
    }

    public DeviceUpdatePage deleteAllExportEntries() {
        switchToFrame(POPUP);
        Table table = getTable("tbl");
        table.clickOn(0, 0);
        deleteButton.click();
        return this;
    }

    public DeviceUpdatePage assertAbsenceOfDeletedExportItem() {
        switchToFrame(POPUP);
        assertAbsenceOfValue();
        return this;
    }

    public void assertExportEntryListIsEmpty() {
        switchToFrame(POPUP);
        if (!pager.getText().equals("No data found")) {
            throw new AssertionError("List of exports is not empty!");
        }
        closeButton.click();
    }

    public void closeMapWindow() {
        switchToFrame(ROOT);
        pause(2000);
        getTable("tblPopupTitle").clickOn(0, 1);
        switchToFrame(DESKTOP);
        enterToDevice();
        waitForUpdate();
    }

    public void validateSearchBy(String option, boolean exactMatch) {
        searchBy(option);
        String[][] opt = {{"Phone number", "telephone"}, {"User ID", "userid"}, {"Full name", "name"}, {"Username", "login_name"}, {"User Tag", "user_tag"},
                {"Serial Number", "Serial"}, {"IP address", "IP address"}, {"MAC address", "MAC address"}, {"ACS Username", "ACS Username"}};
        if (exactMatch) {
            selectCheckbox("rdSearchExactly");
        } else {
            deselectCheckbox("rdSearchExactly");
        }
        for (String[] strings : opt) {
            if (option.equalsIgnoreCase(strings[0])) {
                if (elementIsPresent("pager2_tblPager")) {
                    selectComboBox(itemsOnPageComboBox, "200");
                    waitForUpdate();
                }
                Map<String, Set<String>> dbDeviceMap = DataBaseConnector.getCustomDeviceInfoByColumn(strings[1], exactMatch);
                if (dbDeviceMap != null && !dbDeviceMap.isEmpty()) {
                    String key = dbDeviceMap.keySet().iterator().next();
                    System.out.println("key:" + key);
                    lookFor(key);
                    clickOn("tbDeviceID");
                    searchButton();
                    Set<String> set = dbDeviceMap.get(key);
                    int dbResponseSize = set.size();
                    String error = "Search by '" + option + "' failed. Expected number of items: " + dbResponseSize + ", but actual: ";
                    if (dbResponseSize == 1) {
                        if (elementIsPresent("pager2_lblPagerTotal")) {
                            throw new AssertionError(error + "0 or more than 1");
                        }
                        Table infoTable = getTable("tblDeviceInfo");
                        String actual = infoTable.getCellText("Serial Number:", 1);
                        String expected = set.iterator().next();
                        if (!actual.equalsIgnoreCase(expected)) {
                            throw new AssertionError("Wrong device found! Expected: " + expected + "; actual: " + actual);
                        }
                        leftMenu(Left.SEARCH);
                    } else {
                        if (elementIsAbsent("pager2_lblPagerTotal")) {
                            throw new AssertionError(error + "1");
                        }
                        if (!pager.getText().equals("Total:")) {
                            throw new AssertionError(error + "0");
                        }
                        Table table = getMainTable();
                        int tableSize = table.getTableSize()[0] - 1;
                        if (dbResponseSize != tableSize) {
                            throw new AssertionError(error + (tableSize));
                        }
                        set.removeAll(Arrays.asList(table.getColumn("Serial")));
                        if (set.size() != 0) {
                            throw new AssertionError("one or more devices were not found: " + set.toString());
                        }
                    }
                }
                lookFor("*wrong*");
                clickOn("tbDeviceID");
                searchButton();
                if (elementIsAbsent("pager2_lblPagerTotal") || !pager.getText().equalsIgnoreCase("No data found")) {
                    throw new AssertionError("Unexpected search result! Expected: empty list (No data found)");
                }
                return;
            }
        }
    }

    public void assertTransferToDeviceInfo() {
        if (elementIsPresent("pager2_lblPagerTotal")) {
            throw new AssertionError("Search failed! Expected: only one device '" + getSerial() + "' found!");
        }
        Table infoTable = getTable("tblDeviceInfo");
        String actual = infoTable.getCellText("Serial Number:", 1);
        if (!actual.equalsIgnoreCase(getSerial())) {
            throw new AssertionError("Wrong device found! Expected: " + getSerial() + "; actual: " + actual);
        }
    }

    public void recheckStatus() {
        waitForClickableOf(recheckStatus).click();
        waitForClickableOf(recheckStatus);
        new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(30))
                .pollingEvery(Duration.ofMillis(100))
                .until(ExpectedConditions.invisibilityOf(findElement("divCheckStatusProgress")));
        assertPresenceOfElements("btnReCheck_img");
    }

    public void assertLastActivityIs(String activity) {
        Table table = getTable("tblParameters");
        if (!table.getCellText(1, 2).equalsIgnoreCase(activity)) {
            throw new AssertionError("Activity '" + activity + "' not found in the top row of activity list!");
        }
    }

    public void assertActivityIsPresent(String activity) {
        Table table = getTable("tblParameters");
        if (!table.contains(activity)) {
            throw new AssertionError("Activity '" + activity + "' not found in the top row of activity list!");
        }
    }

    private void assertWindowIsOpened(String validText) {
        switchToFrame(POPUP);
        new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(IMPLICITLY_WAIT * 2))
                .pollingEvery(Duration.ofMillis(100))
                .until(ExpectedConditions.textToBePresentInElementValue(By.id("txtResponse"), validText));
        closeButton.click();
        switchToPreviousFrame();
    }

    public void assertPingWindowIsOpened() {
        assertWindowIsOpened("Ping statistics for");
    }

    public void assertTracerouteWindowIsOpened() {
        assertWindowIsOpened("Trace complete");
    }

    public void assertReplaceWindowIsOpened() {
        switchToFrame(POPUP);
        assertPresenceOfElements("lblReplaceCpeHeader");
        cancelButton.click();
        switchToPreviousFrame();
    }

    private void deviceInfoFakeLink(String link) {
        Table table = getTable("tblDeviceInfo");
        int row = table.getRowNumberByText(0, link);
        table.clickOn(row, 1, 0);
    }

    public DeviceUpdatePage listOfMethods() {
        deviceInfoFakeLink("List of methods:");
        return this;
    }

    public DeviceUpdatePage networkMap() {
        deviceInfoFakeLink("Network map:");
        return this;
    }

    public DeviceUpdatePage assertMapIsPresent() {
        switchToFrame(POPUP);
        assertPresenceOfElements("tblBig");
        switchToPreviousFrame();
        return this;
    }

    public DeviceUpdatePage assertMethodIsPresent(String methodName) {
        switchToFrame(POPUP);
        Table table = getTable("tblFunctions");
        table.assertPresenceOfValue(0, methodName);
        switchToPreviousFrame();
        return this;
    }

    private void setParameter(Table table, String paramName, String value) {
        int row = table.getRowNumberByText(paramName);
        if (parameterMap == null) {
            parameterMap = new HashMap<>();
        }
        String hint = table.getHint(row);
        parameterMap.put(hint, value);
        WebElement input = null;
        WebElement select = null;
        setImplicitlyWait(0);
        List<WebElement> inputList = table.getCellWebElement(row, 1).findElements(By.tagName("input"));
        if (inputList.size() > 0) {
            input = inputList.get(0);
        } else {
            List<WebElement> selectList = table.getCellWebElement(row, 1).findElements(By.tagName("select"));
            if (selectList.size() > 0) {
                select = selectList.get(0);
            }
        }
        setDefaultImplicitlyWait();
        if (input != null && input.isEnabled()) {
            if (input.getAttribute("type").equals("checkbox")) {
                input.click();
            } else if (input.getAttribute("value").equals("*****")) {
                input.clear();
                input.sendKeys("" + (int) (Math.random() * 1000000));
                waitForUpdate();
                input.sendKeys(Keys.BACK_SPACE);
                waitForUpdate();
            } else if (!input.getAttribute("value").equals(value)) {
                input.clear();
                input.sendKeys(value + " ");
                waitForUpdate();
                input.sendKeys(Keys.BACK_SPACE);
                waitForUpdate();
            }
        } else if (select != null && select.isEnabled()) {
            selectComboBox(select, value);
        }
    }

    public DeviceUpdatePage setParameter(String tab, int amount) {
        if (tab != null) {
            waitForUpdate();
            selectTab(tab);
        }
        Table table = new Table("tblParamsTable");
        String[] names = table.getColumn(0);
        for (int i = 0; i < Math.min(Math.abs(amount), names.length); i++) {
            String hint = table.getHint(i + 1);
            WebElement input = null;
            WebElement select = null;
            setImplicitlyWait(0);
            List<WebElement> inputList = table.getCellWebElement(i + 1, 1).findElements(By.tagName("input"));
            if (inputList.size() > 0) {
                if (!inputList.get(0).isEnabled()) {
                    amount++;
                    continue;
                }
                input = inputList.get(0);
            } else {
                List<WebElement> selectList = table.getCellWebElement(i + 1, 1).findElements(By.tagName("select"));
                if (selectList.size() > 0) {
                    if (!selectList.get(0).isEnabled()) {
                        amount++;
                        continue;
                    }
                    select = selectList.get(0);
                }
            }
            setDefaultImplicitlyWait();
            String value = "";
            if (input != null) {
                if (input.getAttribute("type").equals("text")) {
                    if (names[i].equalsIgnoreCase("password") || names[i].equalsIgnoreCase("KeyPassphrase")) {
                        value = "*****";
                    } else {
                        String currentValue = input.getAttribute("value");
                        String s = generateValue(hint, amount < 0 ? (int) (10000 * Math.random()) : i + 1);
                        value = s.equals(currentValue) ? generateValue(hint, i + 20) : s;
                    }
                } else {
                    value = input.isSelected() ? "0" : "1";
                }
            } else if (select != null) {
                String selected = getSelectedOption(select);
                value = selected;
                for (String opt : getOptionList(select)) {
                    if (!opt.equals(selected)) {
                        value = opt;
                        break;
                    }
                }
            }
            setParameter(table, names[i], value);
            table.clickOn(i, 2);
            waitForUpdate();
        }
        return this;
    }

    public void validateAbsenceTaskWithValue(String value) {
        String[] col = getTable("tblParameters").getColumn("Value");
        if (Arrays.asList(col).contains(value)) {
            throw new AssertionError("Task with value '" + value + "' is present in the list!");
        }
    }

    public DeviceUpdatePage validateTasks() {
        verifySinglePage();
        Set<Map.Entry<String, String>> entrySet = parameterMap.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            checkAddedTask("tblParameters", entry.getKey(), entry.getValue(), 6);
        }
        return this;
    }

    public void validateProvisionTasks() {
        Set<Map.Entry<String, String>> entrySet = parameterMap.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            checkAddedTask("tblItems", entry.getKey(), entry.getValue(), 4);
        }
    }

    public void validateProvisionDownloadTasks() {
        Table tabTable = getTable("tabsMain_tblTabs");
        if (!tabTable.getNotEmptyContentList().contains("Download file")) {
            throw new AssertionError("Download file tab not found!");
        }
        tabTable.clickOn("Download file");
        Table table = getTable("tblItems");
        Set<Map.Entry<String, String>> entrySet = parameterMap.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            if (!table.contains(entry.getValue())) {
                table.print();
                throw new AssertionError("Item '" + entry.getValue() + "' not found on current table!");
            }
        }
    }

    public DeviceUpdatePage validateDownloadFileTasks() {
        Set<Map.Entry<String, String>> entrySet = parameterMap.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            checkAddedTask("tblParameters", entry.getKey(), entry.getValue(), 7);
        }
        return this;
    }

    public void validateUploadFileTasks() {
        Set<Map.Entry<String, String>> entrySet = parameterMap.entrySet();
        label:
        for (Map.Entry<String, String> entry : entrySet) {
            Table table = getTable("tblParameters");
            List<Integer> list = table.getRowsWithText(entry.getKey());
            for (int i : list) {
                System.out.println("text: " + table.getCellText(i, "Parameter name"));
                if (table.getCellText(i, "Parameter name").matches(entry.getValue())) {
                    continue label;
                }
            }
            throw new AssertionError("Cannot find entry that matches the regex '" + entry.getValue() + "'");
        }
    }

    @Override
    public DeviceUpdatePage selectBranch(String branch) {
        return (DeviceUpdatePage) super.selectBranch(branch);
    }

    @Override
    public DeviceUpdatePage selectTreeObject(boolean clickOnCheckbox, int objNum) {
        return (DeviceUpdatePage) super.selectTreeObject(clickOnCheckbox, objNum);
    }

    public DeviceUpdatePage selectFileName() {
        waitForUpdate();
        List<String> optList = getOptionList(fileNameComboBox);
        String lastOpt = optList.get(optList.size() - 1);
        selectComboBox(fileNameComboBox, lastOpt);
        if (parameterMap == null) {
            parameterMap = new HashMap<>();
        }
        parameterMap.put("Download", props.getProperty("upload_url") + '/' + lastOpt);
        return this;
    }

    public DeviceUpdatePage defaultUploadRadioButton() {
        defaultUploadRButton.click();
        waitForUpdate();
        if (parameterMap == null) {
            parameterMap = new HashMap<>();
        }
        String url = "^" + props.getProperty("upload_url") + '/' + DataBaseConnector.getGroupId(getSerial())
                + '_' + getSerial() + '_' + CalendarUtil.getDateByPattern("yyyy.MM.dd") + '_' + ".+\\."
                + (getSelectedOption(selectUploadFileTypeComboBox).startsWith("Vendor Conf") ? "cfg$" : "log$");
        parameterMap.put("Upload", url);
        return this;
    }

    @Override
    public DeviceUpdatePage fillUrl() {
        fromListRadioButton.click();
        List<String> optList = getOptionList(fileNameComboBox);
        String lastOpt = optList.get(optList.size() - 1);
        manualRadioButton();
        String value = props.getProperty("upload_url") + '/' + lastOpt;
        urlField.sendKeys(value);
        if (parameterMap == null) {
            parameterMap = new HashMap<>();
        }
        parameterMap.put("Download", value);
        return this;
    }

    @Override
    public DeviceUpdatePage fillUploadUrl() {
        super.fillUploadUrl();
        if (parameterMap == null) {
            parameterMap = new HashMap<>();
        }
        parameterMap.put("Upload", props.getProperty("upload_url"));
        return this;
    }

    public DeviceUpdatePage manualUrlRButton() {
        manualUrlRButton.click();
        waitForUpdate();
        return this;
    }

    public void validateObjectTree() {
        Timer timer = new Timer();
        Table table = getTable("tblTree");
        int defaultExpanded = table.getVisibleRowsNumber();
        if (defaultExpanded < 2) {
            throw new AssertionError("Expected device tree state: expanded, but visible rows number is " + defaultExpanded);
        }
        collapseLink.click();
        table = getTable("tblTree");
        if (table.getVisibleRowsNumber() != 1) {
            throw new AssertionError("Expected device tree state: collapsed, but visible rows number is more than 1");
        }
        setImplicitlyWait(0);
        List<WebElement> images = table.getCellWebElement(0, 0).findElements(By.tagName("img"));
        if (!images.isEmpty()) {
            if (!images.get(0).getAttribute("src").endsWith("expand.png")) {
                throw new AssertionError("Expected top node icon is 'PLUS'!");
            }
        }
        expandLink.click();
        table = getTable("tblTree");
        int allExpanded = table.getVisibleRowsNumber();
        if (allExpanded < defaultExpanded) {
            throw new AssertionError("Expanded objects number is less than default expanded tree it has" + defaultExpanded);
        }
        images = table.getCellWebElement(0, 0).findElements(By.tagName("img"));
        if (!images.isEmpty()) {
            if (!images.get(0).getAttribute("src").endsWith("collapse.png")) {
                throw new AssertionError("Expected top node icon is 'MINUS'!");
            }
        }
        table.clickOn(0, 0, 0);
        table = getTable("tblTree");
        if (table.getVisibleRowsNumber() != 1) {
            throw new AssertionError("Expected device tree state: collapsed, but visible rows number is more than 1");
        }
        images = table.getCellWebElement(0, 0).findElements(By.tagName("img"));
        if (!images.isEmpty()) {
            if (!images.get(0).getAttribute("src").endsWith("expand.png")) {
                throw new AssertionError("Expected top node icon is 'PLUS'!");
            }
        }
        table.clickOn(0, 0, 0);
        table = getTable("tblTree");
        if (table.getVisibleRowsNumber() != allExpanded) {
            throw new AssertionError("Expanded objects number is less than previous expanded tree it has" + defaultExpanded);
        }
        images = table.getCellWebElement(0, 0).findElements(By.tagName("img"));
        if (!images.isEmpty()) {
            if (!images.get(0).getAttribute("src").endsWith("collapse.png")) {
                throw new AssertionError("Expected top node icon is 'MINUS'!");
            }
        }
        System.out.println(timer.stop() + "ms");
    }

    public void validateCsvFile() throws IOException {
        Robot robot = null;
        try {
            robot = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        pause(1000);
        if (robot != null) {
            robot.keyPress(KeyEvent.VK_ESCAPE);
            robot.keyRelease(KeyEvent.VK_ESCAPE);
        }
        if (!HttpConnector.sendGetRequest(props.getProperty("ui_url") + "/CPEs/ExportParams.aspx?fileType=csv").contains(getModelName())) {
            throw new AssertionError("Model Name not found in downloaded csv file!");
        }
    }

    public DeviceUpdatePage storePath(String path) {
        waitForUpdate();
        if (parameterMap == null) {
            parameterMap = new HashMap<>();
        }
        parameterMap.put("Get parameter attributes", path);
        parameterMap.put("Get parameter values", path);
        parameterMap.put("Get parameter names", path);
        return this;
        //InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANPPPConnection.1.
    }

    public void validateGeneratedGets() {
        Set<Map.Entry<String, String>> entrySet = parameterMap.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            checkAddedTask("tblParameters", entry.getKey(), entry.getValue(), 7);
        }
    }

    public DeviceUpdatePage storePath() {
        return storePath(getElementText("divPath") + '.');
    }

    public DeviceUpdatePage storePortMappingPath() {
        return storePath("InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANPPPConnection.1.");
    }

    public DeviceUpdatePage fillAddPortFields(String protocol) {
        if (parameterMap == null) {
            parameterMap = new HashMap<>();
        }
        String desc = BaseTestCase.getTestName();
        String intIp = "192.168.1.111";
        String extIp = "255.255.255.255";
        String intPort = String.valueOf((int) (Math.random() * 65536));
        String extPort = String.valueOf((int) (Math.random() * 65536));
        inputText2(remoteHost, extIp);
        parameterMap.put("Remote host", extIp);
        inputText(this.extPort, extPort);
        parameterMap.put("External port", extPort);
        inputText(this.intPort, intPort);
        parameterMap.put("Internal port", intPort);
        inputText2(computerIp, intIp);
        parameterMap.put("Internal client", intIp);
        selectComboBox(this.protocol, protocol);
        parameterMap.put("Protocol", protocol);
        inputText2(portDescription, desc);
        parameterMap.put("Description", desc);
        pause(1000);
        return this;
    }

    public void validatePortCreating() {
        Timer timer = new Timer(150000);
        verifySinglePage();
        List<String[]> tasks = new ArrayList<>(2);
        String[] example = {"", parameterMap.get("Description"), parameterMap.get("Internal client"), parameterMap.get("Internal port"),
                "", parameterMap.get("Remote host"), parameterMap.get("External port")};
        if (parameterMap.get("Protocol").contains("TCP")) {
            example[4] = "TCP";
            tasks.add(example.clone());
        }
        if (parameterMap.get("Protocol").contains("UDP")) {
            example[4] = "UDP";
            tasks.add(example);
        }
        while (!timer.timeout()) {
            int success = 0;
            Table table = getTable("tblParameters");
            List<Integer> rows = table.getRowsWithText(parameterMap.get("Description"));
            for (String[] task : tasks) {
                for (int row : rows) {
                    if (Arrays.deepEquals(table.getRow(row), task)) {
                        success++;
                        break;
                    }
                }
            }
            if (success == tasks.size()) {
                return;
            }
            pause(1000);
        }
        throw new AssertionError("One or more port mapping item not found!");
    }

    public void verifyPortDeletion() {
        String targetPort = parameterMap.keySet().iterator().next();
        String targetProtocol = parameterMap.get(targetPort);
        verifySinglePage();
        Timer timer = new Timer(60000);
        while (!timer.timeout()) {
            boolean fail = false;
            Table table = getTable("tblParameters");
            String[] ports = table.getColumn("Internal port");
            String[] protocols = table.getColumn("Protocol");
            for (int i = 0; i < ports.length; i++) {
                String port = ports[i];
                String protocol = protocols[i];
                if (targetPort.equals(port) && targetProtocol.equals(protocol)) {
                    fail = true;
                    break;
                }
            }
            if (!fail) {
                return;
            }
            pause(1000);
        }
    }

    public DeviceUpdatePage createDiagnostic(String diagType) {
        try {
            selectComboBox(diagnosticCombobox, diagType);
        } catch (org.openqa.selenium.NoSuchElementException e) {
            throw new AssertionError("Current device doesn't support " + diagType + "!");
        }
        waitForUpdate();
        setImplicitlyWait(0);
        List<WebElement> hostList = findElements("txtHost");
        List<WebElement> dnsList = findElements("txtDnsServer");
        if (hostList.size() > 0) {
            inputText(hostList.get(0), "8.8.8.8 ");
        }
        if (dnsList.size() > 0) {
            inputText(dnsList.get(0), "8.8.8.8 ");
        }
        setDefaultImplicitlyWait();
        parameterSet = new HashSet<>(1);
        parameterSet.add(diagType);
        return this;
    }

    public void validateDiagnosticCreation() {
        Table table = getTable("tblParameters");
        String diagnostic = parameterSet.iterator().next();
        if (!table.contains(diagnostic)) {
            throw new AssertionError(diagnostic + " not found!");
        }
    }

    public DeviceUpdatePage deleteAllDiagnostics() {
        setImplicitlyWait(1);
        List<WebElement> pageList = findElements("pager2_lblPagerTotal");
        if (!pageList.isEmpty()) {
            Table table = getTable("tblParameters");
            table.clickOn(0, 0, 0);
            bottomMenu(DELETE);
            okButtonPopUp();
        }
        setDefaultImplicitlyWait();
        return this;
    }

    public enum Left {
        LIST("List"), DEVICE_INFO("Device Info"), DEVICE_SETTINGS("Device Settings"), ADVANCED_VIEW("Advanced View"),
        PROVISION_MANAGER("Provision Manager"), DEVICE_MONITORING("Device Monitoring"), FILE_DOWNLOAD("File Download"),
        FILE_UPLOAD("File Upload"), DEVICE_DIAGNOSTIC("Device Diagnostics"), CUSTOM_RPC("Custom RPC"),
        DEVICE_HISTORY("Device History"), DEVICE_ACTIVITY("Device Activity"), SEARCH("Search"), PORT_MAPPING("Port Mapping");

        private final String value;

        Left(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    public enum GlobalButtons implements IGlobalButtons {

        ACTIVATE("btnActivate_btn"),
        ADD("btnAdd_btn"),
        ADD_PORT("btnAdd_btn"),
        ADD_TO_PROVISION("addToProvision"),
        ADVANCED_VIEW("btnAdvView_btn"),
        CANCEL("btnCancel_btn"),
        CLEAR_TREE("UcDeviceSettingsControls1_btnClearTree_btn"),
        CREATE_TEMPLATE("btnCreateProfile_btn"),
        DEACTIVATE("btnDeactivate_btn"),
        DELETE("btnDelete_btn"),
        DELETE_GROUP("btnDeleteView_btn"),
        DUPLICATE("btnDuplicate_btn"),
        EDIT("btnEdit_btn"),
        EDIT_SETTINGS("UcDeviceSettingsControls1_btnChange_btn"),
        EDIT_TREE("UcDeviceSettingsControls1_btnEditTree_btn"),
        EXPORTS("btnCompletedExports_btn"),
        EXPORT_TO_CSV("btnExport_btn"),
        EXPORT_TO_XML("btnExport2XML_btn"),
        FACTORY_RESET("btnReset_btn"),
        FINISH("btnFinish_btn"),
        GET_CURRENT_SETTINGS("UcDeviceSettingsControls1_btnGetCurrent_btn"),
        GET_CURRENT_PORTS("btnGetCurrent_btn"),
        NEXT("btnNext_btn"),
        PAUSE("btnPause_btn"),
        REFRESH("btnRefresh_btn"),
        REPLACE("btnReplaceCpe_btn"),
        REPROVISION("btnCPEReprovision_btn"),
        PING("btnPing_btn"),
        PREVIOUS("btnPrev_btn"),
        REBOOT("btnReboot_btn"),
        SAVE("btnSave_btn"),
        SAVE_AND_ACTIVATE("btnSaveActivate_btn"),
        SAVE_PARAMETERS("UcDeviceSettingsControls1_btnSaveParameters_btn"),
        SEARCH_EXPORT_TO_CSV("btnExportToCsv_btn"),
        SEARCH_EXPORT_TO_XML("btnExportToXml_btn"),
        SEND_UPDATE("UcDeviceSettingsControls1_btnSendUpdate_btn"),
        SHOW_ON_MAP("btnMap_btn"),
        SHOW_TRACE("btnShowTrace_btn"),
        SIMPLE_VIEW("btnTabView_btn"),
        START("btnSendUpdate_btn"),
        START_TRACE("btnSetTrace_btn"),
        STOP_TRACE("btnStopTrace_btn"),
        STOP("btnStop_btn"),
        STOP_WITH_RESET("btnStopWithReset_btn"),
        STORE_TREE("UcDeviceSettingsControls1_btnSendTree_btn"),
        TRACE("btnTrace_btn"),
        TRACE_ROUTE("btnTracert_btn"),
        WAIT_UNTIL_CONNECT("rbWait");

        GlobalButtons(String id) {
            this.id = id;
        }

        private final String id;

        public String getId() {
            return id;
        }
    }
}
