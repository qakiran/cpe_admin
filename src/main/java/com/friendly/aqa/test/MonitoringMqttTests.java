package com.friendly.aqa.test;

import com.automation.remarks.testng.UniversalVideoListener;
import com.friendly.aqa.pageobject.BasePage;
import com.friendly.aqa.utils.CalendarUtil;
import com.friendly.aqa.utils.DataBaseConnector;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import static com.friendly.aqa.entities.BottomButtons.*;
import static com.friendly.aqa.pageobject.MonitoringPage.Left.IMPORT;
import static com.friendly.aqa.pageobject.MonitoringPage.Left.NEW;
import static com.friendly.aqa.entities.TopMenu.GROUP_UPDATE;
import static com.friendly.aqa.entities.TopMenu.MONITORING;

/*
Preconditions:
* Each tested manufacturer MUST have at least 2 registered models;
* Devices (emuls) MAY NOT run;
* At least 3 devices with different serials MUST BE registered for current model name;
*/

@Listeners(UniversalVideoListener.class)
public class MonitoringMqttTests extends BaseTestCase {

    @Test
    public void mqtt_mo_000() {
        monPage.createPreconditions();
    }

    @Test
    public void mqtt_mo_001() {
        monPage
                .topMenu(MONITORING)
                .assertMainPageIsDisplayed();
    }

    @Test
    public void mqtt_mo_002() {
        monPage
                .topMenu(MONITORING)
                .assertMainPageIsDisplayed()
                .bottomMenu(REFRESH)
                .assertMainPageIsDisplayed();
    }

    @Test
    public void mqtt_mo_003() {
        monPage
                .topMenu(MONITORING)
                .assertMainPageIsDisplayed()
//                .deleteAllMonitors()
//                .deleteAllCustomViews()
                .newViewButton()
                .assertButtonsAreEnabled(false, PREVIOUS, NEXT, FINISH)
                .assertButtonsAreEnabled(true, CANCEL)
                .bottomMenu(CANCEL)
                .assertMainPageIsDisplayed()
                .assertButtonsAreEnabled(false, ACTIVATE, STOP, DELETE)
                .assertButtonsAreEnabled(true, REFRESH);
    }

    @Test
    public void mqtt_mo_004() {
        monPage
                .topMenu(MONITORING)
                .newViewButton()
                .fillViewName()
                .bottomMenu(NEXT)
                .clickOnTable("tblFilter", 1, 1)
                .bottomMenu(NEXT)
                .addFilter()
                .selectColumnFilter("Created")
                .selectCompare("Is not null")
                .bottomMenu(NEXT)
                .filterRecordsCheckbox()
                .assertButtonIsEnabled(true, "btnDelFilter_btn")
                .bottomMenu(FINISH)
                .okButtonPopUp()
                .assertMainPageIsDisplayed();
    }

    @Test
    public void mqtt_mo_005() {
        monPage
                .topMenu(MONITORING)
                .newViewButton()
                .fillViewName("mqtt_mo_004")
                .bottomMenu(NEXT)
                .assertElementsArePresent("lblNameInvalid");
    }

    @Test
    public void mqtt_mo_006() {
        monPage
                .topMenu(MONITORING)
                .selectView("mqtt_mo_004")
                .editButton()
                .bottomMenu(CANCEL)
                .assertMainPageIsDisplayed();
    }

    @Test
    public void mqtt_mo_007() {
        monPage
                .topMenu(MONITORING)
                .selectView("mqtt_mo_004")
                .editButton()
                .forPublicCheckbox()
                .forUserCheckbox()
                .bottomMenu(FINISH)
                .okButtonPopUp()
                .assertMainPageIsDisplayed()
                .assertSelectedViewIs("mqtt_mo_004")
                .topMenu(GROUP_UPDATE)
                .topMenu(MONITORING)
                .assertSelectedViewIs("mqtt_mo_004");
    }

    @Test
    public void mqtt_mo_008() {
        monPage
                .topMenu(MONITORING)
                .selectView("Default")
                .assertSelectedViewIs("Default");
    }

    @Test
    public void mqtt_mo_009() {
        monPage
                .topMenu(MONITORING)
                .selectView("mqtt_mo_004")
                .assertSelectedViewIs("mqtt_mo_004");
    }

    @Test
    public void mqtt_mo_010() {
        monPage
                .topMenu(MONITORING)
                .selectView("mqtt_mo_004")
                .editButton()
                .forPublicCheckbox()
                .forUserCheckbox()
                .bottomMenu(FINISH)
                .okButtonPopUp()
                .selectView("mqtt_mo_004")
                .editButton()
                .bottomMenu(DELETE_GROUP)
                .okButtonPopUp()
                .assertSelectedViewIs("Default");
    }

