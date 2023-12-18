package com.example.web_test;

import com.example.web_test.mapper.UserMapper;
import com.example.web_test.pojo.User;
import com.example.web_test.utils.*;
import com.example.web_test.utils.DuplicateCheck.DuplicateCheckUtils;
import com.example.web_test.utils.DuplicateCheck.TXT_IO;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.FileMode;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class WebTestApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        System.out.println(EncodeUtils.bCryptEncode("123456"));
        System.out.println(EncodeUtils.bCryptMatch("123456", "$2a$10$fNFSlNBslqycVoKfleCni.A2UxVNb1x27fMEsrroK0x6AJ5WtyWIe"));
    }

    @Test
    void gitTest() throws GitAPIException, IOException {
//        String gitPath = "D:\\jgitTest\\test1";
//        Git git = Git.init().setDirectory(new File(gitPath)).call();
//        System.out.println(git);

//        Git git = Git.cloneRepository()
//                .setURI("https://github.com/eclipse/jgit.git")
//                .setDirectory(new File("/path/to/repo"))
//                .call();

        Git git = Git.open(new File("D:\\jgitTest\\test1\\.git"));
        git.checkout().setName("master").call();
        ObjectId head = git.getRepository().resolve("HEAD^{tree}");
//        TreeWalk treeWalk = new TreeWalk(git.getRepository());
//        treeWalk.addTree(head);
//        treeWalk.setRecursive(true);
//        while(treeWalk.next()) {
//            System.out.println(treeWalk.getPathString());
//        }
        try (TreeWalk treeWalk = new TreeWalk(git.getRepository())) {
            treeWalk.addTree(head);
            treeWalk.setRecursive(true);
            while (treeWalk.next()) {
                System.out.println(treeWalk.getPathString());
            }
        }
    }


    @Test
    void gitTest_json() throws GitAPIException, IOException, JSONException {
//        String gitPath = "D:\\jgitTest\\test1";
//        Git git = Git.init().setDirectory(new File(gitPath)).call();
//        System.out.println(git);

//        Git git = Git.cloneRepository()
//                .setURI("https://github.com/eclipse/jgit.git")
//                .setDirectory(new File("/path/to/repo"))
//                .call();

        Git git = Git.open(new File("D:\\jgitTest\\test1"));
        git.checkout().setName("master").call();
        ObjectId head = git.getRepository().resolve("HEAD^{tree}");
//        TreeWalk treeWalk = new TreeWalk(git.getRepository());
//        treeWalk.addTree(head);
//        treeWalk.setRecursive(true);
//        while(treeWalk.next()) {
//            System.out.println(treeWalk.getPathString());
//        }
        try (TreeWalk treeWalk = new TreeWalk(git.getRepository())) {
            treeWalk.addTree(head);
            treeWalk.setRecursive(true);
            while (treeWalk.next()) {
                System.out.println(treeWalk.getPathString());
                System.out.println(treeWalk.getFileMode());
            }
        }
//        JSONObject json = new JSONObject();
//
//        TreeWalk treeWalk = new TreeWalk(git.getRepository());
//        treeWalk.addTree(head);
//        treeWalk.setRecursive(true);

//        while (treeWalk.next()) {
//            String path = treeWalk.getPathString(); //获取文件或文件夹的路径
//            FileMode mode = treeWalk.getFileMode(0); //获取文件或文件夹的类型
//            if (mode == FileMode.TREE) {
//                //如果是文件夹，则创建一个新的json对象，并将其作为当前json对象的一个属性
//                JSONObject subJson = new JSONObject();
//                json.put(path, subJson);
//            } else {
//                //如果是文件，则直接将路径作为当前json对象的一个属性，值为true
//                json.put(path, true);
//            }
//        }
//        System.out.println(json);
    }

    @Test
    public void createResp() throws GitAPIException {
        Git git = JGitUtils.openRpo("D:/Warehouse/test/");
        git.getRepository().getConfig().getString("remote", "origin", "url");
        System.out.println(git.getRepository().getConfig());

//        String gitPath = "D:/Warehouse/test/";
//        Git git = Git.init().setDirectory(new File(gitPath)).call();
//        String url = git.getRepository().getConfig().getString("origin");
//        System.out.println(url);
    }

    @Test
    public void getBranch() {
//        List<String> branches = JGitUtils.getBranches("D:\\jgitTest\\test1");
//        System.out.println(branches);
    }

    @Test
    public void excelTest() throws IOException {
        List<ExcelData> excelData = ExcelUtils.readExcel(Files.newInputStream(Paths.get("C:\\Users\\HP\\Desktop\\软件学院抽象工作室-OwlSystem\\test.xls")));
        for (ExcelData data : excelData) {
            System.out.println(data.getName() + " " + data.getID());
        }
    }

    @Test
    public void duplicateCheck() throws FileNotFoundException {
        String str1 = TXT_IO.readTxt("C:\\Users\\HP\\Desktop\\用例\\text1.txt");
        String str2 = TXT_IO.readTxt("C:\\Users\\HP\\Desktop\\用例\\text2.txt");
        String str3 = TXT_IO.readTxt("C:\\Users\\HP\\Desktop\\用例\\text3.txt");
        double rate1 = DuplicateCheckUtils.getRate(str1, str2);
//        SimHash simHash1 = new SimHash(str1, 64);
//        SimHash simHash2 = new SimHash(str2, 64);
//        int distance = simHash1.hammingDistance(simHash2);
//        double rate2 = 0.01 * (100 - distance * 100 / 64);
        double rate2 = DuplicateCheckUtils.getRate(str2, str3);
        double rate3 = DuplicateCheckUtils.getRate(str1, str3);
//        System.out.println(distance);
        System.out.println(rate1);
//        System.out.println(rate2);
        System.out.println(rate2);
        System.out.println(rate3);
    }

    @Test
    public void glmTest() throws IOException {
        String reply = GlmUtils.getReply("请问地球是圆的还是方的");
        System.out.println(reply);
    }
}
