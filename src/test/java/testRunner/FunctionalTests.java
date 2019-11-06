package testRunner;

import com.friendly.aqa.utils.HttpGetter;
import org.testng.Assert;
import org.testng.annotations.Test;
import test.BaseTestCase;

import java.io.IOException;

import static com.friendly.aqa.pageobject.GlobalButtons.*;
import static com.friendly.aqa.pageobject.GroupUpdatePage.Left.NEW;
import static com.friendly.aqa.pageobject.TopMenu.GROUP_UPDATE;
import static com.friendly.aqa.utils.Table.Parameter.*;
import static com.friendly.aqa.utils.Table.Policy.*;

public class FunctionalTests extends BaseTestCase {
    @Test
    public void test_001() {
        systemPage.topMenu(GROUP_UPDATE);
        groupUpdatePage.waitForRefresh();
        Assert.assertTrue(groupUpdatePage.noDataFoundLabelIsPresent());
    }

    @Test
    public void test_002() {
        groupUpdatePage
                .topMenu(GROUP_UPDATE);
        groupUpdatePage
                .leftMenu(NEW)
                .selectManufacturer("sercomm")
                .globalButtons(CANCEL);
        Assert.assertTrue(groupUpdatePage.noDataFoundLabelIsPresent());
    }

    @Test
    public void test_003() {
        groupUpdatePage
                .topMenu(GROUP_UPDATE);
        groupUpdatePage
                .leftMenu(NEW)
                .selectManufacturer("sercomm")
                .selectModel()
                .fillName("auto_test_1")
                .globalButtons(CANCEL);
        Assert.assertTrue(groupUpdatePage.noDataFoundLabelIsPresent());
    }

    @Test
    public void test_004() {
        groupUpdatePage
                .topMenu(GROUP_UPDATE);
        groupUpdatePage
                .leftMenu(NEW)
                .selectManufacturer("sercomm")
                .selectModel()
                .fillName("autotest")
                .selectSendTo()
                .showList();
        Assert.assertTrue(groupUpdatePage.serialNumberTableIsPresent());
    }

    @Test
    public void test_005() {
        groupUpdatePage
                .topMenu(GROUP_UPDATE);
        groupUpdatePage
                .leftMenu(NEW)
                .selectManufacturer("sercomm")
                .selectModel()
                .fillName("1234")
                .createGroup();
        Assert.assertTrue(groupUpdatePage.isButtonPresent(FINISH));
        groupUpdatePage
                .globalButtons(CANCEL)
                .waitForRefresh();
        Assert.assertEquals(groupUpdatePage.getAttributeById("txtName", "value"), "1234");
    }

    @Test
    //A Bug was found while pressing "Next" button when "Name group" is filled;
    public void test_006() {
        groupUpdatePage
                .topMenu(GROUP_UPDATE);
        groupUpdatePage
                .leftMenu(NEW)
                .selectManufacturer("sercomm")
                .selectModel()
                .fillName("1234")
                .selectSendTo()
                .createGroup()
                .fillName("test_group_name");
        //Bug is present when press "Next" button
    }

    @Test
    public void test_011() {
        groupUpdatePage
                .topMenu(GROUP_UPDATE);
        groupUpdatePage
                .leftMenu(NEW)
                .selectManufacturer("sercomm")
                .selectModel()
                .fillName("auto_test_1")
                .selectSendTo("Individual")
                .getTable("tblDevices")
                .clickOn(1, 0);
        groupUpdatePage.waitForRefresh();
        Assert.assertTrue(groupUpdatePage.isButtonActive(NEXT));
        groupUpdatePage.getTable("tblDevices")
                .clickOn(1, 0);
        groupUpdatePage.waitForRefresh();
        Assert.assertFalse(groupUpdatePage.isButtonActive(NEXT));
    }

    @Test
    //Doesn't work with Edge
    public void test_012() {
        groupUpdatePage
                .topMenu(GROUP_UPDATE);
        groupUpdatePage
                .leftMenu(NEW)
                .selectManufacturer("sercomm")
                .selectModel()
                .fillName("auto_test_1")
                .selectSendTo("Import")
                .selectImportFile()
                .showList();
        Assert.assertEquals(groupUpdatePage.getTable("tblDevices").getCellText(1, 0), "FT001SN00001SD18F7FFF521");
    }

    @Test
    public void test_014() {
        groupUpdatePage
                .topMenu(GROUP_UPDATE);
        groupUpdatePage
                .leftMenu(NEW)
                .selectManufacturer("sercomm")
                .selectModel()
                .fillName("auto_test_1")
                .selectSendTo()
                .globalButtons(NEXT)
                .immediately()
                .globalButtons(NEXT)
                .addNewTask(1)
                .addTaskButton();
        Assert.assertTrue(groupUpdatePage.isElementPresent("tblParamsValue"));
        Assert.assertFalse(groupUpdatePage.isButtonActive(SAVE_AND_ACTIVATE));
    }

    @Test
    public void test_015() {
        groupUpdatePage
                .goToSetParameters("auto_test_1")
                .setParameter("PeriodicInformInterval, sec", VALUE, "60");
        groupUpdatePage
                .waitForRefresh()
                .globalButtons(NEXT)
                .globalButtons(SAVE)
                .okButtonPopUp()
                .waitForRefresh();
        Assert.assertEquals(groupUpdatePage
                .getMainTable()
                .getCellText(4, "auto_test_1", 1), "Not active");
    }

