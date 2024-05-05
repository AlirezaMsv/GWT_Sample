package com.mycompany.client;

import java.util.ArrayList;
import java.util.HashMap;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.mycompany.server.DBManager;
import com.mycompany.shared.User;

@RemoteServiceRelativePath("users")
public interface UsersService extends RemoteService {
	ArrayList<HashMap<String, String>> fetchusers(Integer start, Integer end);
	HashMap<String, String> fetchUsersDetails(Integer id);
	Boolean updateUser(User user, Integer id);
	Boolean addUser(User user);
	Boolean removeUser(Integer id);
}
