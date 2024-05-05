package com.mycompany.client;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mycompany.client.UsersDS.Type;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.MouseDownEvent;
import com.smartgwt.client.widgets.events.MouseDownHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Cookies;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DSOperationType;
import com.smartgwt.client.types.FetchMode;
import com.smartgwt.client.types.ListGridFieldType;  
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.grid.CellFormatter;  
import com.smartgwt.client.widgets.grid.ListGrid;  
import com.smartgwt.client.widgets.grid.ListGridField;  
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

import jsinterop.annotations.JsMethod;

public class Dashboard {
	
	private static Button logout_btn;
	private static TextAreaItem textAreaItem;
	private static ListGrid usersGrid = new ListGrid();
	private static TabSet topTabSet;
	private static boolean visible = false;

	// form items
	private static TextItem txtIDEdit;
	private static TextItem txtFirstnameEdit;
	private static TextItem txtLastnameEdit;
	private static TextItem txtEmailEdit;
	private static IntegerItem txtAgeEdit;
	private static TextItem txtPhoneNumEdit;
	private static TextItem txtFirstnameCreate;
	private static TextItem txtLastnameCreate;
	private static TextItem txtEmailCreate;
	private static IntegerItem txtAgeCreate;
	private static TextItem txtPhoneNumCreate;
	private static IButton createBtn;
	private static IButton editBtn;
	private static IButton cancelBtnEdit;
	private static IButton cancelBtnCreate;

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
        topTabSet.setWidth("25%");  
        topTabSet.setHeight("80%");  
  
        // text Area
        Tab textTab = new Tab("TextArea");
        
        DynamicForm form = new DynamicForm();  
        form.setWidth100();  
        form.setHeight100();  
		
		textAreaItem = new TextAreaItem("textAreaItem");  
        textAreaItem.setTitle("JSON");  
        textAreaItem.setWidth("100%");  
        textAreaItem.setHeight("100%");  
        form.setFields(textAreaItem);
        
        textTab.setPane(form); 
        
        // Grid
        Tab gridTab = new Tab("Grid"); 
        
        // Columns
	      ListGridField rowNum = new ListGridField("itemNum", "Item No.");  
	      rowNum.setAlign(Alignment.CENTER);
	      rowNum.setWidth(80);  
	      rowNum.setCellFormatter(new CellFormatter() {  
	          public String format(Object value, ListGridRecord record, int rowNum, int colNum) {  
	              return rowNum+1 + "";  
	          }
	      });
	      
	      
	      ListGridField idfield = new ListGridField("id");  
	      idfield.setHidden(true);
	      ListGridField firstname = new ListGridField("firstname", "Firstname", 120);  
	      firstname.setAlign(Alignment.CENTER);
	      ListGridField lastname = new ListGridField("lastname", "Lastname", 120);   
	      lastname.setAlign(Alignment.CENTER);
	      ListGridField age = new ListGridField("age", "Age", 100);  
	      age.setType(ListGridFieldType.INTEGER);  
	      age.setAlign(Alignment.CENTER);
//	      ListGridField phoneNum = new ListGridField("phoneNum", "Phone Number", 200);   
//	      phoneNum.setAlign(Alignment.CENTER);
//	      phoneNum.setType(ListGridFieldType.PHONENUMBER);  
//	      ListGridField email = new ListGridField("email", "Email");   
//	      email.setAlign(Alignment.CENTER);

	      usersGrid.setWidth100();
	      usersGrid.setHeight100();
	      usersGrid.setCanRemoveRecords(true);
	      usersGrid.setFields(rowNum, firstname, lastname, age);  
	
	      gridTab.setPane(usersGrid);  
	  	      
	      //edit tab
	      Tab editTab = new Tab("Edit");
	      HLayout editLayout = new HLayout();
	      
	      DynamicForm editForm = new DynamicForm();  
	      editForm.setWidth("40%");  
	      editForm.setHeight100();
	      
