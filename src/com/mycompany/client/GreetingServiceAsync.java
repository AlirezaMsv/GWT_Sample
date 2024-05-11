package com.mycompany.client;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mycompany.shared.User;

/**
 * The async counterpart of <code>GreetingService</code>.
 */
public interface GreetingServiceAsync {
  void greetServer(User user, AsyncCallback<String> callback);
}
