package com.boreebeko.forum_service_v2.service;

import com.boreebeko.forum_service_v2.domain.*;
import com.boreebeko.forum_service_v2.domain.exception.AccessDeniedException;
import com.boreebeko.forum_service_v2.domain.exception.ResourceNotFoundException;
import com.boreebeko.forum_service_v2.dto.AnswerDTO;
import com.boreebeko.forum_service_v2.dto.QuestionDTO;
import com.boreebeko.forum_service_v2.dto.TagDTO;
import com.boreebeko.forum_service_v2.dto.VoteType;
import com.boreebeko.forum_service_v2.dto.exception.ClosedQuestionException;
import com.boreebeko.forum_service_v2.mapper.AnswerMapper;
import com.boreebeko.forum_service_v2.mapper.QuestionMapper;
import com.boreebeko.forum_service_v2.repository.AnswerRepository;
import com.boreebeko.forum_service_v2.repository.QuestionRepository;
import com.boreebeko.forum_service_v2.repository.QuestionTagRepository;
import com.boreebeko.forum_service_v2.repository.VoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
public class QuestionService {

    private final Logger logger = Logger.getLogger("Question Service");

    private final QuestionRepository questionRepository;
    private final AnswerRepository answerRepository;
    private final QuestionTagRepository questionTagRepository;
    private final VoteRepository votesRepository;

    private final TagService tagService;

    private final QuestionMapper questionMapper = QuestionMapper.INSTANCE;
    private final AnswerMapper answerMapper = AnswerMapper.INSTANCE;

    // One Pageable contains only 10 entities
    private static final int PAGE_SIZE = 10;

    @Autowired
    public QuestionService(QuestionRepository questionRepository, AnswerRepository answerRepository, QuestionTagRepository questionTagRepository, VoteRepository votesRepository, TagService tagService) {
        this.questionRepository = questionRepository;
        this.answerRepository = answerRepository;
        this.questionTagRepository = questionTagRepository;
        this.votesRepository = votesRepository;
        this.tagService = tagService;
    }

    @Transactional(readOnly = true)
    public List<QuestionDTO> getQuestions(int pageNumber, Long tagId) {

        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);

        List<Question> questions = null;

        if (tagId != null) {
            questions = questionTagRepository.findQuestionsByTagId(tagId, pageable).stream().toList();

        } else {
            questions = questionRepository.findAll(pageable).stream().toList();
        }

        List<QuestionDTO> questionDTOS = questionMapper.toDTOList(questions).stream()
                .peek(questionDTO -> {
                    List<QuestionTag> questionTagList = questionTagRepository.findQuestionTagByQuestionId(questionDTO.getId());
                    if (questionTagList.isEmpty()) return;

                    List<TagDTO> tagDTOList = tagService.getAllTagsById(questionTagList.stream().map(questionTag -> { return questionTag.getTag().getId();}).toList());
                    questionDTO.setTagDTOList(tagDTOList);
                })
                .toList();

