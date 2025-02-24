package com.boreebeko.image_service.service.mapper;

import java.util.List;

public interface Mappable<E, D> {

    E toEntity(D dto);
    D toDTO(E entity);
    List<D> toDTOList(List<E> entityList);
}
