package program;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JFrame;

import ultrasonic.Ultrasonic;

public class UI extends JFrame{
	private static final long serialVersionUID = 1L;



	private static final String IP = "37.142.118.102";
	private static final int PORT = 4681;
	private Socket socket;
	private ObjectOutputStream out;
	private ObjectInputStream in;


	private JButton buttonStart;
	private boolean running = false;
	private Thread thread;

	public static void start() throws Exception{

		Ultrasonic.start();
		
		new UI();



	}

	private UI() {
		// TODO Auto-generated constructor stub

		setTitle("UltraSonic");
		setSize(200, 100);
		setResizable(false);
		setBackground(Color.LIGHT_GRAY);


		buttonStart = new JButton("Start");
		buttonStart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

				//ultra sonic start()
				// socket start
				try {
					if(running==true) {
						running = false;
						buttonStart.setText("start");

					} else {
						running = true;
						buttonStart.setText("stop");
						new Thread(new Runnable() {

							@Override
							public void run() {
								// TODO Auto-generated method stub

								while(running) {
									
									try{
									Socket s = new Socket(IP, PORT);
									ObjectOutputStream out = new ObjectOutputStream(s.getOutputStream());
									ObjectInputStream in = new ObjectInputStream(s.getInputStream());
									out.writeObject(Ultrasonic.getDistanceWithLock());
									Thread.sleep(1000);
									in.close();
									out.close();
									s.close();
									} catch(Exception exc) {}
								}

							}
						}).start();

					}

				} catch(Exception ex) {}

				//				if(running==true)
				//					stopTheApp();
				//				else
				//					startTheApp();


			}


		});

		add(buttonStart);




		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);


	}

	protected void startTheApp() {
		// TODO Auto-generated method stub

		System.out.println("UI >> start");

		buttonStart.setText("stop");
		running = true;

		try {
			//openConnectionWithServer();
			Ultrasonic.start();



			thread = new Thread(new Runnable() {

				@Override
				public void run() {
					// TODO Auto-generated method stub
					while(running)
						try {
							openConnectionWithServer();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
				}
			});
			thread.start();


		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}


	}


	private void openConnectionWithServer() throws UnknownHostException, IOException{

		socket = new Socket(IP, PORT);
		out = new ObjectOutputStream(socket.getOutputStream());
		in = new ObjectInputStream(socket.getInputStream());
		out.writeObject(Ultrasonic.getDistanceWithLock());

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		};

		in.close();
		out.close();
		socket.close();

	}

	@SuppressWarnings("unused")
	private void waitForServer() throws IOException, ClassNotFoundException {



		String d="";
		while(running){
			d = (String) in.readObject();
			System.out.println("UI >> from server >> " + d);
			if(d.contains("distance")) {
				//buttonStart.setEnabled(false);
				System.out.println("UI >> send");
				sendDistanceToServer();
				//buttonStart.setEnabled(true);
			}


		}
	}

	private void sendDistanceToServer() {

		double distance = Ultrasonic.getDistanceWithLock();

		System.out.println("UI >> dis is " + distance );

		try {
			System.out.print("UI >> writing ? ");
			out.writeObject(distance);
			System.out.println("done");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("UI >> write double >> ERROR");
		}


	}


	@SuppressWarnings({ "deprecation" })
	protected void stopTheApp() {
		// TODO Auto-generated method stub

		System.out.println("UI >> stop");

		buttonStart.setText("start");
		running = false;

		closeConnectionWithServer();
		thread.stop();
		Ultrasonic.finish();

	}

	private void closeConnectionWithServer() {


		try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			out.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
