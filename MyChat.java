import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JButton;
import javax.swing.JTextField;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Color;
import javax.swing.SwingConstants;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.net.*;
import java.util.concurrent.SynchronousQueue;



public class MyChat implements ActionListener{

	private static JFrame StartChat;
	private JTextField nameTextField;
	private JLabel lblMessage;
	private static DatagramSocket mySocket = null;
	private static SendChat sendInstance;
	private static ArrayOfWindows array;
	private static InetAddress myAddress = null;
	private static String otherPersonName = "";
	private static String myName = "Noranyi";
	private static String receiverName = "";
	private static InetAddress otherPersonAddress = null;
    private static Sounds receiveSound;
    private static String nameOfRequestedIp;
    private static int myPort = 64000;
    
	/**
	 * Create the application.
	 */
	public MyChat() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		StartChat = new JFrame();
		StartChat.setFont(new Font("Comic Sans MS", Font.BOLD, 12));
		StartChat.setTitle("Start Chat");
		StartChat.setBounds(100, 100, 450, 300);
		StartChat.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		StartChat.getContentPane().setLayout(null);
		
		
		
		nameTextField = new JTextField();
		nameTextField.setBounds(43, 123, 334, 26);
		StartChat.getContentPane().add(nameTextField);
		nameTextField.setColumns(10);
		
		
		JButton btnChat =  new JButton("Open New Chat");
		btnChat.addActionListener(this);
		
	
		btnChat.setForeground(Color.PINK);
		btnChat.setBounds(31, 192, 141, 29);
		StartChat.getContentPane().add(btnChat);
		
		JButton btnClose = new JButton("End Chat");
		btnClose.setForeground(Color.PINK);
		btnClose.setBounds(250, 192, 117, 29);
		StartChat.getContentPane().add(btnClose);
		btnClose.addActionListener(this);
		
