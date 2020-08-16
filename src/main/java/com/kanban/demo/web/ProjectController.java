package com.kanban.demo.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.kanban.demo.domain.Project;
import com.kanban.demo.services.MapValidationErrorService;
import com.kanban.demo.services.ProjectService;

import java.security.Principal;

@RestController
@RequestMapping("/api/project")
@CrossOrigin
public class ProjectController {
	
	@Autowired
	private ProjectService projectService;
	
	@Autowired
	private MapValidationErrorService mapValidationError;
	
	@PostMapping("")
	public ResponseEntity<?> createNewProject(@Valid @RequestBody Project project, BindingResult result, Principal principal){
		
		ResponseEntity<?> errorMap = mapValidationError.MapValidationError(result);
		if(errorMap != null) return errorMap;
		
		Project project1 = projectService.saveOrUpdate(project,principal.getName());
		return new ResponseEntity<Project>(project1, HttpStatus.CREATED);
	}

	@GetMapping("/{projectId}")
	public ResponseEntity<?> findByProjectId(@PathVariable String projectId, Principal principal){
		Project project = projectService.findProjectByIdentifier(projectId, principal.getName());
		return new ResponseEntity<Project>(project, HttpStatus.OK);
	}
	
	@GetMapping("/all")
	public Iterable<Project> findAllProject(Principal principal){
		return projectService.findProjects(principal.getName());
	}
	
	@DeleteMapping("/{projectId}")
	public ResponseEntity<?> deleteProject(@PathVariable String projectId, Principal principal){
		projectService.deleteProject(projectId,principal.getName());
		
		return new ResponseEntity<String>("Project Identifier "+ projectId + " is deleted", HttpStatus.OK);
	}
	
}
