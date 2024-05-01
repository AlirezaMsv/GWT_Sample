package com.mycompany.client;

import java.util.ArrayList;
import java.util.HashMap;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.mycompany.server.DBManager;

@RemoteServiceRelativePath("users")
public interface UsersService extends RemoteService {
	ArrayList<HashMap<String, String>> fetchusers(Integer start, Integer end);
}
