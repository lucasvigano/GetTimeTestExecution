package GetTimeTestExecution;

import java.util.ArrayList;
import java.util.List;

public class ResultTestCase {

    private List<TestCase> vTestCase = new ArrayList<>();
    private String error = "";
    private String classe = "";
    private double totalTime = 0;

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public List<TestCase> getvTestCase() {
        return vTestCase;
    }

    public void setvTestCase(List<TestCase> vTestCase) {
        this.vTestCase = vTestCase;
    }

    public double getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(double totalTime) {
        this.totalTime = totalTime;
    }
}
