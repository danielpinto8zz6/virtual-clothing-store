package com.sample;

public class Constants {
    public enum Gender {
        MALE("male"), FEMALE("female");

        private final String gender;

        Gender(String gender) {
            this.gender = gender;
        }

        @Override
        public String toString() {
            return gender;
        }
    }

    public enum MaritalStatus {
        MARRIED("married"), SINGLE("single");

        private final String status;

        MaritalStatus(String status) {
            this.status = status;
        }

        @Override
        public String toString() {
            return status;
        }
    }
}