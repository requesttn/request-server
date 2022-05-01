package tn.request.app.question;

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
public class QuestionController implements IQuestionController {

    private QuestionService questionService;

    @Override
    @PostMapping("/ask")
    public ResponseEntity<Object> askQuestion(@RequestBody QuestionData questionData) {
        questionService.askQuestion(questionData);
        return ResponseEntity.ok(questionData);
    }
}
