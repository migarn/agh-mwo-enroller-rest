package com.company.enroller.persistence;

import java.util.Collection;

import org.hibernate.Query;
import org.hibernate.classic.Session;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;

@Component("meetingService")
public class MeetingService {

	Session session;

	public MeetingService() {
		session = (Session) DatabaseConnector.getInstance().getSession();
	}

	public Collection<Meeting> getAll() {
		String hql = "FROM Meeting";
		Query query = session.createQuery(hql);
		return query.list();
	}
	
	public Meeting findById(long id) {
		return (Meeting) session.get(Meeting.class, id);
	}

}
