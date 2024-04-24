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

import jsinterop.annotations.JsMethod;

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
//        buttonItem.setBaseStyle("loginbtn");
        buttonItem.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				if (empty(email.getValueAsString()) ||
				empty(email.getValueAsString())) {
					SC.say("Error", "Please fill all fields!");
				}
				else if (checkFields()) {
					// create account
					SC.say("Well Done");
				}
			}
		});
          
        form.setFields(email, password, buttonItem);  
  
	}
	
	boolean checkFields() {
		//		email
        if (!checkEmail(email.getValueAsString())) {
        	SC.say("Error", "Invalid email address!");
        	return false;
        }
		return true;
	}
	
	
	public static boolean empty( final String s ) {
	  // Null-safe, short-circuit evaluation.
	  return s == null || s.trim().isEmpty();
	}
	
	// Declare JavaScript function using JsMethod annotation
	@JsMethod(namespace = "window", name = "checkEmail")
	public static native boolean checkEmail(String email);

}
