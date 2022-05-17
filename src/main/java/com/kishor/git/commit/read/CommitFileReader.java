package com.kishor.git.commit.read;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;

import com.kishor.git.Constants;
import com.kishor.git.model.Commit;

/**
 * Responsibility of this class is to read commit file and generate {@link java.util.Map map} of commits
 * @author kishor
 * git log --pretty=format:"%H%x09%an%x09%ad%x09%s" --date=unix > ~/Desktop/tmp/commits/main.csv
 */
public class CommitFileReader implements CommitReader {
    private Optional<String> fileName = null;
    private Set<String> ignoreKeywordSet = new HashSet<>();
    private Set<String> ignoreUserSet    = new HashSet<>();

    @Override
    public void init(Properties props) {
        Objects.requireNonNull(props, "Missing required parameter prop");

        fileName = Optional.ofNullable(props.getProperty(Constants.INPUT_FILE_PATH_PROPS_KEY));
        addPropertiesToSet(props.getProperty(Constants.IGNORE_KEYWARDS_PROPS_KEY), ignoreKeywordSet);
        addPropertiesToSet(props.getProperty(Constants.IGNORE_USERS_PROPS_KEY), ignoreUserSet);
    }

    @Override
    public Map<String, List<Commit>> read() {
        Map<String, List<Commit>> commits = new LinkedHashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(new File(fileName.get())))) {
            reader.lines().forEach(l -> {
                Commit comm = new Commit(l);

                if (!ignoreCommit(comm)) {
                    String key  = comm.getSubject();
                    List<Commit> cmmList = commits.get(key);
                    if (cmmList == null) {
                        cmmList = new ArrayList<Commit>();
                    }

                    cmmList.add(comm);
                    commits.put(key, cmmList);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return commits;
    }

    private boolean ignoreCommit(Commit commit) {
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

        return ignoreCommitDueToSubject || ignoreCommitDueToUser;
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
