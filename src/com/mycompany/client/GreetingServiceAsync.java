package com.mycompany.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
  void greetServer(String firstname, String lastname, String phoneNum, String email, String password, short age, AsyncCallback<String> callback);
}
