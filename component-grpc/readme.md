### protobuf文件处理

- 位置: 默认处理放在 main/proto 文件夹下的 .proto文件
- maven extension: 需要使存在 proto文件的模块拥有以下内容
```xml
<!--可以放在顶级pom下, 也可以放在当前模块下-->
<build>
    <extensions>
        <extension>
            <groupId>kr.motd.maven</groupId>
            <artifactId>os-maven-plugin</artifactId>
            <version>${os-maven.version}</version>
        </extension>
    </extensions>
</build>
```
- maven 插件: 需要使存在 proto文件的模块拥有以下内容
```xml

<build>
    <plugins>
        <plugin>
            <groupId>org.xolstice.maven.plugins</groupId>
            <artifactId>protobuf-maven-plugin</artifactId>
            <version>${protobuf-maven.version}</version>
            <configuration>
                <protocArtifact>com.google.protobuf:protoc:${protobuf.version}:exe:${os.detected.classifier}
                </protocArtifact>
                <pluginId>grpc-java</pluginId>
                <pluginArtifact>io.grpc:protoc-gen-grpc-java:${grpc.version}:exe:${os.detected.classifier}
                </pluginArtifact>
            </configuration>
            <executions>
                <execution>
                    <goals>
                        <goal>compile</goal>
                        <goal>compile-custom</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>

<!--也可以上面这块放在顶级pom的 pluginManagement下, 然后在当前模块下使用下面的引入-->
<build>
    <plugins>
        <plugin>
            <groupId>org.xolstice.maven.plugins</groupId>
            <artifactId>protobuf-maven-plugin</artifactId>
        </plugin>
    </plugins>
</build>
```
