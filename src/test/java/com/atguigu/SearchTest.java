package com.atguigu;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Hobo
 * @create 2020-12-02 8:53
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class SearchTest {
    @Autowired
    RestHighLevelClient client;


    @Test
    public void testMatchAll() throws Exception{
        SearchRequest request = new SearchRequest("hobo");


        request.source(new SearchSourceBuilder().query(QueryBuilders.matchAllQuery()));


        SearchResponse searchResponse = client.search(request, RequestOptions.DEFAULT);

    }
}
