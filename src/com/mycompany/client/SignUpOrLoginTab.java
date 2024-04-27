package com.mycompany.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class SignUpOrLoginTab implements EntryPoint {

	@Override
	public void onModuleLoad() {

        final TabSet topTabSet = new TabSet();  
        topTabSet.setTabBarPosition(Side.TOP);  
        topTabSet.setWidth(350);  
        topTabSet.setHeight(300);  
  
        Tab tTab1 = new Tab("Login");
//        Img tImg1 = new Img("pieces/48/pawn_blue.png", 48, 48);
        Login login = new Login();
        tTab1.setPane(login.buildForm()); 
  
        Tab tTab2 = new Tab("Sign Up"); 
//        Img tImg2 = new Img("pieces/48/pawn_green.png", 48, 48);  
        SignUp sign = new SignUp();
        tTab2.setPane(sign.buildForm(topTabSet));  
  
        topTabSet.addTab(tTab1);  
        topTabSet.addTab(tTab2); 
             
        VLayout vLayout = new VLayout();  
        vLayout.setMembersMargin(15);  
        vLayout.addMember(topTabSet); 
        vLayout.setHeight("*");  
  
        RootPanel.get("loginTab").add(vLayout);
        
	}

}
