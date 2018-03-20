package daycare;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

/*
 * Class for the refresh task.
 */
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
				try {
					driver.navigate().refresh();
					
					if(null != driver.switchTo().alert() && null != driver.switchTo().alert().getText() && driver.switchTo().alert().getText().contains("ticket(s) in list. Take Action")) {
						driver.switchTo().alert().accept();
					}
					
					driver.switchTo().alert().accept();
					System.out.println("Page refresh done. count - " + count);
				} catch (NoAlertPresentException nape) {

					System.out.println("NoAlertPresentException - " + count);
				}
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

			TicketDTO ticketDTO;
			List<TicketDTO> ticketDTOList = new ArrayList<TicketDTO>();
			int rowNum = 1, colNum = 0;

			WebElement table_element = driver.findElement(By.xpath("/html/body/table[2]/tbody/tr/td[2]/table[3]"));
			List<WebElement> tr_collection = table_element
					.findElements(By.xpath("/html/body/table[2]/tbody/tr/td[2]/table[3]/tbody/tr"));

			int numberOfRows = tr_collection.size();
			System.out.println("Number of rows = " + numberOfRows + " NUMBER OF RECORDS = " + (numberOfRows - 5));
			System.out.println();

			for (WebElement trElement : tr_collection) {

				// Data starts from row 4. Row 4 has headings/titles of columns.
				// Data from row 5 onwards.
				if (rowNum > 5) {

					ticketDTO = new TicketDTO();

					List<WebElement> td_collection = trElement.findElements(By.xpath("td"));
					int numberOfCol = td_collection.size();
					// System.out.println("Row number - " + rowNum + " Number of
					// columns = " + numberOfCol);

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
			System.out.println("Final list ---------->");
			System.out.println();

			int countTicket = 1;
			int countOfEligibleTickets = 0;
			for (TicketDTO dto : ticketDTOList) {

				if (dto.isDateTodayOnwards) {

					countOfEligibleTickets++;
					System.out.println(countOfEligibleTickets + "** " + dto.getId() + "\t" + " Rownum - " + dto.getRowNum() + "\t" +  dto.getTitle() + "\t"
									+ dto.getLastModified() + "\t" + dto.getPriority() + "\t" + dto.getAssignedTo());
				}
				countTicket++;
			}
			System.out.println("TotalTickets - " + countTicket + " countOfEligibleTickets - " + countOfEligibleTickets);
			
			JavascriptExecutor javascript = (JavascriptExecutor) driver;
			javascript.executeScript("alert('" + countOfEligibleTickets + " ticket(s) in list. Take Action');");

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
