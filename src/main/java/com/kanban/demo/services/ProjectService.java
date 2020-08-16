package com.kanban.demo.services;

import com.kanban.demo.domain.User;
import com.kanban.demo.exceptions.ProjectNotFoundException;
import com.kanban.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kanban.demo.domain.Backlog;
import com.kanban.demo.domain.Project;
import com.kanban.demo.exceptions.ProjectIdException;
import com.kanban.demo.repository.BacklogRepository;
import com.kanban.demo.repository.ProjectRepository;

@Service
public class ProjectService {

	@Autowired
	private ProjectRepository projectRepository;
	
	@Autowired
	private BacklogRepository backlogRepository;

	@Autowired
	private UserRepository userRepository;
	
	public Project saveOrUpdate(Project project, String username) {

		if(project.getId() != null){
			Project existingProject = projectRepository.findByProjectIdentifier(project.getProjectIdentifier());

			if(existingProject != null && (!existingProject.getUser().getUsername().equals(username))){
				throw new ProjectNotFoundException("Project is not in your account");
			}else if(existingProject == null){
				throw new ProjectNotFoundException("Project with Id: "+ project.getProjectIdentifier() + " cannot be updated because it's not in your account");
			}
		}

		try {
			User user = userRepository.findByUsername(username);
			project.setUser(user);
			project.setProjectLeader(user.getUsername());
			project.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			
			if(project.getId() == null) {
				Backlog backlog = new Backlog();
				project.setBacklog(backlog);
				backlog.setProject(project);
				backlog.setProjectIdentifier(project.getProjectIdentifier().toUpperCase());
			}
			else {
				project.setBacklog(backlogRepository.findByProjectIdentifier(project.getProjectIdentifier().toUpperCase()));
			}
			
			return projectRepository.save(project);
		}
		catch(Exception ex) {
			throw new ProjectIdException("Project Identifier "+ project.getProjectIdentifier().toUpperCase() +" is already taken");
		}
	
	}
	
	public Project findProjectByIdentifier(String projectId, String username) {
		Project project = projectRepository.findByProjectIdentifier(projectId.toUpperCase());
		
		if(project == null) {
			throw new ProjectIdException("Project ID "+ projectId +" does not exists");
		}

		if(!project.getProjectLeader().equals(username)){
			throw new ProjectNotFoundException("Project ID "+ projectId + " is not in your account");
		}
		return project;
	}
	
	public Iterable<Project> findProjects(String username){
		return projectRepository.findAllByProjectLeader(username);
	}
	
	public void deleteProject(String projectId, String username) {

		projectRepository.delete(findProjectByIdentifier(projectId,username));
	}
		
}
