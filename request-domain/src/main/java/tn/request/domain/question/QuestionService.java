package tn.request.domain.question;

import java.util.Objects;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import tn.request.bazooka.Bazooka;
import tn.request.bazooka.opt.BazookaOpt;
import tn.request.data.question.QuestionEntity;
import tn.request.data.question.QuestionRepository;
import tn.request.data.user.UserEntity;
import tn.request.data.user.UserRepository;
import tn.request.domain.question.exception.NonVerifiedAskerException;
import tn.request.domain.question.exception.UnknownAskerException;

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
                .thenThrow(new UnknownAskerException())
                .orElseGet();
        Bazooka.checkIfNot(asker.isVerified())
                .thenThrow(new NonVerifiedAskerException(String.format("Asker with id %d is unverified", asker.getId())));

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
