package com.mycompany.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.mycompany.shared.User;
import com.smartgwt.client.data.DSRequest;
import com.smartgwt.client.data.DSResponse;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.types.PromptStyle;
import com.smartgwt.client.util.BooleanCallback;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;

public class UsersDS extends GwtRpcDataSource {
	
	enum Type {
		GRID,
		FORM,
		COMBOALL,
		COMBOOTHERS,
		TREE,
		PICK
	}
	
	Type type;
	
	private int id;
	
	private int openedID;
	
	public void setOpenedID(int openedID) {
		this.openedID = openedID;
	}

	public void setDSID(int id) {
		this.id = id;
	}
	
	private String searchValue = "";
	
	public void setSearchValue (String searchValue) {
		this.searchValue = searchValue;
	}
	
	private String[] selectedIds = null;
	
	public void setSelectedIds(String[] arr) {
		this.selectedIds = arr;
	}
	
	public void clearSelectedIds() {
		this.selectedIds = null;
	}
	
	ValueCallback cb;
	
	private final static UsersServiceAsync usersService = GWT.create(UsersService.class);
	
	public UsersDS(String[] fields, Type type){
		this.type = type;
		if (type.equals(Type.TREE)) {
			this.setID("treeDS");
		}
		else if(type.equals(Type.PICK)) {
			this.setID("pickDS");
		}
		for (String i : fields) {
			DataSourceField dt = new DataSourceField(i, FieldType.TEXT, i);
			if (i.equals("id")) {
				dt.setPrimaryKey(true);
			}
			else if (i.equals("parentID") && type.equals(Type.TREE)) {
				dt.setForeignKey("treeDS.parentID");
		        // dt.setRootValue("-1");
			}
			else if (i.equals("parentID") && type.equals(Type.PICK)) {
				dt.setForeignKey("pickDS.id");
			}
			this.addField(dt);
		}
	}	
	
	public UsersDS(String[] fields, Type type, int id, ValueCallback cb){
		this(fields, type);
		this.id = id;
		this.cb = cb;
	}	
	
	public UsersDS(String[] fields, Type type, ValueCallback cb){
		this(fields, type);
		this.cb = cb;
	}

	@Override
	protected void executeFetch(String requestId, DSRequest request, DSResponse response) {
		if (type == Type.GRID) {
			// fetch users
			RPCManager.setPromptStyle(PromptStyle.CURSOR);
			usersService.fetchusers(request.getStartRow(), request.getEndRow(), new AsyncCallback<ArrayList<HashMap<String, String>>>() {			
				@Override
				public void onSuccess(ArrayList<HashMap<String, String>> result) {
					//grid
					response.setTotalRows(Integer.parseInt(result.get(result.size() - 1).get("n")));
					result.remove(result.size() - 1);
					setGridData(result, response);
					// process response
					processResponse(requestId, response);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					SC.say("Error!");
				}
			});
		}
		
		else if (type == Type.FORM) {
			// fetch user details
			RPCManager.setPromptStyle(PromptStyle.CURSOR);
			usersService.fetchUsersDetails(id, new AsyncCallback<HashMap<String, String>> () {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					SC.say("Error", "Sth went wrong!");
				}

				@Override
				public void onSuccess(HashMap<String, String> result) {
					// TODO Auto-generated method stub
					Record[] res = {new Record(result)};
					response.setData(res);
					processResponse(requestId, response);
				}
				
			});
		}
		
