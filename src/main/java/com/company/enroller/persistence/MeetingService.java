package com.company.enroller.persistence;

import java.awt.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
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
	
	public void delete(Meeting meeting) {
		Transaction transaction = this.session.beginTransaction();
		session.delete(meeting);
		transaction.commit();
	}
	
	public Meeting update(Meeting meeting) {
		Transaction transaction = this.session.beginTransaction();
		session.merge(meeting);
		transaction.commit();
		return meeting;
	}
	
	public Meeting deleteParticipant(Meeting meeting, Participant participant) {
		Transaction transaction = this.session.beginTransaction();
		meeting.getParticipants().remove(participant);
		session.save(meeting);
		transaction.commit();
		return meeting;
	}
	
	public Collection<Meeting> getAllSorted() {
		
		Comparator<Meeting> compareByTitle = new Comparator<Meeting>() {
			@Override
			public int compare(Meeting meeting1, Meeting meeting2) {
				return meeting1.getTitle().compareTo(meeting2.getTitle());
			}
		};
		
		ArrayList<Meeting> sortedMeetings = new ArrayList<>();
		
		for (Meeting meeting : getAll()) {
			sortedMeetings.add(meeting);
		}
		
		Collections.sort(sortedMeetings, compareByTitle);
		return sortedMeetings;
	}
	
	public Collection<Meeting> getAllContentFiltered(String titleFilter, String descriptionFilter) {
		ArrayList<Meeting> filteredMeetings = new ArrayList<>();
		
		for (Meeting meeting : getAll()) {
			if (meeting.getTitle().toLowerCase().contains(titleFilter.toLowerCase()) &&
					meeting.getDescription().toLowerCase().contains(descriptionFilter.toLowerCase())) {
				filteredMeetings.add(meeting);
			}
		}
		
		return filteredMeetings;
	}
	
	public Collection<Meeting> getAllParticipantFiltered(Participant participant) {
		ArrayList<Meeting> filteredMeetings = new ArrayList<>();
		
		for (Meeting meeting : getAll()) {
			if (meeting.getParticipants().contains(participant)) {
				filteredMeetings.add(meeting);
			}
		}
		
		return filteredMeetings;
	}
}
