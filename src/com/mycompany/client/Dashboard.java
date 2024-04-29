package com.mycompany.client;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.events.MouseDownEvent;
import com.smartgwt.client.widgets.events.MouseDownHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsonUtils;
import com.google.gwt.json.client.JSONParser;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DataSource;  
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.FetchMode;
import com.smartgwt.client.types.ListGridFieldType;  
import com.smartgwt.client.types.PromptStyle;
import com.smartgwt.client.widgets.grid.CellFormatter;  
import com.smartgwt.client.widgets.grid.ListGrid;  
import com.smartgwt.client.widgets.grid.ListGridField;  
import com.smartgwt.client.widgets.grid.ListGridRecord;

public class Dashboard {
	
	private static Button logout_btn;
	private static TextAreaItem textAreaItem;
	private static boolean visible = false;
	private final static UsersServiceAsync usersService = GWT.create(UsersService.class);

	private static Widget buildLogoutBtn() {
		logout_btn = new Button("Logout");
		logout_btn.addMouseDownHandler(new MouseDownHandler() {
			
			@Override
			public void onMouseDown(MouseDownEvent event) {
				// logout
				Cookies.removeCookie("isLoggedIn");
				SC.say("logged out");
				remove();
				SignUpOrLoginTab.show();
			}
		});
		return logout_btn;
	}
	
	private static Widget getButton() {
		if (logout_btn == null)
			buildLogoutBtn();
		return logout_btn;
	}
	
	private static Widget getGrid() {
//		RPCManager.setPromptStyle(PromptStyle.CURSOR);
//        DataSource dataSource = UsersDS.getInstance();  
//  
//        ListGridField rowNum = new ListGridField("itemNum", "Item No.");  
//        rowNum.setAlign(Alignment.CENTER);
//        rowNum.setWidth(80);  
//        rowNum.setCellFormatter(new CellFormatter() {  
//            public String format(Object value, ListGridRecord record, int rowNum, int colNum) {  
//                return rowNum +"";  
//            }  
//        });  
//  
//        ListGridField firstname = new ListGridField("Firstname", 120);  
//        firstname.setAlign(Alignment.CENTER);
//        ListGridField lastname = new ListGridField("Lastname", 100);   
//        lastname.setAlign(Alignment.CENTER);
//        ListGridField age = new ListGridField("Age", 100);  
//        age.setType(ListGridFieldType.INTEGER);  
//        age.setAlign(Alignment.CENTER);
//        ListGridField phoneNum = new ListGridField("Phone Number", 200);   
//        phoneNum.setAlign(Alignment.CENTER);
//        phoneNum.setType(ListGridFieldType.PHONENUMBER);  
//        ListGridField email = new ListGridField("Email");   
//        email.setAlign(Alignment.CENTER);
//          
//        final ListGrid listGrid = new ListGrid();  
//        listGrid.setWidth("50%");
//        listGrid.setHeight("80%");
//        listGrid.setAutoFetchData(true);  
//        listGrid.setDataPageSize(10);
//        listGrid.setDataFetchMode(FetchMode.PAGED);
//        listGrid.setDataSource(dataSource);  
//  
//        listGrid.setFields(rowNum, firstname, lastname, age, phoneNum, email);  
//  
//        return listGrid;  
		DynamicForm form = new DynamicForm();  
        form.setWidth100();  
        form.setHeight(500);  
		
		textAreaItem = new TextAreaItem("textAreaItem");  
        textAreaItem.setTitle("JSON");  
        textAreaItem.setWidth("100%");  
        textAreaItem.setHeight("100%");  
        form.setFields(textAreaItem);
        return form;
	}
	
	public static void remove() {
		if (visible) {
			visible = false;
			RootPanel.get("logout_btn").remove(0);
			RootPanel.get("grid").remove(0);
		}
	}
	
	public static void show() {
		visible = true;
		RootPanel.get("logout_btn").add(getButton());
		RootPanel.get("grid").add(getGrid());
		usersService.fetchusers(new AsyncCallback<ArrayList<HashMap<String, String>>>() {			
			@Override
			public void onSuccess(ArrayList<HashMap<String, String>> result) {
				StringBuilder text = new StringBuilder();
				for (int i = 0 ; i < result.size(); i++) {
					text.append("{\n").append("\tfirstname: ").append(result.get(i).get("firstname"));
					text.append(",\n");
					text.append("\tlastname: ").append(result.get(i).get("lastname"));
					text.append(",\n");
					text.append("\tage: ").append(result.get(i).get("age"));
					text.append(",\n");
					text.append("\temail: ").append(result.get(i).get("email"));
					text.append(",\n");
					text.append("\tphoneNum: ").append(result.get(i).get("phoneNum"));
					text.append("\n");
					text.append("}\n");
				}
				textAreaItem.setValue(text);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				// TODO Auto-generated method stub
				SC.say("Error!");
			}
		});
	}
}
