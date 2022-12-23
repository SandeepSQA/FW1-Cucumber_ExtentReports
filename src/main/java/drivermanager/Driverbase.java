package drivermanager;


import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.util.HashMap;

public class Driverbase {

    public static WebDriver driver;

    public WebDriver getDriver(String browser) {
        ChromeOptions options = new ChromeOptions();
        HashMap<String, Object> prefs = new HashMap<>();
        prefs.put("plugins.always_open_pdf_externally", true);
        //prefs.put("download.default_directory", " have to mention custom dir location here");
        options.setExperimentalOption("prefs", prefs);

        if (browser.equalsIgnoreCase("chrome")) {
            System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\drivers\\chromedriver.exe");
            driver = new ChromeDriver(options);
        } else if (browser.equals("firefox")) {
            System.setProperty("webdriver.gecko.driver", "src\\main\\resources\\drivers\\GeckoDriver.exe");
            driver = new FirefoxDriver();
        } else if (browser.equals("edge")) {
            System.setProperty("webdriver.edge.driver", "src\\main\\resources\\drivers\\msedgedriver.exe");
            driver = new EdgeDriver();
        } else if (browser.equals("safari")) {
            System.setProperty("webdriver.safari.driver", "src\\main\\resources\\drivers\\Safaridriver.exe");
            driver = new SafariDriver();
        } else {
            System.out.println("Please pass the correct browser value: " + browser);
        }
        driver.manage().window().maximize();
        return driver;
    }
}




