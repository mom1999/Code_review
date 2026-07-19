package com.popita.codereviewagent.dto;

import com.popita.codereviewagent.enums.ReviewStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequestDto {
    @NotBlank(message = "Repository URL is required")
    private String repositoryUrl;


}
