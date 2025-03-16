package com.boreebeko.forum_service.service;

import com.boreebeko.forum_service.domain.Comment;
import com.boreebeko.forum_service.domain.Post;
import com.boreebeko.forum_service.domain.exception.AccessDeniedException;
import com.boreebeko.forum_service.domain.exception.ResourceNotFoundException;
import com.boreebeko.forum_service.dto.CommentDTO;
import com.boreebeko.forum_service.mapper.CommentMapper;
import com.boreebeko.forum_service.mapper.PostMapper;
import com.boreebeko.forum_service.repository.CommentRepository;
import com.boreebeko.forum_service.repository.PostRepository;
import com.boreebeko.forum_service.dto.PostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    private final PostMapper postMapper = PostMapper.INSTANCE;
    private final CommentMapper commentMapper = CommentMapper.INSTANCE;

    @Autowired
    public PostService(PostRepository postRepository, CommentRepository commentRepository) {
        this.postRepository = postRepository;
        this.commentRepository = commentRepository;
    }

    public PostDTO save(PostDTO postDTO) {
        if (postDTO == null) throw new RuntimeException();

        Post entity = postMapper.toEntity(postDTO);
        Post persistedEntity = postRepository.save(entity);

        return postMapper.toDTO(persistedEntity);
    }

    public PostDTO update(Long id, PostDTO postDTO) {
        if (postDTO == null) throw new RuntimeException();

        // TODO: Secure it

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID userId = UUID.fromString(jwt.getClaimAsString("sub"));

        Post oldEntity = postRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

        if (userId.compareTo(oldEntity.getUserId()) != 0) {
            throw new AccessDeniedException();
        }

        // TODO: Refactor this code asap

        if (oldEntity.getTitle().compareTo(postDTO.getTitle()) != 0) {
            oldEntity.setTitle(postDTO.getTitle());
        }

        if (oldEntity.getContent().compareTo(postDTO.getContent()) != 0) {
            oldEntity.setContent(postDTO.getContent());
        }

        Post updatedEntity = postRepository.save(oldEntity);
        return postMapper.toDTO(updatedEntity);
    }

    public void delete(Long id) {

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID userId = UUID.fromString(jwt.getClaimAsString("sub"));

        Post persistedEntity = postRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

        if (userId.compareTo(persistedEntity.getUserId()) != 0) {
            throw new AccessDeniedException();
        }

        postRepository.deleteById(id);
    }

    public List<PostDTO> getAllPosts() {
        return postMapper.toDTOList(postRepository.findAll());
    }

    public PostDTO getPostById(Long id) {
        Optional<Post> optional = postRepository.findById(id);

        Post entity = optional.orElseThrow(ResourceNotFoundException::new);
        return postMapper.toDTO(entity);
    }

    public List<PostDTO> getPostsByUserId() {

        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) throw new AccessDeniedException();

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return postMapper.toDTOList(postRepository.findPostsByUserId(UUID.fromString(jwt.getClaimAsString("sub"))));
    }

    public List<CommentDTO> getCommentsByPostId(Long id) {

        Jwt jwt = null;
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }

        List<CommentDTO> response = commentMapper.toDTOList(commentRepository.findCommentsByPostId(id));

        if (jwt != null) {
            UUID userId = UUID.fromString(jwt.getClaimAsString("sub"));
            response = response.stream().map((record) -> {
                if (record.getUserId().compareTo(userId) == 0) {
                    record.setCurrentUserComment(true);
                }
                return record;
            }).collect(Collectors.toList());
        }

        return response;
    }

    public PostDTO addComment(Long postId, CommentDTO commentDTO) {

        // TODO: Check is this good practice
        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
            throw new AccessDeniedException();

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID userId = UUID.fromString(jwt.getClaimAsString("sub"));

        Post post = postRepository.findById(postId).orElseThrow(ResourceNotFoundException::new);

        Comment comment = commentMapper.toEntity(commentDTO);
        comment.setPost(post);
        comment.setUserId(userId);

        commentRepository.save(comment);

        post.getComments().add(comment);

        return postMapper.toDTO(post);
    }

//    public CommentDTO updateComment(Long postId, Long commentId, CommentDTO commentDTO) {
//
//        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
//            throw new AccessDeniedException();
//
//        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        UUID userId = UUID.fromString(jwt.getClaimAsString("sub"));
//
//        Post post = postRepository.findById(postId).orElseThrow(ResourceNotFoundException::new);
//        Comment comment = commentRepository.findById(commentId).orElseThrow(ResourceNotFoundException::new);
//
//        if (!Objects.equals(post.getId(), comment.getPost().getId()))
//            throw new IllegalArgumentException("Comment not from this post");
//
//        if (comment.getUserId().compareTo(userId) != 0)
//            throw new AccessDeniedException();
//
//        comment.setContent(commentDTO.getContent());
//
//        Comment updatedComment = commentRepository.save(comment);
//        return commentMapper.toDTO(updatedComment);
//    }
//
//    public void deleteComment(Long postId, Long commentId) {
//        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
//            throw new AccessDeniedException();
//
//        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        UUID userId = UUID.fromString(jwt.getClaimAsString("sub"));
//
//        Post post = postRepository.findById(postId).orElseThrow(ResourceNotFoundException::new);
//        Comment comment = commentRepository.findById(commentId).orElseThrow(ResourceNotFoundException::new);
//
//        if (!Objects.equals(post.getId(), comment.getPost().getId()))
//            throw new IllegalArgumentException("Comment not from this post");
//
//        if (comment.getUserId().compareTo(userId) != 0)
//            throw new AccessDeniedException();
//
//        commentRepository
//
//        Comment updatedComment = commentRepository.save(comment);
//        return commentMapper.toDTO(updatedComment);
//    }
}
