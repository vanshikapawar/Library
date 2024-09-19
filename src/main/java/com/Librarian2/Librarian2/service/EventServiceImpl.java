package com.Librarian2.Librarian2.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.Librarian2.Librarian2.dao.CustomerDao;
import com.Librarian2.Librarian2.dao.EventDao;
import com.Librarian2.Librarian2.models.Events;

@Service
public class EventServiceImpl implements EventService{

	@Autowired
    private EventDao eventDao;

    @Autowired
    private CustomerDao customerDao;

    @Autowired
    private JavaMailSender emailSender;
    
	@Override
	public void addEvent(Events event) throws Exception {
		eventDao.save(event);
		String subj = event.getEvent_name()+ ": New Event";
				
        customerDao.findAll().forEach(customer -> {
            sendEmail(customer.getEmail(),subj, "Dear "+customer.getCust_name()+",\n\n"
            		+"We are excited to invite you to our upcoming event:\n"

    +"Event Name: "+event.getEvent_name()+"\n"
+"Date: "+event.getEvent_date()+"\n"
+"Time: "+event.getTime()+"\n"
+"Venue: "+event.getVenue()+"\n"

+"Details: "+event.getDetails()+"\n\n"


+"We look forward to your presence and hope you will join us for a memorable experience.\n\n"

+"Best regards,\n Librarian2.0 Team");
        });
		
	}
	
	private void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
    }
	
}
