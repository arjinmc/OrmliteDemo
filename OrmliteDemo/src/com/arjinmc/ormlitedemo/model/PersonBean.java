package com.arjinmc.ormlitedemo.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * @desciption javabean for table person
 * @author Eminem Lu
 * @email arjinmc@hicsg.com
 * @create 2015/2/26
 */
@DatabaseTable (tableName="person")
public class PersonBean implements Serializable{
	
	@DatabaseField (index = true,generatedId = true)
	private int id;
	@DatabaseField (canBeNull = false)
	private String name;
	@DatabaseField (canBeNull = false, defaultValue= "0")
	private int sex;
	//for upgrade database test
//	@DatabaseField (canBeNull = false, defaultValue = "22")
//	private int age;
	@DatabaseField (canBeNull = false, defaultValue = "shenzhen")
	private String address;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getSex() {
		return sex;
	}
	public void setSex(int sex) {
		this.sex = sex;
	}
	//for upgrade database test
//	public int getAge() {
//		return age;
//	}
//	public void setAge(int age) {
//		this.age = age;
//	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}

}
