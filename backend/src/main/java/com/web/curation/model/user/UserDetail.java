// 하단 DB 설정 부분은 Sub PJT II에서 데이터베이스를 구성한 이후에 주석을 해제하여 사용.

package com.web.curation.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDetail {
	@Id
	@Column(name = "user_id")
//	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String userId;

	@Column(name = "user_gender")
	private String userGender;
	
	@Column(name = "user_age")
	private String userAge;

	@Column(name = "user_favorite")
	private String userFavorite;

	@Column(name = "user_personality")
	private String userPersonality;

	@Column(name = "user_interest")
	private String userInterest;
	
	
	public UserDetail() {
		super();
	}

	public UserDetail(String userId, String userGender, String userAge, String userFavorite, String userPersonality,
			String userInterest) {
		super();
		this.userId = userId;
		this.userGender = userGender;
		this.userAge = userAge;
		this.userFavorite = userFavorite;
		this.userPersonality = userPersonality;
		this.userInterest = userInterest;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserGender() {
		return userGender;
	}

	public void setUserGender(String userGender) {
		this.userGender = userGender;
	}

	public String getUserAge() {
		return userAge;
	}

	public void setUserAge(String userAge) {
		this.userAge = userAge;
	}

	public String getUserFavorite() {
		return userFavorite;
	}

	public void setUserFavorite(String userFavorite) {
		this.userFavorite = userFavorite;
	}

	public String getUserPersonality() {
		return userPersonality;
	}

	public void setUserPersonality(String userPersonality) {
		this.userPersonality = userPersonality;
	}

	public String getUserInterest() {
		return userInterest;
	}

	public void setUserInterest(String userInterest) {
		this.userInterest = userInterest;
	}

	@Override
	public String toString() {
		return "UserDetail [userId=" + userId + ", userGender=" + userGender + ", userAge=" + userAge
				+ ", userFavorite=" + userFavorite + ", userPersonality=" + userPersonality + ", userInterest="
				+ userInterest + "]";
	}

}