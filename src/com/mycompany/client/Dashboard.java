package com.mycompany.client;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.events.MouseDownEvent;
import com.smartgwt.client.widgets.events.MouseDownHandler;
import com.google.gwt.user.client.Cookies;

public class Dashboard {
	
	private static Button btn;

	private static Widget build() {
		btn = new Button("Logout");
		btn.addMouseDownHandler(new MouseDownHandler() {
			
			@Override
			public void onMouseDown(MouseDownEvent event) {
				// logout
				Cookies.removeCookie("isLoggedIn");
				SC.say("logged out");
				RootPanel.get("logout_btn").remove(0);
				RootPanel.get("loginTab").add(SignUpOrLoginTab.getWidget());
			}
		});
		return btn;
	}
	
	public static Widget getWidget() {
		if (btn == null)
			build();
		return btn;
	}
}
