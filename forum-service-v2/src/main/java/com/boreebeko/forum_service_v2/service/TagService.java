package com.boreebeko.forum_service_v2.service;

import com.boreebeko.forum_service_v2.domain.Tag;
import com.boreebeko.forum_service_v2.dto.TagDTO;
import com.boreebeko.forum_service_v2.mapper.TagMapper;
import com.boreebeko.forum_service_v2.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagService {

    private final TagRepository tagRepository;
    private final TagMapper tagMapper = TagMapper.INSTANCE;

    @Autowired
    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<TagDTO> getAllTags() {
        return tagMapper.toDTOList(tagRepository.findAll());
    }

    public List<TagDTO> getAllTagsById(List<Long> tagIdList) {
        return tagMapper.toDTOList(tagRepository.findTagsByIdIn(tagIdList));
    }

    public boolean isTagExists(Long tagId) {
        return tagRepository.existsById(tagId);
    }

    public Tag getReferenceById(Long tagId) {
        return tagRepository.getReferenceById(tagId);
    }

    public TagDTO createTag(TagDTO tagDTO) {
        Tag newTag = tagMapper.toEntity(tagDTO);
        Tag persistedTag = tagRepository.save(newTag);
        return tagMapper.toDTO(persistedTag);
    }

    public void deleteTag(Long tagId) {
        tagRepository.deleteById(tagId);
    }
}
