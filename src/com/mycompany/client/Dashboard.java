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
import com.smartgwt.client.widgets.form.fields.IPickTreeItem;
import com.smartgwt.client.widgets.form.fields.IntegerItem;
import com.smartgwt.client.widgets.form.fields.PasswordItem;
import com.smartgwt.client.widgets.form.fields.TextAreaItem;
import com.smartgwt.client.widgets.form.fields.TextItem;
import com.smartgwt.client.widgets.form.fields.events.ChangeEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangeHandler;
import com.smartgwt.client.widgets.form.fields.events.ChangedEvent;
import com.smartgwt.client.widgets.form.fields.events.ChangedHandler;
import com.smartgwt.client.widgets.form.fields.events.FocusEvent;
import com.smartgwt.client.widgets.form.fields.events.FocusHandler;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverEvent;
import com.smartgwt.client.widgets.form.fields.events.ItemHoverHandler;
import com.smartgwt.client.widgets.form.fields.events.TitleClickEvent;
import com.smartgwt.client.widgets.form.fields.events.TitleClickHandler;
import com.smartgwt.client.widgets.form.fields.events.ValueHoverEvent;
import com.smartgwt.client.widgets.form.fields.events.ValueHoverHandler;

import java.util.ArrayList;

import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
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
import com.smartgwt.client.widgets.tree.TreeGrid;
import com.smartgwt.client.widgets.tree.TreeGridField;

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
	private static IPickTreeItem pickParentCreate;
	// buttons
	private static IButton createBtn;
	private static IButton editBtn;
	private static IButton cancelBtnEdit;
	private static IButton cancelBtnCreate;
	private static Canvas trash;
	private static Timer timer;

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
	      rowNum.setWidth(60);  
	      rowNum.setCellFormatter(new CellFormatter() {  
	          public String format(Object value, ListGridRecord record, int rowNum, int colNum) {  
	              return rowNum+1 + "";  
	          }
	      });
	      
	      
	      ListGridField idfield = new ListGridField("id", "ID", 60);  
	      idfield.setAlign(Alignment.CENTER);
//	      idfield.setHidden(true);
	      ListGridField parentIDfield = new ListGridField("parentID", "Parent ID", 80);  
	      parentIDfield.setAlign(Alignment.CENTER);
//	      parentIDfield.setHidden(true);
	      ListGridField firstname = new ListGridField("firstname", "Firstname", 120);  
	      firstname.setAlign(Alignment.CENTER);
	      ListGridField lastname = new ListGridField("lastname", "Lastname", 120);   
	      lastname.setAlign(Alignment.CENTER);
	      ListGridField age = new ListGridField("age", "Age", 80);  
	      age.setType(ListGridFieldType.INTEGER);  
	      age.setAlign(Alignment.CENTER);
	      ListGridField parentName = new ListGridField("parentName", "Parent", 150);
	      parentName.setAlign(Alignment.CENTER);

	      usersGrid.setWidth100();
	      usersGrid.setHeight100();
	      
	      usersGrid.setFields(rowNum, idfield, firstname, lastname, parentName, parentIDfield, age);  
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
	      
	      String[] editFormFields = {"id", "firstname", "lastname","parentInfo", "age", "phoneNum", "email"};
	      UsersDS editFormDS = new UsersDS(editFormFields, Type.FORM, 0, new ValueCallback() {		
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
			});
		  editForm.setDataSource(editFormDS);
	      
	      txtIDEdit = new TextItem("id", "Database Id:");
	      txtIDEdit.setDisabled(true);
	      txtFirstnameEdit = new TextItem("firstname", "Firstname:");	      
	      txtLastnameEdit = new TextItem("lastname", "Lastname:");
	      txtAgeEdit = new IntegerItem("age", "Age:");
	      txtAgeEdit.setKeyPressFilter("[0-9]");
	      txtPhoneNumEdit = new TextItem("phoneNum", "Phone Number:");
	      txtEmailEdit = new TextItem("email", "Email:");
	      // create combo 
	      comboParentEdit = new ComboBoxItem("parentInfo", "Parent Info:");
	      String[] comboEditFields = {"id", "name"};
	      UsersDS comboEditDS = new UsersDS(comboEditFields, Type.COMBOOTHERS, new ValueCallback() {
			
			@Override
			public void execute(String value) {
				// TODO Auto-generated method stub+
				comboParentEdit.setDisabled(false);
			}
		});
	      comboParentEdit.setOptionDataSource(comboEditDS);
	      comboParentEdit.setEmptyDisplayValue("No Value");
	      ListGridField idEditCombo = new ListGridField("id", "id");  
	      idEditCombo.setAlign(Alignment.CENTER);
	      ListGridField nameEditCombo = new ListGridField("name", "name");
	      nameEditCombo.setAlign(Alignment.CENTER);
	      // comboParentEdit.setPickListFields(idCombo, nameCombo);
	      comboParentEdit.setValueField("id");
	      comboParentEdit.setDisplayField("name");
	      comboParentEdit.setFetchDelay(1000);
	      comboParentEdit.setShowPending(true); 
