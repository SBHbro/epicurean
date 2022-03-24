package com.web.curation.model.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.time.LocalDateTime;

@Entity
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPage {
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String userId;

	@Column(name = "user_following")
	private String userFollowing;
	@Column(name = "user_follower")
	private String userFollower;
	@Column(name = "user_content")
	private String userContent;

	public UserPage() {
		super();
	}
	
	public UserPage(String userId, String userFollowing, String userFollower, String userContent) {
		super();
		this.userId = userId;
		this.userFollowing = userFollowing;
		this.userFollower = userFollower;
		this.userContent = userContent;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserFollowing() {
		return userFollowing;
	}

	public void setUserFollowing(String userFollowing) {
		this.userFollowing = userFollowing;
	}

	public String getUserFollower() {
		return userFollower;
	}

	public void setUserFollower(String userFollower) {
		this.userFollower = userFollower;
	}

	public String getUserContent() {
		return userContent;
	}

	public void setUserContent(String userContent) {
		this.userContent = userContent;
	}

	@Override
	public String toString() {
		return "UserPage [userId=" + userId + ", userFollowing=" + userFollowing + ", userFollower=" + userFollower
				+ ", userContent=" + userContent + "]";
	}

}