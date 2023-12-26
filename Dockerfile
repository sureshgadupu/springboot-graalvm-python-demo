FROM ghcr.io/graalvm/jdk-community:21


ARG GRAALVM_VERSION=23.1.1
ARG GRAALVM_AMD_PKG=https://github.com/oracle/graalpython/releases/download/graal-$GRAALVM_VERSION/graalpy-$GRAALVM_VERSION-linux-amd64.tar.gz
ARG GRAALVM_ARM_PKG=https://github.com/oracle/graalpython/releases/download/graal-$GRAALVM_VERSION/graalpy-$GRAALVM_VERSION-linux-aarch64.tar.gz
ARG USER_HOME_DIR="/root"

ENV LANG=en_US.UTF-8 \
    PATH=/opt/graalpy-$GRAALVM_VERSION/bin:$PATH

RUN microdnf install -y curl tar unzip gzip zlib openssl-devel gcc gcc-c++ make  pip patch libtool  glibc-langpack-en libxcrypt-compat wget && rm -rf /var/cache/dnf

RUN uname -m > /tmp/architecture.txt

# conditional installation based on architecture
RUN if [ "$(cat /tmp//architecture.txt)" = "x86_64" ]; then \
       mkdir -p /opt/graalpy-$GRAALVM_VERSION && curl --fail --silent --location --retry 3 $GRAALVM_AMD_PKG | gunzip | tar x -C /opt/graalpy-$GRAALVM_VERSION --strip-components=1; \
    elif [ "$(cat /tmp//architecture.txt)" = "aarch64" ]; then \
       mkdir -p /opt/graalpy-$GRAALVM_VERSION && curl --fail --silent --location --retry 3 $GRAALVM_ARM_PKG | gunzip | tar x -C /opt/graalpy-$GRAALVM_VERSION --strip-components=1; \
    else \
        echo "Architecture not supported"; \
    fi

RUN groupadd  springboot && useradd -g springboot sbuser

RUN graalpy -m venv "$USER_HOME_DIR/.local/vfs/venv"

RUN source "$USER_HOME_DIR/.local/vfs/venv/bin/activate"

COPY requirements.txt .

RUN "$USER_HOME_DIR/.local/vfs/venv/bin/graalpy" -m pip install --no-cache-dir -r requirements.txt

WORKDIR /app

ENV DOCKER_BUILDKIT=1
COPY mvnw mvnw
COPY .mvn .mvn
COPY pom.xml .
COPY src src

RUN chmod +x mvnw

RUN ./mvnw clean test

RUN ./mvnw clean package

ENTRYPOINT ["java", "-Dpolyglotimpl.DisableClassPathIsolation=true", "-jar", "./target/springboot-graalvm-python-demo-0.0.1-SNAPSHOT.jar"]

