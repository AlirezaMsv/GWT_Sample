package com.mycompany.client;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.events.MouseDownEvent;
import com.smartgwt.client.widgets.events.MouseDownHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;

import com.google.gwt.user.client.Cookies;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.FetchMode;
import com.smartgwt.client.types.ListGridFieldType;  
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
//	private static ListGridRecord[] usersList = new ListGridRecord[1];
	private static ListGrid usersGrid = new ListGrid();
	private static TabSet topTabSet;
	private static boolean visible = false;

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
  
        // text Area
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
        
        // Grid
        Tab tTab2 = new Tab("Grid"); 
        
        // Columns
	      ListGridField rowNum = new ListGridField("itemNum", "Item No.");  
	      rowNum.setAlign(Alignment.CENTER);
	      rowNum.setWidth(80);  
	      rowNum.setCellFormatter(new CellFormatter() {  
	          public String format(Object value, ListGridRecord record, int rowNum, int colNum) {  
	              return rowNum+1 + "";  
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

	      usersGrid.setWidth100();
	      usersGrid.setHeight100();
	
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
		// set grid
//		usersGrid.setpage
		String[] fieldNames = {"firstname", "lastname", "age", "phoneNum", "email"};
		usersGrid.setDataSource(new UsersDS(value -> {
			textAreaItem.setValue(value);
		}, fieldNames));
        usersGrid.setAutoFetchData(true); 
        usersGrid.setDataFetchMode(FetchMode.PAGED);
		RootPanel.get("grid").add(getGrid());
	}
}
