# kubernetes 使用ceph

## 下载[external storage](https://github.com/kubernetes-incubator/external-storage)项目

```shell
git clone https://github.com/kubernetes-incubator/external-storage.git
```

## kubernetes 使用 cephfs

进入cephfs所在的目录

```shell
cd external-storage/ceph/cephfs/
ls
```

此目录下应该存在如下文件：

```shell
cephfs_provisioner  cephfs-provisioner.go  CHANGELOG.md  deploy  Dockerfile  Dockerfile.release  example  local-start.sh  Makefile  OWNERS  README.md
```

其中`deploy`文件夹下用于部署`provisioner`,`example`文件夹中提供了使用cephfs的样例

### 部署cephfs 的provisioner


1. 使用rbac的方式部署。修改deployment文件

```shell
cd deploy/rbac
vi deployment.yaml
```

添加 `spec.template.spec.hostNetwork`和`spec.template.spec.tolerations`,修改`spec.template.spec.containers.image`:

```yaml
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: cephfs-provisioner
  namespace: cephfs
spec:
  replicas: 1
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: cephfs-provisioner
    spec:
      hostNetwork: true
      tolerations:
      - key: CriticalAddonsOnly
        operator: Exists
      - effect: NoSchedule
        key: node-role.kubernetes.io/master
      containers:
      - name: cephfs-provisioner
        image: "s7799653/cephfs-provisioner:latest"
        env:
        - name: PROVISIONER_NAME
          value: ceph.com/cephfs
        - name: PROVISIONER_SECRET_NAMESPACE
          value: cephfs
        command:
        - "/usr/local/bin/cephfs-provisioner"
        args:
        - "-id=cephfs-provisioner-1"
      serviceAccount: cephfs-provisioner
```

2. 创建cephfs命名空间

```shell
kubectl create ns cephfs
```

3. 部署provisioner到kubernetes

```shell
kubectl apply -f ./
```

### 在kubernetes中使用cephfs

1. 在`example`文件夹下新建一个`secret.yaml`，并创建`secret`,内容如下

```yaml
apiVersion: v1
kind: Secret
type: "kubernetes.io/rbd"
metadata:
  name: ceph-secret
data:
  #ceph auth get-key client.admin | base64
  key: ************
```



创建

```shell
kubectl apply -f example/secret.yaml -n cephfs
```


2. 修改`example.class.yaml`,并创建`StorageClass`  

`parameters.monitors`修改为`ceph`的mon节点ip，多个mon使用逗号(,)分割.  
`adminId`改为`admin`  
`adminSecretName`改为`ceph-secret`  
`adminSecretNamespace`改为`cephfs`  

```yaml
kind: StorageClass
apiVersion: storage.k8s.io/v1
metadata:
  name: cephfs
provisioner: ceph.com/cephfs
parameters:
    monitors: 10.10.7.52:6789
    adminId: admin
    adminSecretName: ceph-secret
    adminSecretNamespace: "cephfs"
    claimRoot: /pvc-volumes
```

创建

```shell
kubectl apply -f example/class.yaml -n cephfs
```


3. 创建测试的pvc和pod

修改`example/claim.yaml`中的`metadata.name`为`claim1-cephfs`,修改`example/test-pod.yaml`中的`spec.containers.image`为`busybox:1.24`以及`spec.volumes[0].persistentVolumeClaim.claimName`为`claim1-cephfs`

**example/claim.yaml**

```yaml
kind: PersistentVolumeClaim
apiVersion: v1
metadata:
  name: claim1-cephfs
spec:
  storageClassName: cephfs
  accessModes:
    - ReadWriteMany
  resources:
    requests:
      storage: 1Gi
```

**example/test-pod.yaml**

```yaml
kind: Pod
apiVersion: v1
metadata:
  name: test-pod
spec:
  containers:
  - name: test-pod
    image: busybox:1.24
    command:
      - "/bin/sh"
    args:
      - "-c"
      - "touch /mnt/SUCCESS && exit 0 || exit 1"
    volumeMounts:
      - name: pvc
        mountPath: "/mnt"
  restartPolicy: "Never"
  volumes:
    - name: pvc
      persistentVolumeClaim:
        claimName: claim1-cephfs
```

部署

```shell
kubectl apply -f example/claim.yaml
kubectl apply -f example/test-pod.yaml
kubectl get po
kubectl get pvc
```

如果看到`pod.test-pod`的`STATUS`是`Completed`以及`pvc.claim1-cephfs`的`STATUS`是`Bound`就表明部署成功了。  
正常之后即可删除刚刚创建的资源

```shell
kubectl delete -f example/claim.yaml
kubectl delete -f example/test-pod.yaml
```

## kubernetes 使用 ceph-rbd

进入rbd所在的目录

```shell
cd external-storage/ceph/rbd/
ls
```

此目录下应该存在如下文件：

```shell
CHANGELOG.md  cmd  deploy  Dockerfile  Dockerfile.release  examples  local-start.sh  Makefile  OWNERS  pkg  README.md
```

其中`deploy`文件夹下用于部署`provisioner`,`example`文件夹中提供了使用ceph-rbd的样例

### 部署ceph-rbd的provisioner

1. 使用rbac的方式部署。修改部分文件

```shell
cd deploy/rbac
```
**deployment.yaml**:  

添加 `spec.template.spec.hostNetwork`和`spec.template.spec.tolerations`以及挂载本地`/etc/ceph`,修改`spec.template.spec.containers.image`:

