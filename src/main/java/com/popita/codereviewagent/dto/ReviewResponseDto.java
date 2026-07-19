package com.popita.codereviewagent.dto;

import com.popita.codereviewagent.entity.ReviewRequest;
import com.popita.codereviewagent.enums.ReviewStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {
    private UUID reviewId;
    private ReviewStatus reviewStatus;
}