    @Test
    public void mqtt_mo_011() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .assertElementsArePresent("lbActivate")
                .bottomMenu(CANCEL)
                .assertMainPageIsDisplayed();
    }

    @Test
    public void mqtt_mo_012() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .assertButtonIsEnabled(false, "btnAddModel_btn");
    }

    @Test
    public void mqtt_mo_013() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .addDeviceWithoutTemplate()
                .addModelButton()
                .assertEqualsAlertMessage("Template for this model doesn't exist")
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .selectSendTo()
                .immediately()
                .setSingleParameter()
                .assertButtonsAreEnabled(true, SAVE_AND_ACTIVATE, SAVE, CANCEL, ADVANCED_VIEW);
    }

    @Test
    public void mqtt_mo_014() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .selectTab("Management")
                .bottomMenu(ADVANCED_VIEW)
                .assertTableIsEmpty("tabsSettings_tblTabs")
                .bottomMenu(SIMPLE_VIEW)
                .assertTableHasContent("tabsSettings_tblTabs");
    }

    @Test
    public void mqtt_mo_015() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
//                .deleteAllGroups()
                .newGroupButton()
                .assertButtonsAreEnabled(false, PREVIOUS, NEXT, FINISH)
                .bottomMenu(CANCEL)
                .assertInputHasText("tbName", getTestName());
    }

    @Test
    public void mqtt_mo_016() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .newGroupButton()
                .fillGroupName()
                .bottomMenu(NEXT)
                .addFilter()
                .selectColumnFilter("Created")
                .selectCompare("Is not null")
                .bottomMenu(NEXT)
                .assertButtonIsEnabled(false, "btnDelFilter_btn")
                .filterRecordsCheckbox()
                .assertButtonIsEnabled(true, "btnDelFilter_btn")
                .bottomMenu(FINISH)
                .okButtonPopUp()
                .validateSelectedGroup();
    }

    @Test
    public void mqtt_mo_017() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .immediately()
                .newGroupButton()
                .fillGroupName()
                .bottomMenu(NEXT)
                .addFilter()
                .selectColumnFilter("Created")
                .selectCompare("Is null")
                .bottomMenu(NEXT)
                .bottomMenu(FINISH)
                .okButtonPopUp()
                .assertButtonsAreEnabled(false, SAVE_AND_ACTIVATE, SAVE);
    }

    @Test
    public void mqtt_mo_018() {    //is dependent on #016
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .immediately()
                .newGroupButton()
                .fillGroupName("mqtt_mo_016")
                .bottomMenu(NEXT)
                .assertElementsArePresent("lblNameInvalid");
    }

    @Test
    public void mqtt_mo_019() {    //is dependent on #016
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .immediately()
                .selectSendTo("mqtt_mo_016")
                .validateDevicesAmount();
    }

    @Test
    public void mqtt_mo_020() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .immediately()
                .selectSendTo("All")
                .validateDevicesAmount();
    }

    @Test
    public void mqtt_mo_021() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .immediately()
                .selectSendTo("Individual")
                .assertButtonIsEnabled(true, "btnSelectDevices_btn")
                .selectButton()
                .cancelIndividualSelection()
                .selectButton()
                .selectIndividualDevises(1)
                .validateDevicesAmountIs(1);
    }

    @Test
    public void mqtt_mo_022() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .selectSendTo("Import from a file")
                .selectImportDevicesFile()
                .pause(2000)
                .validateDevicesAmountIs(1);
    }

    @Test
    public void mqtt_mo_023() {//is dependent on #016
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .selectSendTo("mqtt_mo_016")
                .editButton()
                .bottomMenu(DELETE_GROUP)
                .okButtonPopUp()
                .assertAbsenceOfOptions("ddlSend", "mqtt_mo_016");
    }

    @Test
    public void mqtt_mo_024() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .immediately()
                .assertButtonsAreEnabled(false, SAVE, SAVE_AND_ACTIVATE);
    }

    @Test
    public void mqtt_mo_025() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .immediately()
                .selectSendTo("All")
                .setParameters("Management", 0, 1)
                .bottomMenu(SAVE)
                .okButtonPopUp()
                .waitForStatus("Not active")
                .enterIntoGroup()
                .validateAddedTasks();
    }

    @Test
    public void mqtt_mo_026() {    //is dependent on #025
        monPage
                .topMenu(MONITORING)
                .enterIntoMonitoring("mqtt_mo_025")
                .immediately()
                .setParameters("Management", 2, 2)
                .bottomMenu(SAVE)
                .okButtonPopUp()
                .waitForStatus("Not active", "mqtt_mo_025")
                .enterIntoMonitoring("mqtt_mo_025")
                .validateAddedTasks();
    }

    @Test
    public void mqtt_mo_027() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .immediately()
                .selectSendTo("All")
                .setParameters("Information", 0, 7)
                .bottomMenu(SAVE_AND_ACTIVATE)
                .okButtonPopUp()
                .waitForStatus("Running")
                .enterIntoGroup()
                .validateAddedTasks();
    }

    @Test
    public void mqtt_mo_028() {    //is dependent on #027
        monPage
                .topMenu(MONITORING)
                .enterIntoMonitoring("mqtt_mo_027")
                .setParameters("Information", 8, 8)
                .bottomMenu(SAVE)
                .okButtonPopUp()
                .waitForStatus("Running", "mqtt_mo_027")
                .enterIntoMonitoring("mqtt_mo_027")
                .validateAddedTasks();
    }

    @Test
    public void mqtt_mo_029() {    //is dependent on #027 and #028
        mqtt_mo_028();
    }

    @Test
    public void mqtt_mo_030() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .immediately()
                .selectSendTo("All")
                .setParameters("Management", 0, 0)
                .bottomMenu(SAVE_AND_ACTIVATE)
                .okButtonPopUp()
                .waitForStatus("Running")
                .enterIntoGroup()
                .validateAddedTasks();
    }

    @Test
    public void mqtt_mo_031() {    //is dependent on #030
        monPage
                .topMenu(MONITORING)
                .enterIntoMonitoring("mqtt_mo_030")
                .setParameters("Management", 1, 3)
                .bottomMenu(SAVE)
                .okButtonPopUp()
                .waitForStatus("Running", "mqtt_mo_030")
                .enterIntoMonitoring("mqtt_mo_030")
                .validateAddedTasks();
    }

    @Test
    public void mqtt_mo_032() {    //is dependent on #030
        monPage
                .topMenu(MONITORING)
                .enterIntoMonitoring("mqtt_mo_030")
                .setParameters("Management", 4, 100)
                .bottomMenu(SAVE)
                .okButtonPopUp()
                .waitForStatus("Running", "mqtt_mo_030")
                .enterIntoMonitoring("mqtt_mo_030")
                .validateAddedTasks();
    }

    @Test
    public void mqtt_mo_033() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .immediately()
                .selectSendTo("All")
                .setParameters("Information", 0, 0)
                .bottomMenu(SAVE_AND_ACTIVATE)
                .okButtonPopUp()
                .waitForStatus("Running")
                .enterIntoGroup()
                .validateAddedTasks();
    }

    @Test
    public void mqtt_mo_034() {    //is dependent on #033
        monPage
                .topMenu(MONITORING)
                .enterIntoMonitoring("mqtt_mo_033")
                .setParameters("Information", 1, 3)
                .bottomMenu(SAVE)
                .okButtonPopUp()
                .waitForStatus("Running", "mqtt_mo_033")
                .enterIntoMonitoring("mqtt_mo_033")
                .validateAddedTasks();
    }

    @Test
    public void mqtt_mo_035() {    //is dependent on #033
        monPage
                .topMenu(MONITORING)
                .enterIntoMonitoring("mqtt_mo_033")
                .setParameters("Information", 4, 100)
                .bottomMenu(SAVE)
                .okButtonPopUp()
                .waitForStatus("Running", "mqtt_mo_033")
                .enterIntoMonitoring("mqtt_mo_033")
                .validateAddedTasks();
    }

    @Test
    public void mqtt_mo_036() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .immediately()
                .selectSendTo("All")
                .setParameters("Management", 0, 1)
                .bottomMenu(SAVE_AND_ACTIVATE)
                .okButtonPopUp()
                .waitForStatus("Running")
                .enterIntoGroup()
                .validateAddedTasks();
    }

    @Test
    public void mqtt_mo_037() {    //is dependent on #036
        monPage
                .topMenu(MONITORING)
                .enterIntoMonitoring("mqtt_mo_036")
                .setParameters("Management", 0, 0)
                .bottomMenu(SAVE)
                .okButtonPopUp()
                .waitForStatus("Running", "mqtt_mo_036")
                .enterIntoMonitoring("mqtt_mo_036")
                .validateAddedTasks();
    }

    @Test
    public void mqtt_mo_038() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .immediately()
                .selectSendTo("All")
                .setParameters("Management", 0, 0)
                .bottomMenu(SAVE)
                .okButtonPopUp()
                .waitForStatus("Not active", 5)
                .pause(1000)
                .selectItem()
                .bottomMenu(ACTIVATE)
                .okButtonPopUp()
                .waitForStatus("Running")
                .enterIntoGroup()
                .validateAddedTasks();
    }

    @Test
    public void mqtt_mo_039() {    //is dependent on #056
        monPage
                .topMenu(MONITORING)
                .selectItem("mqtt_mo_038")
                .bottomMenu(STOP)
                .okButtonPopUp()
                .waitForStatus("Not active", "mqtt_mo_038");
    }

    @Test
    public void mqtt_mo_040() {    //is dependent on #056
        monPage
                .topMenu(MONITORING)
                .selectItem("mqtt_mo_038")
                .bottomMenu(ACTIVATE)
                .okButtonPopUp()
                .waitForStatus("Running", "mqtt_mo_038");
    }

