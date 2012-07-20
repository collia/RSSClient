package view.tree;

import RSS.data.Category;

/**
 * 
 * Class implements wrap for Category 
 * @author Nicolay Klimchuk
 *
 */
public class TreeCategory  {

	private Category category;
	/**
	 * Constructor. 
	 * @param category Inner category
	 */
	public TreeCategory(Category category) {
		this.category = category;
	}
	/**
	 * Getter Category
	 * @return inner Category
	 */
	public Category getCategory(){
		return category;
	}
	
	@Override
	public String toString() {
		return category.getName();
	}

	
}
