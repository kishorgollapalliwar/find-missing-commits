package com.kishor.git.commit.find;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import com.kishor.git.Constants;
import com.kishor.git.model.Commit;

/**
 * Responsibility of this class is to find missing commits.
 * @author kishor
 *
 */
public class CommitFinder {
    private Set<String> ignoreKeywordSet = new HashSet<>();

    public void init(Properties props) {
        String [] ignoreKeywords = props.getProperty(Constants.IGNORE_KEYWARDS_PROPS_KEY).split(",");
        for (int index=0; index < ignoreKeywords.length; index++) {
            ignoreKeywordSet.add(ignoreKeywords[index]);
        }
    }

    public TreeSet<Commit> find(Map<String, List<Commit>> base, Map<String, List<Commit>> other) {
        return null;
    }
}
