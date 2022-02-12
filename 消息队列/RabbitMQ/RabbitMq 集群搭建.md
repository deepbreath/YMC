# 高可用 RabbitMQ 集群简单搭建  

## 契机
- 迫于自己在生产环境中写了个简单的同步服务,导致各种问题,为了减少头发的掉落,故搭建高可用消息服务,添加新路径并保留历史功能(理解成 PLAN B).
不要问我一开始为什么不用? 问就是我技~蔬菜.  
- 由于参考的文章里面里来的镜像是三年前的镜像, 里面的版本可以根据自己的口味进行修改,基础配置没有什么太大变化

## 工具栈
1.三台及以上服务器(这里使用的是 AWS ECS,系统为 AWS Linux)  
2.Docker swarm  
3.Consul  
4.RabbitMQ  
5.Haproxy  
6.[Aws Ecs Docker Install](https://docs.aws.amazon.com/AmazonECS/latest/developerguide/docker-basics.html)  

## 需要了解基础
1.使用 Dockerfile 打包镜像  
2.基本 docker 命令操作  
3.了解 Compose 模板文件  

## 什么是 [Docker Swarm](https://www.runoob.com/docker/docker-swarm.html)
Docker Swarm 是 Docker 的集群管理工具。  
它将 Docker 主机池转变为单个虚拟 Docker 主机。  
Docker Swarm 提供了标准的 Docker API，所有任何已经与 Docker 守护程序通信的工具都可以使用 Swarm 轻松地扩展到多个主机。  

## 什么是 [Consul](https://www.consul.io/)  
一个由 HashiCorp 公司开源的分布式服务发现与配置工具  
基于 CAP 理论  
其他家的有 ZooKeeper、Eureka、Consul 、Nacos  

## 什么是 [HAPROXY](http://www.haproxy.org/)
一款 C 语言编写 提供高可用性,负载均衡,  
基于 TCP 和 HTTP 的开源代理应用程序.

## 什么是 [RabbitMQ](https://www.rabbitmq.com/)
RabbitMQ 是实现了高级消息队列协议(AMQP)的开源信息代理软件(面向消息的中间件)  
HA 经典镜像  
Quorum Queues    

## 安装配置
### 起手式: 初始化 Docker Swarm
[Docker Swarm Init 官方文档](https://docs.docker.com/engine/swarm/swarm-tutorial/)
#### 开放服务器以下端口:(EC2 根据安全组配置)
TCP 2377(用于集群管理之间的联系)  
TCP 和 UDP 7946端口 (node节点之间建立联系)  
UDP 4789端口用来 overlay network traffic (覆盖网络流量)  

#### [创建 Swarm](https://docs.docker.com/engine/swarm/swarm-tutorial/create-swarm/)
在当前 node1 服务器中执行以下命令:  

`docker swarm init --advertise-addr <当前服务器IP>
`    

输出类似结果:  

    Swarm initialized: current node (dxn1zf6l61qsb1josjja83ngz) is now a manager.
    To add a worker to this swarm, run the following command:
   
    docker swarm join --token SWMTKN-1-49nj1cmql0jkz5s954yi3oex3nedyz0fb0xx14ie39trti4wxv-8vxv8rssmk743ojnwacrr2e7c
    192.168.99.100:2377  

    To add a manager to this swarm, run 'docker swarm join-token manager' and follow the instructions.

将复制中间生成的 swarm join 命令并在 node2 node3 中服务器中执行加入 swarm 集群

在 node1 中查看集群状态:  

`docker node ls`  

输出类似:  

    ID                            HOSTNAME          STATUS    AVAILABILITY   MANAGER STATUS   ENGINE VERSION
    kgpzsrjxq1mflwzvwsant1u1j     ip-172-31-18-29   Ready     Active                          20.10.7
    zj7bo9qsyhu4ww0fntbp0t00g     ip-172-31-23-45   Ready     Active                          20.10.7
    tar80zcbvxu3khsbo7gldmumu *   ip-172-31-24-58   Ready     Active         Leader           20.10.7

此时我们已经完成了基本的 Docker swarm 初始化  

### 创建 Docker Swarm 容器互联网络
[创建 Docker overlay 网络](https://docs.docker.com/network/overlay/)  
执行:  
`docker network create --driver=overlay --attachable prod`  

检查网络:  
`docker network ls`  

输出:  

    NETWORK ID     NAME              DRIVER    SCOPE
    d159c522fc03   bridge            bridge    local
    sggctksbbnjv   consul_consul     overlay   swarm
    754eb199d9bf   docker_gwbridge   bridge    local
    7927bb120a36   host              host      local
    vli3gjql43he   ingress           overlay   swarm
    405bcddd898d   none              null      local
    7x14ay79sr5k   prod              overlay   swarm

### Consul:
[Consul Compose File](https://github.com/olgac/consul/blob/master/docker-compose.yml)

    version: '3.6'
    services:
    consul:
        image: consul:1.4.0
        hostname: "{{.Node.Hostname}}"
        networks:
            - consul
            - prod
        ports:
            - 8400:8400
            - 8500:8500
            - 8600:53
        volumes:
            - consul-data:/consul/data
        deploy:
        mode: global
        placement:
        constraints: [node.labels.consul == true]
        command: [ "agent", "-server", "-bootstrap-expect=3", "-retry-max=3", "-retry-interval=10s", "-datacenter=prod", "-join=consul", "-retry-join=consul", "-bind={{ GetInterfaceIP \"eth0\" }}", "-client=0.0.0.0", "-ui"]
    networks:
        consul:
        prod:
            external: true
    volumes:
        consul-data:

第一步执行:  
目的:  
`docker node update --label-add consul=true node-1`  

第二步执行:  
目的: 使用 docker-compose.yml 启动服务栈
`docker stack deploy -c docker-compose.yml consul`  

检查 Consul Leader 节点地址:  
(节点选举出 Leader 才会有返回. 若没有返回, 可尝试清理所有节点数据卷, 删除 prod 网络)  

`curl <node1 ip>:8500/v1/status/leader`  
输出:  
`"10.0.1.9:8300"`

检查 Consul Peers:  
`curl <node1 ip>:8500/v1/status/peers`  
输出:  
`[
"10.0.1.8:8300",
"10.0.1.9:8300",
"10.0.1.10:8300"
]`

此时需要服务器开通 8500 端口,请求:http://<IP>:8500 进入 Consul 管理界面  
![图片](https://miro.medium.com/max/630/1*5U43SPCFA2nSm1I5ALfLSA.png)


###RabbitMQ 上场:

RabbitMQ 镜像打包文件:  
[RabbitMQ Config File](https://github.com/olgac/rabbitmq/blob/master/config/rabbitmq.conf)  

    loopback_users.admin = false
    cluster_formation.peer_discovery_backend = rabbit_peer_discovery_consul
    cluster_formation.consul.host = consul
    cluster_formation.node_cleanup.only_log_warning = true
    cluster_formation.consul.svc_addr_auto = true
    cluster_partition_handling = autoheal
    vm_memory_high_watermark.relative = 0.8
    disk_free_limit.absolute = 5GB

[RabbitMQ Enabled Plugins](https://github.com/olgac/rabbitmq/blob/master/config/enabled_plugins)  

    [rabbitmq_management,
    rabbitmq_peer_discovery_consul,
    rabbitmq_federation,
    rabbitmq_federation_management,
    rabbitmq_shovel,
    rabbitmq_shovel_management].

[RabbitMQ Dockerfile](https://github.com/olgac/rabbitmq/blob/master/Dockerfile)  

    FROM rabbitmq:3.7.8-management (可以使用最新镜像)

    ADD config/ /etc/rabbitmq/


[RabbitMQ Compose File](https://github.com/olgac/rabbitmq/blob/master/docker-compose.yml)

    version: "3.6"
    services:
      rabbitmq-01:
        image: olgac/rabbitmq:3.7.8-management (此处可以更改为自己打包的镜像,可以参照作者的配置文件打包,也可以自定义)
        hostname: rabbitmq-01 (主机名)
        environment: (设置默认账号 以及 密码,以及集群之间通信的 COOKIE 可以使用 echo "你的内容生成sha256sum" | sha256sum)
          - RABBITMQ_DEFAULT_USER=admin
          - RABBITMQ_DEFAULT_PASS=Passw0rd
          - RABBITMQ_ERLANG_COOKIE="MY-SECRET-KEY-123"
        networks:(配置容器连接的网络)
          - prod (网络的名称)
        volumes:(数据卷)
          - rabbitmq-01-data:/var/lib/rabbitmq
        deploy:
         mode: global
         placement:
           constraints: [node.labels.rabbitmq1 == true]
      rabbitmq-02:
          image: olgac/rabbitmq:3.7.8-management
          hostname: rabbitmq-02
          environment:
              - RABBITMQ_DEFAULT_USER=admin
              - RABBITMQ_DEFAULT_PASS=Passw0rd
              - RABBITMQ_ERLANG_COOKIE="MY-SECRET-KEY-123"
          networks:
            - prod
          volumes:
            - rabbitmq-02-data:/var/lib/rabbitmq
          deploy:
            mode: global
            placement:
              constraints: [node.labels.rabbitmq2 == true]
      rabbitmq-03:
          image: olgac/rabbitmq:3.7.8-management
          hostname: rabbitmq-03
          environment: 
            - RABBITMQ_DEFAULT_USER=admin
            - RABBITMQ_DEFAULT_PASS=Passw0rd
            - RABBITMQ_ERLANG_COOKIE="MY-SECRET-KEY-123"
          networks:
            - prod
          volumes:
            - rabbitmq-03-data:/var/lib/rabbitmq
          deploy:
            mode: global
            placement:
              constraints: [node.labels.rabbitmq3 == true]
      networks:
        prod:
          external: true
      volumes:(在每个节点创建数据卷)
      rabbitmq-01-data:
      rabbitmq-02-data:
      rabbitmq-03-data:

执行:  

    wget https://raw.githubusercontent.com/olgac/rabbitmq/master/docker-compose.yml  

    docker stack deploy -c docker-compose.yml rabbitmq

    docker service ls

输出:  

    ID             NAME                   MODE      REPLICAS   IMAGE                                            PORTS
    weg5e47zxjcc   consul_consul          global    3/3        consul:latest                                    *:8400->8400/tcp, *:8500->8500/tcp, *:8600->53/tcp
    wrm610s34908   rabbitmq_rabbitmq-01   global    1/1        deepbreathline/rabbitmq:3.9-management
    yug25i9bunby   rabbitmq_rabbitmq-02   global    1/1        deepbreathline/rabbitmq:3.9-management
    q1rc93nw7d22   rabbitmq_rabbitmq-03   global    1/1        deepbreathline/rabbitmq:3.9-management


### Haproxy 来个收尾:
[Haproxy Compose File](https://github.com/olgac/rabbitmq/blob/master/docker-compose.yml)  
  
    version: "3.6"
    services:
        haproxy:
          image: olgac/haproxy-for-rabbitmq:1.8.14-alpine
          ports:
            - 15672:15672
            - 5672:5672
            - 1936:1936
          networks:
            - prod
          deploy:
            mode: global
    networks:
      prod:
        external: true

[Haproxy Config File](https://github.com/olgac/haproxy-for-rabbitmq/blob/master/config/haproxy.cfg);

    global
    log 127.0.0.1   local0
    log 127.0.0.1   local1 notice
    maxconn 4096

    defaults
    log     global
    option  tcplog
    option  dontlognull
    timeout connect 6s
    timeout client 60s
    timeout server 60s

    listen  stats
    bind *:1936
    mode http
    stats enable
    stats hide-version
    stats realm Haproxy\ Statistics
    stats uri /
    listen rabbitmq
    bind   *:5672
    mode   tcp
    server rabbitmq-01 rabbitmq-01:5672 check
    server rabbitmq-02 rabbitmq-02:5672 check
    server rabbitmq-03 rabbitmq-03:5672 check
    listen rabbitmq-ui
    bind   *:15672
    mode   http
    server rabbitmq-01 rabbitmq-01:15672 check
    server rabbitmq-02 rabbitmq-02:15672 check
    server rabbitmq-03 rabbitmq-03:15672 check

[Haproxy Dockerfile](https://github.com/olgac/haproxy-for-rabbitmq/blob/master/Dockerfile);

    FROM haproxy:1.8.14-alpine
    COPY config/haproxy.cfg /usr/local/etc/haproxy/haproxy.cfg  

执行:
    
    wget https://github.com/olgac/haproxy-for-rabbitmq/blob/master/docker-compose.yml

    docker stack deploy -c docker-compose.yml haproxy
    
    docker service ls

输出:

    ID             NAME                   MODE      REPLICAS   IMAGE                                            PORTS
    weg5e47zxjcc   consul_consul          global    3/3        consul:latest                                    *:8400->8400/tcp, *:8500->8500/tcp, *:8600->53/tcp
    2jdlcuwjgp1v   haproxy_haproxy        global    3/3        deepbreathline/haproxy-for-rabbitmq:2.3-alpine   *:1936->1936/tcp, *:5672->5672/tcp, *:15672->15672/tcp
    wrm610s34908   rabbitmq_rabbitmq-01   global    1/1        deepbreathline/rabbitmq:3.9-management
    yug25i9bunby   rabbitmq_rabbitmq-02   global    1/1        deepbreathline/rabbitmq:3.9-management
    q1rc93nw7d22   rabbitmq_rabbitmq-03   global    1/1        deepbreathline/rabbitmq:3.9-management



### 打完收工:

#### 验证 RabbitMQ:
请求: http://<IP地址>:15672  
![图片](https://miro.medium.com/max/630/1*ZPidzjOzJZ-xb6_PvvBiQQ.png)

#### 验证 Haproxy:
请求: http://<IP地址>:1936  
![图片](https://miro.medium.com/max/630/1*tft6ptkLFDeJ32u3qpTWyg.png)

#### 盘点一下遇到的坑:
1. 使用 Docker 数据卷时, 需要使用 Docker volume prune 清理每个节点无关联数据卷  
2. 端口之间之间的关系需要整理清楚可以使用 0.0.0.0/16 或者 0.0.0.0/24 来匹配私有网络服务之间的地址  
3. 清理 docker node update lab 标记  
遇到的知识点并展开:
  docker network
  deploy mode
  volumes

### 举一反三发布点别的
Redis 缓存集群



### 参考 Copy [Rabbitmq-cluster-on-docker-swarm-using-consul-based-discovery](https://medium.com/hepsiburadatech/implementing-highly-available-rabbitmq-cluster-on-docker-swarm-using-consul-based-discovery-45c4e7919634)