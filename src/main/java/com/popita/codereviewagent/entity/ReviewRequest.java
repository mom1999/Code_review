package com.popita.codereviewagent.entity;

import com.popita.codereviewagent.enums.ReviewStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name ="review_request")
public class ReviewRequest {
    @Id
    private UUID id;
    private String repositoryUrl;
    @Enumerated(EnumType.STRING)
    private ReviewStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;

    @OneToMany(
            mappedBy = "reviewRequest",
            cascade = CascadeType.ALL
    )
    private List<ReviewIssueEntity> issues =
            new ArrayList<>();
}
