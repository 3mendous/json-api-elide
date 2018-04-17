package ru.sbt.qa;

import com.yahoo.elide.Elide;

//Implemented this interface as a lambda to make working with Elide easier
public interface ElideCallable {
    String call(final Elide elide, final String path);
}
