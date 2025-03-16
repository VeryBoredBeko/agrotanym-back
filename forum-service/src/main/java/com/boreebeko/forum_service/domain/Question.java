package com.boreebeko.forum_service.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "questions", schema = "forum")
public class Question extends Base implements Serializable {

    @Column(name = "title")
    private String title;

    @Column(name = "description")
    private String description;
}
