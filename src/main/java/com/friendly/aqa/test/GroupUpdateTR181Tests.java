package com.friendly.aqa.test;

import com.automation.remarks.testng.UniversalVideoListener;
import com.friendly.aqa.pageobject.BasePage;
import com.friendly.aqa.utils.CalendarUtil;
import com.friendly.aqa.utils.DataBaseConnector;
import com.friendly.aqa.utils.HttpConnector;
import com.friendly.aqa.utils.XmlWriter;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;

import static com.friendly.aqa.pageobject.BasePage.getManufacturer;
import static com.friendly.aqa.pageobject.BasePage.getModelName;
import static com.friendly.aqa.pageobject.GlobalButtons.*;
import static com.friendly.aqa.pageobject.GroupUpdatePage.Left.*;
import static com.friendly.aqa.pageobject.GroupUpdatePage.Parameter.VALUE;
import static com.friendly.aqa.pageobject.TopMenu.GROUP_UPDATE;
import static com.friendly.aqa.pageobject.GroupUpdatePage.Conditions.EQUAL;

@Listeners(UniversalVideoListener.class)
public class GroupUpdateTR181Tests extends BaseTestCase {

    @Test
    public void tr181_gu_001() {
        guPage
                .deleteAll()
                .topMenu(GROUP_UPDATE)
                .waitForUpdate()
                .assertMainPageIsDisplayed();
    }

