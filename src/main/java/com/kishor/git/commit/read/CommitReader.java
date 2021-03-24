package com.kishor.git.commit.read;

import java.util.List;
import java.util.Map;
import java.util.Properties;

import com.kishor.git.model.Commit;

/**
 * Interface representing family of classes which will be used to read commit file
 * @author kishor
 *
 */
public interface CommitReader {
    public void init(Properties props);
    public Map<String, List<Commit>> read();
}
