package com.writepavel.usergrid;

/**
 * Created by Pavel on 06.07.14.
 */

	/** 
	* Now this is incorrect class from sample. It has to be realized.
	  TODO Realize correct Users class and convert JSON to Users
	*/
public class Users {

    private final long id;
    private final String content;

    public Users(long id, String content) {
        this.id = id;
        this.content = content;
    }

    public long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }
}
