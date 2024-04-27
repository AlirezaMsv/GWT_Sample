package com.mycompany.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.core.client.EntryPoint;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;
import jsinterop.annotations.JsMethod;

public class SignUp implements EntryPoint {
	
	DynamicForm form;
	
	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	
	public DynamicForm buildForm() {
		if (form == null) {
			form =  new DynamicForm(); 
			onModuleLoad();
		}
		return form;
	}
	
	TextItem firstname;
	TextItem lastname;
	TextItem email;
	PasswordItem password;
	PasswordItem repeat;
	IntegerItem age;
	TextItem phoneNumber;
	ButtonItem buttonItem;

	@Override
	public void onModuleLoad() {
        form.setWidth(300);  
          
        firstname = new TextItem();  
        firstname.setName("firstname");  
        firstname.setTitle("Firstname:");
        firstname.setDefaultValue("");
        
        
        lastname = new TextItem();  
        lastname.setName("lastname");  
        lastname.setTitle("Lastname:");
        lastname.setDefaultValue("");  

        email = new TextItem();  
        email.setName("email");  
        email.setTitle("Email:");
        email.setDefaultValue("");  

        password = new PasswordItem();  
        password.setName("password");  
        password.setTitle("Password:");
        password.setDefaultValue("");  

        repeat = new PasswordItem();  
        repeat.setName("repeat");  
        repeat.setTitle("Repeat:");
        repeat.setDefaultValue("");  

        age = new IntegerItem();  
        age.setName("age");  
        age.setTitle("Age:");
        age.setDefaultValue("");  
        age.setKeyPressFilter("[0-9]");
        
        phoneNumber = new TextItem();  
        phoneNumber.setName("phoneNumber");  
        phoneNumber.setTitle("PhoneNumber:");
        phoneNumber.setDefaultValue("");  
  
        buttonItem = new ButtonItem();  
        buttonItem.setName("submit");  
        buttonItem.setTitle("Create account");
//        buttonItem.setBaseStyle("signupbtn");
        buttonItem.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				System.out.println(greetingService.getClass().toString());
//				if (empty(firstname.getValueAsString()) ||
//						empty(lastname.getValueAsString()) ||
//						empty(email.getValueAsString()) ||
//						empty(password.getValueAsString()) ||
//						empty(repeat.getValueAsString()) ||
//						empty(age.getValueAsString()) ||
//						empty(phoneNumber.getValueAsString())) {
//					SC.say("Error", "Please fill all fields!");
//				}
//				else if (checkFields()) {
					// create account
				greetingService.greetServer(firstname.getValueAsString(), new AsyncCallback<String>() {
					@Override
					public void onFailure(Throwable caught) {
						SC.say("Error", "sth went wrong");
					}

					@Override
					public void onSuccess(String result) {
						Window.alert(result);
						SC.say("Well Done");
					}
				  });
//				}
				
			}
		});
          
        form.setFields(firstname, lastname, email, password, repeat, age, phoneNumber, buttonItem);  
  
	}
	
	boolean checkFields() {
		//		email
        if (!checkEmail(email.getValueAsString())) {
        	SC.say("Error", "Invalid email address!");
        	return false;
        }
        
        // age
		int age = Integer.parseInt(this.age.getValueAsString());
		if (age > 80) {
			SC.say("Error", "Age can't be more than 80!");
			return false;
		}
		else if(age <= 4) {
			SC.say("Error", "Age can't be less than 4!");
			return false;
		}
		
		// password
		String pas = password.getValueAsString();
		String rep = repeat.getValueAsString();
		// equal pas and rep
		if (!pas.equals(rep)) {
			SC.say("Error", "Repeat password correctly!");
			return false;
		}
		// 
		if (pas.length() < 8) {
			SC.say("Error", "Password should be more than 8 charachters!");
			return false;
		}
		//
		boolean hasUppercase = false;
		boolean hasLowercase = false;
		boolean hasDigit = false;
		for (char ch : pas.toCharArray()) {
			if (Character.isUpperCase(ch)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(ch)) {
                hasLowercase = true;
            } else if (Character.isDigit(ch)) {
                hasDigit = true;
            }
		}
		if (!hasUppercase || !hasLowercase || !hasDigit) {
			SC.say("Error", "Password should contains both lowercase, uppercase and numbers!");
			return false;
		}
		
		//phone number
		String phone = phoneNumber.getValueAsString();
		if (phone.charAt(0) != '0' || phone.length() != 11) {
			SC.say("Error", "Phone Number is not valid!");
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