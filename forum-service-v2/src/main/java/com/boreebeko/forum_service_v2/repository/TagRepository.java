package com.boreebeko.forum_service_v2.repository;

import com.boreebeko.forum_service_v2.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TagRepository extends JpaRepository<Tag, Long> {

    List<Tag> findTagsByIdIn(List<Long> tagIdList);
}
