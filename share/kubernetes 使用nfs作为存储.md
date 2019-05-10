# kubernetes 使用 nfs作为存储

## 安装nfs

### 服务端安装

```shell
yum install -y nfs-utils rpcbind
mkdir /nfsdata
echo "/nfsdata <client-ip>(rw,sync,root_squash)" >> /etc/exports
service firewalld stop
systemctl enable rpcbind
systemctl start rpcbind
systemctl enable nfs
systemctl start nfs
```

### 客户端安装

在k8s的每个节点上执行:

```shell
yum install -y nfs-utils rpcbind
systemctl enable rpcbind
systemctl start rpcbind
```

检查时候可以连接nfs

```shell
showmount -e <server-ip>
mount -t nfs <server-ip>:/nfsdata/ /<Existing path>/
```

## k8s中安装nfs的provisioner

下载k8s nfs provisioner的[helmchart](../resources/nfs-client-provisioner-1.2.0.tgz)

```shell
kubectl create ns nfs-provisioner
helm install nfs-client-provisioner-1.2.0.tgz --set nfs.server=<servver-ip> --set nfs.path=/nfsdata --set image.repository=s7799653/nfs-client-provisioner --set image.tag=v3.1.0-k8s1.11 --namespace nfs-provisioner --tls
```

## 测试

创建pvc文件 claim.yaml

```yaml
kind: PersistentVolumeClaim
apiVersion: v1
metadata:
  name: claim1
spec:
  storageClassName: nfs-client
  accessModes:
    - ReadWriteMany
  resources:
    requests:
      storage: 1Gi
```

```shell
kubectl apply -f claim.yaml
kubectl get pvc # pvc STATUS Bound
```

创建pod文件 pod.yaml

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
        claimName: claim1
```

```shell
kubectl apply -f pod.yaml
kubectl get pod # pod STATUS Completed
```

清理测试数据

```shell
kubectl delete po test-pod
kubectl delete pvc claim1
```