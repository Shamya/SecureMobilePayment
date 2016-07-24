package com.example.loginserver;

public class Server {
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		InputStream sockInput = new SocketInputStream(new SSLProtocol());
		sockInput.init();
		while (true){
			String input = sockInput.read();
			if (input == null || input.equals(".")) {
				break;
			}         
			System.out.println("Encrypted data: ");
			System.out.println(input);
			EncryptionAlgorithm d = new MyEncryptionAlgorithm();
			String decryptedvalue = " ";
			try {
				//long start = System.currentTimeMillis();
				decryptedvalue = d.decrypt(input);
				//long time = System.currentTimeMillis() - start;
				//System.out.println("\nTime to decrypt: " + time + "\n");

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String[] LoginInfo = decryptedvalue.split("-");
			System.out.println("\nDecrypted data: \nCard holder name - " + LoginInfo[0] + "\nCard number - " + LoginInfo[1] + "\nExpiry date - " + LoginInfo[2] + "\nCVV - " + LoginInfo[3]);
			sockInput.write("payement success");				
		}
	}
}
