package arduinoStack;

public interface NotifyByArduino {

	// immobilizer
	public void arduino_NumberPressed(int number);
	
	// car
	public void Arduino_rightMove();
	public void Arduino_leftMove();
	public void Arduino_breakMove();
	public void Arduino_speedUpMove();
	
}
