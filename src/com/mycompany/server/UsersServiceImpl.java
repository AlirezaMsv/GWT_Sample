package com.mycompany.server;

import java.util.ArrayList;
import java.util.HashMap;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.mycompany.client.UsersService;
import com.mycompany.shared.User;

public class UsersServiceImpl extends RemoteServiceServlet implements UsersService {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public ArrayList<HashMap<String, String>> fetchusers(Integer start, Integer end){
		return DBManager.fetchUsers(start, end);
	}

	@Override
	public HashMap<String, String> fetchUsersDetails(Integer id) {
		return DBManager.fetchUsersDetails(id);
	}

	@Override
	public Boolean updateUser(User user, Integer id) {
		// TODO Auto-generated method stub
		return DBManager.updateUser(user, id);
	}

	@Override
	public Boolean addUser(User user) {
		// TODO Auto-generated method stub
		return DBManager.addUser(user);
	}

	@Override
	public Boolean removeSelected(String[] ids) {
		// TODO Auto-generated method stub
		return DBManager.removeSelected(ids);
	}

	@Override
	public ArrayList<HashMap<String, String>> fetchCombo(Integer id, Integer start, Integer end, String search) {
		// TODO Auto-generated method stub
		return DBManager.fetchCombo(id, start, end, search);
	}
	
}
