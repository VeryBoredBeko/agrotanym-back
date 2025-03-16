package com.boreebeko.forum_service.service;

import com.boreebeko.forum_service.domain.Answer;
import com.boreebeko.forum_service.domain.Question;
import com.boreebeko.forum_service.domain.exception.AccessDeniedException;
import com.boreebeko.forum_service.domain.exception.ResourceNotFoundException;
import com.boreebeko.forum_service.dto.AnswerDTO;
import com.boreebeko.forum_service.dto.QuestionDTO;
import com.boreebeko.forum_service.mapper.AnswerMapper;
import com.boreebeko.forum_service.mapper.QuestionMapper;
import com.boreebeko.forum_service.repository.AnswerRepository;
import com.boreebeko.forum_service.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuestionMapper questionMapper = QuestionMapper.INSTANCE;
    private final AnswerMapper answerMapper = AnswerMapper.INSTANCE;

    // One Pageable contains only 10 entities
    private static final int PAGE_SIZE = 10;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, AnswerRepository answerRepository) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
    }

    @Transactional(readOnly = true)
    public List<QuestionDTO> getQuestions(int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);

        List<Question> questions = questionRepository.findAll(pageable).stream().toList();
        return questionMapper.toDTOList(questions);
    }

    @Transactional(readOnly = true)
    public QuestionDTO getQuestionById(Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(ResourceNotFoundException::new);
        return questionMapper.toDTO(question);
    }

    @Transactional
    public QuestionDTO createQuestion(QuestionDTO questionDTO) {

        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) throw new AccessDeniedException();

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID currentUserId = UUID.fromString(jwt.getClaimAsString("sub"));

        Question newQuestion = questionMapper.toEntity(questionDTO);
        newQuestion.setUserId(currentUserId);

        Question persistedQuestion = questionRepository.save(newQuestion);
        return questionMapper.toDTO(persistedQuestion);
    }

    @Transactional
    public QuestionDTO updateQuestion(Long questionId, QuestionDTO questionDTO) {

        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) throw new AccessDeniedException();

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID currentUserId = UUID.fromString(jwt.getClaimAsString("sub"));

        Question persistedQuestion = questionRepository.findById(questionId).orElseThrow(ResourceNotFoundException::new);

        if (persistedQuestion.getUserId().compareTo(currentUserId) != 0) throw new AccessDeniedException();

        if (!questionDTO.getTitle().isEmpty())
            persistedQuestion.setTitle(questionDTO.getTitle());

        if (!questionDTO.getDescription().isEmpty())
            persistedQuestion.setDescription(questionDTO.getDescription());

        Question updatedQuestion = questionRepository.save(persistedQuestion);
        return questionMapper.toDTO(updatedQuestion);
    }

    @Transactional
    public void deleteQuestion(Long questionId) {

        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) throw new AccessDeniedException();

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID currentUserId = UUID.fromString(jwt.getClaimAsString("sub"));

        Question persistedQuestion = questionRepository.findById(questionId).orElseThrow(ResourceNotFoundException::new);

        if (persistedQuestion.getUserId().compareTo(currentUserId) != 0) throw new AccessDeniedException();

        questionRepository.deleteById(questionId);
    }

    @Transactional(readOnly = true)
    public List<AnswerDTO> getAnswersByQuestionId(Long questionId, int pageNumber) {
        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);

        List<Answer> answers = answerRepository.findAnswersByQuestionId(questionId, pageable).stream().toList();
        List<AnswerDTO> answerDTOList = answerMapper.toDTOList(answers);

        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            answerDTOList = answerDTOList.stream().map(
                    answerDTO -> {
                        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                        UUID currentUserId = UUID.fromString(jwt.getClaimAsString("sub"));

                        if (answerDTO.getUserId().compareTo(currentUserId) == 0) answerDTO.setCurrentUserAnswer(true);
                        return answerDTO;
                    }
            ).toList();
        }

        return answerDTOList;
    }

    @Transactional
    public AnswerDTO createAnswer(Long questionId, AnswerDTO answerDTO) {

        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) throw new AccessDeniedException();

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID currentUserId = UUID.fromString(jwt.getClaimAsString("sub"));

        Question currentQuestion = questionRepository.findById(questionId).orElseThrow(ResourceNotFoundException::new);

        Answer newAnswer = answerMapper.toEntity(answerDTO);
        newAnswer.setUserId(currentUserId);
        newAnswer.setQuestion(currentQuestion);

        Answer persistedAnswer = answerRepository.save(newAnswer);
        return answerMapper.toDTO(persistedAnswer);
    }

    @Transactional
    public AnswerDTO updateAnswer(Long answerId, AnswerDTO answerDTO) {

        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) throw new AccessDeniedException();

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID currentUserId = UUID.fromString(jwt.getClaimAsString("sub"));

        Answer currentAnswer = answerRepository.findById(answerId).orElseThrow(ResourceNotFoundException::new);

        if (currentAnswer.getUserId().compareTo(currentUserId) != 0) throw new AccessDeniedException();

        if (!answerDTO.getContent().isEmpty())
            currentAnswer.setContent(answerDTO.getContent());

        Answer persistedAnswer = answerRepository.save(currentAnswer);
        return answerMapper.toDTO(persistedAnswer);
    }

    @Transactional
    public void deleteAnswer(Long answerId) {

        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) throw new AccessDeniedException();

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID currentUserId = UUID.fromString(jwt.getClaimAsString("sub"));

        Answer currentAnswer = answerRepository.findById(answerId).orElseThrow(ResourceNotFoundException::new);

        if (currentAnswer.getUserId().compareTo(currentUserId) != 0) throw new AccessDeniedException();

        answerRepository.deleteById(answerId);
    }

    // TODO: In this service we have a certain amount of code which repeats itself - need to refactor
    private boolean isUserHasAccess() {
        return false;
    }
}
