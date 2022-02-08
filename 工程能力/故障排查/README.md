故障排查
##摘自 Mini-Jvm 鲁大师的总结
纵向简单理解 。 就是时序图。  

app -> nginx -> web server -> service -> db -> os

每个节点都可以继续细化。

横向 是针对资源的  

CPU  
MEM  
Port  
Socket  
Fd  
Disk  
Queue/Pool
。。。
钱 



可以用  
USE： Utilisation, Saturation, Error  
USE 方法 来排查。  

全局视野:（纵向，横向）  
分而治之: (隔离问题域）  
控制变量: （猜测->改变->预测->验证）  
记录过程    。。。








