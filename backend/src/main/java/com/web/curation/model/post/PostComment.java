package com.web.curation.model.post;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
public class PostComment {

	@Id
	@Column(name = "comment_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String commentid;

	@Column(name = "post_id")
	private String postid;
	@Column(name = "post_comment")
	private String postcomment;
	@Column(name = "user_nickname")
	private String usernickname;
	@Column(name = "user_img")
	private String userimg;
	@Column(name = "comment_userid")
	private String commentuserid;
	@Column(name = "comment_date")
	private String commentdate;
	


	public String getCommentdate() {
		return commentdate;
	}

	public void setCommentdate(String commentdate) {
		this.commentdate = commentdate;
	}

	public String getCommentuserid() {
		return commentuserid;
	}

	public void setCommentuserid(String commentuserid) {
		this.commentuserid = commentuserid;
	}

	public String getCommentid() {
		return commentid;
	}

	public void setCommentid(String commentid) {
		this.commentid = commentid;
	}

	public String getPostid() {
		return postid;
	}

	public void setPostid(String postid) {
		this.postid = postid;
	}

	public String getPostcomment() {
		return postcomment;
	}

	public void setPostcomment(String postcomment) {
		this.postcomment = postcomment;
	}

	public String getUsernickname() {
		return usernickname;
	}

	public void setUsernickname(String usernickname) {
		this.usernickname = usernickname;
	}

	public String getUserimg() {
		return userimg;
	}

	public void setUserimg(String userimg) {
		this.userimg = userimg;
	}

	@Override
	public String toString() {
		return "PostComment [commentid=" + commentid + ", postid=" + postid + ", postcomment=" + postcomment
				+ ", usernickname=" + usernickname + ", userimg=" + userimg + "]";
	}

}
