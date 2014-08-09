package com.writepavel.usergrid.domain;

/**
 * Created by Pavel on 20.07.2014.
 */
public interface UserNode {
    void setText(String text);
    String getText();
    void setLeaf(boolean leaf);
    boolean isLeaf();
    void setId(String text);
    String getId();
}
