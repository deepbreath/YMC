import com.yc.aop.GreetBeforeAdvice;
import com.yc.aop.Waiter;
import com.yc.aop.WaiterImpl;
import org.junit.jupiter.api.Test;
import org.springframework.aop.BeforeAdvice;
import org.springframework.aop.framework.ProxyFactory;

public class BeforeAdviceTest {
    @Test
    public void before(){
        Waiter target=new WaiterImpl();

        BeforeAdvice advice=new GreetBeforeAdvice();

        ProxyFactory pf=new ProxyFactory();
        pf.setTarget(target);

        pf.addAdvice(advice);

        Waiter proxy= (Waiter) pf.getProxy();

        proxy.greatTo("Chen");
        proxy.serverTo("Chen");

    }
}
