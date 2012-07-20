package view.html;

/**
 * Class implements work with representation of unicode symbols in HTML 
 * @author Nikolay Klimchuk
 *
 */
public class HTMLUnicode {
	/**
	 * Method convert Unicode symbols from <i>text</i> to HTML string
	 * English symbols not convert, other symbols convert to &#XXXX   
	 * @param text - text to convert
	 * @return - html string 
	 */
	public static String  convertToUnicode(String text)
	{
		final char[] cText = text.toCharArray();
		String result = "";
		for(int i = 0; i < cText.length; i++)
		{
			if(cText[i] < 0x007E){
				result += cText[i];
			} else {
				result += "&#" + Integer.toString(cText[i]) + ";";
			}
		}
		return result;
	}
	/**
	 * Method calculate shift from begin in text on html representation 
	 * @param text - unicode text
	 * @param position - position in unicode text
	 * @return position in html text
	 */
	public static int calculateShift(String text, int position)
	{
		char[] t = new char[text.length()];
		text.getChars(0, text.length(), t, 0);
		char old = ' ';
		int count = 0;
		boolean isTag = false;
		int begin = text.indexOf("<body>")+"<body>".length();
		for(int q = begin; q < t.length; q++ )
		{
			char i = t[q];
			
			if(old == '&')
			{
				if(i == '#')
				{
					isTag = true;
				}
			}else if(i == '<'){
				isTag = true;
			}else if((i == '>' || i == ';')&& isTag) 
			{
				isTag = false;
			}else if(!isTag)
			{	
				if(q > position) 
					if(isTag)
						return 0;
					else
					{
						return count;
					}
						
				if(i != '\n' && i != '\t' && (i != ' ' || old != ' ' ))
					count++;
			}
			
			old = i;
		}
		return 0;
	}
	
}
