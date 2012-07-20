package test.RSS.filer;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.NoSuchPaddingException;

import org.junit.Before;
import org.junit.Test;

import RSS.filer.Crypt;

public class CryptNewTest {

	Crypt crypt;
	String keyfile = "testkey";
	String file = "testfile";
	
	@Before
	public void setUp() throws Exception {
		
		crypt = new Crypt(keyfile);
	//	FileOutputStream fos = new FileOutputStream(file);
	//	fos.write("Hello world!".getBytes());
	//	fos.close();
	}

	@Test
	public void testCreateKeyFile() {
		try {
			crypt.createKeyFile(/*keyfile*/);
			File f = new File(keyfile);
			assertTrue(f.exists());
			
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			fail("Exception");
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			fail("Exception");
		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception");
		}
		
	}
	
	@Test
	public void testEncryptFile() {
		try {
	/*		BufferedOutputStream bos = new BufferedOutputStream(
					crypt.encryptFile(file)
					);
			bos.write("Hello world!".getBytes());
			bos.flush();
			bos.close();
			*/
	//		ObjectOutputStream ois = new ObjectOutputStream(crypt.encryptFile(file));
	  
	//		ois.writeObject(new String("HelloWorld"));
	//		crypt.encryptFile(file);
	//		ois.close();
	//		crypt.anotherMethod();
			crypt.encryptFile(file, new String("HelloWorld"));
			
			File f = new File(file);
			assertTrue(f.exists());
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			fail("Exception");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			fail("Exception");
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			fail("Exception");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			fail("Exception");			
		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception");
		}
	}
	
	@Test
	public void testDecryptFile() {
		try {
	/*		BufferedInputStream bis = new BufferedInputStream(
					crypt.decryptFile(file)
					);*/
	//		ObjectInputStream oos = new ObjectInputStream(crypt.decryptFile(file));
/*			byte[] d = new byte[bis.available()];
			bis.read(d);
			String result = new String(d);
			*/
			
//			String result = (String)oos.readObject();
//			oos.close();
			String result = (String) crypt.decryptFile(file);
			assertEquals( "HelloWorld", result);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
			fail("Exception");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			fail("Exception");
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
			fail("Exception");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			fail("Exception");			
		} catch (IOException e) {
			e.printStackTrace();
			fail("Exception");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			fail("Exception");
		}
	}

}
