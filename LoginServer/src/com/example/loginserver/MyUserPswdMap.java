package com.example.loginserver;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MyUserPswdMap implements UserPswdMap {
	
	HashMap<String, String> LoginLst;
	
	MyUserPswdMap() {
		LoginLst = new HashMap<String, String>();
	}
	
	public String insertUser (String[] LoginInfo) {
		System.out.println("\nRegistration Success");
		System.out.println("Stored UserName: " + LoginInfo[1] + " Password: " + LoginInfo[2]);
		this.LoginLst.put(LoginInfo[1], LoginInfo[2]);
		return "registration success";
		
	}
	public String findUser (String[] LoginInfo) {
		boolean keyFound = false;
		Set<?> mapSet = (Set<?>) this.LoginLst.entrySet();
		Iterator<?> mapIterator = mapSet.iterator();
		while (mapIterator.hasNext()) 
		{
			@SuppressWarnings("rawtypes")
			Map.Entry mapEntry = (Map.Entry) mapIterator.next();
			String keyValue = (String) mapEntry.getKey();
			if(keyValue.equals(LoginInfo[0])){
				String value = mapEntry.getValue().toString();
				if(value.equals(LoginInfo[1])){
					keyFound = true;
					break;
				}
			}
		}
		if(!keyFound){
			System.out.println("\nLogin Failed");
			return "login failure";
		} else {
			System.out.println("\nLogin Success");
			System.out.println("Client msg: \nUserId - " + LoginInfo[0] + "\nHash_Password - " + LoginInfo[1]);
			return "login success";
		}
	}
}