//	      comboParentEdit.setAutoFetchData(true); 
//	      comboParentEdit.setPickListPlacement("fillScreen");
	      comboParentEdit.addChangedHandler(new ChangedHandler() {
			
			@Override
			public void onChanged(ChangedEvent event) {
				if (comboParentEdit.getValueAsString() == null) {
					comboEditDS.setSearchValue("");
				}
				else
					comboEditDS.setSearchValue(comboParentEdit.getValueAsString().trim().toString());
				// TODO Auto-generated method stub
				if (timer != null) {
                    timer.cancel();
                }
                
                // Start a new timer to trigger the server request
                timer = new Timer() {
                    @Override
                    public void run() {
                        // Call your server request method here
                    	comboParentEdit.fetchData();
                    }
                };
                timer.schedule(1000); // Schedule the timer to execute after 1 second
			}
		});
	      //end combo
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

		  String[] addFormFields = {"firstname", "lastname", "parentInfo", "age", "phoneNum", "email", "password"};
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
	      // Combo Create
	      
	      /// end combo
	      
	      // create pick item
	      pickParentCreate = new IPickTreeItem("parentInfo", "Parent Info:");
	      String[] pickFields = {"id", "name", "parentID", "ch_count"};
	      UsersDS pickDS = new UsersDS(pickFields, Type.PICK);
	      pickParentCreate.setDataSource(pickDS);
	      pickParentCreate.setValueField("name");
	      pickParentCreate.setEmptyMenuMessage("No parents");  
	      pickParentCreate.setCanSelectParentItems(true); 
	      pickParentCreate.setEmptyDisplayValue("Select your parent!"); 
	      pickParentCreate.setLoadDataOnDemand(true);
	      ////end pick
	      
	      
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
	      
	      addForm.setFields(txtFirstnameCreate, txtLastnameCreate, pickParentCreate, txtAgeCreate, 
	    		  txtPhoneNumCreate, txtEmailCreate, txtPasswordCreate);
	        
	      addLayout.addMember(addForm);
	      addLayout.addMember(btns);
	      addTab.setPane(addLayout);
	      
	      // tree tab
	      Tab treeTab = new Tab("Tree");
	      
	      final TreeGrid treeGrid = new TreeGrid();  
//	      treeGrid.setLoadDataOnDemand(false);  
	      treeGrid.setWidth100();  
	      treeGrid.setHeight100();    
//	      treeGrid.setNodeIcon("icons/16/person.png");  
//	        treeGrid.setFolderIcon("icons/16/person.png"); 
	      treeGrid.setShowOpenIcons(false);  
	      treeGrid.setShowDropIcons(false);  
	      treeGrid.setShowNodeIcons(false);
	      treeGrid.setClosedIconSuffix("");  
	      treeGrid.setShowFolderIcons(true);
	      
	      //columns
	      TreeGridField icons = new TreeGridField("icons", "", 100);
	    	icons.setCellFormatter(new CellFormatter() {  
		          public String format(Object value, ListGridRecord record, int rowNum, int colNum) {  
		        	  if (record.getAttributeAsInt("ch_count") == 0) {
		        		  treeGrid.openGroup(record);
		        	  }
		        	  return "";
		          }
		      });    	
	    	
	      icons.setAlign(Alignment.CENTER);
	      // handle show open
	      
	      //
	      TreeGridField idfieldTree = new TreeGridField("id", "ID" , 40);  
	      idfieldTree.setAlign(Alignment.CENTER);
