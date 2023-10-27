package com.example.web_test;

import com.example.web_test.mapper.UserMapper;
import com.example.web_test.pojo.User;
import com.example.web_test.utils.JGitUtils;
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
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class WebTestApplicationTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    void contextLoads() {
        List<User> users = userMapper.login("apple", "123456");
        for (User user : users) {
            System.out.println(user);
        }

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
}
