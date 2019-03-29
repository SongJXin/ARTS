# kubernetes中搭建 jumpserver 跳板机

这里是 [jumpserver的官方文档](http://docs.jumpserver.org/zh/docs/)  
  
搭建过程主要分三步：

1. 创建mysql
2. 创建redis
3. 创建jumpserver

## 创建 mysql 和 redis

我都是通过helm创建的。这里过程就省略了。

## 创建jumpserver服务

### 创建pv/pvc

这里是通过`cephfs`的StorageClass创建的pvc，它会自动创建pv，考虑到可能需要多个pod进行负载均衡，所以pv/pvc的访问类型是RWX。

```yaml
apiVersion: v1
kind: PersistentVolumeClaim
metadata:
  name: jumpserver-media
  namespace: test
spec:
  accessModes:
  - ReadWriteMany
  resources:
    requests:
      storage: 50Gi
  storageClassName: cephfs
```

### 创建deployment

```yaml
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  labels:
    app: jumpserver
  name: jumpserver
  namespace: test
spec:
  replicas: 1
  selector:
    matchLabels:
      app: jumpserver
  strategy:
    rollingUpdate:
      maxSurge: 25%
      maxUnavailable: 25%
    type: RollingUpdate
  template:
    metadata:
      labels:
        app: jumpserver
    spec:
      containers:
      - env:
        - name: SECRET_KEY #生成方式： cat /dev/urandom | tr -dc A-Za-z0-9 head -c 50
          value: hP9WlzaLwGlgnc**********p38ZWJRGdTXoIAAvitl3GnAaeT 
        - name: BOOTSTRAP_TOKEN #生成方式： cat /dev/urandom | tr -dc A-Za-z0-9 | head -c 16
          value: nQyx******JQFu9K
        - name: DB_HOST  #MySQL地址
          value: jumpserver-mysql-mysqlha
        - name: DB_PORT  #mysql端口
          value: "3306"
        - name: DB_USER  #MySQL用户
          value: root
        - name: DB_PASSWORD #MySQL密码
          value: quJ****02yqV
        - name: DB_NAME #mysql数据库 数据库编码要求 uft8 创建语句：  create database jumpserver default charset 'utf8';
          value: jumpserver
        - name: REDIS_HOST #redis 地址
          value: jumpserver-redis-ibm-redis-ha-dev-master-svc
        - name: REDIS_PORT # redis 端口
          value: "6379"
        - name: REDIS_PASSWORD # redis密码。如果没有，可以不写
          value: "aaaaaaaaa"
        image: jumpserver/jms_all
        imagePullPolicy: IfNotPresent
        name: jumpserver
        ports:
        - containerPort: 2222 #用于ssh client端访问
          protocol: TCP
        - containerPort: 80 #用于web端访问
          protocol: TCP
        resources: {}
        terminationMessagePath: /dev/termination-log
        terminationMessagePolicy: File
        volumeMounts: #保存录像
        - mountPath: /opt/jumpserver/data/media
          name: jumpserver-media
      dnsPolicy: ClusterFirst
      restartPolicy: Always
      schedulerName: default-scheduler
      securityContext: {}
      volumes:
      - name: jumpserver-media
        persistentVolumeClaim:
          claimName: jumpserver-media
```

### 创建service

一共创建两个service：

* web端访问的，为了方便记忆，这里将service的类型设置为`ClusterIP` 然后创建ingress。如果要使用nodeport的方式，两个service可以合并为一个service。
* ssh端访问。使用nodeport类型的svc。

web端：

```yaml
apiVersion: v1
kind: Service
metadata:
  labels:
    app: jumpserver
  name: jumpserver
  namespace: test
spec:
  ports:
  - name: http
    port: 80
    protocol: TCP
    targetPort: 80
  selector:
    app: jumpserver
  sessionAffinity: ClientIP
  type: ClusterIP

```

ssh client端：

```yaml
apiVersion: v1
kind: Service
metadata:
  labels:
    app: jumpserver
  name: jumpserver-ssh
  namespace: test
spec:
  externalTrafficPolicy: Cluster
  ports:
  - name: ssh
    nodePort: 22222
    port: 2222
    protocol: TCP
    targetPort: 2222
  selector:
    app: jumpserver
  sessionAffinity: None
  type: NodePort
```

### 创建ingress

为了方便记忆访问端地址，减少主机port的占用，使用ingress的方式访问网页端：

```yaml
apiVersion: extensions/v1beta1
kind: Ingress
metadata:
  name: jumpserver
  namespace: test
spec:
  backend:
    serviceName: jumpserver
    servicePort: 80
  rules:
  - host: jumpserver.example.com
    http:
      paths:
      - backend:
          serviceName: jumpserver
          servicePort: 80
```

## 配置：

jumpserver的配置及使用在官方文档中已经很详细了。我说两点jumpserver配置AD域时的注意事项：

1. 用户OU部分。多个OU使用管道符`|`隔开，例如:ou=ou1,dc=example,dc=come|ou=ou2,dc=example,dc=come，如果不使用ou（如直接填写：dc=example,dc=come）或超过15000用户可能会存在异常（我不适用ou的时候用户超过了15000，出现了异常，不知道是因为ou的愿意还是用户过多的原因）
2. 用户过滤器填写：(sAMAccountName=%(user)s)
3. LDAP属性映射填写：{"username": "sAMAccountName", "name": "sn", "email": "mail"}，如果使用默认的，会因为username中出现中文而使用户登陆报500错误。