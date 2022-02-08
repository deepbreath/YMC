import com.yc.aop.CglibProxy;
import com.yc.aop.ForumServiceImpl;
import org.junit.jupiter.api.Test;

public class ForumServiceTest {
    @Test
    public void proxy(){
        CglibProxy proxy=new CglibProxy();
        ForumServiceImpl forumService= (ForumServiceImpl) proxy.getProxy(ForumServiceImpl.class);
        forumService.removeForum(10);
        forumService.removeTopic(1023);
    }
}