        return questionDTOS;
    }

    @Transactional(readOnly = true)
    public QuestionDTO getQuestionById(Long questionId) {
        Question question = questionRepository.findById(questionId).orElseThrow(ResourceNotFoundException::new);

        QuestionDTO response = questionMapper.toDTO(question);

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // TODO: this branch works if user doesn't authenticated.
        // Need to change it.
        if (authentication != null &&
                !(authentication instanceof AnonymousAuthenticationToken) &&
                authentication.isAuthenticated()) {

            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            UUID currentUserId = UUID.fromString(jwt.getClaimAsString("sub"));

            if (question.getUserId().compareTo(currentUserId) != 0) {
                response.setIsVoted(votesRepository.existsByPostIdAndUserId(questionId, currentUserId));
            } else {
                response.setIsOwner(true);
            }

            if (response.getIsVoted() != null && response.getIsVoted()) {
                com.boreebeko.forum_service_v2.domain.VoteType voteType =
                        votesRepository.findVoteTypeByUserIdAndPostIdAndPostType(currentUserId, questionId, PostType.question)
                                .orElseThrow(IllegalStateException::new);

                switch (voteType) {
                    case upvote -> response.setVoteType(VoteType.UP);
                    case downvote -> response.setVoteType(VoteType.DOWN);
                }
            }
        }

        return response;
    }

    @Transactional(readOnly = true)
    public List<QuestionDTO> getQuestionsByUserId(String userId, int pageNumber, Long tagId) {

        Pageable pageable = PageRequest.of(pageNumber, PAGE_SIZE);

        List<Question> questions = null;

        if (tagId != null) {
            questions = questionTagRepository.findQuestionByUserAndTagId(UUID.fromString(userId), tagId, pageable).stream().toList();
        } else {
            questions = questionRepository.findQuestionByUserId(UUID.fromString(userId), pageable).stream().toList();
        }

        List<QuestionDTO> questionDTOS = questionMapper.toDTOList(questions).stream()
                .peek(questionDTO -> {
                    List<QuestionTag> questionTagList = questionTagRepository.findQuestionTagByQuestionId(questionDTO.getId());
                    if (questionTagList.isEmpty()) return;

                    List<TagDTO> tagDTOList = tagService.getAllTagsById(questionTagList.stream().map(questionTag -> { return questionTag.getTag().getId();}).toList());
                    questionDTO.setTagDTOList(tagDTOList);
                })
                .toList();

        return questionDTOS;
    }

    @Transactional
    public QuestionDTO createQuestion(QuestionDTO questionDTO) {

        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) throw new AccessDeniedException();

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID currentUserId = UUID.fromString(jwt.getClaimAsString("sub"));

        Question newQuestion = questionMapper.toEntity(questionDTO);
        newQuestion.setUserId(currentUserId);

        Question persistedQuestion = questionRepository.save(newQuestion);

        if (questionDTO.getTagIdList() == null)
            throw new IllegalArgumentException();

        if (questionDTO.getTagIdList().isEmpty())
            throw new IllegalArgumentException();

        for (Long tagId : questionDTO.getTagIdList()) {
            if (tagService.isTagExists(tagId)) {
                Tag tag = tagService.getReferenceById(tagId);
                QuestionTag questionTag = new QuestionTag(persistedQuestion, tag);
                questionTagRepository.save(questionTag);
            }
        }

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

        if (!questionDTO.getBody().isEmpty())
            persistedQuestion.setBody(questionDTO.getBody());

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

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        // TODO: this branch works if user doesn't authenticated.
        // Need to change it.
        if (authentication != null &&
                !(authentication instanceof AnonymousAuthenticationToken) &&
                authentication.isAuthenticated()) {
            try {
                Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                UUID currentUserId = UUID.fromString(jwt.getClaimAsString("sub"));

                answerDTOList =  answerDTOList.stream().map(
                        answerDTO -> {
                            if (answerDTO.getUserId().compareTo(currentUserId) == 0) answerDTO.setCurrentUserAnswer(true);
                            return answerDTO;
                        }
                ).toList();
            } catch (RuntimeException e) {
                logger.log(Level.SEVERE, e.getMessage());
                throw new RuntimeException(e.getMessage());
            } catch (Exception e) {
                logger.log(Level.SEVERE, e.getMessage());
            }
        } else {
            logger.log(Level.INFO, "Not authenticated user");
        }

        return answerDTOList;
    }

    @Transactional
    public AnswerDTO createAnswer(Long questionId, AnswerDTO answerDTO) {

        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) throw new AccessDeniedException();

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID currentUserId = UUID.fromString(jwt.getClaimAsString("sub"));

        Question currentQuestion = questionRepository.findById(questionId).orElseThrow(ResourceNotFoundException::new);

        if (currentQuestion.getIsClosed() != null && currentQuestion.getIsClosed())
            throw new ClosedQuestionException("This question is already closed. You can't answer to it.");

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

        if (!answerDTO.getBody().isEmpty())
            currentAnswer.setBody(answerDTO.getBody());

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

    @Transactional
    public void acceptAnswerToQuestion(Long questionId, Long answerId) {
        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) throw new AccessDeniedException();

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID currentUserId = UUID.fromString(jwt.getClaimAsString("sub"));

        QuestionDTO questionDTO = questionRepository.getQuestionDTO(questionId).orElseThrow(ResourceNotFoundException::new);

        if (questionDTO.getUserId().compareTo(currentUserId) != 0)
            throw new AccessDeniedException();

        if (answerRepository.existsByIdAndUserId(answerId, currentUserId))
            throw new IllegalStateException("You can't accept own answer to question.");

        answerRepository.acceptAnswer(answerId);
        closeQuestionById(questionId);
    }

    @Transactional
    public void updateVotesOfQuestionById(Long questionId, VoteType voteType) {

        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) throw new AccessDeniedException();

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID currentUserId = UUID.fromString(jwt.getClaimAsString("sub"));

        if (questionRepository.existsByIdAndUserId(questionId, currentUserId)) throw new IllegalStateException("You can't vote to own question.");

        if (votesRepository.existsByPostIdAndUserId(questionId, currentUserId)) throw new IllegalStateException("You had already voted.");

        Vote newVote = new Vote();
        newVote.setUserId(currentUserId);
        newVote.setPostId(questionId);
        newVote.setPostType(PostType.question);

        switch (voteType) {
            case UP -> newVote.setVoteType(com.boreebeko.forum_service_v2.domain.VoteType.upvote);
            case DOWN -> newVote.setVoteType(com.boreebeko.forum_service_v2.domain.VoteType.downvote);
        }

        votesRepository.save(newVote);
    }

    @Transactional
    public void deleteVoteToQuestionById(Long questionId) {
        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) throw new AccessDeniedException();

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID currentUserId = UUID.fromString(jwt.getClaimAsString("sub"));

        Vote persistedVote = votesRepository.findByPostIdAndUserId(questionId, currentUserId).orElseThrow(IllegalStateException::new);

        votesRepository.deleteById(persistedVote.getId());
    }

    @Transactional
    public void closeQuestionById(Long questionId) {
        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) throw new AccessDeniedException();

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID currentUserId = UUID.fromString(jwt.getClaimAsString("sub"));

        QuestionDTO questionDTO = questionRepository.getQuestionDTO(questionId).orElseThrow(ResourceNotFoundException::new);

        if (questionDTO.getUserId().compareTo(currentUserId) != 0)
            throw new AccessDeniedException();

        questionRepository.closeQuestion(questionId);
    }

    // TODO: In this service we have a certain amount of code which repeats itself - need to refactor
    private boolean isUserHasAccess() {
        return false;
    }
}
