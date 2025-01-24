# 第一阶段：构建阶段
FROM maven:3.8.5-openjdk-17 AS build-stage

WORKDIR /app

COPY pom.xml .

RUN mvn dependency:go-offline

COPY src /app/src
# 打包
RUN mvn clean package -DskipTests

FROM bellsoft/liberica-openjdk-alpine:17 AS production-stage
LABEL authors="aniwoh"
# 将构建好的 jar 文件复制到新的镜像中
COPY --from=build-stage /app/target/*.jar app.jar
EXPOSE 8888
ENTRYPOINT ["java", "-jar", "/app.jar"]