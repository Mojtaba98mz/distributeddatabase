package com.example.university.distributeddatabase.pojo;

public class CoreTimePojo {
    private Integer coreNumber;
    private Long executionTime;

    public CoreTimePojo(Integer coreNumber, Long executionTime) {
        this.executionTime = executionTime;
        this.coreNumber = coreNumber;
    }

    public Long getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(Long executionTime) {
        this.executionTime = executionTime;
    }

    public Integer getCoreNumber() {
        return coreNumber;
    }

    public void setCoreNumber(Integer coreNumber) {
        this.coreNumber = coreNumber;
    }
}
