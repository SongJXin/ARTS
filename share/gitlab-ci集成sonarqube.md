# gitlab-ci 集成 sonarqube

项目demo在github上：https://github.com/mkyong/maven-examples/tree/master/maven-code-coverage

## 使用helm安装gitlab ci runner到k8s集群

下载[chart包](../resources/gitlab-runner-0.1.44.tgz)或者使用以下命令：

```shell
helm repo add gitlab  https://charts.gitlab.io/
helm search gitlab-runner
helm fetch gitlab/gitlab-runner
```

解压并修改部分参数:

```shell
tar zxvf gitlab-runner-0.1.44.tgz
cd gitlab-runner
vim values.yaml
```

* gitlabUrl gitlab的地址
* runnerRegistrationToken 从gitlab project -> Settings -> CI/CD Runners settings ->Specific Runners中获取
* rbac.create 设为true
* runners.tags 自定义设置 方便在pipeline中调用。
* runners.privileged 设为true为了使用docker in docker

### 安装

```shell
helm install ./
```

## sonarqube 配置

### 安装插件：

* GitLab
* GitLab Auth
* JaCoCo

### security

Permission Templates -> Default template  

将Creators 后面的权限全选（即 项目的创建者有项目的所有权限）  
将sonar-administrators后面的权限全选（即 sonar的admin有所有项目的所有权限）  
把sonar-users 的权限全部取消（每个人不能看到其他人的项目）  

### 配置

#### General

* Server base URL 配成自己的sonarqube的url。

#### GitLab

##### Authentication

* Enabled 设为true
* GitLab url 设为gitlab地址
* Application ID   来源  gitlab admin area -> Applications 新建 application Scopes选择read_user。Redirect URI填 sonarqube url + `/oauth2/callback/gitlab`
* Secret 同上

##### Reporting

Global template：  注意 sonarqubeurl 换成实际情况的url

``` xml
<#if qualityGate??>
<#if  qualityGate.status == "OK">  
    [ :heavy_check_mark: SonarQube analysis indicates that quality gate is  "PASS"  ](http://sonarqubeurl/sessions/init/gitlab?return_to=/dashboard?id=${projectId?split('/')[0]}-${projectId?split('/')[1]})
</#if>
<#if  qualityGate.status == "ERROR">  
   [ :heavy_multiplication_x: SonarQube analysis indicates that quality gate is  "FAILED"  ](http://sonarqubeurl/sessions/init/gitlab?return_to=/dashboard?id=${projectId?split('/')[0]}-${projectId?split('/')[1]})
</#if>  


<#list qualityGate.conditions() as condition>
<@c condition=condition/>

</#list>
</#if>
<#macro c condition>* ${condition.metricName} is <@s status=condition.status/>: Actual value ${condition.actual}<#if condition.status == WARN> is ${condition.symbol} ${condition.warning}</#if><#if condition.status == ERROR> is ${condition.symbol} ${condition.error}</#if></#macro>
<#macro s status><#if status == OK>passed<#elseif status == WARN>warning<#elseif status == ERROR>failed<#else>unknown</#if></#macro>
<#assign newIssueCount = issueCount() notReportedIssueCount = issueCount(false)>
<#assign hasInlineIssues = newIssueCount gt notReportedIssueCount extraIssuesTruncated = notReportedIssueCount gt maxGlobalIssues>
<#if newIssueCount == 0>
SonarQube analysis reported no issues.
<#else>
SonarQube analysis reported ${newIssueCount} issue<#if newIssueCount gt 1>s</#if>
    <#assign newIssuesBlocker = issueCount(BLOCKER) newIssuesCritical = issueCount(CRITICAL) newIssuesMajor = issueCount(MAJOR) newIssuesMinor = issueCount(MINOR) newIssuesInfo = issueCount(INFO)>
    <#if newIssuesBlocker gt 0>
* ${emojiSeverity(BLOCKER)} ${newIssuesBlocker} blocker
    </#if>
    <#if newIssuesCritical gt 0>
* ${emojiSeverity(CRITICAL)} ${newIssuesCritical} critical
    </#if>
    <#if newIssuesMajor gt 0>
* ${emojiSeverity(MAJOR)} ${newIssuesMajor} major
    </#if>
    <#if newIssuesMinor gt 0>
* ${emojiSeverity(MINOR)} ${newIssuesMinor} minor
    </#if>
    <#if newIssuesInfo gt 0>
* ${emojiSeverity(INFO)} ${newIssuesInfo} info
    </#if>
    <#if !disableIssuesInline && hasInlineIssues>

Watch the comments in this conversation to review them.
    </#if>
    <#if notReportedIssueCount gt 0>
        <#if !disableIssuesInline>
            <#if hasInlineIssues || extraIssuesTruncated>
                <#if notReportedIssueCount <= maxGlobalIssues>

#### ${notReportedIssueCount} extra issue<#if notReportedIssueCount gt 1>s</#if>
                <#else>

#### Top ${maxGlobalIssues} extra issue<#if maxGlobalIssues gt 1>s</#if>
                </#if>
            </#if>

Note: The following issues were found on lines that were not modified in the commit. Because these issues can't be reported as line comments, they are summarized here:
        <#elseif extraIssuesTruncated>

#### Top ${maxGlobalIssues} issue<#if maxGlobalIssues gt 1>s</#if>
        </#if>

        <#assign reportedIssueCount = 0>
        <#list issues(false) as issue>
            <#if reportedIssueCount < maxGlobalIssues>
1. ${print(issue)}
            </#if>
            <#assign reportedIssueCount++>
        </#list>
        <#if notReportedIssueCount gt maxGlobalIssues>
* ... ${notReportedIssueCount-maxGlobalIssues} more
        </#if>
    </#if>
</#if>
```

