import org.junit.Assert;
import org.junit.Test;

public class DoesItCompileTest {

    @Test
    public void main() {
        DoesItCompile.main(new String[]{});
        Assert.assertTrue(true);
    }
}