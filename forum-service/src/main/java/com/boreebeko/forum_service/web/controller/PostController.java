package com.boreebeko.forum_service.web.controller;

import com.boreebeko.forum_service.dto.CommentDTO;
import com.boreebeko.forum_service.service.PostService;
import com.boreebeko.forum_service.dto.PostDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }

    // TODO: Use Redis for caching

    @GetMapping(value = "/posts")
    @Cacheable("posts")
    public ResponseEntity<List<PostDTO>> getAllPosts() {
        List<PostDTO> response = postService.getAllPosts();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping(value = "/posts/{id}")
    @Cacheable(value = "posts", key = "#id")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Long id) {
        PostDTO response = postService.getPostById(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/posts")
    @CacheEvict(value = "posts", allEntries = true)
    public ResponseEntity<PostDTO> createPost(@AuthenticationPrincipal Jwt jwt, @RequestBody PostDTO postDTO) {

        UUID userUUID = UUID.fromString(jwt.getClaimAsString("sub"));
        postDTO.setUserId(userUUID);

        PostDTO response = postService.save(postDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping(value = "/posts/{id}")
    public ResponseEntity<PostDTO> updatePost(@PathVariable Long id,
                                              @RequestBody PostDTO postDTO) {

        PostDTO response = postService.update(id, postDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping(value = "/posts/{id}")
    @CacheEvict(value = "posts", key = "#id", allEntries = true)
    public ResponseEntity<Void> deletePostById(@PathVariable Long id) {
        postService.delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(value = "/posts/{id}/comments")
    public ResponseEntity<List<CommentDTO>> getCommentsByPostId(@PathVariable Long id) {
        List<CommentDTO> response = postService.getCommentsByPostId(id);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping(value = "/posts/{id}/comments")
    public ResponseEntity<PostDTO> addCommentToPost(@PathVariable Long id, @RequestBody CommentDTO commentDTO) {
        PostDTO response = postService.addComment(id, commentDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
