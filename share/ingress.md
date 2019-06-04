# Ingress

管理对集群中的服务(通常是HTTP)的外部访问的API对象。Ingress可以提供负载平衡、SSL终端和基于名称的虚拟主机。

## 术语

为清楚起见，本指南定义了以下术语:

* Node（节点）: kubernetes集群中的一台虚拟机或物理机。
* Cluster（集群）: 一组与因特网通过隔离的节点，他们是是Kubernetes管理的主要计算资源。
* Edge router（边界路由器）: 为您的集群强制执行防火墙策略的路由器。这可以是由云提供商管理的网关，也可以是物理硬件。
* Cluster network（集群网络）: 根据Kubernetes网络模型，促进集群内通信的一组逻辑上的或物理上的链路。
* Service（服务）: 一种Kubernetes服务，它使用标签选择器标识一组pod。除非另有说明，否则假定服务只有在集群网络中可路由的虚拟ip。

## 什么是ingress

ingress（在kubernetes v1.1时添加）暴露从集群外到集群内服务的`HTTP`或`HTTPS`路由。定义在`ingress`资源上的规则控制流量的路由。

```text
    internet
        |
   [ Ingress ]
   --|-----|--
   [ Services ]
```

一个`ingress`可以配置用于提供外部可访问的服务url、负载均衡流量、SSL终端和提供虚拟主机名配置。`ingress controller`负责实现（通常使用负载均衡器(loadbalancer)）入口（ingress）。但是它也可以配置你的边缘路由器或额外的前端来帮助处理流量。  
`ingress`不暴露任何端口或协议。将HTTP和HTTPS之外的服务公开到因特网通常使用类型是NodePort或loadbalance的service。

## ingress 资源

一个最小的ingress示例：

```yaml
apiVersion: networking.k8s.io/v1beta1
kind: Ingress
metadata:
  name: test-ingress
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
spec:
  rules:
  - http:
      paths:
      - path: /testpath
        backend:
          serviceName: test
          servicePort: 80
```

和其他的kubernetes资源一样，ingress需要`apiVersion`、`kind`和`metadata`字段。  
Ingress spec包含配置负载均衡器或代理服务器所需的所有信息。最重要的是，它包含一个针对所有传入请求匹配的规则列表。ingress资源只支持用于指导HTTP通信的规则

### ingress 规则

每一个HTTP规则包含以下信息：

* 一个可选的`host`。在本例中没有`host`,因此，该规则适用于通过指定的IP地址进行的所有入站HTTP通信。如果提供一个`host`(例如，foo.bar.com)，这个规则是适用于这一个`host`
* 一个`paths`（例如 /testpath）的列表。每一个`path`都有与之关联的`serviceName`和`servicePort`,在负载均衡器将流量导向所引用的服务之前，主机和路径必须匹配传入请求的内容
* 后端是服务和端口名称的组合。对与规则的主机和路径匹配的入口的HTTP(和HTTPS)请求将发送到列出的后端。

默认后端通常配置在一个Ingress控制器中，该控制器将服务于任何与规范中的路径不匹配的请求。（404页面）

### 默认后端

没有规则的ingress把所有的流量都转发到一个默认后端。默认后端通常是Ingress控制器的一个配置选项，并没有在Ingress资源中指定。  
如果没有任何主机或路径匹配Ingress对象中的HTTP请求，则流量将路由到默认后端。

## ingress的类型

### 单service的ingress

现有的Kubernetes概念允许您公开单个服务。您还可以通过指定一个没有规则的默认后端来对一个入口执行此操作。

```yaml
apiVersion: networking.k8s.io/v1beta1
kind: Ingress
metadata:
  name: test-ingress
spec:
  backend:
    serviceName: testsvc
    servicePort: 80
```

通过`kubectl apply -f <文件名>`创建后，你可以看到：

```shell
kubectl get ingress test-ingress
```

```text
NAME           HOSTS     ADDRESS           PORTS     AGE
test-ingress   *         107.178.254.228   80        59s
```

其中107.178.254.228是入口控制器为满足该入口而分配的IP。

### 简单的扇出

根据所请求的HTTP URI，扇出配置将流量从单个IP地址路由到多个服务。一个入口允许您将负载平衡器的数量保持到最小。例如，设置如下:

```text
foo.bar.com -> 178.91.123.132 -> / foo    service1:4200
                                 / bar    service2:8080
```

定义的ingress如下：

