package com.boreebeko.forum_service.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "posts", schema = "forum")
@NoArgsConstructor
@Getter
@Setter
public class Post extends Base implements Serializable {

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;
}
