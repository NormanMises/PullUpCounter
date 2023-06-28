#基础镜像使用jdk1.8
FROM java:8
#暴露8080端口
EXPOSE 8080
#作者
MAINTAINER ldu

# VOLUME 指定临时文件目录为/tmp，在主机/var/lib/docker目录下创建了一个临时文件并链接到容器的/tmp
VOLUME /tmp

# 将jar包添加到容器更名为app.jar
ADD ruoyi-admin.jar app.jar

# 运行jar包
 RUN bash -c 'touch /app.jar'

# 为了缩短 Tomcat 启动时间，添加一个系统属性指向 “/dev/./urandom” 作为 Entropy Source
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]
#ENTRYPOINT ["java","-jar","app.jar"]
#ENTRYPOINT ["nohup","java","-jar","/data/cat/ruoyi-admin.jar","&"]