```yaml
apiVersion: networking.k8s.io/v1beta1
kind: Ingress
metadata:
  name: simple-fanout-example
  annotations:
    nginx.ingress.kubernetes.io/rewrite-target: /
spec:
  rules:
  - host: foo.bar.com
    http:
      paths:
      - path: /foo
        backend:
          serviceName: service1
          servicePort: 4200
      - path: /bar
        backend:
          serviceName: service2
          servicePort: 8080
```

通过`kubectl apply -f <文件名>`创建后：

```shell
kubectl describe ingress simple-fanout-example
```

```yaml
Name:             simple-fanout-example
Namespace:        default
Address:          178.91.123.132
Default backend:  default-http-backend:80 (10.8.2.3:8080)
Rules:
  Host         Path  Backends
  ----         ----  --------
  foo.bar.com
               /foo   service1:4200 (10.8.0.90:4200)
               /bar   service2:8080 (10.8.0.91:8080)
Annotations:
  nginx.ingress.kubernetes.io/rewrite-target:  /
Events:
  Type     Reason  Age                From                     Message
  ----     ------  ----               ----                     -------
  Normal   ADD     22s                loadbalancer-controller  default/test
```

只要服务(s1, s2)存在，Ingress控制器提供一个满足Ingress的特定于实现的负载均衡器。当它这样做时，您可以在address字段中看到负载均衡器的地址。

### 基于名称的虚拟主机

基于名称的虚拟主机支持将HTTP流量路由到同一IP地址的多个主机名。

```text
foo.bar.com --|                 |-> foo.bar.com s1:80
              | 178.91.123.132  |
bar.foo.com --|                 |-> bar.foo.com s2:80
```

下面的ingress告诉后台负载均衡器根据主机头路由请求。

```yaml
apiVersion: networking.k8s.io/v1beta1
kind: Ingress
metadata:
  name: name-virtual-host-ingress
spec:
  rules:
  - host: foo.bar.com
    http:
      paths:
      - backend:
          serviceName: service1
          servicePort: 80
  - host: bar.foo.com
    http:
      paths:
      - backend:
          serviceName: service2
          servicePort: 80
```

如果您创建一个没有在规则中定义任何主机的Ingress资源，那么可以匹配到Ingress控制器IP地址的任何web流量，而不需要基于名称的虚拟主机。例如，下面的Ingress资源将把first.bar.com请求的流量路由到service1, second.foo.com路由到service2，将任何没有在request中定义主机名(即没有显示请求头)的流量路由到service3。

```yaml
apiVersion: networking.k8s.io/v1beta1
kind: Ingress
metadata:
  name: name-virtual-host-ingress
spec:
  rules:
  - host: first.bar.com
    http:
      paths:
      - backend:
          serviceName: service1
          servicePort: 80
  - host: second.foo.com
    http:
      paths:
      - backend:
          serviceName: service2
          servicePort: 80
  - http:
      paths:
      - backend:
          serviceName: service3
          servicePort: 80
```

### TLS

您可以通过指定包含TLS私钥和证书的秘密来保护ingress。目前，入口只支持一个TLS端口443，并假设TLS终端。如果一个入口中的TLS配置部分指定了不同的主机，那么它们将根据通过SNI TLS扩展指定的主机名在同一个端口上进行多路复用(前提是入口控制器支持SNI)。TLS密钥必须包含名为TLS的密钥。crt和tls。包含用于TLS的证书和私钥的密钥，例如:

```yaml
apiVersion: v1
kind: Secret
metadata:
  name: testsecret-tls
  namespace: default
data:
  tls.crt: base64 encoded cert
  tls.key: base64 encoded key
type: kubernetes.io/tls
```
在一个Ingress中引用这个`secret`将告诉Ingress控制器使用TLS保护从客户机到负载均衡器的通道。您需要确保您创建的TLS secret来自一个包含sslexample.foo.com CN的证书。

```yaml
apiVersion: networking.k8s.io/v1beta1
kind: Ingress
metadata:
  name: tls-example-ingress
spec:
  tls:
  - hosts:
    - sslexample.foo.com
    secretName: testsecret-tls
  rules:
    - host: sslexample.foo.com
      http:
        paths:
        - path: /
          backend:
            serviceName: service1
            servicePort: 80
```

### Loadbalancing

一个ingress controller 通过一些应用于所有入口的负载平衡策略设置来引导，例如负载平衡算法、后端权重方案等。更高级的负载平衡概念(例如持久会话、动态权重)还没有通过ingress公开。同样值得注意的是，尽管健康检查不是直接通过入口暴露的，但是在Kubernetes中也存在类似的概念，比如就绪探测，它允许您实现相同的最终结果。