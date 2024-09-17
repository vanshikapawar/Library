package com.Librarian2.Librarian2.models;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Events {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)	
	private Long id;
	private String event_name;
	private String details;
	private String event_date;
	private String venue;
	private String time;
	public Events() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Events(Long id, String event_name, String details, String event_date, String venue, String time) {
		super();
		this.id = id;
		this.event_name = event_name;
		this.details = details;
		this.event_date = event_date;
		this.venue = venue;
		this.time = time;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getEvent_name() {
		return event_name;
	}
	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}
	public String getDetails() {
		return details;
	}
	public void setDetails(String details) {
		this.details = details;
	}
	public String getEvent_date() {
		return event_date;
	}
	public void setEvent_date(String event_date) {
		this.event_date = event_date;
	}
	public String getVenue() {
		return venue;
	}
	public void setVenue(String venue) {
		this.venue = venue;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	@Override
	public String toString() {
		return "Events [id=" + id + ", event_name=" + event_name + ", details=" + details + ", event_date=" + event_date
				+ ", venue=" + venue + ", time=" + time + "]";
	}
	
}
