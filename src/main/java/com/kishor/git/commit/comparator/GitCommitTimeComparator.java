package com.kishor.git.commit.comparator;

import java.util.Comparator;

import com.kishor.git.model.Commit;

public class GitCommitTimeComparator implements Comparator<Commit> {

    @Override
    public int compare(Commit commit1, Commit commit2) {
        return commit1.getTime().compareTo(commit2.getTime());
    }

}