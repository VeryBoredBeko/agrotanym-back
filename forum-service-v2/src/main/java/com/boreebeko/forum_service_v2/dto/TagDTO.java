package com.boreebeko.forum_service_v2.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class TagDTO {

    private Long id;
    private String name;
    private String description;
}
