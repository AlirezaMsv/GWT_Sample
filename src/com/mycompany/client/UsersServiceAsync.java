package com.mycompany.client;

import java.util.ArrayList;
import java.util.HashMap;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mycompany.server.DBManager;
import com.mycompany.shared.User;

public interface UsersServiceAsync {
	void fetchusers(Integer start, Integer end, AsyncCallback<ArrayList<HashMap<String, String>>> callback);
	void fetchUsersDetails(Integer id, AsyncCallback<HashMap<String, String>> callback);
	void updateUser(User user, Integer id, AsyncCallback<Boolean> callback);
	void addUser(User user, AsyncCallback<Boolean> callback);
	void removeUser(Integer id, AsyncCallback<Boolean> callback);
}
