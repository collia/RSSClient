package controller;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.crypto.NoSuchPaddingException;

import main.RSSLogger;

import org.jdom.JDOMException;

import com.sun.syndication.io.FeedException;

import RSS.*;
import RSS.data.Answer;
import RSS.data.Category;
import RSS.data.RSSEntry;
import RSS.data.RSSServer;
import RSS.filer.Crypt;

import RSS.filer.RSSFileWriter;
import RSS.filer.RSSServerFile;

/**
 * Class for controlling kernel. Communicate between 
 * user interface and kernel.   
 * @author Nikolay Klimchuk
 *
 */
public class Controller {

	private static Logger logger = RSSLogger.loggerController; 
	private Map<RSSServer, List<RSSEntry>> data;
	private Category root;
	private RSSReader reader;
	private RSSServerFile serverFile;
	private boolean isLogged = false;
	
	/**
	 * Constructor. Initializing local variables.
	 */
	public Controller()
	{
		logger.entering(this.getClass().getName(),"Controller()");
		serverFile = new RSSServerFile();
		data = new HashMap<RSSServer, List<RSSEntry>>();
		logger.exiting(this.getClass().getName(),"Controller()");
	}
	
	/**
	 * Method connect to server and trying download RSS feed.
	 * After, loading from file last entryes, and remove 
	 * old entryes from downloaded.
	 * Method return last num  
	 * @param servers - list of updating servers 
	 * @param num - number of last entryes. -1 for all. 
	 * @return last num new entryes
	 * @see Answer
	 */
	public Answer update(List<RSSServer> servers, int num)
	{
		logger.entering(this.getClass().getName(),"update(List<RSSServer> servers, int num)");
		reader = new RSSThread(new RSSNet());
		List<Integer> n= new ArrayList<Integer>();
		for(int i = 0; i< servers.size(); i++)
			n.add(num);
		
		logger.fine("Send request");
		Answer ans = reader.getLastRSS(servers, n);
		logger.finer("get: " + ans.getEntryes().size() + "Exceptinos: " + ans.getExceptions().size());
			
		logger.fine("Load from file");
		Answer ansLocal = (new RSSFiler(this)).getLastRSS(servers, n, false, true);
		logger.finer("get: " + ansLocal.getEntryes().size() + "Exceptinos: " + ansLocal.getExceptions().size());
		logger.fine("Minus");
		ans.minus(ansLocal);
		logger.finer("get: " + ans.getEntryes().size() + "Exceptinos: " + ans.getExceptions().size());
		
		if(!ans.getExceptions().isEmpty()){
			logger.fine("Begin load from file if Exception");
			reader = new RSSThread(new RSSFiler(this));
			
			List<RSSServer> listServers = new ArrayList<RSSServer>();// Set<RSSServer>
			listServers.addAll(ans.getExceptions().keySet());
			logger.fine("Bad servers: "+ listServers);
			
			Answer ansF = reader.getLastRSS(listServers, n);
			logger.finer("get: " + ans.getEntryes().size() + "Exceptinos: " + ans.getExceptions().size());
			
			ans.add(ansF);
		}
		addNewToMap(data, ans);
		logger.exiting(this.getClass().getName(),"update(List<RSSServer> servers, int num)");
		return ans;
	}
	/**
	 * Load last num entryes from file. 
	 * @param servers - list of servers
	 * @param num - number of last entryes
	 * @return - last num entryes from file
	 */
	public Answer getLast(List<RSSServer> servers, int num)
	{
		logger.entering(this.getClass().getName(), "getLast(List<RSSServer> servers, int num)");
		List<Integer> n = new ArrayList<Integer>();
		for(int i = 0; i< servers.size(); i++)
			n.add(num);
		logger.fine("Loading from file");
		reader = new RSSThread(new RSSFiler(this));
		Answer ans = reader.getLastRSS(servers, n);
		logger.finer("get: " + ans.getEntryes().size() + "Exceptinos: " + ans.getExceptions().size());
				
		addNewToMap(data, ans);
		logger.exiting(this.getClass().getName(), "getLast(List<RSSServer> servers, int num)");
		return ans;
	} 
	/**
	 * Method return all entryes, that mark us not read
	 * @param servers - List of servers
	 * @return all entryes, that mark us not read
	 */
	public Answer getNotReadEntryes(List<RSSServer> servers)
	{
		logger.entering(this.getClass().getName(), "getNotReadEntryes(List<RSSServer> servers)");
		Answer ans = getLast(servers, -1);
		logger.finer("Load from file \nget: " + ans.getEntryes().size() + "Exceptinos: " + ans.getExceptions().size());
		
		Answer result = new Answer(new HashMap<RSSServer, List<RSSEntry>>(),
									new HashMap<RSSServer, Exception>());
		
		for(RSSServer ser : ans.getEntryes().keySet()){
			for(RSSEntry entry : ans.getEntryes().get(ser)){
				if(!entry.isReaded())
					result.addEntry(ser, entry);
			}
		}
		result.addExceptions(ans);
		logger.finer("Get: " + result.getEntryes().size() + "Exceptinos: " + result.getExceptions().size());
				
		logger.exiting(this.getClass().getName(), "getNotReadEntryes(List<RSSServer> servers)");
		return result;
	} 
	/**
	 * Method return root Category, if it null, load server list from file
	 * @return root category
	 * @throws JDOMException - bad server file format
	 * @throws IOException - couldn't found file
	 */
	public Category getServers() throws JDOMException, IOException {
		if(root == null){
			root = serverFile.load();
			logger.fine("Loading servers");
		}
		return root;
			
	}
	/**
	 * Method write server list to file
	 * @throws IOException - can't write file
	 */
	public void writeServers() throws IOException{
		logger.fine("Writing servers");
		if(root != null)
			serverFile.write(root);
	}
	/**
	 * Set root category
	 * @param root - new root category
	 */
	public void setServers(Category root){
		this.root = root;
	}
	/**
	 * convert tree of category to server list
	 * @return - list of all servers
	 */
	public List<RSSServer> getServersList()
	{
		ArrayList<RSSServer> list = new ArrayList<RSSServer>();
		categoryToList(list, root);
		return list;	
	}
	/**
	 * helper method. Recursively convert tree to list
	 * @param serv - list for servers
	 * @param c - root category
	 */
	private void categoryToList(List<RSSServer> serv, Category c)
	{
		serv.addAll(c.getServers());
		Iterator<Category> it = c.getChildren().iterator();
		while(it.hasNext())
			categoryToList(serv, it.next());
		
	}
	
