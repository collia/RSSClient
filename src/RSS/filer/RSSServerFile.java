package RSS.filer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

import main.RSSLogger;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Text;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import RSS.OPML.Opml;
import RSS.data.Category;
import RSS.data.RSSServer;
/**
 * Class save to file and load from list of servers and categories.
 *
 * File config/servers.xml 
 * 
 * @author Nikolay Klimchuk
 *
 */
public class RSSServerFile {
	private File f;
	private Category rootCategory;
	private Document doc;
	
	private static Logger logger = RSSLogger.loggerRSSFilerServer;
	
	/**
	 * Constructor. 
	 */
	public RSSServerFile()
	{
		f = new File("config/servers.xml");
		logger.fine("Constructor");
	}
	/**
	 * Load Tree category from file
	 * @return - tree category
	 * @throws JDOMException
	 * @throws IOException
	 */
	public Category load() throws JDOMException, IOException{
	
		SAXBuilder build = new SAXBuilder();
		doc = build.build(f);
		Element root = doc.getRootElement();
		
		rootCategory = new Category(null).setName("root");
		
		analiz(root, rootCategory);
		logger.fine("Loading :" + rootCategory.getServers().size());
		return rootCategory;
		
	}
	/**
	 * Write category tree to file
	 * @param writed - root element of category
	 * @throws IOException
	 */
	public void write(Category writed) throws IOException
	{
		doc = new Document();
		Element root = new Element("servers");
		doc.setRootElement(root);
		synthesis(root, writed);
		
		logger.fine("Writing :" + rootCategory.getServers().size());
				
		f.delete();
		f.createNewFile();
		Writer w = new FileWriter(f);
		
		XMLOutputter out = new XMLOutputter();
		Format outFormat = out.getFormat();
		outFormat.setEncoding("Cp1251");
		out.setFormat(outFormat);
		out.output(doc, w);
		w.flush();
		w.close();
	}
	/**
	 * Recursively method convert Category to xml element 
	 * @param root - destination xml Element
	 * @param parent - source Category
	 */
	private void analiz(Element root, Category parent)
	{
		Iterator<Element> servers = root.getChildren("server").iterator();
		while(servers.hasNext())
		{
			Element e = servers.next();
			RSSServer s = new RSSServer(e.getAttributeValue("name"));
			s.setLink(e.getAttributeValue("link"));
			s.setFeedType(e.getAttributeValue("feedType") == null||e.getAttributeValue("feedType").equals("")?
									null : e.getAttributeValue("feedType"));
			s.setLastModified((e.getAttributeValue("lastModify") == null||e.getAttributeValue("lastModify").equals("")? 
									null : new Date(Long.parseLong(e.getAttributeValue("lastModify")))));
			s.setModifyInterval((e.getAttributeValue("updatePeriod") == null||e.getAttributeValue("updatePeriod").equals("")? 
					0 : Long.parseLong(e.getAttributeValue("updatePeriod"))));
			s.setLocalCategory(parent);
			parent.addServer(s);
		}
		
		Iterator<Element> categoryes = root.getChildren("category").iterator();
		while(categoryes.hasNext())
		{
			Element e = categoryes.next();
			Category c = new Category(parent).setName(e.getAttributeValue("name"));
			parent.addChild(c);
			analiz(e, c);
		}
		
			
	}
	/**
	 * Recursively method. Convert xml tree to Category.  
	 * @param root - source xml Element 
	 * @param parent - destination root category 
	 */
	private void synthesis(Element root, Category parent)
	{
		Iterator<RSSServer> servers = parent.getServers().iterator();
		while(servers.hasNext())
		{
			RSSServer s = servers.next();
			Element e = new Element("server");
			e.setAttribute("name", s.getName());
			e.setAttribute("link", s.getLink());
			e.setAttribute("feedType", s.getFeedType()==null?"":s.getFeedType());
			e.setAttribute("lastModify", s.getLastModified()==null?"":Long.toString(s.getLastModified().getTime()));
			e.setAttribute("updatePeriod", Long.toString(s.getModifyInterval()));
			root.addContent(e);
		}
		Iterator<Category> categoryes = parent.getChildren().iterator();
		while(categoryes.hasNext())
		{
			Category c = categoryes.next();
			Element e = new Element("category");
			e.setAttribute("name", c.getName());
			root.addContent(e);
			synthesis(e, c);
			//root.addContent()
		}
		
	}
}
