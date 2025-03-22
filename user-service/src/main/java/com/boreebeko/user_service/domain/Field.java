package com.boreebeko.user_service.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "fields", schema = "user")
public class Field extends Base implements Serializable {

    @Column(name = "name")
    private String name;

    @Column(name = "area")
    private Double area;


}
