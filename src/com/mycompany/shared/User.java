package com.mycompany.shared;

import java.io.Serializable;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String firstname, lastname, phoneNum, email, password, parentName, parentID;
	
	public String getName() {
		return this.firstname + " " + this.lastname;
	}
	
	public short age;
	
	public User(String firstname, String lastname, String phoneNum, String email, String password, String age) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNum = phoneNum;
        this.email = email;
        this.password = password;
        this.age = Short.parseShort(age);
    }

//	public User(String firstname, String lastname, String phoneNum, String email, String password, String age, String parentName, String parentID) {
//        this.firstname = firstname;
//        this.lastname = lastname;
//        this.phoneNum = phoneNum;
//        this.email = email;
//        this.password = password;
//        this.age = Short.parseShort(age);
//        this.parentID = parentID;
//        this.parentName = parentName;
//    }
	
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public String getParentName() {
		return parentName;
	}

	public void setParentName(String parentName) {
		this.parentName = parentName;
	}

	public String getParentID() {
		return parentID;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	public User(String firstname, String lastname, String phoneNum, String email, String age) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.phoneNum = phoneNum;
        this.email = email;
        this.age = Short.parseShort(age);
    }
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		StringBuilder str = new StringBuilder();
		str.append("firstname\t").append(firstname).append("\n");
		str.append("lastname\t").append(lastname).append("\n");
		str.append("age\t").append(age).append("\n");
		str.append("email\t").append(email).append("\n");
		str.append("password\t").append(password).append("\n");
		str.append("phoneNum\t").append(phoneNum).append("\n");
		str.append("parentName\t").append(parentName).append("\n");
		str.append("parentID\t").append(parentID).append("\n");
		return str.toString();
	}

}
