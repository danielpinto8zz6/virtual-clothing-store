package com.sample;

public class Constants {
    public enum Gender {
        MALE("Male"), FEMALE("Female"), KID("Kid");

        private final String name;

        private Gender(String g) {
            name = g;
        }

        public boolean equalsName(String otherName) {
            return name.equals(otherName);
        }

        public String toString() {
            return this.name;
        }
    }

    public enum MaritalStatus {
        MARRIED, SINGLE;
    }
}