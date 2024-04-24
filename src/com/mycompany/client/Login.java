package com.mycompany.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ButtonItem;
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
				// login
				else if (checkFields()) {
					// login
					login(email.getValueAsString(), password.getValueAsString(), new AsyncCallback<String>() {
					    @Override
					    public void onSuccess(String result) {
					        // Handle successful login
					    	SC.say("Welcome", "logged in");
					    }

					    @Override
					    public void onFailure(Throwable caught) {
					        // Handle login failure
					    	SC.say("Error", "Username or password is wrong!");
					    }
					});
					// SC.say("Well Done");
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

	
	// calling api
	public static void login(String username, String password, final AsyncCallback<String> callback) {
        // String url = "https://api.coindesk.com/v1/bpi/currentprice.json"; // URL of your login endpoint
        String url = "/login"; // URL of your login endpoint
        
        // Create request builder
        RequestBuilder builder = new RequestBuilder(RequestBuilder.POST, URL.encode(url));
        builder.setHeader("Content-Type", "application/x-www-form-urlencoded");
        
        // Prepare request data
        String requestData = "username=" + URL.encodeQueryString(username) + "&password=" + URL.encodeQueryString(password);
        
        try {
            builder.sendRequest(requestData, new RequestCallback() {
                @Override
                public void onResponseReceived(Request request, Response response) {
                    if (response.getStatusCode() == Response.SC_OK) {
                        callback.onSuccess(response.getText());
                    } else {
                        callback.onFailure(new Throwable(response.getStatusText()));
                    }
                }

                @Override
                public void onError(Request request, Throwable exception) {
                    callback.onFailure(exception);
                }
            });
        } catch (RequestException e) {
            callback.onFailure(e);
        }
    }
}
