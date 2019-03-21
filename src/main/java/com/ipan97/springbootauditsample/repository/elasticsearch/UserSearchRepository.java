package com.ipan97.springbootauditsample.repository.elasticsearch;

import com.ipan97.springbootauditsample.domain.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserSearchRepository extends ElasticsearchRepository<User, String> {
}
