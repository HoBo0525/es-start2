package com.atguigu;

import com.alibaba.fastjson.JSON;
import com.atguigu.domain.Person;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Hobo
 * @create 2020-12-01 20:52
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TextTest {

    @Autowired
    RestHighLevelClient client;

    //添加字段 使用map对象
    @Test
    public void addDoc() throws Exception{
        Map data = new HashMap();
        data.put("address", "黑龙江绥化");
        data.put("name", "hobo");
        data.put("age", 22);

        //获取操控字段对象
        IndexRequest request = new IndexRequest("hobo").id("2").source(data);

        IndexResponse response = client.index(request, RequestOptions.DEFAULT);

        System.out.println(response.getId());

    }
    @Test
    public void addDoc2() throws Exception{
        Person person = new Person();
        person.setId("3");
        person.setAge(26);
        person.setAddress("绥化庆安");
        person.setName("less");

        //将对象转化为Json
        String data = JSON.toJSONString(person);

        //获取操控字段对象
        IndexRequest request = new IndexRequest("hobo").id(person.getId()).source(data, XContentType.JSON);

        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        System.out.println(response.getId());
    }



    //根据id修改字段  如果没有该id 将做添加操作
    @Test
    public void updateDoc() throws Exception{
        Person p = new Person();
        p.setId("3");
        p.setAge(26);
        p.setAddress("庆安");
        p.setName("bob");

        String data = JSON.toJSONString(p);

        IndexRequest request = new IndexRequest("hobo").id(p.getId()).source(data, XContentType.JSON);

        IndexResponse response = client.index(request, RequestOptions.DEFAULT);
        System.out.println(response.getId());
    }


    //根据id查询字段
    @Test
    public void findDocById() throws Exception{
        GetRequest request = new GetRequest("hobo");
        request.id("2");
        GetResponse response = client.get(request, RequestOptions.DEFAULT);
        System.out.println(response.getSourceAsString());
    }


    //根据id删除字段
    @Test
    public void deleteDocById() throws Exception{
        DeleteRequest request = new DeleteRequest("hobo");
        request.id("3");
        DeleteResponse response = client.delete(request, RequestOptions.DEFAULT);
        System.out.println(response.getId());
        System.out.println(response.getResult());
    }


    //批量操作
    @Test
    public void testBulk() throws Exception{
        //创建BulkRequest对象,整合所有操作
        BulkRequest bulkRequest = new BulkRequest();

        DeleteRequest deleteRequest = new DeleteRequest("hobo", "2");
        bulkRequest.add(deleteRequest);

        Person p = new Person();
        p.setId("6");
        p.setName("六号");
        String jsonString = JSON.toJSONString(p);
        IndexRequest indexRequest = new IndexRequest("hobo").id(p.getId()).source(jsonString, XContentType.JSON);
        bulkRequest.add(indexRequest);

        Person p2 = new Person();
        p2.setId("1");
        p2.setName("hooboo");
        String data = JSON.toJSONString(p);
        IndexRequest indexRequest1 = new IndexRequest("hobo").id(p2.getId()).source(data, XContentType.JSON);
        bulkRequest.add(indexRequest1);

        BulkResponse responses = client.bulk(bulkRequest, RequestOptions.DEFAULT);

        RestStatus status = responses.status();
        System.out.println(status);
    }

}
