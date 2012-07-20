package RSS.filer;

import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.logging.Logger;


import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import main.RSSLogger;

/**
 * 
 * Class encode and decode files
 * @author Nikolay Klimchuk
 *
 */

public class Crypt {

	private static Logger logger = RSSLogger.loggerRSSFilerCrypt;
	private String keyfile;
	
	public static final String algorithm = "DESede";
	
	/**
	 * Constructor gets path to keyfile
	 * @param keyfile - path to keyfile
	 */
	public Crypt(String keyfile)
	{
		logger.fine("Contstructor");
		this.keyfile = keyfile;
	}
	/**
	 * Decoding file. Filename is in operand.
	 * In file reading class Object and return. 
	 * @param file - filename to decode
	 * @return - instance of Object  
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 * @throws ClassNotFoundException
	 */
	public Object decryptFile(String file ) 
			throws IOException, NoSuchAlgorithmException, 
			InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException, ClassNotFoundException
	{
		logger.entering(this.getClass().getName(), 
				"decryptFile(String file )");	   
		FileInputStream fos = null;
	    
		logger.fine("Load keyfile");
	    fos = new FileInputStream(keyfile);
	    byte[] a = new byte[fos.available()];
	    fos.read(a);
	    fos.close();
	           
	    SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
	    key = skf.generateSecret(new DESedeKeySpec(a));
	           
	    Cipher cipher = Cipher.getInstance(algorithm);
	    cipher.init(Cipher.DECRYPT_MODE, key);
	           
	    logger.fine("Read object");       
	    ObjectInputStream ois = new ObjectInputStream(new CipherInputStream(new FileInputStream(file), cipher));
	    Object secret = ois.readObject();
	    logger.fine("Read: " + secret);
	    
	    logger.exiting(this.getClass().getName(), 
	    	"decryptFile(String file )");	   
	    return secret;	    
	}

	private SecretKey key;
	/**
	 * Save object secret to file and encoding it.
	 * @param file - filename
	 * @param secret - object to save
	 * @throws IOException
	 * @throws NoSuchAlgorithmException
	 * @throws InvalidKeyException
	 * @throws InvalidKeySpecException
	 * @throws NoSuchPaddingException
	 */
	public void encryptFile(String file, Object secret)
			throws IOException, NoSuchAlgorithmException, 
			InvalidKeyException, InvalidKeySpecException, NoSuchPaddingException
	{
		logger.entering(this.getClass().getName(), 
			"encryptFile(String file, Object secret)");	   
		logger.fine("Load key");
		 FileInputStream fis =  new FileInputStream(keyfile);
         byte[] a = new byte[fis.available()];
         fis.read(a);
         fis.close();
         
         SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
         key = skf.generateSecret(new DESedeKeySpec(a));
         Cipher cipher = Cipher.getInstance(algorithm);
         cipher.init(Cipher.ENCRYPT_MODE, key);
         
         logger.fine("Write object");
          ObjectOutputStream oos = new ObjectOutputStream(new CipherOutputStream(new FileOutputStream(file), cipher));
          oos.writeObject(secret);
   
          oos.close();
          logger.exiting(this.getClass().getName(), 
			"encryptFile(String file, Object secret)");	   

	}

	/**
	 * Algorithm DESede need key file. Method generate new file with name in constructor.
	 * @throws NoSuchAlgorithmException
	 * @throws IOException
	 * @throws InvalidKeySpecException
	 */
	public void createKeyFile() 
		throws NoSuchAlgorithmException, IOException, InvalidKeySpecException
	{
		logger.entering(this.getClass().getName(), 
			"createKeyFile(String filename)");	
		FileOutputStream fos = null;
		
		KeyGenerator kg = KeyGenerator.getInstance(algorithm);
        SecretKey key = kg.generateKey();
        
       
        SecretKeyFactory skf = SecretKeyFactory.getInstance(algorithm);
        DESedeKeySpec keyspec = (DESedeKeySpec) skf.getKeySpec(key, DESedeKeySpec.class);
  
        File f = new File(keyfile);
        if(!f.exists()) f.createNewFile();
        
        fos = new FileOutputStream(keyfile,false);
        fos.write(keyspec.getKey());
        fos.close();
 
        logger.exiting(this.getClass().getName(), 
			"createKeyFile(String filename)");	
	}
}
