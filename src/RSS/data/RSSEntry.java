package RSS.data;

import java.io.Serializable;
import java.util.Date;
import java.util.Iterator;
import com.sun.syndication.feed.synd.SyndEntry;
import controller.Ban;

/**
 * Class implementation shell to SyndEntry
 * @author Nicolay Klimchuk
 */
public class RSSEntry implements Serializable{
	
	private static final long serialVersionUID = 2269787164240187806L;
	private SyndEntry entry;
	private boolean is18plus; // this field only for read
	private boolean isReaded = false;
	private Date lastViewed;
	
	/**
	 * Constructor. Check if 18+ and isRead=false
	 * @param entry - inner SyndEntry
	 */
	public RSSEntry(SyndEntry entry) {
		this.entry = entry;
		is18plus = test18plus(entry);
		
	}
	/**
	 * Constructor. Check if 18+
	 * @param entry - inner SyndEntry
	 * @param r - is read
	 */
	public RSSEntry(SyndEntry entry, boolean r) {
		this.entry = entry;
		isReaded = r;
		is18plus = test18plus(entry);
	}
	/**
	 * Constructor. Check if 18+
	 * @param entry - inner SyndEntry
	 * @param r - is read
	 * @param d - date of last update
	 */
	public RSSEntry(SyndEntry entry, boolean r, Date d) {
		this.entry = entry;
		isReaded = r;
		lastViewed = d;
		is18plus = test18plus(entry);
	}
	/**
	 * Is user read entry
	 * @return - is read entry
	 */
	public boolean isReaded() {
		return isReaded;
	}
	/**
	 * set flag is read
	 * @param isReaded - is user read entry
	 */
	public void setReaded(boolean isReaded) {
		this.isReaded = isReaded;
	}
	/**
	 * Is entry 18+
	 * @return is contents of entry 18+
	 */
	public boolean is18plus() {
		return is18plus;
	}
	/**
	 * Return inner SyndEntry
	 * @return - inner SyndEntry
	 */
	public SyndEntry getEntry() {
		return entry;
	}
	/**
	 * Set inner entry
	 * @param entry - inner entry
	 */
	public void setEntry(SyndEntry entry) {
		this.entry = entry;
	}

	/**
	 * Set date of last view
	 * @param lastViewed
	 */
	public void setLastView(Date lastViewed) {
		this.lastViewed = lastViewed;
	}

	/**
	 * Get date of last view
	 * @return - date of last view
	 */
	public Date getLastViewed() {
		return lastViewed;
	}
	/**
	 * Testing - if text of entry contains banned words
	 * @param entry - testing entry
	 * @return - if entry contains banned words
	 */
	private boolean test18plus(SyndEntry entry){
		String title = ""+entry.getTitle();
		String descripton = "";
		if(entry.getDescription() != null)
			descripton = entry.getDescription().getValue();
		String link = ""+entry.getLink();
		
		Ban ban = Ban.getInstanceBan();
		
		Iterator<String> it = ban.getBadWords().iterator();
		while(it.hasNext()){
			String test = title.concat(descripton);
			if(test.toLowerCase().indexOf(it.next().toLowerCase()) != -1)
				return true;
		}

		it = ban.getBanLinks().iterator();
		while(it.hasNext()){
			if(link.equalsIgnoreCase(it.next()))
				return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return "RSSEntry [entry=" + entry + ", is18plus=" + is18plus + "]";
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((entry == null) ? 0 : entry.hashCode());
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
		RSSEntry other = (RSSEntry) obj;
		if (entry == null) {
			if (other.entry != null)
				return false;
		} else if (!entry.equals(other.entry))
			return false;
		return true;
	}
	
}
