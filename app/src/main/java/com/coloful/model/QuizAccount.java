package com.coloful.model;

import java.time.LocalDateTime;

public class QuizAccount {
    private int quizId;
    private int accountId;
    private LocalDateTime lastTimeAccess;

    public int getQuizId() {
        return quizId;
    }

    public void setQuizId(int quizId) {
        this.quizId = quizId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public LocalDateTime getLastTimeAccess() {
        return lastTimeAccess;
    }

    public void setLastTimeAccess(LocalDateTime lastTimeAccess) {
        this.lastTimeAccess = lastTimeAccess;
    }
}
