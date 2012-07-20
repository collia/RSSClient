package RSS.data;

import java.util.HashSet;
import java.util.Set;
import RSS.filer.Filename;

/**
 * Class implements tree structure - category and servers
 * @author Nicolay Klimchuk
 */
public class Category {
	private Category parent;
	
	private Set<Category> children;
	private Set<RSSServer> servers;
	private String name;
	
	/**
	 * Constructor
	 * @param parent - parent category
	 */
	public Category(Category parent)
	{
		this.parent = parent;
		children = new HashSet<Category>();
		servers = new HashSet<RSSServer>();
	}
	/**
	 * add child category
	 * @param cat child category
	 */
	public void addChild(Category cat)
	{
		children.add(cat);
	}
	/**
	 * add child server
	 * @param serv - child server
	 */
	public void addServer(RSSServer serv)
	{
		servers.add(serv);
	}
	/**
	 * parent of local element. If element root return null 
	 * @return
	 */
	public Category getParent()
	{
		return parent;
	}
	/**
	 * Change parent of local element
	 * @param c - new parent
	 */
	public void setParent(Category c)
	{
		parent = c;
	}
	/**
	 * return children of local element 
	 * @return children of local element
	 */
	public Set<Category> getChildren() {
		return children;
	}
	/**
	 * return list of servers in local element
	 * @return
	 */
	public Set<RSSServer> getServers() {
		return servers;
	}

	/**
	 * Set name of local category 
	 * @param name - new name
	 * @return - this
	 */
	public Category setName(String name) {
		this.name = name;
		return this;
	}
	/**
	 * Get local name
	 * @return name
	 */
	public String getName() {
		return name;
	}
	/**
	 * Is has category child
	 * @return is has child
	 */
	public boolean hasChild()
	{
		return !children.isEmpty();
	}
	/**
	 * Remove subserver
	 * @param server server to remove
	 */
	public void removeServer(RSSServer server)
	{
		servers.remove(server);
	}
	/**
	 * Clear all child 
	 */
	public void removeAllServers()
	{
		servers.clear();
	}
	/**
	 * Remove all children
	 */
	public void removeAllChildren(){
		children.clear();
	}
	/**
	 * Remove child
	 * @param child - child to remove
	 */
	public void removeChild(Category child)
	{
		child.removeAllServers();
		child.removeAllChildren();
		child.setParent(null);
		if(!children.remove(child)) System.out.println("ERROR!!!");
	}
	/**
	 * parents not include
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((children == null) ? 0 : children.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
	//	result = prime * result + ((parent == null) ? 0 : parent.hashCode());
		result = prime * result + ((servers == null) ? 0 : servers.hashCode());
		return result;
	}
	/**
	 * parents not compare
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Category other = (Category) obj;
		if (children == null) {
			if (other.children != null)
				return false;
		} else if (!children.equals(other.children))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (parent == null) {
			if (other.parent != null)
				return false;
		} else if (!parent.equals(other.parent))
			return false;
	/*	if (servers == null) {
			if (other.servers != null)
				return false;
		} else if (!servers.equals(other.servers))
			return false;*/
		return true;
	}

	@Override
	public String toString() {
		String n = Filename.convert(name);
		return (parent==null?"":(parent.toString())+"/"+n);
	}
	
	
	
}