Inline template

```xml
<#list issues() as issue>
<@p issue=issue/>
</#list>
<#macro p issue>
${emojiSeverity(issue.severity)} ${issue.message} [:blue_book:](${issue.ruleLink})
</#macro>
```

## Gitlab

在项目中添加 .gitlab-ci.yml

```yaml
stages:
  - scanner

example-sonar:
  stage: scanner
  tags:
    - ****
  image: stable.icp:8500/testcenter/maven:3.6.1-jdk-8
  script:
    - mvn test
    - |
       mvn sonar:sonar \
       -Dsonar.host.url=http://sonarqubeurl \
       -Dsonar.login=***********  \
       -Dsonar.jacoco.reportPaths=target/jacoco.exec \
       -Dsonar.projectKey=songjianxin-maven-jacoco \
       -Dsonar.gitlab.project_id=$CI_PROJECT_PATH \
       -Dsonar.gitlab.commit_sha=$CI_COMMIT_SHA \
       -Dsonar.gitlab.ref_name=$CI_COMMIT_REF_NAME \
       -Dsonar.gitlab.url=gitlab url \
       -Dsonar.gitlab.user_token=********** \
       -Dsonar.gitlab.comment_no_issue=true -X \
       -Dsonar.gitlab.ci_merge_request_iid=$CI_MERGE_REQUEST_IID \
       -Dsonar.gitlab.merge_request_discussion=true
```

修改：

* tags 在创建gitlab runner时所输入的tags
* image  maven+jdk的即可
* sonarqubeurl  sonarqube的地址
* sonar.login   登陆 sonarqube网页->login with gitlab -> 右上角自己的姓->My Account -> Security -> Generate Tokens
生成的token只能看到一次。。如果忘记了 需要再生成一个
* sonar.gitlab.user_token   gitlab右上角用户 -> settings ->Personal Access Tokens  Scopes选api

## 最终效果

gitlab commit会自动触发gitlab ci的过程(如果某次commit不想触发 可以在commit信息中写"[skip ci]")。  
从gitlab上点击本次commit。 在comment中可以看到sonarqube 返回的结果。返回结果的第一行链接可以带着gitlab的用户认证信息跳到sonarqube中（免登陆）。并且只展示自己创建的项目。