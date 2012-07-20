package RSS.OPML;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import RSS.data.Category;
import RSS.data.RSSServer;

/**
 * Class writing list of servers to OPML file
 * 
 * @author Nikolay Klimchuk
 *
 */
public class OPMLWriter {

	File file;
	Document doc;
	Element body;
	
	/**
	 * Constructor. Open file and create opml head.
	 * @param file - OPML file
	 * @throws IOException
	 */
	public OPMLWriter(File file) throws IOException
	{
		
		this.file = file;
		doc = new Document();
		Element root = new Element("opml");
		root.setAttribute("version","1.0");
		doc.setRootElement(root);
		
		Element head = new Element("head");
		Element headTitle = new Element("title");
			headTitle.addContent("Import from RSSReader");
		Element headOwnerName = new Element("ownerName");
			headOwnerName.addContent("");
		Element headDateModified = new Element("dateModified");
			headDateModified.addContent(new Date().toString());
		List<Element> l1 = new ArrayList();
		l1.add(headTitle);
		l1.add(headOwnerName);
		l1.add(headDateModified);
		head.addContent(l1);
		
		root.addContent(head);
		
		body = new Element("body");
		root.addContent(body);
		
		
	}
	
	/**
	 * Convert Category to list of servers. Get all
	 * servers from selected category and all subcategories 
	 * @param c - root category
	 * @return
	 */
	private List<RSSServer> prepareDate(Category c){
		List<RSSServer> result = new ArrayList<RSSServer>();
		for(RSSServer i : c.getServers())
			result.add(i);
		for(Category i : c.getChildren())
			result.addAll(prepareDate(i));
		return result;
	}
	/**
	 * Get list of all servers in category and all sub
	 * categories, and prepare xml Element to writing 
	 * @param c
	 */
	private void createDocument(Category c){
		List<RSSServer> list = prepareDate(c);
		for(RSSServer i : list)
		{
			Element e = new Element("outline");
			e.setAttribute("type", "rss");
			if(i.getTitle() != null)
				e.setAttribute("title", i.getTitle());
			else if(i.getName() != null)
				e.setAttribute("title", i.getName());
			if(i.getDescription() != null)
				e.setAttribute("description", i.getDescription());
			//e.setAttribute("title", i.getTitle());
			e.setAttribute("xmlUrl", i.getUrl().toString());
			body.addContent(e);
		}
	}
	/**
	 * Get list of all servers in category and all sub
	 * categories, prepare xml Element to writing and 
	 * writing it on file 
	 * @param c - root Category 
	 * @throws IOException
	 */
	public void write(Category c) throws IOException{
		createDocument(c);
		
		file.delete();
		file.createNewFile();
		Writer w = new FileWriter(file);
		
		XMLOutputter out = new XMLOutputter();

		out.output(doc, w);
		w.flush();
		w.close();
	}
	
}
