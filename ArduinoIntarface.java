package arduinoStack;

import java.util.ArrayList;

public interface ArduinoIntarface {

	
	
	public void connect();
	public void disconnect();

	public void registerToNotifyByArduino(NotifyByArduino nba);
	public void unregisterToNotifyByArduino(NotifyByArduino nba);
	
	
	// immobilizer
	public void number();
	
	// car
	public void move();
	
	
}
