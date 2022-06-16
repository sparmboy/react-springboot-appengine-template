package com.example.utils;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.jasypt.encryption.pbe.config.EnvironmentStringPBEConfig;

public class PropertyEncryptionUtil
{
	public final static String ALGORITHM = "PBEWithMD5AndDES";
	
	public static void main(String[] args )
	{
		if( args.length < 2 )
		{
			System.out.println("java com.example.utils.PropertyEncryptionUtil <value_to_encrypt> <encryption_password>");
			System.exit(0);
		}
		else if( args.length == 2 )
		{
			System.out.println(encrypt(args[0],args[1]));
		}
	}

	public static String encrypt( String value, String password ) {
		EnvironmentStringPBEConfig config =  new EnvironmentStringPBEConfig();
		config.setAlgorithm(ALGORITHM);
		config.setPassword(password);
		StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
		encryptor.setConfig(config);
		return encryptor.encrypt(value);
	}
}