package com.testingAPI.backend.http;

import com.testingAPI.backend.model.Request;
import com.testingAPI.backend.model.Response;
import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.*;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.client.CookieStore;
import org.apache.http.util.EntityUtils;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class HttpExecutor {

    CookieStore cookieStore = new BasicCookieStore();
    HttpContext httpContext = new BasicHttpContext();
    {
        httpContext.setAttribute(HttpClientContext.COOKIE_STORE,cookieStore);
    }

    public Response execute(Request request) throws IOException {
        String method = request.getMethod().getStringValue();
        String url = request.getUrl();
        SSLConnectionSocketFactory sslsf = null;
        try {
            sslsf = new SSLConnectionSocketFactory(SSLContext.getDefault(), NoopHostnameVerifier.INSTANCE);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        final Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http",new PlainConnectionSocketFactory())
                .register("https",sslsf)
                .build();
        final PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
        cm.setMaxTotal(100);

        HttpClient client = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .setConnectionManager(cm)
                .build();

        HttpRequestBase connection = switch (method) {
            case "POST" -> new HttpPost(url);
            case "GET" -> new HttpGet(url);
            case "PUT" -> new HttpPut(url);
            case "DELETE" -> new HttpDelete(url);
            case "PATCH" -> new HttpPatch(url);
            default -> null;
        };

        Map<String, String> requestHeader = request.getHeaders();
        for(String key: requestHeader.keySet()){
            assert connection != null;
            connection.addHeader(key,requestHeader.get(key));
        }

        String requestBody = request.getBody();
        if (requestBody != null && (method.equals("POST") || method.equals("PUT") || method.equals("PATCH"))){
            try {
                ((HttpEntityEnclosingRequestBase)connection).setEntity(new StringEntity(requestBody));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        HttpResponse response = client.execute(connection,httpContext);

        Map<String,String> responseHeaders = new HashMap<>();

        for(Header header: response.getAllHeaders()){
            responseHeaders.put(header.getName(), header.getValue())
;        }

        byte[] responseBody = EntityUtils.toByteArray(response.getEntity());

        return  new Response(response.getStatusLine().getStatusCode(),responseBody,responseHeaders);
    }
}