	      txtIDEdit = new TextItem("id", "Database Id:");
	      txtIDEdit.setDisabled(true);
	      txtFirstnameEdit = new TextItem("firstname", "Firstname:");	      
	      txtLastnameEdit = new TextItem("lastname", "Lastname:");
	      txtAgeEdit = new IntegerItem("age", "Age:");
	      txtPhoneNumEdit = new TextItem("phoneNum", "Phone Number:");
	      txtEmailEdit = new TextItem("email", "Email:");
	      VLayout btns = new VLayout();
	      btns.setMembersMargin(15);
	      editBtn = new IButton("Edit");
	      editBtn.setWidth(100);
	      editBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				if (empty(editForm.getValueAsString("firstname")) ||
						empty(editForm.getValueAsString("lastname")) ||
						empty(editForm.getValueAsString("age")) ||
						empty(editForm.getValueAsString("email")) ||
						empty(editForm.getValueAsString("phoneNum"))
						) {
					SC.say("Error!", "Please fill all fields!");
				}
				//check fields
				if(checkFields(editForm.getValueAsString("email"), editForm.getValueAsString("age"), editForm.getValueAsString("phoneNum"))) {
					// execute update
					GWT.log(editForm.getValueAsString("firstname"));
					GWT.log(editForm.getValueAsString("lastname"));
					GWT.log(editForm.getValueAsString("age"));
					GWT.log(editForm.getValueAsString("email"));
					GWT.log(editForm.getValueAsString("phoneNum"));
					editForm.setSaveOperationType(DSOperationType.UPDATE);
					editForm.saveData();
				}
			}
		});
	      cancelBtnEdit = new IButton("Cancel");
	      cancelBtnEdit.setWidth(100);
	      cancelBtnEdit.setBackgroundColor("red");
	      cancelBtnEdit.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				topTabSet.selectTab(0);
				topTabSet.disableTab(2);
				//reset form from datasource
				
				//reset manually
				clearEditForm();
			}
		});
	      btns.addMember(editBtn);
	      btns.addMember(cancelBtnEdit);
	      
	      editForm.setFields(txtIDEdit, txtFirstnameEdit, txtLastnameEdit, txtAgeEdit, 
	    		  txtPhoneNumEdit, txtEmailEdit);
	        
	      editLayout.addMember(editForm);
	      editLayout.addMember(btns);
	      editTab.setPane(editLayout); 
	      
	      //add tab
	      Tab addTab = new Tab("Add");
	      HLayout addLayout = new HLayout();
	      
	      DynamicForm addForm = new DynamicForm();  
	      addForm.setWidth("40%");  
	      addForm.setHeight100();
	      
	      txtFirstnameCreate = new TextItem("firstname", "Firstname:");	      
	      txtLastnameCreate = new TextItem("lastname", "Lastname:");
	      txtAgeCreate = new IntegerItem("age", "Age:");
	      txtPhoneNumCreate = new TextItem("phoneNum", "Phone Number:");
	      txtEmailCreate = new TextItem("email", "Email:");
	      btns = new VLayout();
	      btns.setMembersMargin(15);
	      createBtn = new IButton("Add");
	      createBtn.setWidth(100);
	      createBtn.setBackgroundColor("green");
	      cancelBtnCreate = new IButton("Cancel");
	      cancelBtnCreate.setWidth(100);
	      cancelBtnCreate.setBackgroundColor("red");
	      cancelBtnCreate.addClickHandler(new ClickHandler() {
				
				@Override
				public void onClick(ClickEvent event) {
					// TODO Auto-generated method stub
					// reset from datasource
					
					//reset form manually
					topTabSet.selectTab(0);
					clearCreateForm();
				}
			});
	      btns.addMember(createBtn);
	      btns.addMember(cancelBtnCreate);
	      
	      addForm.setFields(txtFirstnameCreate, txtLastnameCreate, txtAgeCreate, 
	    		  txtPhoneNumCreate, txtEmailCreate);
	        
	      addLayout.addMember(addForm);
	      addLayout.addMember(btns);
	      addTab.setPane(addLayout);
	      
	      //add tabs
	      topTabSet.addTab(gridTab); 
	      topTabSet.addTab(addTab); 
	      topTabSet.addTab(editTab); 