		lblMessage = new JLabel("Receiver's Name");
		lblMessage.setHorizontalAlignment(SwingConstants.CENTER);
		lblMessage.setForeground(Color.PINK);
		lblMessage.setBounds(64, 72, 293, 39);
	    StartChat.getContentPane().add(lblMessage);
	}
	
    public static void main(String[] args) {
		
		
		array = new ArrayOfWindows();
		 
		
			try {
				myAddress = InetAddress.getLocalHost();
				
				
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
			
			System.out.println("this is my address from  main method " + myAddress);
			
			try {
					mySocket = new DatagramSocket(myPort);
					} catch (Exception e) {
						e.printStackTrace();
						System.exit(-1);
					}
			
			sendInstance = new SendChat(mySocket);
			Thread receiveThread = new Thread(new Runnable () {
				public void run() {
					receiveMethod();
				}
			});
			
			receiveThread.setName("Receive Thread");
			receiveThread.start();
		
			
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MyChat window = new MyChat();
					window.StartChat.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
    
	public static void receiveMethod() {
		
		InetAddress sourceAddress = null;
		byte[] inBuffer = new byte[1000];
		
		DatagramPacket inPacket = new DatagramPacket(inBuffer, inBuffer.length);

		  
		do {
			for ( int i = 0 ; i < inBuffer.length ; i++ ) {
				inBuffer[i] = ' ';
			}
			
			try {
				// this thread will block in the receive call until a message is received
				System.out.println("Waiting for message");
				mySocket.receive(inPacket);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-1);
			}
			
			

			sourceAddress = inPacket.getAddress();
			System.out.println("source add when i receive:" + sourceAddress);
			
			String message = new String(inPacket.getData());
			
			System.out.println("Received message = " + message);
		
			
			receiveSound  = new Sounds("messageSound/234524__foolboymedia__notification-up-1.wav");
			
			
			if ((isResquestingIp(message)) && (nameOfRequestedIp.equalsIgnoreCase(myName)))
				{
				     sendInstance.send("##### " + myName + " ##### " + myAddress.getHostAddress(), sourceAddress);
				
				        if(!(array.isInArray(myPort, sourceAddress)))
				           { 
					      newChat myChat = new newChat();
					      myChat.setTitle(otherPersonName + "    IP Address: " + sourceAddress.getHostAddress());
					      myChat.setInstance(array, sendInstance, StartChat, myPort, sourceAddress);
					      array.addWindow(myChat);
					      
				         } 
				 //} else {
					//   System.out.println("Message Ignored");
				     //    }
					
					
			
			} else if (isMyIpRequestAnswer(message)) {
				
				
				if(!(array.isInArray(myPort, otherPersonAddress))){ 
					newChat myChat = new newChat();
					myChat.setTitle(otherPersonName + "    IP Address: " + otherPersonAddress.getHostAddress());
					myChat.setInstance(array, sendInstance, StartChat, myPort, otherPersonAddress);
					myChat.setVisible(true);
					array.addWindow(myChat);
					receiveSound.play();
					
					} else {
						
					int index = array.getWindowIndex();
						array.getWindows()[index].setVisible(true);
						receiveSound.play();
					}
				
			} else if(array.isInArray(myPort, sourceAddress)){
				int index = array.getWindowIndex();
				array.getWindows()[index].addToTextArea(otherPersonName + ": " + message);
				array.getWindows()[index].setVisible(true);
				receiveSound.play();
			  
			}  // else if(!(array.isInArray(myPort, sourceAddress))){
				//System.out.println("Message ignored");
			//}
			
		} while(true);
	}
	
	
	
	private static boolean isResquestingIp(String message) {
		
		if(message.startsWith("?????")) {
			String[] splittedMsg = message.split(" ");
			
			  if(splittedMsg[2].equalsIgnoreCase("#####")){
			      nameOfRequestedIp = splittedMsg[1];
				  otherPersonName = splittedMsg[3];
			   
			for (int i = 0; i < splittedMsg.length; i++) {
				System.out.println(splittedMsg[i]);
			}
			  return true;
			  }
		}
		return false;
	}
	
	private static boolean isMyIpRequestAnswer(String message) {
		if(message.startsWith("#####")) {
			
			String[] splittedMsg = message.split(" ");
			if((splittedMsg[1].equalsIgnoreCase(receiverName)) && splittedMsg[2].equalsIgnoreCase("#####")){/// Im not sure if i should include receivers name here
				otherPersonName = splittedMsg[1];
			try {
				otherPersonAddress =  InetAddress.getByName(splittedMsg[3]);
			} catch (UnknownHostException e) {
				
				e.printStackTrace();
			}  
			return true;
			
			}
		}
		return false;
	}

	public void sentNotification() {
		Sounds sent = new Sounds("messageSound/316798__grey24__flyffnotif.wav");
		sent.play();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JButton btnClicked = (JButton) e.getSource();
		
		
		
		switch (btnClicked.getText()) {
		case "Open New Chat":
			InetAddress broadscastAddress = null;
			
			try {
				
				broadscastAddress = InetAddress.getByName("255.255.255.255");
				//broadscastAddress = InetAddress.getByName("25.2.7.224");
			} catch (UnknownHostException e1) {
				
				e1.printStackTrace();
			}
			 
			receiverName = nameTextField.getText();
			nameTextField.setText("");
			
			sendInstance.send("????? " + receiverName + " ##### " + myName, broadscastAddress);
			
			  sentNotification();
			  StartChat.setState(Frame.ICONIFIED);
			
			 
			break;
		    case "End Chat":
		    	Sounds  bye = new Sounds("messageSound/213286__aderumoro__goodbye-female-friendly.wav");
		    	bye.play();
			JOptionPane.showMessageDialog(null, "BYE");
			System.exit(JFrame.EXIT_ON_CLOSE);
			System.exit(0);
			break;
		}
		
	}
}
