import com.yc.aop.*;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

public class ForumServiceTest2 {
    @Test
    public void proxy(){
        ForumServiceImpl target=new ForumServiceImpl();

        PerformanceHandler handler=new PerformanceHandler(target);

        ForumService proxy= (ForumService) Proxy.newProxyInstance(target.getClass().getClassLoader(),target.getClass().getInterfaces(),handler);

        proxy.removeForum(10);
        proxy.removeTopic(204);

    }
}
