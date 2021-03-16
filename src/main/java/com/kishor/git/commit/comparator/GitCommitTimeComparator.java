package com.kishor.git.commit.comparator;

import java.util.Comparator;

import com.kishor.git.model.Commit;

/**
 * A GIT commit comparator based on commit time.
 * @author kishor
 *
 */
public class GitCommitTimeComparator implements Comparator<Commit> {

    /**
     * Compares two GIT commits by their commit time.
     */
    @Override
    public int compare(Commit commit1, Commit commit2) {
        return commit1.getTime().compareTo(commit2.getTime());
    }
}