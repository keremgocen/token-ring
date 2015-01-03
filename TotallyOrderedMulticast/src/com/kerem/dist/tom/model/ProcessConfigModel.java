package com.kerem.dist.tom.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by keremgocen on 12/30/14.
 */
public class ProcessConfigModel {
    private Integer processId;
    private Integer totalNumberOfProcesses;
    private List<Integer> portList;

    public Integer getProcessId() {
        return processId;
    }

    public void setProcessId(Integer processId) {
        this.processId = processId;
    }

    public Integer getTotalNumberOfProcesses() {
        return totalNumberOfProcesses;
    }

    public void setTotalNumberOfProcesses(Integer totalNumberOfProcesses) {
        this.totalNumberOfProcesses = totalNumberOfProcesses;
    }

    public List<Integer> getPortList() {
        return portList;
    }

    public void setPortList(List<Integer> portList) {
        this.portList = portList;
    }

    @Override
    public String toString() {
        return "\nCONFIG PARAMETERS\nprocessId:" + processId +
         " totalNumberOfProcesses:" + totalNumberOfProcesses +
         " portList:" + portList.toString();
    }
}
