import java.net.InetAddress;

public class ArrayOfWindows {

	private newChat[] windows = new newChat[0];
	private int index;
	
	
	
	// add a window to array
public void addWindow(newChat newWindow) {
		
	
	
		
		
		if(newWindow instanceof newChat) {
	        
			if (windows.length == 0) {
	         	windows = new newChat[2];
	        } else if (windows[windows.length -1] != null) {
	         	resize();
	        }
				
		    for (int i = 0; i < windows.length; i++) {
			   if(windows[i] == null) {
				  windows[i] = newWindow;
				  index = i;
				
				System.out.println(windows[i].getTitle() + "Window Added");
				
				break;
			    }
			
		     
		    } 
		 
		 }
		
		
	} 

//return array of windows
public newChat[] getWindows() {
	return windows;
  }
  
  //return the index of the window
public int getWindowIndex() {
	return index;
}


	//this method will double the window array size if it is full
	private void resize() {
		
		newChat[] A = new newChat[windows.length * 2];
		
		for(int i = 0; i < windows.length; i++) {
			A[i] = windows[i]; 
		}
		windows = A;
	}

//remove the window from array when window is closed
public void removeAddress(int port, InetAddress add) {

		System.out.println( "searching for window");
		
		windows[index] = null;
		System.out.println( "window found");
		System.out.println("window removed ");
	
		int i = index;
	 if (index != windows.length -1 )  {
	 while ((i < windows.length - 1)  &&  (windows[i + 1] != null)){
		 windows[i] = windows[i +1];
		 windows[i + 1] = null;
		 i++;
	    }
	     } else {
		   windows[i] = null;  
	    }
	  
		
}
	

	// return number of windows currently open
	public int numberOfWindowsOpen() {
		int number = 0;
		for(int i = 0; i < windows.length; i++) {
			while (windows[i] != null) {
				number++;
			}
		}
		System.out.println("windows " + number);
		return number;
	}
	

	// return true if window is in array else returns false
	public boolean isInArray(int port, InetAddress add) {
		
	
			
		for (int i = 0; i < windows.length; i++) {
			
			if(windows[i] == null) {
				return false;
				
			} else if((windows[i].getPort() == port) && (windows[i].getAdrress().equals(add))){
					
					index = i;
					
					System.out.println(windows[i].getPort() + " add " + windows[i].getAdrress());
				return true;
		      
			}
		  }
		
		
		return false;
	}
	
}
