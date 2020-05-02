package com.company.enroller.persistence;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.Query;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;
import org.springframework.stereotype.Component;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;

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
	
	public Meeting add(Meeting meeting) {
		Transaction transaction = this.session.beginTransaction();
		session.save(meeting);
		transaction.commit();
		return meeting;
	}
	
	public Participant addParticipant(Meeting meeting, Participant participant) {
		Transaction transaction = this.session.beginTransaction();
		meeting.addParticipant(participant);
		session.merge(meeting);
		transaction.commit();
		return participant;
	}
	
	public Collection<String> getParticipants(Meeting meeting) {
		Set<String> participants = new HashSet<>();
		for (Participant participant : meeting.getParticipants()) {
			participants.add(participant.getLogin());
		}
		return participants;
	}
}
