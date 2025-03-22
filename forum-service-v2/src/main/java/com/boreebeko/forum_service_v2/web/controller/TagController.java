package com.boreebeko.forum_service_v2.web.controller;

import com.boreebeko.forum_service_v2.dto.TagDTO;
import com.boreebeko.forum_service_v2.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TagController {

    private final TagService tagService;

    @Autowired
    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @GetMapping(value = "/tags")
    @Cacheable("tags")
    public ResponseEntity<List<TagDTO>> getAllTags() {
        return new ResponseEntity<>(tagService.getAllTags(), HttpStatus.OK);
    }

    @PostMapping(value = "/tags")
    @CacheEvict(value = "tags", allEntries = true)
    public ResponseEntity<TagDTO> createTag(@RequestBody TagDTO tagDTO) {
        return new ResponseEntity<>(tagService.createTag(tagDTO), HttpStatus.OK);
    }

    @DeleteMapping(value = "/tags/{id}")
    @CacheEvict(value = "tags", allEntries = true)
    public ResponseEntity<Void> deleteTagById(@PathVariable Long id) {
        tagService.deleteTag(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
