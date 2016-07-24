package com.example.loginserver;

public class Server {
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		UserPswdMap registry = new MyUserPswdMap();
		InputStream sockInput = new SocketInputStream(new SSLProtocol());
		sockInput.init();
		while (true){
			String input = sockInput.read();
			if (input == null || input.equals(".")) {
				break;
			}         
			String[] LoginInfo = input.split(" - ");
			if(LoginInfo.length == 2){
				sockInput.write(registry.findUser(LoginInfo));
			}
			else if(LoginInfo.length == 3){
				sockInput.write(registry.insertUser(LoginInfo));	
			}	
		}

	}
}
