package com.coloful.constant;

public class Constant {

//    public enum System {
//        JSON_ACCOUNT("JSON_ACCOUNT");
//        private final String value;
//
//        System(String value) {
//            this.value = value;
//        }
//
//        public String getValue() {
//            return this.value;
//        }
//    }

    public enum Account {
        ID("id"),
        USERNAME("username"),
        PASSWORD("password"),
        DOB("dob"),
        EMAIL("email"),

        ALL_VALUES("all"),
        TABLE_NAME("Account");

        private final String value;

        Account(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public String[] getAllValues(){
            return new String[] {
                ID.getValue(), USERNAME.getValue(), PASSWORD.getValue(), DOB.getValue(), EMAIL.getValue()
            };
        }
    }

    public enum Quiz {
        ID("id"),
        TITLE("title"),
        AUTHOR("author"),

        ALL_VALUES("all"),
        TABLE_NAME("Quiz");

        private final String value;

        Quiz(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public String[] getAllValues(){
            return new String[] {
               ID.getValue(), TITLE.getValue(), AUTHOR.getValue()
            };
        }
    }

    public enum Question {
        ID("id"),
        CONTENT("question_content"),
        QUIZ_ID("quiz_id"),

        ALL_VALUES("all"),
        TABLE_NAME("Question");
        private final String value;

        Question(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public String[] getAllValues(){
            return new String[] {
                ID.getValue(), CONTENT.getValue(), QUIZ_ID.getValue()
            };
        }
    }

    public enum Answer {
        ID("id"),
        CONTENT("answer_content"),
        QUES_ID("question_id"),

        ALL_VALUES("all"),
        TABLE_NAME("Answer"),
        ;
        private final String value;

        Answer(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public String[] getAllValues(){
            return new String[] {
                ID.getValue(), CONTENT.getValue(), QUES_ID.getValue()
            };
        }
    }

    public enum QuizAccount {
        QUIZ_ID("quiz_id"),
        ACCOUNT_ID("account_id"),
        LAST_TIME_JOIN("last_time_join"),

        ALL_VALUES("all"),
        TABLE_NAME("Quiz_Account");
        private final String value;

        QuizAccount(String value) {
            this.value = value;
        }

        public String getValue() {
            return this.value;
        }

        public String[] getAllValues(){
            return new String[] {
                QUIZ_ID.getValue(), ACCOUNT_ID.getValue(), LAST_TIME_JOIN.getValue()
            };
        }
    }
}