    @Test
    public void test_016() {
        groupUpdatePage
                .topMenu(GROUP_UPDATE);
        groupUpdatePage
                .getMainTable()
                .clickOn("auto_test_1", 4);
        groupUpdatePage
                .globalButtons(EDIT)
                .globalButtons(NEXT)
                .immediately()
                .globalButtons(NEXT)
                .getTable("tblTasks")
                .clickOn("InternetGatewayDevice.ManagementServer.PeriodicInformInterval", 3);
        groupUpdatePage
                .getTable("tblParamsValue")
                .setParameter("PeriodicInformInterval, sec", VALUE, "61");
        groupUpdatePage
                .globalButtons(NEXT)
                .globalButtons(SAVE)

                .okButtonPopUp()
                .getMainTable()
                .clickOn("auto_test_1", 4);
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.ManagementServer.")
                .checkResults("PeriodicInformInterval", "61");
    }

    @Test
    public void test_017() {
        groupUpdatePage
                .goToSetParameters("auto_test_2")
                .setParameter("PeriodicInformInterval, sec", VALUE, "60");
        groupUpdatePage
                .waitForRefresh()
                .globalButtons(NEXT)
                .globalButtons(SAVE_AND_ACTIVATE)
                .okButtonPopUp()
                .waitForRefresh();
        softAssert(groupUpdatePage.getMainTable().getCellText(4, "auto_test_2", 1),
                "Completed", "Running");
    }

    @Test
    public void test_018() {
        groupUpdatePage
                .topMenu(GROUP_UPDATE);
        groupUpdatePage
                .getMainTable()
                .clickOn("auto_test_2", 4);
        groupUpdatePage
                .globalButtons(EDIT);
        Assert.assertFalse(groupUpdatePage.isInputActive("ddlSend"));
        groupUpdatePage
                .globalButtons(NEXT);
        Assert.assertFalse(groupUpdatePage.isInputActive("lrbImmediately"));
        groupUpdatePage
                .globalButtons(NEXT);
        Assert.assertFalse(groupUpdatePage.isButtonActive(SAVE_AND_ACTIVATE));
    }

    @Test
    public void test_019() {
        groupUpdatePage
                .topMenu(GROUP_UPDATE);
        groupUpdatePage
                .leftMenu(NEW)
                .selectManufacturer("sercomm")
                .selectModel()
                .fillName("auto_test_2")
                .selectSendTo()
                .globalButtons(NEXT)
                .scheduledToRadioButton()
                .timeHoursSelect(0)
                .globalButtons(NEXT)
                .waitForRefresh();
        Assert.assertEquals(groupUpdatePage.getAlertTextAndClickOk(), "Can't be scheduled to the past");
        groupUpdatePage
                .checkIsCalendarClickable();
    }

    @Test
    public void test_021() throws IOException {
        systemPage.topMenu(GROUP_UPDATE);
        groupUpdatePage.waitForRefresh();
        Assert.assertTrue(HttpGetter.getUrlSource(groupUpdatePage
                .getMainTable()
                .getExportLink("auto_test_2"))
                .contains("\"InternetGatewayDevice.ManagementServer.PeriodicInformInterval\" value=\"60\""));
    }

    @Test
    public void test_022() {
        groupUpdatePage
                .goToSetParameters("auto_test_3")
                .setParameter("PeriodicInformInterval, sec", VALUE, "60")
                .setParameter("Username", VALUE, "ftacs")
                .setParameter("Password", VALUE, "ftacs");
        groupUpdatePage
                .saveAndActivate("auto_test_3");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.ManagementServer.")
                .checkResults("PeriodicInformInterval", "60")
                .checkResults("Username", "ftacs")
                .checkResults("Password", "ftacs");
    }

    @Test
    public void test_023() {
        groupUpdatePage
                .goToSetParameters("auto_test_4")
                .setParameter("Username", VALUE, "ftacs");
        groupUpdatePage
                .saveAndActivate("auto_test_4");
        groupUpdatePage
                .getTable("tblTasks")
                .checkResults("InternetGatewayDevice.ManagementServer.Username", "ftacs");
    }

    @Test
    public void test_024() {
        groupUpdatePage
                .goToSetParameters("auto_test_5", "tabsSettings_tblTabs")
                .clickOn("Time");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setParameter("NTPServer1", VALUE, "time.windows.com")
                .setParameter("NTPServer2", VALUE, "time.nist.gov")
                .setParameter("LocalTimeZone", VALUE, "GMT+2")
                .setParameter("LocalTimeZoneName", VALUE, "Ukraine")
                .setParameter("DaylightSavingsUsed", TRUE, null)
                .setParameter("DaylightSavingsStart", VALUE, "29.03.2020 02:00")
                .setParameter("DaylightSavingsEnd", VALUE, "25.10.2020 03:00");
        groupUpdatePage
                .saveAndActivate("auto_test_5");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.Time.")
                .checkResults("NTPServer1", "time.windows.com")
                .checkResults("NTPServer2", "time.nist.gov")
                .checkResults("LocalTimeZone", "GMT+2")
                .checkResults("LocalTimeZoneName", "Ukraine")
                .checkResults("DaylightSavingsUsed", "1")
                .checkResults("DaylightSavingsStart", "29.03.2020 02:00")
                .checkResults("DaylightSavingsEnd", "25.10.2020 03:00");
    }

