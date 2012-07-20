package view.tree;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;

import org.jdom.Element;
import org.jdom.JDOMException;

import view.html.TextPanel;

import RSS.OPML.OPMLWriter;
import RSS.OPML.Servers;
import RSS.data.Category;
import RSS.data.RSSServer;

import controller.Controller;
import controller.Parameters;

/**
 * Class implements JPanel with tree categories and servers
 * 
 * @author Nicolay Klimchuk
 *
 */

public class TreePanel extends JPanel {
	
	private static final long serialVersionUID = 1L;
	Controller controller;
    protected JTree             tree;
    /** Tree model. */
    protected DefaultTreeModel        treeModel;
    private Category rootComponent;
	
    private TextPanel textPanel;
    
	/**
	 * Constructor. Get instance of Controller and TextPanel
	 * @param controller - instance of Controller
	 * @param tp - instance of TextPanel
	 */
    public TreePanel(Controller controller, TextPanel tp)
	{
		this.controller = controller;
		textPanel = tp;
		try {
			rootComponent = controller.getServers();
		} catch (JDOMException e) {
			e.printStackTrace();
			JOptionPane error = new JOptionPane("Error " + e.getMessage(),
												JOptionPane.ERROR_MESSAGE);
			error.setVisible(true);
		} catch (IOException e) {
			e.printStackTrace();
			JOptionPane error = new JOptionPane("Error " + e.getMessage(),
					JOptionPane.ERROR_MESSAGE);
			error.setVisible(true);
		}
		/* Create the JTreeModel. */
		DefaultMutableTreeNode root = createNewNode();
		treeModel = new SampleTreeModel(root, rootComponent);

		
		/* Create the tree. */
		tree = new JTree(treeModel);
		tree.setRootVisible(false);
		
		/* Enable tool tips for the tree, without this tool tips will not
		   be picked up. */
		ToolTipManager.sharedInstance().registerComponent(tree);

		/* Make tree ask for the height of each row. */
		tree.setRowHeight(-1);

		/* Put the Tree in a scroller. */
		JScrollPane        sp = new JScrollPane();
		sp.setPreferredSize(new Dimension(300, 300));
		sp.getViewport().add(tree);

		/* And show it. */
		this.setLayout(new BorderLayout());
		this.add("Center", sp);

		 MouseListener popupListener = new PopupListener(buildPopupMainMenu(), buildPopupCategoryMenu());
	     tree.addMouseListener(popupListener);
	
		this.setPreferredSize(new Dimension(100,150));
		ImageIcon iconServ = createImageIcon("28.png","");
		ImageIcon iconCat = createImageIcon("DossierJaune2.png","");
		ImageIcon iconCatSel = createImageIcon("DossierBleu Documents2.png","");
		ImageIcon iconCatSelEmpty = createImageIcon("DossierBleu2.png","");
		if (iconServ != null && iconCat!= null && iconCatSel != null && iconCatSelEmpty != null) {
			MyTreeCellRenderer renderer = new MyTreeCellRenderer(iconCat,iconCatSel, iconCatSelEmpty,iconServ);
			tree.setCellRenderer(renderer);
		    
		}
		this.setMinimumSize(new Dimension(200,200));
			
	}
	/**
	 * Method create MutableTreeNode with tree of severs
	 * @return MutableTreeNode with tree of severs
	 */
	protected DefaultMutableTreeNode createNewNode(/*String name*/) {
		    	DefaultMutableTreeNode dfmtn = new DefaultMutableTreeNode("root");
		    	
		    	if(rootComponent != null)
		    	{
		    		buildTree(dfmtn, rootComponent);
		    	}
		    	return dfmtn;
		    }

