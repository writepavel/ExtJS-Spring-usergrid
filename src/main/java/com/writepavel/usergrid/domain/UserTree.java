package com.writepavel.usergrid.domain;

/**
 * Created by Pavel on 06.07.14.
 */

import java.util.LinkedHashSet;

/**
     * UserTree extends ArrayList for correct generating JSON-body by
     * Spring's @ResponseBody.
	*/
public class UserTree extends LinkedHashSet<UserNode> {

    @Override
    public String toString(){
        return "List of Users:\n" + UserTreeBuilder.childrenString(this);
    }
}
