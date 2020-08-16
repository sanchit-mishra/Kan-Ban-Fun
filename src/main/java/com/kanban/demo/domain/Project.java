package com.kanban.demo.domain;

import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
//OneToOne -> Backlog
public class Project {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "Project Name is required")
	private String projectName;
	
	@NotBlank(message = "Project Identifier is required")
	@Size(min = 4, max = 5, message = "Please use 4 to 5 characters")
	@Column(updatable = false, unique = true)
	private String projectIdentifier;
	
	@NotBlank(message = "Project Description is required")
	private String description;
	
	@JsonFormat(pattern = "yyyy-mm-dd")
	private Date startDate;
	
	@JsonFormat(pattern = "yyyy-mm-dd")
	private Date endDate;
	
	@JsonFormat(pattern = "yyyy-mm-dd")
	@Column(updatable = false)
	private Date createdAt;
	
	@JsonFormat(pattern = "yyyy-mm-dd")
	private Date updatedAt;
	
	@OneToOne(fetch = FetchType.EAGER, cascade =  CascadeType.ALL, mappedBy = "project")
	@JsonIgnore
	private Backlog backlog;

	@ManyToOne(fetch = FetchType.LAZY)
	@JsonIgnore
	private User user;

	private String projectLeader;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getProjectLeader() {
		return projectLeader;
	}

	public void setProjectLeader(String projectLeader) {
		this.projectLeader = projectLeader;
	}

	public Project() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectIdentifier() {
		return projectIdentifier;
	}

	public void setProjectIdentifier(String projectIdentifier) {
		this.projectIdentifier = projectIdentifier;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	
	public Backlog getBacklog() {
		return backlog;
	}

	public void setBacklog(Backlog backlog) {
		this.backlog = backlog;
	}

	@PrePersist
	protected void onCreate() {
		this.createdAt = new Date();
	}
	
	@PreUpdate
	protected void onUpdate() {
		this.updatedAt = new Date();
	}
}
