# kubeadm install k8s_v1.14.1 and ceph luminous

## 综述

在 ubuntu 18.04 中使用 kubeadm 安装 k8s v1.14.1版本。k8s 集群使用 ceph 集群作为存储。

## 安装

以下步骤均在master节点操作即可：  

1. 安装ansible

```shell
apt install -y ansible
```

2. 修改 hosts ，把节点的 hostname 以及对应的 ip 添加到 hosts  
3. 建立ssh 互信，免密登陆。  
4. 新建一个ansible工作目录以及host文件

```shell
mkdir ansible
cd ansible
vi host
```

host文件中写入所有节点的ip，格式如下:  

```yaml
[all]
10.10.7.52
10.10.7.53
10.10.7.54
10.10.7.55
```
5. 获取ansible文件，[k8s-pre](../resources/playbook-k8s.yml),[ceph](../resources/ceph-ansible.tgz)  
6. 执行playbook-k8s

```shell
ansible-playbook -i host playbook-k8s.yml
```

7. 安装ceph

解压ceph ansible包

```shell
tar zxvf ceph-ansible.tgz
```

修改`hosts.cfg`：在对应的角色下添加相应节点的`hostname`。  
修改`group_vars\all.yml`:  

* monitor_interface：需要配置为实际环境mon节点选用的网卡名
* public_network：需要配置monitor_interface网卡的地址段
* cluster_network：需要配置OSD同步的网卡地址段

修改`group_vars\osds.yml`:devices需要修改为实际环境的osd节点选用的硬盘名（可同时配置多个）

```yaml
devices:
  - /dev/sdb
  - /dev/sdc
```

使用ansible部署

```shell
ansible-playbook -i hosts.cfg site.yml
```

创建OSD pool：

```shell
ceph osd pool create rbd 128
ceph osd pool application enable rbd rbd
```

查看集群状态

```shell
cpeh -s
```

如果`health`为`warnning`状态，需要根据提示进行修改，直到状态为`OK`

8. kubeadm安装k8s

```shell
kubeadm init --pod-network-cidr=192.168.0.0/16
```

9. 配置kubectl

```shell
mkdir -p $HOME/.kube
sudo cp -i /etc/kubernetes/admin.conf $HOME/.kube/config
sudo chown $(id -u):$(id -g) $HOME/.kube/config
```

10. 安装calico

```shell
kubectl apply -f \
https://docs.projectcalico.org/v3.6/getting-started/kubernetes/installation/hosted/kubernetes-datastore/calico-networking/1.7/calico.yaml
```

11. 添加节点

`kubeadm init`结束之后会给一个添加节点的命令。改也可以通过如下命令获取:

```shell
kubeadm token create --print-join-command
```

在所有的**work节点**执行该添加节点的命令(`kubeadm join ...`)

## 卸载

卸载k8s的命令为`kubeadm reset -f`,需要在**每一个节点**执行。

## 结语

在这篇文章中，很多配置都写在了`playbook-k8s.yml`脚本里。具体如何不通过ansible，手动的配置可以参考[kubeadm部署kubernetes 1.13](http://songjxin.cn/?p=557)。虽然安装的版本不同，但是大体步骤是一致的。  
文章到这里只是安装了ceph集群和k8s集群，至于k8s如何调用ceph作为存储还需要进一步的配置。