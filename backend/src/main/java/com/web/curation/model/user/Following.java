package com.web.curation.model.user;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Following {
		@Id
		@Column(name = "user_id")
		@GeneratedValue(strategy = GenerationType.IDENTITY)
		private int userId;

		@JsonIgnore
		@Column(name = "user_following")
		private int userFollowing;

		public int getUserId() {
			return userId;
		}

		public void setUserId(int userId) {
			this.userId = userId;
		}

		public int getUserFollowing() {
			return userFollowing;
		}

		public void setUserFollowing(int userFollowing) {
			this.userFollowing = userFollowing;
		}
}
