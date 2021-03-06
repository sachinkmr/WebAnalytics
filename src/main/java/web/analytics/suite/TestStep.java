package web.analytics.suite;

public class TestStep {
    private String stepNumber;
    private TestCase testCase;
    private String action;
    private String testStepID;
    private String objectLocator;
    private String onError;
    private String data;
    private TestStatus status;
    private String ex;

    public String getEx() {
	return ex;
    }

    public void setEx(String ex) {
	this.ex = ex;
    }

    public TestStatus getStatus() {
	return this.status;
    }

    public void setStatus(TestStatus status) {
	this.status = status;
    }

    public String getStepNumber() {
	return stepNumber;
    }

    public void setStepNumber(String stepNumber) {
	this.stepNumber = stepNumber;
    }

    public String getAction() {
	return action;
    }

    public void setAction(String action) {
	this.action = action;
    }

    public String getTestStepID() {
	return testStepID;
    }

    public void setTestStepID(String testStepID) {
	this.testStepID = testStepID;
    }

    public String getObjectLocator() {
	return objectLocator;
    }

    public void setObjectLocator(String objectLocator) {
	this.objectLocator = objectLocator;
    }

    public boolean onError() {
	return !onError.equalsIgnoreCase("continue");
    }

    public void setOnError(String onError) {
	this.onError = onError;
    }

    public String getData() {
	return data;
    }

    public void setData(String data) {
	this.data = data;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + ((stepNumber == null) ? 0 : stepNumber.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	TestStep other = (TestStep) obj;
	if (stepNumber == null) {
	    if (other.stepNumber != null)
		return false;
	} else if (!stepNumber.equals(other.stepNumber))
	    return false;
	return true;
    }

    @Override
    public String toString() {
	return stepNumber;
    }

    public TestCase getTestCase() {
	return testCase;
    }

    public void setTestCase(TestCase testCase) {
	this.testCase = testCase;
    }

}
