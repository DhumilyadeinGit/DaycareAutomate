package daycare;

import java.util.Calendar;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class DaycareAllFormsTicketsMain {

	public static void main(String[] args) {

		// Setting property for geckodriver.exe to start automation - firefox
		/*
		 * System.setProperty("webdriver.gecko.driver",
		 * "C:/Kapil Java Workspace/External Resources/geckodriver.exe");
		 * WebDriver driver = new FirefoxDriver();
		 */

		// Setting property for chromedriver.exe to start automation - Chrome
		System.setProperty("webdriver.chrome.driver", "C:/Kapil Java Workspace/External Resources/chromedriver.exe");
		WebDriver driver = new ChromeDriver();

		// Launch the Online Store Website
		driver.get("https://kavyas@adobe.com:Thankyouadobe@1@daycare.day.com/home.html");
		// driver.manage().timeouts().implicitlyWait(arg0, arg1)

		// Scheduling Daycare All Tickets refresh task
		startAllTicketsRefreshTask(driver);

		System.out.println("Main ENDED");

	}

	public static void closeBrowser() {

		// Close the driver
		// driver.quit();
	}

	public static void selectAssignedTo(WebDriver driver) {

		Select oSelect = new Select(driver.findElement(By.id("AssignedTo")));
		//oSelect.selectByVisibleText("AEM Forms (LiveCycle) : entrsupp@adobe.com");
		
		oSelect.selectByVisibleText("Kapil Vyas : kavyas@adobe.com");
		
	}

	public static void startAllTicketsRefreshTask(WebDriver driver) {

		// System.out.println("startAllTicketsRefreshTask Start");

		Timer time = new Timer(); // Instantiate Timer Object
		Calendar calendar = Calendar.getInstance();
		long interval = TimeUnit.MINUTES.toMillis(1);

		// Start running the task on Monday at 15:40:00, period is set to 8
		// hours
		// if you want to run the task immediately, set the 2nd parameter to 0
		time.schedule(new AllTicketsRefreshTask(driver), calendar.getTime(), interval);

		System.out.println("Refresh scheduled for " + interval);

		// System.out.println("startAllTicketsRefreshTask End");
	}

}
