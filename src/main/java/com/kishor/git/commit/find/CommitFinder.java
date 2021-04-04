package com.kishor.git.commit.find;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import com.kishor.git.Constants;
import com.kishor.git.commit.comparator.GitCommitTimeComparator;
import com.kishor.git.model.Commit;

/**
 * Responsibility of this class is to find missing commits.
 * 
 * @author kishor
 *
 */
public class CommitFinder {
    private Set<String> ignoreKeywordSet = new HashSet<>();
    private Set<String> ignoreUserSet    = new HashSet<>();

    public void init(Properties props) {
        if (Objects.nonNull(props)) {
//          String[] ignoreKeywords = props.getProperty(Constants.IGNORE_KEYWARDS_PROPS_KEY).split(",");
//          for (int index = 0; index < ignoreKeywords.length; index++) {
//              ignoreKeywordSet.add(ignoreKeywords[index]);
//          }
          addPropertiesToSet(props.getProperty(Constants.IGNORE_KEYWARDS_PROPS_KEY), ignoreKeywordSet);

//          String[] ignoreUsers = props.getProperty(Constants.IGNORE_USERS_PROPS_KEY).split(",");
//          for (int index = 0; index < ignoreUsers.length; index++) {
//              ignoreUserSet.add(ignoreUsers[index]);
//          }
          addPropertiesToSet(props.getProperty(Constants.IGNORE_USERS_PROPS_KEY), ignoreUserSet);
        }
    }

    public TreeSet<Commit> find(Map<String, List<Commit>> srcCommitMap, Map<String, List<Commit>> destCommitMap) {
        TreeSet<Commit> missingCommits = new TreeSet<Commit>(new GitCommitTimeComparator());

        /**
         * Iterate over each source commit and try to find corresponding commit in destination
         */
        for (Map.Entry<String, List<Commit>> commitEntry : srcCommitMap.entrySet()) {
            List<Commit> destCommitList = destCommitMap.get(commitEntry.getKey());
            if (destCommitList == null || destCommitList.size() != commitEntry.getValue().size()) {

                for (Commit commit : commitEntry.getValue()) {
                    // check ignore keyword present in subject
                    boolean ignoreCommitDueToSubject = false;
                    for (String ignoreSubjectKeyword: ignoreKeywordSet) {
                        if (commit.getSubject().startsWith(ignoreSubjectKeyword)) {
                            ignoreCommitDueToSubject = true;
                            break;
                        }
                    }

                    // check ignore user
                    boolean ignoreCommitDueToUser    = false;
                    if (ignoreUserSet.contains(commit.getAuthor())) {
                        ignoreCommitDueToUser = true;
                    }

                    if (ignoreCommitDueToSubject || ignoreCommitDueToUser) {
//                        System.out.println("Ignoring commit : "+commit.getSubject());
                    } else {
                        missingCommits.add(commit);
                    }
                }
            }
        }

        return missingCommits;
    }

    private void addPropertiesToSet(String valuesStr, Set<String> set) {
        if (Objects.nonNull(valuesStr)) {
            String[] values = valuesStr.split(",");

            if (Objects.nonNull(values)) {
                for (int index = 0; index < values.length; index++) {
                    set.add(values[index]);
                }
            }
        }
    }
}
