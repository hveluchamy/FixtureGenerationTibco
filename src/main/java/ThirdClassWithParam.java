import java.io.Serializable;

public class ThirdClassWithParam implements Serializable {
    public String returnParamTest(String testValue){
        return "Got this param " + testValue;
    }
}
