要求：
之前服务器端只是被动应答模式，要求服务器端也能主动发送信息
增加聊天模式
服务器端对客户端进行统一的管理

增加一个类ClientManage.java，用于管理客户端
在服务器开启时，同时开启这个线程

ClientManage.java
成员变量 vSocket，用于保存每个客户端连接
成员变量vMessage，用于保存每个客户端发送过来的消息
在线程中，每隔200ms，就对客户端连接进行检测，然后将vMessage中的信息进行群发

增加一个类ServerManage.java，用于管理服务器
在服务器开启时，同时开启这个线程

ServerManage.java
成员变量 ClientManage cm;
在线程中，不断循环，从服务器的控制台获取命令，然后根据管理员的输入来进行不同的操作，目前只支持shutdown,showclientnum，showallclientinfo这三个命令

