/*
 * To change this license header, choose License Headers in Project Properties. To change this
 * template file, choose Tools | Templates and open the template in the editor.
 */
package com.comm.sr.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.exception.ExceptionUtils;

import com.comm.sr.common.core.AbstractQueryService;
import com.comm.sr.common.elasticsearch.EsQueryService;
import com.comm.sr.common.entity.SearchServiceRule;
import com.comm.sr.service.search.BasedSearchService;
import com.comm.sr.service.search.EsTestSearchService;
import com.comm.sr.service.topic.KafkaTopicService;
import com.comm.sr.service.topic.TopicService;
import com.google.common.collect.Maps;

/**
 * @author jasstion
 */
public class SearchServiceFactory {

  final public static Map<String, BasedSearchService> srServices = Maps.newHashMap();

  static {

    SearchServiceRule searchServiceRule = new SearchServiceRule();
    Properties settings = new Properties();
    try {
      settings
          .load(SearchServiceFactory.class.getClassLoader().getResourceAsStream("sr.properties"));
    } catch (IOException e) {
      throw new RuntimeException(
          "error to load sr.properties, exception:" + ExceptionUtils.getMessage(e.getCause()) + "");

    }
    TopicService topicService = new KafkaTopicService(settings);
    AbstractQueryService searchService = new EsQueryService(settings, null);

    srServices.put("esTest",
      new EsTestSearchService(searchService, searchServiceRule, settings, topicService));
  }

  public static void main(String[] args) {
    BasedSearchService searchService = srServices.get("esTest");
    String queryStr = null;
    List<Map<String, Object>> results = (List<Map<String, Object>>) searchService.search(queryStr);

  }

}
