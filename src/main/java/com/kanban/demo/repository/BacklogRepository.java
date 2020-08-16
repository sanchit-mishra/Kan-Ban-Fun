package com.kanban.demo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.kanban.demo.domain.Backlog;

@Repository
public interface BacklogRepository extends CrudRepository<Backlog, Long> {

	Backlog findByProjectIdentifier(String projectIdentifier);
}
