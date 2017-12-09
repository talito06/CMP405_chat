

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.InetAddress;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextArea;
import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JScrollPane;

public class newChat extends JFrame implements ActionListener{

	
    private InetAddress address = null;
    private int port;
	private static ArrayOfWindows array;
    private static SendChat sendReceive;
    private JFrame mainWindow;
    private JTextArea textArea;
    private JTextField textField;
    private String myName = "Noranyi";

    
	/**
	 * Launch the application.
	 */
	
	
	public void setInstance(ArrayOfWindows myArray, SendChat newSend, JFrame startChat, int portNumber, InetAddress add) {
		array = myArray;
		sendReceive = newSend;
		mainWindow = startChat;
		address = add;
		port = portNumber;
		
	}

	
	public int getPort() {
		return port;
	}
	
	public InetAddress getAdrress() {
		return address;
	}
	
	/**
	 * Create the frame.
	 */
	public newChat() {
		
		 
		setResizable(false);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		JPanel contentPane = new JPanel();
		contentPane.setBackground(Color.PINK);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		
		textArea = new JTextArea();
		textArea.setCaretColor(Color.BLACK);
		textArea.setForeground(Color.BLACK);
		textArea.setLineWrap(false);
		textArea.setEnabled(false);
		textArea.setEditable(false);
		textArea.setBounds(25, 29, 407, 153);
		textArea.setDisabledTextColor(Color.BLUE);
		contentPane.add(textArea);
		
		 
	
		
		JButton	btnClose = new JButton("Close");
		btnClose.setBounds(35, 242, 117, 29);
		contentPane.add(btnClose);
		btnClose.addActionListener(this);
		
		JButton btnSend = new JButton("Send");
		btnSend.setBounds(274, 242, 117, 29);
		contentPane.add(btnSend);
		btnSend.addActionListener(this);
		contentPane.getRootPane().setDefaultButton(btnSend);
		btnSend.setFocusable(true);
	

		
		textField = new JTextField();
		textField.setBounds(25, 194, 407, 36);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JButton btnNewChat = new JButton("Open New Chat/End Chat");
		btnNewChat.setBounds(25, 0, 209, 29);
		contentPane.add(btnNewChat);
		btnNewChat.addActionListener(this);
		
	
		
		JScrollPane scrollPane = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(25, 29, 407, 153);		
		contentPane.add(scrollPane);
		
		
		
		
		
	}

	public void addToTextArea(String text) {
		if (textArea.getText().trim().length() == 0) {
			textArea.append(text);
		} else {
		textArea.append("\n" + text);
		}
	}
	
	
	public void sentNotification() {
		Sounds sent = new Sounds("messageSound/316798__grey24__flyffnotif.wav");
		sent.play();
	}
	
	@Override 
	public void actionPerformed(ActionEvent e) {
    
		JButton btnClicked = (JButton) e.getSource();
		String button = btnClicked.getText();
		
		switch (button) {
		case "Send":
			 String text = textField.getText();
			 textField.setText("");
			 sendReceive.send(text, address);
			 addToTextArea(myName + ": " + text);
			 sentNotification();
			 
			break;
			
		 case "Close":
			JOptionPane.showMessageDialog(null, "BYE");
			setVisible(false);
			
			
			if(array.isInArray(port, address)) {
				System.out.println("Window is in array");
				System.out.println();
			array.removeAddress(port, address);
		
			} else {
				System.out.println("not found");
			}
			Sounds  bye = new Sounds("messageSound/213286__aderumoro__goodbye-female-friendly.wav");
			bye.play();
		
			break;
			
		 case "Open New Chat/End Chat":
			mainWindow.setVisible(true);
		}				
		
	 }
	}

