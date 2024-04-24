package com.mycompany.client;

import com.google.gwt.core.client.EntryPoint;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

public class Login implements EntryPoint {

	DynamicForm form;
//	private static final String EMAIL_REGEX =
//            "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
//
//    private static final Pattern pattern = Pattern.compile(EMAIL_REGEX);
	
	public DynamicForm buildForm() {
		if (form == null) {
			form =  new DynamicForm(); 
			onModuleLoad();
		}
		return form;
	}
	TextItem email;
	PasswordItem password;
	ButtonItem buttonItem;

	@Override
	public void onModuleLoad() {
        form.setWidth(300);  
        

        email = new TextItem();  
        email.setName("email");  
        email.setTitle("Email:");
        email.setDefaultValue("");  

        password = new PasswordItem();  
        password.setName("password");  
        password.setTitle("Password:");
        password.setDefaultValue("");  

  
        buttonItem = new ButtonItem();  
        buttonItem.setName("submit");  
        buttonItem.setTitle("Login");
        buttonItem.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				if (email.getValueAsString().isEmpty() ||
				password.getValueAsString().isEmpty()) {
					SC.say("Error", "Please fill all fields!");
				}
				
			}
		});
          
        form.setFields(email, password, buttonItem);  
  
	}
	

}
