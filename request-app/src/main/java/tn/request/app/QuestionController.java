package tn.request.app;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.request.service.question.QuestionData;
import tn.request.service.question.QuestionService;

@RestController
@RequestMapping("/api/v1/questions")
@AllArgsConstructor
@Slf4j
public class QuestionController {

    private QuestionService questionService;

    @Operation(summary = "Post a new question")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "Invalid asker id", content = {@Content(mediaType = "application/json")})
    @ApiResponse(responseCode = "401", description = "Asker is unauthorized to ask questions", content = {@Content(mediaType = "application/json")})
    @PostMapping("/ask")
    public ResponseEntity<Object> askQuestion(@RequestBody QuestionData questionData) {
        questionService.askQuestion(questionData);
        return ResponseEntity.ok(questionData);
    }
}
