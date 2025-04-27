package com.boreebeko.forum_service_v2.web.controller;

import com.boreebeko.forum_service_v2.dto.AnswerDTO;
import com.boreebeko.forum_service_v2.dto.QuestionDTO;
import com.boreebeko.forum_service_v2.dto.VoteType;
import com.boreebeko.forum_service_v2.dto.validation.OnCreate;
import com.boreebeko.forum_service_v2.dto.validation.OnUpdate;
import com.boreebeko.forum_service_v2.service.QuestionService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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
    public ResponseEntity<List<QuestionDTO>> getQuestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Long tagId) {
        List<QuestionDTO> response = questionService.getQuestions(page, tagId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    // TODO: Different caching logic for owner and not user
    @GetMapping(value = "/questions/{id}")
//    @Cacheable(value = "questions", key = "#id")
    public ResponseEntity<QuestionDTO> getQuestionById(@PathVariable Long id) {
        QuestionDTO response = questionService.getQuestionById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/questions")
    @CacheEvict(value = "questions", allEntries = true)
    public ResponseEntity<QuestionDTO> createQuestion(@Validated(OnCreate.class) @RequestBody QuestionDTO questionDTO) {
        QuestionDTO response = questionService.createQuestion(questionDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping(value = "/questions/{id}/votes")
    @CacheEvict(value = "questions", key = "#id")
    public ResponseEntity<Void> updateVotesOfQuestionById(@PathVariable Long id, @RequestParam VoteType voteType) {
        questionService.updateVotesOfQuestionById(id, voteType);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping(value = "/questions/{id}/votes")
    @CacheEvict(value = "questions", key = "#id")
    public ResponseEntity<Void> deleteVoteToQuestionById(@PathVariable Long id) {
        questionService.deleteVoteToQuestionById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/questions/{id}")
    @CacheEvict(value = "questions", allEntries = true)
    public ResponseEntity<QuestionDTO> updateQuestion(@PathVariable Long id,
                                              @Validated(OnUpdate.class) @RequestBody QuestionDTO questionDTO) {

        QuestionDTO response = questionService.updateQuestion(id, questionDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping(value = "/questions/{id}")
    public ResponseEntity<Void> closeQuestionById(@PathVariable Long id) {
        questionService.closeQuestionById(id);
        return new ResponseEntity<>(HttpStatus.OK);
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
    public ResponseEntity<AnswerDTO> addAnswerToQuestion(@PathVariable Long questionId, @Validated(OnCreate.class) @RequestBody AnswerDTO answerDTO) {
        AnswerDTO response = questionService.createAnswer(questionId, answerDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PatchMapping(value = "/questions/{questionId}/answers/{answerId}")
    public ResponseEntity<Void> acceptAnswerToQuestion(@PathVariable Long questionId, @PathVariable Long answerId) {
        questionService.acceptAnswerToQuestion(questionId, answerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PutMapping(value = "/questions/{questionId}/answers/{answerId}")
    public ResponseEntity<AnswerDTO> updateAnswer(@PathVariable Long questionId,
                                                 @PathVariable Long answerId,
                                                 @Validated(OnUpdate.class) @RequestBody AnswerDTO answerDTO) {

        AnswerDTO response = questionService.updateAnswer(answerId, answerDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/questions/{id}/answers/{answerId}")
    public ResponseEntity<Void> deleteAnswerById(@PathVariable Long answerId) {
        questionService.deleteAnswer(answerId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/users/{userId}/posts")
    public ResponseEntity<List<QuestionDTO>> getUserCreatedQuestions(
            @PathVariable String userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(required = false) Long tagId) {
        List<QuestionDTO> response = questionService.getQuestionsByUserId(userId, page, tagId);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
