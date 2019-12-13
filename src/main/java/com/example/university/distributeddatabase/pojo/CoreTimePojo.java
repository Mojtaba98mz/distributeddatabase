package com.example.university.distributeddatabase.pojo;

public class CoreTimePojo {
    private Integer coreNumber;
    private Long executionTime;
    private int level;

    public CoreTimePojo(Integer coreNumber, Long executionTime) {
        this.executionTime = executionTime;
        this.coreNumber = coreNumber;
    }

    public CoreTimePojo(Integer coreNumber, Long executionTime, int level) {
        this.coreNumber = coreNumber;
        this.executionTime = executionTime;
        this.level = level;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public Integer getCoreNumber() {
        return coreNumber;
    }

    public void setCoreNumber(Integer coreNumber) {
        this.coreNumber = coreNumber;
    }
}
