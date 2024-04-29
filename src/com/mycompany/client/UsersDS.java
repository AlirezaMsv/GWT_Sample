package com.mycompany.client;

import com.smartgwt.client.types.FieldType;
import com.smartgwt.client.util.SC;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.smartgwt.client.data.DataSource;
import com.smartgwt.client.data.DataSourceField;
import com.smartgwt.client.types.DSDataFormat;  

public class UsersDS extends DataSource {
	private static UsersDS instance = null;  
	
    
    public static UsersDS getInstance() {  
        if (instance == null) {  
          instance = new UsersDS("userDS_JSON");  
        }  
        return instance;  
    }  

    public UsersDS(String id) {  
        setID(id);  
        setDataFormat(DSDataFormat.JSON);  
        DataSourceField firstnameField = new DataSourceField("firstname", FieldType.TEXT, "Firstname");  
        DataSourceField lastnameField = new DataSourceField("lastname", FieldType.TEXT, "Lastname");  
        DataSourceField ageField = new DataSourceField("age", FieldType.TEXT, "Age");  
        DataSourceField phoneNumbField = new DataSourceField("phoneNum", FieldType.TEXT, "Phone Number");  
        DataSourceField emailField = new DataSourceField("email", FieldType.TEXT, "Email");  
        setFields(firstnameField, lastnameField, ageField, phoneNumbField, emailField);  
        setDataURL("");
    }  
	
}
