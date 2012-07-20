package RSS.data;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.List;

import org.jdom.*;

//import com.sun.syndication.feed.synd.SyndEntry;
import RSS.filer.Filename;

import com.sun.syndication.feed.synd.SyndFeed;
import com.sun.syndication.feed.synd.SyndImage;
import com.sun.syndication.io.FeedException;
import com.sun.syndication.io.SyndFeedInput;

/**
 * Class realization saving RSS Server information
 * @author Nikolay Klimchuk
 *
 */
public class RSSServer {
	private String name;
	private Date lastModified = new Date();
	private long modifyInterval = 1000*60*60*24*7; // one week
	private URL url;

	private Category localCategory;
	
	private String author;
	private List authors;
	private List categories;
	private List  contributors;
	private String copyright;
	private String description;
	private String encoding;
	private String feedType;
	private SyndImage image;
	private String language;
	private String link;
	private List links;
	private Date publishedDate;
	private String title;
	private String uri;
	
	/**
	 * Constructor with name of server
	 * @param name - name of server
	 */
	public RSSServer(String name){
		this.name = name;
	}
	/**
	 * Constructor with name and category of server
	 * @param name - server name 
	 * @param category - server category
	 */
	public RSSServer(String name, Category category){
		this.name = name;
		this.setLocalCategory(category);
	}
	/**
	 * Constructor. Gets all parameters from SyndFeed
	 * @param aaa - SyndFeed with parameters
	 * @param name - name of server
	 */
	public RSSServer(SyndFeed aaa, String name){
		initialize(aaa);
		this.name = name;
	}
	/**
	 * Constructor. Gets parameters from SyndFeed
	 * @param aaa - SyndFeed with parameters
	 * @param name - name of server
	 * @param modify - date of last modify
	 */
	public RSSServer(SyndFeed aaa, String name, Date modify){
		this(aaa, name);
		lastModified = modify;
	}
	/**
	 * Constructor. Gets parameters from SyndFeed
	 * @param aaa -  SyndFeed with parameters
	 * @param name - name of server
	 * @param modify - date of last modify
	 * @param interval - interval between modifying
	 */
	public RSSServer(SyndFeed aaa, String name, Date modify, long interval){
		this(aaa, name);
		lastModified = modify;
		modifyInterval = interval;
	}
	/**
	 * Constructor. Get parameters from XML document
	 * @param e - XML Document
	 * @param name - name of server
	 * @throws IllegalArgumentException
	 * @throws FeedException
	 */
	public RSSServer(Document e, String name) throws IllegalArgumentException, FeedException{
		//Document d = new Document(e);
		initialize(new SyndFeedInput().build(e));
		this.name = name;
	}
	/**
	 * Constructor. Get parameters from XML document
	 * @param e - XML Document
	 * @param name - name of server
	 * @param modify - date and time of last modify
	 * @throws IllegalArgumentException
	 * @throws FeedException
	 */
	public RSSServer(Document e, String name, Date modify) throws IllegalArgumentException, FeedException{
		this(e, name);
		lastModified = modify;
	}
	/**
	 * Constructor. Get parameters from XML document
	 * @param e - XML Document
	 * @param name - name of server
	 * @param modify - date and time of last modify
	 * @param interval - interval between modifying
	 * @throws IllegalArgumentException
	 * @throws FeedException
	 */
	public RSSServer(Document e, String name, Date modify, long interval) throws IllegalArgumentException, FeedException{
		this(e, name);
		lastModified = modify;
		modifyInterval = interval;
	}
	/**
	 * Method copy parameters from SyndFeed
	 * @param aaa - SyndFeed with parameters 
	 */
	private void initialize(SyndFeed aaa){
		setAuthor(aaa.getAuthor());
		setAuthors(aaa.getAuthors());
		setCategories(aaa.getCategories());
		setContributors(aaa.getContributors());
		setCopyright(aaa.getCopyright());
		setDescription(aaa.getDescription());
		setEncoding(aaa.getEncoding());
		setFeedType(aaa.getFeedType());
		setImage(aaa.getImage());
		setLanguage(aaa.getLanguage());
		setLink(aaa.getLink());
		setLinks(aaa.getLinks());
		setPublishedDate(aaa.getPublishedDate());
		setTitle(aaa.getTitle());
		setUri(aaa.getUri());

	}
	/**
	 * Get Author of feeds 
	 * @return author of feeds
	 */
	public String getAuthor() {
		return author;
	}
	/**
	 * Return authors of feeds
	 * @return
	 */
	public List getAuthors() {
		return authors;
	}
	/**
	 * Return categories
	 * @return categories
	 */
	public List getCategories() {
		return categories;
	}
	/**
	 * Return contributions
	 * @return contributions of feeds
	 */
	public List getContributors() {
		return contributors;
	}
	/**
	 * Return copyright
	 * @return copyright
	 */
	public String getCopyright() {
		return copyright;
	}
	/**
	 * Return descriptions
	 * @return descriptions
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * Return encoding 
	 * @return encoding
	 */
	public String getEncoding() {
		return encoding;
	}
	/**
	 * Return feed type
	 * @return feed type
	 */
	public String getFeedType() {
		return feedType;
	}
	/**
	 * Return image
	 * @return  image
	 */
	public SyndImage getImage() {
		return image;
	}
	/**
	 * Return language of feeds 
	 * @return language of feeds
	 */
	public String getLanguage() {
		return language;
	}
	/**
	 * Return link attached to feed
	 * @return link attached to feed
	 */
	public String getLink() {
		return link;
	}
	/**
	 * Return links attached to feed
	 * @return links attached to feed
	 */
	public List getLinks() {
		return links;
	}
	/**
	 * Return published date
	 * @return published date
	 */
	public Date getPublishedDate() {
		return publishedDate;
	}
	/**
	 * Return title of feeds
	 * @return title of feeds
	 */
	public String getTitle() {
		return title;
	}
	/**
	 * URI of feeds
	 * @return URI of feeds 
	 */
	public String getUri() {
		return uri;
	}

