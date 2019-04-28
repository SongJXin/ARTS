# 下载 k8s.gcr.io等仓库的镜像

## 方式一，通过github和dockerhub

在github上创建一个仓库，编写`Dockerfile`,内容就是`FROM k8s.gcr.io/<image-name>:<tag>`。  
在docker hub上创建一个仓库，与github关联，并创建自动构建。  

## 方式二，自己创建虚机，科学上网（蓝灯）。

1. 创建ubuntu 18.04 的**桌面版**虚拟机
2. 下载[蓝灯](https://github.com/getlantern/lantern)最新版
3. 从**桌面**安装蓝灯。`dpkg -i lantern-installer-64-bit.deb`
4. 从**桌面**启动蓝灯。激活专业版。从设置中查看端口,如(37269)
5. 安装docker.
6. `service docker status`查看`docker`的`service`地址如(`/lib/systemd/system/docker.service`)
7. 为[docker设置代理](https://docs.docker.com/config/daemon/systemd/#httphttps-proxy)

创建`/lib/systemd/system/docker.service.d/http-proxy.conf`。内容为：

```yaml
[Service]
Environment="HTTP_PROXY=http://127.0.0.1:37269/"
Environment="HTTPS_PROXY=http://127.0.0.1:37269/"
```

**注意，https_proxy后面也是http，没有s**  
  
重启docker

```yaml
sudo systemctl daemon-reload
sudo systemctl restart docker
```

8. 下载镜像试试`docker pull k8s.gcr.io/pause:3.1`