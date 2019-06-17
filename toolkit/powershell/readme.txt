常用命令：
    1. 环境变量：
        查看：
            所有环境变量：dir env:
            特定环境变量：$env:path
        修改：
            $env:etcdctl_api=3
            $env:path=$env:path+";C:\home\java"
        移除：
            remove-item env:etcdctl_api


