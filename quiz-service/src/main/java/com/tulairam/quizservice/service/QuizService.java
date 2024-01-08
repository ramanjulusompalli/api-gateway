package com.tulairam.quizservice.service;

import com.tulairam.quizservice.dao.QuizDao;
import com.tulairam.quizservice.feign.QuizInterface;
import com.tulairam.quizservice.model.QuestionWrapper;
import com.tulairam.quizservice.model.Quiz;
import com.tulairam.quizservice.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QuizService {

    @Autowired
    QuizInterface quizInterface;

    @Autowired
    QuizDao quizDao;
    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        List<Integer> questions=quizInterface.getQuestionForQuiz(category,numQ).getBody();
        Quiz quiz=new Quiz();
        quiz.setTitle(title);
        quiz.setQuestionIds(questions);
        quizDao.save(quiz);
        return new ResponseEntity<>("Succes", HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Quiz quiz=quizDao.findById(id).get();
        List<Integer> questionIds=quiz.getQuestionIds();
        List<QuestionWrapper> questionWrappers=quizInterface.getQuestionsFromId(questionIds).getBody();
        return new ResponseEntity<>(questionWrappers,HttpStatus.OK);
    }



    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        Integer score=quizInterface.getScore(responses).getBody();
        return new ResponseEntity<>(score,HttpStatus.OK);
    }
}
