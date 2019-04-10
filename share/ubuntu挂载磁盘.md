# ubuntu 挂载新磁盘

## 查看空闲磁盘

```bash
lsblk
NAME   MAJ:MIN RM   SIZE RO TYPE MOUNTPOINT
sda      8:0    0 111.3G  0 disk
├─sda1   8:1    0     1M  0 part
└─sda2   8:2    0 111.3G  0 part /
sdb      8:16   0 111.3G  0 disk
sdc      8:32   0   1.8T  0 disk
```

可以看到 sdc是空闲的。

## 创建分区

```bash
fdisk sdc
```

如果把整个盘作为新的分区的话。输入`n` 一直回车即可。最后输入`w`保存并写入。

## 格式化分区

```bash
mkfs.ext4 /dev/sdc1
```

## 挂载到指定目录

临时挂载：  

```bash
mount /dev/sdc1 /var/lib/docker
```

永久挂载：  

```bash
echo "/dev/sdc1 /var/lib/docker ext4 rw 0 0 " >> /etc/fstab
```