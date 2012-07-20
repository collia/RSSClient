package RSS.OPML;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jdom.Element;

/**
 * Class parsing Outline element in opml document
 * @author collia
 *
 */
public class Outline {

	Element outline;
	
	/**
	 * Constructor. Get xml Element.
	 * @see org.jdom.Element
	 * @param next
	 * @throws Exception
	 */
	public Outline(Element next) throws Exception {
		if(next == null) throw new Exception("Next mustn't be null!");
		outline = next;
	}
	/**
	 * text is the string of characters that's displayed when the 
	 * outline is being browsed or edited. 
	 * There is no specific limit on the length of the text attribute.
	 * @return Text parameter
	 */
	public String getText() {
			return outline.getAttributeValue("text");
	}
	/**
	 * Return list of attributes
	 * @return list of attributes
	 */
	public List getAttributes() {
			return outline.getAttributes();
	}
	/**
	 * Return attribute by name  
	 * @param name - name of attribute
	 * @return attribute value
	 */
	public String getAttributeValue(String name){// throws Exception{
			return outline.getAttribute(name).getValue();
	}
	/**
	 * 
	 * @return
	 */
	public String getHtmlUrl(){
			return outline.getAttributeValue("htmlUrl ");
	}
	/**
	 * 
	 * @return
	 */
	public String getTitle(){
			return outline.getAttributeValue("title");
	}
	/**
	 * type is a string, it says how the other attributes of the <outline> are interpreted. 
	 * @return type of outline
	 */
	public String getType(){
			return outline.getAttributeValue("type");
	}
	/**
	 * Get URL to HTML page
	 * @return URL to HTML page
	 */
	public String getUrl(){
			return outline.getAttributeValue("htmlUrl");
	}
	/**
	 * Get URL of RSS feed
	 * @return
	 */
	public String getXmlUrl(){
			return outline.getAttributeValue("xmlUrl");
	}
	/**
	 * isBreakpoint is a string, either "true" or "false", 
	 * indicating whether a breakpoint is set on this outline. 
	 * This attribute is mainly necessary for outlines used to edit scripts that execute. 
	 * If it's not present, the value is false.
	 * 
	 * @return value of isBreakpoint
	 */
	public boolean isBreakpoint(){
			String is = outline.getAttributeValue("isBreakpoint");
			return ((is != null)&&(is.equalsIgnoreCase("true")));
	}  
	/**
	 * isComment is a string, either "true" or "false", 
	 * indicating whether the outline is commented or not. 
	 * By convention if an outline is commented, all subordinate 
	 * outlines are considered to be commented as well. 
	 * If it's not present, the value is false.
	 * 
	 * @return isComment value
	 */
	public boolean isComment(){
			String is = outline.getAttributeValue("isComment");
			return ((is != null)&&(is.equalsIgnoreCase("true")));
	}  
	/**
	 * Get Outline sub-elements 
	 * @return
	 * @throws Exception
	 */
	public List<Outline> getOutlines() throws Exception{// throws Exception{
			 ArrayList<Outline> ret = new ArrayList<Outline>(); 
			 List<Element> elem = outline.getChildren("outline");
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
		result = prime * result + ((outline == null) ? 0 : outline.hashCode());
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
		Outline other = (Outline) obj;
		if (outline == null) {
			if (other.outline != null)
				return false;
		} else if (!outline.equals(other.outline))
			return false;
		return true;
	}
	@Override
	public String toString() {
	
			try {
				return "Outline [ getText()=" + getText() 
						+ ", getOutlines()=" + getOutlines()  + "]";
			} catch (Exception e) {
				e.printStackTrace();
			}
			return "Outline [ getText()=" + getText() + "]";
	
	}
	 
}