//	      topTabSet.addTab(textTab);  
		  topTabSet.disableTab(2);
	      
	      // define double click
	      usersGrid.addCellDoubleClickHandler(new CellDoubleClickHandler() {
			
			@Override
			public void onCellDoubleClick(CellDoubleClickEvent event) {
				// TODO Auto-generated method stub
				topTabSet.enableTab(2);
				ListGridRecord user = event.getRecord();
				// create datasource
				String[] fieldNames = {"id", "firstname", "lastname", "age", "phoneNum", "email"};
				editForm.setDataSource(new UsersDS(fieldNames, Type.LIST, Integer.parseInt(user.getAttribute("id")), new ValueCallback() {
					
					@Override
					public void execute(String value) {
						// TODO Auto-generated method stub
						SC.say("Success", "Updated user info successfully!");
						// TODO Auto-generated method stub
						topTabSet.selectTab(0);
						topTabSet.disableTab(2);
						//reset form from datasource
						
						//reset manually
						clearEditForm();
						// usersGrid.setFetchOperation("fetch");
						usersGrid.invalidateCache();
					}
				}));
				editForm.setSaveOperationType(DSOperationType.FETCH);
				editForm.fetchData();
				//
				topTabSet.selectTab(2);
			}
		});
        
        return topTabSet; 
        
	}
	
	public static void remove() {
		if (visible) {
			visible = false;
			RootPanel.get("logout_btn").remove(0);
			RootPanel.get("grid").remove(0);
		}
	}
	
	private static void clearEditForm() {
		txtIDEdit.setValue("");
		txtFirstnameEdit.setValue("");
		txtLastnameEdit.setValue("");
		txtAgeEdit.setValue("");
		txtPhoneNumEdit.setValue("");
		txtEmailEdit.setValue("");
	}
	
	private static void clearCreateForm() {
		txtFirstnameCreate.setValue("");
		txtLastnameCreate.setValue("");
		txtAgeCreate.setValue("");
		txtPhoneNumCreate.setValue("");
		txtEmailCreate.setValue("");
	}
	
	public static void show() {
		visible = true;
		RootPanel.get("logout_btn").add(getButton());
		// set grid
//		usersGrid.setpage
		String[] fieldNames = {"id", "firstname", "lastname", "age"};
		usersGrid.setDataSource(new UsersDS(fieldNames, Type.GRID));
        usersGrid.setAutoFetchData(true); 
        usersGrid.setDataFetchMode(FetchMode.PAGED);
		RootPanel.get("grid").add(getGrid());
	}
	
	static boolean checkFields(String email, String age, String phone) {
		//		email
        if (!checkEmail(email)) {
        	SC.say("Error", "Invalid email address!");
        	return false;
        }
        
        // age
		if (Integer.parseInt(age) > 80) {
			SC.say("Error", "Age can't be more than 80!");
			return false;
		}
		else if(Integer.parseInt(age) <= 4) {
			SC.say("Error", "Age can't be less than 4!");
			return false;
		}
		
		//phone number
		if (phone.charAt(0) != '0' || phone.length() != 11) {
			SC.say("Error", "Phone Number is not valid!");
			return false;
		}
		
		return true;
	}

	public static boolean empty( final String s ) {
	  // Null-safe, short-circuit evaluation.
	  return s == null || s.trim().isEmpty();
	}

    // Declare JavaScript function using JsMethod annotation
    @JsMethod(namespace = "window", name = "checkEmail")
    public static native boolean checkEmail(String email);
	
}
