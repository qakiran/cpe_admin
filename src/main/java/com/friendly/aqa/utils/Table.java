package com.friendly.aqa.utils;

import com.friendly.aqa.pageobject.BasePage;
import com.friendly.aqa.test.BaseTestCase;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Table {
    private final static Logger LOGGER = Logger.getLogger(Table.class);
    private static Map<String, String> paramSet;
    private List<WebElement> rowsList;
    private String[][] textTable;
    private WebElement[][] elementTable;
    private String prefix;
    private WebElement table;
    private Properties props;

    public Table(WebElement table) {
        this.table = table;
        props = BasePage.getProps();
        rowsList = table.findElements(By.tagName("tr"));
        textTable = new String[rowsList.size()][];
        elementTable = new WebElement[rowsList.size()][];
        prefix = "";
        parseTable();
    }

    public Table(String id) {
        this(BasePage.getDriver().findElement(By.id(id)));
    }

    private void parseTable() {
        BasePage.setImplicitlyWait(0);
        rowsList.parallelStream()
                .forEach(webElement -> {
                    int i = rowsList.indexOf(webElement);
                    String tagName = i == 0 ? "th" : "td";
                    List<WebElement> tdList = webElement.findElements(By.tagName(tagName));
                    elementTable[i] = tdList.toArray(new WebElement[0]);
                });
        Pattern row = Pattern.compile("<(tr).*?>(.*?)</(\\1)>");
        Pattern cell = Pattern.compile("<(t[dh]).*?>(.*?)</(\\1)>");
        String tableHtml = table.getAttribute("outerHTML")
                .replace("\t", "")
                .replace("\n", "");
        Matcher mRow = row.matcher(tableHtml);
        int i = 0;
        while (mRow.find()) {
            List<String> cellList = new ArrayList<>();
            Matcher mCell = cell.matcher(mRow.group(2));
            while (mCell.find()) {
                cellList.add(getCellContent(mCell.group(2).replace("&nbsp;", " ")));
            }
            textTable[i] = cellList.toArray(new String[0]);
            i++;
        }
        BasePage.setDefaultImplicitlyWait();
    }

    private static String getCellContent(String input) {
        String out = input;
        Pattern p = Pattern.compile("<(div|span|a).*?>(.*?)</(\\1)>");
        Matcher m = p.matcher(input);
        while (m.find()) {
            out = getCellContent(m.group(2));
        }
        if (out.startsWith("<input")) {
            return "";
        } else {
            return out;
        }
    }

    public int[] getTableSize() {
        return new int[]{textTable.length, textTable[1].length};
    }

    public Table clickOn(int row, int column, int tagNum) {
        if (tagNum == -1) {
            elementTable[row][column].click();
        } else {
            List<WebElement> tagList = elementTable[row][column].findElements(By.xpath("child::img | child::span | child::input | child::select"));
            if (tagNum == -2) {
                tagList.get(tagList.size() - 1).click();
            } else {
                tagList.get(tagNum).click();
            }
        }
        return this;
    }

    public Table clickOn(int row, int column) {
        return clickOn(row, column, -1);
    }

    public Table clickOn(String text, int column) {
        clickOn(getRowNumberByText(column, text), column);
        return this;
    }

    public Table clickOn(String text) {
        return clickOn(-1, text);
    }

    public int getColumnNumber(int row, String text) {
        String[] strings = textTable[row];
        for (int i = 0; i < strings.length; i++) {
            String cell = strings[i];
            if (cell.equals(text)) {
                return i;
            }
        }
        return -1;
    }

    public Table clickOn(int tagNum, String text) {
        for (int i = 0; i < textTable.length; i++) {
            for (int j = 0; j < textTable[i].length; j++) {
                if (textTable[i][j].toLowerCase().equals(text.toLowerCase())) {
                    return clickOn(i, j, tagNum);
                }
            }
        }
        return this;
    }

    public Table print() {
        int[] size = new int[textTable[1].length];
        for (String[] strings : textTable) {
            for (int j = 0; j < strings.length; j++) {
                int k = strings[j].length();
                if (k > size[j]) {
                    size[j] = k;
                }
            }
        }
        for (String[] strings : textTable) {
            System.out.print("| ");
            for (int j = 0; j < strings.length; j++) {
                System.out.printf("%-" + (size[j] + 1) + "s%s", strings[j], "| ");
            }
            System.out.println();
        }
        return this;
    }

    public Table pause(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

    public String[] getColumn(int column) {
        String[] out = new String[textTable.length - 1];
        for (int i = 0; i < textTable.length - 1; i++) {
            out[i] = textTable[i + 1][column];
        }
        return out;
    }

    public String[] getColumn(String column) {
        return getColumn(getColumnNumber(0, column));
    }

    public Table readTasksFromDB() {
        String groupName = BaseTestCase.getTestName();
        List<String[]> groupList;
        int count = Integer.parseInt(props.getProperty("pending_tasks_check_time"));
        for (int i = 0; i < count; i++) {
            long start = System.currentTimeMillis();
            groupList = DataBaseConnector.getTaskList(getGroupId(groupName));
            if (groupList.isEmpty()) {
                String warn = "There are no tasks created by '" + groupName + "' Group Update";
                LOGGER.warn(warn);
                throw new AssertionError(warn);
            }
            Set<String> StateSet = new HashSet<>();
            for (String[] line : groupList) {
                StateSet.add(line[2]);
            }
            if (!StateSet.contains("1")) {
                if (StateSet.size() != 1 || !StateSet.contains("2")) {
                    LOGGER.info("All tasks created. One or more tasks failed or rejected");
                }
                return this;
            }
//            System.out.println("contains pending");
            long timeout;
            if ((timeout = 1000 - System.currentTimeMillis() + start) > 0) {
                try {
                    Thread.sleep(timeout);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        LOGGER.info("All tasks created. One or more tasks remains in pending state");
        return this;
    }

    public String getExportLink(String groupName) {
        return props.getProperty("ui_url") + "/Update/Export.aspx?updateId=" + getGroupId(groupName);
    }

    private String getGroupId(String groupName) {
        WebElement cell = getCellWebElement(getRowNumberByText(4, groupName), 11);
        String attr = cell.getAttribute("onclick");
        return attr.substring(10, attr.indexOf("event)") - 2);
    }

    public String getCellText(int row, int column) {
        return textTable[row][column];
    }

    public String getCellText(int searchColumn, String searchText, int resultColumn) {
        return textTable[getRowNumberByText(searchColumn, searchText)][resultColumn];
    }

    public WebElement getCellWebElement(int row, int column) {
        return elementTable[row][column];
    }

    public Table setPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    private int getRowNumberByText(int columnNum, String text) {
        int rowNum = -1;
        String[] column = getColumn(columnNum);
        for (int i = 0; i < column.length; i++) {
            if (column[i].toLowerCase().equals(text.toLowerCase())) {
                rowNum = i + 1;
                break;
            }
        }
        if (rowNum < 0) {
            String warning = "Text '" + text + "' not found in current table";
            LOGGER.warn(warning);
            throw new AssertionError(warning);
        }
        return rowNum;
    }

    public Table checkResults() {
        printResults();
        Set<Map.Entry<String, String>> entrySet = paramSet.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            checkResults(entry.getKey(), entry.getValue());
        }
        return this;
    }

    public Table checkResults(String parameter, String value) {
        boolean match = false;
        for (String[] row : textTable) {
            try {
                int length = row.length;
                if (row[length - 2].equals(prefix + parameter) && row[length - 1].equals(value)) {
                    match = true;
                    break;
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                System.out.println(e.getMessage());
            }
        }
        if (!match) {
            String warning = "Pair '" + parameter + "' : '" + value + "' not found";
            LOGGER.warn(warning);
            throw new AssertionError(warning);
        }
        return this;
    }

    public Table getTable(String id) {
        return new Table(id);
    }

    public void getParameter(int row, int column) {
        if (paramSet == null) {
            paramSet = new HashMap<>();
        }
        String hint = elementTable[row][0].findElement(By.tagName("span")).getAttribute("hintbody");
        String values;
        if (column < 1) {
            values = "values,names,attributes";
            for (int i = 1; i < elementTable[row].length; i++) {
                clickOn(row, i, 0);
            }
        } else {
            String[] valuesArr = {"", "names", "values", "attributes"};
            values = valuesArr[column];
            clickOn(row, column, 0);
        }
        paramSet.put(hint, values);
    }

    public Table setAllParameters() {
        setParameter(0);
        return this;
    }

    public void setParameter(int amount) {
        if (paramSet == null) {
            paramSet = new HashMap<>();
        }
        BasePage.setImplicitlyWait(0);
        int counter = amount == 0 ? elementTable.length : amount + 1;
        for (int i = 1; i < counter; i++) {
            WebElement paramVal = elementTable[i][1];
            List<WebElement> selectList = paramVal.findElements(By.tagName("select"));
            if (selectList.size() == 0) {
                if (amount != 0) {
                    counter++;
                }
                continue;
            }
            String hint = elementTable[i][0].findElement(By.tagName("span")).getAttribute("hintbody");
            List<WebElement> optionList = selectList.get(0).findElements(By.tagName("option"));
            Parameter option;
            String value = "1";
            String attr = optionList.get(1).getAttribute("value");
            if (attr.equals("sendEmpty")) {
                option = Parameter.VALUE;
                String paramType = DataBaseConnector.getValueType(hint);
                switch (paramType) {
                    case "string":
                        value = "value" + i;
                        break;
                    case "int":
                    case "unsignedInt":
                        value = "" + i;
                        break;
                    case "boolean":
                        break;
                    default:
                        throw new AssertionError("Unsupported data type:" + paramType);
                }
            } else if (attr.equals("true")) {
                option = Parameter.TRUE;
            } else {
                option = Parameter.CUSTOM;
                value = attr;
            }
            setParameter(textTable[i][0], option, value);
            paramSet.put(hint, value);
        }
        BasePage.setDefaultImplicitlyWait();
//        printResults();
    }

    public void printResults() {
        Set<Map.Entry<String, String>> entrySet = paramSet.entrySet();
        for (Map.Entry<String, String> entry : entrySet) {
            System.out.println(entry.getKey() + ":" + entry.getValue());
        }
    }

    public void setAnyAdvancedParameter() {
        new Table(BasePage.getDriver().findElement(By.id("tblTree"))).setAdvancedParameter();
    }

    public void setAdvancedParameter() {
        String path = BasePage.getElementText("divPath");
        for (int i = 1; i < elementTable.length; i++) {
            clickOn(i, 0, -1);
            if (!BasePage.getElementText("divPath").equals(path)) {
                break;
            }
        }
        System.out.println("marker before");
        new Table(BasePage.getDriver().findElement(By.id("tblParamsValue"))).setParameter(1);
        System.out.println("marker after");
    }

    public Table setParameter(String paramName, Parameter option, String value) {
        int rowNum = getRowNumberByText(0, paramName);
        if (rowNum < 0) {
            throw new AssertionError("Parameter name '" + paramName + "' not found");
        }
        WebElement paramCell = getCellWebElement(rowNum, 1);
        if (props.getProperty("browser").equals("edge")) {
            BasePage.scrollToElement(paramCell);
        }
        new Select(paramCell.findElement(By.tagName("select"))).selectByValue(option != Parameter.CUSTOM ? option.option : value);
        if (value != null && option == Parameter.VALUE) {
            WebElement input = paramCell.findElement(By.tagName("input"));
            input.clear();
            input.sendKeys(value);
        }
        if (!BasePage.BROWSER.equals("edge")) {
            clickOn(0, 0);
        }
        return this;
    }

    public Table setCondition(String conditionName, Conditions condition, String value) {
        int rowNum = getRowNumberByText(0, conditionName);
        if (rowNum < 0) {
            throw new AssertionError("Condition name '" + conditionName + "' not found");
        }
        WebElement conditionCell = getCellWebElement(rowNum, 1);
        WebElement valueCell = getCellWebElement(rowNum, 2);
        if (BasePage.BROWSER.equals("edge")) {
            BasePage.scrollToElement(conditionCell);
        }
        if (condition != null) {
            new Select(conditionCell.findElement(By.tagName("select"))).selectByValue(condition.value);
        }
        if (value != null && condition != Conditions.VALUE_CHANGE) {
            valueCell.findElement(By.tagName("input")).sendKeys(value);
        }
        clickOn(0, 0);
        return this;
    }

    public void setAllPolicies() {
        if (paramSet == null) {
            paramSet = new HashMap<>();
        }
        for (int i = 1; i < elementTable.length; i++) {
            setPolicy(textTable[i][0], Policy.ACTIVE, Policy.ALL);
            String hint = elementTable[i][0].findElement(By.tagName("span")).getAttribute("hintbody");
            paramSet.put(hint, "Notification=Active Access=All");
        }
    }

    public void setPolicy(int scenario) {
        if (scenario > textTable.length + 1) {
            throw new AssertionError("Number of parameters on current tab is not enough to execute this testcase");
        }
        if (paramSet == null) {
            paramSet = new HashMap<>();
        }
        if (scenario == 1) {
            setPolicy(textTable[1][0], null, Policy.ACS_ONLY);
            String hint = elementTable[1][0].findElement(By.tagName("span")).getAttribute("hintbody");
            paramSet.put(hint, "Access=AcsOnly");
        } else if (scenario == 2) {
            for (int i = 1; i < 3; i++) {
                setPolicy(textTable[i][0], Policy.OFF, null);
                String hint = elementTable[i][0].findElement(By.tagName("span")).getAttribute("hintbody");
                paramSet.put(hint, "Notification=Off ");
            }
        } else {
            String hint1 = elementTable[1][0].findElement(By.tagName("span")).getAttribute("hintbody");
            String hint2 = elementTable[2][0].findElement(By.tagName("span")).getAttribute("hintbody");
            String hint3 = elementTable[3][0].findElement(By.tagName("span")).getAttribute("hintbody");
            setPolicy(textTable[1][0], Policy.OFF, Policy.ACS_ONLY);
            paramSet.put(hint1, "Notification=Off Access=AcsOnly");
            setPolicy(textTable[2][0], Policy.PASSIVE, Policy.ALL);
            paramSet.put(hint2, "Notification=Passive Access=All");
            setPolicy(textTable[3][0], Policy.ACTIVE, null);
            paramSet.put(hint3, "Notification=Active ");
        }
    }

    public Table setPolicy(String policyName, Policy notification, Policy accessList) {
        int rowNum = getRowNumberByText(0, policyName);
        if (rowNum < 0) {
            throw new AssertionError("Policy name '" + policyName + "' not found");
        }
        WebElement notificationCell = getCellWebElement(rowNum, 1);
        WebElement accessListCell = getCellWebElement(rowNum, 2);
        if (BasePage.BROWSER.equals("edge")) {
            BasePage.scrollToElement(notificationCell);
        }
        if (notification != null) {
            new Select(notificationCell.findElement(By.tagName("select"))).selectByValue(notification.option);
        }
        BasePage.waitForUpdate();
        if (accessList != null) {
            new Select(accessListCell.findElement(By.tagName("select"))).selectByValue(accessList.option);
        }
        BasePage.waitForUpdate();
//        clickOn(0, 0);
        return this;
    }

    public Table assertPresenceOfParameter(String value) {
        for (String[] row : textTable) {
            int length = row.length;
            if (row[length - 2].equals(value) || row[length - 2].equals(prefix + value)) {
                return this;
            }
        }
        String warning = "Specified table does not contain value '" + value + "'";
        throw new AssertionError(warning);
    }

    public Table assertAbsenceOfParameter(String value) {
        for (String[] row : textTable) {
            int length = row.length;
            if (row[length - 2].equals(value) || row[length - 2].equals(prefix + value)) {
                String warning = "Specified table contains value '" + value + "', but must NOT!";
                throw new AssertionError(warning);
            }
        }
        return this;
    }

    public Table assertPresenceOfValue(int column, String value) {
        if (getRowNumberByText(column, value) < 0) {
            throw new AssertionError("Specified column '" + column + "' does not contain value: " + value);
        }
        return this;
    }

    public Table assertAbsenceOfValue(int column, String value) {
        if (getRowNumberByText(column, value) >= 0) {
            throw new AssertionError("Specified column '" + column + "' contains value '" + value + "', but MUST NOT!");
        }
        return this;
    }

    public static void flushResult() {
        paramSet = null;
    }

    public enum Parameter {
        EMPTY_VALUE("sendEmpty"),
        VALUE("sendValue"),
        FALSE("0"),
        TRUE("1"),
        DO_NOT_SEND("notSend"),
        NULL(""),
        CUSTOM(null);

        private String option;

        Parameter(String option) {
            this.option = option;
        }
    }

    public enum Policy {
        DEFAULT("-1"),
        OFF("0"),
        PASSIVE("1"),
        ACTIVE("2"),
        ACS_ONLY("1"),
        ALL("2");

        private String option;

        Policy(String option) {
            this.option = option;
        }
    }

    public enum Conditions {
        CONTAINS(1, "5"),
        EQUAL(2, "2"),
        GREATER(3, "1"),
        GREATER_EQUAL(4, "8"),
        LESS(5, "4"),
        LESS_EQUAL(6, "3"),
        NOT_EQUAL(7, "6"),
        STARTS_WITH(8, "7"),
        VALUE_CHANGE(9, "9");

        Conditions(int index, String value) {
            this.index = index;
            this.value = value;
        }

        int index;
        String value;

        public String getValue() {
            return value;
        }
    }
}
