package daycare;

import java.util.Comparator;

public class TicketDTOComparator implements Comparator<TicketDTO> {

	@Override
	public int compare(TicketDTO t1, TicketDTO t2) {

		return (int) (t1.getId() - t2.getId());
	}

}