    @Test
    public void test_025() {
        groupUpdatePage
                .goToSetParameters("auto_test_6", "tabsSettings_tblTabs")
                .clickOn("Time");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setParameter("LocalTimeZone", VALUE, "GMT+2");
        groupUpdatePage
                .saveAndActivate("auto_test_6");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.Time.")
                .checkResults("LocalTimeZone", "GMT+2");
    }

    @Test
    public void test_026() {
        groupUpdatePage
                .goToSetParameters("auto_test_7", "tabsSettings_tblTabs")
                .clickOn("Time");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setParameter("NTPServer1", VALUE, "time.windows.com")
                .setParameter("NTPServer2", VALUE, "time.nist.gov");
        groupUpdatePage
                .saveAndActivate("auto_test_7");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.Time.")
                .checkResults("NTPServer1", "time.windows.com")
                .checkResults("NTPServer2", "time.nist.gov");
    }

    @Test
    public void test_027() {
        groupUpdatePage
                .goToSetParameters("auto_test_8", "tabsSettings_tblTabs")
                .clickOn("WAN");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setParameter("Enable", TRUE, null)
                .setParameter("ConnectionType", CUSTOM, "PPPoE_Bridged")
                .setParameter("RouteProtocolRx", CUSTOM, "Off")
                .setParameter("Name", VALUE, "PPPoE_Connection")
                .setParameter("Username", VALUE, "admin")
                .setParameter("Password", VALUE, "qwerty123")
                .setParameter("PPPoEACName", VALUE, "PPPoE_AC_Name")
                .setParameter("PPPoEServiceName", VALUE, "PPPoE_Service_Name")
                .setParameter("DNSEnabled", TRUE, null)
                .setParameter("DNSOverrideAllowed", FALSE, null)
                .setParameter("DNSServers", VALUE, "8.8.8.8")
                .setParameter("MaxMRUSize", VALUE, "1492")
                .setParameter("NATEnabled", TRUE, null)
                .setParameter("ConnectionTrigger", CUSTOM, "AlwaysOn")
                .setParameter("AutoDisconnectTime, sec", VALUE, "86400")
                .setParameter("IdleDisconnectTime, sec", VALUE, "0");
        groupUpdatePage
                .saveAndActivate("auto_test_8");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANPPPConnection.2.")
                .checkResults("Enable", "1")
                .checkResults("ConnectionType", "PPPoE_Bridged")
                .checkResults("RouteProtocolRx", "Off")
                .checkResults("Name", "PPPoE_Connection")
                .checkResults("Username", "admin")
                .checkResults("Password", "qwerty123")
                .checkResults("PPPoEACName", "PPPoE_AC_Name")
                .checkResults("PPPoEServiceName", "PPPoE_Service_Name")
                .checkResults("DNSEnabled", "1")
                .checkResults("DNSOverrideAllowed", "0")
                .checkResults("DNSServers", "8.8.8.8")
                .checkResults("MaxMRUSize", "1492")
                .checkResults("NATEnabled", "1")
                .checkResults("ConnectionTrigger", "AlwaysOn")
                .checkResults("AutoDisconnectTime", "86400")
                .checkResults("IdleDisconnectTime", "0");
    }

    @Test
    public void test_028() {
        groupUpdatePage
                .goToSetParameters("auto_test_9", "tabsSettings_tblTabs")
                .clickOn("WAN");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setParameter("Enable", FALSE, null);
        groupUpdatePage
                .saveAndActivate("auto_test_9");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANPPPConnection.2.")
                .checkResults("Enable", "0");
    }

    @Test
    public void test_029() {
        groupUpdatePage
                .goToSetParameters("auto_test_10", "tabsSettings_tblTabs")
                .clickOn("WAN");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setParameter("Enable", TRUE, null)
                .setParameter("Name", VALUE, "PPPoE_Connection_2")
                .setParameter("DNSServers", VALUE, "8.8.4.4");
        groupUpdatePage
                .saveAndActivate("auto_test_10");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANPPPConnection.2.")
                .checkResults("Enable", "1")
                .checkResults("Name", "PPPoE_Connection_2")
                .checkResults("DNSServers", "8.8.4.4");
    }

    @Test
    public void test_030() {
        groupUpdatePage
                .goToSetParameters("auto_test_11", "tabsSettings_tblTabs")
                .clickOn("LAN");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setParameter("DHCPServerConfigurable", TRUE, null)
                .setParameter("DHCPServerEnable", TRUE, null)
                .setParameter("DHCPRelay", FALSE, null)
                .setParameter("MinAddress", VALUE, "192.168.1.2")
                .setParameter("MaxAddress", VALUE, "192.168.1.254")
                .setParameter("ReservedAddresses", EMPTY_VALUE, null)
                .setParameter("SubnetMask", VALUE, "255.255.255.0")
                .setParameter("DNSServers", VALUE, "8.8.8.8")
                .setParameter("DomainName", VALUE, "friendly")
                .setParameter("IPRouters", EMPTY_VALUE, null)
                .setParameter("DHCPLeaseTime, sec", VALUE, "7200");
        groupUpdatePage
                .saveAndActivate("auto_test_11");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.")
                .checkResults("DHCPServerConfigurable", "1")
                .checkResults("DHCPServerEnable", "1")
                .checkResults("DHCPRelay", "0")
                .checkResults("MinAddress", "192.168.1.2")
                .checkResults("MaxAddress", "192.168.1.254")
                .checkResults("ReservedAddresses", " ")
                .checkResults("SubnetMask", "255.255.255.0")
                .checkResults("DNSServers", "8.8.8.8")
                .checkResults("DomainName", "friendly")
                .checkResults("IPRouters", " ")
                .checkResults("DHCPLeaseTime", "7200");
    }

