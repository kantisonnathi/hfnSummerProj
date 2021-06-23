package org.heartfulness.avtc.model.AfterCallClasses;

import org.heartfulness.avtc.model.Call;

import java.util.ArrayList;
import java.util.List;

public class CategoryCreationDTO {
    private List<Call> callList;
    public CategoryCreationDTO(){
        callList=new ArrayList<>();
    }
    public void addCall(Call call) {
        this.callList.add(call);
    }
    public List<Call> getCallList() {
        return callList;
    }

    public void setCallList(List<Call> callList) {
        this.callList = callList;
    }
}
