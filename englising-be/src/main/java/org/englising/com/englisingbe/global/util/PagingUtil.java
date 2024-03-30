package org.englising.com.englisingbe.global.util;

import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLQuery;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PagingUtil {

    private final EntityManager entityManager;

    private Querydsl getQuerydsl(Class<?> clazz) {
        PathBuilder<?> builder = new PathBuilderFactory().create(clazz);
        return new Querydsl(entityManager, builder);
    }

    public <T> Page<T> getPageImpl(Pageable pageable, JPQLQuery<T> query, Class<?> clazz) {
        long totalCount = query.fetchCount();
        List<T> results = getQuerydsl(clazz).applyPagination(pageable, query).fetch();
        return new PageImpl<>(results, pageable, totalCount);
    }
}