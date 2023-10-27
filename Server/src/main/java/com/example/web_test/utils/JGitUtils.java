package com.example.web_test.utils;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.Ref;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.storage.file.FileRepositoryBuilder;
import org.eclipse.jgit.treewalk.TreeWalk;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class JGitUtils {
    public static Git openRpo(String dir){
        Git git = null;
        try {
            Repository repository = new FileRepositoryBuilder()
                    .setGitDir(Paths.get(dir, ".git").toFile())
                    .build();
            git = new Git(repository);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return git;
    }

    public static List<String> getFiles(String warePath, String branch) throws IOException, GitAPIException {
        Git git = Git.open(new File(warePath));//打开仓库
        git.checkout().setName(branch).call();
        ObjectId head = git.getRepository().resolve("HEAD^{tree}");//获取文件树

        List<String> fileList = new ArrayList<>();

        try (TreeWalk treeWalk = new TreeWalk(git.getRepository())) {
            treeWalk.addTree(head);
            treeWalk.setRecursive(true);
            while (treeWalk.next()) {
                fileList.add(treeWalk.getPathString());
            }
        }

        return fileList;
    }

    public static List<String> getBranches(String path) {
        List<String> branches = new ArrayList<>();
        try {
            // Open the repository using the path
            Git git = Git.open(new java.io.File(path));
            // Get the list of references to the branches
            List<Ref> refs = git.branchList().call();
            // Loop through the references and extract the branch names
            for (Ref ref : refs) {
                // Get the full name of the branch, such as "refs/heads/master"
                String fullName = ref.getName();
                // Get the short name of the branch, such as "master"
                String shortName = fullName.substring(fullName.lastIndexOf("/") + 1);
                // Add the short name to the list
                branches.add(shortName);
            }
            // Close the repository
            git.close();
        } catch (Exception e) {
            // Handle any exceptions that may occur
            e.printStackTrace();
        }
        // Return the list of branch names
        return branches;
    }

}
