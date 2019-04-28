# Tips Collection

## 第1周

蓝灯自定义白名单（公司内网域名不使用代理服务器）：设置 -> 网络和Internet -> 代理 -> 手动设置代理  
> 请勿对以下条目开头的地址使用代理服务器。  

填入白名单的地址即可

## 第2周

### kvm安装虚拟机时提示权限问题

ERROR    internal error: qemu unexpectedly closed the monitor: 2019-03-25T03:06:20.722368Z qemu-kvm: -drive file=/root/CentOS-7-x86_64-Minimal-1804.iso,format=raw,if=none,id=drive-ide0-0-0,readonly=on: could not open disk image /root/CentOS-7-x86_64-Minimal-1804.iso: Could not open '/root/CentOS-7-x86_64-Minimal-1804.iso': Permission denied

把 iso镜像移动到/var/lib/libvirt/images/

### centos 18.04修改主机名

`vi /etc/cloud/cloud.cfg`将`preserve_hostname`修改为true

### ceph auth认证失败

```bash
ceph auth ls
Error EACCES: access denied
```

使用权限最大的账户（mon.）登陆,然后给admin赋权

```bash
ceph auth caps client.admin osd 'allow *' mds 'allow ' mon 'allow *' mgr 'allow *' --name mon. --keyring /var/lib/ceph/mon/ceph/keyring
```

### ceph HEALTH_WARN 2 pools have many more objects per pg than average

```shell
ceph health detail
HEALTH_WARN 2 pools have many more objects per pg than average
MANY_OBJECTS_PER_PG 2 pools have many more objects per pg than average
    pool cephfs_metadata objects per pg (27686) is more than 21.1021 times cluster average (1312)
    pool cephfs_data objects per pg (59816) is more than 45.5915 times cluster average (1312)
```

增大pg_num的数量

```shell
ceph osd pool set cephfs_data pg_num 56
ceph osd pool set cephfs_data pgp_num 56
```

## 第3周

### python3 的 除法

在python3中 `3/2`的结果是1.5（float）。如果想要像其他语言（如C++）一样得到一个整数。需要用两个除号如`3//2`

## 第4周

### 批量杀死进程

ps -ef|grep LOCAL=NO|grep -v grep|cut -c 9-15|xargs kill -9

### 设置别名

alias kubectl=microk8s.kubectl

## 第5周

### rsync同步文件失败 chdir failed

可能是因为开着SELINUX

```shell
getsebool -a | grep rsync
setsebool -P rsync_full_access on
```

### Gitlab 返回 “No Repository” 但是 git_data文件夹里存在该仓库

```shell
sudo gitlab-rake cache:clear
```
参考[Gitlab returns “No Repository” even though the repository exists in the git_data_dirs path(s)](https://stackoverflow.com/questions/51179946/gitlab-returns-no-repository-even-though-the-repository-exists-in-the-git-data?tdsourcetag=s_pctim_aiomsg)
