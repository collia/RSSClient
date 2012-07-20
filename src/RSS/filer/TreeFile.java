package RSS.filer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import main.RSSLogger;

import org.jdom.*;
import org.jdom.input.SAXBuilder;
import org.jdom.output.XMLOutputter;

import RSS.data.RSSEntry;
/**
 * 
 * Class read and write file root.xml in directory with entries
 * @author Nicolay Klimchuk
 *
 */
public class TreeFile {
	private String path="";
	private File root;
	private Document doc;
	
	private static Logger logger = RSSLogger.loggerRSSFilerTreeFile;
	
	/**
	 * Constructor. Get path to root.xml file.
	 * If file don't exist - it creating, 
	 * if directory don't exist it creating  
	 * @param path - path to file
	 * @throws JDOMException
	 * @throws IOException
	 */
	public TreeFile(String path) throws JDOMException, IOException{
		
		logger.entering(this.getClass().getName(), 
				"Constructor");
		
		this.path = path;
		root = new File("doc/"+this.path+"/root.xml");
		
		logger.fine("Load file: " + root.getAbsolutePath());
		
		if(!root.exists())
		{
			File directory = new File("doc/"+this.path+"/");
		
			logger.fine("Open/create directory: " + directory.getAbsolutePath());
			
			if(!directory.exists())
				directory.mkdirs();
				
			root.createNewFile();
			FileWriter fw = new FileWriter(root);
			
			fw.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
					 "<data>\n"+"</data>");
			fw.close();
		}
		SAXBuilder builder = new SAXBuilder();
		doc = builder.build(root);
		
