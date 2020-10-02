package com.ipan97.springbootsecurityhmacsample.repository.elasticsearch;

import com.ipan97.springbootsecurityhmacsample.domain.User;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface UserSearchRepository extends ElasticsearchRepository<User, String> {
}
