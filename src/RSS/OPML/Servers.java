package RSS.OPML;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.swing.JOptionPane;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import view.tree.TreeCategory;

import controller.Controller;

import RSS.data.Category;
import RSS.data.RSSServer;

/**
 * 
 * Class load list of servers from a file
 * @author Nikolay Klimchuk
 *
 */
public class Servers {
	private Opml opml;
	private JTree tree;


	/**
	 * Constructor. Using 
	 * @param file  - name of OPML file  
	 * @param t - JTree with categories and servers. 
	 * @throws Exception
	 */
	public Servers( File file,  JTree t) throws Exception
	{
		opml = new Opml(file);
		tree = t;
	}
	/**
	 * Analysis of opml file and add founded categories and servers to jTree
	 * @param mtn - parent Tree Node  
	 * @throws Exception
	 */
	public void analiz(DefaultMutableTreeNode mtn) throws Exception
	{
		List<Outline> l = opml.getOutlines();
	
		analiz(l, mtn);
	}
	/**
	 * Recursively method. Convert List of Outlines to tree of servers and categories
	 * @param l
	 * @param mtn
	 * @throws Exception
	 */
	private void analiz(List<Outline> l, DefaultMutableTreeNode mtn) throws Exception
	{
		
		for(Outline o : l)
		{
			if(!o.isComment())
			{
				if(o.getType().equals("rss")|| o.getXmlUrl()!= null)
				{
					addServer(o, mtn);
				} else {
					analiz(o.getOutlines(), addCategory(o, mtn));
				}
			}
		}
	}
	/**
	 * Add server from <i>o</i> to <i>sel</i> 
	 * @param o - server source 
	 * @param sel - server destination
	 */
	private void addServer( Outline o, DefaultMutableTreeNode sel)
	{
		Category cat = ((TreeCategory)sel.getUserObject()).getCategory();
		RSSServer server = new RSSServer(o.getTitle(), cat);
		cat.addServer(server);
		server.setLink(o.getXmlUrl());
		server.setDescription(o.getAttributeValue("description"));
	
		
		   DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
		   	DefaultMutableTreeNode tmp = new DefaultMutableTreeNode(server);
            model.insertNodeInto(tmp, sel, sel.getChildCount());
                
   
	}
	/**
	 * Add category from <i>o</i> to <i>sel</i> 
	 * @param o - category source 
	 * @param sel - category destination
	 */
	private DefaultMutableTreeNode addCategory( Outline o, DefaultMutableTreeNode sel)
	{
		Category cat = ((TreeCategory)sel.getUserObject()).getCategory();
		Category c = new Category(cat);
		c.setName(o.getTitle());
		cat.addChild(c);
		
		DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
		  
	   	DefaultMutableTreeNode tmp = new DefaultMutableTreeNode(new TreeCategory(c));
        model.insertNodeInto(tmp, sel, sel.getChildCount());

		return tmp;
	}
	
}
