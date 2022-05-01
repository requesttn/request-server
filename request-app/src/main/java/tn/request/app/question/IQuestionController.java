package tn.request.app.question;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import tn.request.service.question.QuestionData;

public interface IQuestionController {
    @Operation(summary = "Post a new question")
    @ApiResponse(responseCode = "200", content = {@Content(mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "Invalid asker id", content = {@Content(mediaType = "application/json")})
    @ApiResponse(responseCode = "401", description = "Asker is unauthorized to ask questions", content = {@Content(mediaType = "application/json")})
    ResponseEntity<Object> askQuestion(@RequestBody QuestionData questionData);
}
