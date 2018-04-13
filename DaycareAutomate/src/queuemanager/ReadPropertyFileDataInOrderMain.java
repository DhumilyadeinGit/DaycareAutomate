package queuemanager;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class ReadPropertyFileDataInOrderMain extends Properties {

	public ReadPropertyFileDataInOrderMain() {
		super();

		_names = new Vector();
	}

	public Enumeration propertyNames() {
		return _names.elements();
	}

	public Object put(Object key, Object value) {
		if (_names.contains(key)) {
			_names.remove(key);
		}

		_names.add(key);

		return super.put(key, value);
	}

	public Object remove(Object key) {
		_names.remove(key);

		return super.remove(key);
	}

	private Vector _names;

	public static void main(String[] args) {

		QueueRoundRobin qrr = new QueueRoundRobin();
		String value = "";

		System.out.println(System.getProperty("user.dir"));
		
		String rootPath = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		String queueMembersFilePath = rootPath + "files/" + "FormsMemberQueue.properties";
		
		System.out.println("path - " + queueMembersFilePath);

		try {

			/*
			 * Read property file in order
			 */
			Properties qMProperties = new ReadPropertyFileDataInOrderMain();
			qMProperties.load(new FileInputStream(queueMembersFilePath));

			Enumeration<?> e = qMProperties.propertyNames();

			int firstIterationCount = 0;
			boolean startFillingQueue = false;
			while (e.hasMoreElements()) {
				String key = (String) e.nextElement();
				value = qMProperties.getProperty(key);
				System.out.println("Key : " + key + " Value : " + value);

				if (!startFillingQueue && "start".equalsIgnoreCase(value)) {

					startFillingQueue = true;
				}

				if (startFillingQueue) {
					qrr.addToQueue(key);
					firstIterationCount++;
				}

			}

			qrr.printQueueElements();
			System.out.println("count - " + firstIterationCount);

			e = qMProperties.propertyNames();
			int secondIterationCount = 0;
			while (e.hasMoreElements() && (secondIterationCount < (qMProperties.size() - firstIterationCount))) {

				String key = (String) e.nextElement();
				value = qMProperties.getProperty(key);

				qrr.addToQueue(key);
				secondIterationCount++;
			}
			
			qrr.printQueueElements();
			System.out.println("secondIterationCount - " + secondIterationCount);
			
			System.out.println();
			
			for (int i = 0; i < 100; i++) {
				qrr.performRoundRobinPick();
				qrr.printQueueElements();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
