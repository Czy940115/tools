# git上传文件步骤

## 1. 上传文件步骤

```
1.git init (初始化仓库)
2.git add * (将当前文件提交到该仓库中)
3.git commit -m "提交描述" (提交操作)
4. git branch -M main (切换到main分支)
5. git remote add origin https://github.com/Czy940115/Blog.git (链接到git仓库中)
6. git pull origin main --allow-unrelated-histories(从远程仓库的主分支（"main"）拉取最新的更改到本地仓库)
7. git push -u origin main(提交到远程仓库)
```



## 2. 删除文件步骤

```
1.git pull origin master                    # 将远程仓库里面的项目拉下来
2.dir                                                # 查看有哪些文件夹
3.git rm -r --cached READ.md  # 删除READ.md文件
4.git commit -m '删除了READ.md'        # 提交,添加操作说明
5.git push -u origin master               # 将本次更改更新到github项目上去
```



## 3.更新文件步骤

```
1.git add * (将当前文件提交到该仓库中)
2.git commit -m "提交描述" (提交操作)
3.git push -u origin main(提交到远程仓库)
```

