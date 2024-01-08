package com.tulasiram.questionservice.service;

import com.tulasiram.questionservice.dao.QuestionDao;
import com.tulasiram.questionservice.model.Question;
import com.tulasiram.questionservice.model.QuestionWrapper;
import com.tulasiram.questionservice.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;

    public ResponseEntity<List<Question>> getAllQuestions() {
        try {
            return new ResponseEntity<>(questionDao.findAll(), HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
    }

    public ResponseEntity<String> addQuestion(Question question) {
        questionDao.save(question);
        return new ResponseEntity<>("success",HttpStatus.CREATED);
    }

    public ResponseEntity<List<Question>> getQuestionsByCategory(String category) {
        try {
            return new ResponseEntity<>(questionDao.findByCategory(category),HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);

    }

    public ResponseEntity<List<Integer>> getQuestionForQuiz(String categoryName, Integer numOfQuestions) {
        List<Integer> questions = questionDao.findRandomQuestionsByCategory(categoryName, numOfQuestions);
        return new ResponseEntity<>(questions,HttpStatus.OK);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuestionFromId(List<Integer> questionIds) {
        List<QuestionWrapper> questionWrappers=new ArrayList<>();
        List<Question> questions=new ArrayList<>();
        for(Integer id:questionIds){
            questions.add(questionDao.findById(id).get());
        }
        for(Question q:questions){
            QuestionWrapper wrapper=new QuestionWrapper();
            wrapper.setId(q.getId());
            wrapper.setQuestionTitle(q.getQuestionTitle());
            wrapper.setOption1(q.getOption1());
            wrapper.setOption2(q.getOption2());
            wrapper.setOption3(q.getOption3());
            wrapper.setOption4(q.getOption4());
            questionWrappers.add(wrapper);
        }
        return new ResponseEntity<>(questionWrappers,HttpStatus.OK);
    }

    public ResponseEntity<Integer> getScore(List<Response> responses) {
        int right=0;
        for(Response response:responses){
            Question question=questionDao.findById(response.getId()).get();
             if(response.getResponse().equals(question.getRightAnswer())){
                 right++;
             }
        }
        return new ResponseEntity<>(right,HttpStatus.OK);
    }

}
