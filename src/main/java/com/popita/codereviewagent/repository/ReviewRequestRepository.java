package com.popita.codereviewagent.repository;

import com.popita.codereviewagent.entity.ReviewRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReviewRequestRepository extends JpaRepository<ReviewRequest, UUID> {

}