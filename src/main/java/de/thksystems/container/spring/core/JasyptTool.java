package de.thksystems.container.spring.core;

import java.util.Scanner;

import org.jasypt.util.text.BasicTextEncryptor;

public class JasyptTool {

	public static void main(String[] args) throws Exception {
		@SuppressWarnings("resource")
		Scanner in = new Scanner(System.in);
		System.out.println("Text: ");
		String text = in.nextLine();
		System.out.println("Password: ");
		String password = in.nextLine();

		BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
		textEncryptor.setPassword(password);

		System.out.println(textEncryptor.encrypt(text));
	}
}
