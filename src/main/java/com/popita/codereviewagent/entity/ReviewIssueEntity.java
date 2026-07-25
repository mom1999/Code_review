package com.popita.codereviewagent.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewIssueEntity {


    @Id
    private UUID id;


    private String tool;


    private String rule;


    private String fileName;


    private int lineNumber;


    private String severity;


    private String category;


    @Column(length = 2000)
    private String message;



    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id")
    private ReviewRequest reviewRequest;

}