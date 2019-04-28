# 在kubernetes中安装helm-v2.13.1

## 准备工作

1. 下载[helm的二进制包](https://github.com/helm/helm/releases)

```shell
wget https://storage.googleapis.com/kubernetes-helm/helm-v2.13.1-linux-amd64.tar.gz
```

2. 解压

```shell
tar zxvf helm-v2.13.1-linux-amd64.tar.gz
```

3. 把helm添加到path

```shell
cp linux-amd64/helm /usr/bin/
```

`helm -h` 能够打印出`helm`命令的帮助信息

4. 创建一个helm的文件夹，用于存放安装helm的信息

```shell
mkdir helm
cd helm
```

## 创建helm的rbac

1. 创建rbac-config.yaml，内容如下:


```shell
apiVersion: v1
kind: ServiceAccount
metadata:
  name: tiller
  namespace: kube-system
---
apiVersion: rbac.authorization.k8s.io/v1
kind: ClusterRoleBinding
metadata:
  name: tiller
roleRef:
  apiGroup: rbac.authorization.k8s.io
  kind: ClusterRole
  name: cluster-admin
subjects:
  - kind: ServiceAccount
    name: tiller
    namespace: kube-system
```

2. 在集群中创建helm的rbac信息。

```shell
kubectl apply -f rbac-config.yaml
```

## 创建helm的ssl证书

1. 生成证书颁发机构(Certificate Authority)

```shell
openssl genrsa -out ./ca.key.pem 4096
openssl req -key ca.key.pem -new -x509 -days 7300 -sha256 -out ca.cert.pem -extensions v3_ca
```

2. 生成证书(Certificates)

生成两个证书，一个用于tiller,另一个用于user  

2-1. 生成tiller key和证书，并使用CA进行签名(根据实际情况调整days天数)

```shell
openssl genrsa -out ./tiller.key.pem 4096
openssl req -key tiller.key.pem -new -sha256 -out tiller.csr.pem
openssl x509 -req -CA ca.cert.pem -CAkey ca.key.pem -CAcreateserial -in tiller.csr.pem -out tiller.cert.pem -days 365
```

2-2. 生成client key和证书，并使用CA进行签名(根据实际情况调整days天数)

```shell
openssl genrsa -out ./helm.key.pem 4096
openssl req -key helm.key.pem -new -sha256 -out helm.csr.pem
openssl x509 -req -CA ca.cert.pem -CAkey ca.key.pem -CAcreateserial -in helm.csr.pem -out helm.cert.pem  -days 365
```

此时，该文件夹下应该有以下文件

```shell
# CA相关的文件
ca.cert.pem
ca.key.pem
# Helm client 相关的文件。
helm.cert.pem
helm.key.pem
# helm tiller相关的文件.
tiller.cert.pem
tiller.key.pem
# helm的rbac配置文件
rbac-config.yaml
```

3. 创建自定义的tiller安装。

```shell
helm init --dry-run --debug --tiller-tls --tiller-tls-cert ./tiller.cert.pem --tiller-tls-key ./tiller.key.pem --tiller-tls-verify --tls-ca-cert ca.cert.pem --service-account tiller >> helm.yaml
```

此时，`ls`应该能够看到一个名为`helm.yaml`的文件，修改这个文件的`Deployment.spec.template.spec.image`为`s7799653/kubernetes-helm-tiller:v2.13.1`(第34行)

部署到集群中:

```shell
kubectl apply -f helm.yaml
```

4. 配置helm client

```shell
cp ca.cert.pem $(helm home)/ca.pem
cp helm.cert.pem $(helm home)/cert.pem
cp helm.key.pem $(helm home)/key.pem
```

5. 验证

查看pod是否正常

```shell
kubectl get deploy -l app=helm -n kube-system
# 正常输出是这样的。
NAME            READY   UP-TO-DATE   AVAILABLE   AGE
tiller-deploy   1/1     1            1           5h1m
```

运行helm命令是否正常

```shell
helm version --tls
# 正常的状态是这样的
Client: &version.Version{SemVer:"v2.13.1", GitCommit:"618447cbf203d147601b4b9bd7f8c37a5d39fbb4", GitTreeState:"clean"}
Server: &version.Version{SemVer:"v2.13.1", GitCommit:"618447cbf203d147601b4b9bd7f8c37a5d39fbb4", GitTreeState:"clean"}

# 如果不加 --tls
helm ls
# 输出是这样的（会卡一会）
Error: transport is closing
```