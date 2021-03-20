package com.kishor.git.commit;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import com.kishor.git.model.Commit;

/**
 * Responsibility of this class is to read commit file and generate {@link java.util.Map map} of commits
 * @author kishor
 *
 */
public class CommitFileReader implements CommitReader{

    private Optional<String> fileName = null;

    @Override
    public void init(Properties props) {
        fileName = Optional.ofNullable(props.getProperty("input.file.path"));
    }

    @Override
    public Map<String, List<Commit>> read() {
        // TODO Auto-generated method stub
        return null;
    }
}
