package edu.ucsb.cs.cs184.speedrun.speedrunapp.user;

import java.util.Map;

/**
 * Created by giovanni_rojas on 12/4/17.
 */

public class Location {

    private SubLocation country;
    private SubLocation region;

    private static class SubLocation{
        private String code;
        private Map<String, String> names;
    }

}