    @Test
    public void test_031() {
        groupUpdatePage
                .goToSetParameters("auto_test_12", "tabsSettings_tblTabs")
                .clickOn("LAN");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setParameter("ReservedAddresses", VALUE, "192.168.100");
        groupUpdatePage
                .saveAndActivate("auto_test_12");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.")
                .checkResults("ReservedAddresses", "192.168.100");
    }

    @Test
    public void test_032() {
        groupUpdatePage
                .goToSetParameters("auto_test_13", "tabsSettings_tblTabs")
                .clickOn("LAN");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setParameter("MinAddress", VALUE, "192.168.1.50")
                .setParameter("MaxAddress", VALUE, "192.168.1.200");
        groupUpdatePage
                .saveAndActivate("auto_test_13");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.")
                .checkResults("MinAddress", "192.168.1.50")
                .checkResults("MaxAddress", "192.168.1.200");
    }

    @Test
    public void test_033() {
        groupUpdatePage
                .goToSetParameters("auto_test_14", "tabsSettings_tblTabs")
                .clickOn("Wireless");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setParameter("Enable", TRUE, null)
                .setParameter("Channel", VALUE, "6")
                .setParameter("SSID", VALUE, "Home_WiFi")
                .setParameter("BeaconType", CUSTOM, "WPA")
                .setParameter("WEPKeyIndex", VALUE, "0")
                .setParameter("BasicEncryptionModes", CUSTOM, "None")
                .setParameter("BasicAuthenticationMode", CUSTOM, "EAPAuthentication")
                .setParameter("WPAEncryptionModes", CUSTOM, "AESEncryption")
                .setParameter("WPAAuthenticationMode", CUSTOM, "EAPAuthentication")
                .setParameter("IEEE11iEncryptionModes", CUSTOM, "AESEncryption")
                .setParameter("IEEE11iAuthenticationMode", CUSTOM, "EAPandPSKAuthentication");
        groupUpdatePage
                .saveAndActivate("auto_test_14");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.")
                .checkResults("Enable", "1")
                .checkResults("Channel", "6")
                .checkResults("SSID", "Home_WiFi")
                .checkResults("BeaconType", "WPA")
                .checkResults("WEPKeyIndex", "0")
                .checkResults("BasicEncryptionModes", "None")
                .checkResults("BasicAuthenticationMode", "EAPAuthentication")
                .checkResults("WPAEncryptionModes", "AESEncryption")
                .checkResults("WPAAuthenticationMode", "EAPAuthentication")
                .checkResults("IEEE11iEncryptionModes", "AESEncryption")
                .checkResults("IEEE11iAuthenticationMode", "EAPandPSKAuthentication");
    }

    @Test
    public void test_034() {
        groupUpdatePage
                .goToSetParameters("auto_test_15", "tabsSettings_tblTabs")
                .clickOn("Wireless");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setParameter("Channel", VALUE, "11");
        groupUpdatePage
                .saveAndActivate("auto_test_15");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.")
                .checkResults("Channel", "11");
    }

    @Test
    public void test_035() {
        groupUpdatePage
                .goToSetParameters("auto_test_16", "tabsSettings_tblTabs")
                .clickOn("Wireless");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setParameter("Channel", VALUE, "1")
                .setParameter("SSID", VALUE, "WiFi");
        groupUpdatePage
                .saveAndActivate("auto_test_16");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.LANDevice.1.WLANConfiguration.1.")
                .checkResults("Channel", "1")
                .checkResults("SSID", "WiFi");
    }

    @Test
    public void test_036() {
        groupUpdatePage
                .goToSetParameters("audiocodes", "MP252", "auto_test_17", "tabsSettings_tblTabs")
                .clickOn("DSL settings");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setParameter("Enable", TRUE, null)
                .setParameter("LinkType", CUSTOM, "PPPoE")
                .setParameter("DestinationAddress", VALUE, "35.12.48.78")
                .setParameter("ATMEncapsulation", CUSTOM, "LLC");
        groupUpdatePage
                .saveAndActivate("auto_test_17");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANDSLLinkConfig.")
                .checkResults("Enable", "1")
                .checkResults("LinkType", "PPPoE")
                .checkResults("DestinationAddress", "35.12.48.78")
                .checkResults("ATMEncapsulation", "LLC");
    }