	/**
	 * Local name of server
	 * @return name of server
	 */
	public String getName() {
		return name;
	}
	/**
	 * Set name of server
	 * @param name - server name
	 */
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * Date and time of last modified
	 * @return
	 */
	public Date getLastModified() {
		return lastModified;
	}
	/**
	 * Set date and time of last modified
	 * @param lastModified
	 */
	public void setLastModified(Date lastModified) {
		this.lastModified = lastModified;
	}
	/**
	 * Return update interval
	 * @return update interval
	 */
	public long getModifyInterval() {
		return modifyInterval;
	}
	/**
	 * Set modify interval
	 * @param modifyInterval - new modify interval
	 */
	public void setModifyInterval(long modifyInterval) {
		this.modifyInterval = modifyInterval;
	}
	/**
	 * Set author of feeds
	 * @param author - new author of feeds
	 */
	public void setAuthor(String author) {
		this.author = author;
	}
	/**
	 * Set authors of feeds
	 * @param authors list of authors of feeds
	 */
	public void setAuthors(List authors) {
		this.authors = authors;
	}
	/**
	 * Set Categories of feeds
	 * @param categories list of categories
	 */
	public void setCategories(List categories) {
		this.categories = categories;
	}
	/**
	 * set contributions of list
	 * @param contributors - list of contributions of list
	 */
	public void setContributors(List contributors) {
		this.contributors = contributors;
	}
	/**
	 * Set copyright of feeds
	 * @param copyright - copyright of feeds
	 */
	public void setCopyright(String copyright) {
		this.copyright = copyright;
	}
	/**
	 * Set descriptions of list
	 * @param description - descriptions of list
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * Set encoding of feeds
	 * @param encoding - encoding of feeds
	 */
	public void setEncoding(String encoding) {
		this.encoding = encoding;
	}
	/**
	 * Set type of feeds
	 * @param feedType - feed type
	 */
	public void setFeedType(String feedType) {
		this.feedType = feedType;
	}
	/**
	 * Sets image of feeds
	 * @param image - new image
	 */
	public void setImage(SyndImage image) {
		this.image = image;
	}
	/**
	 * Set text representation of language
	 * @param language - text representation of language
	 */
	public void setLanguage(String language) {
		this.language = language;
	}
	/**
	 * Set link, where sending request
	 * @param link
	 */
	public void setLink(String link) {
		this.link = link;
	}
	/**
	 * set list of links
	 * @param links - list of links
	 */
	public void setLinks(List links) {
		this.links = links;
	}
	/**
	 * set publications date and time
	 * @param publishedDate - new publications date and time
	 */
	public void setPublishedDate(Date publishedDate) {
		this.publishedDate = publishedDate;
	}
	/**
	 * Set title of server
	 * @param title - title of server
	 */
	public void setTitle(String title) {
		this.title = title;
	}
	/**
	 * set uri where downloaded feed
	 * @param uri - uri where downloaded feed
	 */
	public void setUri(String uri) {
		this.uri = uri;
	}
	/**
	 * set url where downloaded feed
	 * @param uri - url where downloaded feed
	 */
	public void setUrl(URL url) {
		this.url = url;
	}

