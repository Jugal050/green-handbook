# Scoop Guide
## 安装：
	Windows：
		PowerShell（3+）执行命令：
			§ Set-ExecutionPolicy RemoteSigned -scope CurrentUser
			§ iex (new-object net.webclient).downloadstring('https://get.scoop.sh')

## 使用：
	scoop help
	scoop install curl
		curl -L https://get.scoop.sh 
	scoop help <command>
	scoop help install
	
	scoop search ssh 
	scoop search hg 
	
	scoop update
	scoop update curl 
	scoop update * 
