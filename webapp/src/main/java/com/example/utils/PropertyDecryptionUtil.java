package com.example.utils;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;

public class PropertyDecryptionUtil
{
	public static void main(String[] args )
	{
		if( args.length != 2 )
		{
			System.out.println("java com.example.utils.PropertyDecryptionUtil <value_to_decrypt> <encryption_password>");
			System.exit(0);
		}
		else
		{
			System.out.println(decrypt(args[0],args[1]));
		}
	}
	
	public static String decrypt( String value, String password )
	{
		EnvironmentStringPBEConfig config =  new EnvironmentStringPBEConfig();
		config.setAlgorithm(PropertyEncryptionUtil.ALGORITHM);
		config.setPassword(password);
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();		
		encryptor.setConfig(config);
		
		return encryptor.decrypt( value);
	}

}