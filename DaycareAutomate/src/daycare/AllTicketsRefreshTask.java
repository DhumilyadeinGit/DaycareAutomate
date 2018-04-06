package daycare;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/*
 * Class for the refresh task.
 */
public class AllTicketsRefreshTask extends TimerTask {

	static long count = 0;
	static int countTicket = 0;
	static int countOfEligibleTickets = 0;

	public static WebDriver driver;

	public AllTicketsRefreshTask(WebDriver driver) {

		AllTicketsRefreshTask.driver = driver;
	}

	@Override
	public void run() {

		countTicket = 0;
		countOfEligibleTickets = 0;

		AllTicketsRefreshTask.performTicketTask();

	}

	public static void performTicketTask() {

		System.out.println();

		count++;

		// Start Daycare and perform required tasks
		try {

			try {

				if (null != driver.switchTo().alert() && null != driver.switchTo().alert().getText()
						&& driver.switchTo().alert().getText().contains("ticket(s) in list. Take Action")) {
					driver.switchTo().alert().accept();
				}

			} catch (NoAlertPresentException nape) {

				System.out.println("NoAlertPresentException - " + count);
			}

			WebDriverWait wait = new WebDriverWait(driver, 15);
			// System.out.println("Waiting for AssignedTO dropdown start");
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("AssignedTo")));
			// System.out.println("Wait for AssignedTO dropdown END");

			DaycareAllFormsTicketsMain.selectAssignedTo(driver);

