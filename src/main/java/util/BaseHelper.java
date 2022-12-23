package util;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.Random;
import java.util.concurrent.TimeUnit;


import io.cucumber.core.logging.LoggerFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

//import com.cucumber.listener.Reporter;
import com.google.common.io.Files;

import drivermanager.Driverbase;

//import com.staf.drivermanager.WebDriverManagerWEBDRIVER;
//import com.vimalselvam.cucumber.listener.Reporter;
//import io.cucumber.java.Scenario;
//import io.appium.java_client.windows.WindowsDriver;


/**
 * @author SandeepChandraBoseV
 * This class methods are used to Perform operations in
 *         WebApplication
 */
public class BaseHelper {

    //private static final Logger logger = Logger.getLogger(BaseHelper.class);
    private static final Logger logger = LogManager.getLogger("BaseHelper");

    private ConfigReader configReader = new ConfigReader();
    WebDriver driver;
    private Properties properties;
    //public WindowsDriver<WebElement> session;



    public Driverbase session;

    /**
     * @name : SeleniumHelper
     * @description constructor of present class
     * @param driver
     *            - instance of WebDriver
     */
//    public BaseHelper(WebDriver driver) {
//        this.driver = driver;
//        this.driver.manage().window().maximize();
//    }
//
//    public BaseHelper(WebDriver session) {
//        this.session = session;
//        this.session.manage().window().maximize();
//    }

    /**
     * @name : isEnabled
     * @description verifying element is enabled or not
     * @param element
     *            - PO
     * @return Boolean
     */
    public boolean isEnabled(WebElement element) {
        boolean temp = false;
        try {
            if (element.isEnabled()) {
                temp = true;
                return temp;
            }
        } catch (Exception e) {
            raiseException();
        }
        return temp;
    }

