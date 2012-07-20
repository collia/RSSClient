package RSS.filer;

/**
 * Class converting string to correct filename
 * @author Nicolay Klimchuk
 *
 */

public class Filename {
	public Filename()
	{
		
	}
	/**
	 * Converting string to correct filename
	 * @param name - string to convert
	 * @return - correct filename
	 */
	static public String convert(String name)
	{
		if(name.contains("/"))
			name = name.replaceAll("/", "_");
		if(name.contains("\\"))
			name = name.replaceAll("\\", "_");
		if(name.contains("*"))
			name = name.replaceAll("*", "_");
	/*	if(name.contains("="))
			name = name.replaceAll("=", "_");
		if(name.contains("["))
			name = name.replaceAll("[", "_");
		if(name.contains("]"))
			name = name.replaceAll("]", "_");
		if(name.contains("+"))
			name = name.replaceAll("+", "_");
		if(name.contains("["))
			name = name.replaceAll("[", "_");*/
		if(name.contains(":"))
			name = name.replaceAll(":", "_");
		if(name.contains(";"))
			name = name.replaceAll(";", "_");
		if(name.contains("?"))
			name = name.replaceAll("?", "_");
		if(name.contains("\""))
			name = name.replaceAll("\"", "_");
		if(name.contains("<"))
			name = name.replaceAll("<", "_");
		if(name.contains(">"))
			name = name.replaceAll(">", "_");
		if(name.contains("|"))
			name = name.replaceAll("|", "_");
	/*	if(name.contains("..."))
			name = name.replaceAll("\u002E\u002E\u002E", "_");
		if(name.contains(".."))
			name = name.replaceAll("\u002E\u002E", "_");
		if(name.contains("."))
			name = name.replaceAll("\u002E", "_");
		*/
		while(name.contains("."))
			name = name.replace('.', '_');
		return name;
	}
}
