package com.example.web_test.utils;

import com.obs.services.ObsClient;
import com.obs.services.model.PutObjectRequest;

import java.io.File;

public class OBSUtils {
    public static String uploadFile(File file, String fileName) {
        String endPoint = "https://obs.cn-north-4.myhuaweicloud.com";
        String ak = "ARVLZRGEVSZLFW5PIFQU";
        String sk = "tFCh48Hvg5rZIxVUnv2Wrz1ZJrFFyqkv1qIDIQAg";
        // 创建ObsClient实例
        ObsClient obsClient = new ObsClient(ak, sk, endPoint);

        // localfile为待上传的本地文件路径，需要指定到具体的文件名
        obsClient.putObject("bjtu-ccn-hp", "header/" + fileName, file);

//        // localfile2 为待上传的本地文件路径，需要指定到具体的文件名
//        PutObjectRequest request = new PutObjectRequest();
//        request.setBucketName("bucketname");
//        request.setObjectKey("objectkey2");
//        request.setFile(new File("localfile2"));
//        obsClient.putObject(request);
        return "https://bjtu-ccn-hp.obs.cn-north-4.myhuaweicloud.com/header/" + fileName;
    }
}
