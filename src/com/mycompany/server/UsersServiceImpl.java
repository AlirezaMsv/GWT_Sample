package com.mycompany.server;

import java.util.ArrayList;
import java.util.HashMap;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mycompany.client.UsersService;

public class UsersServiceImpl extends RemoteServiceServlet implements UsersService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public ArrayList<HashMap<String, String>> fetchusers(Integer start, Integer end){
		return DBManager.fetchUsers(start, end);
	}
}
