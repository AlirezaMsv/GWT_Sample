package com.mycompany.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
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
        tTab2.setPane(sign.buildForm());  
  
        topTabSet.addTab(tTab1);  
        topTabSet.addTab(tTab2); 
        
        HLayout buttons = new HLayout();  
        buttons.setMembersMargin(15);  
        
        IButton blueButton = new IButton("Select Blue");  
        blueButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				topTabSet.selectTab(0);  
			}
		});
		
        IButton greeButton = new IButton("Select Green");  
        greeButton.addClickHandler(new ClickHandler() {  
            public void onClick(ClickEvent event) {  
                topTabSet.selectTab(1);  
            }  
        });  
  
        buttons.addMember(greeButton);  
        buttons.addMember(blueButton);  
        
        VLayout vLayout = new VLayout();  
        vLayout.setMembersMargin(15);  
        vLayout.addMember(topTabSet);  
        vLayout.addMember(buttons);
        vLayout.setHeight("*");  
  
        RootPanel.get("loginTab").add(vLayout);
        
	}

}
