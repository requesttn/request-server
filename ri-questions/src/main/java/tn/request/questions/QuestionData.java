package tn.request.questions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class QuestionData {
    private Long askerId;
    private String title;
    private String content;
}