    @Test
    public void test_037() {
        groupUpdatePage
                .goToSetParameters("audiocodes", "MP252", "auto_test_18", "tabsSettings_tblTabs")
                .clickOn("DSL settings");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setParameter("DestinationAddress", VALUE, "95.217.85.220");
        groupUpdatePage
                .saveAndActivate("auto_test_18");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANDSLLinkConfig.")
                .checkResults("DestinationAddress", "95.217.85.220");
    }

    @Test
    public void test_038() {
        groupUpdatePage
                .goToSetParameters("audiocodes", "MP252", "auto_test_19", "tabsSettings_tblTabs")
                .clickOn("DSL settings");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setParameter("LinkType", CUSTOM, "EoA")
                .setParameter("ATMEncapsulation", CUSTOM, "VCMUX");
        groupUpdatePage
                .saveAndActivate("auto_test_19");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANDSLLinkConfig.")
                .checkResults("LinkType", "EoA")
                .checkResults("ATMEncapsulation", "VCMUX");
    }

    @Test
    public void test_039() {
        groupUpdatePage
                .goToSetParameters("audiocodes", "MP252", "auto_test_20", "tabsSettings_tblTabs")
                .clickOn("VoIP settings");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setParameter("Name", VALUE, "VoIP")
                .setParameter("Enable", CUSTOM, "Enabled")
                .setParameter("Reset", FALSE, null)
                .setParameter("SignalingProtocol", VALUE, "SIP")
                .setParameter("DTMFMethod", CUSTOM, "SIPInfo")
                .setParameter("Region", VALUE, "USA")
                .setParameter("DigitMapEnable", TRUE, null)
                .setParameter("DigitMap", EMPTY_VALUE, null)
                .setParameter("STUNEnable", TRUE, null)
                .setParameter("STUNServer", VALUE, "12.13.14.15")
                .setParameter("FaxPassThrough", CUSTOM, "Auto")
                .setParameter("ModemPassThrough", CUSTOM, "Auto");
        groupUpdatePage
                .saveAndActivate("auto_test_20");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.Services.VoiceService.1.VoiceProfile.1.")
                .checkResults("Name", "VoIP")
                .checkResults("Enable", "Enabled")
                .checkResults("Reset", "0")
                .checkResults("SignalingProtocol", "SIP")
                .checkResults("DTMFMethod", "SIPInfo")
                .checkResults("Region", "USA")
                .checkResults("DigitMapEnable", "1")
                .checkResults("DigitMap", " ")
                .checkResults("STUNEnable", "1")
                .checkResults("STUNServer", "12.13.14.15")
                .checkResults("FaxPassThrough", "Auto")
                .checkResults("ModemPassThrough", "Auto");
    }

    @Test
    public void test_040() {
        groupUpdatePage
                .goToSetParameters("audiocodes", "MP252", "auto_test_21", "tabsSettings_tblTabs")
                .clickOn("VoIP settings");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setParameter("Name", VALUE, "VoIP_new");
        groupUpdatePage
                .saveAndActivate("auto_test_21");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.Services.VoiceService.1.VoiceProfile.1.")
                .checkResults("Name", "VoIP_new");
    }

    @Test
    public void test_041() {
        groupUpdatePage
                .goToSetParameters("audiocodes", "MP252", "auto_test_22", "tabsSettings_tblTabs")
                .clickOn("VoIP settings");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setParameter("Region", VALUE, "EU")
                .setParameter("DigitMapEnable", FALSE, null);
        groupUpdatePage
                .saveAndActivate("auto_test_22");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.Services.VoiceService.1.VoiceProfile.1.")
                .checkResults("Region", "EU")
                .checkResults("DigitMapEnable", "0");
    }

    @Test
    public void test_042() {
        groupUpdatePage
                .goToSetPolicies("auto_test_23", "tblParamsValue")
                .setPolicy("URL", ACTIVE, ALL)
                .setPolicy("Username", ACTIVE, ALL)
                .setPolicy("Password", ACTIVE, ALL)
                .setPolicy("PeriodicInformEnable", ACTIVE, ALL)
                .setPolicy("PeriodicInformInterval, sec", ACTIVE, ALL)
                .setPolicy("PeriodicInformTime", ACTIVE, ALL)
                .setPolicy("ParameterKey", ACTIVE, ALL)
                .setPolicy("ConnectionRequestURL", ACTIVE, ALL)
                .setPolicy("ConnectionRequestUsername", ACTIVE, ALL)
                .setPolicy("ConnectionRequestPassword", ACTIVE, ALL)
                .setPolicy("UpgradesManaged", ACTIVE, ALL);
        groupUpdatePage
                .saveAndActivate("auto_test_23");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.ManagementServer.")
                .checkResults("URL", "Notification=Active Access=All")
                .checkResults("Username", "Notification=Active Access=All")
                .checkResults("Password", "Notification=Active Access=All")
                .checkResults("PeriodicInformEnable", "Notification=Active Access=All")
                .checkResults("PeriodicInformInterval", "Notification=Active Access=All")
                .checkResults("PeriodicInformTime", "Notification=Active Access=All")
                .checkResults("ParameterKey", "Notification=Active Access=All")
                .checkResults("ConnectionRequestURL", "Notification=Active Access=All")
                .checkResults("ConnectionRequestUsername", "Notification=Active Access=All")
                .checkResults("ConnectionRequestPassword", "Notification=Active Access=All")
                .checkResults("UpgradesManaged", "Notification=Active Access=All");
    }

