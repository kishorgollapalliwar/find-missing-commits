package com.kishor.git;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.kishor.git.commit.read.CommitFileReader;
import com.kishor.git.commit.read.CommitReader;
import com.kishor.git.model.Commit;

/**
 * Main class
 * Responsibility of this class is as follows
 * <ol>
 *  <li>Input validation</li>
 *  <li>Parse input</li>
 *  <li>Delegate commit finding functionality</li>
 * </ol>to start the commit finding process
 *
 * @author kishor
 */

public class App {
    public static void main( String[] args ) {
        //#1. Input Validation
        validateArguments(args);

        //#2. Input parsing/ cleansing
        String inputDirectory = args[0];
        String baseBranchName = args[1];
        String otherBranchName= args[2];

        if (!inputDirectory.endsWith(File.separator)) {
            inputDirectory = inputDirectory + File.separator;
        }

        //#3 Read base branch commits
        CommitReader baseCommitReader = getCommitFileReader(inputDirectory+File.separator+baseBranchName);
        Map<String, List<Commit>> baseBranch = baseCommitReader.read();

        //#4 Read other branch commits
        CommitReader otherCommitReader = getCommitFileReader(inputDirectory+File.separator+otherBranchName);
        Map<String, List<Commit>> otherBranch = otherCommitReader.read();
    }

    private static void validateArguments(String[] args) {
        if (args.length < 3) {
            throw new RuntimeException("\n\nThis program expects 3 inputs as follow. \n1. Input directory \n2. Base Branch name \n3. Other Branch name");
        }
    }

    private static CommitReader getCommitFileReader(final String inputFilePath) {
        Properties props = new Properties();
        props.setProperty(Constants.INPUT_FILE_PATH_PROPS_KEY, inputFilePath);

        CommitReader commitReader = new CommitFileReader();
        commitReader.init(props);

        return commitReader;
    }
}