//	      idfieldTree.setHidden(true);
	      TreeGridField ch_count = new TreeGridField("ch_count", "ch_count" , 80);  
	      ch_count.setAlign(Alignment.CENTER);
	      TreeGridField parentIDfieldTree = new TreeGridField("parentID", "Parent ID", 80);  
	      parentIDfieldTree.setAlign(Alignment.CENTER);
	      parentIDfieldTree.setHidden(true);
	      TreeGridField firstnameTree = new TreeGridField("firstname", "Firstname", 120);  
	      firstnameTree.setAlign(Alignment.CENTER);
	      TreeGridField lastnameTree = new TreeGridField("lastname", "Lastname", 120);   
	      lastnameTree.setAlign(Alignment.CENTER);
	      TreeGridField ageTree = new TreeGridField("age", "Age", 80);  
	      ageTree.setType(ListGridFieldType.INTEGER);  
	      ageTree.setAlign(Alignment.CENTER);
	      TreeGridField parentNameTree = new TreeGridField("parentName", "Parent", 150);
	      parentNameTree.setAlign(Alignment.CENTER);
	      
	      treeGrid.setFields(icons, ch_count, idfieldTree, firstnameTree, lastnameTree, parentNameTree
	    		  , parentIDfieldTree, ageTree); 
	      
	      String[] treeFields = {"ch_count", "id", "parentID", "firstname", "lastname", "parentName", "age"};
	      UsersDS treeDS = new UsersDS(treeFields, Type.TREE, new ValueCallback() {
			
				@Override
				public void execute(String value) {
					// TODO Auto-generated method stub
					SC.say("Done", "Items removed successfully!");
					treeGrid.invalidateCache();
				}
	      });

	      treeGrid.addFolderOpenedHandler(e -> {
	    	  treeDS.setOpenedID(Integer.parseInt(e.getNode().getAttribute("id")));
	      });
	      treeGrid.setDataSource(treeDS);
//	      treeGrid.setDataSource(gridDS);
	      treeGrid.setAutoFetchData(true); 
	      treeGrid.setDataFetchMode(FetchMode.PAGED);
	      treeGrid.setCanRemoveRecords(true);
	      treeGrid.setSelectionType(SelectionStyle.SIMPLE);  
	      treeGrid.setSelectionAppearance(SelectionAppearance.CHECKBOX);
	      
	      treeGrid.addSelectionUpdatedHandler(new SelectionUpdatedHandler() {
				
				@Override
				public void onSelectionUpdated(SelectionUpdatedEvent event) {
					// TODO Auto-generated method stub
					if (treeGrid.getSelectedRecords().length > 0) {
						trash.setVisible(true);
					}
					else {
						trash.setVisible(false);
						treeDS.clearSelectedIds();
					}
				}
			});
	      
	      treeGrid.setShowSelectedIcons(true);  
	      
	   // define double click
	      treeGrid.addCellDoubleClickHandler(new CellDoubleClickHandler() {
			
			@Override
			public void onCellDoubleClick(CellDoubleClickEvent event) {
				// TODO Auto-generated method stub
				topTabSet.enableTab(2);
				ListGridRecord user = event.getRecord();
				// create datasource
				SC.say(Integer.parseInt(user.getAttribute("id")) + "");
				editFormDS.setDSID(Integer.parseInt(user.getAttribute("id")));
				comboEditDS.setDSID(Integer.parseInt(user.getAttribute("id")));
				comboParentEdit.setDisabled(true);
				comboParentEdit.fetchData();
				editForm.setSaveOperationType(DSOperationType.FETCH);
				editForm.fetchData();
				//
				topTabSet.selectTab(2);
			}
		});
	      
	      treeTab.setPane(treeGrid);
	      //add tabs
	      
	      topTabSet.addTab(gridTab); 
	      topTabSet.addTab(addTab); 
	      topTabSet.addTab(editTab); 
	      topTabSet.addTab(treeTab); 
	            
		  topTabSet.disableTab(2);
		  
	      
	      // define double click
	      usersGrid.addCellDoubleClickHandler(new CellDoubleClickHandler() {
			
			@Override
			public void onCellDoubleClick(CellDoubleClickEvent event) {
				// TODO Auto-generated method stub
				topTabSet.enableTab(2);
				ListGridRecord user = event.getRecord();
				// create datasource
				SC.say(Integer.parseInt(user.getAttribute("id")) + "");
				editFormDS.setDSID(Integer.parseInt(user.getAttribute("id")));
				comboEditDS.setDSID(Integer.parseInt(user.getAttribute("id")));
				comboParentEdit.setDisabled(true);
				comboParentEdit.fetchData();
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
		txtIDEdit.clearValue();
		txtFirstnameEdit.clearValue();
		txtLastnameEdit.clearValue();
		txtAgeEdit.clearValue();
		txtPhoneNumEdit.clearValue();
		txtEmailEdit.clearValue();
		comboParentEdit.clearValue();
	}
	
	private static void clearCreateForm() {
		txtFirstnameCreate.clearValue();
		txtLastnameCreate.clearValue();
		txtAgeCreate.clearValue();
		txtPhoneNumCreate.clearValue();
		txtEmailCreate.clearValue();
		pickParentCreate.clearValue();
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