    @Test
    public void test_043() {
        groupUpdatePage
                .goToSetPolicies("auto_test_24", "tblParamsValue")
                .setPolicy("URL", ACTIVE, ACS_ONLY)
                .setPolicy("URL", DEFAULT, null);
        groupUpdatePage
                .saveAndActivate("auto_test_24");
        groupUpdatePage
                .getTable("tblTasks")
                .checkResults("InternetGatewayDevice.ManagementServer.URL", "Access=AcsOnly");
    }

    @Test
    public void test_044() {
        groupUpdatePage
                .goToSetPolicies("auto_test_25", "tblParamsValue")
                .setPolicy("Username", OFF, ALL)
                .setPolicy("Password", OFF, ALL)
                .setPolicy("Username", null, DEFAULT)
                .setPolicy("Password", null, DEFAULT);
        groupUpdatePage
                .saveAndActivate("auto_test_25");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.ManagementServer.")
                .checkResults("Username", "Notification=Off ")
                .checkResults("Password", "Notification=Off ");
    }

    @Test
    public void test_045() {
        groupUpdatePage
                .goToSetPolicies("auto_test_26", "tblParamsValue")
                .setPolicy("URL", PASSIVE, null)
                .setPolicy("Username", ACTIVE, null)
                .setPolicy("Password", OFF, null)
                .setPolicy("PeriodicInformEnable", ACTIVE, null)
                .setPolicy("PeriodicInformEnable", DEFAULT, null)
                .setPolicy("PeriodicInformInterval, sec", null, ACS_ONLY)
                .setPolicy("PeriodicInformTime", null, ALL)
                .setPolicy("ParameterKey", null, ALL)
                .setPolicy("ParameterKey", null, DEFAULT);
        groupUpdatePage
                .saveAndActivate("auto_test_26");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.ManagementServer.")
                .checkResults("URL", "Notification=Passive ")
                .checkResults("Username", "Notification=Active ")
                .checkResults("Password", "Notification=Off ")
                .checkResults("PeriodicInformInterval", "Access=AcsOnly")
                .checkResults("PeriodicInformTime", "Access=All")
                .assertAbsenceOfParameter("PeriodicInformEnable")
                .assertAbsenceOfParameter("ParameterKey");
    }

    @Test
    public void test_046() {
        groupUpdatePage
                .goToSetPolicies("auto_test_27", "tabsSettings_tblTabs")
                .clickOn("Time");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setPolicy("NTPServer1", ACTIVE, ALL)
                .setPolicy("NTPServer2", ACTIVE, ALL)
                .setPolicy("CurrentLocalTime", ACTIVE, ALL)
                .setPolicy("LocalTimeZone", ACTIVE, ALL)
                .setPolicy("LocalTimeZoneName", ACTIVE, ALL)
                .setPolicy("DaylightSavingsUsed", ACTIVE, ALL)
                .setPolicy("DaylightSavingsStart", ACTIVE, ALL)
                .setPolicy("DaylightSavingsEnd", ACTIVE, ALL);
        groupUpdatePage
                .saveAndActivate("auto_test_27");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.Time.")
                .checkResults("NTPServer1", "Notification=Active Access=All")
                .checkResults("NTPServer2", "Notification=Active Access=All")
                .checkResults("CurrentLocalTime", "Notification=Active Access=All")
                .checkResults("LocalTimeZone", "Notification=Active Access=All")
                .checkResults("LocalTimeZoneName", "Notification=Active Access=All")
                .checkResults("DaylightSavingsUsed", "Notification=Active Access=All")
                .checkResults("DaylightSavingsStart", "Notification=Active Access=All")
                .checkResults("DaylightSavingsEnd", "Notification=Active Access=All");
    }

    @Test
    public void test_047() {
        groupUpdatePage
                .goToSetPolicies("auto_test_28", "tabsSettings_tblTabs")
                .clickOn("Time");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setPolicy("NTPServer1", ACTIVE, ACS_ONLY)
                .setPolicy("NTPServer1", DEFAULT, null);
        groupUpdatePage
                .saveAndActivate("auto_test_28");
        groupUpdatePage
                .getTable("tblTasks")
                .checkResults("InternetGatewayDevice.Time.NTPServer1", "Access=AcsOnly");
    }

    @Test
    public void test_048() {
        groupUpdatePage
                .goToSetPolicies("auto_test_29", "tabsSettings_tblTabs")
                .clickOn("Time");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setPolicy("NTPServer1", OFF, ALL)
                .setPolicy("NTPServer2", OFF, ALL)
                .setPolicy("NTPServer1", null, DEFAULT)
                .setPolicy("NTPServer2", null, DEFAULT);
        groupUpdatePage
                .saveAndActivate("auto_test_29");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.Time.")
                .checkResults("NTPServer1", "Notification=Off ")
                .checkResults("NTPServer2", "Notification=Off ");
    }