	/**
	 * Recursively method - convert Category <i>parent</i> to 
	 * tree in DefaultMutableTreeNode
	 * @param root - destination of elements
	 * @param parent - source of elements
	 */
	 private void buildTree(DefaultMutableTreeNode root, Category parent)
	   {
		   Iterator<RSSServer> servers = parent.getServers().iterator();
			while(servers.hasNext())
			{
				RSSServer s = servers.next();
				DefaultMutableTreeNode a = new DefaultMutableTreeNode(s);
				root.add(a);
		    	
			}
			Iterator<Category> categoryes = parent.getChildren().iterator();
			while(categoryes.hasNext())
			{
				Category c = categoryes.next();
				DefaultMutableTreeNode a = new DefaultMutableTreeNode(new TreeCategory(c));
				root.add(a);
				buildTree(a,c);
			}
			
	   }
	   /**
	    * Method build popup menu
	    * @return - popup menu
	    */
	   private JPopupMenu buildPopupMainMenu(){
		   JPopupMenu menu = new JPopupMenu();
		
		   JMenuItem popupNewCategory, popupNewServer, popupUpdate, popupRemove, 
		   				popupRename, popupShowLast, popupShowAll, popupNotRead;
		   JMenu submenu;
			menu.add( popupNewCategory = new JMenuItem("New Category"));
			menu.add( popupNewServer = new JMenuItem("New Server"));
			menu.addSeparator();
			submenu = new JMenu("Entryes");
			submenu.add( popupUpdate = new JMenuItem("Update"));
			submenu.add( popupShowLast = new JMenuItem("Show last..."));
			submenu.add( popupShowAll = new JMenuItem("Show All"));
			submenu.add( popupNotRead = new JMenuItem("Show not readed"));
			menu.add(submenu);
			menu.add( popupRename = new JMenuItem("Rename..."));
			menu.addSeparator();
			menu.add( popupRemove = new JMenuItem("Remove"));
			
			
			popupNewServer.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					addNewServer();
				}
			});
			popupNewCategory.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					addNewCategory();
				}
			});

			popupRemove.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					removeCategory();
				}
			});
			popupUpdate.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					updateCategory();
				}
				
			});
			popupRename.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					//controller.renameCategory(oldName, newName)
					renameCategory();
				}
			});
			popupShowLast.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					//controller.renameCategory(oldName, newName)
					//renameCategory();
					showLastRSSCategory();
				}
			});
			popupShowAll.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					//controller.renameCategory(oldName, newName)
					//renameCategory();
					categoryShowAllRSS();
				}
			});
			popupNotRead.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					//controller.renameCategory(oldName, newName)
					//renameCategory();
					//categoryShowAllRSS();
					categoryShowNotReadedRSS();
				}
			});
		   return menu;
	   }
	   
	 /**
	  * Get selected category and update all subservers in it
	  * But subcategories is't updating  
	  */
	private void updateCategory() {
		Category c = ((TreeCategory)((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject()).getCategory();
		List<RSSServer> servList = new ArrayList<RSSServer>();
		servList.addAll(c.getServers());
		textPanel.updateRSS(servList);
	}
	/**
	 * Load new entries for server and show it
	 */
	private void updateServer() {
		RSSServer server = (RSSServer)(((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject());
		textPanel.updateRSS(server);
		server.setLastModified(new Date());
	
	}
	/**
	 * Download and show new entries
	 */
	public void updateAllServers() {
		List<RSSServer> servers = controller.getServersList();
		textPanel.updateRSS(servers);
	}

	/**
	 * Ask user new name and rename selected category
	 */
	private void renameCategory(){
		Category c = ((TreeCategory)((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject()).getCategory();
		String newName = JOptionPane.showInputDialog(
				   null, 
				   "Insert new category name?", 
				   "Input", 
				   JOptionPane.OK_CANCEL_OPTION);
		if(newName != null)
		{
			controller.renameCategory(c, newName);
			c.setName(newName);
			
			try {
				controller.writeServers();
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, 
 			   			"alert", "Could not write file", 
 			   			JOptionPane.ERROR_MESSAGE);
			}
		}
	}
	/**
	 * Build popup menu to category 
	 * @return new popup menu
	 */
	private JPopupMenu buildPopupCategoryMenu(){
		   JPopupMenu menu = new JPopupMenu();
		
		   JMenuItem  popupUpdate, popupNotReaded, popupShowLast, 
		   				popupRemove, popupParameters, popupShowAll;
			menu.add( popupUpdate = new JMenuItem("Update"));
			menu.add( popupNotReaded = new JMenuItem("Show not readed"));
			menu.add( popupShowLast = new JMenuItem("Show last..."));
			menu.add( popupShowAll = new JMenuItem("Show all"));
			menu.addSeparator();
			menu.add( popupRemove = new JMenuItem("Remove"));
			menu.addSeparator();
			menu.add( popupParameters = new JMenuItem("Parameters"));
			
			popupRemove.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					removeServer();
					try {
						controller.writeServers();
					} catch (IOException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, 
	     			   			"alert", "Could not write file", 
	     			   			JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			popupUpdate.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					updateServer();
					try {
						controller.writeServers();
					} catch (IOException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, 
	     			   			"alert", "Could not write file", 
	     			   			JOptionPane.ERROR_MESSAGE);
					}
				}
			});
			//showServerProperties
			popupParameters.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					showServerProperties();
					try {
						controller.writeServers();
					} catch (IOException e) {
						e.printStackTrace();
						JOptionPane.showMessageDialog(null, 
	     			   			"alert", "Could not write file", 
	     			   			JOptionPane.ERROR_MESSAGE);
					}
				}
			});
	
			popupNotReaded.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					showNotReadedRSS();
				}
			});
			popupShowLast.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					String strNumber = JOptionPane.showInputDialog(
							   null, 
							   "How much do you want read feeds?", 
							   "Confirm", 
							   JOptionPane.OK_CANCEL_OPTION);
					try{
						int number = Integer.parseInt(strNumber);
						 RSSServer server = (RSSServer)(((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject());
						 textPanel.openRSS(server, number);
					}catch(NumberFormatException e)
					{
						JOptionPane.showMessageDialog(null, 
								strNumber+" is not valid number", 
								"Error!", 
								JOptionPane.ERROR_MESSAGE);
					}

				}
			});
			popupShowAll.addActionListener(new ActionListener(){
				@Override
				public void actionPerformed(ActionEvent arg0) {
					showAllRSS();
				}
			});
		   return menu;
	   }

	/**
	 * Add new server to category
	 */
	   public void addNewServer()
	    {
		   if(((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject() instanceof TreeCategory)
		   {
		   Category c = ((TreeCategory)((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject()).getCategory();
		   DialogNewServer dns = new DialogNewServer(c);
		   dns.setVisible(true);
		   RSSServer server = dns.getServer();
		   if(server != null){
			   DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
			   Object obj = tree.getLastSelectedPathComponent();
			   if(obj!=null)
			   {
				   	DefaultMutableTreeNode sel = (DefaultMutableTreeNode)obj;
	            
				   	DefaultMutableTreeNode tmp = new DefaultMutableTreeNode(server);
	                model.insertNodeInto(tmp, sel, sel.getChildCount());
	                
	                try {
	     			   controller.writeServers();
	     		   } catch (IOException e) {
	     			   	JOptionPane.showMessageDialog(this, 
	     			   			"alert", "Could not write file", 
	     			   			JOptionPane.ERROR_MESSAGE);
	     				e.printStackTrace();
	     			}
		   		}
		   	}
		   }
	    }
	   /**
	    * Add new category
	    */
	   public void addNewCategory()
	    {
		   if(((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject() instanceof TreeCategory)
		   {
		   Category parent = ((TreeCategory)((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject()).getCategory();
		   String s = (String)JOptionPane.showInputDialog(
                   this,
                   "Name: ",
                   "New category"
                  );

		   if(s != null){
			   DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
			   Object obj = tree.getLastSelectedPathComponent();
			   if(obj!=null)
			   {
				   Category c = new Category(parent);
				   c.setName(s);
				   parent.addChild(c);
				   try {
					   controller.writeServers();
				   } catch (IOException e) {
					   	JOptionPane.showMessageDialog(this, 
					   			"alert", "Could not write file", 
					   			JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
				   DefaultMutableTreeNode sel = (DefaultMutableTreeNode)obj;
	            
				   	DefaultMutableTreeNode tmp = new DefaultMutableTreeNode(new TreeCategory(c));
	                model.insertNodeInto(tmp, sel, sel.getChildCount());
			   }
		   	}
		   }
	    }
	   /**
	    * add new root category
	    */
	   public void addRootcategory(){
		   String newWord = JOptionPane.showInputDialog(
				   null, 
				   "Input name of new category: ", 
				   "Input", 
				   JOptionPane.OK_CANCEL_OPTION);
		if(newWord != null)
		{
			try {
				Category root = controller.getServers();
				
				Category cat = new Category(root).setName(newWord);
				root.addChild(cat);
				
				DefaultMutableTreeNode tmp = new DefaultMutableTreeNode(new TreeCategory(cat));
				 DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
				  
				 DefaultMutableTreeNode sel = (DefaultMutableTreeNode)model.getRoot();
		            
                model.insertNodeInto(tmp, sel, sel.getChildCount());

				
				
				controller.writeServers();
			} catch (JDOMException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, 
 			   			 "Error on read file", "alert",
 			   			JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				e.printStackTrace();
				JOptionPane.showMessageDialog(null, 
 			   			 "Error on read file", "alert",
 			   			JOptionPane.ERROR_MESSAGE);
			}
			
		}
	   }
	   /**
	    * Remove servers
	    */
	   public void removeServer(){
		   if(((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject() instanceof RSSServer)
		   {
		   RSSServer server = (RSSServer)(((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject());
		   int isDelete = JOptionPane.showConfirmDialog(
				   this, 
				   "Do you realy want to delete this server?", 
				   "Confirm", 
				   JOptionPane.OK_CANCEL_OPTION);
				   					
                   

		   if(isDelete == JOptionPane.OK_OPTION){
			   Category parent = server.getLocalCategory();
			   parent.removeServer(server);
			   
			   try {
				   controller.writeServers();
			   } catch (IOException e) {
				   	JOptionPane.showMessageDialog(this, 
				   			"alert", "Could not write file", 
				   			JOptionPane.ERROR_MESSAGE);
					e.printStackTrace();
				}
			   DefaultTreeModel model = (DefaultTreeModel)tree.getModel();
			   Object obj = tree.getLastSelectedPathComponent();
			   if(obj!=null)
			   {
				   	DefaultMutableTreeNode sel = (DefaultMutableTreeNode)obj;
				   	model.removeNodeFromParent(sel);
		   		
		   	}
		   }
		   }
	   }
	   /**
	    * Showing not read entries 
	    */
	   private void showNotReadedRSS()
       {
		   RSSServer server = (RSSServer)(((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject());
			textPanel.showNotReadRSS(server);
       }
	   /**
	    * Showing not read entryes by category
	    */
	   private void categoryShowNotReadedRSS()
       {
		   	 Category parent = ((TreeCategory)((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject()).getCategory();
		   	 List<RSSServer> s = new ArrayList<RSSServer>();
			 s.addAll(parent.getServers());
			 textPanel.showNotReadRSS(s);
       }
	   /**
	    * Showing all entries by all servers
	    */
	   public void showAllRSS()
       {
		   List<RSSServer> servers = controller.getServersList();
		   textPanel.showAllRSS(servers);
       }
	   /**
	    * Showing all not read entries by all servers
	    */
	   public void showAllRSSNotRead()
       {
		  // RSSServer server = (RSSServer)(((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject());
		   List<RSSServer> servers = controller.getServersList();
		   textPanel.showNotReadRSS(servers);
       }
	   /**
	    * Showing all not read entries by all servers in the category
	    */
	   private void categoryShowAllRSS()
       {
		  // RSSServer server = (RSSServer)(((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject());
		    Category parent = ((TreeCategory)((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject()).getCategory();
		    List<RSSServer> s = new ArrayList<RSSServer>();
			 s.addAll(parent.getServers());
		    textPanel.showAllRSS(s);
       }
	   /**
	    * Load from file and show in HTML panel last rss feeds by selected server
	    */
	   private void showLastRSSCategory()
	   {
		   String strNumber = JOptionPane.showInputDialog(
				   null, 
				   "How much do you want read feeds?", 
				   "Confirm", 
				   JOptionPane.OK_CANCEL_OPTION);
		try{
			int number = Integer.parseInt(strNumber);
			 Category showing = ((TreeCategory)((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject()).getCategory();
			 List<RSSServer> s = new ArrayList<RSSServer>();
			 s.addAll(showing.getServers());
			 textPanel.openRSS(s, number);
		}catch(NumberFormatException e)
		{
			JOptionPane.showMessageDialog(null, 
					strNumber+" is not valid number", 
					"Error!", 
					JOptionPane.ERROR_MESSAGE);
		}

	   }
	   /**
	    * Load from file and show in HTML panel all rss feeds by selected server
	    */
	   public void showAllLastRSSCategory()
	   {
		   String strNumber = JOptionPane.showInputDialog(
				   null, 
				   "How much do you want read feeds?", 
				   "Confirm", 
				   JOptionPane.OK_CANCEL_OPTION);
		   try{
			   int number = Integer.parseInt(strNumber);
			   List<RSSServer> servers = controller.getServersList();
			   textPanel.openRSS(servers, number);
		   }catch(NumberFormatException e)
		   {
			   JOptionPane.showMessageDialog(null, 
					strNumber+" is not valid number", 
					"Error!", 
					JOptionPane.ERROR_MESSAGE);
		   }

	   }
	   /**
	    * 
	    */
	   private void showServerProperties(){
		   RSSServer server = (RSSServer)(((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject());
		   JDialog dsp = new DialogServerProperties(server, controller);
		   dsp.setVisible(true);
	   }
	   public void removeCategory() {
		   if(((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject() instanceof  TreeCategory)
		   {
		   Category removed = ((TreeCategory)((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject()).getCategory();
			int isDelete = JOptionPane.showConfirmDialog(
				   this, 
				   "Do you realy want to delete all servers in this category?", 
				   "Confirm", 
				   JOptionPane.OK_CANCEL_OPTION);
				   					
                   

			   if(isDelete == JOptionPane.OK_OPTION){
				   removed.getParent().removeChild(removed);
				   
			//	   removed.getParent().removeChild(removed);
				   try {
					   controller.writeServers();
				   } catch (IOException e) {
					   	JOptionPane.showMessageDialog(this, 
					   			"alert", "Could not write file", 
					   			JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}

				   
		/*		   try {
					   controller.writeServers();
				   } catch (IOException e) {
					   	JOptionPane.showMessageDialog(this, 
					   			"alert", "Could not write file", 
					   			JOptionPane.ERROR_MESSAGE);
						e.printStackTrace();
					}
*/
				   TreePath[] selected = tree.getSelectionPaths();

		            if (selected != null && selected.length > 0) {
		                TreePath shallowest;

		                // The remove process consists of the following steps:
		                // 1 - find the shallowest selected TreePath, the shallowest
		                //     path is the path with the smallest number of path
		                //     components.
		                // 2 - Find the siblings of this TreePath
		                // 3 - Remove from selected the TreePaths that are descendants
		                //     of the paths that are going to be removed. They will
		                //     be removed as a result of their ancestors being
		                //     removed.
		                // 4 - continue until selected contains only null paths.
		                while ((shallowest = findShallowestPath(selected)) != null) {
		                    removeSiblings(shallowest, selected);
		                }
		            }
		            }
		   }
		  
		   }
		     /**
	         * Removes the sibling TreePaths of <code>path</code>, that are
	         * located in <code>paths</code>.
	         */
	      private void removeSiblings(TreePath path, TreePath[] paths) {
	            // Find the siblings
	            if (path.getPathCount() == 1) {
	                // Special case, set the root to null
	                for (int counter = paths.length - 1; counter >= 0; counter--) {
	                    paths[counter] = null;
	                }
	                treeModel.setRoot(null);
	            }
	            else {
	                // Find the siblings of path.
	                TreePath parent = path.getParentPath();
	                MutableTreeNode parentNode = (MutableTreeNode)parent.
	                                getLastPathComponent();
	                ArrayList toRemove = new ArrayList();
	                int depth = parent.getPathCount();

	                // First pass, find paths with a parent TreePath of parent
	                for (int counter = paths.length - 1; counter >= 0; counter--) {
	                    if (paths[counter] != null && paths[counter].
	                              getParentPath().equals(parent)) {
	                        toRemove.add(paths[counter]);
	                        paths[counter] = null;
	                    }
	                }

	                // Second pass, remove any paths that are descendants of the
	                // paths that are going to be removed. These paths are
	                // implicitly removed as a result of removing the paths in
	                // toRemove
	                int rCount = toRemove.size();
	                for (int counter = paths.length - 1; counter >= 0; counter--) {
	                    if (paths[counter] != null) {
	                        for (int rCounter = rCount - 1; rCounter >= 0;
	                             rCounter--) {
	                            if (((TreePath)toRemove.get(rCounter)).
	                                           isDescendant(paths[counter])) {
	                                paths[counter] = null;
	                            }
	                        }
	                    }
	                }

	                // Sort the siblings based on position in the model
	                if (rCount > 1) {
	                    Collections.sort(toRemove, new PositionComparator());
	                }
	                int[] indices = new int[rCount];
	                Object[] removedNodes = new Object[rCount];
	                for (int counter = rCount - 1; counter >= 0; counter--) {
	                    removedNodes[counter] = ((TreePath)toRemove.get(counter)).
	                                getLastPathComponent();
	                    indices[counter] = treeModel.getIndexOfChild
	                                        (parentNode, removedNodes[counter]);
	                    parentNode.remove(indices[counter]);
	                }
	                treeModel.nodesWereRemoved(parentNode, indices, removedNodes);
	            }
	        }

	        /**
	         * Returns the TreePath with the smallest path count in
	         * <code>paths</code>. Will return null if there is no non-null
	         * TreePath is <code>paths</code>.
	         */
	        private TreePath findShallowestPath(TreePath[] paths) {
	            int shallowest = -1;
	            TreePath shallowestPath = null;

	            for (int counter = paths.length - 1; counter >= 0; counter--) {
	                if (paths[counter] != null) {
	                    if (shallowest != -1) {
	                        if (paths[counter].getPathCount() < shallowest) {
	                            shallowest = paths[counter].getPathCount();
	                            shallowestPath = paths[counter];
	                            if (shallowest == 1) {
	                                return shallowestPath;
	                            }
	                        }
	                    }
	                    else {
	                        shallowestPath = paths[counter];
	                        shallowest = paths[counter].getPathCount();
	                    }
	                }
	            }
	            return shallowestPath;
	        }

	        /**
	         * An Comparator that bases the return value on the index of the
	         * passed in objects in the TreeModel.
	         * <p>
	         * This is actually rather expensive, it would be more efficient
	         * to extract the indices and then do the comparision.
	         */
	        private class PositionComparator implements Comparator {
	            public int compare(Object o1, Object o2) {
	                TreePath p1 = (TreePath)o1;
	                int o1Index = treeModel.getIndexOfChild(p1.getParentPath().
	                          getLastPathComponent(), p1.getLastPathComponent());
	                TreePath p2 = (TreePath)o2;
	                int o2Index = treeModel.getIndexOfChild(p2.getParentPath().
	                          getLastPathComponent(), p2.getLastPathComponent());
	                return o1Index - o2Index;
	            }
	        }
	   /**
	    * Class realization program reaction to mouse
	    * @author Nicolay Klimchuk
	    */
	   class PopupListener extends MouseAdapter {
	        JPopupMenu popup;
	        JPopupMenu popupCategory;

	        PopupListener(JPopupMenu popupMenu, JPopupMenu popupCategory) {
	            popup = popupMenu;
	            this.popupCategory = popupCategory;
	        }

	        public void mousePressed(MouseEvent e) {
	            maybeShowPopup(e);
	            showRSS(e);
	        }

	        public void mouseReleased(MouseEvent e) {
	            maybeShowPopup(e);
	        }

	        private void maybeShowPopup(MouseEvent e) {
	            if (e.isPopupTrigger()) {
	            		int selRow = tree.getRowForLocation(e.getX(), e.getY());
	            		if (selRow<0)
	            		return;

	            		TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
	            		tree.setSelectionPath(selPath);

	            		//if(((DefaultMutableTreeNode)selPath.getLastPathComponent()).isLeaf())
	            		if(((DefaultMutableTreeNode)selPath.getLastPathComponent()).getUserObject() instanceof TreeCategory)
	            			popup.show(e.getComponent(),
			                           e.getX(), e.getY());
		            	else
		            		popupCategory.show(e.getComponent(),
			                           e.getX(), e.getY());
	            }
	        }
	        private void showRSS(MouseEvent e)
	        {
	        	TreePath selPath = tree.getPathForLocation(e.getX(), e.getY());
        		tree.setSelectionPath(selPath);
	        	 if(e.getButton() == MouseEvent.BUTTON1){
	        		 if(selPath != null)
	        		 if(((DefaultMutableTreeNode)selPath.getLastPathComponent()).getUserObject() instanceof RSSServer)
	        		 {
	        			 RSSServer ser = (RSSServer)((DefaultMutableTreeNode)selPath.getLastPathComponent()).getUserObject();
	        			 
	        			 textPanel.openRSS(ser);
	        			 
	        		 }
			             
		          }
	        }
	       
	    }
	   /**
	    * Method open image and create icon
	    * @param filename - filename of image
	    * @param description - description of image
	    * @return - new ImageIcon
	    */
	   public ImageIcon createImageIcon(String filename, String description) {
		    String path = "/resources/images/" + filename;
		    ImageIcon ii = new ImageIcon(getClass().getResource(path), description);
		    return ii;
	    } 
	   /**
	    * Open OPML file and add all servers to tree
	    * @throws JDOMException - bad xml syntax 
	    * @throws IOException - can't read file
	    * @throws Exception - other exception
	    */
	   public void addOPML() throws JDOMException, IOException, Exception
	   {
		   
			  
			   JFileChooser fc = new JFileChooser(); 
			   int result = fc.showOpenDialog(this);
			   if(result == JFileChooser.APPROVE_OPTION) {
				   if(((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject() instanceof TreeCategory)
				   {
					   Category parent = ((TreeCategory)((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject()).getCategory();
						  
					   Servers s = //new Servers(controller,fc.getSelectedFile(), parent, tree);
						   new Servers(fc.getSelectedFile(), tree);
					   s.analiz((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent());
					   controller.writeServers();
				   } else {
					   
					   Servers s = //new Servers(controller,fc.getSelectedFile(), controller.getServers(), tree);
						   new Servers(fc.getSelectedFile(), tree);
					   s.analiz((DefaultMutableTreeNode)tree.getModel().getRoot());
					   controller.writeServers();
				   }
			   }
	   }
	   /**
	    * method ask filename and export xml list to opml file
	    * @throws JDOMException - bad xml syntax 
	    * @throws IOException - can't write file
	    * @throws Exception - other exceptions
	    */
	   public void saveOPML() throws JDOMException, IOException, Exception
	   {
			  
			   JFileChooser fc = new JFileChooser(); 
			   // show the filechooser
			   int result = fc.showSaveDialog(this);
			   if(result == JFileChooser.APPROVE_OPTION) {
				   if(((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject() instanceof TreeCategory)
				   {
					   Category parent = ((TreeCategory)((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject()).getCategory();
					   OPMLWriter wr = new OPMLWriter(fc.getSelectedFile());
					   wr.write(parent);
				   } else {
					   Category parent = controller.getServers();//((TreeCategory)((DefaultMutableTreeNode)tree.getSelectionPath().getLastPathComponent()).getUserObject()).getCategory();
					   OPMLWriter wr = new OPMLWriter(fc.getSelectedFile());
					   wr.write(parent);
				   }
			   }
	   }
}
