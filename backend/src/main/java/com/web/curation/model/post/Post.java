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
public class Post {

	@Id
	@Column(name = "post_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String postid;

	@Column(name = "post_userid")
	private String postuserid;
	@Column(name = "post_date")
	private String postdate;
	@Column(name = "post_content")
	private String postcontent;
	@Column(name = "post_img_url")
	private String postimgurl;
	@Column(name = "post_like")
	private String postlike;
	@Column(name = "post_update_date")
	private String postupdatedate;
	@Column(name = "user_nickname")
	private String usernickname;
	@Column(name = "user_img")
	private String user_img;

	public Post() {
		super();
	}

	public Post(String postid, String postuserid, String postdate, String postcontent, String postimgurl,
			String postlike, String usernickname) {
		super();
		this.postid = postid;
		this.postuserid = postuserid;
		this.postdate = postdate;
		this.postcontent = postcontent;
		this.postimgurl = postimgurl;
		this.postlike = postlike;
		this.usernickname = usernickname;
	}

	public String getUsernickname() {
		return usernickname;
	}

	public void setUsernickname(String usernickname) {
		this.usernickname = usernickname;
	}

	public String getUser_img() {
		return user_img;
	}

	public void setUser_img(String user_img) {
		this.user_img = user_img;
	}

	public String getPostupdatedate() {
		return postupdatedate;
	}

	public void setPostupdatedate(String postupdatedate) {
		this.postupdatedate = postupdatedate;
	}

	public String getPostid() {
		return postid;
	}

	public void setPostid(String postid) {
		this.postid = postid;
	}

	public String getPostuserid() {
		return postuserid;
	}

	public void setPostuserid(String postuserid) {
		this.postuserid = postuserid;
	}

	public String getPostdate() {
		return postdate;
	}

	public void setPostdate(String postdate) {
		this.postdate = postdate;
	}

	public String getPostcontent() {
		return postcontent;
	}

	public void setPostcontent(String postcontent) {
		this.postcontent = postcontent;
	}

	public String getPostimgurl() {
		return postimgurl;
	}

	public void setPostimgurl(String postimgurl) {
		this.postimgurl = postimgurl;
	}

	public String getPostlike() {
		return postlike;
	}

	public void setPostlike(String postlike) {
		this.postlike = postlike;
	}

	@Override
	public String toString() {
		return "Post [postid=" + postid + ", postuserid=" + postuserid + ", postdate=" + postdate + ", postcontent="
				+ postcontent + ", postimgurl=" + postimgurl + ", postlike=" + postlike + "]";
	}

}