	/**
	 * Helper method.
	 * Adding entryes from Answer to Map 
	 * @param d - result map
	 * @param a - Answer
	 * @see Answer
	 */
	private void addNewToMap(Map<RSSServer, List<RSSEntry>> d, Answer a)
	{
		Iterator<RSSServer> it = a.getEntryes().keySet().iterator();
		
		while(it.hasNext())
		{
			RSSServer s = it.next();
			if(d.containsKey(s))
			{
				Iterator<RSSEntry> itEntry = a.getEntryes().get(s).iterator();
				while(itEntry.hasNext())
				{
					RSSEntry e = itEntry.next();
					if(!d.get(s).contains(e))
					{
						d.get(s).add(e);
					}
				}
			}
		}
	}
	/**
	 * Method save to disk entries for server
	 * @param server - RSS Server - where save
	 * @param data - list of data
	 * @see RSSServer
	 * @throws IllegalArgumentException
	 * @throws JDOMException
	 * @throws IOException
	 * @throws FeedException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 */
	public void writeRSS(RSSServer server, List<RSSEntry> data) throws IllegalArgumentException, JDOMException, IOException, FeedException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException{
		logger.entering(this.getClass().getName(), "writeRSS(RSSServer server, List<RSSEntry> data)");
		RSSFileWriter rfw = new RSSFileWriter(server.getLocalCategory().toString());
		logger.fine("Write " + server);
		rfw.writeRSS(data, server);
		logger.exiting(this.getClass().getName(), "writeRSS(RSSServer server, List<RSSEntry> data)");
	}
	/**
	 * Write to disk all getting data for all servers in Answer
	 * @param ans - data with servers
	 * @see Answer
	 * @throws IllegalArgumentException
	 * @throws JDOMException
	 * @throws IOException
	 * @throws FeedException
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 */
	public void writeRSS(Answer ans) 
		throws IllegalArgumentException, JDOMException, 
		IOException, FeedException, InvalidKeyException, 
		NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException{
		
		logger.entering(this.getClass().getName(), "writeRSS(Answer ans)");
		
		Iterator<RSSServer> itServer = ans.getEntryes().keySet().iterator();
		while(itServer.hasNext())
		{
			RSSServer s = itServer.next();
			writeRSS(s, ans.getEntryes().get(s));
		}
		logger.exiting(this.getClass().getName(), "writeRSS(Answer ans)");
	}
	/**
	 * Rename servers and save it on disk
	 * @param oldName - server to rename
	 * @param newName - new name
	 * @see RSSServer
	 */
	public void renameServer(RSSServer oldName, String newName)
	{
		logger.fine("Rename servers");
		RSSFileWriter rfw = new RSSFileWriter(oldName.getLocalCategory().toString());
		rfw.renameServer(oldName, newName);
	}
	/**
	 * Rename category and save it on disk
	 * @param oldName - category to rename
	 * @param newName - new name
	 */
	public void renameCategory(Category oldName, String newName)
	{
		logger.fine("Rename category");
		RSSFileWriter rfw = new RSSFileWriter(oldName.toString());
		rfw.renameCategory(oldName, newName);
	}
	/**
	 * Return - is user logged
	 * @return - is user logged
	 */
	public boolean isLogged(){
		return isLogged;
	}
	/**
	 * set isLogged  to falseS
	 */
	public void logOff(){
		isLogged = false;
	}
	/**
	 * Method get user password, load right password from file
	 * and return - is user password right. Logging if true.
	 * If can't found pass file, create new with default password 
	 * @param password - user password
	 * @return - is logged
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws IOException
	 */
	public boolean logged(String password) 
		throws InvalidKeyException, NoSuchAlgorithmException, 
		InvalidKeySpecException, NoSuchPaddingException, IOException
	{
		logger.entering(this.getClass().getName(), "logged(String password)");
		
		Crypt crypt = new Crypt(Parameters.getParameters().getKeyFile());

		File keyFile = new File(Parameters.getParameters().getKeyFile());
		
		if(!keyFile.exists())
			crypt.createKeyFile(/*Parameters.getParameters().getKeyFile()*/);
		
		File passFile = new File(Parameters.getParameters().getPassFile());
		if(!passFile.exists()){
			String p = "abc";
			crypt.encryptFile(Parameters.getParameters().getPassFile(), p);
		}
		
		try {
			String pass = (String) crypt.decryptFile(Parameters.getParameters().getPassFile());
			logger.fine("Read pass: "+pass);
			isLogged = pass.equals(password);
			logger.fine("is logged: " + isLogged);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		logger.exiting(this.getClass().getName(), "logged(String password)");
		return isLogged;	
	}
	/**
	 * Method change password, and write it to file. If old password 
	 * is corrupt - password not change and return false
	 * @param oldPass - old password
	 * @param newPass - new password
	 * @return result of operation
	 * @throws InvalidKeyException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws IOException
	 */
	public boolean changePassword(String oldPass, String newPass) 
		throws InvalidKeyException, NoSuchAlgorithmException, 
		InvalidKeySpecException, NoSuchPaddingException, IOException
	{
		if(logged(oldPass)){
			logger.fine("Passworg changed!");
			Crypt crypt = new Crypt(Parameters.getParameters().getKeyFile());
			crypt.encryptFile(Parameters.getParameters().getPassFile(),
								newPass);
			return true;
		}
		logger.info("Trying changing password");
		return false;
	}
}
