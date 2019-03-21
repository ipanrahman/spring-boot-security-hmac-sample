package com.ipan97.springbootauditsample.service.mapper;

import java.util.List;

public interface EntityMapper<E, D> {

    E toEntity(D dto);

    D toDto(E entity);

    List<E> toEntities(List<E> dtos);

    List<D> toDtos(List<E> entities);
}