    @Test
    public void test_049() {
        groupUpdatePage
                .goToSetPolicies("auto_test_30", "tabsSettings_tblTabs")
                .clickOn("Time");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setPolicy("NTPServer1", PASSIVE, null)
                .setPolicy("NTPServer2", ACTIVE, null)
                .setPolicy("CurrentLocalTime", OFF, null)
                .setPolicy("LocalTimeZone", ACTIVE, null)
                .setPolicy("LocalTimeZone", DEFAULT, null)
                .setPolicy("LocalTimeZoneName", null, ACS_ONLY)
                .setPolicy("DaylightSavingsUsed", null, ALL)
                .setPolicy("DaylightSavingsStart", null, ALL)
                .setPolicy("DaylightSavingsStart", null, DEFAULT);
        groupUpdatePage
                .saveAndActivate("auto_test_30");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.Time.")
                .checkResults("NTPServer1", "Notification=Passive ")
                .checkResults("NTPServer2", "Notification=Active ")
                .checkResults("CurrentLocalTime", "Notification=Off ")
                .checkResults("LocalTimeZoneName", "Access=AcsOnly")
                .checkResults("DaylightSavingsUsed", "Access=All")
                .assertAbsenceOfParameter("LocalTimeZone")
                .assertAbsenceOfParameter("DaylightSavingsStart");
    }

    @Test
    public void test_050() {
        groupUpdatePage
                .goToSetPolicies("auto_test_31", "tabsSettings_tblTabs")
                .clickOn("WAN");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setPolicy("Enable", ACTIVE, ALL)
                .setPolicy("ConnectionStatus", ACTIVE, ALL)
                .setPolicy("PossibleConnectionTypes", ACTIVE, ALL)
                .setPolicy("ConnectionType", ACTIVE, ALL)
                .setPolicy("RouteProtocolRx", ACTIVE, ALL)
                .setPolicy("Name", ACTIVE, ALL)
                .setPolicy("Username", ACTIVE, ALL)
                .setPolicy("Password", ACTIVE, ALL)
                .setPolicy("PPPoEACName", ACTIVE, ALL)
                .setPolicy("PPPoEServiceName", ACTIVE, ALL)
                .setPolicy("DNSEnabled", ACTIVE, ALL)
                .setPolicy("DNSOverrideAllowed", ACTIVE, ALL)
                .setPolicy("DNSServers", ACTIVE, ALL)
                .setPolicy("MaxMRUSize", ACTIVE, ALL)
                .setPolicy("NATEnabled", ACTIVE, ALL)
                .setPolicy("ConnectionTrigger", ACTIVE, ALL)
                .setPolicy("AutoDisconnectTime, sec", ACTIVE, ALL)
                .setPolicy("IdleDisconnectTime, sec", ACTIVE, ALL);
        groupUpdatePage
                .saveAndActivate("auto_test_31");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANPPPConnection.2.")
                .checkResults("Enable", "Notification=Active Access=All")
                .checkResults("ConnectionStatus", "Notification=Active Access=All")
                .checkResults("PossibleConnectionTypes", "Notification=Active Access=All")
                .checkResults("ConnectionType", "Notification=Active Access=All")
                .checkResults("RouteProtocolRx", "Notification=Active Access=All")
                .checkResults("Name", "Notification=Active Access=All")
                .checkResults("Username", "Notification=Active Access=All")
                .checkResults("Password", "Notification=Active Access=All")
                .checkResults("PPPoEACName", "Notification=Active Access=All")
                .checkResults("PPPoEServiceName", "Notification=Active Access=All")
                .checkResults("DNSEnabled", "Notification=Active Access=All")
                .checkResults("DNSOverrideAllowed", "Notification=Active Access=All")
                .checkResults("DNSServers", "Notification=Active Access=All")
                .checkResults("MaxMRUSize", "Notification=Active Access=All")
                .checkResults("NATEnabled", "Notification=Active Access=All")
                .checkResults("ConnectionTrigger", "Notification=Active Access=All")
                .checkResults("AutoDisconnectTime", "Notification=Active Access=All")
                .checkResults("IdleDisconnectTime", "Notification=Active Access=All");
    }

    @Test
    public void test_051() {
        groupUpdatePage
                .goToSetPolicies("auto_test_32", "tabsSettings_tblTabs")
                .clickOn("WAN");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setPolicy("Enable", ACTIVE, ACS_ONLY)
                .setPolicy("Enable", DEFAULT, null);
        groupUpdatePage
                .saveAndActivate("auto_test_32");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANPPPConnection.2.")
                .checkResults("Enable", "Access=AcsOnly");
    }

    @Test
    public void test_052() {
        groupUpdatePage
                .goToSetPolicies("auto_test_33", "tabsSettings_tblTabs")
                .clickOn("WAN");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setPolicy("Username", OFF, ALL)
                .setPolicy("Password", OFF, ALL)
                .setPolicy("Username", null, DEFAULT)
                .setPolicy("Password", null, DEFAULT);
        groupUpdatePage
                .saveAndActivate("auto_test_33");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANPPPConnection.2.")
                .checkResults("Username", "Notification=Off ")
                .checkResults("Password", "Notification=Off ");
    }

