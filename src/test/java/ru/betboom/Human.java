package ru.betboom; // класс который полностью мапится на JSON

import java.util.List;

public class Human {
    public String name;
    public Integer age; // используем обертку чтоб по-дефолту был null
    public Boolean isClever;
    public List<String> hobbies;
    public Passport passport;

    public static class Passport {
        public Long number;
        public String issueDate;
    }
}
