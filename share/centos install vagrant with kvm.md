# centos install vagrant with kvm

## 先检查一下是否支持KVM

```shell
cat /proc/cpuinfo | egrep 'vmx|svm'
flags           : fpu vme de pse tsc msr pae mce cx8 apic sep mtrr pge mca cmov pat pse36 clflush dts acpi mmx fxsr sse sse2 ss ht tm pbe syscall nx pdpe1gb rdtscp lm constant_tsc arch_perfmon pebs bts rep_good nopl xtopology nonstop_tsc aperfmperf eagerfpu pni pclmulqdq dtes64 ds_cpl vmx smx est tm2 ssse3 sdbg fma cx16 xtpr pdcm pcid dca sse4_1 sse4_2 x2apic movbe popcnt tsc_deadline_timer aes xsave avx f16c rdrand lahf_lm abm 3dnowprefetch epb cat_l3 cdp_l3 intel_ppin intel_pt tpr_shadow vnmi flexpriority ept vpid fsgsbase tsc_adjust bmi1 hle avx2 smep bmi2 erms invpcid rtm cqm rdt_a rdseed adx smap xsaveopt cqm_llc cqm_occup_llc cqm_mbm_total cqm_mbm_local dtherm ida arat pln pt
```

## 安装kvm等环境

```shell
yum install qemu libvirt libvirt-devel ruby-devel gcc qemu-kvm
```

## 安装vagrant

```shell
wget https://releases.hashicorp.com/vagrant/2.2.4/vagrant_2.2.4_x86_64.rpm
rpm -Uvh vagrant_2.2.4_x86_64.rpm
```

## 安装vagrant-libvirt插件

```shell
vagrant plugin install vagrant-libvirt
```

## 创建kvm

```shell
vagrant box add centos/7
mkdir centos7
cd centos7
vagrant init centos/7
vagrant up
```

## 进入kvm

```shell
vagrant ssh
```