package com.kanban.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kanban.demo.domain.Project;

@Repository
public interface ProjectRepository extends CrudRepository<Project, Long> {

	Project findByProjectIdentifier(String projectIdentifier);
	
	@Override
    Iterable<Project> findAll();

	Iterable<Project> findAllByProjectLeader(String username);
	
}
