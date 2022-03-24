package com.web.curation.model.review;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@AllArgsConstructor
public class Restaurant {
	@Id
	@Column(name = "rest_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	@Column(name = "rest_name")
	private String name;
	@Column(name = "rest_category")
	private String category;
	@Column(name = "rest_like")
	private String like;
	@Column(name = "rest_review")
	private String review;
	@Column(name = "rest_scrap")
	private String scrap;
	@Column(name = "rest_telphone")
	private String telphone;
	@Column(name = "rest_location")
	private String location;
	@Column(name = "rest_url")
	private String url;
	@Column(name = "rest_img")
	private String img;
	
	public String getImg() {
		return img;
	}
	public void setImg(String img) {
		this.img = img;
	}
	public String getScrap() {
		return scrap;
	}
	public void setScrap(String scrap) {
		this.scrap = scrap;
	}
	public String getTelphone() {
		return telphone;
	}
	public void setTelphone(String telphone) {
		this.telphone = telphone;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getLike() {
		return like;
	}
	public void setLike(String like) {
		this.like = like;
	}
	public String getReview() {
		return review;
	}
	public void setReview(String review) {
		this.review = review;
	}
	@Override
	public String toString() {
		return "Restaurant [id=" + id + ", name=" + name + ", category=" + category + ", like=" + like + ", review="
				+ review + ", scrap=" + scrap + ", telphone=" + telphone + ", location=" + location + ", url=" + url
				+ ", img=" + img + "]";
	}

	
	
}
