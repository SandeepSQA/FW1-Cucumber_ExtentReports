package steps;

import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter;

import drivermanager.Driverbase;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class Test {
	@Given("I want to test")
	public void i_want_to_test() throws InterruptedException {
	   Driverbase.driver.get("");
	   Thread.sleep(4000);
	   ExtentCucumberAdapter.getCurrentStep().log(Status.PASS, "User keys in URL successfully");
	}

	@Given("test1")
	public void test1() throws InterruptedException {
		
		   Thread.sleep(4000);
	}

	@When("test2")
	public void test2() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

	@Then("Test3")
	public void test3() {
	    // Write code here that turns the phrase above into concrete actions
	    throw new io.cucumber.java.PendingException();
	}

}
