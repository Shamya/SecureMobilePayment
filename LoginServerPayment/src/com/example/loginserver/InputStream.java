package com.example.loginserver;

public interface InputStream {
	public void init();
	public String read();
	public void write(String op);
}
