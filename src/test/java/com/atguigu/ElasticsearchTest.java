package com.atguigu;

import org.apache.http.HttpHost;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.support.master.AcknowledgedResponse;
import org.elasticsearch.client.*;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.client.indices.GetIndexRequest;
import org.elasticsearch.client.indices.GetIndexResponse;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.xcontent.XContentType;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * @author Hobo
 * @create 2020-12-01 19:59
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ElasticsearchTest {

    @Autowired
    RestHighLevelClient client;

    //测试连接
    @Test
    public void contextLoads(){
        System.out.println(client);
    }

    //创建索引
    @Test
    public void addIndex() throws IOException {
        //使用Client 获取操作索引的对象
        IndicesClient indicesClient = client.indices();

        //具体操作  获取返回值
        CreateIndexRequest request = new CreateIndexRequest("ccc");

        //提交请求
        CreateIndexResponse response = indicesClient.create(request, RequestOptions.DEFAULT);

        //根据返回值判断结果
        System.out.println(response.isAcknowledged());

    }


    //添加索引与映射
    @Test
    public void addIndexAndMapping() throws Exception{
        //使用Client 获取操作索引的对象
        IndicesClient indicesClient = client.indices();
        //具体操作 获取返回值
        CreateIndexRequest request = new CreateIndexRequest("hobo");

        //设置mapping
        String mapping = "{\n" +
                "  \"properties\": {\n" +
                "    \"address\": {\n" +
                "      \"type\": \"text\",\n" +
                "      \"analyzer\": \"ik_max_word\"\n" +
                "    },\n" +
                "    \"age\": {\n" +
                "      \"type\": \"long\"\n" +
                "    },\n" +
                "    \"name\": {\n" +
                "      \"type\": \"keyword\"\n" +
                "    }\n" +
                "  }\n" +
                "}";

        request.mapping(mapping, XContentType.JSON);

        CreateIndexResponse response = indicesClient.create(request, RequestOptions.DEFAULT);

        System.out.println(response.isAcknowledged());

    }


    //查询索引
    @Test
    public void queryIndex() throws Exception{
        //获取操作索引的对象 通过Client
        IndicesClient indicesClient = client.indices();

        //具体操作  获取返回值
        GetIndexRequest request = new GetIndexRequest("hobo");

        GetIndexResponse response = indicesClient.get(request, RequestOptions.DEFAULT);

        Map<String, MappingMetaData> mappings = response.getMappings();
        for (String key : mappings.keySet()) {
            System.out.println("s = " + mappings.get(key).getSourceAsMap());
        }
    }


    //删除索引
    @Test
    public void deleteIndex() throws Exception{
        IndicesClient indicesClient = client.indices();

        DeleteIndexRequest request = new DeleteIndexRequest("aaa");

        AcknowledgedResponse response = indicesClient.delete(request, RequestOptions.DEFAULT);
        System.out.println(response.isAcknowledged());
    }


    //判断索引是否存在
    @Test
    public void existIndex() throws Exception{
        IndicesClient indicesClient = client.indices();

        GetIndexRequest request = new GetIndexRequest("aaa");

        boolean exists = indicesClient.exists(request, RequestOptions.DEFAULT);
        System.out.println(exists);

    }






}
