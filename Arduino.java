package arduinoStack;


import java.util.ArrayList;

public class Arduino implements ArduinoIntarface{
	
	///////////////////////////////////////////
	////// DO NOT TOUCH THIS CLASS (YET) //////
	///////////////////////////////////////////	
	
	ArrayList<NotifyByArduino> notifyByArduino;
	
	public Arduino() {
		// TODO Auto-generated constructor stub
		
		notifyByArduino = new ArrayList<>();
		
	}

	@Override
	public void connect() {
		// TODO Auto-generated method stub
		
		System.out.println("Arduino >> connect");
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
		System.out.println("Arduino >> disconnect");
	}

	@Override
	public void registerToNotifyByArduino(NotifyByArduino nba) {
		// TODO Auto-generated method stub
		
		notifyByArduino.add(nba);
	}

	@Override
	public void unregisterToNotifyByArduino(NotifyByArduino nba) {
		// TODO Auto-generated method stub
		
		notifyByArduino.remove(nba);		
	}

	@Override
	public void number() {
		// TODO Auto-generated method stub
		
		System.out.println("Arduino >> number");
		
		
		// int number = get number from arduino
		// e.arduino_NumberPressed(number);
		
		////////////////
		// SIMULATION //
		////////////////
		for(int number=1;number<=4;number++) { // run all number 1 to 4
			
			System.out.println("Arduino >> number >> press >> " + number);

			for(NotifyByArduino e: notifyByArduino) // notify all registered
				e.arduino_NumberPressed(number);
		}
		////////////////
		////////////////
		
		
	}

	@Override
	public void move() {
		// TODO Auto-generated method stub
		
		System.out.println("Arduino >> move");
		
	}
	
	
	
	
}
