package com.kishor.git;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;

import com.kishor.git.commit.find.CommitFinder;
import com.kishor.git.commit.read.CommitFileReader;
import com.kishor.git.commit.read.CommitReader;
import com.kishor.git.model.Commit;

/**
 * Main class Responsibility of this class is as follows
 * <ol>
 * <li>Input validation</li>
 * <li>Parse input</li>
 * <li>Delegate commit finding functionality</li>
 * </ol>
 * to start the commit finding process
 *
 * @author kishor
 */

public class App {
    public static void main(String[] args) {
        System.out.println("==================== Start ==================== ");
        // #1. Input Validation
        validateArguments(args);

        // #2. Input parsing/ cleansing
        String  inputDirectory   = args[0];
        String  srcBranchName    = args[1]; // source branch from where commit will be pulled
        String  destBranchName   = args[2]; // destination branch to which branch commit will be pushed
        boolean enableDebugFiles = false;

        if (args.length == 4 && "EnableDebug".equalsIgnoreCase(args[3])) {
            enableDebugFiles = true;
        }

        if (!inputDirectory.endsWith(File.separator)) {
            inputDirectory = inputDirectory + File.separator;
        }

        // #3 Read base branch commits
        CommitReader              srcCommitReader = getCommitFileReader(inputDirectory + File.separator + srcBranchName);
        Map<String, List<Commit>> srcBranch       = srcCommitReader.read();

        // #4 Read other branch commits
        CommitReader              destCommitReader = getCommitFileReader(inputDirectory + File.separator + destBranchName);
        Map<String, List<Commit>> destBranch       = destCommitReader.read();

        // #5 Find missing commits
        processMissingCommits(srcBranch, destBranch, inputDirectory);

        // #6 if requested generated debug files
        if (enableDebugFiles) {
            generateDebugFiles(srcBranch, destBranch, inputDirectory);
        }

        System.out.println("==================== End ==================== ");
    }

    private static void generateDebugFiles(final Map<String, List<Commit>> srcBranch, final Map<String, List<Commit>> destBranch, final String inputDirectory) {
        Map<String, List<Commit>> targetCommits = new LinkedHashMap<>(destBranch);

        StringBuilder output = new StringBuilder();
        output.append("Commit Subject" + "\t");
        output.append("Source Commit Hash" + "\t");
        output.append("Target Commit Hash" + "\n");

        for (String commitSubject : srcBranch.keySet()) {
            for (Commit srcCommit : srcBranch.get(commitSubject)) {
                StringBuilder line = new StringBuilder();
                line.append(commitSubject + "\t");
                line.append(srcCommit.getHash() + "\t");

                List<Commit> trgCommitList = targetCommits.remove(commitSubject);

                if (Objects.nonNull(trgCommitList)) {
                    for (Commit trgCommit : trgCommitList) {
                        StringBuilder tmp = new StringBuilder(line);
                        tmp.append(trgCommit.getHash() + "\n");
                        output.append(tmp);
                    }
                } else {
                    output.append(line + "\n");
                }
            }
        }

        // remaining target commits
        for (String commitSubject : targetCommits.keySet()) {
            for (Commit commit: targetCommits.get(commitSubject)) {
                StringBuilder line = new StringBuilder();
                line.append(commitSubject + "\t" + "\t");
                line.append(commit.getHash() + "\n");
                output.append(line);
            }
        }

        // dump into file
        try (FileWriter fw = new FileWriter(inputDirectory + "commit-debug-report.csv")) {
            fw.write(output.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processMissingCommits(final Map<String, List<Commit>> srcBranch, final Map<String, List<Commit>> destBranch, final String inputDirectory) {
        Properties prop = new Properties();
        prop.put(Constants.IGNORE_USERS_PROPS_KEY, "Jenkins,Jenkins User");
        prop.put(Constants.IGNORE_KEYWARDS_PROPS_KEY, Constants.IGNORE_KEYWARDS_DEFAULT);

        CommitFinder finder = new CommitFinder();
        finder.init(prop);
        Set<Commit> missingCommits = finder.find(srcBranch, destBranch);

        StringBuilder output = new StringBuilder();
        output.append("Commit Hash" + "\t");
        output.append("Commit Time" + "\t");
        output.append("Commit Author" + "\t");
        output.append("Commit Subject" + "\n");

        for (Commit commit : missingCommits) {
            StringBuilder line = new StringBuilder();
            line.append(commit.getHash() + "\t");
            line.append(commit.getTime() + "\t");
            line.append(commit.getAuthor() + "\t");
            line.append(commit.getSubject() + "\n");
            output.append(line);
        }

        try (FileWriter fw = new FileWriter(inputDirectory + "probable-missing-commits.csv")) {
            fw.write(output.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void validateArguments(String[] args) {
        if (args.length < 3) {
            throw new RuntimeException("\n\nThis program expects 3 inputs as follow. \n1. Input directory \n2. Base Branch name \n3. Other Branch name");
        }
    }

    private static CommitReader getCommitFileReader(final String inputFilePath) {
        Properties props = new Properties();
        props.setProperty(Constants.INPUT_FILE_PATH_PROPS_KEY, inputFilePath);
        props.setProperty(Constants.IGNORE_USERS_PROPS_KEY, "Jenkins,Jenkins User");
        props.setProperty(Constants.IGNORE_KEYWARDS_PROPS_KEY, Constants.IGNORE_KEYWARDS_DEFAULT);

        CommitReader commitReader = new CommitFileReader();
        commitReader.init(props);

        return commitReader;
    }
}
