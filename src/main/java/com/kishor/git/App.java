package com.kishor.git;

import java.io.File;

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
    }

    private static void validateArguments(String[] args) {
        if (args.length < 3) {
            throw new RuntimeException("\n\nThis program expects 3 inputs as follow. \n1. Input directory \n2. Base Branch name \n3. Other Branch name");
        }
    }
}
