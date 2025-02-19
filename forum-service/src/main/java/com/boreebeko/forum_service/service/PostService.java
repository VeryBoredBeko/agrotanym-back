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

    public PostDTO addComment(Long id, CommentDTO commentDTO) {

        // TODO: Check is this good practice
//        if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated())
//            throw new AccessDeniedException();

        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UUID userId = UUID.fromString(jwt.getClaimAsString("sub"));

        Post post = postRepository.findById(id).orElseThrow(ResourceNotFoundException::new);

        Comment comment = commentMapper.toEntity(commentDTO);
        comment.setPost(post);
        comment.setUserId(userId);

        commentRepository.save(comment);

        post.getComments().add(comment);

        return postMapper.toDTO(post);
    }
}
