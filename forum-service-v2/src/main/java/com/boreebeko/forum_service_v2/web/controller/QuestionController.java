package com.boreebeko.forum_service_v2.web.controller;

import com.boreebeko.forum_service_v2.dto.AnswerDTO;
import com.boreebeko.forum_service_v2.dto.QuestionDTO;
import com.boreebeko.forum_service_v2.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class QuestionController {

    private final QuestionService questionService;

    @Autowired
    public QuestionController(QuestionService questionService) {
        this.questionService = questionService;
    }

    @GetMapping(value = "/questions")
    @Cacheable("questions")
    public ResponseEntity<List<QuestionDTO>> getQuestions(@RequestParam(defaultValue = "0") int page, @RequestParam(required = false) Long tagId) {
        List<QuestionDTO> response = questionService.getQuestions(page, tagId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/questions/{id}")
    @Cacheable(value = "questions", key = "#id")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable Long id) {
        QuestionDTO response = questionService.getQuestionById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/questions")
    @CacheEvict(value = "questions", allEntries = true)
    public ResponseEntity<QuestionDTO> createQuestion(@RequestBody QuestionDTO questionDTO) {
        QuestionDTO response = questionService.createQuestion(questionDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/questions/{id}")
    @CacheEvict(value = "questions", allEntries = true)
    public ResponseEntity<QuestionDTO> updateQuestion(@PathVariable Long id,
                                              @RequestBody QuestionDTO questionDTO) {

        QuestionDTO response = questionService.updateQuestion(id, questionDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/questions/{id}")
    @CacheEvict(value = "questions", allEntries = true)
    public ResponseEntity<Void> deleteQuestionById(@PathVariable Long id) {
        questionService.deleteQuestion(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/questions/{questionId}/answers")
    public ResponseEntity<List<AnswerDTO>> getAnswersByQuestionId(@PathVariable Long questionId, @RequestParam(defaultValue = "0") int page) {
        List<AnswerDTO> response = questionService.getAnswersByQuestionId(questionId, page);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/questions/{questionId}/answers")
    public ResponseEntity<AnswerDTO> addAnswerToQuestion(@PathVariable Long questionId, @RequestBody AnswerDTO answerDTO) {
        AnswerDTO response = questionService.createAnswer(questionId, answerDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // TODO: This function can have unexpected behave
    @PutMapping(value = "/questions/{questionId}/answers/{answerId}")
    public ResponseEntity<AnswerDTO> updateQuestion(@PathVariable Long questionId,
                                                 @PathVariable Long answerId,
                                                 @RequestBody AnswerDTO answerDTO) {

        AnswerDTO response = questionService.updateAnswer(answerId, answerDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/questions/{id}/answers/{answerId}")
    public ResponseEntity<Void> deleteAnswerById(@PathVariable Long answerId) {
        questionService.deleteAnswer(answerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
