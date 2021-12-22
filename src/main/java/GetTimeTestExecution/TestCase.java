package GetTimeTestExecution;

public class TestCase {

    private double executionTime = 0;
    private String classe = "";
    private String test = "";

    public TestCase() {
    }

    public TestCase(double pExecutionTime, String pClasse, String pTest) {
        this.executionTime = pExecutionTime;
        this.classe = pClasse;
        this.test = pTest;
    }

    public double getExecutionTime() {
        return executionTime;
    }

    public void setExecutionTime(double executionTime) {
        this.executionTime = executionTime;
    }

    public String getClasse() {
        return classe;
    }

    public void setClasse(String classe) {
        this.classe = classe;
    }

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }
}
