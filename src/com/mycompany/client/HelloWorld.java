package com.mycompany.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;

public class HelloWorld implements EntryPoint {
	
	
	public void onModuleLoad() {
		if("true".equalsIgnoreCase(Cookies.getCookie("isLoggedIn"))) {
			RootPanel.get("logout_btn").add(Dashboard.getWidget());
			RootPanel.get("loginTab").remove(0);
		}
		else {
			RootPanel.get("loginTab").add(SignUpOrLoginTab.getWidget());
			RootPanel.get("logout_btn").remove(0);
		}
	}
}