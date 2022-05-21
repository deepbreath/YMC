什么LinkedList?
可以看成一个顺序容器，也可以看成一个队列

LikedList 同时实现了 List 和 Deque 接口

LikedList 底层实现?
双向链表实现

什么是双向链表：
后厨循环的存放菜品的，传输带 
双向链表的插入删除过程。

getFirst 和 getLast

删除双向传送链上的菜品呢?
使用 remove(index) 删除指定的位置的菜品
如下使用 remove(object) 可以存放 null 也可以删除 null，会删除链表中第一次出现的 null 值

在双向链表上添加菜品  
存在两种添加方式：
1。在 LinkedList 容器末尾插入元素
2。在 LinkedList 指定位置插入元素

addAll 的实现

