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
@Data
@AllArgsConstructor
public class Meetup {
	@Id
	@Column(name = "meetup_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	@Column(name = "meetup_master")
	private String master;
	@Column(name = "meetup_title")
	private String title;
	@Column(name = "meetup_content")
	private String content;
	@Column(name = "meetup_location")
	private String location;
	@Column(name = "meetup_address")
	private String address;
	@Column(name = "meetup_date")
	private String date;
	@Column(name = "meetup_cur_personnel")
	private String curPersonnel;
	@Column(name = "meetup_max_personnel")
	private String maxPersonnel;
	@Column(name = "meetup_create_date")
	private String createDate;
	@Column(name = "meetup_img")
	private String img;
	@Column(name = "meetup_personality")
	private String personalities;
	@Column(name = "meetup_category")
	private String category;
	@Column(name = "meetup_rest_id")
	private String restId;

	public Meetup() {
	}

	public Meetup(String master, String title, String content, String location, String address, String date,
			String curPersonnel, String maxPersonnel, String img, String personalities, String category,
			String restId) {
		super();
		this.master = master;
		this.title = title;
		this.content = content;
		this.location = location;
		this.address = address;
		this.date = date;
		this.curPersonnel = curPersonnel;
		this.maxPersonnel = maxPersonnel;
		this.img = img;
		this.personalities = personalities;
		this.category = category;
		this.restId = restId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMaster() {
		return master;
	}

	public void setMaster(String master) {
		this.master = master;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getCurPersonnel() {
		return curPersonnel;
	}

	public void setCurPersonnel(String curPersonnel) {
		this.curPersonnel = curPersonnel;
	}

	public String getMaxPersonnel() {
		return maxPersonnel;
	}

	public void setMaxPersonnel(String maxPersonnel) {
		this.maxPersonnel = maxPersonnel;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getImg() {
		return img;
	}

	public void setImg(String img) {
		this.img = img;
	}

	public String getPersonalities() {
		return personalities;
	}

	public void setPersonalities(String personalities) {
		this.personalities = personalities;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getRestId() {
		return restId;
	}

	public void setRestId(String restId) {
		this.restId = restId;
	}

	@Override
	public String toString() {
		return "Meetup [id=" + id + ", master=" + master + ", title=" + title + ", content=" + content + ", location="
				+ location + ", address=" + address + ", date=" + date + ", curPersonnel=" + curPersonnel
				+ ", maxPersonnel=" + maxPersonnel + ", createDate=" + createDate + ", img=" + img + ", personalities="
				+ personalities + ", category=" + category + ", restId=" + restId + "]";
	}

}
