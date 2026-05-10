# =============================================================
# Multi-stage Dockerfile for Portfolio CMS (Spring Boot)
#
# Stage 1: Maven で JAR をビルド（依存関係をキャッシュレイヤーに分離）
# Stage 2: JRE のみのスリムなランタイムイメージで JAR を実行
# =============================================================

# ---- Stage 1: build ----
FROM eclipse-temurin:17-jdk-alpine AS build
WORKDIR /app

# 依存解決をキャッシュ可能なレイヤーに切り出す
COPY .mvn ./.mvn
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw -B -q dependency:go-offline

# ソースを投入してパッケージング（テストはCIで実施済みのためスキップ）
COPY src ./src
RUN ./mvnw -B -q package -DskipTests \
    && cp target/*.jar /app/app.jar

# ---- Stage 2: runtime ----
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# JAR + 同梱画像（portfolio-samples）をコピー
COPY --from=build /app/app.jar ./app.jar
COPY uploads/portfolio-samples ./uploads/portfolio-samples

# Render が指定する PORT を expose（情報用、実際は環境変数で動的バインド）
EXPOSE 8080

# 本番プロファイル + JVM のメモリを Render 無料枠（512MB）に最適化
ENV JAVA_OPTS="-XX:MaxRAMPercentage=75 -XX:+UseG1GC"
ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["sh", "-c", "exec java $JAVA_OPTS -jar app.jar"]
