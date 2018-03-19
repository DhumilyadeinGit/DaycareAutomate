package daycare;

import java.util.List;
import java.util.TimerTask;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class AllTicketsRefreshTask extends TimerTask {

	static long count = 0;

	public static WebDriver driver;

	public AllTicketsRefreshTask(WebDriver driver) {

		AllTicketsRefreshTask.driver = driver;
	}

	@Override
	public void run() {

		AllTicketsRefreshTask.performTicketTask();

	}

	public static void performTicketTask() {

		System.out.println();

		// Start Daycare and perform required tasks
		try {

			if (count != 0) {

				System.out.println("Refreshing page. count - " + ++count);
				driver.navigate().refresh();
				driver.switchTo().alert().accept();
				System.out.println("Page refresh done. count - " + count);
			} else {
				System.out.println("Skipping page refresh and alert accept for the first time. count - " + ++count);
			}

			// Thread.sleep(1 * 1000);
			checkPageIsReady();

			DaycareAllFormsTickets.selectAssignedTo(driver);

			Thread.sleep(2 * 1000);

			System.out.println("Now searching the tickets.. count - " + count);

			// driver.findElement(By.xpath("//a[@href='/html/body/table[2]/tbody/tr/td[2]/table[2]/tbody/tr[17]/td/table/tbody/tr[3]/td[4]/table/tbody/tr[1]/td[1]/table/tbody/tr/td/a']")).click();
			driver.findElement(By
					.xpath("/html/body/table[2]/tbody/tr/td[2]/table[2]/tbody/tr[17]/td/table/tbody/tr[3]/td[4]/table/tbody/tr[1]/td[1]/table/tbody/tr/td/a"))
					.click();

			Thread.sleep(2 * 1000);

			checkPageIsReady();
			System.out.println("Ticket list DISPLAYED. count - " + count);

			Thread.sleep(5 * 1000);

			checkPageIsReady();
			System.out.println("Reading table data. count - " + count);
			// Reading table data
			readTableData();

			// Thread.sleep(30 * 1000);

		} catch (NoSuchElementException nsee) {

			System.out.println("NoSuchElementException - " + count);
		} catch (NoAlertPresentException nape) {

			System.out.println("NoAlertPresentException - " + count);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	public static void checkPageIsReady() {

		JavascriptExecutor js = (JavascriptExecutor) driver;

		// Initially bellow given if condition will check ready state of page.
		if (js.executeScript("return document.readyState").toString().equals("complete")) {
			System.out.println("Page Is loaded.");
			return;
		}

		// This loop will rotate for 25 times to check If page Is ready after
		// every 1 second.
		// You can replace your value with 25 If you wants to Increase or
		// decrease wait time.
		for (int i = 0; i < 25; i++) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
			}
			// To check page ready state.
			if (js.executeScript("return document.readyState").toString().equals("complete")) {
				System.out.println("Page Is loaded. " + i);
				break;
			}
		}
	}

	public static void readTableData() {

		try {

			WebElement table_element = driver.findElement(By.xpath("/html/body/table[2]/tbody/tr/td[2]/table[3]"));
			List<WebElement> tr_collection = table_element
					.findElements(By.xpath("/html/body/table[2]/tbody/tr/td[2]/table[3]/tbody/tr"));

			int numberOfRows = tr_collection.size();
			System.out.println("Number of rows = " + numberOfRows);
			System.out.println("NUMBER OF RECORDS = " + (numberOfRows - 5));
			int row_num, col_num;
			row_num = 1;
			for (WebElement trElement : tr_collection) {

				System.out.println();

				if (row_num > 4) {

					List<WebElement> td_collection = trElement.findElements(By.xpath("td"));
					int numberOfCol = td_collection.size();
					System.out.println("Row number - " + (row_num - 5) + " Number of columns = " + numberOfCol);
					col_num = 1;
					for (WebElement tdElement : td_collection) {

						if (col_num > 3 && col_num % 2 == 0) {
							// System.out.println("row # " + row_num + ", col #
							// " +
							// col_num + "text=" + tdElement.getText());
							if (null != tdElement && null != tdElement.getText() && !tdElement.getText().isEmpty()) {
								System.out.println(tdElement.getText());
							} else {
								System.out.println("N/A");
							}
						}
						col_num++;
					}
				}
				row_num++;
			}
		} catch (StaleElementReferenceException sere) {

			System.out.println("StaleElementReferenceException. count - " + count);
		}
	}

}
