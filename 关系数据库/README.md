https://dunwu.github.io/db-tutorial/sql/common/sql-interview.html#%E4%B8%80%E3%80%81%E7%B4%A2%E5%BC%95%E5%92%8C%E7%BA%A6%E6%9D%9F
1. 什么是索引? (数据结构欠缺,B树 和 B+ 树),简单复习完成
   1. 索引的优缺点?  增加查询速度 缺点是啥?
   2. 何时使用索引   根据查询条件查询速度缓慢时候 什么时候不适用  写操作频繁不适合,需要更新索引空间 非常小的表也不适合几万数量的表, 特大型的表不适合
   3. 索引的数据结构  B树
   4. 索引策略       不知道
   5. 约束          不知道
2. 并发控制
   1. 乐观锁和悲观锁  乐观锁依据主键是否存在选择更新操作 ,悲观锁是什么呢?锁行? (https://juejin.cn/post/6844903639207641096)(https://juejin.cn/post/6844903645125820424#heading-16)
   2. 行级锁和表级锁 干嘛用的?
   3. 读写锁  不清楚
   4. 意向锁  不清楚
   5. MVCC   不清楚
   6. Next-key 锁 不清楚
3. 事务  
   1. ACID 原子性 一致性 
   2. 并发一致性
   3. 分布式事务  不清楚什么场景下需要分布式事务
   4. 事务隔离   有哪几种事务隔离级别? 有四种吧 
4. 分库分表
   1. 什么是分库分表  单表数据量过大时候, 什么才算数据量过大? 水平分表,垂直分表, 全局唯一性id 雪花算法 leaf
   2. 分库分表中间件 不清楚
   3. 分库分表的问题  主键 ID 如何保持唯一
5. 集群
   1. 复制机制  主从复制 多主节点复制  无主节点复制 如何保证数据最终的一致性?
   2. 读写分离机制
6. 数据库优化
   1. SQL 优化  小表join大表
   2. 结构优化
   3. 配置优化
   4. 硬件优化
7. 数据库理论
   1. 函数依赖
   2. 异常
   3. 范式
8. Msql存储引擎
   1. InnoDB vs MylSAM
9. 数据库比较
   1.常见数据库比较
10. Oracle vs Mysql vs Pgsql
    1. 数据类型比较

一些资料:
https://github.com/jobbole/awesome-mysql-cn
(索引资源)[https://www.kancloud.cn/jdxia/booknote/545266]  
(数据库思维导图)[https://www.pdai.tech/md/db/sql/sql-db.html]  


