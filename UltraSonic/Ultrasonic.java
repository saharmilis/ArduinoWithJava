package ultrasonic;


import java.util.Scanner;
import java.util.concurrent.Semaphore;


import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;

/**
 * 	Initialize with configure() - Default COM3
 * 	Get current distance with getDistance() 
 * @author Sahar
 *	@version 2.0
 */
public class Ultrasonic {

	@SuppressWarnings("unused")
	private final static String PROJECT_PATH = System.getProperty("user.dir");
	@SuppressWarnings("unused")
	private final static String ULTRASONIC_DIRECTORY = "ultrasonic";


	private final static String ARDUINO = "Arduino";

	private final static int MAX_DISTANCE = 400;
	private final static int MIN_DISTANCE = 0;
	private final static int MILISECONDS_GET_FROM_BUFFER = 100;


	private static SerialPort port ;
	
	private static double currentDistance;
	private static Semaphore lock = new Semaphore(1);
	
	


	//////////////////////////////////////////////////////////////


	/**
	 * initialize all resources
	 * using configure()
	 * @param int COM
	 * @throws Exception
	 */
	public static void start() throws Exception {
		
		chooseTheRightCom();

		if(port == null)
			throw new Exception(ARDUINO + "not connected");

		configure();
	}

	/**
	 * 
	 * Initialize all resources
	 */
	private static void configure() {

		setDistanceWithLock(0);

		port.openPort();
		

		//istaticIN = in;
		
		port.addDataListener(new SerialPortDataListener() {

			@Override
			public void serialEvent(SerialPortEvent event) {
				
				if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
					return;
				
				
				Scanner in = new Scanner(port.getInputStream());
				
				if(!in.hasNextInt()) {
					in.close();
					return;
				}
				
				try {
					Thread.sleep(MILISECONDS_GET_FROM_BUFFER);
				} catch (InterruptedException e) { 
					
				}
				
				int dis = in.nextInt();
				
				if(!(MIN_DISTANCE<dis && dis<MAX_DISTANCE)){
					in.close();
					return;
				}
				
				setDistanceWithLock(dis);
				
				
				in.close();
				

			}

			@Override
			public int getListeningEvents() {
				// TODO Auto-generated method stub

				return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
			}
		});

	}
	
	
	

	/**
	 * 
	 * Initialize 'SerialPort port' for "Arduino uno"
	 */
	private static void chooseTheRightCom() {

		SerialPort[] ports = SerialPort.getCommPorts();

		for(int i=0;i<ports.length;i++) 
			if(ports[i].getDescriptivePortName().contains(ARDUINO)) {
				port  = SerialPort.getCommPort(ports[i].getSystemPortName());
				break;
			}



	}
	
	///////////////////////////////////////////////////////////////
	


	//////////////////////////////////////////////////////////////


	/**
	 * Get the current distance from Ultrasonic sensor
	 * @return int currentDistance
	 */
	public static double getDistanceWithLock() {

		double temp = -1;

		try {
			lock.acquire();
			temp = currentDistance; 
			lock.release();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}		

		return temp;
	}
	
	

	private static void setDistanceWithLock(int newDistance) {

		if(lock.tryAcquire()) {
			currentDistance = newDistance;
			//System.out.println(currentDistance);
			lock.release();
		}
	}

	
	///////////////////////////////////////////////////////////////
	
	/**
	 * Close all resources.
	 */
	public static void finish(){

		port.closePort();
	}


}
