package controller;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.HashSet;
import java.util.Set;
import javax.crypto.NoSuchPaddingException;
import RSS.filer.Crypt;

/**
 * Class contains bad words and bad links
 * If entry contains any word of collection -> it is 18+
 * Words load from encrypted file config/badWords.crt
 * Links contained in config/badLinks.crt
 * Class implements template Singleton
 * @author Nicolay Klimchuk
 *
 */

public class Ban {
	private static Ban ban = new Ban();
	private Set<String> words;
	
	private Set<String> links;
	
	/**
	 * In constructor fill Sets from file
	 */
	private Ban() {
		words = new HashSet<String>();
		links = new HashSet<String>();
		try {
			reread();
		}catch (IOException e) {
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Return instance of Ban
	 * @return Ban instance 
	 */
	static public Ban getInstanceBan(){
		return ban;
	}
	
	/**
	 * Methods read, encrypt file and fill Sets 
	 * with banned words and links  
	 * @throws InvalidKeyException - not found keyfile
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException 
	 * @throws NoSuchPaddingException
	 * @throws IOException - can't read file
	 * @throws ClassNotFoundException
	 */
	
	public void reread() throws InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException, IOException, ClassNotFoundException
	{
		Crypt crypt = new Crypt(Parameters.getParameters().getKeyFile());
		words =  (Set<String>) crypt.decryptFile("config/badWords.crt");
		links =  (Set<String>) crypt.decryptFile("config/badLinks.crt");
	
	}
	/**
	 * Write set with banned words to file.
	 * @throws IOException - can't read file
	 * @throws InvalidKeyException - bad keyfile 
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 */
	public void rewriteWords() 
		throws IOException, InvalidKeyException, 
		NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException
	{
		Crypt crypt = new Crypt(Parameters.getParameters().getKeyFile());
		crypt.encryptFile("config/badWords.crt", words);
	}
	/**
	 * 
	 * Write set with banned links to file.
	 * @throws IOException - can't found file
	 * @throws InvalidKeyException - bad keyfile
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 */
	public void rewriteLinks() 
		throws IOException, InvalidKeyException, 
		NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException
	{
		Crypt crypt = new Crypt(Parameters.getParameters().getKeyFile());
		crypt.encryptFile("config/badLinks.crt", links);
	}
	/**
	 * Set of banned words
	 * @return Set of banned words
	 */
	public Set<String> getBadWords(){
		return words;
	}
	/**
	 * Set of banned links
	 * @return Set of banned links
	 */
	public Set<String> getBanLinks(){
		return links;
	}
	/**
	 * Setting Set of banned words  
	 * @param w Set of banned words
	 */
	public void setBadWords(Set<String> w){
		words = w;
	}
	/**
	 * Setting Set of banned links
	 * @param w Set of banned links
	 */
	public void setBanLinks(Set<String> w){
		links = w;
	}
	/**
	 * Add new banned word, and save set to file
	 * @param s - new banned word
	 * @throws IOException - can't found file
	 * @throws InvalidKeyException - bad keyfile
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 */
	public void addWord(String s) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException{
		words.add(s);
		rewriteWords();
	}
	/**
	 * Add new banned link to set, and save it to file
	 * @param s - new Link
	 * @throws IOException - can't found file
	 * @throws InvalidKeyException - bad keyfile
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 */
	public void addLink(String s) throws IOException, InvalidKeyException, NoSuchAlgorithmException, InvalidKeySpecException, NoSuchPaddingException{
		links.add(s);
		rewriteLinks();
	}
}
