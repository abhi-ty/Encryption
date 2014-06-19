package com.example.demoasymenc;

import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.os.Build;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.SecretKeyFactorySpi;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends Activity {

	static MainActivity objMain;

	/**
	 * String to hold name of the encryption algorithm.
	 */

	FileOutputStream outputStream;

	/*************** Symmetric Key Variables **************************/
	KeyGenerator objSymmKeyGen;
	SecretKey objSecKey;
	Cipher cipher;
	Cipher decipher;
	SecretKeyFactory objSecKeyFac;
	byte[] encrypted ;
	byte[] decrypted ;
	  byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
	  String doc2;
	 IvParameterSpec ivspec = new IvParameterSpec(iv);

	/*************** Assymmetric Key Variables **************************/
	KeyPairGenerator objkeyGen;
	KeyPair objPair;
	String filePrivate = "PrivateKey.pem";
	// File privatefile = new File(objMain.getFilesDir(), filePrivate);

	String filePublic = "PrivateKey.pem";
	// File publicfile = new File(objMain.getFilesDir(), filePublic);
	public static final String ALGORITHM = "RSA";

	StringBuilder sb = new StringBuilder();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_main);
		objMain = this;
		//android.os.Debug.waitForDebugger();

		/*
		 * if (savedInstanceState == null) {
		 * getFragmentManager().beginTransaction() .add(R.id.container, new
		 * PlaceholderFragment()) .commit(); }
		 */

		TextView txt1 = (TextView) findViewById(R.id.txt1);
		TextView txt2 = (TextView) findViewById(R.id.txt2);

		generateAsymmKey();
		writeAsymmKeys();
		readAsymmKeys();
		txt1.setText(sb);
		Toast.makeText(getApplicationContext(), "Keys are generated. Now encryption will be performed.!!! =)",
				   Toast.LENGTH_LONG).show();
		/*try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		generateSecretKey();
	
		
		encrypt();
		txt2.setText(sb);
		Toast.makeText(getApplicationContext(), "Encryption is done. Now Decryption will be performed.!!! =)",
				   Toast.LENGTH_LONG).show();
		/*try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}*/
		decrypt();
		txt1.setText(doc2);
		
		

	}

	public void generateAsymmKey() {
		try {
			objkeyGen = KeyPairGenerator.getInstance(ALGORITHM);
			objkeyGen.initialize(2048);

			objPair = objkeyGen.generateKeyPair();

		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void writeAsymmKeys() {
		try {
			outputStream = openFileOutput(filePrivate, objMain.MODE_PRIVATE);
			outputStream.write(objPair.getPrivate().toString().getBytes());
			outputStream.close();

			outputStream = openFileOutput(filePublic, objMain.MODE_PRIVATE);
			outputStream.write(objPair.getPublic().toString().getBytes());
			outputStream.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void readAsymmKeys() {
		try {
			FileInputStream fin = openFileInput(filePublic);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					fin));
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			fin.close();
		} catch (OutOfMemoryError om) {
			om.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void generateSecretKey() {
		try {
			objSymmKeyGen = KeyGenerator.getInstance("AES");
			 cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			 decipher =  Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		objSymmKeyGen.init(256);
		objSecKey = objSymmKeyGen.generateKey();
	}

	public void encrypt()
	{
		try {
			cipher.init(cipher.ENCRYPT_MODE, objSecKey,ivspec);
			//cipher.i
			encrypted = cipher.doFinal(sb.toString().getBytes("UTF-8"));
			sb.delete(0	, sb.length());
			sb.append(encrypted.toString());
							
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void decrypt()
	{
		try {
			decipher.init(decipher.DECRYPT_MODE, objSecKey,ivspec);
			decrypted = decipher.doFinal(encrypted);
			 doc2 = new String(decrypted, "UTF-8");
			//sb.delete(0	, sb.length());
			//sb.append(decrypted.toString());
			
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BadPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * A placeholder fragment containing a simple view.
	 */
	public static class PlaceholderFragment extends Fragment {

		public PlaceholderFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_main, container,
					false);
			return rootView;
		}
	}

}
