package tn.request.service.question;

import java.util.Objects;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import tn.request.bazooka.opt.BazookaOpt;
import tn.request.data.question.QuestionEntity;
import tn.request.data.question.QuestionRepository;
import tn.request.data.user.UserEntity;
import tn.request.data.user.UserRepository;
import tn.request.service.question.exception.RequestException;

@Service
@AllArgsConstructor
public class QuestionService {
    private UserRepository userRepository;
    private QuestionRepository questionRepository;

    public void askQuestion(QuestionData questionData) {
        Objects.requireNonNull(questionData);
        Objects.requireNonNull(questionData.getAskerId());

        Optional<UserEntity> askerOpt = userRepository.findById(questionData.getAskerId());

        UserEntity asker = BazookaOpt.checkIfEmpty(askerOpt)
                .thenThrow(new RequestException(HttpStatus.BAD_REQUEST, "The provided asker id doesn't correspond to any user"))
                .orElseGet();

        if (!asker.isAccountActivated()) {
            throw new RequestException(HttpStatus.BAD_REQUEST, "The asker account is not confirmed yet");
        }

        if (isValidQuestion(questionData)) {
            questionRepository.save(new QuestionEntity(null, asker, questionData.getTitle(), questionData.getContent()));
        } else {
            // TODO('Throw an exception if the format of the question is invalid, too short or bad format(Markdown)...')
        }
    }

    public boolean isValidQuestion(QuestionData questionData) {
        return true;
    }
}
