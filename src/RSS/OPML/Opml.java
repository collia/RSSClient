package RSS.OPML;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
/**
 * Class parsing opml files
 * @author Nicolay Klimchuk
 *
 */
public class Opml {
	Element opmlElement;
	
	/**
	 * Constructor
	 * @param f - name of opml file
	 * @throws Exception
	 */
	public Opml(File f) throws Exception{
		if(!f.canRead()) throw new Exception("Error file read!");
		
	    SAXBuilder sb = new SAXBuilder();
	    Document doc = sb.build(f);
	    	//some setup
	    opmlElement = doc.getRootElement();
	    String ver = opmlElement.getAttributeValue("version");
	    if(ver != null && !(Double.parseDouble(ver)>=(1.0)))//equals("2.0"))
	    	throw new Exception("Wrong opml version!");
	    if(opmlElement == null)
	    	throw new Exception("Something is wrong. Can't parse "+f.getName());
	}
	/**
	 * Title of oplm data
	 * @return title
	 */
	public String getTitle() {// throws Exception{
			return opmlElement.getChild("head").getChildTextTrim("title");
	}
	/**
	 * 	Date and time of creation
	 * @return Date and time of creation
	 */
	public String getCreated(){// throws Exception{
			return opmlElement.getChild("head").getChild("dateCreated").getValue();
	}
	/**
	 * Return a date-time, indicating when the document was last modified.
	 * @return a date-time, indicating when the document was last modified.
	 */
	public String getModified(){// throws Exception{
			return opmlElement.getChild("head").getChild("dateModified").getValue();
	}
	/**
	 * Return a string, the owner of the document.
	 * @return string, the owner of the document.
	 */
	public String getOwnerName(){// throws Exception{
			return opmlElement.getChild("head").getChildTextTrim("ownerName");
	}
	/**
	 * Return a string, the email address of the owner of the document.
	 * @return a string, the email address of the owner of the document.
	 */
	public String getOwnerEmail(){// throws Exception{
			return opmlElement.getChild("head").getChildTextTrim("ownerEmail");
	}
	/**
	 * a comma-separated list of line numbers that are expanded. 
	 * The line numbers in the list tell you which headlines to expand.
	 * The order is important. For each element in the list, X, starting  
	 * at the first summit, navigate flatdown X times and expand. 
	 * Repeat for each element in the list.
	 * 
	 * @return comma-separated list of line numbers that are expanded
	 */
	public String getExpansionState(){// throws Exception{
			return opmlElement.getChild("head").getChildTextTrim("expansionState");
	}
	/**
	 * a number, saying which line of the outline is displayed on the top line 
	 * of the window. This number is calculated with the 
	 * expansion state already applied.
	 * @return number, saying which line of the outline is displayed on the top line 
	 * of the window
	 */
	public String getVertScrollState(){// throws Exception{
			return opmlElement.getChild("head").getChildTextTrim("vertScrollState");
	}
	/**
	 * Return a number, the pixel location of the top edge of the window.
	 * @return a number, the pixel location of the top edge of the window.
	 */
	public String getWindowTop(){// throws Exception{
			return opmlElement.getChild("head").getChildTextTrim("windowTop");
	}
	/**
	 * Return a number, the pixel location of the left edge of the window.
	 * @return a number, the pixel location of the left edge of the window.
	 */
	public String getWindowLeft(){// throws Exception{
			return opmlElement.getChild("head").getChildTextTrim("windowLeft");
	}
	/**
	 * Return a number, the pixel location of the bottom edge of the window.
	 * @return a number, the pixel location of the bottom edge of the window.
	 */
	public String getWindowBottom(){// throws Exception{
			return opmlElement.getChild("head").getChildTextTrim("windowBottom");
	}
	/**
	 * Return a number, the pixel location of the right edge of the window.
	 * @return a number, the pixel location of the right edge of the window.
	 */
	public String getWindowRight(){// throws Exception{
			return opmlElement.getChild("head").getChildTextTrim("windowRight");
	}
	/**
	 * an XML element, possibly containing one or more attributes, 
	 * and containing any number of <outline> sub-elements.		
	 * @return list of Outline elements
	 * @throws Exception
	 */
	 public List<Outline> getOutlines() throws Exception{
			 ArrayList<Outline> ret = new ArrayList<Outline>(); 
			 List<Element> elem = opmlElement.getChild("body").getChildren("outline");
			 Iterator<Element> it = elem.iterator();
			 while(it.hasNext()){
				 ret.add(new Outline(it.next()));
			 }
			 return ret;
	 }
	 
	 
	 
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((opmlElement == null) ? 0 : opmlElement.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Opml other = (Opml) obj;
		if (opmlElement == null) {
			if (other.opmlElement != null)
				return false;
		} else if (!opmlElement.equals(other.opmlElement))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Opml [opmlElement=" + opmlElement;
	}
			
	 
}
