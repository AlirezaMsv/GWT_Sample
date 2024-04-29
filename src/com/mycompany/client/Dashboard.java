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
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.data.Record;
import com.smartgwt.client.rpc.RPCManager;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.FetchMode;
import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.types.ListGridFieldType;  
import com.smartgwt.client.types.PromptStyle;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.grid.CellFormatter;  
import com.smartgwt.client.widgets.grid.ListGrid;  
import com.smartgwt.client.widgets.grid.ListGridField;  
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

public class Dashboard {
	
	private static Button logout_btn;
	private static TextAreaItem textAreaItem;
	private static ListGridRecord[] usersList = new ListGridRecord[1];
//	private static DataSource usersDataSource;
	private static ListGrid usersGrid;
	private static TabSet topTabSet;
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
		topTabSet = new TabSet();
		
        topTabSet.setTabBarPosition(Side.TOP);  
        topTabSet.setWidth("50%");  
        topTabSet.setHeight("80%");  
  
        Tab tTab1 = new Tab("TextArea");
        
        DynamicForm form = new DynamicForm();  
        form.setWidth100();  
        form.setHeight100();  
		
		textAreaItem = new TextAreaItem("textAreaItem");  
        textAreaItem.setTitle("JSON");  
        textAreaItem.setWidth("100%");  
        textAreaItem.setHeight("100%");  
        form.setFields(textAreaItem);
        
        tTab1.setPane(form);  
  
        Tab tTab2 = new Tab("Grid"); 
        RPCManager.setPromptStyle(PromptStyle.CURSOR);
        
	      ListGridField rowNum = new ListGridField("itemNum", "Item No.");  
	      rowNum.setAlign(Alignment.CENTER);
	      rowNum.setWidth(80);  
	      rowNum.setCellFormatter(new CellFormatter() {  
	          public String format(Object value, ListGridRecord record, int rowNum, int colNum) {  
	              return rowNum+1 +"";  
	          }  
	      });  
	
	      ListGridField firstname = new ListGridField("firstname", "Firstname", 120);  
	      firstname.setAlign(Alignment.CENTER);
	      ListGridField lastname = new ListGridField("lastname", "Lastname", 100);   
	      lastname.setAlign(Alignment.CENTER);
	      ListGridField age = new ListGridField("age", "Age", 100);  
	      age.setType(ListGridFieldType.INTEGER);  
	      age.setAlign(Alignment.CENTER);
	      ListGridField phoneNum = new ListGridField("phoneNum", "Phone Number", 200);   
	      phoneNum.setAlign(Alignment.CENTER);
	      phoneNum.setType(ListGridFieldType.PHONENUMBER);  
	      ListGridField email = new ListGridField("email", "Email");   
	      email.setAlign(Alignment.CENTER);

	      usersGrid = new ListGrid();  
	      usersGrid.setWidth100();
	      usersGrid.setHeight100();
	      usersGrid.setAutoFetchData(true);  
	      usersGrid.setDataPageSize(10);
	      usersGrid.setDataFetchMode(FetchMode.PAGED);
	
	      usersGrid.setFields(rowNum, firstname, lastname, age, phoneNum, email);  
	
	      tTab2.setPane(usersGrid);  
	  
	      topTabSet.addTab(tTab2); 
	      topTabSet.addTab(tTab1);  
        
        return topTabSet; 
        
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
				//grid
				setData(result);
				// text area
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

	private static void setData(ArrayList<HashMap<String, String>> result) {
//		usersDataSource = new DataSource();
//		for (String key : result.get(0).keySet()) {
//			usersDataSource.addField(new DataSourceField(key, FieldType.TEXT));
//		}
		for (int i = 0; i < result.size(); i++) {
			ListGridRecord record = new ListGridRecord();
			HashMap<String, String> user = result.get(i);
			record.setAttribute("firstname", user.get("firstname"));
			record.setAttribute("lastname", user.get("lastname"));
			record.setAttribute("age", user.get("age"));
			record.setAttribute("phoneNum", user.get("phoneNum"));
			record.setAttribute("email", user.get("email"));
			usersList[i] = record;
//			usersDataSource.addData(new Record(result.get(i)));
		}
		usersGrid.setData(usersList);
//		usersGrid.setDataSource(usersDataSource);
	}
}
