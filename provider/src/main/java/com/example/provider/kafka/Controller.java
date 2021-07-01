package com.example.provider.kafka;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.common.vo.Message;
import com.example.common.vo.UserVO;
import com.example.provider.config.BloomFilterHelper;
import com.example.provider.config.CacheManager;
import com.example.provider.test.MyBloomFilter;
import com.example.provider.uitl.RedisUtil;
import com.google.common.base.Charsets;
import com.google.common.hash.Funnel;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@Log4j2
public class Controller {

    @Autowired
    KafkaSender kafkaSender;

    @Autowired
    CacheManager cacheManager;



    @GetMapping("test")
    public String test(){
      /*  kafkaSender.send();*/
        MyBloomFilter myBloomFilter=new MyBloomFilter();

        Message message=new Message();
        cacheManager.set("test",message);

        Message test = (Message)cacheManager.get("test");

        System.out.println(test==message);

        return "succes";
    }

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private RedisUtil redisUtil;

    @PostMapping(value = "/addEmailToBloom", produces = "application/json")
    public ResponseEntity<String> addUser(@RequestBody String params) {
        ResponseEntity<String> response = null;
        String returnResultStr;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        Map<String, Object> result = new HashMap<>();
        try {
            JSONObject requestJsonObj = JSON.parseObject(params);
            UserVO inputUser = getUserFromJson(requestJsonObj);
            BloomFilterHelper<String> myBloomFilterHelper = new BloomFilterHelper<>((Funnel<String>) (from,
                                                                                                      into) -> into.putString(from, Charsets.UTF_8).putString(from, Charsets.UTF_8), 1500000, 0.00001);
            redisUtil.addByBloomFilter(myBloomFilterHelper, "email_existed_bloom", inputUser.getEmail());
            result.put("code", HttpStatus.OK.value());
            result.put("message", "add into bloomFilter successfully");
            result.put("email", inputUser.getEmail());
            returnResultStr = JSON.toJSONString(result);
            log.info("returnResultStr======>" + returnResultStr);
            response = new ResponseEntity<>(returnResultStr, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("add a new product with error: " + e.getMessage(), e);
            result.put("message", "add a new product with error: " + e.getMessage());
            returnResultStr = JSON.toJSONString(result);
            response = new ResponseEntity<>(returnResultStr, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    @PostMapping(value = "/checkEmailInBloom", produces = "application/json")
    public ResponseEntity<String> findEmailInBloom(@RequestBody String params) {
        ResponseEntity<String> response = null;
        String returnResultStr;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        Map<String, Object> result = new HashMap<>();
        try {
            JSONObject requestJsonObj = JSON.parseObject(params);
            UserVO inputUser = getUserFromJson(requestJsonObj);
            BloomFilterHelper<String> myBloomFilterHelper = new BloomFilterHelper<>((Funnel<String>) (from,
                                                                                                      into) -> into.putString(from, Charsets.UTF_8).putString(from, Charsets.UTF_8), 1500000, 0.00001);
            boolean answer = redisUtil.includeByBloomFilter(myBloomFilterHelper, "email_existed_bloom",
                    inputUser.getEmail());
            log.info("answer=====" + answer);
            result.put("code", HttpStatus.OK.value());
            result.put("email", inputUser.getEmail());
            result.put("exist", answer);
            returnResultStr = JSON.toJSONString(result);
            log.info("returnResultStr======>" + returnResultStr);
            response = new ResponseEntity<>(returnResultStr, headers, HttpStatus.OK);
        } catch (Exception e) {
            log.error("add a new product with error: " + e.getMessage(), e);
            result.put("message", "add a new product with error: " + e.getMessage());
            returnResultStr = JSON.toJSONString(result);
            response = new ResponseEntity<>(returnResultStr, headers, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return response;
    }

    private UserVO getUserFromJson(JSONObject requestObj) {
        String userName = requestObj.getString("username");
        String userAddress = requestObj.getString("address");
        String userEmail = requestObj.getString("email");
        int userAge = requestObj.getInteger("age");
        UserVO u = new UserVO();
        u.setName(userName);
        u.setAge(userAge);
        u.setEmail(userEmail);
        u.setAddress(userAddress);
        return u;

    }


}
