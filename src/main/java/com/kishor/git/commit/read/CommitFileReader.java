package com.kishor.git.commit.read;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

import com.kishor.git.Constants;
import com.kishor.git.model.Commit;

/**
 * Responsibility of this class is to read commit file and generate {@link java.util.Map map} of commits
 * @author kishor
 * git log --pretty=format:"%H%x09%an%x09%ad%x09%s" --date=unix > ~/Desktop/tmp/commits/main.csv
 */
public class CommitFileReader implements CommitReader {
    private Optional<String> fileName = null;

    @Override
    public void init(Properties props) {
        fileName = Optional.ofNullable(props.getProperty(Constants.INPUT_FILE_PATH_PROPS_KEY));
    }

    @Override
    public Map<String, List<Commit>> read() {
        Map<String, List<Commit>> commits = new LinkedHashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(new File(fileName.get())))) {
            reader.lines().forEach(l -> {
                Commit comm = new Commit(l);
                String key  = comm.getSubject();

                List<Commit> cmmList = commits.get(key);
                if (cmmList == null) {
                    cmmList = new ArrayList<Commit>();
                }

                cmmList.add(comm);
                commits.put(key, cmmList);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return commits;
    }
}
