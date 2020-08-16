package com.kanban.demo.web;

import java.security.Principal;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import com.kanban.demo.domain.ProjectTask;
import com.kanban.demo.services.MapValidationErrorService;
import com.kanban.demo.services.ProjectTaskService;

@RestController
@RequestMapping("/api/backlog")
@CrossOrigin
public class ProjectTaskController {

	@Autowired
	private ProjectTaskService projectTaskService;

	@Autowired
	private MapValidationErrorService mapValidationError;

	@PostMapping("/{backlog_id}")
	public ResponseEntity<?> addPTtoBacklog(@Valid @RequestBody ProjectTask projectTask
			, BindingResult result, @PathVariable String backlog_id, Principal principal){
		ResponseEntity<?> errorMap = mapValidationError.MapValidationError(result);
		if(errorMap != null) return errorMap;

		ProjectTask projectTaskObj = projectTaskService.addProjectTask(backlog_id, projectTask, principal.getName());
		return new ResponseEntity<ProjectTask>(projectTaskObj,HttpStatus.CREATED);

	}

	@GetMapping("/{backlog_id}")
	public Iterable<ProjectTask> getBacklogById(@PathVariable String backlog_id, Principal principal){

		return projectTaskService.findByBacklogId(backlog_id,principal.getName());
	}

	@GetMapping("/{backlog_id}/{project_id}")
	public ResponseEntity<?> getProjectTaskBySequence(@PathVariable String backlog_id, @PathVariable String project_id, Principal principal) {
		
		ProjectTask projectTask = projectTaskService.findPTByProjectSequence(backlog_id, project_id, principal.getName());
		return new ResponseEntity<ProjectTask>(projectTask, HttpStatus.OK);
		
	}

	@PatchMapping("/{backlog_id}/{project_id}")
	public ResponseEntity<?> updateProjectTask(@Valid @RequestBody ProjectTask updateProjectTask, BindingResult result,
											   @PathVariable String backlog_id, @PathVariable String project_id, Principal principal){
		ResponseEntity<?> errorMap = mapValidationError.MapValidationError(result);
		if(errorMap != null) return errorMap;

		ProjectTask updatedProjectTask = projectTaskService.updatePTByProjectSequence(updateProjectTask,backlog_id,project_id,principal.getName());
		return  new ResponseEntity<ProjectTask>(updatedProjectTask,HttpStatus.OK);
	}

	@DeleteMapping("/{backlog_id}/{project_id}")
	public ResponseEntity<?> deleteProjectTask(@PathVariable String backlog_id, @PathVariable String project_id, Principal principal){
		projectTaskService.deletePTByProjectSequence(backlog_id,project_id,principal.getName());
		return  new ResponseEntity<String>("Project Task with ID '"+ project_id + "' is deleted successfully",HttpStatus.OK);
	}
}