    @Test
    public void tr181_gu_002() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .globalButtons(CANCEL)
                .assertMainPageIsDisplayed();
    }

    @Test
    public void tr181_gu_003() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .deleteFilterGroups()
                .globalButtons(CANCEL)
                .waitForUpdate()
                .assertMainPageIsDisplayed();
    }

    @Test
    public void tr181_gu_004() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .showList()
                .assertTrue(guPage.serialNumberTableIsPresent());
    }

    @Test
    public void tr181_gu_005() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .createGroup()
                .assertTrue(guPage.isButtonPresent(FINISH))
                .globalButtons(CANCEL)
                .waitForUpdate()
                .pause(500)
                .assertEquals(guPage.getAttributeById("txtName", "value"), testName);
    }

    @Test
    public void tr181_gu_006() {
        guPage
                .gotoAddFilter()
                .selectColumnFilter("device_created")
                .compareSelect("IsNull")
                .globalButtons(NEXT)
                .assertFalse(guPage.isButtonActive("btnDelFilter_btn"))
                .filterRecordsCheckbox()
                .assertTrue(guPage.isButtonActive("btnDelFilter_btn"))
                .globalButtons(FINISH)
                .okButtonPopUp()
                .assertEquals(testName, guPage.getSelectedValue("ddlSend"));
        setTargetTestName();
    }

    @Test
    public void tr181_gu_007() {
        guPage
                .gotoAddFilter()
                .selectColumnFilter("cust2")
                .compareSelect("Equal")
                .inputTextField("111")
                .globalButtons(NEXT)
                .globalButtons(FINISH)
                .okButtonPopUp()
                .assertEquals(testName, guPage.getSelectedValue("ddlSend"), "Created group isn't selected!\n")
                .assertTrue(guPage.isElementDisplayed("lblNoSelectedCpes"), "Warning 'No devices selected' isn't displayed!\n");
    }

    @Test
    public void tr181_gu_008() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .createGroup()
                .fillName(targetTestName)
                .globalButtons(NEXT)
                .assertTrue(guPage.isElementDisplayed("lblNameInvalid"), "Warning 'This name is already in use' isn't displayed!\n");
    }

    @Test
    public void tr181_gu_009() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo(targetTestName)
                .editGroupButton()
                .globalButtons(DELETE_GROUP)
                .okButtonPopUp()
                .assertFalse(guPage.isOptionPresent("ddlSend", targetTestName), "Option '" + targetTestName + "' is still present on 'Send to' list!\n");
    }

    @Test
    public void tr181_gu_010() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo("Individual")
                .clickOnTable("tblDevices", 1, 0)
                .waitForUpdate()
                .assertButtonsAreEnabled(true, NEXT)
                .clickOnTable("tblDevices", 1, 0)
                .waitForUpdate()
                .assertButtonsAreEnabled(false, NEXT);
    }

    @Test
    //Doesn't work with Edge
    public void tr181_gu_011() {
        XmlWriter.createImportCpeFile();
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo("Import")
                .selectImportDevicesFile()
                .showList()
                .assertPresenceOfValue("tblDevices", 0, getSerial());
    }

    @Test
    public void tr181_gu_012() {
        guPage
                .gotoAddFilter()
                .selectColumnFilter("device_created")
                .compareSelect("Is not null")
                .globalButtons(NEXT)
                .globalButtons(FINISH)
                .okButtonPopUp()
                .selectSendTo(testName)
                .showList()
                .assertPresenceOfValue("tblDevices", 0, getSerial());
    }

    @Test
    public void tr181_gu_013() {
        guPage
                .gotoSetParameters()
                .assertElementIsPresent("tblParamsValue")
                .assertButtonsAreEnabled(false, SAVE_AND_ACTIVATE);
    }

    @Test
    public void tr181_gu_014() {
        guPage
                .gotoSetParameters()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Not active", 5);
        setTargetTestName();
    }

    @Test
    public void tr181_gu_015() {
        guPage
                .topMenu(GROUP_UPDATE)
                .enterIntoGroup(targetTestName)
                .globalButtons(EDIT)
                .globalButtons(NEXT)
                .immediately()
                .globalButtons(NEXT)
                .clickOnTable("tblTasks", "Device.ManagementServer.PeriodicInformInterval")
                .setParameter("PeriodicInformInterval, sec", VALUE, "61")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .enterIntoGroup(targetTestName)
                .checkResults();
    }

    @Test
    public void tr181_gu_016() {
        guPage
                .gotoSetParameters()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .nextSaveAndActivate();
        setTargetTestName();
    }

    @Test
    public void tr181_gu_017() {
        guPage
                .topMenu(GROUP_UPDATE)
                .enterIntoGroup(targetTestName)
                .globalButtons(EDIT)
                .assertFalse(guPage.isInputActive("ddlSend"))
                .globalButtons(NEXT)
                .assertFalse(guPage.isInputActive("lrbImmediately"))
                .globalButtons(NEXT)
                .assertButtonsAreEnabled(false, SAVE_AND_ACTIVATE);
    }

    @Test
    public void tr181_gu_018() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .timeHoursSelect("0")
                .globalButtons(NEXT)
                .assertEqualsAlertMessage("Update can't be scheduled to the past")
                .checkIsCalendarClickable();
    }

    @Test
    public void tr181_gu_019() throws IOException {
        guPage
                .topMenu(GROUP_UPDATE)
                .assertTrue(HttpConnector.sendGetRequest(guPage
                        .getGuExportLink("tr181_gu_016"))
                        .contains("\"Device.ManagementServer.PeriodicInformInterval\" value=\"60\""));
    }

    @Test
    public void tr181_gu_020() {
        guPage
                .gotoSetParameters()
                .setParameter("Username", VALUE, "ftacs")
                .setParameter("Password", VALUE, "ftacs")
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_021() {
        guPage
                .gotoSetParameters()
                .setParameter("Username", VALUE, "ftacs")
                .nextSaveAndActivate()
                .checkResults("Device.ManagementServer.Username", "ftacs");
    }

    @Test
    public void tr181_gu_022() {
        guPage
                .gotoSetParameters("Information")
                .setParameter(1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_023() {
        guPage
                .gotoSetParameters("Time")
                .setAllParameters()
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_024() {
        guPage
                .gotoSetParameters("Time")
                .setParameter(1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_025() {
        guPage
                .gotoSetParameters("Time")
                .setParameter(2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_026() {
        guPage
                .gotoSetParameters("WiFi")
                .setAllParameters()
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_027() {
        guPage
                .gotoSetParameters("WiFi")
                .setParameter(1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_028() {
        guPage
                .gotoSetParameters("WiFi")
                .setParameter(2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_029() {
        guPage
                .gotoSetParameters("IP")
                .setAllParameters()
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_030() {
        guPage
                .gotoSetParameters("IP")
                .setParameter(1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_031() {
        guPage
                .gotoSetParameters("IP")
                .setParameter(2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_032() {
        guPage
                .gotoSetParameters("Firewall")
                .setAllParameters()
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_033() {
        guPage
                .gotoSetParameters("Firewall")
                .setParameter(1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_034() {
        guPage
                .gotoSetParameters("Firewall")
                .setParameter(2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_035() {
        guPage
                .gotoSetParameters("DHCPv4")
                .setAllParameters()
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_036() {
        guPage
                .gotoSetParameters("DHCPv4")
                .setParameter(1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_037() {
        guPage
                .gotoSetParameters("DHCPv4")
                .setParameter(2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_038() {
        guPage
                .gotoSetParameters("DHCPv6")
                .setAllParameters()
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_039() {
        guPage
                .gotoSetParameters("DHCPv6")
                .setParameter(1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_040() {
        guPage
                .gotoSetParameters("DHCPv6")
                .setParameter(2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_041() {
        guPage
                .gotoSetParameters("DNS")
                .setAllParameters()
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_042() {
        guPage
                .gotoSetParameters("DNS")
                .setParameter(1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_043() {
        guPage
                .gotoSetParameters("DNS")
                .setParameter(2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_044() {
        guPage
                .gotoSetParameters("Users")
                .setAllParameters()
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_045() {
        guPage
                .gotoSetParameters("Users")
                .setParameter(1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_046() {
        guPage
                .gotoSetParameters("Users")
                .setParameter(2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_047() {
        guPage
                .gotoSetParameters("Ethernet")
                .setAllParameters()
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_048() {
        guPage
                .gotoSetParameters("Ethernet")
                .setParameter(1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_049() {
        guPage
                .gotoSetParameters("Ethernet")
                .setParameter(2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_050() {
        guPage
                .goToSetPolicies("Management")
                .setAllPolicies()
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_051() {
        guPage
                .goToSetPolicies("Management")
                .setPolicy(1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_052() {
        guPage
                .goToSetPolicies("Management")
                .setPolicy(2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_053() {
        guPage
                .goToSetPolicies("Management")
                .setPolicy(3)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_054() {
        guPage
                .goToSetPolicies("Information")
                .setAllPolicies()
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_055() {
        guPage
                .goToSetPolicies("Information")
                .setPolicy(1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_056() {
        guPage
                .goToSetPolicies("Information")
                .setPolicy(2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_057() {
        guPage
                .goToSetPolicies("Information")
                .setPolicy(3)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_058() {
        guPage
                .goToSetPolicies("Time")
                .setAllPolicies()
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_059() {
        guPage
                .goToSetPolicies("Time")
                .setPolicy(1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_060() {
        guPage
                .goToSetPolicies("Time")
                .setPolicy(2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_061() {
        guPage
                .goToSetPolicies("Time")
                .setPolicy(3)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_062() {
        guPage
                .goToSetPolicies("WiFi")
                .setAllPolicies()
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_063() {
        guPage
                .goToSetPolicies("WiFi")
                .setPolicy(1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_064() {
        guPage
                .goToSetPolicies("WiFi")
                .setPolicy(2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_065() {
        guPage
                .goToSetPolicies("WiFi")
                .setPolicy(3)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_066() {
        guPage
                .goToSetPolicies("IP")
                .setAllPolicies()
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_067() {
        guPage
                .goToSetPolicies("IP")
                .setPolicy(1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_068() {
        guPage
                .goToSetPolicies("IP")
                .setPolicy(2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_069() {
        guPage
                .goToSetPolicies("IP")
                .setPolicy(3)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_070() {
        guPage
                .goToSetPolicies("Firewall")
                .setAllPolicies()
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_071() {
        guPage
                .goToSetPolicies("Firewall")
                .setPolicy(1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_072() {
        guPage
                .goToSetPolicies("Firewall")
                .setPolicy(2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_073() {
        guPage
                .goToSetPolicies("Firewall")
                .setPolicy(3)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_074() {
        guPage
                .goToSetPolicies("DHCPv4")
                .setAllPolicies()
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_075() {
        guPage
                .goToSetPolicies("DHCPv4")
                .setPolicy(1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_076() {
        guPage
                .goToSetPolicies("DHCPv4")
                .setPolicy(2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_077() {
        guPage
                .goToSetPolicies("DHCPv4")
                .setPolicy(3)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_078() {
        guPage
                .goToSetPolicies("DHCPv6")
                .setAllPolicies()
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_079() {
        guPage
                .goToSetPolicies("DHCPv6")
                .setPolicy(1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_080() {
        guPage
                .goToSetPolicies("DHCPv6")
                .setPolicy(2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_081() {
        guPage
                .goToSetPolicies("DHCPv6")
                .setPolicy(3)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_082() {
        guPage
                .goToSetPolicies("DNS")
                .setAllPolicies()
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_083() {
        guPage
                .goToSetPolicies("DNS")
                .setPolicy(1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_084() {
        guPage
                .goToSetPolicies("DNS")
                .setPolicy(2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_085() {
        guPage
                .goToSetPolicies("DNS")
                .setPolicy(3)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_086() {
        guPage
                .goToSetPolicies("Users")
                .setAllPolicies()
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_087() {
        guPage
                .goToSetPolicies("Users")
                .setPolicy(1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_088() {
        guPage
                .goToSetPolicies("Users")
                .setPolicy(2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_089() {
        guPage
                .goToSetPolicies("Users")
                .setPolicy(3)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_090() {
        guPage
                .goToSetPolicies("Ethernet")
                .setAllPolicies()
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_091() {
        guPage
                .goToSetPolicies("Ethernet")
                .setPolicy(1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_092() {
        guPage
                .goToSetPolicies("Ethernet")
                .setPolicy(2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_093() {
        guPage
                .goToSetPolicies("Ethernet")
                .setPolicy(3)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_094() {
        guPage
                .gotoAction()
                .reprovisionRadioButton()
                .nextSaveAndActivate()
                .assertPresenceOfValue("tblTasks", 2, "Device Reprovision");
    }

    @Test
    public void tr181_gu_095() {
        guPage
                .gotoAction()
                .customRpcRadioButton()
                .selectMethod("GetRPCMethods")
                .nextSaveAndActivate()
                .checkResults("Custom RPC", "GetRPCMethods");
    }

    @Test
    public void tr181_gu_096() {
        guPage
                .gotoAction()
                .customRpcRadioButton()
                .selectMethod("GetParameterNames")
                .nextSaveAndActivate()
                .checkResults("Custom RPC", "GetParameterNames");
    }

    @Test
    public void tr181_gu_097() {
        guPage
                .gotoAction()
                .customRpcRadioButton()
                .selectMethod("GetParameterAttributes")
                .nextSaveAndActivate()
                .checkResults("Custom RPC", "GetParameterAttributes");
    }

    @Test
    public void tr181_gu_098() {
        guPage
                .gotoAction()
                .customRpcRadioButton()
                .selectMethod("GetParameterValues")
                .nextSaveAndActivate()
                .checkResults("Custom RPC", "GetParameterValues");
    }

    @Test
    public void tr181_gu_099() {
        guPage
                .gotoAction()
                .customRpcRadioButton()
                .selectMethod("SetParameterValues")
                .nextSaveAndActivate()
                .checkResults("Custom RPC", "SetParameterValues");
    }

    @Test
    public void tr181_gu_100() {
        guPage
                .gotoAction()
                .customRpcRadioButton()
                .selectMethod("SetParameterAttributes")
                .nextSaveAndActivate()
                .checkResults("Custom RPC", "SetParameterAttributes");
    }

    @Test
    public void tr181_gu_101() {
        guPage
                .gotoAction()
                .customRpcRadioButton()
                .selectMethod("AddObject")
                .nextSaveAndActivate()
                .checkResults("Custom RPC", "AddObject");
    }

    @Test
    public void tr181_gu_102() {
        guPage
                .gotoAction()
                .customRpcRadioButton()
                .selectMethod("DeleteObject")
                .nextSaveAndActivate()
                .checkResults("Custom RPC", "DeleteObject");
    }

    @Test
    public void tr181_gu_103() {
        guPage
                .gotoSetParameters("time", true)
                .setAllParameters()
                .setAnyAdvancedParameter()
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_104() {
        guPage
                .gotoSetParameters("time", true)
                .setParameter(1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_105() {
        guPage
                .gotoSetParameters("time", true)
                .setParameter(2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_106() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .immediately()
                .onlineDevicesCheckBox()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("Time", 1)
                .globalButtons(NEXT)
                .globalButtons(SAVE_AND_ACTIVATE)
                .okButtonPopUp()
                .waitForStatus("Running", 5)
                .readTasksFromDb()
                .enterIntoGroup()
                .assertOnlineDevices()
                .checkResults();
    }

    @Test
    public void tr181_gu_107() {
        XmlWriter.createImportGroupFile();
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(IMPORT)
                .selectImportGuFile()
                .selectSendTo()
                .showList()
                .assertPresenceOfValue("tblDevices", 0, getSerial());
//                .assertElementIsPresent("lblTitle1");
    }

    @Test
    public void tr181_gu_108() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(IMPORT)
                .globalButtons(CANCEL)
                .assertElementIsPresent("tblParameters");
    }

    @Test
    public void tr181_gu_109() {
        guPage
                .topMenu(GROUP_UPDATE)
                .checkFiltering("Manufacturer", getManufacturer());
    }

    @Test
    public void tr181_gu_110() {
        guPage
                .topMenu(GROUP_UPDATE)
                .checkFiltering("Model", getModelName());
    }

    @Test
    public void tr181_gu_111() {
        guPage
                .topMenu(GROUP_UPDATE)
                .checkFiltering("State", "Completed")
                .checkFiltering("State", "Not active")
                .checkFiltering("State", "Error")
                .checkFiltering("State", "All");
    }

    @Test
    public void tr181_gu_112() {
        guPage
                .topMenu(GROUP_UPDATE)
                .checkSorting("Manufacturer");
    }

    @Test
    public void tr181_gu_113() {
        guPage
                .topMenu(GROUP_UPDATE)
                .checkSorting("Model");
    }

    @Test
    public void tr181_gu_114() {
        guPage
                .topMenu(GROUP_UPDATE)
                .checkSorting("Name");
    }

    @Test
    public void tr181_gu_115() {
        guPage
                .topMenu(GROUP_UPDATE)
                .checkSorting("Name")
                .checkSorting("Created");
    }

    @Test
    public void tr181_gu_116() {
        guPage
                .topMenu(GROUP_UPDATE)
                .checkSorting("Creator");
    }

    @Test
    public void tr181_gu_117() {
        guPage
                .topMenu(GROUP_UPDATE)
                .checkSorting("Updated");
    }

    @Test   //reported bug #9389
    public void tr181_gu_118() {
        guPage
                .topMenu(GROUP_UPDATE)
                .checkSorting("Activated");
    }

    @Test
    public void tr181_gu_119() {
        guPage
                .topMenu(GROUP_UPDATE)
                .selectManufacturer()
                .checkResetView();
    }

    @Test
    public void tr181_gu_120() {
        guPage
                .topMenu(GROUP_UPDATE)
                .enterIntoGroup("Manufacturer")
                .checkResetView()
                .leftMenu(VIEW)
                .itemsOnPage("10")
                .pause(5000);
    }

    @Test
    public void tr181_gu_121() {
        guPage
                .gotoGetParameter("Management")
                .getParameter(1, 1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_122() {
        guPage
                .gotoGetParameter("Information")
                .getParameter(1, 1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_123() {
        guPage
                .gotoGetParameter("Time")
                .getParameter(1, 1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_124() {
        guPage
                .gotoGetParameter("WiFi")
                .getParameter(1, 1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_125() {
        guPage
                .gotoGetParameter("IP")
                .getParameter(1, 1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_126() {
        guPage
                .gotoGetParameter("Firewall")
                .getParameter(1, 1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_127() {
        guPage
                .gotoGetParameter("DHCPv4")
                .getParameter(1, 1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_128() {
        guPage
                .gotoGetParameter("DHCPv6")
                .getParameter(1, 1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_129() {
        guPage
                .gotoGetParameter("DNS")
                .getParameter(1, 1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_130() {
        guPage
                .gotoGetParameter("Users")
                .getParameter(1, 1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_131() {
        guPage
                .gotoGetParameter("Ethernet")
                .getParameter(1, 1)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_132() {
        guPage
                .gotoGetParameter("Management")
                .getParameter(1, 2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_133() {
        guPage
                .gotoGetParameter("Information")
                .getParameter(1, 2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_134() {
        guPage
                .gotoGetParameter("Time")
                .getParameter(1, 2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_135() {
        guPage
                .gotoGetParameter("WiFi")
                .getParameter(1, 2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_136() {
        guPage
                .gotoGetParameter("IP")
                .getParameter(1, 2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_137() {
        guPage
                .gotoGetParameter("Firewall")
                .getParameter(1, 2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_138() {
        guPage
                .gotoGetParameter("DHCPv4")
                .getParameter(1, 2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_139() {
        guPage
                .gotoGetParameter("DHCPv6")
                .getParameter(1, 2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_140() {
        guPage
                .gotoGetParameter("DNS")
                .getParameter(1, 2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_141() {
        guPage
                .gotoGetParameter("Users")
                .getParameter(1, 2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_142() {
        guPage
                .gotoGetParameter("Ethernet")
                .getParameter(1, 2)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_143() {
        guPage
                .gotoGetParameter("Management")
                .getParameter(1, 3)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_144() {
        guPage
                .gotoGetParameter("Information")
                .getParameter(1, 3)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_145() {
        guPage
                .gotoGetParameter("Time")
                .getParameter(1, 3)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_146() {
        guPage
                .gotoGetParameter("WiFi")
                .getParameter(1, 3)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_147() {
        guPage
                .gotoGetParameter("IP")
                .getParameter(1, 3)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_148() {
        guPage
                .gotoGetParameter("Firewall")
                .getParameter(1, 3)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_149() {
        guPage
                .gotoGetParameter("DHCPv4")
                .getParameter(1, 3)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_150() {
        guPage
                .gotoGetParameter("DHCPv6")
                .getParameter(1, 3)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_151() {
        guPage
                .gotoGetParameter("DNS")
                .getParameter(1, 3)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_152() {
        guPage
                .gotoGetParameter("Users")
                .getParameter(1, 3)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_153() {
        guPage
                .gotoGetParameter("Ethernet")
                .getParameter(1, 3)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_154() {
        guPage
                .gotoGetParameter("Management")
                .getParameter(1, 0)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_155() {
        guPage
                .gotoGetParameter("Information")
                .getParameter(1, 0)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_156() {
        guPage
                .gotoGetParameter("Time")
                .getParameter(1, 0)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_157() {
        guPage
                .gotoGetParameter("WiFi")
                .getParameter(1, 0)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_158() {
        guPage
                .gotoGetParameter("IP")
                .getParameter(1, 0)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_159() {
        guPage
                .gotoGetParameter("Firewall")
                .getParameter(1, 0)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_160() {
        guPage
                .gotoGetParameter("DHCPv4")
                .getParameter(1, 0)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_161() {
        guPage
                .gotoGetParameter("DHCPv6")
                .getParameter(1, 0)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_162() {
        guPage
                .gotoGetParameter("DNS")
                .getParameter(1, 0)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test   //Bug: Parameter name isn't displayed into result table;
    public void tr181_gu_163() {
        guPage
                .gotoGetParameter("Users")
                .getParameter(1, 0)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_164() {
        guPage
                .gotoGetParameter("Ethernet")
                .getParameter(1, 0)
                .nextSaveAndActivate()
                .checkResults();
    }

    @Test
    public void tr181_gu_165() {
        guPage
                .gotoBackup()
                .saveAndActivate()
                .assertPresenceOfValue("tblTasks", 0, "Backup");
    }

    @Test
    public void tr181_gu_166() {
        guPage
                .gotoBackup()
                .addCondition(1, "ManagementServer", "PeriodicInformInterval, sec", EQUAL, "60")
                .saveAndActivate(false)
                .assertPresenceOfValue("tblTasks", 0, "Backup")
                .assertPresenceOfValue("tblTasks", 1, "Present");
    }

    @Test
    public void tr181_gu_167() {
        guPage
                .gotoBackup()
                .clickOnTable("tblTasks", 1, 0)
                .deleteButton()
                .assertResultTableIsAbsent();
    }

    @Test
    public void tr181_gu_168() {
        guPage
                .gotoRestore()
                .saveAndActivate()
                .assertPresenceOfValue("tblTasks", 0, "Restore");
    }

    @Test
    public void tr181_gu_169() {
        guPage
                .gotoRestore()
                .addCondition(1, "ManagementServer", "PeriodicInformInterval, sec", EQUAL, "60")
                .globalButtons(SAVE_AND_ACTIVATE)
                .okButtonPopUp()
                .readTasksFromDb()  //No waiting for "Completed" status
                .enterIntoGroup()
                .assertPresenceOfValue("tblTasks", 0, "Restore")
                .assertPresenceOfValue("tblTasks", 1, "Present");
    }

    @Test
    public void tr181_gu_170() {
        guPage
                .gotoRestore()
                .clickOnTable("tblTasks", 1, 0)
                .deleteButton()
                .assertResultTableIsAbsent();
    }

    @Test //bug: Result table title and content mismatch
    public void tr181_gu_171() {
        guPage
                .gotoDiagnostic()
                .selectDiagnostic("Trace diagnostic")
                .inputHostField("8.8.8.8")
                .numOfRepetitionsField("3")
                .nextSaveAndActivate()
                .checkResults("Trace diagnostic", "8.8.8.8");
    }

    @Test //bug: TaskDiagnostic task with id=2657 not found, NSLookUp/NSLoopback
    public void tr181_gu_172() {
        guPage
                .gotoDiagnostic()
                .selectDiagnostic("NSLookUp diagnostics")
                .inputDnsField("8.8.8.8")
                .inputHostField("127.0.0.1")
                .nextSaveAndActivate()
                .checkResults("NSLookupDiagnostics", "8.8.8.8");
    }

    @Test //bug: Result table title and content mismatch
    public void tr181_gu_173() {
        guPage
                .gotoDiagnostic()
                .selectDiagnostic("IPPing diagnostic")
                .inputHostField("8.8.8.8")
                .nextSaveAndActivate()
                .checkResults("IPPing diagnostic", "8.8.8.8");
    }

    @Test //bug: Result table title and content mismatch
    public void tr181_gu_174() {
        guPage
                .gotoDiagnostic()
                .selectDiagnostic("Download diagnostic")
                .nextSaveAndActivate()
                .checkResults("Download diagnostic", "http://127.0.0.1/webdav/Test.cfg");
//                .assertPresenceOfValue("tblTasks", -2, "Download diagnostic");
    }

    @Test //bug: TaskDiagnostic task with id=1994 not found (analog in BT #9682)
    public void tr181_gu_175() {
        guPage
                .gotoDiagnostic()
                .selectDiagnostic("Download diagnostic")
                .addToMonitoringCheckBox()
                .nextSaveAndActivate()
                .checkResults("Download diagnostic", "http://127.0.0.1/webdav/Test.cfg");
    }

    @Test //bug: Result table title and content mismatch
    public void tr181_gu_176() {
        guPage
                .gotoDiagnostic()
                .selectDiagnostic("Upload diagnostic")
                .nextSaveAndActivate()
                .checkResults("Upload diagnostic", "http://127.0.0.1/webdav/");
    }

    @Test //bug: TaskDiagnostic task with id=1994 not found
    public void tr181_gu_177() {
        guPage
                .gotoDiagnostic()
                .selectDiagnostic("Upload diagnostic")
                .addToMonitoringCheckBox()
                .nextSaveAndActivate()
                .checkResults("Upload diagnostic", "http://127.0.0.1/webdav/");
    }

    @Test
    public void tr181_gu_178() {
        guPage
                .gotoDiagnostic()
                .selectDiagnostic("Wi-Fi Neighboring diagnostic")
                .nextSaveAndActivate()
                .assertPresenceOfValue("tblTasks", -2, "Wi-Fi Neighboring diagnostic");
    }

    @Test //bug: TaskDiagnostic task with id=1994 not found
    public void tr181_gu_179() {
        guPage
                .gotoDiagnostic()
                .selectDiagnostic("Wi-Fi Neighboring diagnostic")
                .addToMonitoringCheckBox()
                .nextSaveAndActivate()
                .assertPresenceOfValue("tblTasks", -2, "Wi-Fi Neighboring diagnostic");
    }

    @Test
    public void tr181_gu_180() {
        guPage
                .gotoSetParameters("Time")
                .setParameter(1)
                .globalButtons(NEXT)
                .globalButtons(SAVE_AND_ACTIVATE)
                .okButtonPopUp()
                .waitForStatusWithoutRefresh("Completed", 65)
                .enterIntoGroup()
                .checkResults();
    }

    @Test
    public void tr181_gu_181() {
        guPage
                .gotoFileDownload()
                .selectFileType("Vendor Configuration File")
                .manualRadioButton()
                .fillUrl(BasePage.getProps().getProperty("ftp_config_file_url"))
                .fillUserName(BasePage.getProps().getProperty("ftp_user"))
                .fillPassword(BasePage.getProps().getProperty("ftp_password"))
                .nextSaveAndActivate()
                .checkResults("Vendor Configuration File", BasePage.getProps().getProperty("ftp_config_file_url"));
    }

    @Test
    public void tr181_gu_182() {
        guPage
                .gotoFileDownload()
                .selectFileType("Firmware Image")
                .manualRadioButton()
                .fillUrl(BasePage.getProps().getProperty("ftp_image_file_url"))
                .fillUserName(BasePage.getProps().getProperty("ftp_user"))
                .fillPassword(BasePage.getProps().getProperty("ftp_password"))
                .nextSaveAndActivate()
                .checkResults("Firmware Image", BasePage.getProps().getProperty("ftp_image_file_url"));
    }

    @Test
    public void tr181_gu_183() {
        guPage
                .gotoFileDownload()
                .selectFileType("Vendor Configuration File")
                .fromListRadioButton()
                .selectFileName(1)
                .nextSaveAndActivate()
                .assertPresenceOfValue("tblTasks", 2, "Vendor Configuration File");
    }

    @Test
    public void tr181_gu_184() {
        guPage
                .gotoFileDownload()
                .selectFileType("Firmware Image")
                .fromListRadioButton()
                .selectFileName(1)
                .nextSaveAndActivate()
                .assertPresenceOfValue("tblTasks", 2, "Firmware Image");
    }

    @Test
    public void tr181_gu_185() {
        guPage
                .gotoFileUpload()
                .selectUploadFileType(1)
                .manuallyUrlRadioButton()
                .fillDescriptionUploadFile("test config file upload")
                .fillUploadUrl(BasePage.getProps().getProperty("upload_url"))
                .nextSaveAndActivate()
                .checkResults("Vendor Configuration File", BasePage.getProps().getProperty("upload_url"));
    }

    @Test
    public void tr181_gu_186() {
        guPage
                .gotoFileUpload()
                .selectUploadFileType(2)
                .manuallyUrlRadioButton()
                .fillDescriptionUploadFile("test log file upload")
                .fillUploadUrl(BasePage.getProps().getProperty("upload_url"))
                .nextSaveAndActivate()
                .checkResults("Vendor Log File", BasePage.getProps().getProperty("upload_url"));
    }

    @Test
    public void tr181_gu_187() {
        guPage
                .gotoFileUpload()
                .selectUploadFileType(1)
                .defaultUploadRadioButton()
                .fillDescriptionUploadFile("test config file upload")
                .nextSaveAndActivate()
                .checkResults("Vendor Configuration File", BasePage.getProps().getProperty("upload_url"));
    }

    @Test
    public void tr181_gu_188() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName(BaseTestCase.getTestName())
                .selectSendTo()
                .globalButtons(NEXT)
                .immediately()
                .waitUntilConnectRadioButton()
                .globalButtons(NEXT)
                .addNewTask("Upload file")
                .addTaskButton()
                .selectUploadFileType(1)
                .defaultUploadRadioButton()
                .fillDescriptionUploadFile("test config file upload")
                .nextSaveAndActivate()
                .checkResults("Vendor Configuration File", BasePage.getProps().getProperty("upload_url"));
    }

    @Test
    public void tr181_gu_189() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName(BaseTestCase.getTestName())
                .selectSendTo()
                .globalButtons(NEXT)
                .immediately()
                .waitUntilConnectRadioButton()
                .globalButtons(NEXT)
                .addNewTask("Upload file")
                .addTaskButton()
                .selectUploadFileType(2)
                .defaultUploadRadioButton()
                .fillDescriptionUploadFile("test log file upload")
                .nextSaveAndActivate()
                .checkResults("Vendor Log File", BasePage.getProps().getProperty("upload_url"));
    }

    @Test
    public void tr181_gu_190() {
        guPage
                .gotoFileUpload()
                .selectUploadFileType(2)
                .defaultUploadRadioButton()
                .fillDescriptionUploadFile("test log file upload")
                .nextSaveAndActivate()
                .checkResults("Vendor Log File", BasePage.getProps().getProperty("upload_url"));
    }

    @Test
    public void tr181_gu_191() {
        guPage
                .gotoFileUpload()
                .selectUploadFileType(1)
                .defaultUploadRadioButton()
                .fillDescriptionUploadFile("test configuration file upload")
                .globalButtons(NEXT)
                .checkResults("Vendor Configuration File", BasePage.getProps().getProperty("upload_url"))
                .clickOnTable("tblTasks", 1, 0)
                .deleteButton()
                .assertResultTableIsAbsent();
    }

    @Test
    public void tr181_gu_192() {
        guPage
                .gotoAction()
                .rebootRadioButton()
                .nextSaveAndActivate()
                .assertPresenceOfParameter("tblTasks", "Reboot");
    }

    @Test
    public void tr181_gu_193() {
        guPage
                .gotoAction()
                .rebootRadioButton()
                .globalButtons(NEXT)
                .addCondition(1, "ManagementServer", "PeriodicInformInterval, sec", EQUAL, "60")
                .saveAndActivate(false)
                .assertPresenceOfParameter("tblTasks", "Reboot");
    }

    @Test
    public void tr181_gu_194() {
        guPage
                .gotoAction()
                .factoryResetRadioButton()
                .nextSaveAndActivate()
                .assertPresenceOfParameter("tblTasks", "Factory Reset");
    }

    @Test
    public void tr181_gu_195() {
        guPage
                .gotoAction()
                .factoryResetRadioButton()
                .globalButtons(NEXT)
                .addCondition(1, "ManagementServer", "PeriodicInformInterval, sec", EQUAL, "60")
                .saveAndActivate(false)
                .assertPresenceOfParameter("tblTasks", "Factory Reset");
    }

    @Test
    public void tr181_gu_196() {
        guPage
                .gotoAction()
                .customRpcRadioButton()
                .selectMethod("Reboot")
                .nextSaveAndActivate()
                .checkResults("Custom RPC", "Reboot");
    }

    @Test
    public void tr181_gu_197() {
        guPage
                .gotoAction()
                .customRpcRadioButton()
                .selectMethod("Reboot")
                .globalButtons(NEXT)
                .addCondition(1, "ManagementServer", "PeriodicInformInterval, sec", EQUAL, "60")
                .saveAndActivate(false)
                .checkResults("Custom RPC", "Reboot");
    }

    @Test
    public void tr181_gu_198() {
        guPage
                .gotoAction()
                .customRpcRadioButton()
                .selectMethod("Download")
                .nextSaveAndActivate()
                .checkResults("Custom RPC", "Download");
    }

    @Test
    public void tr181_gu_199() {
        guPage
                .gotoAction()
                .customRpcRadioButton()
                .selectMethod("Download")
                .globalButtons(NEXT)
                .addCondition(1, "ManagementServer", "PeriodicInformInterval, sec", EQUAL, "60")
                .saveAndActivate(false)
                .checkResults("Custom RPC", "Download");
    }

    @Test   //needs running device
    public void tr181_gu_200() {
        guPage
                .gotoAction()
                .customRpcRadioButton()
                .selectMethod("Upload")
                .globalButtons(NEXT)
                .globalButtons(SAVE_AND_ACTIVATE)
                .okButtonPopUp()
                .selectItem()
                .globalButtons(PAUSE)
                .okButtonPopUp()
                .waitForStatus("Paused", 5)
                .readTasksFromDb()
                .pause(1000)
                .enterIntoGroup()
                .checkResults("Custom RPC", "Upload");
    }

    @Test
    public void tr181_gu_201() {
        guPage
                .gotoAction()
                .customRpcRadioButton()
                .selectMethod("Upload")
                .globalButtons(NEXT)
                .addCondition(1, "ManagementServer", "PeriodicInformInterval, sec", EQUAL, "60")
                .saveAndActivate(false)
                .checkResults("Custom RPC", "Upload");
    }

    @Test
    public void tr181_gu_202() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName(BaseTestCase.getTestName())
                .selectSendTo()
                .globalButtons(NEXT)
                .immediately()
                .selectRepeatsDropDown("Hourly")
                .selectRepeatEveryHourDropDown("1")
                .globalButtons(NEXT)
                .addNewTask("Action")
                .addTaskButton()
                .customRpcRadioButton()
                .selectMethod("FactoryReset")
                .globalButtons(NEXT)
                .globalButtons(SAVE_AND_ACTIVATE)
                .okButtonPopUp()
                .waitForStatus("Reactivation", 30)
                .enterIntoGroup()
                .checkResults("Custom RPC", "FactoryReset");
    }

    @Test
    public void tr181_gu_203() {
        guPage
                .gotoAction()
                .customRpcRadioButton()
                .selectMethod("FactoryReset")
                .globalButtons(NEXT)
                .addCondition(1, "ManagementServer", "PeriodicInformInterval, sec", EQUAL, "60")
                .saveAndActivate(false)
                .checkResults("Custom RPC", "FactoryReset");
    }

    @Test
    public void tr181_gu_204() {
        guPage
                .topMenu(GROUP_UPDATE)
                .checkFiltering("State", "Scheduled");
    }

    @Test
    public void tr181_gu_205() {
        guPage
                .topMenu(GROUP_UPDATE)
                .checkFiltering("State", "Running");
    }

    @Test
    public void tr181_gu_206() {
        guPage
                .topMenu(GROUP_UPDATE)
                .checkFiltering("State", "Paused");
    }

    @Test
    public void tr181_gu_207() {
        guPage
                .topMenu(GROUP_UPDATE)
                .checkFiltering("State", "Reactivation");
    }

    @Test
    public void tr181_gu_208() {
        guPage
                .gotoAddFilter()
                .selectColumnFilter("device_created")
                .compareSelect("Is null")
                .globalButtons(NEXT)
                .globalButtons(FINISH)
                .okButtonPopUp()
                .assertTrue(guPage.isElementDisplayed("lblNoSelectedCpes"), "Cannot find label'No devices selected'!\n");
    }

    @Test
    public void tr181_gu_209() {
        guPage
                .gotoAddFilter()
                .selectColumnFilter("device_created")
                .compareSelect("Is not null")
                .globalButtons(NEXT)
                .globalButtons(FINISH)
                .okButtonPopUp()
                .assertFalse(guPage.isElementDisplayed("lblNoSelectedCpes"), "No devices selected!\n")
                .globalButtons(NEXT)
                .immediately()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .nextSaveAndActivate()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_210() {
        DataBaseConnector.createFilterPreconditions(BasePage.getSerial());
        guPage
                .gotoAddFilter()
                .selectColumnFilter("Created")
                .compareSelect("On Day")
                .clickOn("calFilterDate_image")
                .selectDate(CalendarUtil.getShiftedDate(-10))
                .globalButtons(NEXT)
                .globalButtons(FINISH)
                .okButtonPopUp()
                .assertFalse(guPage.isElementDisplayed("lblNoSelectedCpes"), "No devices selected!\n")
                .globalButtons(NEXT)
                .immediately()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .nextSaveAndActivate()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_211() {
        guPage
                .gotoAddFilter()
                .selectColumnFilter("Created")
                .compareSelect("Prior to")
                .clickOn("calFilterDate_image")
                .selectDate(CalendarUtil.getShiftedDate(-9))
                .inputText("txtTimeHour", CalendarUtil.getHours())
                .inputText("txtTimeMinute", CalendarUtil.getMinutes())
                .globalButtons(NEXT)
                .globalButtons(FINISH)
                .okButtonPopUp()
                .assertFalse(guPage.isElementDisplayed("lblNoSelectedCpes"), "No devices selected!\n")
                .globalButtons(NEXT)
                .immediately()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .nextSaveAndActivate()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_212() {
        guPage
                .gotoAddFilter()
                .selectColumnFilter("Created")
                .compareSelect("Later than")
                .clickOn("calFilterDate_image")
                .selectDate(CalendarUtil.getShiftedDate(-9))
                .inputText("txtTimeHour", CalendarUtil.getHours())
                .inputText("txtTimeMinute", CalendarUtil.getMinutes())
                .globalButtons(NEXT)
                .globalButtons(FINISH)
                .okButtonPopUp()
                .assertFalse(guPage.isElementDisplayed("lblNoSelectedCpes"), "No devices selected by filter 'Created - Later than'!\n")
                .globalButtons(NEXT)
                .immediately()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .nextSaveAndActivate()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_213() {
        guPage
                .gotoAddFilter()
                .selectColumnFilter("Created")
                .compareSelect("Today")
                .globalButtons(NEXT)
                .globalButtons(FINISH)
                .okButtonPopUp()
                .assertFalse(guPage.isElementDisplayed("lblNoSelectedCpes"), "No devices selected by filter 'Created - Today'!\n")
                .globalButtons(NEXT)
                .immediately()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .nextSaveAndActivate()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_214() {
        guPage
                .gotoAddFilter()
                .selectColumnFilter("Created")
                .compareSelect("Before Today")
                .globalButtons(NEXT)
                .globalButtons(FINISH)
                .okButtonPopUp()
                .assertFalse(guPage.isElementDisplayed("lblNoSelectedCpes"), "No devices selected by filter 'Created - Before Today'!\n")
                .globalButtons(NEXT)
                .immediately()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .nextSaveAndActivate()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_215() {
        guPage
                .gotoAddFilter()
                .selectColumnFilter("Created")
                .compareSelect("Yesterday")
                .globalButtons(NEXT)
                .globalButtons(FINISH)
                .okButtonPopUp()
                .assertFalse(guPage.isElementDisplayed("lblNoSelectedCpes"), "No devices selected by filter 'Created - Yesterday'!\n")
                .globalButtons(NEXT)
                .immediately()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .nextSaveAndActivate()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_216() {
        guPage
                .gotoAddFilter()
                .selectColumnFilter("Created")
                .compareSelect("Prev 7 days")
                .globalButtons(NEXT)
                .globalButtons(FINISH)
                .okButtonPopUp()
                .assertFalse(guPage.isElementDisplayed("lblNoSelectedCpes"), "No devices selected by filter 'Created - Prev 7 days'!\n")
                .globalButtons(NEXT)
                .immediately()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .nextSaveAndActivate()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_217() {
        guPage
                .gotoAddFilter()
                .selectColumnFilter("Created")
                .compareSelect("Prev X days")
                .inputText("txtInt", "9")
                .globalButtons(NEXT)
                .globalButtons(FINISH)
                .okButtonPopUp()
                .assertFalse(guPage.isElementDisplayed("lblNoSelectedCpes"), "No devices selected by filter 'Created - Prev X days'!\n")
                .globalButtons(NEXT)
                .immediately()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .nextSaveAndActivate()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_218() {
        guPage
                .presetFilter("mycust03", testName)
                .gotoAddFilter()
                .selectColumnFilter("mycust03")
                .compareSelect("=")
                .inputText("txtText", testName)
                .globalButtons(NEXT)
                .globalButtons(FINISH)
                .okButtonPopUp()
                .assertFalse(guPage.isElementDisplayed("lblNoSelectedCpes"), "No devices selected by filter 'mycust03 - equals'!\n")
                .globalButtons(NEXT)
                .immediately()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .nextSaveAndActivate()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_219() {
        guPage
                .presetFilter("mycust03", testName)
                .gotoAddFilter()
                .selectColumnFilter("mycust03")
                .compareSelect("!=")
                .inputText("txtText", testName)
                .globalButtons(NEXT)
                .globalButtons(FINISH)
                .okButtonPopUp()
                .assertFalse(guPage.isElementDisplayed("lblNoSelectedCpes"), "No devices selected by filter 'mycust03 - not equals'!\n")
                .globalButtons(NEXT)
                .immediately()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .nextSaveAndActivate()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_220() {
        guPage
//                .presetFilter("mycust03", testName)
                .gotoAddFilter()
                .selectColumnFilter("Hardware version")
                .compareSelect("Starts with")
                .inputText("txtText", testName)
                .globalButtons(NEXT)
                .globalButtons(FINISH)
                .okButtonPopUp()
                .assertTrue(guPage.isElementDisplayed("lblNoSelectedCpes"), "Cannot find label'No devices selected'!\n");
    }

    @Test
    public void tr181_gu_221() {
        guPage
                .presetFilter("mycust03", testName)
                .gotoAddFilter()
                .selectColumnFilter("mycust03")
                .compareSelect("Like")
                .inputText("txtText", testName.substring(1, 5))
                .globalButtons(NEXT)
                .globalButtons(FINISH)
                .okButtonPopUp()
                .assertFalse(guPage.isElementDisplayed("lblNoSelectedCpes"), "No devices selected by filter 'mycust03 - Like'!\n")
                .globalButtons(NEXT)
                .immediately()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .nextSaveAndActivate()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_222() {
        guPage
                .presetFilter("mycust03", testName)
                .gotoAddFilter()
                .selectColumnFilter("mycust03")
                .compareSelect("No like")
                .inputText("txtText", testName.substring(1, 5))
                .globalButtons(NEXT)
                .globalButtons(FINISH)
                .okButtonPopUp()
                .assertFalse(guPage.isElementDisplayed("lblNoSelectedCpes"), "No devices selected by filter 'mycust03 - No like'!\n")
                .globalButtons(NEXT)
                .immediately()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .nextSaveAndActivate()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_223() {
        guPage
                .presetFilter("mycust03", testName)
                .gotoAddFilter()
                .selectColumnFilter("mycust03")
                .compareSelect("Is null")
                .globalButtons(NEXT)
                .globalButtons(FINISH)
                .okButtonPopUp()
                .assertFalse(guPage.isElementDisplayed("lblNoSelectedCpes"), "No devices selected by filter 'mycust03 - Is null'!\n")
                .globalButtons(NEXT)
                .immediately()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .nextSaveAndActivate()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_224() {
        guPage
                .presetFilter("mycust03", testName)
                .gotoAddFilter()
                .selectColumnFilter("mycust03")
                .compareSelect("Is not null")
                .globalButtons(NEXT)
                .globalButtons(FINISH)
                .okButtonPopUp()
                .assertFalse(guPage.isElementDisplayed("lblNoSelectedCpes"), "No devices selected by filter 'mycust03 - Is not null'!\n")
                .globalButtons(NEXT)
                .immediately()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .nextSaveAndActivate()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_225() {
        guPage
                .gotoAddFilter()
                .selectColumnFilter("Created")
                .compareSelect("Is not null")
                .globalButtons(CANCEL)
                .waitForUpdate()
                .assertTrue(guPage.isElementDisplayed("lblHead"), "Filter creation didn't cancel properly!\n");
    }

    @Test
    public void tr181_gu_226() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .createGroup()
                .fillName()
                .globalButtons(NEXT)
                .globalButtons(PREVIOUS)
                .globalButtons(CANCEL)
                .waitForUpdate()
                .assertFalse(guPage.isOptionPresent("ddlSend", testName), "Option '" + testName + "' is present on 'Send to' list!\n");
    }

    @Test
    public void tr181_gu_227() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_228() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .waitUntilConnectRadioButton()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_229() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .setPeriod(1)
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_230() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .setPeriod(1)
                .waitUntilConnectRadioButton()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_231() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .setPeriod(1)
                .setPeriod(2)
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_232() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .setPeriod(1)
                .setPeriod(2)
                .waitUntilConnectRadioButton()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_233() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .onlineDevicesCheckBox()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_234() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .onlineDevicesCheckBox()
                .waitUntilConnectRadioButton()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_235() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .setPeriod(1)
                .onlineDevicesCheckBox()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_236() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .setPeriod(1)
                .onlineDevicesCheckBox()
                .waitUntilConnectRadioButton()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_237() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .immediately()
                .setPeriod(1)
                .onlineDevicesCheckBox()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Not active", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_238() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .immediately()
                .setPeriod(1)
                .onlineDevicesCheckBox()
                .waitUntilConnectRadioButton()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Not active", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_239() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .immediately()
                .setPeriod(1)
                .setPeriod(2)
                .onlineDevicesCheckBox()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Not active", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_240() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .immediately()
                .setPeriod(1)
                .setPeriod(2)
                .onlineDevicesCheckBox()
                .waitUntilConnectRadioButton()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Not active", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_241() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .setPeriod(1)
                .setThreshold(50)
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_242() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .setPeriod(1)
                .setThreshold(50)
                .waitUntilConnectRadioButton()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_243() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .selectShiftedDate("calDate", 1)
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_244() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .selectRepeatsDropDown("Hourly")
                .selectRepeatEveryHourDropDown("1")
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_245() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .selectRepeatsDropDown("Hourly")
                .selectRepeatEveryHourDropDown("1")
                .selectShiftedDate("calReactivationStartsOnDay", 2)
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_246() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .selectRepeatsDropDown("Hourly")
                .selectRepeatEveryHourDropDown("2")
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_247() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .selectRepeatsDropDown("Hourly")
                .selectRepeatEveryHourDropDown("1")
                .endAfterRadiobutton()
                .inputText("txtReactivationEndsOccurrences", "1")
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_248() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .selectRepeatsDropDown("Hourly")
                .selectRepeatEveryHourDropDown("1")
                .endAOnRadiobutton()
                .selectShiftedDate("calReactivationEndsOnDay", 8)
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_249() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .selectRepeatsDropDown("Hourly")
                .selectRepeatEveryHourDropDown("1")
                .runOnFailed()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_250() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .selectRepeatsDropDown("Daily")
                .selectRepeatEveryDayDropDown("1")
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_251() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .selectRepeatsDropDown("Daily")
                .selectRepeatEveryDayDropDown("1")
                .selectShiftedDate("calReactivationStartsOnDay", 2)
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_252() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .selectRepeatsDropDown("Daily")
                .selectRepeatEveryDayDropDown("2")
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_253() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .selectRepeatsDropDown("Daily")
                .selectRepeatEveryDayDropDown("1")
                .endAfterRadiobutton()
                .inputText("txtReactivationEndsOccurrences", "2")
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_254() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .selectRepeatsDropDown("Daily")
                .selectRepeatEveryDayDropDown("1")
                .endAOnRadiobutton()
                .selectShiftedDate("calReactivationEndsOnDay", 8)
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_255() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .selectRepeatsDropDown("Daily")
                .selectRepeatEveryDayDropDown("1")
                .runOnFailed()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_256() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .selectRepeatsDropDown("Weekly")
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_257() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .immediately()
                .selectRepeatsDropDown("Weekly")
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Not active", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_258() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .immediately()
                .selectRepeatsDropDown("Weekly")
                .endAOnRadiobutton()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Not active", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_259() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .selectRepeatsDropDown("Weekly")
                .endAfterRadiobutton()
                .inputText("txtReactivationEndsOccurrences", "2")
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_260() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .selectRepeatsDropDown("Weekly")
                .endAOnRadiobutton()
                .selectShiftedDate("calReactivationEndsOnDay", 32)
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_261() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .selectRepeatsDropDown("Weekly")
                .runOnFailed()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_262() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .selectRepeatsDropDown("Monthly")
                .selectRepeatEveryMonthDropDown("1")
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_263() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .selectRepeatsDropDown("Monthly")
                .selectRepeatEveryMonthDropDown("1")
                .selectShiftedDate("calReactivationStartsOnDay", 31)
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_264() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .selectRepeatsDropDown("Monthly")
                .selectRepeatEveryMonthDropDown("2")
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_265() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .selectRepeatsDropDown("Monthly")
                .selectRepeatEveryMonthDropDown("1")
                .endAfterRadiobutton()
                .inputText("txtReactivationEndsOccurrences", "2")
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_266() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .selectRepeatsDropDown("Monthly")
                .selectRepeatEveryMonthDropDown("1")
                .endAOnRadiobutton()
                .selectShiftedDate("calReactivationEndsOnDay", 31)
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_267() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .selectRepeatsDropDown("Monthly")
                .selectRepeatEveryMonthDropDown("1")
                .runOnFailed()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_268() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .selectRepeatsDropDown("Yearly")
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_269() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .selectRepeatsDropDown("Yearly")
                .selectShiftedDate("calReactivationStartsOnDay", 2)
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_270() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .selectRepeatsDropDown("Yearly")
                .endAfterRadiobutton()
                .inputText("txtReactivationEndsOccurrences", "2")
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_271() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .selectRepeatsDropDown("Yearly")
                .endAOnRadiobutton()
                .selectShiftedDate("calReactivationEndsOnDay", 365)
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_272() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .selectRepeatsDropDown("Yearly")
                .runOnFailed()
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60");
    }

    @Test
    public void tr181_gu_273() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .setParameter("Username", VALUE, "ftacs")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Device.ManagementServer.PeriodicInformInterval", "60")
                .checkResults("Device.ManagementServer.Username", "ftacs");
    }

    @Test
    public void tr181_gu_274() {
        guPage.setScheduledParameters("Information");
    }

    @Test
    public void tr181_gu_275() {
        guPage.setScheduledParameters("WiFi");
    }

    @Test
    public void tr181_gu_276() {
        guPage.setScheduledParameters("IP");
    }

    @Test
    public void tr181_gu_277() {
        guPage.setScheduledParameters("Firewall");
    }

    @Test
    public void tr181_gu_278() {
        guPage.setScheduledParameters("DHCPv4");
    }

    @Test
    public void tr181_gu_279() {
        guPage.setScheduledParameters("DHCPv6");
    }

    @Test
    public void tr181_gu_280() {
        guPage.setScheduledParameters("DNS");
    }

    @Test
    public void tr181_gu_281() {
        guPage.setScheduledParameters("Users");
    }

    @Test
    public void tr181_gu_282() {
        guPage.setScheduledParameters("Ethernet");
    }

    @Test
    public void tr181_gu_283() {
        guPage.setScheduledPolicy("Management");
    }

    @Test
    public void tr181_gu_284() {
        guPage.setScheduledPolicy("Information");
    }

    @Test
    public void tr181_gu_285() {
        guPage.setScheduledPolicy("Time");
    }

    @Test
    public void tr181_gu_286() {
        guPage.setScheduledPolicy("WiFi");
    }

    @Test
    public void tr181_gu_287() {
        guPage.setScheduledPolicy("IP");
    }

    @Test
    public void tr181_gu_288() {
        guPage.setScheduledPolicy("Firewall");
    }

    @Test
    public void tr181_gu_289() {
        guPage.setScheduledPolicy("DHCPv4");
    }

    @Test
    public void tr181_gu_290() {
        guPage.setScheduledPolicy("DHCPv6");
    }

    @Test
    public void tr181_gu_291() {
        guPage.setScheduledPolicy("DNS");
    }

    @Test
    public void tr181_gu_292() {
        guPage.setScheduledPolicy("Users");
    }

    @Test
    public void tr181_gu_293() {
        guPage.setScheduledPolicy("Ethernet");
    }

    @Test
    public void tr181_gu_294() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .globalButtons(NEXT)
                .addNewTask("Action")
                .addTaskButton()
                .reprovisionRadioButton()
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .assertPresenceOfValue("tblTasks", 2, "Device Reprovision");
    }

    @Test
    public void tr181_gu_295() {
        guPage.scheduledCallCustomRPC("GetRPCMethods");
    }

    @Test
    public void tr181_gu_296() {
        guPage.scheduledCallCustomRPC("GetParameterNames");
    }

    @Test
    public void tr181_gu_297() {
        guPage.scheduledCallCustomRPC("GetParameterAttributes");
    }

    @Test
    public void tr181_gu_298() {
        guPage.scheduledCallCustomRPC("GetParameterValues");
    }

    @Test
    public void tr181_gu_299() {
        guPage.scheduledCallCustomRPC("SetParameterValues");
    }

    @Test
    public void tr181_gu_300() {
        guPage.scheduledCallCustomRPC("SetParameterAttributes");
    }

    @Test
    public void tr181_gu_301() {
        guPage.scheduledCallCustomRPC("AddObject");
    }

    @Test
    public void tr181_gu_302() {
        guPage.scheduledCallCustomRPC("DeleteObject");
    }

    @Test
    public void tr181_gu_303() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .globalButtons(NEXT)
                .addNewTask("Set parameter value")
                .addTaskButton()
                .globalButtons(ADVANCED_VIEW)
                .setAdvancedParameter("Device.Time", 2)
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults();
    }

    @Test
    public void tr181_gu_304() {
        guPage.getScheduledParameter("Management", 1);
    }

    @Test
    public void tr181_gu_305() {
        guPage.getScheduledParameter("Information", 1);
    }

    @Test
    public void tr181_gu_306() {
        guPage.getScheduledParameter("Time", 1);
    }

    @Test
    public void tr181_gu_307() {
        guPage.getScheduledParameter("WiFi", 1);
    }

    @Test
    public void tr181_gu_308() {
        guPage.getScheduledParameter("IP", 1);
    }

    @Test
    public void tr181_gu_309() {
        guPage.getScheduledParameter("Firewall", 1);
    }

    @Test
    public void tr181_gu_310() {
        guPage.getScheduledParameter("DHCPv4", 1);
    }

    @Test
    public void tr181_gu_311() {
        guPage.getScheduledParameter("DHCPv6", 1);
    }

    @Test
    public void tr181_gu_312() {
        guPage.getScheduledParameter("DNS", 1);
    }

    @Test
    public void tr181_gu_313() {
        guPage.getScheduledParameter("Users", 1);
    }

    @Test
    public void tr181_gu_314() {
        guPage.getScheduledParameter("Ethernet", 1);
    }

    @Test
    public void tr181_gu_315() {
        guPage.getScheduledParameter("Management", 2);
    }

    @Test
    public void tr181_gu_316() {
        guPage.getScheduledParameter("Information", 2);
    }

    @Test
    public void tr181_gu_317() {
        guPage.getScheduledParameter("Time", 2);
    }

    @Test
    public void tr181_gu_318() {
        guPage.getScheduledParameter("WiFi", 2);
    }

    @Test
    public void tr181_gu_319() {
        guPage.getScheduledParameter("IP", 2);
    }

    @Test
    public void tr181_gu_320() {
        guPage.getScheduledParameter("Firewall", 2);
    }

    @Test
    public void tr181_gu_321() {
        guPage.getScheduledParameter("DHCPv4", 2);
    }

    @Test
    public void tr181_gu_322() {
        guPage.getScheduledParameter("DHCPv6", 2);
    }

    @Test
    public void tr181_gu_323() {
        guPage.getScheduledParameter("DNS", 2);
    }

    @Test
    public void tr181_gu_324() {
        guPage.getScheduledParameter("Users", 2);
    }

    @Test
    public void tr181_gu_325() {
        guPage.getScheduledParameter("Ethernet", 2);
    }

    @Test
    public void tr181_gu_326() {
        guPage.getScheduledParameter("Management", 3);
    }

    @Test
    public void tr181_gu_327() {
        guPage.getScheduledParameter("Information", 3);
    }

    @Test
    public void tr181_gu_328() {
        guPage.getScheduledParameter("Time", 3);
    }

    @Test
    public void tr181_gu_329() {
        guPage.getScheduledParameter("WiFi", 3);
    }

    @Test
    public void tr181_gu_330() {
        guPage.getScheduledParameter("IP", 3);
    }

    @Test
    public void tr181_gu_331() {
        guPage.getScheduledParameter("Firewall", 3);
    }

    @Test
    public void tr181_gu_332() {
        guPage.getScheduledParameter("DHCPv4", 3);
    }

    @Test
    public void tr181_gu_333() {
        guPage.getScheduledParameter("DHCPv6", 3);
    }

    @Test
    public void tr181_gu_334() {
        guPage.getScheduledParameter("DNS", 3);
    }

    @Test
    public void tr181_gu_335() {
        guPage.getScheduledParameter("Users", 3);
    }

    @Test
    public void tr181_gu_336() {
        guPage.getScheduledParameter("Ethernet", 3);
    }

    @Test
    public void tr181_gu_337() {
        guPage.getScheduledParameter("Management", 0);
    }

    @Test
    public void tr181_gu_338() {
        guPage.getScheduledParameter("Information", 0);
    }

    @Test
    public void tr181_gu_339() {
        guPage.getScheduledParameter("Time", 0);
    }

    @Test
    public void tr181_gu_340() {
        guPage.getScheduledParameter("WiFi", 0);
    }

    @Test
    public void tr181_gu_341() {
        guPage.getScheduledParameter("IP", 0);
    }

    @Test
    public void tr181_gu_342() {
        guPage.getScheduledParameter("Firewall", 0);
    }

    @Test
    public void tr181_gu_343() {
        guPage.getScheduledParameter("DHCPv4", 0);
    }

    @Test
    public void tr181_gu_344() {
        guPage.getScheduledParameter("DHCPv6", 0);
    }

    @Test
    public void tr181_gu_345() {
        guPage.getScheduledParameter("DNS", 0);
    }

    @Test   //Bug: Parameter name isn't displayed into result table;
    public void tr181_gu_346() {
        guPage.getScheduledParameter("Users", 0);
    }

    @Test
    public void tr181_gu_347() {
        guPage.getScheduledParameter("Ethernet", 0);
    }

    @Test
    public void tr181_gu_348() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .globalButtons(NEXT)
                .addNewTask("Backup")
                .addTaskButton()
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .assertPresenceOfValue("tblTasks", 0, "Backup");
    }

    @Test
    public void tr181_gu_349() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .globalButtons(NEXT)
                .addNewTask("Restore")
                .addTaskButton()
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .assertPresenceOfValue("tblTasks", 0, "Restore");
    }

    @Test
    public void tr181_gu_350() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .globalButtons(NEXT)
                .addNewTask("diagnostic")
                .addTaskButton()
                .selectDiagnostic("IPPing diagnostic")
                .inputHostField("8.8.8.8")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .assertPresenceOfValue("tblTasks", -2, "IPPing diagnostic");

    }

    @Test
    public void tr181_gu_351() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .globalButtons(NEXT)
                .addNewTask("Download file")
                .addTaskButton()
                .selectFileType("Vendor Configuration File")
                .manualRadioButton()
                .fillUrl(BasePage.getProps().getProperty("ftp_config_file_url"))
                .fillUserName(BasePage.getProps().getProperty("ftp_user"))
                .fillPassword(BasePage.getProps().getProperty("ftp_password"))
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Vendor Configuration File", BasePage.getProps().getProperty("ftp_config_file_url"));
    }

    @Test
    public void tr181_gu_352() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .globalButtons(NEXT)
                .addNewTask("Download file")
                .addTaskButton()
                .selectFileType("Firmware Image")
                .manualRadioButton()
                .fillUrl(BasePage.getProps().getProperty("ftp_image_file_url"))
                .fillUserName(BasePage.getProps().getProperty("ftp_user"))
                .fillPassword(BasePage.getProps().getProperty("ftp_password"))
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Firmware Image", BasePage.getProps().getProperty("ftp_image_file_url"));
    }

    @Test
    public void tr181_gu_353() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .globalButtons(NEXT)
                .addNewTask("Download file")
                .addTaskButton()
                .selectFileType("Vendor Configuration File")
                .fromListRadioButton()
                .selectFileName(1)
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .assertPresenceOfValue("tblTasks", 2, "Vendor Configuration File");
    }

    @Test
    public void tr181_gu_354() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .globalButtons(NEXT)
                .addNewTask("Download file")
                .addTaskButton()
                .selectFileType("Firmware Image")
                .fromListRadioButton()
                .selectFileName(1)
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .assertPresenceOfValue("tblTasks", -2, "Firmware Image");
    }

    @Test
    public void tr181_gu_355() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .globalButtons(NEXT)
                .addNewTask("Upload file")
                .addTaskButton()
                .selectUploadFileType(1)
                .manuallyUrlRadioButton()
                .fillUploadUrl(BasePage.getProps().getProperty("upload_url"))
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Vendor Configuration File", BasePage.getProps().getProperty("upload_url"));
    }

    @Test
    public void tr181_gu_356() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .globalButtons(NEXT)
                .addNewTask("Upload file")
                .addTaskButton()
                .selectUploadFileType(2)
                .manuallyUrlRadioButton()
                .fillUploadUrl(BasePage.getProps().getProperty("upload_url"))
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Vendor Log File", BasePage.getProps().getProperty("upload_url"));
    }

    @Test
    public void tr181_gu_357() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .globalButtons(NEXT)
                .addNewTask("Upload file")
                .addTaskButton()
                .selectUploadFileType(1)
                .defaultUploadRadioButton()
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Vendor Configuration File", BasePage.getProps().getProperty("upload_url"));
    }

    @Test
    public void tr181_gu_358() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .globalButtons(NEXT)
                .addNewTask("Action")
                .addTaskButton()
                .rebootRadioButton()
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .assertPresenceOfParameter("tblTasks", "Reboot");
    }

    @Test
    public void tr181_gu_359() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .globalButtons(NEXT)
                .addNewTask("Action")
                .addTaskButton()
                .factoryResetRadioButton()
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .assertPresenceOfParameter("tblTasks", "Factory Reset");
    }

    @Test
    public void tr181_gu_360() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .globalButtons(NEXT)
                .addNewTask("Action")
                .addTaskButton()
                .customRpcRadioButton()
                .selectMethod("Reboot")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Custom RPC", "Reboot");
    }

    @Test
    public void tr181_gu_361() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .globalButtons(NEXT)
                .addNewTask("Action")
                .addTaskButton()
                .customRpcRadioButton()
                .selectMethod("Download")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Custom RPC", "Download");
    }

    @Test
    public void tr181_gu_362() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .globalButtons(NEXT)
                .addNewTask("Action")
                .addTaskButton()
                .customRpcRadioButton()
                .selectMethod("Upload")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Custom RPC", "Upload");
    }

    @Test
    public void tr181_gu_363() {
        guPage
                .topMenu(GROUP_UPDATE)
                .leftMenu(NEW)
                .selectManufacturer()
                .selectModel()
                .fillName()
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .setDelay(10)
                .globalButtons(NEXT)
                .addNewTask("Action")
                .addTaskButton()
                .customRpcRadioButton()
                .selectMethod("FactoryReset")
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForStatus("Scheduled", 5)
                .enterIntoGroup()
                .checkResults("Custom RPC", "FactoryReset");
    }

}
