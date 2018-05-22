# SpringCloud-interfaceLimit
接口限流 redis + lua 实现

基于redis+lua
设置限流接口的方法,http://192.168.1.42:9999/setLimiter3?requestCount=3&url=http://www.baidu.com/

单例接口调用(feign)
设置限流接口的方法,http://192.168.1.42:9999/setLimiter2?requestCount=3

单例接口调用(restTemplate)
设置限流接口的方法,http://192.168.1.42:9999/setLimiter?requestCount=3&url=http://www.baidu.com/

服务调用接口(ceshi模块下,启动Application类)
http://192.168.1.42:9999/say


模块介绍:
1.eureka-server  Eureka服务器,端口:8761
2.providerB      启动服务实例(两个),端口:8900,9000
3.ceshi          服务调用方,端口:9999
