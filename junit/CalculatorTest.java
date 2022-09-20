package examples;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

public class CalculatorTest {

    static calculator calc;

    @BeforeAll
    public static void setUp(){
        calc = new calculator();
    }

    @Test
    public void addTest() throws Exception{
        int result1 = calc.add(5,10);

        Assertions.assertEquals(15,result1,"The value should be 15");
    }
}