		else if (type == Type.COMBOALL || type == Type.COMBOOTHERS) {
			if (request.getStartRow() == null || request.getEndRow() == null ) {
				request.setStartRow(0);
				request.setEndRow(100);
			}
			// fetch users
			RPCManager.setPromptStyle(PromptStyle.CURSOR);
			usersService.fetchCombo(type == Type.COMBOOTHERS ? id : -1, request.getStartRow(), request.getEndRow(), searchValue, new AsyncCallback<ArrayList<HashMap<String, String>>>() {			
				@Override
				public void onSuccess(ArrayList<HashMap<String, String>> result) {
					//grid
					response.setTotalRows(result.size() > 0 ? Integer.parseInt(result.get(result.size() - 1).get("n")) : 1);
					result.remove(result.size() - 1);
					setGridData(result, response);
					// process response
					processResponse(requestId, response);
					if (cb != null)
						cb.execute("");
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					SC.say("Error!");
				}
			});
		}
		// handle tree fetch
		else if (type.equals(Type.TREE) || type.equals(Type.PICK)) {
			// fetch users
			if (type.equals(Type.PICK)) {
				for (String i : request.getAttributes()) {
					GWT.log(i);
				}
			}
			RPCManager.setPromptStyle(PromptStyle.CURSOR);
			usersService.fetchTree(openedID, type.equals(Type.PICK) ? -1 : request.getStartRow(), type.equals(Type.PICK) ? -1 : request.getEndRow(), new AsyncCallback<ArrayList<HashMap<String, String>>>() {			
				@Override
				public void onSuccess(ArrayList<HashMap<String, String>> result) {
					//grid
					if (type.equals(Type.TREE)) {
						response.setTotalRows(Integer.parseInt(result.get(result.size() - 1).get("n")));
						result.remove(result.size() - 1);
					}
					else
						response.setTotalRows(result.size());
					setGridData(result, response);
					// process response
					processResponse(requestId, response);
				}
				
				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					SC.say("Error!");
				}
			});
		}
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
		Record record = request.getAttributeAsRecord("data");
		String PID = record.getAttributeAsString("parentInfo");
		User user = new User(record.getAttributeAsString("firstname"), 
				record.getAttributeAsString("lastname"), 
				record.getAttributeAsString("phoneNum"), 
				record.getAttributeAsString("email"), 
				record.getAttributeAsString("password"), 
				record.getAttributeAsString("age"));
		user.setParentID(PID);
		GWT.log(user.toString());
		usersService.addUser(user, new AsyncCallback<Boolean>() {
			
			@Override
			public void onSuccess(Boolean result) {
				// TODO Auto-generated method stub
				if (result) {
					cb.execute("");
				}
				else {
					SC.say("Error!", "Sth went wrong!");	
				}
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				SC.say("Error!", "Sth went wrong!");
			}
		});
	}

	@Override
	protected void executeUpdate(String requestId, DSRequest request, DSResponse response) {
		// TODO Auto-generated method stub
		Record record = request.getAttributeAsRecord("data");
		String PID = record.getAttributeAsString("parentInfo");
		User user = new User(record.getAttributeAsString("firstname"), 
				record.getAttributeAsString("lastname"), 
				record.getAttributeAsString("phoneNum"), 
				record.getAttributeAsString("email"), 
				record.getAttributeAsString("age")
		);
		user.setParentID(PID);
		GWT.log(user.toString());
		usersService.updateUser(user, id, new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				SC.say("Error!", "Sth went wrong!");
			}

			@Override
			public void onSuccess(Boolean result) {
				// TODO Auto-generated method stub
				if (result)
					cb.execute("");
				else
					SC.say("Error", "Sth went wrong!");
			}
		});
	}

	@Override
	protected void executeRemove(String requestId, DSRequest request, DSResponse response) {
		// TODO Auto-generated method stub
		SC.confirm("Warning!", "You are going to delete " 
					+ (selectedIds == null ? 1 : selectedIds.length) +
					" records."
				, new BooleanCallback() {
					
					@Override
					public void execute(Boolean value) {
						// TODO Auto-generated method stub
						if (value) {
							usersService.removeSelected(selectedIds == null ? 
									new String[] {request.getAttributeAsRecord("data").getAttribute("id")} : selectedIds
									, new AsyncCallback<Boolean>(){

								@Override
								public void onFailure(Throwable caught) {
									// TODO Auto-generated method stub
									SC.say("Error", "Sth went wrong!");
								}

								@Override
								public void onSuccess(Boolean result) {
									// TODO Auto-generated method stub
									if (result)
										cb.execute("");
									else
										SC.say("Error", "Sth went wrong!");
								}
								
							});
						}
					}
				});			
	}
	 
	
}
