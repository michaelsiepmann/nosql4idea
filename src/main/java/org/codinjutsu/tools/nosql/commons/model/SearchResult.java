package org.codinjutsu.tools.nosql.commons.model;

import org.codinjutsu.tools.nosql.commons.view.wrapper.ObjectWrapper;

import java.util.List;

public interface SearchResult {

    String getName();

    List<ObjectWrapper> getRecords();
}
