package tn.request.app;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tn.request.questions.NonVerifiedAskerException;
import tn.request.questions.QuestionData;
import tn.request.questions.QuestionService;
import tn.request.questions.UnknownAskerException;

@RestController
@RequestMapping("/api/v1/questions")
@AllArgsConstructor
@Slf4j
public class QuestionController {

    private QuestionService questionService;

    @PostMapping("/ask")
    public ResponseEntity<Object> askQuestion(@RequestBody QuestionData questionData) {
        try {
            questionService.askQuestion(questionData);
            return ResponseEntity.ok(questionData);
        }
        catch (UnknownAskerException unknownAskerException) {
            log.error(unknownAskerException.getMessage());
            return new ResponseEntity<>("Asker not found", HttpStatus.BAD_REQUEST);
        }
        catch (NonVerifiedAskerException nonVerifiedAskerException) {
            log.error(nonVerifiedAskerException.getMessage());
            return new ResponseEntity<>("Asker is not verified", HttpStatus.UNAUTHORIZED);
        }
        catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }
}
