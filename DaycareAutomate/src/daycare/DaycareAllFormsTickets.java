package daycare;

import java.util.Calendar;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;

public class DaycareAllFormsTickets {

	public static void main(String[] args) {

		//Setting property for geckodriver.exe to start automation
		System.setProperty("webdriver.gecko.driver", "C:/Kapil Java Workspace/External Resources/geckodriver.exe");
		
		//Scheduling Daycare All Tickets refresh task
		startAllTicketsRefreshTask();

	}

	public static void performTicketTask() {

		// Create a new instance of the Firefox driver
		WebDriver driver = new FirefoxDriver();

		// Launch the Online Store Website
		driver.get("https://kavyas@adobe.com:Thankyouadobe@1@daycare.day.com/home.html");

		// Print a Log In message to the screen
		System.out.println("Successfully opened the website www.Store.Demoqa.com");

		// Wait for 5 Sec
		try {
			Thread.sleep(1 * 1000);

			selectAssignedTo(driver);

			Thread.sleep(1 * 1000);

			// driver.findElement(By.xpath("//a[@href='/html/body/table[2]/tbody/tr/td[2]/table[2]/tbody/tr[17]/td/table/tbody/tr[3]/td[4]/table/tbody/tr[1]/td[1]/table/tbody/tr/td/a']")).click();
			driver.findElement(By
					.xpath("/html/body/table[2]/tbody/tr/td[2]/table[2]/tbody/tr[17]/td/table/tbody/tr[3]/td[4]/table/tbody/tr[1]/td[1]/table/tbody/tr/td/a"))
					.click();

			Thread.sleep(30 * 1000);

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		// Close the driver
		driver.quit();
	}

	public static void selectAssignedTo(WebDriver driver) {

		Select oSelect = new Select(driver.findElement(By.id("AssignedTo")));
		oSelect.selectByVisibleText("AEM Forms (LiveCycle) : entrsupp@adobe.com");
	}

	public static void startAllTicketsRefreshTask() {

		Timer time = new Timer(); // Instantiate Timer Object
		Calendar calendar = Calendar.getInstance();

		// Start running the task on Monday at 15:40:00, period is set to 8
		// hours
		// if you want to run the task immediately, set the 2nd parameter to 0
		time.schedule(new AllTicketsRefreshTask(), calendar.getTime(), TimeUnit.MINUTES.toMillis(1));
	}

}
