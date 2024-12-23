package ua.cn.stu.univer03.debt;

import org.springframework.stereotype.Component;

@Component
public class GlobalVariable {
    private String globalVar = "Initial Value";

    public String getGlobalVar() {
        return globalVar;
    }

    public void setGlobalVar(String globalVar) {
        this.globalVar = globalVar;
    }
}