			// System.out.println("Waiting for Search Button start");
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(
					"/html/body/table[2]/tbody/tr/td[2]/table[2]/tbody/tr[17]/td/table/tbody/tr[3]/td[4]/table/tbody/tr[1]/td[1]/table/tbody/tr/td/a")));
			// System.out.println("Wait for Search Button END");

			System.out.println("Search button clicked. Now searching the tickets.. count - " + count);

			driver.findElement(By
					.xpath("/html/body/table[2]/tbody/tr/td[2]/table[2]/tbody/tr[17]/td/table/tbody/tr[3]/td[4]/table/tbody/tr[1]/td[1]/table/tbody/tr/td/a"))
					.click();

			// System.out.println("Waiting for Truste Ticket list start");
			wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("/html/body/div/p[2]/a")));
			// System.out.println("Wait for Truste Ticket list END");

			System.out.println(new Date().toString() + "Ticket list DISPLAYED. count - " + count);

			// System.out.println("Reading table data. count - " + count);

			// Reading table data
			readTableData();

		} catch (NoSuchElementException nsee) {

			System.out.println("NoSuchElementException - " + count);
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

			TicketDTO ticketDTO;
			List<TicketDTO> ticketDTOList = new ArrayList<TicketDTO>();
			int rowNum = 1, colNum = 0;

			WebElement table_element = driver.findElement(By.xpath("/html/body/table[2]/tbody/tr/td[2]/table[3]"));
			List<WebElement> tr_collection = table_element
					.findElements(By.xpath("/html/body/table[2]/tbody/tr/td[2]/table[3]/tbody/tr"));

			int numberOfRows = tr_collection.size();
			// System.out.println("Number of rows = " + numberOfRows + " NUMBER
			// OF RECORDS = " + (numberOfRows - 5));
			// System.out.println();

			System.out.println(new Date().toString() + " Time before first loop. count - " + count);

			for (WebElement trElement : tr_collection) {

				// Data starts from row 4. Row 4 has headings/titles of columns.
				// Data from row 5 onwards.
				if (rowNum > 5) {

					ticketDTO = new TicketDTO();

					List<WebElement> td_collection = trElement.findElements(By.xpath("td"));
					int numberOfCol = td_collection.size();

					colNum = 1;
					for (WebElement tdElement : td_collection) {

						if (colNum > 3 && colNum % 2 == 0) {

							if (null != tdElement) {

								AllTicketsRefreshTask.setTicketDTO(ticketDTO, colNum, tdElement.getText());
								ticketDTO.setRowNum(rowNum);
							} else {
								System.out.println("COLUMN ELEMENT IS NULL");
							}
						}
						colNum++;
					}
					ticketDTOList.add(rowNum - 6, ticketDTO);
				}
				rowNum++;
			}

			/*
			 * Print eligible tickets -- raised/last update today.
			 */
			// System.out.println("Final list ---------->");
			// System.out.println();

			List<TicketDTO> eligibleTicketList = AllTicketsRefreshTask.identifyEligibleTickets(ticketDTOList);

			// JavascriptExecutor javascript = (JavascriptExecutor) driver;
			// javascript.executeScript("alert('" + countOfEligibleTickets + "
			// ticket(s) in list. Take Action');");

			for (TicketDTO ticket : eligibleTicketList) {

				AllTicketsRefreshTask.openTicketToAssign(ticket);

				break;
			}

		} catch (StaleElementReferenceException sere) {

			System.out.println("StaleElementReferenceException. count - " + count);
		}
	}

	public static Date parseStringToDate(String sDate) {

		Date date1 = new Date();
		try {

			date1 = new SimpleDateFormat("dd.MM.yyyy").parse(sDate);

		} catch (ParseException e) {
			e.printStackTrace();
		}

		return date1;

	}

	public static TicketDTO setTicketDTO(TicketDTO ticketDTO, long colNum, String colData) {

		if (!(null != colData && !colData.isEmpty())) {
			colData = "N/A";
		}

		if (colNum == 4) {
			ticketDTO.setId(stringToLong(colData));
		} else if (colNum == 6) {

			Date ticketDt = parseStringToDate(colData);
			ticketDTO.setLastModified(ticketDt);

			boolean isDateTodayOnwards = AllTicketsRefreshTask.isDateTodayOnwards(ticketDt);
			ticketDTO.setDateTodayOnwards(isDateTodayOnwards);

		} else if (colNum == 8) {
			ticketDTO.setAssignedTo(colData);
		} else if (colNum == 10) {
			ticketDTO.setClientInfo(colData);
		} else if (colNum == 12) {
			ticketDTO.setTitle(colData);
		} else if (colNum == 14) {
			ticketDTO.setPriority(colData);
		} else if (colNum == 16) {
			ticketDTO.setSeverity(colData);
		} else if (colNum == 18) {
			ticketDTO.setScope(colData);
		} else if (colNum == 20) {
			ticketDTO.setStatus(colData);

			if (!(ticketDTO.getStatus().equals("closed"))) {
				ticketDTO.setTicketEligible(true);
			}
		}

		return ticketDTO;
	}

	public static long stringToLong(String sData) {

		long lData = 0L;
		if (null != sData && !sData.isEmpty()) {
			lData = Long.parseLong(sData);
		}
		return lData;
	}

	public static boolean isDateTodayOnwards(Date ticketDate) {

		boolean result = false;

		Calendar now = Calendar.getInstance();
		now.set(Calendar.HOUR, 0);
		now.set(Calendar.MINUTE, 0);
		now.set(Calendar.SECOND, 0);
		now.set(Calendar.HOUR_OF_DAY, 0);
		now.set(Calendar.MILLISECOND, 0);

		Date currentDate = now.getTime();

		if (ticketDate.equals(currentDate) || ticketDate.after(currentDate)) {
			result = true;
		}

		return result;
	}

	public static List<TicketDTO> identifyEligibleTickets(List<TicketDTO> ticketDTOList) {

		List<TicketDTO> eligibleTicketDTOList = new ArrayList<TicketDTO>();

		for (TicketDTO dto : ticketDTOList) {

			// if (dto.isDateTodayOnwards) {
			//
			// countOfEligibleTickets++;
			// System.out.println(countOfEligibleTickets + "** " + dto.getId() +
			// "\t" + " Rownum - " + dto.getRowNum()
			// + "\t" + dto.getTitle() + "\t" + dto.getLastModified() + "\t" +
			// dto.getPriority() + "\t"
			// + dto.getAssignedTo());
			// }

			if (dto.isTicketEligible()) {

				countOfEligibleTickets++;
				eligibleTicketDTOList.add(dto);
				// System.out.println(countOfEligibleTickets + "** " +
				// dto.getId() + "\t" + " Rownum - " + dto.getRowNum()
				// + "\t" + dto.getTitle() + "\t" + dto.getLastModified() + "\t"
				// + dto.getPriority() + "\t"
				// + dto.getAssignedTo() + "\t" + dto.getStatus());
			}
			countTicket++;
		}
		System.out.println(new Date().toString() + "TotalTickets - " + countTicket + " countOfEligibleTickets - "
				+ countOfEligibleTickets);

		// System.out.println("\nEligible tickets sorted on Ticket ID");
		Collections.sort(eligibleTicketDTOList, new TicketDTOComparator());

		// Printing sorted Ticket list data
		for (TicketDTO dto1 : eligibleTicketDTOList) {
			System.out.println("Ticket ID - " + dto1.getId() + "\t" + " Rownum - " + dto1.getRowNum() + "\t"
					+ dto1.getTitle() + "\t" + dto1.getLastModified() + "\t" + dto1.getPriority() + "\t"
					+ dto1.getAssignedTo() + "\t" + dto1.getStatus());
		}

		return eligibleTicketDTOList;
	}

	public static void openTicketToAssign(TicketDTO ticketDTO) {

		String ticketXPath = "/html/body/table[2]/tbody/tr/td[2]/table[3]/tbody/tr[" + ticketDTO.getRowNum()
				+ "]/td[2]/a";

		System.out.println("Opening Ticket ID - " + ticketDTO.getId());
		driver.findElement(By.xpath(ticketXPath)).click();

		try {

			System.out.println("Ticket # " + ticketDTO.getId() + " opened. Waiting for 5 seconds.");
			Thread.sleep(5000);

			System.out.println("Ticket url - " + driver.getCurrentUrl());

			AllTicketsRefreshTask.fillTicketData();

			System.out.println("Going BACK.");
			driver.navigate().back();

		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void fillTicketData() {

		System.out.println("Filling ticket data");

		try {

			/*
			 * Update summary
			 */
			WebElement updateSummaryLabel = driver.findElement(
					By.xpath("/html/body/table[2]/tbody/tr/td[2]/table[4]/tbody/tr[27]/td/table/tbody/tr[3]/td[2]"));

			if ("Update Summary*".equals(updateSummaryLabel.getText())) {

				System.out.println("Adding update summary");
				WebElement updateSummaryTextArea = driver.findElement(By.xpath(
						"/html/body/table[2]/tbody/tr/td[2]/table[4]/tbody/tr[27]/td/table/tbody/tr[3]/td[4]/table/tbody/tr[1]/td[1]/textarea"));
				updateSummaryTextArea.sendKeys("Assigning to Kapil Vyas");

				Thread.sleep(2000);

			}

			/*
			 * Assign to
			 */
			WebElement assignToDD = driver.findElement(By.id("_Data_AssignedTo"));
			assignToDD.sendKeys("Aayush Srivastava : T 919718314266, aaysriva@adobe.com");
			Thread.sleep(3000);

		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*
	 * Column position
	 * 
	 * @4 id
	 * 
	 * @6 last modified date
	 * 
	 * @8 assignedTo
	 * 
	 * @10 Client Info
	 * 
	 * @12 Title
	 * 
	 * @14 Priority
	 * 
	 * @16 Severity
	 * 
	 * @18 Scope
	 * 
	 * @20 Status
	 */

}
