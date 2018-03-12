package daycare;

import java.util.TimerTask;

public class AllTicketsRefreshTask extends TimerTask {

	public AllTicketsRefreshTask() {

	}

	@Override
	public void run() {

		// Start Daycare and perform required tasks
		DaycareAllFormsTickets.performTicketTask();
		//Kapil
	}

}
