package steps;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import util.ConfigReader;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import drivermanager.Driverbase;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Properties;

public class AppHooks {

    private WebDriver driver;
    private ConfigReader configReader;
    public Driverbase driverbase;
    Properties prop;


    @Before(order = 0)
    public void getProperty() {
        configReader = new ConfigReader();
        prop = configReader.init_prop();
    }

    @Before(order = 1)
    public void launchBrowser() throws MalformedURLException {
        String browserName = prop.getProperty("browser");
        driverbase = new Driverbase();
        driver = driverbase.getDriver(browserName);
    }

    @Before(order = 2)
    public void createScreenShotsDirectory(Scenario scenario) {
      /*File dir = new File(System.getProperty("user.dir") + "//screenshots//" + scenario.getName());
      dir.mkdir();*/
    }

    @AfterStep
    public void screenShotCaptured(Scenario scenario) throws IOException {
        String screenshotName = scenario.getName().replaceAll(scenario.getName(), "");
        byte[] sourcePath = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        scenario.attach(sourcePath, "image/png", screenshotName);


        //Take screenshots by scenario name
       /*  TakesScreenshot scrShot = ((TakesScreenshot) driver);
        File srcFile = scrShot.getScreenshotAs(OutputType.FILE);
        String imageFileName = scenario.getName() + "-"
                + new SimpleDateFormat("MM-dd-yyyy_HH-mm").format(new Date()) + ".png";

        File destFile = new File(System.getProperty("user.dir") + "//screenshots//" + imageFileName);
        FileUtils.copyFile(srcFile, destFile);*/
    }

    @After(order = 0)
    public void quitBrowser() {
        driver.quit();
    }
}
