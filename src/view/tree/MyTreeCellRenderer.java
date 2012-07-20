package view.tree;

import java.awt.Color;
import java.awt.Component;

import javax.swing.Icon;
import javax.swing.JTree;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;

import RSS.data.Category;
import RSS.data.RSSServer;

import sun.swing.DefaultLookup;
/**
 * Implements new Cell Renderer to JTree. 
 * Class change standard pictures
 * @author collia
 *
 */
public class MyTreeCellRenderer extends DefaultTreeCellRenderer {

	private static final long serialVersionUID = -9020046736312627934L;
	private Icon iconCategoryNotSel;
	private Icon iconCategorySel;
	private Icon iconCategorySelEmpty;
	private Icon iconRSS;
	
	/**
	 * Constructor.
	 * @param iconCategoryNotSel - icon of not selected category
	 * @param iconCategoruSel - icon of selected category
	 * @param iconCategoruSelEmpty - icon of selected empty category
	 * @param iconRSS - icon of server
	 */
	public MyTreeCellRenderer(Icon iconCategoryNotSel, Icon iconCategoruSel,
			Icon iconCategoruSelEmpty, Icon iconRSS) {
		super();
		this.iconCategoryNotSel = iconCategoryNotSel;
		this.iconCategorySel = iconCategoruSel;
		this.iconCategorySelEmpty = iconCategoruSelEmpty;
		this.iconRSS = iconRSS;
	}
	
	@Override
	public Component getTreeCellRendererComponent(JTree tree, 
							Object value, 
							boolean sel, 
							boolean expanded, 
							boolean leaf, 
							int row, 
							boolean hasFocus)
	
	{
		String stringValue = tree.convertValueToText(
                value, sel, expanded, leaf, row, hasFocus);

		setText(stringValue);

        Color fg = null;
 
        JTree.DropLocation dropLocation = tree.getDropLocation();
        if (dropLocation != null
                && dropLocation.getChildIndex() == -1
                && tree.getRowForPath(dropLocation.getPath()) == row) {

            Color col = DefaultLookup.getColor(this, ui, "Tree.dropCellForeground");
            if (col != null) {
                fg = col;
            } else {
                fg = getTextSelectionColor();
            }

         } else if (sel) {
            fg = getTextSelectionColor();
        } else {
            fg = getTextNonSelectionColor();
        }

       setForeground(fg);

        Icon icon = null;
        DefaultMutableTreeNode ooo = (DefaultMutableTreeNode) value;
        if (leaf) {
        	if(ooo.getUserObject() instanceof RSSServer)
        		icon = iconRSS;
    		else if(ooo.getUserObject() instanceof TreeCategory)
    		{
    			icon = iconCategoryNotSel;
    		}
        } else if (expanded) {
        	if(ooo.getUserObject() instanceof RSSServer)
    			icon = iconRSS;
    		else if(ooo.getUserObject() instanceof TreeCategory)
    		{
    			if(((TreeCategory)ooo.getUserObject()).getCategory().getServers().isEmpty() && ((TreeCategory)ooo.getUserObject()).getCategory().hasChild())
    				icon = iconCategorySelEmpty;
    			else
    				icon = iconCategorySel;
    		}
        } else {
        	icon = iconCategoryNotSel;
        }
        
	if (!tree.isEnabled()) {
	    setEnabled(false);
            LookAndFeel laf = UIManager.getLookAndFeel();
            Icon disabledIcon = laf.getDisabledIcon(tree, icon);
            if (disabledIcon != null) icon = disabledIcon;
            setDisabledIcon(icon);
	} else {
	    setEnabled(true);
            setIcon(icon);
	}
	
        setComponentOrientation(tree.getComponentOrientation());
	    
        selected = sel;
		return this;
	}
}