```yaml
apiVersion: extensions/v1beta1
kind: Deployment
metadata:
  name: rbd-provisioner
spec:
  replicas: 1
  strategy:
    type: Recreate
  template:
    metadata:
      labels:
        app: rbd-provisioner
    spec:
      hostNetwork: true
      tolerations:
      - key: CriticalAddonsOnly
        operator: Exists
      - effect: NoSchedule
        key: node-role.kubernetes.io/master
      containers:
      - name: rbd-provisioner
        image: "s7799653/rbd-provisioner:latest"
        env:
        - name: PROVISIONER_NAME
          value: ceph.com/rbd
        volumeMounts:
        - mountPath: /etc/ceph
          name: ceph-config
      serviceAccount: rbd-provisioner
      volumes:
      - name: ceph-config
        hostPath:
          path: /etc/ceph/
```

**clusterrolebinding.yaml**:  

修改`subjects.namespace`为`cephfs`

```yaml
kind: ClusterRoleBinding
apiVersion: rbac.authorization.k8s.io/v1
metadata:
  name: rbd-provisioner
subjects:
  - kind: ServiceAccount
    name: rbd-provisioner
    namespace: cephfs
roleRef:
  kind: ClusterRole
  name: rbd-provisioner
  apiGroup: rbac.authorization.k8s.io
```

**rolebinding.yaml**:

修改`subjects.namespace`为`cephfs`:

```yaml
apiVersion: rbac.authorization.k8s.io/v1
kind: RoleBinding
metadata:
  name: rbd-provisioner
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: Role
  name: rbd-provisioner
subjects:
- kind: ServiceAccount
  name: rbd-provisioner
  namespace: cephfs
```

2. 创建cephfs的namespace(如果有可以略过这一步，命名为cephfs是为了把关于ceph的都放到同一个namespace下。)

```shell
kubectl create ns cephfs
```

3. 部署provisioner到kubernetes

```shell
kubectl apply -f ./ -n cephfs
```

### 在kubernetes中使用ceph-rbd

1. 修改`examples`中的`secrets.yaml`，并创建secret

**如果进行过cephfs的配置，此步骤可以省略**  
**这里是只用ceph admin一个用户的配置。**  

```yaml
apiVersion: v1
kind: Secret
type: "kubernetes.io/rbd"
metadata:
  name: ceph-secret
data:
  #ceph auth get-key client.admin | base64
  key: ************
```

创建

```shell
kubectl apply -f examples/secrets.yaml -n cephfs
```

2. 修改`examples`中的`class.yaml`,并创建`StorageClass`

**这里是只用ceph admin一个用户的配置**  

`metadata.name`修改为`ceph-rbd`
`parameters.monitors`修改为`ceph`的mon节点ip，多个mon使用逗号(,)分割.  
`adminId`改为`admin`  
`adminSecretName`改为`ceph-secret`  
`adminSecretNamespace`改为`cephfs`  
`pool`改为`rbd`（根据实际情况，通过`ceph osd pool ls`查看ceph有那些pool，通过`ceph osd pool create rbd 128&&ceph osd pool application enable rbd rbd`创建rbd pool）  
`userId`改为`admin`  
`userSecretName`改为`ceph-secret`  
`userSecretNamespace`改为`cephfs`  

```yaml
kind: StorageClass
apiVersion: storage.k8s.io/v1
metadata:
  name: ceph-rbd
provisioner: ceph.com/rbd
parameters:
  monitors: 10.10.7.52:6789
  pool: rbd
  adminId: admin
  adminSecretNamespace: cephfs
  adminSecretName: ceph-secret
  userId: admin
  userSecretNamespace: cephfs
  userSecretName: ceph-secret
  imageFormat: "2"
  imageFeatures: layering
```

创建

```shell
kubectl apply -f examples/class.yaml -n cephfs
```

3. 创建测试的pvc和pod

修改`examples/claim.yaml`中的`metadata.name`为`claim1-rbd`以及`spec.storageClassName`为`ceph-rbd`,修改`examples/test-pod.yaml`中的`spec.containers.image`为`busybox:1.24`以及`spec.volumes[0].persistentVolumeClaim.claimName`为`claim1-rbd`

**examples/claim.yaml**

```yaml
kind: PersistentVolumeClaim
apiVersion: v1
metadata:
  name: claim1-rbd
spec:
  accessModes:
    - ReadWriteOnce
  storageClassName: ceph-rbd
  resources:
    requests:
      storage: 1Gi
```

**examples/test-pod.yaml**

```yaml
kind: Pod
apiVersion: v1
metadata:
  name: test-pod
spec:
  containers:
  - name: test-pod
    image: busybox:1.24
    command:
    - "/bin/sh"
    args:
    - "-c"
    - "touch /mnt/SUCCESS && exit 0 || exit 1"
    volumeMounts:
    - name: pvc
      mountPath: "/mnt"
  restartPolicy: "Never"
  volumes:
  - name: pvc
    persistentVolumeClaim:
      claimName: claim1-rbd
```

部署

```shell
kubectl apply -f examples/claim.yaml
kubectl apply -f examples/test-pod.yaml
kubectl get po
kubectl get pvc
```

如果看到`pod.test-pod`的`STATUS`是`Completed`以及`pvc.claim1-rbd`的`STATUS`是`Bound`就表明部署成功了。  
正常之后即可删除刚刚创建的资源

```shell
kubectl delete -f examples/claim.yaml
kubectl delete -f examples/test-pod.yaml
```