		logger.entering(this.getClass().getName(), 
			"Constructor");
	}

	/**
	 * Return <i>*.xml</i> filenames, in which saving <i>numbers</i> entries 
	 * @param numbers - number entries user want to read  
	 * @param encrypted - if true - load <i>*.crt</i> files too,
	 * if false don't load  <i>*.crt</i> files.
	 * @return list of filenames, in which saving <i>numbers</i> entries 
	 */
	public List<String> getFilenames(int numbers, boolean encrypted){
		
		logger.entering(this.getClass().getName(), "getFilenames(int numbers, boolean encrypted)");
		
		ListIterator<Element> itl = doc.getRootElement().getChildren("file").listIterator();
		
		List<String> files = new ArrayList<String>();
		int currentNumbers = 0;
		while(itl.hasNext())
			itl.next();
		while(itl.hasPrevious()){
			Element e = itl.previous();//it.next(); 
			
			logger.fine("Loading: " + e.getAttributeValue("filename"));
			
			currentNumbers += Integer.parseInt(
					e.getAttributeValue("number")
					);
			if(e.getAttributeValue("encrypt").equalsIgnoreCase("false") || encrypted)
				files.add(e.getAttributeValue("filename"));
			if((numbers != -1) && (currentNumbers >= numbers)) break;
		}
		
		logger.exiting(this.getClass().getName(), "getFilenames(int numbers, boolean encrypted)");
		return files;
	}
	/**
	 * Return all <i>*.xml</i> filenames, written in root.xml  
	 * @param encrypted - if true loading  <i>*.crt</i> files too
	 * @return all filenames, written in root.xml  
	 */
	public List<String> getAllFilenames(boolean encrypted)// throws JDOMException, IOException
	{
		logger.entering(this.getClass().getName(), "getAllFilenames(boolean encrypted)");
		
		ListIterator<Element> itl = doc.getRootElement().getChildren("file").listIterator();
		while(itl.hasNext())
			itl.next();
		
		List<String> files = new ArrayList<String>();
		
		while(itl.hasPrevious()){
			Element e = itl.previous(); 
			if(e.getAttribute("encrypt").getValue().equalsIgnoreCase("false") || encrypted){
				files.add(e.getAttributeValue("filename"));
				logger.fine("Loading: " + e.getAttributeValue("filename"));
			}
		}
		
		logger.exiting(this.getClass().getName(), "getAllFilenames(boolean encrypted)");
		return files;
	}

	/**
	 * Return list of <i>*.xml</i> filenames, in which written <i>numbers</i> entries 
	 * @param numbers - number of entries
	 * @return list of filenames, in which written <i>numbers</i> entries
	 */
	public List<String> getFilenames(int numbers) //throws JDOMException, IOException
	{
		return  getFilenames(numbers, false);
	}
	/**
	 * Return list of all <i>*.xml</i> filenames
	 * @return list of all <i>*.xml</i> filenames
	 */
	public List<String> getAllFilenames()// throws JDOMException, IOException 
	{
		return getAllFilenames(false);
	}
	/**
	 * Return number of feeds, marked us not read 
	 * @param filenames - list of filenames, in which search not read entryes
	 * @return map - filename, list numbers of feeds, marked us not read
	 */
	public Map<String,Set<Integer>> getNumberNotReaded(List<String> filenames)
	{
		ListIterator<Element> itl = doc.getRootElement().getChildren("file").listIterator();
		
			
		Set<Integer> numbers = new HashSet<Integer>();
		Map<String,Set<Integer>> result = new HashMap<String,Set<Integer>>();
		
		while(itl.hasNext())
		{
			Element e = itl.next();//itl.previous();//it.next();
			String filename = e.getAttributeValue("filename");
			
			if(filenames.contains(filename))
			{
				String notReaded = e.getAttributeValue("notReaded");
				if(notReaded.equals(""))
				{
					numbers = new HashSet<Integer>();
					result.put(filename, numbers);
				} else {
					String[] n = notReaded.split(",");
					numbers = new HashSet<Integer>();
					for(String i : n)
					{
						numbers.add(Integer.parseInt(i));
						result.put(filename, numbers);
					}
				}
				logger.fine("Not read from " + filename + ": " + numbers);
			}
		}
		return result;
	}
	/**
	 * Save information of new written entries to <i>root.xml</i>.  
	 * @param data - written entries 
	 * @param number - number of file in series
	 * @param encrypted - if encrypted file with entries 
	 * @return - filename, in which writing entries 
	 * @throws IOException
	 */
	public String writeFeeds(List<RSSEntry> data, int number, boolean encrypted) throws IOException
	{
		int size = data.size();
		String notReaded = "";
		for(int i = 0; i < size; i++)
		{
			if(!data.get(i).isReaded())
			{
				if(notReaded.equals(""))
					notReaded = Integer.toString(i);
				else
					notReaded = notReaded+","+Integer.toString(i);
			}
		}
		Element e = new Element("file");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
		String filename = sdf.format(new Date())+"_"+number+ (encrypted?".crt":".xml");
		e.setAttribute("filename", filename);
		e.setAttribute("encrypt", Boolean.toString(encrypted));
		e.setAttribute("number", Integer.toString(size));
		e.setAttribute("notReaded", notReaded);
		
		doc.getRootElement().addContent(e);
		
		Writer w = new FileWriter(root);
		XMLOutputter out = new XMLOutputter();
		out.output(doc, w);
	
		logger.fine("Save to file: " + filename);
		
		return filename;
	}
	/**
	 * Remove filename from list in <i>root.xml</i>
	 * @param filename - name of removed file
	 * @param encrypted - is file <i>*.crt</i>
	 * @throws IOException
	 */
	public void removeFeeds(String filename, boolean encrypted) throws IOException
	{
		logger.fine("Remove feeds: "+ filename);
		List<String> files = getFilenames(-1, encrypted);
		
		if(files.contains(filename))
		{
			List<Element> e = doc.getRootElement().getChildren("file");
			Iterator<Element> iterator = e.iterator();
			while(iterator.hasNext())
			{
				Element localElement = iterator.next();
				if(localElement.getAttributeValue("filename").equalsIgnoreCase(filename))
				{
					 doc.getRootElement().removeContent(localElement);
					 break;
				}
			}
			
			Writer w = new FileWriter(root);
			XMLOutputter out = new XMLOutputter();
			out.output(doc, w);
			
		}
	}
	/**
	 * Mark some entries as read.
	 * 
	 * @param numbers - Map (filename, Set( numbers of read entries))
	 * @throws IOException
	 */
	public void markUsReaded(Map<String,Set<Integer>> numbers) throws IOException
	{
		List<String> files = getFilenames(-1, true);
		
		HashSet<String> numbersNotReaded = new HashSet<String>();
		
		Iterator<String> itFiles = numbers.keySet().iterator();	
		while (itFiles.hasNext())
		{
			String file = itFiles.next();
			if(files.contains(file))
			{
				List<Element> e = doc.getRootElement().getChildren("file");
				Iterator<Element> iterator = e.iterator();
				while(iterator.hasNext())
				{
					Element localElement = iterator.next();
					if(localElement.getAttributeValue("filename").equalsIgnoreCase(file))
					{
						String notReaded = localElement.getAttributeValue("notReaded");
						if(notReaded.equals(""))
						{
						} else {
							String[] n = notReaded.split(",");
							numbersNotReaded = new HashSet<String>();
							for(String i : n)
							{
								if(!numbers.get(file).contains(Integer.parseInt(i)))
								{
									numbersNotReaded.add(i);
								}
							}
							String strNumbersNotReaded = new String();
							Iterator<String> itStr = numbersNotReaded.iterator();
							if(itStr.hasNext()) strNumbersNotReaded = itStr.next();
							while(itStr.hasNext())
								strNumbersNotReaded += ","+itStr.next();
							localElement.setAttribute("notReaded", strNumbersNotReaded);
							logger.fine("Mark as read in file: " + file + "-" + strNumbersNotReaded);
						}
					}
				}
				
				Writer w = new FileWriter(root);
				XMLOutputter out = new XMLOutputter();
				out.output(doc, w);
				
			}
		}
	}
	
}
