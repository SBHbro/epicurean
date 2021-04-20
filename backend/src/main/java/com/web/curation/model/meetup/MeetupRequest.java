package com.web.curation.model.meetup;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Null;

import org.springframework.lang.Nullable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
public class MeetupRequest {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	@Column(name = "meetup_id")
	private String meetupId;
	@Column(name = "host_id")
	private String hostId;
	@Column(name = "guest_id")
	private String guestId;
	@Column(name = "request_message")
	private String requestMessage;
	
	public MeetupRequest() {
		super();
	}

	public MeetupRequest(String id, String meetupId, String hostId, String guestId, String requestMessage) {
		super();
		this.id = id;
		this.meetupId = meetupId;
		this.hostId = hostId;
		this.guestId = guestId;
		this.requestMessage = requestMessage;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMeetupId() {
		return meetupId;
	}

	public void setMeetupId(String meetupId) {
		this.meetupId = meetupId;
	}

	public String getHostId() {
		return hostId;
	}

	public void setHostId(String hostId) {
		this.hostId = hostId;
	}

	public String getGuestId() {
		return guestId;
	}

	public void setGuestId(String guestId) {
		this.guestId = guestId;
	}

	public String getRequestMessage() {
		return requestMessage;
	}

	public void setRequestMessage(String requestMessage) {
		this.requestMessage = requestMessage;
	}

	@Override
	public String toString() {
		return "MeetupRequest [id=" + id + ", meetupId=" + meetupId + ", hostId=" + hostId + ", guestId=" + guestId
				+ ", requestMessage=" + requestMessage + "]";
	}
	
	
}
