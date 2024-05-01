package com.mycompany.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.types.PromptStyle;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;

public class UsersDS extends GwtRpcDataSource {
	
	private final static UsersServiceAsync usersService = GWT.create(UsersService.class);
	// public final static UsersDS usersDataSource = new UsersDS();
	public ValueCallback textAreaCB;
	
	public UsersDS(ValueCallback textAreaCB, String[] fields)
	{
		for (String i : fields) {
			this.addField(new DataSourceField(i, FieldType.TEXT, i));
		}
		this.textAreaCB = textAreaCB;
	}
	
	private void setText(ArrayList<HashMap<String, String>> result) {
		StringBuilder str = new StringBuilder();
		for (int i = 0 ; i < result.size(); i++) {
			str.append("{\n").append("\tfirstname: ").append(result.get(i).get("firstname"));
			str.append(",\n");
			str.append("\tlastname: ").append(result.get(i).get("lastname"));
			str.append(",\n");
			str.append("\tage: ").append(result.get(i).get("age"));
			str.append(",\n");
			str.append("\temail: ").append(result.get(i).get("email"));
			str.append(",\n");
			str.append("\tphoneNum: ").append(result.get(i).get("phoneNum"));
			str.append("\n");
			str.append("}\n");
		}
		textAreaCB.execute(str.toString());
	}
	

	@Override
	protected void executeFetch(String requestId, DSRequest request, DSResponse response) {
		GWT.log("start: " + request.getStartRow());
		GWT.log("len: " + (request.getEndRow() - request.getStartRow()));
		// fetch users
		RPCManager.setPromptStyle(PromptStyle.CURSOR);
		usersService.fetchusers(request.getStartRow(), request.getEndRow(), new AsyncCallback<ArrayList<HashMap<String, String>>>() {			
			@Override
			public void onSuccess(ArrayList<HashMap<String, String>> result) {
				//grid
				response.setTotalRows(Integer.parseInt(result.get(0).get("n")));
				result.remove(0);
				setGridData(result, response);
				// text area
				setText(result);
				processResponse(requestId, response);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				SC.say("Error!");
			}
		});
	}
	
	private void setGridData(ArrayList<HashMap<String, String>> result, DSResponse response) {
		List<Record> recs = new ArrayList<>();
		for (HashMap<String, String> user : result) {
			recs.add(new Record(user));
		}
		response.setData(recs.toArray(new Record[0]));
	}

	@Override
	protected void executeAdd(String requestId, DSRequest request, DSResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void executeUpdate(String requestId, DSRequest request, DSResponse response) {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void executeRemove(String requestId, DSRequest request, DSResponse response) {
		// TODO Auto-generated method stub
		
	}
	 
	
}
