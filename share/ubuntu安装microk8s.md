# ubuntu 安装 microk8s

## 准备工作

准备一台ubuntu的机器，配置如下：

* OS: ubuntu 18.04
* docker: 18.06.1
* cpu、内存：4core 4G
* 硬盘： 100G
* 能连通docker hub（不必翻墙）

CPU 内存过低可能带不动。4核4G感觉有点卡。硬盘低了可能会导致docker image持续GC，造成不必要的性能浪费。

## 安装

```shell
snap install microk8s --classic
```

这样就安装完了。。。下面对这个集群做一下改动，方便使用。  
使用`/snap/bin/microk8s.status`查看集群状态。

## 配置

### 配置 microk8s命令

1. 把`/snap/bin`加入 `PATH`：

```shell
export PATH=$PATH:/snap/bin #临时写入
echo "export PATH=$PATH:/snap/bin" >> ~/.bashrc #永久写入
```

2. 设置`kubectl`别名：

```shell
snap alias microk8s.kubectl kubectl
```

3. 修改pod的sandbox

pod的sandbox 默认是 `k8s.gcr.io/pause:3.1`，这个镜像是无法获取的。需要将sandbox修改为国内可以获取的镜像。  

* 修改`/var/snap/microk8s/current/args/kubelet`。 添加`--pod-infra-container-image=s7799653/pause:3.1`
* 修改`/var/snap/microk8s/current/args/containerd-template.toml`的`plugins -> plugins.cri -> sandbox_image`为`s7799653/pause:3.1`
* 重启服务 `microk8s.stop`,`microk8s.start`

4. 启用 dns和 dashboard

```shell
microk8s.enable dns dashboard
```

由于这些组件用到的镜像是墙外的。需要修改一下镜像，使用墙内的。  
这里提供部分镜像。  

墙内|墙外
---|---
s7799653/heapster-amd64:v1.5.2|k8s.gcr.io/heapster-amd64:v1.5.2
s7799653/heapster-influxdb-amd64:v1.3.3|k8s.gcr.io/heapster-influxdb-amd64:v1.3.3
s7799653/k8s-dns-sidecar-amd64:1.14.7|gcr.io/google_containers/k8s-dns-sidecar-amd64:1.14.7
s7799653/heapster-grafana-amd64:v4.4.3|k8s.gcr.io/heapster-grafana-amd64:v4.4.3
s7799653/kubernetes-dashboard-amd64:v1.8.3|k8s.gcr.io/kubernetes-dashboard-amd64:v1.8.3
s7799653/k8s-dns-kube-dns-amd64:1.14.7|gcr.io/google_containers/k8s-dns-kube-dns-amd64:1.14.7
s7799653/fluentd-elasticsearch:v2.2.0|k8s.gcr.io/fluentd-elasticsearch:v2.2.0
s7799653/k8s-dns-dnsmasq-nanny-amd64:1.14.7|gcr.io/google_containers/k8s-dns-dnsmasq-nanny-amd64:1.14.7

分享一个无需翻墙即可获取墙外镜像的小技巧。  
利用docker hub的自动构建。从github获取dockerfile来构建镜像。  

## 使用

```shell
kubectl get po -n kube-system
```

如果所有pod都是`Running`状态，就证明这个集群可以使用了。

```shell
kubectl edit svc -n kube-system  kubernetes-dashboard
```

把`spec.type`修改为`NodePort`,在`spec.ports`中添加`nodePort: 30000`，保存之后就可以通过<https://ip:30000>访问界面了。