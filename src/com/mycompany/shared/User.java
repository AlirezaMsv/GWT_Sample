package com.mycompany.shared;

import java.io.Serializable;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public String firstname, lastname, phoneNum, email, password;
	
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
	
	public User() {
		// TODO Auto-generated constructor stub
	}

}
