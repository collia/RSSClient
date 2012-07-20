package controller;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;

/**
 * Class contains parameters of program
 * Load and save it in file  config/config.xml
 * Class implements pattern Singleton
 * @author Nicolay Klimchuk
 *
 */
public class Parameters {

	private  long updatePeriod = 1000*60;//*30;
	private  int updateNumber = -1;//20;
	private  int bufferLenght = 50;
	private  int showNumber = 20;
	private  String keyFile = "config/key";
	private String passFile = "config/passwd";
//	private final String WORK_DIRECTORY = ".";
	
	private static Parameters parameters;

	/**
	 * Constructor. Load parameters from file
	 */
	private Parameters(){
		try {
			loadParameters();
		} catch (JDOMException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Return single instance of Parameters 
	 * @return
	 */
	static public Parameters getParameters(){
		if(parameters == null) parameters = new Parameters();
		return parameters;
	}
	
	/**
	 * Update period - time between updating servers in schedule
	 * @return update period
	 */
	public long getUpdatePeriod(){
		return updatePeriod;
	}
	/**
	 * default number of entries loading from file 
	 * @return number of entries loading from file 
	 */
	public int getUpdateNumber(){
		return updateNumber;
	}
	/**
	 * Size of buffer of view data
	 * @return - buffer size
	 */
	public int getBufferLenght(){
		return bufferLenght; 
	}
	/**
	 * number of default showing when user click on server
	 * @return number of default showing when user click on server
	 */
	public int getShowNumber(){
		return showNumber;
	}
	/**
	 * name and path to keyfile
	 * @return
	 */
	public String getKeyFile(){
		return keyFile;
	}
	/**
	 * name and path of pass file
	 * @return
	 */
	public String getPassFile(){
		return passFile;
	}
	
	/**
	 * Update period - time between updating servers in schedule
	 * @param a new update period
	 */
	public void setUpdatePeriod(long a){
		updatePeriod = a;
		try {
			write();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * set default number of entries loading from file 
	 * @param a new default number of entries loading from file 
	 */
	public void setUpdateNumber(int a){
		updateNumber = a;
		try {
			write();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Size of buffer of view data
	 * @param a buffer size
	 */
	public void setBufferLenght(int a){
		 bufferLenght = a; 
		 try {
				write();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	/**
	 * number of default showing when user click on server
	 * @param a number of default showing when user click on server
	 */
	public void setShowNumber(int a){
		showNumber = a;
		try {
			write();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * name and path to keyfile
	 * @param a
	 */
	public void setKeyFile(String a){
		keyFile = a;
		try {
			write();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/**
	 * name and path of password file
	 * @param a name and path of password file
	 */
	public void setPassFile(String a){
		passFile = a;
		try {
			write();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
/*	public String getWorkDirectory(){
		return WORK_DIRECTORY; 
	}*/
	
	/**
	 * Helper method. Load parameters from file  config/config.xml
	 */
	private void loadParameters() throws JDOMException, IOException{
		File conf = new File("config/config.xml");
		
		if(!conf.canRead()) return;
		
	    SAXBuilder sb = new SAXBuilder();
	    Document doc = sb.build(conf);
	    	//some setup
	    Element propElement = doc.getRootElement();

	      //Access a child element
	    Element posElement = propElement.getChild("updatePeriod");
	      //show success or failure
	    if(posElement != null) {
	    	setUpdatePeriod(Long.parseLong(posElement.getValue()));
	      } else {
	          System.out.println("Something is wrong.  We did not find a propperties.xml");
	          return;
	      }
	    posElement = propElement.getChild("updateNumber");
	    if(posElement != null) {
	       setUpdateNumber(Integer.parseInt(posElement.getValue()));
	    } else {
	       System.out.println("Something is wrong.  We did not find a propperties.xml");
	       return;
	    }
	    
	    posElement = propElement.getChild("bufferLength");
	    if(posElement != null) {
	       setBufferLenght(Integer.parseInt(posElement.getValue()));
	    } else {
	       System.out.println("Something is wrong.  We did not find a propperties.xml");
	       return;
	    }
	    posElement = propElement.getChild("showNumber");
	    if(posElement != null) {
	       setShowNumber(Integer.parseInt(posElement.getValue()));
	    } else {
	       System.out.println("Something is wrong.  We did not find a propperties.xml");
	       return;
	    }
	    posElement = propElement.getChild("keyFile");
	    if(posElement != null) {
	       setKeyFile(posElement.getValue());
	    } else {
	       System.out.println("Something is wrong.  We did not find a propperties.xml");
	       return;
	    }
	    posElement = propElement.getChild("passFile");
	    if(posElement != null) {
	       setPassFile(posElement.getValue());
	    } else {
	       System.out.println("Something is wrong.  We did not find a propperties.xml");
	       return;
	    }
	}
	/**
	 * write parameters to file
	 * @throws IOException - can't write to file
	 */
	private void write() throws IOException
	{
		File conf = new File("config/config.xml");
		FileWriter fw = new FileWriter(conf);
		
		fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		fw.write("<config>\n");
		fw.write("<updatePeriod>"+	getUpdatePeriod()  +"</updatePeriod>");
		fw.write("<updateNumber>"+getUpdateNumber() +"</updateNumber>");
		fw.write("<bufferLength>"+ getBufferLenght()+"</bufferLength>");
		fw.write("<showNumber>"+getShowNumber() +"</showNumber>");
		fw.write("<keyFile>"+getKeyFile() +"</keyFile>");
		fw.write("<passFile>"+getPassFile() +"</passFile>");

		fw.write("</config>");
		fw.flush();
		fw.close();
	}
}
