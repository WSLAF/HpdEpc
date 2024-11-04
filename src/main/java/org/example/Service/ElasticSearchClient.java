package org.example.Service;

import com.mysql.cj.util.StringUtils;
import org.apache.http.HttpHost;
import org.elasticsearch.client.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

//有哪些问题
//加到es索引里面
//时间，
//空间，vehicle、pd
//功能，
//怎样是比较好的，vehicle
public class ElasticSearchClient {

    public static RestHighLevelClient client;

    public static RestHighLevelClient InitClient(String hosts) {
        client = build(getHttpHosts(hosts));
        return client;
    }

    private static List<HttpHost> getHttpHosts(String host) {
        if (StringUtils.isNullOrEmpty(host)) {
            return null;
        }

        List<HttpHost> hosts = Arrays.stream(host.split(",")).map(q -> HttpHost.create(q)).collect(Collectors.toList());
        return hosts;
    }

    public static RestHighLevelClient build(List<HttpHost> hosts) {
        RestClientBuilder builder = RestClient.builder(hosts.toArray(new HttpHost[hosts.size()]))
                .setRequestConfigCallback(
                        config -> config.setConnectTimeout(20000)
                        .setSocketTimeout(40000)
                        .setConnectionRequestTimeout(60000)
                )
                .setHttpClientConfigCallback(
                        httpClientBuilder -> httpClientBuilder.setMaxConnTotal(500)
                                .setMaxConnPerRoute(100)
                ).setFailureListener(new RestClient.FailureListener() {
                    @Override
                    public void onFailure(Node node) {
                        boolean success = false;
                        Request request = new Request("GET", "/_cluster/health");

                        for (int i = 0; i < 5; i++) {
                            try {
                                RestHighLevelClient highClient = new RestHighLevelClient(RestClient.builder(node));
                                Response response = highClient.getLowLevelClient().performRequest(request);
                                if (response.getStatusLine().getStatusCode() == 200) {
                                    success = true;
                                    break;
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                });

        return new RestHighLevelClient(builder);
    }
}
