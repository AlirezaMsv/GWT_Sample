package com.mycompany.client;

import com.google.gwt.core.client.EntryPoint;

public class HelloWorld implements EntryPoint {
	
	public void onModuleLoad() {
		SignUpOrLoginTab tab = new SignUpOrLoginTab();
		tab.onModuleLoad();
	}
}