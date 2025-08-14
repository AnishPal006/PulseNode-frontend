package com.anish.quizapp.services;

import com.anish.quizapp.dao.QuestionDao;
import com.anish.quizapp.dao.QuizDao;
import com.anish.quizapp.model.QuestionWrapper;
import com.anish.quizapp.model.Questions;
import com.anish.quizapp.model.Quiz;
import com.anish.quizapp.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class QuizService {

    @Autowired
    QuizDao quizDao;

    @Autowired
    QuestionDao questionDao;


    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {

    List<Questions> questions = questionDao.findRandomQuestionsByCategory(category, numQ);
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quizDao.save(quiz);

        return new ResponseEntity<>("Success", HttpStatus.CREATED);

    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Optional<Quiz> quiz = quizDao.findById(id);
        List<Questions> questionsFromDB = quiz.get().getQuestions();
        List<QuestionWrapper> questionsForUser = new ArrayList<>();
        for (Questions q : questionsFromDB) {
            QuestionWrapper qw = new QuestionWrapper(q.getId(), q.getQuestionTitle(), q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4());
            questionsForUser.add(qw);
        }
        return new ResponseEntity<>(questionsForUser, HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateResult(int id, List<Response> responses) {
        Quiz quiz = quizDao.findById(id).get();
        List<Questions> questions = quiz.getQuestions();
        int score = 0;
        int i=0;
        for (Response response : responses) {
            if (response.getResponse().equals(questions.get(i).getRightAnswer())) {
                score++;
            }
            i++;
        }
        return new ResponseEntity<>(score, HttpStatus.OK);
    }
//
//    }
//
//    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
//        ResponseEntity<Integer> score = quizInterface.getScore(responses);
//        return score;
//    }
}