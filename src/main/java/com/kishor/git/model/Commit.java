package com.kishor.git.model;

public class Commit implements Comparable<Commit>{
    private String hash;
    private String author;
    private Long   time;
    private String subject;

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Commit(String commitLine) {
        String str[] = commitLine.split("\t");
        this.hash    = str[0];
        this.author  = str[1];
        this.time    = Long.valueOf(str[2]);
        this.subject = str[3];
    }

    @Override
    public String toString() {
        return "Commit [hash=" + hash + ", author=" + author + ", time=" + time + ", subject=" + subject + "]";
    }

    @Override
    public int compareTo(Commit o) {
        return this.time.compareTo(o.time);
    }
}