//    @Test
//    public void mqtt_mo_041() {    //is dependent on #056
//        monPage
//                .topMenu(MONITORING)
//                .selectItem("mqtt_mo_038")
//                .bottomMenu(STOP_WITH_RESET)  //this button is no longer displayed (BT item #9766)
//                .okButtonPopUp()
//                .waitForStatus("Not active", "mqtt_mo_038");
//    }

    @Test
    public void mqtt_mo_042() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .immediately()
                .selectSendTo("All")
                .clickOn("calDateTo")
                .selectShiftedDate("calDateTo", 0)
                .setEndDateDelay(-10)
                .setSingleParameter()
                .bottomMenu(SAVE)
                .okButtonPopUp()
                .assertEqualsAlertMessage("Ending date can't be scheduled to the past")
                .selectShiftedDate("calDateTo", 0)
                .bottomMenu(SAVE)
                .okButtonPopUp()
                .assertEqualsAlertMessage("Ending date can't be scheduled to the past");
    }

    @Test
    public void mqtt_mo_043() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .immediately()
                .selectSendTo("All")
                .setParameters("Management", 0, 100)
                .setParameters("Information", 0, 0)
//                .setParameters("Connectivity monitoring", 0, 0)
                .selectShiftedDate("calDateTo", 0)
                .setEndDateDelay(2)
                .bottomMenu(SAVE_AND_ACTIVATE)
                .okButtonPopUp()
                .waitForStatus("Running")
                .waitForStatus("Completed", 150)
                .enterIntoGroup()
                .validateAddedTasks();
    }

    @Test
    public void mqtt_mo_044() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .immediately()
                .selectSendTo("All")
                .setAdvancedParameters("Device.ManagementServer", 0, 100)
                .bottomMenu(SAVE)
                .okButtonPopUp()
                .waitForStatus("Not active")
                .enterIntoGroup()
                .validateAddedTasks();
    }

    @Test
    public void mqtt_mo_045() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .scheduledToRadioButton()
                .selectShiftedDate("calDateFrom", 0)
                .setScheduledDelay(10)
                .selectSendTo("All")
                .setParameters("Management", 0, 0)
                .bottomMenu(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled")
                .enterIntoGroup()
                .validateAddedTasks();
    }

    @Test
    public void mqtt_mo_046() {    //is dependent on #045
        monPage
                .topMenu(MONITORING)
                .enterIntoMonitoring("mqtt_mo_045")
                .scheduledToRadioButton()
                .setParameters("Management", 1, 3)
                .bottomMenu(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", "mqtt_mo_045")
                .enterIntoMonitoring("mqtt_mo_045")
                .validateAddedTasks();
    }

    @Test
    public void mqtt_mo_047() {    //is dependent on #045
        monPage
                .topMenu(MONITORING)
                .enterIntoMonitoring("mqtt_mo_045")
                .scheduledToRadioButton()
                .setParameters("Management", 4, 100)
                .bottomMenu(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", "mqtt_mo_045")
                .enterIntoMonitoring("mqtt_mo_045")
                .validateAddedTasks();
    }

    @Test
    public void mqtt_mo_048() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .scheduledToRadioButton()
                .selectShiftedDate("calDateFrom", 0)
                .setScheduledDelay(10)
                .selectSendTo("All")
                .setParameters("Information", 0, 0)
                .bottomMenu(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled")
                .enterIntoGroup()
                .validateAddedTasks();
    }

    @Test
    public void mqtt_mo_049() {    //is dependent on #066
        monPage
                .topMenu(MONITORING)
                .enterIntoMonitoring("mqtt_mo_048")
                .scheduledToRadioButton()
                .setParameters("Information", 1, 3)
                .bottomMenu(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", "mqtt_mo_048")
                .enterIntoMonitoring("mqtt_mo_048")
                .validateAddedTasks();
    }

    @Test
    public void mqtt_mo_050() {    //is dependent on #066
        monPage
                .topMenu(MONITORING)
                .enterIntoMonitoring("mqtt_mo_048")
                .scheduledToRadioButton()
                .setParameters("Information", 4, 100)
                .bottomMenu(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", "mqtt_mo_048")
                .enterIntoMonitoring("mqtt_mo_048")
                .validateAddedTasks();
    }

    @Test
    public void mqtt_mo_051() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .scheduledToRadioButton()
                .selectShiftedDate("calDateFrom", 0)
                .setScheduledDelay(10)
                .selectSendTo("All")
                .setParameters("Management", 0, 1)
                .bottomMenu(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled")
                .selectItem()
                .bottomMenu(STOP)
                .okButtonPopUp()
                .bottomMenu(REFRESH)
                .waitForStatus("Not active", 5)
                .enterIntoGroup()
                .validateAddedTasks();
    }

    @Test
    public void mqtt_mo_052() {
        monPage
                .topMenu(MONITORING)
                .selectItem("mqtt_mo_051")
                .bottomMenu(ACTIVATE)
                .okButtonPopUp()
                .bottomMenu(REFRESH)
                .waitForStatus("Scheduled", "mqtt_mo_051");
    }

    @Test
    public void mqtt_mo_053() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .selectSendTo("All")
                .scheduledToRadioButton()
                .setSingleParameter()
                .bottomMenu(SAVE)
                .okButtonPopUp()
                .assertEqualsAlertMessage("Activation date can't be scheduled to the past");
    }

    @Test
    public void mqtt_mo_054() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(IMPORT)
                .selectImportMonitorFile()
                .assertPresenceOfValue("tblModels", 0, BasePage.deviceToString());
    }

    @Test
    public void mqtt_mo_055() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(IMPORT)
                .bottomMenu(CANCEL)
                .assertMainPageIsDisplayed();
    }

    @Test
    public void mqtt_mo_056() {
        monPage
                .topMenu(MONITORING)
                .validateFilteringByManufacturer();
    }

    @Test
    public void mqtt_mo_057() {
        monPage
                .topMenu(MONITORING)
                .validateFilteringByModelName();
    }

    @Test
    public void mqtt_mo_058() {
        monPage
                .topMenu(MONITORING)
//                .deleteAllCustomViews()
                .newViewButton()
                .fillCustomViewName()
                .bottomMenu(NEXT)
                .setVisibleColumns(1, 99)
                .bottomMenu(NEXT)
                .bottomMenu(FINISH)
                .okButtonPopUp()
                .validateViewColumns();
    }

    @Test
    public void mqtt_mo_059() {
        monPage
                .topMenu(MONITORING)
                .selectView("mqtt_mo_058")
                .validateSorting("Created");
    }

    @Test
    public void mqtt_mo_060() {
        monPage
                .topMenu(MONITORING)
                .selectView("mqtt_mo_058")
                .validateSorting("Date from");
    }

    @Test
    public void mqtt_mo_061() {
        monPage
                .topMenu(MONITORING)
                .selectView("mqtt_mo_058")
                .validateSorting("Date to");
    }

    @Test
    public void mqtt_mo_062() {
        monPage
                .topMenu(MONITORING)
                .selectView("mqtt_mo_058")
                .validateSorting("Description");
    }

    @Test
    public void mqtt_mo_063() {
        monPage
                .topMenu(MONITORING)
                .selectView("mqtt_mo_058")
                .validateSorting("Name");
    }

    @Test
    public void mqtt_mo_064() {    //Bug: Unclear sorting algorithm by "State" column
        monPage
                .topMenu(MONITORING)
                .selectView("mqtt_mo_058")
                .validateSorting("State");
    }

    @Test
    public void mqtt_mo_065() {
        monPage
                .topMenu(MONITORING)
                .selectView("mqtt_mo_058")
                .validateSorting("Updated");
    }

    @Test
    public void mqtt_mo_066() {
        monPage
                .topMenu(MONITORING)
                .selectView("mqtt_mo_058")
                .resetView()
                .assertSelectedViewIs( "Default");
    }

    @Test
    public void mqtt_mo_067() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .selectSendTo()
                .addAnotherModel()
                .setParametersFor2Devices(true);
    }

    @Test
    public void mqtt_mo_068() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .selectSendTo()
                .addAnotherModel()
                .setParametersFor2Devices(false);
    }

    @Test
    public void mqtt_mo_069() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
