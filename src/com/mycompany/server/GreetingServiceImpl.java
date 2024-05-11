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

  public String greetServer(User user) {
	  
//	  getThreadLocalRequest().getSession().set
	  
	  if(DBManager.addUser(user))
		  return "ok";
	  return "error";
  }
}
