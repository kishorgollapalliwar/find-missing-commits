package com.kishor.git.commit.find;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeSet;

import com.kishor.git.model.Commit;

/**
 * Responsibility of this class is to find missing commits.
 * @author kishor
 *
 */
public class CommitFinder {

    public void init(Properties props) {
        
    }

    public TreeSet<Commit> find(Map<String, List<Commit>> base, Map<String, List<Commit>> other) {
        return null;
    }
}
