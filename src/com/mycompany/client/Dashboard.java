package com.mycompany.client;

import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.mycompany.client.UsersDS.Type;
import com.smartgwt.client.util.SC;
import com.smartgwt.client.util.ValueCallback;
import com.smartgwt.client.widgets.Button;
import com.smartgwt.client.widgets.Canvas;
import com.smartgwt.client.widgets.IButton;
import com.smartgwt.client.widgets.Img;
import com.smartgwt.client.widgets.events.ClickEvent;
import com.smartgwt.client.widgets.events.ClickHandler;
import com.smartgwt.client.widgets.events.MouseDownEvent;
import com.smartgwt.client.widgets.events.MouseDownHandler;
import com.smartgwt.client.widgets.form.DynamicForm;
import com.smartgwt.client.widgets.form.fields.ComboBoxItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;

import java.util.ArrayList;

import com.google.gwt.user.client.Cookies;
import com.smartgwt.client.types.Alignment;
import com.smartgwt.client.types.DSOperationType;
import com.smartgwt.client.types.FetchMode;
import com.smartgwt.client.types.ListGridFieldType;
import com.smartgwt.client.types.SelectionAppearance;
import com.smartgwt.client.types.SelectionStyle;
import com.smartgwt.client.types.Side;
import com.smartgwt.client.widgets.grid.CellFormatter;  
import com.smartgwt.client.widgets.grid.ListGrid;  
import com.smartgwt.client.widgets.grid.ListGridField;  
import com.smartgwt.client.widgets.grid.ListGridRecord;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickEvent;
import com.smartgwt.client.widgets.grid.events.CellDoubleClickHandler;
import com.smartgwt.client.widgets.grid.events.SelectionUpdatedEvent;
import com.smartgwt.client.widgets.grid.events.SelectionUpdatedHandler;
import com.smartgwt.client.widgets.layout.HLayout;
import com.smartgwt.client.widgets.layout.VLayout;
import com.smartgwt.client.widgets.tab.Tab;
import com.smartgwt.client.widgets.tab.TabSet;

import jsinterop.annotations.JsMethod;

public class Dashboard {
	
	private static Button logout_btn;
	private static TextAreaItem textAreaItem;
	private static ListGrid usersGrid;
	private static TabSet topTabSet;
	private static boolean visible = false;

	// edit form items
	private static TextItem txtIDEdit;
	private static TextItem txtFirstnameEdit;
	private static TextItem txtLastnameEdit;
	private static TextItem txtEmailEdit;
	private static IntegerItem txtAgeEdit;
	private static TextItem txtPhoneNumEdit;
	private static ComboBoxItem comboParentEdit;
	// create form items
	private static TextItem txtFirstnameCreate;
	private static TextItem txtLastnameCreate;
	private static TextItem txtEmailCreate;
	private static IntegerItem txtAgeCreate;
	private static TextItem txtPhoneNumCreate;
	private static PasswordItem txtPasswordCreate;
	// buttons
	private static IButton createBtn;
	private static IButton editBtn;
	private static IButton cancelBtnEdit;
	private static IButton cancelBtnCreate;
	private static Canvas trash;

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
	
