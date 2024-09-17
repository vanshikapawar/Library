package com.Librarian2.Librarian2.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Customer {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Long cust_id;
	private String cust_name;
	private Integer age;
	@Column(unique = true)
	private String email;
	private String mobile_no;
	private String cust_address;
	public Customer() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Customer(Long cust_id, String cust_name, Integer age, String email, String mobile_no, String cust_address) {
		super();
		this.cust_id = cust_id;
		this.cust_name = cust_name;
		this.age = age;
		this.email = email;
		this.mobile_no = mobile_no;
		this.cust_address = cust_address;
	}
	public Long getCust_id() {
		return cust_id;
	}
	public void setCust_id(Long cust_id) {
		this.cust_id = cust_id;
	}
	public String getCust_name() {
		return cust_name;
	}
	public void setCust_name(String cust_name) {
		this.cust_name = cust_name;
	}
	public Integer getAge() {
		return age;
	}
	public void setAge(Integer age) {
		this.age = age;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile_no() {
		return mobile_no;
	}
	public void setMobile_no(String mobile_no) {
		this.mobile_no = mobile_no;
	}
	public String getCust_address() {
		return cust_address;
	}
	public void setCust_address(String cust_address) {
		this.cust_address = cust_address;
	}
	@Override
	public String toString() {
		return "Customer [cust_id=" + cust_id + ", cust_name=" + cust_name + ", age=" + age + ", email=" + email
				+ ", mobile_no=" + mobile_no + ", cust_address=" + cust_address + "]";
	}
	
	
	
}
