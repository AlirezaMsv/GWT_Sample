package com.mycompany.server;

import com.mycompany.client.GreetingService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class GreetingServiceImpl extends RemoteServiceServlet implements
    GreetingService {

  public String greetServer(String firstname) {
	  System.out.print(firstname);
    return "Hello " + firstname;
  }
}
