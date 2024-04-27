package com.mycompany.server;

import com.mycompany.client.GreetingService;
import com.mycompany.shared.User;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
    GreetingService {

  public String greetServer(String firstname, String lastname, String phoneNum, String email, String password, short age) {
	  if(DBManager.addUser(new User(firstname, lastname, phoneNum, email, password, age)))
		  return "ok";
	  return "error";
  }
}
