package com.Librarian2.Librarian2.service;

import java.util.List;


import com.Librarian2.Librarian2.models.Events;

public interface EventService {
	public List<Events> getEvents();
	void addEvent(Events event) throws Exception;
}