	private static Widget getTrash() {
		trash = new Canvas();
		Img trashIcon = new Img("http://127.0.0.1:8888/assets/img/delete.png", 40, 40);
        trashIcon.setPrompt("Delete");
        trash.addChild(trashIcon);
        trash.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				ArrayList<String> arr = new ArrayList<String>();
				for (ListGridRecord record : usersGrid.getSelectedRecords()) {
					arr.add(record.getAttribute("id"));
				}
				UsersDS ds = (UsersDS)usersGrid.getDataSource();
				ds.setSelectedIds(arr.toArray(new String[0]));
				usersGrid.removeSelectedData();
			}
		});
        trash.setVisible(false);
		return trash;
	}
	
	private static Widget getGrid() {
//		if (topTabSet != null)
//		{
//			usersGrid.invalidateCache();
//			return topTabSet;
//		}
		
		topTabSet = new TabSet();
		usersGrid = new ListGrid();
		
        topTabSet.setTabBarPosition(Side.TOP);  
        topTabSet.setWidth("45%");  
        topTabSet.setHeight("80%");  
  
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
	      ListGridField parentIDfield = new ListGridField("parentID", "Parent ID", 80);  
	      parentIDfield.setAlign(Alignment.CENTER);
//	      parentIDfield.setHidden(true);
	      ListGridField firstname = new ListGridField("firstname", "Firstname", 140);  
	      firstname.setAlign(Alignment.CENTER);
	      ListGridField lastname = new ListGridField("lastname", "Lastname", 140);   
	      lastname.setAlign(Alignment.CENTER);
	      ListGridField age = new ListGridField("age", "Age", 80);  
	      age.setType(ListGridFieldType.INTEGER);  
	      age.setAlign(Alignment.CENTER);
	      ListGridField parentName = new ListGridField("parentName", "Parent", 200);
	      parentName.setAlign(Alignment.CENTER);

	      usersGrid.setWidth100();
	      usersGrid.setHeight100();
	      
	      usersGrid.setFields(rowNum, firstname, lastname, parentName, parentIDfield, age);  
	      String[] gridFields = {"id", "parentID", "firstname", "lastname", "parentName", "age"};
	      UsersDS gridDS = new UsersDS(gridFields, Type.GRID, new ValueCallback() {
			
			@Override
			public void execute(String value) {
				// TODO Auto-generated method stub
				SC.say("Done", "Items removed successfully!");
				usersGrid.invalidateCache();
			}
		});
		  usersGrid.setDataSource(gridDS);
	      usersGrid.setAutoFetchData(true); 
	      usersGrid.setDataFetchMode(FetchMode.PAGED);
	      usersGrid.setCanRemoveRecords(true);
	      usersGrid.setSelectionType(SelectionStyle.SIMPLE);  
	      usersGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
	      usersGrid.addSelectionUpdatedHandler(new SelectionUpdatedHandler() {
			
			@Override
			public void onSelectionUpdated(SelectionUpdatedEvent event) {
				// TODO Auto-generated method stub
				if (usersGrid.getSelectedRecords().length > 0) {
					trash.setVisible(true);
				}
				else {
					trash.setVisible(false);
					gridDS.clearSelectedIds();
				}
			}
		});
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
	      txtAgeEdit.setKeyPressFilter("[0-9]");
	      txtPhoneNumEdit = new TextItem("phoneNum", "Phone Number:");
	      txtEmailEdit = new TextItem("email", "Email:");
	      comboParentEdit = new ComboBoxItem("parentInfo", "Parent");
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
	      
	      editForm.setFields(txtIDEdit, txtFirstnameEdit, txtLastnameEdit, comboParentEdit, txtAgeEdit, 
	    		  txtPhoneNumEdit, txtEmailEdit);
	        
	      editLayout.addMember(editForm);
	      editLayout.addMember(btns);
	      editTab.setPane(editLayout); 
	      
	      //add tab
	      Tab addTab = new Tab("Add");
	      HLayout addLayout = new HLayout();

		  String[] addFormFields = {"firstname", "lastname", "age", "phoneNum", "email", "password"};
	      UsersDS addFormDS = new UsersDS(addFormFields, Type.FORM, new ValueCallback() {

			@Override
			public void execute(String value) {
				// TODO Auto-generated method stub
				clearCreateForm();
				SC.say("Success", "User added successfully!");
				topTabSet.selectTab(0);
				usersGrid.invalidateCache();
			}
	      });
	      
	      DynamicForm addForm = new DynamicForm();  
	      addForm.setWidth("40%");  
	      addForm.setHeight100();
	      addForm.setDataSource(addFormDS);
	      
	      txtFirstnameCreate = new TextItem("firstname", "Firstname:");	      
	      txtLastnameCreate = new TextItem("lastname", "Lastname:");
	      txtAgeCreate = new IntegerItem("age", "Age:");
	      txtAgeCreate.setKeyPressFilter("[0-9]");
	      txtPhoneNumCreate = new TextItem("phoneNum", "Phone Number:");
	      txtEmailCreate = new TextItem("email", "Email:");
	      txtPasswordCreate = new PasswordItem("password", "Password");
	      btns = new VLayout();
	      btns.setMembersMargin(15);
	      createBtn = new IButton("Add");
	      createBtn.setWidth(100);
	      createBtn.setBackgroundColor("green");
	      createBtn.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) {
				// TODO Auto-generated method stub
				if (empty(addForm.getValueAsString("firstname")) ||
						empty(addForm.getValueAsString("lastname")) ||
						empty(addForm.getValueAsString("age")) ||
						empty(addForm.getValueAsString("email")) ||
						empty(addForm.getValueAsString("phoneNum"))
						) {
					SC.say("Error!", "Please fill all fields!");
				}
				//check fields
				if(checkFields(addForm.getValueAsString("email"), addForm.getValueAsString("age"), addForm.getValueAsString("phoneNum"))) {
					// execute update
					addForm.setSaveOperationType(DSOperationType.ADD);
					addForm.saveData();
				}
			}
		});
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
	    		  txtPhoneNumCreate, txtEmailCreate, txtPasswordCreate);
	        
	      addLayout.addMember(addForm);
	      addLayout.addMember(btns);
	      addTab.setPane(addLayout);
	      
	      //add tabs
	      topTabSet.addTab(gridTab); 
	      topTabSet.addTab(addTab); 
	      topTabSet.addTab(editTab); 
	      
		  topTabSet.disableTab(2);
	      
	      // define double click
	      usersGrid.addCellDoubleClickHandler(new CellDoubleClickHandler() {
			
			@Override
			public void onCellDoubleClick(CellDoubleClickEvent event) {
				// TODO Auto-generated method stub
				topTabSet.enableTab(2);
				ListGridRecord user = event.getRecord();
				// create datasource
				String[] editFormFields = {"id", "firstname", "lastname","parentInfo", "age", "phoneNum", "email"};
				editForm.setDataSource(new UsersDS(editFormFields, Type.FORM, Integer.parseInt(user.getAttribute("id")), new ValueCallback() {
					
					@Override
					public void execute(String value) {
						// TODO Auto-generated method stub
						SC.say("Success", "Updated user info successfully!");
						topTabSet.selectTab(0);
						topTabSet.disableTab(2);
						//reset form from datasource
						
						//reset manually
						clearEditForm();
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
			RootPanel.get("trash").remove(0);
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
		RootPanel.get("grid").add(getGrid());
		RootPanel.get("trash").add(getTrash());
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
