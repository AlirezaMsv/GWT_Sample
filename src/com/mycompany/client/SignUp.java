package com.mycompany.client;


import com.google.gwt.core.client.EntryPoint;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.events.ClickEvent;
import com.smartgwt.client.widgets.form.fields.events.ClickHandler;

public class SignUp implements EntryPoint {
	
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
        buttonItem.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				
				if (firstname.getValueAsString().isEmpty() ||
				lastname.getValueAsString().isEmpty() ||
				email.getValueAsString().isEmpty() ||
				password.getValueAsString().isEmpty() ||
				repeat.getValueAsString().isEmpty() ||
				age.getValueAsString().isEmpty() ||
				phoneNumber.getValueAsString().isEmpty()) {
					SC.say("Error", "Please fill all fields!");
				}
				
			}
		});
          
        form.setFields(firstname, lastname, email, password, repeat, age, phoneNumber, buttonItem);  
  
	}
	
	boolean checkFields() {
		//		email
//		Matcher matcher = pattern.matcher(email.getValueAsString());
//        if (!matcher.matches()) {
//        	SC.say("Error", email.getValueAsString());
//        	return false;
//        }
        
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
		String pas = password.getValueAsString().split(" ")[0];
		String rep = password.getValueAsString().split(" ")[1];
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
		}
		//
		
		return true;
	}

}