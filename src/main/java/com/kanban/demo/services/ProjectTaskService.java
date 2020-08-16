package com.kanban.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.kanban.demo.domain.Backlog;
import com.kanban.demo.domain.Project;
import com.kanban.demo.domain.ProjectTask;
import com.kanban.demo.exceptions.ProjectNotFoundException;
import com.kanban.demo.exceptions.ProjectNotFoundExceptionResponse;
import com.kanban.demo.repository.BacklogRepository;
import com.kanban.demo.repository.ProjectRepository;
import com.kanban.demo.repository.ProjectTaskRepository;

@Service
public class ProjectTaskService {

	@Autowired
	private BacklogRepository backlogRepository;

	@Autowired
	private ProjectTaskRepository projectTaskRepository;

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private ProjectService projectService;
	public ProjectTask addProjectTask(String projectIdentifier, ProjectTask projectTask, String username) {

			//PTs to be added to a specific project, project != null , BL exists
			Backlog backlog = projectService.findProjectByIdentifier(projectIdentifier,username).getBacklog();

			//set BL to PT
			projectTask.setBacklog(backlog);
			// ProjectSequence like :-TEST1-1, TEST1-2 ... so on!
			Integer BacklogSequence = backlog.getPTSequence();
			//Update backlog Sequence by 1
			BacklogSequence++;

			//Set to new sequence
			backlog.setPTSequence(BacklogSequence);

			//Add sequence to Project Task
			projectTask.setProjectSequence(projectIdentifier +"-"+ BacklogSequence);
			projectTask.setProjectIdentifier(projectIdentifier.toUpperCase());

			//Initial priority when priority is null
			if(projectTask.getPriority() == null || projectTask.getPriority() == 0) {
				projectTask.setPriority(3);
			}

			//Initial status when status is null
			if(projectTask.getStatus() == "" || projectTask.getStatus() == null) {
				projectTask.setStatus("TO-DO");
			}

			return projectTaskRepository.save(projectTask);
	}

	public Iterable<ProjectTask> findByBacklogId(String backlog_id, String username) {

		projectService.findProjectByIdentifier(backlog_id,username);
		return projectTaskRepository.findByProjectIdentifierOrderByPriority(backlog_id);
	}

	public ProjectTask findPTByProjectSequence(String backlog_id, String project_id, String username){
		//Check Backlog
		projectService.findProjectByIdentifier(backlog_id,username);
		//To check if ProjectTask exits or not
		ProjectTask projectTask = projectTaskRepository.findByProjectSequence(project_id);
		if(projectTask == null) {
			throw new ProjectNotFoundException("Project Task with ID '"+ project_id + "' does not exist");
		}
		//To check if backlog/projectTask path is correct or not
		if(!projectTask.getProjectIdentifier().equals(backlog_id)) {
			throw new ProjectNotFoundException("Project Task with this ID "+ project_id + " does not exist for this backlog ID: "+ backlog_id);
	}
		
		return projectTask;
	}

	public ProjectTask updatePTByProjectSequence(ProjectTask updateProjectTask, String backlog_id, String project_id, String username){

		ProjectTask projectTask = findPTByProjectSequence(backlog_id,project_id,username);

		projectTask = updateProjectTask;
		return projectTaskRepository.save(projectTask);
	}

	public void deletePTByProjectSequence(String backlog_id, String project_id, String username){
		ProjectTask projectTask = findPTByProjectSequence(backlog_id,project_id,username);

		projectTaskRepository.delete(projectTask);
	}
}
