package com.mycompany.client;

import java.util.ArrayList;
import java.util.HashMap;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mycompany.server.DBManager;

public interface UsersServiceAsync {
	void fetchusers(Integer start, Integer end, AsyncCallback<ArrayList<HashMap<String, String>>> callback);
}
