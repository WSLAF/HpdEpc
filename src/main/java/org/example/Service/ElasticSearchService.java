package org.example.Service;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import com.domain.HpdEpcPartByVinDto;
import com.domain.HpdEpcPartDto;
import com.domain.VinPartOptionNew;
import com.domain.VinPartOptionNewestCopy2;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.xcontent.XContentType;
import org.example.mapper.VinPartOptionNewMapper;
import org.example.mybatismapper.ModelPartOptionMapper;
import org.example.mybatismapper.SectionPartsMapper;
import org.example.mybatismapper.VinPartOptionNewestCopy2Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ElasticSearchService {

    protected RestHighLevelClient client;

    @Autowired
    private SectionPartsMapper sectionPartsMapper;

    @Autowired
    private ModelPartOptionMapper modelPartOptionMapper;

    @Autowired
    private VinPartOptionNewMapper vinPartOptionNewMapper;

    @Autowired
    private VinPartOptionNewestCopy2Mapper vinPartOptionNewestCopy2Mapper;

    String host = "http://172.24.200.119:9200";

//    private ProtostuffSerializer protostuffSerializer = new ProtostuffSerializer();

    public ElasticSearchService() {
        this.client = ElasticSearchClient.InitClient(host);
    }

    //对比一下
    public void insert() throws IOException {
        //todo 批量,一次处理5000条
        int start = 0;

        ObjectMapper objectMapper = new ObjectMapper();
        List<HpdEpcPartDto> hpdEpcPartDtos = sectionPartsMapper.select(null, start);

//        System.out.println("kkkkk" + hpdEpcPartDtos.size());
        while (hpdEpcPartDtos.size() > 0) {
            BulkRequest bulkRequest = new BulkRequest();
            List<IndexRequest> indexRequests = new ArrayList<>();
            hpdEpcPartDtos.forEach(e -> {
                e.setUserNote(new JSONObject(e.getUserNote2()));
                e.setNotes(new JSONArray(e.getNotes2()));
                e.setPrice(new JSONObject(e.getPrice2()));
                e.setLookups(new JSONArray(e.getLookups2()));
                e.setClientIdentificationData(new JSONObject(e.getClientIdentificationData2()));
                e.setSections(new JSONArray(e.getSections2()));
                e.setNotesReferenceKeys(new JSONArray(e.getNotesReferenceKeys2()));
                IndexRequest indexRequest = new IndexRequest("hpd-epc-part");
                indexRequest.id(e.getId());
                try {
                    indexRequest.source(objectMapper.writeValueAsString(e), XContentType.JSON);
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
                indexRequest.opType(DocWriteRequest.OpType.CREATE);
                indexRequests.add(indexRequest);
            });
            indexRequests.forEach(bulkRequest::add);
//        System.out.println("bbbb" + bulkRequest.getDescription());
            BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            start = Integer.parseInt(hpdEpcPartDtos.get(hpdEpcPartDtos.size() - 1).getId());
            hpdEpcPartDtos = sectionPartsMapper.select(null, start);
            System.out.println(start + " " + hpdEpcPartDtos.size());
        }
        System.out.println("finished");
//        System.out.println("hhhhh" + bulkResponse.getItems()[0].getFailureMessage());
        //        System.out.println(bulkResponse.getItems());
//        System.out.println(6666);
        //        modelPartOptionMapper.batchInsert();
    }

    public void insertPartByVin() throws IOException {
        //todo 批量,一次处理5000条
        int start = 0;
//        System.out.println("mmmm");
        ObjectMapper objectMapper = new ObjectMapper();
        List<HpdEpcPartByVinDto> hpdEpcPartDtos = sectionPartsMapper.selectPartByVinNew(null, start);
//        System.out.println("hhhh" + hpdEpcPartDtos.get(0).toString());
        System.out.println("kkkkk" + hpdEpcPartDtos.size());
        while (hpdEpcPartDtos.size() > 0) {
            BulkRequest bulkRequest = new BulkRequest();
            List<IndexRequest> indexRequests = new ArrayList<>();
//            System.out.println("hhhh" + hpdEpcPartDtos.get(0).toString());
            hpdEpcPartDtos.forEach(e -> {
                e.setUserNote(new JSONObject(e.getUserNote2()));
                e.setNotes(new JSONArray(e.getNotes2()));
                e.setPrice(new JSONObject(e.getPrice2()));
                e.setLookups(new JSONArray(e.getLookups2()));
                e.setClientIdentificationData(new JSONObject(e.getClientIdentificationData2()));
                e.setSections(new JSONArray(e.getSections2()));
                e.setOptionIds(new JSONArray(e.getOptionIds2()));
                e.setNotesReferenceKeys(new JSONArray(e.getNotesReferenceKeys2()));
                if (e.getEndDate().isEmpty()) {
                    e.setEndDate(null);
                }
                IndexRequest indexRequest = new IndexRequest("hpd-epc-part-by-vin-new3");
                indexRequest.id(e.getCatalogCode() + e.getId());
                try {
                    indexRequest.source(objectMapper.writeValueAsString(e), XContentType.JSON);
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
                indexRequest.opType(DocWriteRequest.OpType.CREATE);
                indexRequests.add(indexRequest);
            });
            indexRequests.forEach(bulkRequest::add);
//            System.out.println("bbbb" + bulkRequest.getDescription());
            BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
            System.out.println("ffff:" + bulkResponse.getItems()[0].getFailureMessage());
            start = Integer.parseInt(hpdEpcPartDtos.get(hpdEpcPartDtos.size() - 1).getId());
            hpdEpcPartDtos = sectionPartsMapper.selectPartByVinNew(null, start);
            System.out.println(start + " " + hpdEpcPartDtos.size());
        }
        System.out.println("finished");
//        System.out.println("hhhhh" + bulkResponse.getItems()[0].getFailureMessage());
        //        System.out.println(bulkResponse.getItems());
//        System.out.println(6666);
        //        modelPartOptionMapper.batchInsert();
    }

    public void insertVinOption() throws IOException {
        int start = 0;
        ObjectMapper objectMapper = new ObjectMapper();
        String catalog = "HMA2B0PA06";
        List<VinPartOptionNewestCopy2> list = vinPartOptionNewestCopy2Mapper.selectFromStartNew(start, catalog);
        while (list.size() > 0) {
            BulkRequest bulkRequest = new BulkRequest();
            List<IndexRequest> indexRequests = new ArrayList<>();
//            System.out.println("hhhh" + hpdEpcPartDtos.get(0).toString());
            list.forEach(e -> {
//                e.setUserNote(new JSONObject(e.getUserNote2()));
//                e.setNotes(new JSONArray(e.getNotes2()));
//                e.setPrice(new JSONObject(e.getPrice2()));
//                e.setLookups(new JSONArray(e.getLookups2()));
//                e.setClientIdentificationData(new JSONObject(e.getClientIdentificationData2()));
//                e.setSections(new JSONArray(e.getSections2()));
//                e.setOptionIds(new JSONArray(e.getOptionIds2()));
//                e.setNotesReferenceKeys(new JSONArray(e.getNotesReferenceKeys2()));
//                if (e.getEndDate().isEmpty()) {
//                    e.setEndDate(null);
//                }
                if ("0909111".equals(e.getSectionCode()) && "6B28XBBMWDH6B   1031".equals(e.getFullSpecificationCode())) {
                    System.out.println(66678);
                }
                IndexRequest indexRequest = new IndexRequest("hpd-epc-option-by-vin-new2");
                indexRequest.id(e.getCatalog() + e.getId());
                try {
                    indexRequest.source(objectMapper.writeValueAsString(e), XContentType.JSON);
                } catch (JsonProcessingException ex) {
                    throw new RuntimeException(ex);
                }
                indexRequest.opType(DocWriteRequest.OpType.CREATE);
                indexRequests.add(indexRequest);
            });
            indexRequests.forEach(bulkRequest::add);
//            System.out.println("bbbb" + bulkRequest.getDescription());
            BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
//            System.out.println("ffff:" + bulkResponse.getItems()[0].getFailureMessage());
            start = Integer.parseInt(String.valueOf(list.get(list.size() - 1).getId()));
            list = vinPartOptionNewestCopy2Mapper.selectFromStartNew(start, catalog);
        }
        System.out.println("finished2");

    }



}
