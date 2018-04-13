package queuemanager;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.SortedSet;
import java.util.TreeSet;

public class QueueRoundRobin {

	private Queue<String> q = new LinkedList<String>();

	public void performRoundRobinPick() {

		String elementPolled = q.poll();
		q.add(elementPolled);
	}

	public void addToQueue(String element) {

		q.add(element);
	}

	public void printQueueElements() {

		System.out.println();
		for (String s : q) {

			System.out.print(s);
		}
	}

	public void queueSorter() {

		System.out.println("\nSortedQueue - ");
		SortedSet<String> s = new TreeSet<String>(q);
		Iterator<String> it = s.iterator();

		while (it.hasNext()) {
			System.out.print(it.next());
		}
		
		q = new LinkedList<>(s);

		// Queue<String> q1 = new LinkedList<String>();
		// Queue<String> q2 = new LinkedList<String>();
		//
		// while(!q.isEmpty()) {
		//
		// q1.add(q.remove());
		//
		// while(!q1.isEmpty()) {
		//
		// String next = q1.remove();
		// System.out.println("next " + q2);
		//
		// while(!q2.isEmpty() && next<q2.peek()) {
		//
		// if(next<q2.peek()) {
		// q1.add(q2.remove());
		// q2.add(next);
		// }
		// }
		// }
		// }
	}

}
