// 하단 DB 설정 부분은 Sub PJT II에서 데이터베이스를 구성한 이후에 주석을 해제하여 사용.

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
@NoArgsConstructor
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String userId;

//	@JsonIgnore
	@Column(name = "user_pwd")
	private String userPwd;
	@Column(name = "user_email")
	private String userEmail;

	@Column(name = "user_joindate", insertable = false, updatable = false)
	private String userJoindate;
//    private LocalDateTime createDate;

	@Column(name = "user_name")
	private String userName;
	@Column(name = "user_nickname")
	private String userNickname;
	@Column(name = "user_phone")
	private String userPhone;
	@Column(name = "user_comment")
	private String userComment;
	@Column(name = "user_img")
	private String userImg;

	public String getUserImg() {
		return userImg;
	}

	public void setUserImg(String userImg) {
		this.userImg = userImg;
	}

	public User() {
		super();
	}

	@Builder
	public User(String userPwd, String userEmail, String userName, String userNickname, String userPhone, String userComment) {
		super();
		this.userPwd = userPwd;
		this.userEmail = userEmail;
		this.userName = userName;
		this.userNickname = userNickname;
		this.userPhone = userPhone;
		this.userComment = userComment;
	}
	
	@Builder
	public User(String userPwd, String userEmail,String userNickname,String userComment) {
		super();
		this.userPwd = userPwd;
		this.userEmail = userEmail;
		this.userNickname = userNickname;
		this.userComment = userComment;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", userPwd=" + userPwd + ", userEmail=" + userEmail + ", userJoindate="
				+ userJoindate + ", userName=" + userName + ", userNickname=" + userNickname + ", userPhone="
				+ userPhone + ", usercomment=" + userComment + "]";
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserPwd() {
		return userPwd;
	}

	public void setUserPwd(String userPwd) {
		this.userPwd = userPwd;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public String getUserJoindate() {
		return userJoindate;
	}

	public void setUserJoindate(String userJoindate) {
		this.userJoindate = userJoindate;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserNickname() {
		return userNickname;
	}

	public void setUserNickname(String userNickname) {
		this.userNickname = userNickname;
	}

	public String getUserPhone() {
		return userPhone;
	}

	public void setUserPhone(String userPhone) {
		this.userPhone = userPhone;
	}

	public String getUserComment() {
		return userComment;
	}

	public void setUserComment(String userComment) {
		this.userComment = userComment;
	}
	
}