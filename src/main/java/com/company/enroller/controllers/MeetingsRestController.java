package com.company.enroller.controllers;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.company.enroller.model.Meeting;
import com.company.enroller.model.Participant;
import com.company.enroller.persistence.MeetingService;
import com.company.enroller.persistence.ParticipantService;

@RestController
@RequestMapping("/meetings")
public class MeetingsRestController {

	@Autowired
	MeetingService meetingService;

	@RequestMapping(value = "", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetings() {
		Collection<Meeting> participants = meetingService.getAll();
		return new ResponseEntity<Collection<Meeting>>(participants, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getMeeting(@PathVariable("id") long id) {
	    Meeting meeting = meetingService.findById(id);
	    if (meeting == null) {
	    	return meetingNotFound();
	    }
	    return new ResponseEntity<Meeting>(meeting, HttpStatus.OK); 
		}
	
	@RequestMapping(value = "", method = RequestMethod.POST)
	public ResponseEntity<?> addMeeting(@RequestBody Meeting meeting) {
	    meetingService.add(meeting);
	    return new ResponseEntity<Meeting>(meeting, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.POST)
	public ResponseEntity<?> addParticipant(@PathVariable("id") long id, @RequestParam String login) {
		ParticipantService participantService = new ParticipantService();
		Meeting meeting = meetingService.findById(id);
	    if (meeting == null) {
	    	return meetingNotFound();
	    }
		Participant participant = participantService.findByLogin(login);
		if (participant == null) {
			return new ResponseEntity("Unable to create. Participant with login " + login + " not found.",HttpStatus.NOT_FOUND);
		}
		meetingService.addParticipant(meeting, participant);
	    return new ResponseEntity<Participant>(participant, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "/{id}/participants", method = RequestMethod.GET)
	public ResponseEntity<?> getMeetingParticipants(@PathVariable("id") long id) {
	    Meeting meeting = meetingService.findById(id);
	    if (meeting == null) {
	    	return meetingNotFound();
	    }
	    return new ResponseEntity<Collection<String>>(meetingService.getParticipants(meeting), HttpStatus.OK); 
	}
	
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> delete(@PathVariable("id") long id) {
	    Meeting meeting = meetingService.findById(id);
	    if (meeting == null) { 
	    	return meetingNotFound();
	    }
	    meetingService.delete(meeting);
	    return new ResponseEntity<Meeting>(meeting, HttpStatus.OK); 
	}
	

	
	
	
	
	
	
	
	private ResponseEntity meetingNotFound() {
		return new ResponseEntity("Meeting not found.", HttpStatus.NOT_FOUND);
	}
}
