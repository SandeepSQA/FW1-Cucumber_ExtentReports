package runners;


import org.junit.runner.RunWith;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;



@RunWith(Cucumber.class)
@CucumberOptions(
        features = {"src/test/resources/features/"},
        glue = {"steps"},
        tags = ("@tag"),
        plugin = {"pretty",
                "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"
        }
)

public class BDDTestRunner {


}

