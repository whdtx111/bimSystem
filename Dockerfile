FROM 10.5.57.53:30002/zxgj/openjdk:8-jdk-oracle

ARG env
ENV env=${env}
ENV LANG=C.UTF-8
ENV TZ=Asia/Shanghai

ADD ./target/sp-server.jar /home/app.jar

WORKDIR /home

RUN ["jar", "-xvf" , "app.jar"]

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom","-jar","/home/app.jar"]

CMD ["--spring.profiles.active=${env}"]