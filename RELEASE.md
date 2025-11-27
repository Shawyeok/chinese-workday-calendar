# Release备忘录

1. 执行`./gradlew clean test`运行单元测试，确保所有测试通过。
2. 修改`gradle.properties`中`version`版本号, 然后执行`./gradlew clean publishAllPublicationsToMavenCentralRepository`发布
3. 登录[maven central repository][1]，检查发布状态，最终状态应为`PUBLISHED`。
4. 根据版本号创建git tag，例如`v2026.0.0`
5. 若涉及年度数据更新，登录[Cloudflare][3]，使用本地的`src/main/resources/workday/years.properties`文件更新`chinese-workday-calendar`桶中的`years.properties`文件

## 参考链接
- [Publishing open source projects to Maven Central][2]

[1]: https://central.sonatype.com/publishing/deployments
[2]: https://vanniktech.github.io/gradle-maven-publish-plugin/central/
[3]: https://dash.cloudflare.com/