    @Test
    public void test_053() {
        groupUpdatePage
                .goToSetPolicies("auto_test_34", "tabsSettings_tblTabs")
                .clickOn("WAN");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setPolicy("Enable", PASSIVE, null)
                .setPolicy("ConnectionStatus", ACTIVE, null)
                .setPolicy("PossibleConnectionTypes", OFF, null)
                .setPolicy("ConnectionType", ACTIVE, null)
                .setPolicy("ConnectionType", DEFAULT, null)
                .setPolicy("RouteProtocolRx", null, ACS_ONLY)
                .setPolicy("Name", null, ALL)
                .setPolicy("Username", null, ALL)
                .setPolicy("Username", null, DEFAULT);
        groupUpdatePage
                .saveAndActivate("auto_test_34");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.WANDevice.1.WANConnectionDevice.1.WANPPPConnection.2.")
                .checkResults("Enable", "Notification=Passive ")
                .checkResults("ConnectionStatus", "Notification=Active ")
                .checkResults("PossibleConnectionTypes", "Notification=Off ")
                .checkResults("RouteProtocolRx", "Access=AcsOnly")
                .checkResults("Name", "Access=All")
                .assertAbsenceOfParameter("ConnectionType")
                .assertAbsenceOfParameter("Username");
    }

    @Test
    public void test_054() {
        groupUpdatePage
                .goToSetPolicies("auto_test_35", "tabsSettings_tblTabs")
                .clickOn("LAN");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setPolicy("DHCPServerConfigurable", ACTIVE, ALL)
                .setPolicy("DHCPServerEnable", ACTIVE, ALL)
                .setPolicy("DHCPRelay", ACTIVE, ALL)
                .setPolicy("MinAddress", ACTIVE, ALL)
                .setPolicy("MaxAddress", ACTIVE, ALL)
                .setPolicy("ReservedAddresses", ACTIVE, ALL)
                .setPolicy("SubnetMask", ACTIVE, ALL)
                .setPolicy("DNSServers", ACTIVE, ALL)
                .setPolicy("DomainName", ACTIVE, ALL)
                .setPolicy("IPRouters", ACTIVE, ALL)
                .setPolicy("DHCPLeaseTime, sec", ACTIVE, ALL);
        groupUpdatePage
                .saveAndActivate("auto_test_35");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.")
                .checkResults("DHCPServerConfigurable", "Notification=Active Access=All")
                .checkResults("DHCPServerEnable", "Notification=Active Access=All")
                .checkResults("DHCPRelay", "Notification=Active Access=All")
                .checkResults("MinAddress", "Notification=Active Access=All")
                .checkResults("MaxAddress", "Notification=Active Access=All")
                .checkResults("ReservedAddresses", "Notification=Active Access=All")
                .checkResults("SubnetMask", "Notification=Active Access=All")
                .checkResults("DNSServers", "Notification=Active Access=All")
                .checkResults("DomainName", "Notification=Active Access=All")
                .checkResults("IPRouters", "Notification=Active Access=All")
                .checkResults("DHCPLeaseTime", "Notification=Active Access=All");
    }

    @Test
    public void test_055() {
        groupUpdatePage
                .goToSetPolicies("auto_test_36", "tabsSettings_tblTabs")
                .clickOn("LAN");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setPolicy("DHCPServerConfigurable", ACTIVE, ACS_ONLY)
                .setPolicy("DHCPServerConfigurable", DEFAULT, null);
        groupUpdatePage
                .saveAndActivate("auto_test_36");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.")
                .checkResults("DHCPServerConfigurable", "Access=AcsOnly");
    }

    @Test
    public void test_056() {
        groupUpdatePage
                .goToSetPolicies("auto_test_37", "tabsSettings_tblTabs")
                .clickOn("LAN");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setPolicy("MinAddress", OFF, ALL)
                .setPolicy("MaxAddress", OFF, ALL)
                .setPolicy("MinAddress", null, DEFAULT)
                .setPolicy("MaxAddress", null, DEFAULT);
        groupUpdatePage
                .saveAndActivate("auto_test_37");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.")
                .checkResults("MinAddress", "Notification=Off ")
                .checkResults("MaxAddress", "Notification=Off ");
    }

    @Test
    public void test_057() {
        groupUpdatePage
                .goToSetPolicies("auto_test_38", "tabsSettings_tblTabs")
                .clickOn("LAN");
        groupUpdatePage
                .getTable("tblParamsValue")
                .setPolicy("DHCPServerConfigurable", PASSIVE, null)
                .setPolicy("DHCPServerEnable", ACTIVE, null)
                .setPolicy("DHCPRelay", OFF, null)
                .setPolicy("MinAddress", ACTIVE, null)
                .setPolicy("MinAddress", DEFAULT, null)
                .setPolicy("MaxAddress", null, ACS_ONLY)
                .setPolicy("ReservedAddresses", null, ALL)
                .setPolicy("SubnetMask", null, ALL)
                .setPolicy("SubnetMask", null, DEFAULT);
        groupUpdatePage
                .saveAndActivate("auto_test_38");
        groupUpdatePage
                .getTable("tblTasks")
                .setPrefix("InternetGatewayDevice.LANDevice.1.LANHostConfigManagement.")
                .checkResults("DHCPServerConfigurable", "Notification=Passive ")
                .checkResults("DHCPServerEnable", "Notification=Active ")
                .checkResults("DHCPRelay", "Notification=Off ")
                .checkResults("MaxAddress", "Access=AcsOnly")
                .checkResults("ReservedAddresses", "Access=All")
                .assertAbsenceOfParameter("MinAddress")
                .assertAbsenceOfParameter("SubnetMask");
    }
}