package com.mycompany.client;

import java.util.ArrayList;
import java.util.HashMap;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface UsersServiceAsync {
	void fetchusers(AsyncCallback<ArrayList<HashMap<String, String>>> callback);
}