    /**
     * @description Clear text from the text box
     * @param element
     *
     */
    public void clear(WebElement element)
    {
        try {
            element.clear();
            logger.info(element+" is cleared");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e.getMessage());
            raiseException();
        }
    }


    /**
     * @name : getLocatorFromObjecRepo
     * @description return the locator value from Object repository
     * @param objectRepoPath
     *            - exact path of repository file
     * @param locator
     *            - name of the locator variable
     * @param values
     *            - array of values needs to replace
     * @return String
     */
    public String getLocatorFromObjecRepo(String objectRepoPath, String locator, String... values) {

        String loc = null;
        try (BufferedReader reader = new BufferedReader(new FileReader(System.getProperty("user.dir") + objectRepoPath))) {
            properties = new Properties();
            properties.load(reader);
            loc = properties.getProperty(locator);
            for (int i = 0; i < values.length; i++) {
                loc = loc.replace("parameterizedValue" + i, values[i]);
            }
            logger.info(loc);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException(
                    "Configuration.properties not found at " + System.getProperty("user.dir") + objectRepoPath);
        }catch (IOException e) {
            logger.error("IOException:: getLocatorFromObjecRepo:: " + e.getMessage());
        } catch (Exception e) {
            logger.error("Exception:: getLocatorFromObjecRepo:: " + e.getMessage());
        }
        return loc;
    }

    /**
     * @name : isExists
     * @description verifying is an element exists or not
     * @param element
     *            - PO
     * @return Boolean
     */
    public boolean isExists(WebElement element) {

        boolean eleExists = false;
        try {
            if (element.getText() != null) {
                eleExists = true;
                logger.info("element is present");
            }
        } catch (NoSuchElementException e) {
            logger.error("element is not present");
            raiseException();
        }
        return eleExists;

    }

    /**
     * @name : SelectCheckBox
     * @description Selects a check box
     * @param element
     *            - PO
     * @param strFieldName
     *            - Name of the object
     * @return - void
     */
    public void SelectCheckBox(WebElement element, String strFieldName) {
        try {
            if (this.isExists(element)) {
                if (this.isEnabled(element)) {
                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(8));

                    if (!element.isSelected()) {
                        element.click();
                        logger.info(strFieldName + " check box is selected");
                    }
                } else

                    logger.error(strFieldName + " check box is selected");
            } else

                logger.error(strFieldName + " check box is selected");
        } catch (Exception e) {
            raiseException();
        }

    }

    /**
     * @name : selectListValuebyIndex
     * @description select the element from a dropdown based on the index when
     *              element passed as WebElement
     * @param element
     *            - PO
     * @param strFieldName
     *            - name of the element to select from dropdown
     * @param index
     *            - the index of the element to select - starts with 0
     * @return - void
     */
    public void selectListValuebyIndex(WebElement element, String strFieldName, int index) {
        try {
            if (this.isExists(element)) {
                new Select(element).selectByIndex(index);
                String selectedOption = new Select(element).getFirstSelectedOption().getText();
                if (selectedOption != null)
                    logger.info(strFieldName + "  is selected");
                else
                    logger.error(strFieldName + "  is not selected");
            }
        } catch (Exception e) {
            logger.error(strFieldName + "  is not selected");
            raiseException();
        }
    }

    /**
     * @name : selectListValuebyIndex
     * @description select the element from a dropdown based on the index when
     *              element passed as String
     * @param element
     *            - String
     * @param strFieldName
     *            - name of the element to select from dropdown
     * @param index
     *            - the index of the element to select - starts with 0
     * @return - void
     */
    public void selectListValuebyIndex(String element, String strFieldName, int index) {

        WebElement tempEle = getElement(element);
        try {
            if (this.isExists(tempEle)) {
                new Select(tempEle).selectByIndex(index);
                String selectedOption = new Select(tempEle).getFirstSelectedOption().getText();
                if (selectedOption != null)
                    logger.info(strFieldName + "  is selected");
                else
                    logger.error(strFieldName + "  is not selected");
            }
        } catch (Exception e) {
            logger.error(strFieldName + "  is not selected");
            raiseException();
        }
    }

    /**
     * @name : selectListValuebyVisbileText
     * @description select the element from a dropdown based on the visible text
     *              when element passed as WebElement
     * @param element
     *            - PO
     * @param strFieldName
     *            - visible text of the element to select from dropdown
     * @param description
     *            - text to log information
     * @return - void
     */
    public void selectListValuebyVisbileText(WebElement element, String strFieldName, String description) {
        String selectedOption = null;
        try {
            if (this.isExists(element)) {
                new Select(element).selectByVisibleText(strFieldName);
                waitForPageLoad();
                selectedOption = new Select(element).getFirstSelectedOption().getText();
                if (selectedOption != null)
                    logger.info(strFieldName + "  is selected");
                else
                    logger.error(strFieldName + "  is not selected");
            }
        } catch (Exception e) {
            logger.error(strFieldName + "  is not selected");
            raiseException();

        }
    }

    /**
     * @name : selectListValuebyVisbileText
     * @description select the element from a dropdown based on the visible text
     *              when element passed as String
     * @param element
     *            - PO
     * @param strFieldName
     *            - visible text of the element to select from dropdown
     * @param description
     *            - text to log information
     * @return - void
     */
    public void selectListValuebyVisbileText(String element, String strFieldName, String description) {
        WebElement tempEle = getElement(element);
        String selectedOption = null;
        try {
            if (this.isExists(tempEle)) {
                new Select(tempEle).selectByVisibleText(strFieldName);
                waitForPageLoad();
                selectedOption = new Select(tempEle).getFirstSelectedOption().getText();
                if (selectedOption != null)
                    logger.info(strFieldName + "  is selected");
                else
                    logger.error(strFieldName + "  is not selected");
            }
        } catch (Exception e) {
            logger.error(strFieldName + "  is not selected");
            raiseException();

        }
    }

    /**
     * @name : selectListValuebyValue
     * @description select the element from a dropdown based on the value of the
     *              element when an element passed as WebElement
     * @param element
     *            - PO
     * @param strFieldName
     *            - name of the element value
     * @return - void
     */
    public void selectListValuebyValue(WebElement element, String strFieldName) {
        String selectedOption = null;
        try {
            if (this.isExists(element)) {
                new Select(element).selectByValue(strFieldName);
                selectedOption = new Select(element).getFirstSelectedOption().getText();
                if (selectedOption != null)
                    logger.info(strFieldName + "  is selected");
                else
                    logger.error(strFieldName + "  is not selected");
            }
        } catch (Exception e) {

            logger.error(strFieldName + "  is not selected");
            raiseException();
        }
    }

    /**
     * @name : selectListValuebyValue
     * @description select the element from a dropdown based on the value of the
     *              element when an element passed as String
     * @param element
     *            - PO
     * @param strFieldName
     *            - name of the element value
     * @return - void
     */
    public void selectListValuebyValue(String element, String strFieldName) {
        String selectedOption = null;
        /* WebElement tempEle=getElement(element); */
        try {
            if (this.isExists(driver.findElement(By.xpath(element)))) {
                new Select(driver.findElement(By.xpath(element))).selectByValue(strFieldName);
                // waitForPageLoad();
                selectedOption = new Select(driver.findElement(By.xpath(element))).getFirstSelectedOption().getText();
                if (selectedOption != null)
                    logger.info(strFieldName + "  is selected");
                else
                    logger.error(strFieldName + "  is not selected");
            }
        } catch (Exception e) {

            logger.error(strFieldName + "  is not selected");
            raiseException();
        }
    }

    /**
     * @name : setValueTextBox
     * @description enter the text into a text box when an element passed as
     *              WebElement
     * @param element
     *            - PO
     * @param data
     *            - text to enter
     * @param strFieldName
     *            - name of the text box
     * @return - void
     */
    public void setValueTextBox(WebElement element, String data, String strFieldName) {
        try {
            if (this.isExists(element)) {// System.out.println("Before enter data");
                if (this.isEnabled(element)) {// System.out.println("Before enter data");
                    element.click();
                    element.clear();
                    element.clear();
                    driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(8));

                    element.click();
                    element.clear();
                    element.clear();
                    element.sendKeys(data);
                    logger.error(data + "  is entered");
                } else

                    System.out.println();
                logger.error(data + "  is not entered");

            } else
                logger.error(data + "  is not entered");
            System.out.println();
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        } catch (Exception e) {
            raiseException();
        }
    }

    /**
     * @name : switchToFirstBrowser
     * @description the driver switches to first opened browser. Works for any
     *              browser
     * @return void
     */

    public void switchToFirstBrowser() {

        try {
            ArrayList<String> getAllWindows = new ArrayList<String>(driver.getWindowHandles());
            driver.switchTo().window(getAllWindows.get(0));
            driver.manage().window().maximize();
        } catch (Exception e) {
            raiseException();
        }
    }

    /**
     * @name : switchToLatestBrowser
     * @description the driver switches to first opened browser. Works for any
     *              browser
     * @return void
     */
    public void switchToLatestBrowser() {

        try {
            ArrayList<String> getAllWindows = new ArrayList<String>(driver.getWindowHandles());
            int maxWindowSize = driver.getWindowHandles().size();
            driver.switchTo().window(getAllWindows.get(maxWindowSize - 1));
            driver.manage().window().maximize();
        } catch (Exception e) {
            raiseException();
        }
    }

    /**
     * @name : verifyElementExists
     * @description verifies if an element is visible in the application. If the
     *              element is not present, will return FAILED in the reports
     * @param elementValue
     *            - PO
     * @param elementName
     *            - name of the element
     * @return Boolean
     */
    public boolean verifyElementExists(WebElement elementValue, String elementName) {
        boolean isElementExists = false;
        try {
            if (this.isExists(elementValue)) {

                logger.info("element exists");
                isElementExists = true;
            } else {
                logger.error("element does not exists");
            }
        } catch (NoSuchElementException e) {
            logger.error("element does not exists");
            raiseException();
        }
        return isElementExists;
    }

    /**
     * @name : mouseOverObject
     * @description hover the mouse on the element when an element passed as
     *              WebElement
     * @MouseOver on element
     * @param element
     *            - PO
     * @param strFieldName
     *            -
     * @return void
     */
    public void mouseOverObject(WebElement element, String strFieldName) {
        try {
            if (this.isExists(element)) {
                if (this.isEnabled(element)) {

                    waitForPageLoad();
                    Actions action = new Actions(driver);
                    action.moveToElement(element).build().perform();
                    logger.info("mouse hover is performed");

                } else
                    logger.error("mouse hover is not performed");
            } else
                logger.error("mouse hover is not performed");
        } catch (Exception e) {
            logger.error("mouse hover is not performed");
            raiseException();
        }

    }

    /**
     * @name : mouseOverObject
     * @MouseOver on an element when an element passed as String
     * @param element
     *            - PO
     * @param strFieldName
     *            -
     * @return void
     */
    public void mouseOverObject(String element, String strFieldName) {
        WebElement ele = getElement(element);
        try {
            if (this.isExists(ele)) {
                if (this.isEnabled(ele)) {

                    waitForPageLoad();
                    Actions action = new Actions(driver);
                    action.moveToElement(ele).build().perform();
                    logger.info("mouse hover is performed");

                } else
                    logger.error("mouse hover is not performed");
            } else
                // log
                logger.error("mouse hover is not performed");
        } catch (Exception e) {
            logger.error("mouse hover is not performed");
            raiseException();
        }

    }


    /**
     * @name : convertOneDateFormatToOtherFormat
     * @description convert a particular date format to required date format
     * @param dateUI
     *            - value of the date mentioned in initialFormat
     * @param initialFormat
     *            - type of format by passing date
     * @param targetFormat
     *            - type of format required for a date
     * @return String
     * @throws ParseException
     *             when fails to parse a String that is ought to have a special
     *             format
     */
    public String convertOneDateFormatToOtherFormat(String dateUI, String initialFormat, String targetFormat)
            throws ParseException {

        String returnValue = null;
        try {
            SimpleDateFormat formatter = new SimpleDateFormat(initialFormat);
            Date date = formatter.parse(dateUI);
            System.out.println(date);
            System.out.println(formatter.format(date));
            SimpleDateFormat formatter1 = new SimpleDateFormat(targetFormat);
            System.out.println(formatter1.format(date));
            returnValue = formatter1.format(date);

        } catch (Exception e) {
            raiseException();
        }
        return returnValue;

    }

    /**
     * @name : getNumberOfDays
     * @description return the number of days in between two dates
     * @param fromDate
     *            - value of date, when to start counting number of days
     * @param toDate
     *            - value of date, option when to stop
     * @return Long
     */
    public long getNumberOfDays(String fromDate, String toDate) {
        SimpleDateFormat myFormat = new SimpleDateFormat("MM/dd/yyyy");
        String todayDate = fromDate;
        if (todayDate.equals("")) {
            Date date = new Date();
            todayDate = (myFormat.format(date));
        }
        long diff = 0;
        try {
            Date date1 = myFormat.parse(todayDate);
            Date date2 = myFormat.parse(toDate);
            diff = date2.getTime() - date1.getTime();
            System.out.println("Days: " + TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS));
            return TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            e.printStackTrace();
            raiseException();
        }
        return diff;
    }

    /**
     * @name : currentTime
     * @description return the current time
     * @return String
     */
    public String currentTime() {

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String currTime = sdf.format(cal.getTime());
        return currTime;
    }

    /**
     * @name : getElementAttribute
     * @description return the value of attribute
     * @param element
     *            - PO
     * @param attributeName
     *            - name of the attribute
     * @return String
     */
    public String getElementAttribute(WebElement element, String attributeName) {

        String txtValue = "";
        try {
            if (this.isExists(element)) {
                txtValue = element.getAttribute(attributeName);

            } else
                return null;

        } catch (Exception e) {
            raiseException();
        }
        return txtValue;
    }

    /**
     * @name : sortArrayList
     * @description sort the elements in the ArrayList and return the sorted
     *              ArrayList
     * @param UIRetValues
     *            - the object of ArrayList which holds a group of elements
     * @return ArrayList
     */
    public ArrayList<String> sortArrayList(ArrayList<String> UIRetValues) {

        try {
            Collections.sort(UIRetValues);
            return UIRetValues;

        } catch (Exception e) {
            raiseException();
        }
        return UIRetValues;
    }

    /**
     * @name : getElement
     * @description return the WebElement
     * @param strElement
     *            - type of locator and value of locator separated by ";"
     * @return WebElement
     */
    protected WebElement getElement(String strElement) {
        WebElement tempWebElement = null;
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(configReader.init_prop().getProperty("webdriverwait"))));
        try {
            logger.info("Into getElement and working on : " + strElement);

            String tempElementPrefix, tempElementId;
            String[] tempElement;

            tempElement = strElement.split(";");
            tempElementPrefix = tempElement[0];
            tempElementId = tempElement[1];

            if (tempElementPrefix.toLowerCase().trim().startsWith("xpath")) {
                tempWebElement = wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath(tempElementId))));
            } else if (tempElementPrefix.toLowerCase().trim().startsWith("id")) {
                tempWebElement = wait.until(ExpectedConditions.presenceOfElementLocated((By.id(tempElementId))));
            } else if (tempElementPrefix.toLowerCase().trim().startsWith("link")) {
                tempWebElement = wait.until(ExpectedConditions.presenceOfElementLocated((By.linkText(tempElementId))));
            } else if (tempElementPrefix.toLowerCase().trim().startsWith("css")) {
                tempWebElement = wait
                        .until(ExpectedConditions.presenceOfElementLocated((By.cssSelector(tempElementId))));
            } else if (tempElementPrefix.toLowerCase().trim().startsWith("name")) {
                tempWebElement = wait.until(ExpectedConditions.presenceOfElementLocated((By.name(tempElementId))));
            } else if (tempElementPrefix.toLowerCase().trim().startsWith("tag")) {
                tempWebElement = wait.until(ExpectedConditions.presenceOfElementLocated((By.tagName(tempElementId))));
            } else if (tempElementPrefix.toLowerCase().trim().startsWith("class")) {
                tempWebElement = wait.until(ExpectedConditions.presenceOfElementLocated((By.className(tempElementId))));
            }
            if (tempWebElement != null) {
                logger.info("Into getElement WebElement returned is : " + tempWebElement.toString());
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            raiseException();
        }
        return tempWebElement;

    }

    /**
     * @name : enterText
     * @description enter text into the element
     * @param element
     *            - PO
     * @param strText
     *            - text to enter
     * @param description
     *            -
     * @return void
     */
    protected void enterText(WebElement element, String strText, String description) {
        logger.info("Into enterText by using input text " + strText + " and locator element :" + element.toString());
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(configReader.init_prop().getProperty("webdriverwait"))));

            if (isExists(element)) {
                if (isEnabled(element)) {
                    wait.until(ExpectedConditions.elementToBeClickable(element));
                    element.click();
                    element.clear();
                    element.sendKeys(strText);
                    logger.info("Enter text: " + strText);
                }
            }
        } catch (Exception e) {
            logger.error("Exception generated while working with enterText by using input text " + strText
                    + " and locator element :" + element.toString());
            raiseException();
        }
    }

    // ################################################################################

    /**
     * @name : click
     * @description click on an element when an element passed as String
     * @param element
     *            - PO
     * @param description
     *            - text to log information
     * @return void
     */
    protected void click(String element, String description) {
        logger.info("Into click by using locatior :" + element.toString());
        WebElement tempWebElement = null;
        try {
            tempWebElement = getElement(element);
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(configReader.init_prop().getProperty("webdriverwait"))));

            if (isExists(tempWebElement)) {
                wait.until(ExpectedConditions.elementToBeClickable(tempWebElement));

                tempWebElement.click();
                logger.info(description + "is clicked");
            } else {
                logger.info(description + "is NOT clicked");
            }
        } catch (Exception e) {
            logger.error("Exception generated while working with Click and element " + element.toString());
            // System.out.println(e.getMessage());
            // e.printStackTrace();
            raiseException();
        }
    }

    /**
     * @name : click
     * @description click on an element when an element passed as WebElement
     * @param element
     *            - PO
     * @param description
     *            - text to log information
     */
    protected void click(WebElement element, String description) {
        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(configReader.init_prop().getProperty("webdriverwait"))));

            if (isExists(element)) {
                wait.until(ExpectedConditions.elementToBeClickable(element));
                element.click();
                logger.info(description + " is clicked");
            } else {
                logger.info(description + " is NOT clicked");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            raiseException();
        }
    }

    // ################################################################################

    /**
     * @name : getText
     * @description return the text form element when an element passed as String
     * @param element
     *            - PO
     * @param description
     *            - text to log information
     * @return String
     */
    protected String getText( String element, String description) {
        WebElement temEle;
        String text = "";
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(configReader.init_prop().getProperty("webdriverwait"))));
        try {
            temEle = getElement(element);
            if (isExists(temEle)) {
                wait.until(ExpectedConditions.visibilityOf(temEle));
                text = temEle.getText();
            } else {
                logger.info(description + " is not retrieved");
            }
        } catch (Exception e) {
            logger.info("Element is not displayed");
            raiseException();
        }

        return text;
    }

    /**
     * @name : pause
     * @description providing time delay explicitly
     * @return void
     */
    protected void pause() {

        try {
            Thread.sleep(Long.parseLong(configReader.init_prop().getProperty("pause")));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            raiseException();
        }
    }

    /**
     * @name : pageLoadPause
     * @description providing time delay until page is loaded
     * @return void
     */
    protected void pageLoadPause() {

        try {
            Thread.sleep(Long.parseLong(configReader.init_prop().getProperty("pageLoadPause")));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            raiseException();
        }
    }

    // ################################################################################
    /**
     * @name : isElementVisible
     * @description verifies the element is visible or not when element passed as
     *              String
     * @param element
     *            - PO
     * @param description
     *            - text to log information
     * @return Boolean
     */
    protected boolean isElementVisible(String element, String description) {
        WebElement tempEle = getElement(element);
        Boolean eleStat = false;
        try {

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(configReader.init_prop().getProperty("webdriverwait"))));
            if (isExists(tempEle)) {
                wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(element))));
                if (tempEle.isDisplayed()) {
                    logger.info(description + " is displayed");
                    eleStat = true;
                } else {
                    logger.info(description + " is not displayed");
                    eleStat = false;
                }
            }

        } catch (Exception e) {
            logger.error(description + " is not displayed");
            raiseException();

        }
        return eleStat;

    }

    // ################################################################################
    /**
     * @name : isElementVisibleAs
     * @description verifies the element is visible or not when element passed as
     *              WebElement
     * @param element
     *            - PO
     * @param description
     *            - text to log information
     * @return Boolean
     */
    protected boolean isElementVisibleAs(WebElement element, String description) {

        Boolean eleStat = false;
        try {

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(configReader.init_prop().getProperty("webdriverwait"))));

            if (isExists(element)) {
                wait.until(ExpectedConditions.visibilityOf((element)));
                if (element.isDisplayed()) {
                    logger.info(description + " is displayed");
                    eleStat = true;
                } else {
                    logger.info(description + " is not displayed");
                    eleStat = false;
                }
            }

        } catch (Exception e) {
            logger.error(description + " is not displayed");
            raiseException();

        }
        return eleStat;

    }

    /**
     * @name : isElementVisible
     * @description verifies the element is visible or not when element passed as
     *              WebElement
     * @param element
     *            - PO
     * @param description
     *            - text to log information
     * @return void
     */
    public void isElementVisible(WebElement element, String description) {

        try {
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(configReader.init_prop().getProperty("webdriverwait"))));
            wait.until(ExpectedConditions.visibilityOf(element));

            if (isExists((element))) {
                if (element.isDisplayed()) {
                    logger.info(description + " is visible");
                } else {
                    logger.info(description + " is not visible");
                    raiseException();
                }
            }
        } catch (Exception e) {
            raiseException();
        }

    }

    /**
     * @name : raiseException
     * @description send the fail condition when exception raised
     * @return void
     */
    protected void raiseException() {

        takeScreenShot();
        //***IMP***
        //cleanup();
        Assert.assertTrue(false);
    }

    /**
     * @name : raiseException
     * @description send the fail condition when exception raised
     * @param assertionMsg
     *            -
     */
    protected void raiseException(String assertionMsg) {

        takeScreenShot();
        Assert.assertTrue(assertionMsg, false);

    }

    /**
     * @name : takeScreenShot
     * @description taking the screenshot and stick it in the report
     * @return void
     */
    protected void takeScreenShot() {
        try {
            Random rand = SecureRandom.getInstanceStrong();
            // This takes a screenshot from the driver at save it to the specified location
            String screenshotName = "screenshotName";
            File sourcePath = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            // Building up the destination path for the screenshot to save
            // Also make sure to create a folder 'screenshots' with in the cucumber-report
            // folder
            logger.info("target screenshot : " + (System.getProperty("user.dir")
                    + "/target/cucumber-reports/screenshots/" + screenshotName + "_" + rand.nextInt(10000) + ".png"));

            File destinationPath = new File(System.getProperty("user.dir") + "/target/cucumber-reports/screenshots/"
                    + screenshotName + "_" + rand.nextInt(10000) + ".png");

            destinationPath.getParentFile().mkdirs();
            destinationPath.createNewFile();
            // Copy taken screenshot from source location to destination location
            Files.copy(sourcePath, destinationPath);
            logger.info("before reporter screenshot : " + destinationPath + "," + sourcePath);
            // This attach the specified screenshot to the test
           //***IMP*** uncomment below line
            // Reporter.addScreenCaptureFromPath(destinationPath.toString());

        } catch (NoSuchAlgorithmException e) {
            logger.error("NoSuchAlgorithmException:: "+e.getMessage());
        }catch (IOException e) {
            logger.error("IOException:: "+e.getMessage());
        }catch (Exception e) {
            logger.error("Exception:: "+e.getMessage());
        }

    }

    /**
     * @name : waitForPageLoad
     * @description waiting for until page is loaded
     * @return void
     */
    public void waitForPageLoad() {

        ExpectedCondition<Boolean> pageLoadCondition = new ExpectedCondition<Boolean>() {
            public Boolean apply(WebDriver driver) {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").equals("complete");
            }
        };
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        wait.until(pageLoadCondition);
        try {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
            pause();
            System.out.println();
        } catch (Exception e) {

        }
    }

    /**
     * @name : getCurrentPageURL
     * @description return the current page URL
     * @return String
     */
    public String getCurrentPageURL() {
        String url = driver.getCurrentUrl();
        return url;
    }

    /**
     * @name : cleanup
     * @description close the driver
     * @return void
     */
    /*protected void cleanup() {
        WebDriverManagerWEBDRIVER.closeDriver();
        ;
    }*/

    /**
     * @name : getDriver
     * @description open the driver
     * @return void
     */
   /* protected WebDriver getDriver() {
        return WebDriverManagerWEBDRIVER.getDriver();
    }*/



    /**
     * @name : browseFile
     * @description sending the file path to the element need to browse
     * @param element
     *            -PO
     * @param filepath
     *            - the exact path of the file needs to browse
     * @return void
     */
    protected void browseFile(WebElement element, String filepath) {

        try {
            element.sendKeys(System.getProperty("user.dir") + filepath);
        } catch (Exception e) {
            logger.error("File not found " + filepath);
            raiseException();
        }
    }

    /**
     * @name : isElementPresent
     * @description verifies element is present or not
     * @param by
     *            - name of By class Object which represent the element
     * @return Boolean
     */
    public Boolean isElementPresent(By by) {
        Boolean isElementPresent = false;
        try {
            waitForPageLoad();
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(400));
            isElementPresent = driver.findElements(by).size() != 0;
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            return isElementPresent;
        } catch (NoSuchElementException e) {
            raiseException();

        }
        return isElementPresent;
    }

    /**
     * @name : isElementPresent
     * @description verifies element is present or not
     * @param by
     *            - name of By class Object
     * @param description
     *            - text to log information
     * @return void
     */
    public void isElementPresent(By by, String description) {
        Boolean isElementPresent = false;
        try {
            waitForPageLoad();
            driver.manage().timeouts().implicitlyWait(Duration.ofMillis(400));
            isElementPresent = driver.findElements(by).size() != 0;
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            if (isElementPresent) {
                logger.info(description + " is displayed");
            } else {
                logger.error(description + " is not displayed");
            }

        } catch (NoSuchElementException e) {
            logger.error(description + " is not displayed");
            raiseException();
        }
    }

    /**
     * @name scrollIntoView
     * @description scroll the page upto visibility of element when element passed
     *              as webElement
     * @param element
     *            - PO
     * @param description-
     *            text to log information
     * @return void
     */
    public void scrollIntoView(WebElement element, String description) {
        try {
            if (this.isExists(element)) {
                if (this.isEnabled(element)) {
                    waitForPageLoad();
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
                    logger.info("scroll is performed");
                } else
                    logger.error("scroll is not performed " + description);
            } else
                logger.error("scroll is not performed " + description);
        } catch (Exception e) {
            logger.error("mouse hover is not performed " + description);
            raiseException();
        }
    }

    /**
     * @name scrollIntoView
     * @description scroll the page upto visibility of element when element passed
     *              as String
     * @param element
     *            - PO
     * @param description-
     *            text to log information
     * @return void
     */
    public void scrollIntoView(String element, String description) {
        WebElement ele = getElement(element);
        try {
            if (this.isExists(ele)) {
                if (this.isEnabled(ele)) {
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", ele);
                    logger.info("scroll is performed on " + description);
                } else
                    logger.error("scroll is not performed on " + description);
            } else
                logger.error("scroll is not performed " + description);
            // waitForPageLoad();
        } catch (Exception e) {
            logger.error("mouse hover is not performed " + description);
            raiseException();
        }
    }

    /**
     * @name scrollUp
     * @description scroll the page up
     * @param driver
     *            - instance of WebDriver
     * @return void
     */
    public static void scrollUp(WebDriver driver) {

        JavascriptExecutor js = ((JavascriptExecutor) driver);
        js.executeScript("scroll(0, -1500);");
    }

    /**
     * @name scrollDown
     * @description scroll the page down
     * @param driver
     *            - instance of WebDriver
     * @return void
     */
    public static void scrollDown(WebDriver driver) {

        JavascriptExecutor js = ((JavascriptExecutor) driver);
        js.executeScript("scroll(1500, 0);");
    }

    /**
     * @name scrollIntoView
     * @description scroll the page until visibility of the element
     * @param element
     *            - PO
     * @param driver
     *            - instance of WebDriver
     * @return void
     */
    public void scrollIntoView(WebElement element, WebDriver driver) {
        try {
            JavascriptExecutor js = ((JavascriptExecutor) driver);
            js.executeScript("arguments[0].scrollIntoView(true);", element);
        } catch (Exception e) {
            raiseException();
        }

    }

    /**
     * @name clickWithJavaScriptExecutor
     * @description click an elements using java script executor
     * @param element
     *            - PO
     * @param description
     *            - text to log information
     * @return void
     */
    public void clickWithJavaScriptExecutor(WebElement element, String description) {
        try {

            if (isExists(element)) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click()", element);
                logger.info(description + " is clicked");
            } else {
                logger.info(description + " is NOT clicked");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            raiseException();
        }

    }

    /**
     * @name uploadFile
     * @description upload a single file using robot class
     * @param element
     *            - PO
     * @param path
     *            - path of the file to upload
     * @param description
     *            - text to log information
     * @return void
     */
    public void uploadFile(WebElement element, String path, String description) {
        // clickWithJavaScriptExecutor(element, description);
        Robot robot = null;
        try {
            StringSelection stringSelection = new StringSelection(System.getProperty("user.dir") + path);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
            robot = new Robot();
            robot.delay(5000);
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.delay(5000);
        } catch (AWTException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            raiseException();
        }
    }

    /**
     * @name uploadFile
     * @description upload a single file using robot class
     * @param path
     *            - path of the file to upload
     * @param description
     *            - text to log information
     * @return void
     */
    public void uploadFile( String path, String description) {

        Robot robot = null;
        try {
            StringSelection stringSelection = new StringSelection(System.getProperty("user.dir") + path);
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(stringSelection, null);
            robot = new Robot();
            robot.delay(5000);
            robot.keyPress(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_V);
            robot.keyRelease(KeyEvent.VK_CONTROL);
            robot.keyPress(KeyEvent.VK_TAB);
            robot.keyRelease(KeyEvent.VK_TAB);
            robot.keyPress(KeyEvent.VK_DOWN);
            robot.keyRelease(KeyEvent.VK_DOWN);
            robot.keyPress(KeyEvent.VK_DOWN);
            robot.keyRelease(KeyEvent.VK_DOWN);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.keyPress(KeyEvent.VK_ENTER);
            robot.keyRelease(KeyEvent.VK_ENTER);
            robot.delay(5000);
        } catch (AWTException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            raiseException();
        }
    }

    /**
     * @name : selectListOfValuebyIndex
     * @description select the element from a dropdown based on the index when
     *              element passed as WebElement
     * @param ele      - PO
     * @param element      - PO
     * @param strFieldName - name of the element to select from dropdown
     * @param index        - the index of the element to select - starts with 0
     * @return - void
     */
    public void selectListOfValuebyIndex(List<WebElement> element, WebElement ele, int index, String strFieldName) {
        try {
            for (int i = 0; i < element.size(); i++) {
                element.get(i).click();
                if (this.isExists((WebElement) ele)) {
                    Select sel = new Select(element.get(i));
                    pause();
                    sel.selectByIndex(index);
                    String selectedOption = sel.getFirstSelectedOption().getText();
                    if (selectedOption != null)
                        logger.info(strFieldName + "  is selected");
                    else
                        logger.error(strFieldName + "  is not selected");
                }
            }
        } catch (Exception e) {
            logger.error(strFieldName + "  is not selected");
            raiseException();
        }
    }

    /**
     * @name assertData
     * @description Compare actual and expected strings
     * @param expectedVal
     *            - Expected string value
     * @param actualVal
     *            - Actual string value
     * @return void
     */
    public void assertData(String expectedVal, String actualVal) {
        try {
            Assert.assertEquals(expectedVal, actualVal);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            raiseException();
        }
    }


    /**
     * @name : ImagetakeScreenShot
     * @description taking the screenshot and stick it in the report
     * @return void
     */
    public void imageScreenShot(String imageName) {
        try {
            //String imageName = "imageName";
            File sourcePath = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            //System.out.println("target screenshot : " + System.getProperty("user.dir") + PropertyFileLoader.getConfigInstance().getConfigValue("actualpath")+ imageName +".png");

            File destinationPath = new File(System.getProperty("user.dir") + configReader.init_prop().getProperty("actualpath")+ imageName +".png");
            destinationPath.getParentFile().mkdirs();
            destinationPath.createNewFile();
            Files.copy(sourcePath, destinationPath);
            //System.out.println("before reporter screenshot : " + destinationPath + "," + sourcePath);
            //***IMP*** uncomment below line
            //Reporter.addScreenCaptureFromPath(destinationPath.toString());

        } catch (IOException e) {
            System.out.println("Into Imparision Result");
        }

    }

    /**
     * @name :Validate file
     * @description dowloaded file is validated
     * @return void
     */

    public void validateFile(String downloadPath, String fileName)
    {
        File dir= new File(downloadPath);
        File[] dirContents= dir.listFiles();
        for(int i=0;i<dirContents.length;i++)
        {
            if(dirContents[i].getName().equalsIgnoreCase(fileName))
            {
                dirContents[i].delete();
                Assert.assertTrue(true);

            }
        }
    }

    /**
     * @name : getText
     * @description return the text form element when an element passed as WebElement
     * @param element
     *            - PO
     * @param description
     *            - text to log information
     * @return String
     */
    protected String getText(WebElement element, String description) {
        String text = "";
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(Long.parseLong(configReader.init_prop().getProperty("webdriverwait"))));
        try {
            if (isExists(element)) {
                wait.until(ExpectedConditions.visibilityOf(element));
                text = element.getText();
                return text;
            } else {
                logger.info(description + " is not retrieved");
            }
        } catch (Exception e) {
            logger.info("Element is not displayed");
            raiseException();
        }

        return text;
    }

    /**
     * @name : Click On EachElement in List
     * @description Click On Each Element in List
     * @return void
     */
    public void clickOnEachElementinList(List<WebElement> element, String description) {
        try {
            WebDriverWait wait = new WebDriverWait(driver,
                    Duration.ofSeconds(Long.parseLong(configReader.init_prop().getProperty("webdriverwait"))));

            for (int i = 0; i < element.size(); i++) {
                wait.until(ExpectedConditions.elementToBeClickable(element.get(i))).click();
                logger.info(description + " is clicked");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            raiseException();
        }
    }

    /**
     * @name : Click On Specific Element in List By Number
     * @description Click On Specific  Element in List By Number
     * @return void
     */
    public void clickOnSpecificElementinListByNumber(List<WebElement> element, Integer ElementNumber) {
        try {
            WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(Long.parseLong(configReader.init_prop().getProperty("webdriverwait"))));
            wait.until(ExpectedConditions.elementToBeClickable(element.get(ElementNumber))).click();
            logger.info(ElementNumber + " is clicked");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            raiseException();
        }
    }

    /**
     * @name : Click On Specific Element in List By Text
     * @description Click On Specific  Element in List By Text
     * @return void
     */
    public void clickOnSpecificElementinListByText(List<WebElement> element, String Text)
    {
        try {
            WebDriverWait wait = new WebDriverWait(driver,Duration.ofSeconds(Long.parseLong(configReader.init_prop().getProperty("webdriverwait"))));
            for(int i=0;i<element.size();i++){
                String Elementtext=wait.until(ExpectedConditions.visibilityOf(element.get(i))).getText();
                if(Elementtext.equalsIgnoreCase(Text)){
                    wait.until(ExpectedConditions.elementToBeClickable(element.get(i))).click();
                    logger.info(Text + " is clicked");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            raiseException();
        }
    }




    /**
     * @name : clickOnList
     * @description It iterate every element when it equalsIgnoreCase true
     * @return void
     */

    public void clickOnList(List<WebElement> element, String text, String description) {
        try {

            WebDriverWait wait = new WebDriverWait(driver,
                    Duration.ofSeconds(Long.parseLong(configReader.init_prop().getProperty("webdriverwait"))));

            for (int i = 0; i < element.size(); i++) {
                if (element.get(i).getText().equalsIgnoreCase(text))
                    element.get(i).click();
                logger.info(description + " is clicked");

            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            raiseException();
        }

    }

    /**
     * @name : clickOnList
     * @description click on an element when an element passed as WebElement
     * @param element
     *            - PO
     * @param description
     *            - text to log information
     */
    public void clickOnList1(List<WebElement> element, String text, String description) {
        try {
            WebDriverWait wait = new WebDriverWait(driver,
                    Duration.ofSeconds(Long.parseLong(configReader.init_prop().getProperty("webdriverwait"))));
            for (int i = 0; i < element.size(); i++) {
                if (element.get(i).getText().equalsIgnoreCase(text)) ;
                element.get(i).click();
                logger.info(description + " is clicked");
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            raiseException();
        }
    }

    /**
     * @name : selectListValuebyIndex
     * @description select the element from a dropdown based on the index when
     *              element passed as WebElement
     * @param element      - PO
     * @param strFieldName - name of the element to select from dropdown
     * @param ele        - the index of the element to select - starts with 0
     * @return
     * @return - void
     */
    public void selectListValueinDropDown(List<WebElement> element, WebElement ele, String strFieldName) {
        try {
            for (int i = 0; i < element.size(); i++) {
                element.get(i).click();
                if (this.isExists((WebElement) ele)) {
                    Select sel = new Select(element.get(i));
                    pause();
                    sel.selectByIndex(i + 1);
                    String selectedOption = sel.getFirstSelectedOption().getText();
                    if (selectedOption != null)
                        logger.info(strFieldName + "  is selected");
                    else
                        logger.error(strFieldName + "  is not selected");

                }
            }

        } catch (Exception e) {
            logger.error(strFieldName + "  is not selected");
            raiseException();
        }
    }

    public void isFileDownloaded(String downloadPath, String fileName, String description) {
        try {
            File dir = new File(downloadPath);
            File[] dirContents = dir.listFiles();
            for (int i = 0; i < dirContents.length; i++) {
                if (dirContents[i].getName().equalsIgnoreCase(fileName)) {
                    dirContents[i].delete();
                    Assert.assertTrue(true);
                    logger.info(description + " is Deleted");
                    break;
                }
            }
            logger.info(description + " is Not Deleted");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            raiseException();
        }


    }


    /**
     * @name : reloadpage
     * @description page is loaded
     *
     */
    public void reloadPage() {
        Robot robot = null;
        try {
            robot = new Robot();
            robot.delay(2000);
            robot.keyPress(KeyEvent.VK_F5);
            robot.keyRelease(KeyEvent.VK_F5);
            robot.delay(2000);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            raiseException();
        }

    }



    /**
     * @name : clickOnListSecondElement
     * @description click on an element when an element passed as WebElement
     * @param element
     *            - PO
     * @param description
     *            - text to log information
     */
    public void clickOnListSecondElement(List<WebElement> textElement, List<WebElement> element, String text, String description) {
        try {
            WebDriverWait wait = new WebDriverWait(driver,
                    Duration.ofSeconds(Long.parseLong(configReader.init_prop().getProperty("webdriverwait"))));
            for (int i = 0; i < textElement.size(); i++) {
                if (textElement.get(i).getText().equalsIgnoreCase(text)) {
                    element.get(i).click();
                    logger.info(description + " is clicked");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            raiseException();
        }
    }


}