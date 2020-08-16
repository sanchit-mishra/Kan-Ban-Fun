package com.kanban.demo.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kanban.demo.domain.ProjectTask;

@Repository
public interface ProjectTaskRepository extends CrudRepository<ProjectTask, Long> {

	//To get list of projectTask
	List<ProjectTask> findByProjectIdentifierOrderByPriority(String backlog_id);
	
	ProjectTask findByProjectSequence(String projectSequence);
	
}