//                .deleteAllGroups()
                .newGroupButton()
                .assertButtonsAreEnabled(false, PREVIOUS, NEXT, FINISH)
                .bottomMenu(CANCEL)
                .assertInputHasText("tbName", getTestName());
    }

    @Test
    public void mqtt_mo_070() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .newGroupButton()
                .fillGroupName()
                .bottomMenu(NEXT)
                .addFilter()
                .selectColumnFilter("Created")
                .selectCompare("Is null")
                .bottomMenu(NEXT)
                .assertButtonIsEnabled(false, "btnDelFilter_btn")
                .filterRecordsCheckbox()
                .assertButtonIsEnabled(true, "btnDelFilter_btn")
                .bottomMenu(FINISH)
                .okButtonPopUp()
                .validateSelectedGroup();
    }

    @Test
    public void mqtt_mo_071() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .immediately()
                .newGroupButton()
                .fillGroupName()
                .bottomMenu(NEXT)
                .addFilter()
                .selectColumnFilter("Created")
                .selectCompare("Is null")
                .bottomMenu(NEXT)
                .bottomMenu(FINISH)
                .okButtonPopUp()
                .assertButtonsAreEnabled(false, SAVE_AND_ACTIVATE, SAVE);
    }

    @Test
    public void mqtt_mo_072() {    //is dependent on #070
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .immediately()
                .newGroupButton()
                .fillGroupName("mqtt_mo_070")
                .bottomMenu(NEXT)
                .assertElementsArePresent("lblNameInvalid");
    }

    @Test
    public void mqtt_mo_073() {//is dependent on #106
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .selectSendTo("mqtt_mo_070")
                .editButton()
                .bottomMenu(DELETE_GROUP)
                .okButtonPopUp()
                .assertAbsenceOfOptions("ddlSend", "mqtt_mo_070");
    }

    @Test
    public void mqtt_mo_074() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .newGroupButton()
                .fillGroupName()
                .bottomMenu(NEXT)
                .addFilter()
                .selectColumnFilter("Created")
                .selectCompare("Is not null")
                .bottomMenu(NEXT)
                .assertFalse(monPage.isButtonActive("btnDelFilter_btn"))
                .filterRecordsCheckbox()
                .assertTrue(monPage.isButtonActive("btnDelFilter_btn"))
                .bottomMenu(FINISH)
                .okButtonPopUp()
                .assertEquals(monPage.getSelectedOption("ddlSend"), getTestName())
                .immediately()
                .setSingleParameter()
                .bottomMenu(SAVE_AND_ACTIVATE)
                .okButtonPopUp()
                .waitForStatus("Running", 5);
    }

    @Test
    public void mqtt_mo_075() {
        DataBaseConnector.createFilterPreconditions(BasePage.getSerial());
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .newGroupButton()
                .fillGroupName()
                .bottomMenu(NEXT)
                .addFilter()
                .selectColumnFilter("Created")
                .selectCompare("On Day")
                .bottomMenu(NEXT)
                .bottomMenu(FINISH)
                .okButtonPopUp()
                .immediately()
                .setSingleParameter()
                .bottomMenu(SAVE_AND_ACTIVATE)
                .okButtonPopUp()
                .waitForStatus("Running");
    }

    @Test
    public void mqtt_mo_076() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .newGroupButton()
                .fillGroupName()
                .bottomMenu(NEXT)
                .addFilter()
                .selectColumnFilter("Created")
                .selectCompare("Prior to")
                .bottomMenu(NEXT)
                .bottomMenu(FINISH)
                .okButtonPopUp()
                .immediately()
                .setSingleParameter()
                .bottomMenu(SAVE_AND_ACTIVATE)
                .okButtonPopUp()
                .waitForStatus("Running");
    }

    @Test
    public void mqtt_mo_077() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .newGroupButton()
                .fillGroupName()
                .bottomMenu(NEXT)
                .addFilter()
                .selectColumnFilter("Created")
                .selectCompare("Later than")
                .clickOn("calFilterDate_image")
                .selectDate(CalendarUtil.getMonthBeforeDate())
                .bottomMenu(NEXT)
                .bottomMenu(FINISH)
                .okButtonPopUp()
                .immediately()
                .setSingleParameter()
                .bottomMenu(SAVE_AND_ACTIVATE)
                .okButtonPopUp()
                .waitForStatus("Running");
    }

    @Test
    public void mqtt_mo_078() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .newGroupButton()
                .fillGroupName()
                .bottomMenu(NEXT)
                .addFilter()
                .selectColumnFilter("Created")
                .selectCompare("Today")
                .bottomMenu(NEXT)
                .bottomMenu(FINISH)
                .okButtonPopUp()
                .immediately()
                .setSingleParameter()
                .bottomMenu(SAVE_AND_ACTIVATE)
                .okButtonPopUp()
                .waitForStatus("Running");
    }

    @Test
    public void mqtt_mo_079() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .newGroupButton()
                .fillGroupName()
                .bottomMenu(NEXT)
                .addFilter()
                .selectColumnFilter("Created")
                .selectCompare("Before Today")
                .bottomMenu(NEXT)
                .bottomMenu(FINISH)
                .okButtonPopUp()
                .immediately()
                .setSingleParameter()
                .bottomMenu(SAVE_AND_ACTIVATE)
                .okButtonPopUp()
                .waitForStatus("Running");
    }

    @Test
    public void mqtt_mo_080() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .newGroupButton()
                .fillGroupName()
                .bottomMenu(NEXT)
                .addFilter()
                .selectColumnFilter("Created")
                .selectCompare("Yesterday")
                .bottomMenu(NEXT)
                .bottomMenu(FINISH)
                .okButtonPopUp()
                .immediately()
                .setSingleParameter()
                .bottomMenu(SAVE_AND_ACTIVATE)
                .okButtonPopUp()
                .waitForStatus("Running");
    }

    @Test
    public void mqtt_mo_081() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .newGroupButton()
                .fillGroupName()
                .bottomMenu(NEXT)
                .addFilter()
                .selectColumnFilter("Created")
                .selectCompare("Prev 7 days")
                .bottomMenu(NEXT)
                .bottomMenu(FINISH)
                .okButtonPopUp()
                .immediately()
                .setSingleParameter()
                .bottomMenu(SAVE_AND_ACTIVATE)
                .okButtonPopUp()
                .waitForStatus("Running");
    }

    @Test
    public void mqtt_mo_082() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .newGroupButton()
                .fillGroupName()
                .bottomMenu(NEXT)
                .addFilter()
                .selectColumnFilter("Created")
                .selectCompare("Prev X days")
                .inputText("txtInt", "4")
                .bottomMenu(NEXT)
                .bottomMenu(FINISH)
                .okButtonPopUp()
                .immediately()
                .setSingleParameter()
                .bottomMenu(SAVE_AND_ACTIVATE)
                .okButtonPopUp()
                .waitForStatus("Running");
    }

    @Test
    public void mqtt_mo_083() {
        monPage
                .presetFilter("mycust03", getTestName())
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .newGroupButton()
                .fillGroupName()
                .bottomMenu(NEXT)
                .addFilter()
                .selectColumnFilter("mycust03")
                .selectCompare("=")
                .inputText("txtText", getTestName())
                .bottomMenu(NEXT)
                .bottomMenu(FINISH)
                .okButtonPopUp()
                .immediately()
                .setSingleParameter()
                .bottomMenu(SAVE_AND_ACTIVATE)
                .okButtonPopUp()
                .waitForStatus("Running");
    }

    @Test
    public void mqtt_mo_084() {
        monPage
                .presetFilter("mycust03", getTestName())
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .newGroupButton()
                .fillGroupName()
                .bottomMenu(NEXT)
                .addFilter()
                .selectColumnFilter("mycust03")
                .selectCompare("!=")
                .inputText("txtText", getTestName())
                .bottomMenu(NEXT)
                .bottomMenu(FINISH)
                .okButtonPopUp()
                .immediately()
                .setSingleParameter()
                .bottomMenu(SAVE_AND_ACTIVATE)
                .okButtonPopUp()
                .waitForStatus("Running");
    }

    @Test   // bug: failed due to device does not have MAC address
    public void mqtt_mo_085() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .newGroupButton()
                .fillGroupName()
                .bottomMenu(NEXT)
                .addFilter()
                .selectColumnFilter("MAC address")
                .selectCompare("Starts with")
                .partOfMacAddress()
                .bottomMenu(NEXT)
                .bottomMenu(FINISH)
                .okButtonPopUp()
                .assertButtonsAreEnabled(false, SAVE_AND_ACTIVATE, SAVE);
    }

    @Test
    public void mqtt_mo_086() {
        monPage
                .presetFilter("mycust03", getTestName())
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .newGroupButton()
                .fillGroupName()
                .bottomMenu(NEXT)
                .addFilter()
                .selectColumnFilter("mycust03")
                .selectCompare("Like")
                .inputText("txtText", getTestName().substring(1, 5))
                .bottomMenu(NEXT)
                .bottomMenu(FINISH)
                .okButtonPopUp()
                .immediately()
                .setSingleParameter()
                .bottomMenu(SAVE_AND_ACTIVATE)
                .okButtonPopUp()
                .waitForStatus("Running");
    }

    @Test
    public void mqtt_mo_087() {
        monPage
                .presetFilter("mycust03", getTestName())
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .newGroupButton()
                .fillGroupName()
                .bottomMenu(NEXT)
                .addFilter()
                .selectColumnFilter("mycust03")
                .selectCompare("No like")
                .inputText("txtText", getTestName().substring(1, 5))
                .bottomMenu(NEXT)
                .bottomMenu(FINISH)
                .okButtonPopUp()
                .immediately()
                .setSingleParameter()
                .bottomMenu(SAVE_AND_ACTIVATE)
                .okButtonPopUp()
                .waitForStatus("Running");
    }

    @Test
    public void mqtt_mo_088() {
        monPage
                .presetFilter("mycust03", "")
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .newGroupButton()
                .fillGroupName()
                .bottomMenu(NEXT)
                .addFilter()
                .selectColumnFilter("mycust03")
                .selectCompare("Is null")
                .bottomMenu(NEXT)
                .bottomMenu(FINISH)
                .okButtonPopUp()
                .immediately()
                .setSingleParameter()
                .bottomMenu(SAVE_AND_ACTIVATE)
                .okButtonPopUp()
                .waitForStatus("Running");
    }

    @Test
    public void mqtt_mo_089() {
        monPage
                .presetFilter("mycust03", getTestName())
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .newGroupButton()
                .fillGroupName()
                .bottomMenu(NEXT)
                .addFilter()
                .selectColumnFilter("mycust03")
                .selectCompare("Is not null")
                .bottomMenu(NEXT)
                .bottomMenu(FINISH)
                .okButtonPopUp()
                .immediately()
                .setSingleParameter()
                .bottomMenu(SAVE_AND_ACTIVATE)
                .okButtonPopUp()
                .waitForStatus("Running");
    }

    @Test
    public void mqtt_mo_090() {
        monPage
                .presetFilter("mycust03", getTestName())
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .newGroupButton()
                .fillGroupName()
                .bottomMenu(NEXT)
                .addFilter()
                .selectColumnFilter("mycust03")
                .selectCompare("Is not null")
                .bottomMenu(NEXT)
                .bottomMenu(FINISH)
                .okButtonPopUp()
                .selectManufacturer()
                .setSingleParameter()
                .addAnotherModel()
                .newGroupButton()
                .fillGroupName(getTestName() + "_1")
                .bottomMenu(NEXT)
                .addFilter()
                .selectColumnFilter("mycust03")
                .selectCompare("Is null")
                .bottomMenu(NEXT)
                .bottomMenu(FINISH)
                .okButtonPopUp()
                .immediately()
                .setSingleParameter()
                .bottomMenu(SAVE_AND_ACTIVATE)
                .okButtonPopUp()
                .waitForStatus("Running");
    }

    @Test
    public void mqtt_mo_091() {
        monPage
                .topMenu(MONITORING)
                .leftMenu(NEW)
                .fillName()
                .selectManufacturer()
                .selectModel()
                .addModelButton()
                .newGroupButton()
                .fillGroupName()
                .bottomMenu(NEXT)
                .addFilter()
                .selectColumnFilter("mycust04")
                .selectCompare("Is null")
                .bottomMenu(NEXT)
                .bottomMenu(FINISH)
                .okButtonPopUp()
                .selectManufacturer()
                .setSingleParameter()
                .addAnotherModel()
                .newGroupButton()
                .fillGroupName(getTestName() + "_1")
                .bottomMenu(NEXT)
                .addFilter()
                .selectColumnFilter("mycust04")
                .selectCompare("Is null")
                .bottomMenu(NEXT)
                .bottomMenu(FINISH)
                .okButtonPopUp()
                .immediately()
                .setSingleParameter()
                .bottomMenu(SAVE_AND_ACTIVATE)
                .okButtonPopUp()
                .waitForStatus("Running");
    }
}