	/**
	 * Get url. Method used for download feeds. 
	 * If url == null -> return link
	 * else return url
	 * @return
	 */
	public URL getUrl() {
		if(url == null && link != null && !link.equals(""))
			try {
				return new URL(link);
			} catch (MalformedURLException e) {
				e.printStackTrace();
				return url;
			}
		
		return url;
	}

	/**
	 *	Set category of current RSS server  
	 * @param localCategory - category of current RSS server
	 */
	public void setLocalCategory(Category localCategory) {
		this.localCategory = localCategory;
	}
	/**
	 *	Return category of current RSS server  
	 *  @return - category of current RSS server
	 */
	public Category getLocalCategory() {
		return localCategory;
	}

	@Override
	public int hashCode() {
		final int prime = 17;
		int result = 1;
		result = prime * result + ((author == null) ? 0 : author.hashCode());
		result = prime * result + ((authors == null) ? 0 : authors.hashCode());
		result = prime * result
				+ ((categories == null) ? 0 : categories.hashCode());
		result = prime * result
				+ ((contributors == null) ? 0 : contributors.hashCode());
		result = prime * result
				+ ((copyright == null) ? 0 : copyright.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((encoding == null) ? 0 : encoding.hashCode());
		result = prime * result
				+ ((feedType == null) ? 0 : feedType.hashCode());
		result = prime * result + ((image == null) ? 0 : image.hashCode());
		result = prime * result
				+ ((language == null) ? 0 : language.hashCode());
		result = prime * result
				+ ((lastModified == null) ? 0 : lastModified.hashCode());
		result = prime * result + ((link == null) ? 0 : link.hashCode());
		result = prime * result + ((links == null) ? 0 : links.hashCode());
		result = prime * result
				+ (int) (modifyInterval ^ (modifyInterval >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((publishedDate == null) ? 0 : publishedDate.hashCode());
		result = prime * result + ((title == null) ? 0 : title.hashCode());
		result = prime * result + ((uri == null) ? 0 : uri.hashCode());
		result = prime * result + ((url == null) ? 0 : url.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof RSSServer))
			return false;
		RSSServer other = (RSSServer) obj;
		if (author == null) {
			if (other.author != null)
				return false;
		} else if (!author.equals(other.author))
			return false;
		if (authors == null) {
			if (other.authors != null)
				return false;
		} else if (!authors.equals(other.authors))
			return false;
		if (categories == null) {
			if (other.categories != null)
				return false;
		} else if (!categories.equals(other.categories))
			return false;
		if (contributors == null) {
			if (other.contributors != null)
				return false;
		} else if (!contributors.equals(other.contributors))
			return false;
		if (copyright == null) {
			if (other.copyright != null)
				return false;
		} else if (!copyright.equals(other.copyright))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (encoding == null) {
			if (other.encoding != null)
				return false;
		} else if (!encoding.equals(other.encoding))
			return false;
		if (feedType == null) {
			if (other.feedType != null)
				return false;
		} else if (!feedType.equals(other.feedType))
			return false;
		if (image == null) {
			if (other.image != null)
				return false;
		} else if (!image.equals(other.image))
			return false;
		if (language == null) {
			if (other.language != null)
				return false;
		} else if (!language.equals(other.language))
			return false;
		if (lastModified == null) {
			if (other.lastModified != null)
				return false;
		} else if (!lastModified.equals(other.lastModified))
			return false;
		if (link == null) {
			if (other.link != null)
				return false;
		} else if (!link.equals(other.link))
			return false;
		if (links == null) {
			if (other.links != null)
				return false;
		} else if (!links.equals(other.links))
			return false;
		if (modifyInterval != other.modifyInterval)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (publishedDate == null) {
			if (other.publishedDate != null)
				return false;
		} else if (!publishedDate.equals(other.publishedDate))
			return false;
		if (title == null) {
			if (other.title != null)
				return false;
		} else if (!title.equals(other.title))
			return false;
		if (uri == null) {
			if (other.uri != null)
				return false;
		} else if (!uri.equals(other.uri))
			return false;
		if (url == null) {
			if (other.url != null)
				return false;
		} else if (!url.equals(other.url))
			return false;
		return true;
	}

	@Override
	public String toString() {
		String n = Filename.convert(getName());
		//return getName();
		return n;
	}
	
}
