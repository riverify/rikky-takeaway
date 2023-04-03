### 悦刻外卖-readme

[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![Apache License][license-shield]][license-url]

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/riverify/rikky-takeaway">
    <img src="https://avatars.githubusercontent.com/u/97610640?v=4" alt="Logo" width="80" height="80">
  </a>

<h2 align="center">悦刻外卖</h2>

  <p align="center">
    吃得完有奖励，吃不完有惩罚
    <br />
    <a href="https://github.com/riverify/rikky-takeaway/releases"><strong>最新版本</strong></a>
    <br />
    <br />
    <a href="https://github.com/riverify/rikky-takeaway">View Code</a>
    ·
    <a href="https://github.com/riverify/rikky-takeaway/issues">Report Bug</a>
    ·
    <a href="https://github.com/riverify/rikky-takeaway/issues">Request Feature</a>
  </p>
</div>



<!-- 内容目录 -->
<details open="open">
  <summary>内容目录</summary>
  <ol>
    <li>
      <a href="#关于本项目">关于本项目</a>
      <ul>
        <li><a href="#built-with">构建于</a></li>
      </ul>
    </li>
    <li>
      <a href="#开始构建">开始构建</a>
      <ul>
        <li><a href="#所需环境">所需环境</a></li>
        <li><a href="#进行配置">进行配置</a></li>
      </ul>
    </li>
    <li><a href="#用途">用途</a></li>
    <li><a href="#贡献">贡献</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#联系">联系</a></li>
    <li><a href="#acknowledgments">Acknowledgments</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->

## 关于本项目

[![](https://github.com/riverify/rikky-takeaway/blob/main/img/login1.png?raw=true)](https://github.com/riverify/rikky-takeaway/blob/main/img/login1.png?raw=true)

这是一个较为完整功能的版本，实现了一个外卖平台基本的后台人员菜品套餐管理和前台点餐功能。但是暂不包含后续配送功能，
本代码遵循[Apache-2.0 license](https://github.com/riverify/rikky-takeaway/blob/main/LICENSE)，可以作为学习用途使用，亦可做于其它用途。

该项目Java版本为`jdk8`，主要技术有`Spring Boot` + `Mybatis Plus`，数据库使用`MySQL`，使用`Redis`缓存优化查询。
这算是我接触的第一个Spring Boot项目，作为学习的一个记录，我将每次的提交都在此仓库中，学习素材取自
[_黑马程序员Java项目实战《瑞吉外卖》，轻松掌握springboot + mybatis
plus开发核心技术的真java实战项目_](https://www.bilibili.com/video/BV13a411q753)。
相比较原教程，我做了代码优化和格式上的改进，对于短信验证，我修改成了邮箱验证的登陆方式。考虑到复杂性，项目也暂未使用主从分离设计和前后端分离，
可能存在前后端登陆越界的问题。但依然是正在学习这个项目的同学的一个参考，希望大家都能顺利完成这个项目。

<p align="right">(<a href="#悦刻外卖-readme">back to top</a>)</p>

### 构建于

* [![JDk8][JDK8.com]][JDK8-url]
* [![Spring][Spring.com]][Spring-url]
* [![Spring Boot][SpringBoot.com]][SpringBoot-url]
* [![MySQL][MySQL.com]][MySQL-url]
* [![Mybatis Plus][MybatisPlus.com]][MybatisPlus-url]
* [![Redis][Redis.com]][Redis-url]
* [![Vue][Vue.js]][Vue-url]
* [![ElementUI][ElementUI.com]][ElementUI-url]
* [![IntelliJ IDEA][IntelliJ IDEA.com]][IntelliJ IDEA-url]

<p align="right">(<a href="#悦刻外卖-readme">back to top</a>)</p>



<!-- 开始构建 -->

## 开始构建

对于熟悉Spring Boot项目的同学，以下说明略看即可，以下说明为了保证每个人都能顺利构建项目，所以会有些冗余。

### 所需环境

* JDK8 本项目由JDK8构建，请在运行本项目前确保您的电脑已安装JDK8，若您使用的是IntelliJ
  IDEA，您可以很方便的在`Project Structure`中配置JDK版本。
* MySQL 本项目使用MySQL数据库，请在运行本项目前确保您可以顺利连接到MySQL数据库。
* Redis
  本项目使用Redis缓存，请在运行本项目前确保您可以顺利连接到Redis数据库，如果实在不愿意使用Redis，请下载[v1.0版本](https://github.com/riverify/rikky-takeaway/releases/tag/v1.0.3)
  的代码，
  或在本项目的[release/basic-functionality](https://github.com/riverify/rikky-takeaway/tree/release/basic-functionality)
  分支中下载代码，该分支为本项目的基础功能分支，不包含Redis缓存功能。
  Windows用户使用和配置可以参考:[在 windows 上安装 Redis](https://www.redis.com.cn/redis-installation.html)。
* Maven 本项目使用Maven构建，初次打开项目时，IntelliJ IDEA会自动下载Maven依赖，若您的IntelliJ
  IDEA没有识别到Maven，请右键项目，选择`Add Framework Support`，选择`Maven`，然后点击`OK`。
  若在Maven下载依赖时出现问题（大部分国内用户都会出现这个问题），为了一劳永逸，建议您在IntelliJ
  IDEA的`File`->`Settings`->`Build,Execution,Deployment`->`Build Tools`->`Maven`中配置Maven。
  通过在您电脑用户目录下的`.m2`文件夹中找到`settings.xml`文件（若没有则创建一个该名的文件），在`<Settings>`内部添加以下内容：

```xml
<settings>
  <mirrors>
      <mirror>
          <id>aliyunmaven</id>
          <mirrorOf>*</mirrorOf>
          <name>阿里云公共仓库</name>
          <url>https://maven.aliyun.com/repository/public</url>
      </mirror>
  </mirrors>
</settings>
```

再在该设置目录下的`User Settings File`中选择刚刚创建的`settings.xml`文件，勾选`Override`，点击`OK`即可，再次重启IntelliJ
IDEA，Maven依赖就会自动下载了。
若`pom.xml`依然爆红，尝试再次加载。

### 进行配置

* **数据库配置** </p>
  无论你使用什么数据库**管理工具**，请在数据库中创建一个容易被记住的数据库名，例如`rikky_takeaway`，然后在导入本仓库的`sql`
  文件夹中的[`db.sql`文件](https://github.com/riverify/rikky-takeaway/blob/main/sql/db.sql)。
  之后需要回到本项目的`src/main/resources`目录下，找到`application.yml`文件，补充数据库的连接信息。


* **Redis配置** </p>
  当你已经安装好Redis并且可以顺利连接到Redis数据库时，需要回到本项目的`src/main/resources`目录下，找到`application.yml`
  文件，补充Redis的连接信息。


* **邮箱配置** </p>
  该邮箱作为发送验证码的用途，在这里我建议使用QQ邮箱，因为QQ邮箱的SMTP服务是免费的，而且不需要进行额外的配置。SMTP服务的配置信息可以在QQ邮箱的设置中找到，
  登陆[你的QQ邮箱](https://mail.qq.com/)，点击左上角的设置按钮，选择`设置`，选择`账户`，找到`开启POP3/SMTP服务`，经过一系列的确认后，
  你将获得一个`授权码`，这个授权码就是你的邮箱密码`password`，你可以在`application.yml`中的邮箱配置信息中配置它。


* **文件存储位置配置** </p>
  该系统由于需要存储用户上传的图片，所以需要配置一个文件存储的位置，建议在IntelliJ IDEA中右键`img`文件夹，`Copy Path`
  ，选择复制绝对路径，
  然后在`application.yml`的文件存储位置配置信息中配置它。

自此，配置基本完成，你可以顺利在本地运行项目了，通过运行在`src/main/java/com/rikky`目录下的`RikkyApplication.java`即可启动本项目。
后台登陆默认账号为`admin`,密码为`123456`。
前台用户界面为手机适配，电脑直接打开可能会出现排版错乱，所以请使用手机浏览器打开，
或者使用开发者工具模拟手机浏览器打开。使用手机访问需要使得你的**电脑和手机处于同一局域网**
下，[获取电脑ip地址教程](https://support.microsoft.com/zh-cn/windows/%E5%9C%A8-windows-%E4%B8%AD%E6%9F%A5%E6%89%BE-ip-%E5%9C%B0%E5%9D%80-f21a9bbc-c582-55cd-35e0-73431160a1b9#:~:text=%E5%9C%A8%E4%BB%BB%E5%8A%A1%E6%A0%8F%E4%B8%8A%EF%BC%8C%E9%80%89%E6%8B%A9%E2%80%9C%E4%BB%A5%E5%A4%AA%E7%BD%91%E7%BD%91%E7%BB%9C%E2%80%9D%E5%9B%BE%E6%A0%87,%E5%88%97%E5%87%BA%E7%9A%84IP%20%E5%9C%B0%E5%9D%80%E3%80%82)
（Linux/Macos用户自己查）。
然后在手机浏览器中输入`你的电脑的IP地址:端口号`即可访问。 而电脑推荐使用Chrome内核的浏览器，打开方法为在浏览界面按下`F12`，
然后点击`Toggle device toolbar`（通常情况下快捷键为`ctrl + shift + m`），选择`iPhone 6/7/8`，即可模拟手机浏览器打开。

对于希望在服务器上运行，同理修改配置文件（**_注意文件存储位置配置信息_**）后在IntelliJ IDEA终端输入:

`mvn package -Dmaven.test.skip=true`

即可完成打包，云服务器部署请自行利用互联网搜索。

- 后台管理系统默认链接: http://localhost:8080/backend/index.html
- 前台管理系统默认链接: http://localhost:8080/front/index.html

<!-- USAGE EXAMPLES -->

## 用途

本项目是一个外卖系统，用于学习 Spring Boot 的开发，功能较为受限，但是也有一些功能，例如：

* 用户注册
* 用户登录
* 用户地址管理
* 用户订单管理
* 用户购物车
* 员工管理
* 菜品管理
* 菜品分类管理
* 套餐管理
* 员工订单管理

[![](https://github.com/riverify/rikky-takeaway/blob/main/img/dishes.png?raw=true)](https://github.com/riverify/rikky-takeaway/blob/main/img/dishes.png?raw=true)
[![](/home/river/code/IdeaProjects/rikky-takeaway/img/login2.png)](https://github.com/riverify/rikky-takeaway/blob/main/img/login2.png?raw=true)
[![](https://github.com/riverify/rikky-takeaway/blob/main/img/choose.png?raw=true)](https://github.com/riverify/rikky-takeaway/blob/main/img/choose.png?raw=true)

这算是我接触的第一个Spring Boot项目，作为学习的一个记录，我将每次的提交都在此仓库中，学习素材取自
[_黑马程序员Java项目实战《瑞吉外卖》，轻松掌握springboot + mybatis
plus开发核心技术的真java实战项目_](https://www.bilibili.com/video/BV13a411q753)。
相比较原教程，我做了代码优化和格式上的改进，对于短信验证，我修改成了邮箱验证的登陆方式。考虑到复杂性，项目也暂未使用主从分离设计和前后端分离，
可能存在前后端登陆越界的问题。但依然是正在学习这个项目的同学的一个参考，你遇到的坑，可能是我已经在这里踩过的，但无论如何，都希望大家都能顺利完成这个项目。

<p align="right">(<a href="#悦刻外卖-readme">back to top</a>)</p>




<!-- CONTRIBUTING -->

## 贡献

**贡献是使开源社区成为一个学习、激励和创造的奇妙场所的原因。我们非常感谢您的任何贡献。**

如果你有什么建议可以让这个项目变得更好，请[fork](https://github.com/riverify/rikky-takeaway/fork)该版本并**创建一个PR**。
如果在学习中遇到了一些困难，你也可以[在这里](https://github.com/riverify/rikky-takeaway/issues)**提交一个issue**，我会尽快回复你。
如果它对你有帮助，请**star💫**它，再次感谢!

关于如何贡献的更多信息，请查看[CONTRIBUTING.md](https://gist.github.com/PurpleBooth/b24679402957c63ec426)。

1. [Fork](https://github.com/riverify/rikky-takeaway/fork) the Project
2. Create your Feature Branch (`git checkout -b feature/AmazingFeature`)
3. Commit your Changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the Branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

<p align="right">(<a href="#悦刻外卖-readme">back to top</a>)</p>



<!-- LICENSE -->

## License

Distributed under the [ApacheApache-2.0 license](https://github.com/riverify/rikky-takeaway/blob/main/LICENSE).
See `LICENSE.txt` for more information.

<p align="right">(<a href="#悦刻外卖-readme">back to top</a>)</p>



<!-- CONTACT -->

## 联系

riverify - [@riverify](https://github.com/riverify) - https://github.com/riverify

项目链接: [🔗https://github.com/riverify/rikky-takeaway](https://github.com/riverify/rikky-takeaway)

<p align="right">(<a href="#悦刻外卖-readme">back to top</a>)</p>



<!-- ACKNOWLEDGMENTS -->

## Acknowledgments

以下资源亦可帮助学习：

* [Java项目实战《瑞吉外卖》](https://www.bilibili.com/video/BV13a411q753)
* [Best-README-Template](https://github.com/othneildrew/Best-README-Template)
* [toBeBetterJavaer](https://github.com/itwanger/toBeBetterJavaer)
* [黑马程序员Java学习路线图](https://www.bilibili.com/read/cv9965357)
* [build-your-own-x自己造轮子](https://github.com/codecrafters-io/build-your-own-x)
* [编程猫codingmore](https://github.com/itwanger/codingmore-learning)

<p align="right">(<a href="#悦刻外卖-readme">back to top</a>)</p>

## End

<p align="right">(<a href="#悦刻外卖-readme">back to top</a>)</p>




<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->

[contributors-shield]: https://img.shields.io/github/contributors/riverify/rikky-takeaway.svg?style=for-the-badge

[contributors-url]: https://github.com/riverify/rikky-takeaway/graphs/contributors

[forks-shield]: https://img.shields.io/github/forks/riverify/rikky-takeaway.svg?style=for-the-badge

[forks-url]: https://github.com/riverify/rikky-takeaway/network/members

[stars-shield]: https://img.shields.io/github/stars/riverify/rikky-takeaway.svg?style=for-the-badge

[stars-url]: https://github.com/riverify/rikky-takeaway/stargazers

[issues-shield]: https://img.shields.io/github/issues/riverify/rikky-takeaway.svg?style=for-the-badge

[issues-url]: https://github.com/riverify/rikky-takeaway/issues

[license-shield]: https://img.shields.io/github/license/riverify/rikky-takeaway.svg?style=for-the-badge

[license-url]: https://github.com/riverify/rikky-takeaway/blob/master/LICENSE.txt

[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555

[linkedin-url]: https://linkedin.com/in/riverify

[product-screenshot]: images/screenshot.png

[Next.js]: https://img.shields.io/badge/next.js-000000?style=for-the-badge&logo=nextdotjs&logoColor=white

[Next-url]: https://nextjs.org/

[React.js]: https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB

[React-url]: https://reactjs.org/

[Vue.js]: https://img.shields.io/badge/Vue.js-35495E?style=for-the-badge&logo=vuedotjs&logoColor=4FC08D

[Vue-url]: https://vuejs.org/

[Angular.io]: https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white

[Angular-url]: https://angular.io/

[Svelte.dev]: https://img.shields.io/badge/Svelte-4A4A55?style=for-the-badge&logo=svelte&logoColor=FF3E00

[Svelte-url]: https://svelte.dev/

[Laravel.com]: https://img.shields.io/badge/Laravel-FF2D20?style=for-the-badge&logo=laravel&logoColor=white

[Laravel-url]: https://laravel.com

[Bootstrap.com]: https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white

[Bootstrap-url]: https://getbootstrap.com

[JQuery.com]: https://img.shields.io/badge/jQuery-0769AD?style=for-the-badge&logo=jquery&logoColor=white

[JQuery-url]: https://jquery.com

[Java.com]: https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white

[Java-url]: https://www.java.com/en/

[Python.com]: https://img.shields.io/badge/Python-3776AB?style=for-the-badge&logo=python&logoColor=white

[Python-url]: https://www.python.org/

[Spring.com]: https://img.shields.io/badge/Spring-6DB33F?style=for-the-badge&logo=spring&logoColor=white

[Spring-url]: https://spring.io/

[SpringBoot.com]: https://img.shields.io/badge/Spring_Boot-F2F4F9?style=for-the-badge&logo=spring-boot

[SpringBoot-url]: https://spring.io/projects/spring-boot

[MyBatis.com]: https://img.shields.io/badge/MyBatis-2779BD?style=for-the-badge&logo=mybatis&logoColor=white

[MyBatis-url]: https://mybatis.org/mybatis-3/

[MySQL.com]: https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white

[MySQL-url]: https://www.mysql.com/

[MybatisPlus.com]: https://img.shields.io/badge/MyBatis_Plus-2779BD?style=for-the-badge&logo=mybatis&logoColor=white

[MybatisPlus-url]: https://mybatis.plus/

[Redis.com]: https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white

[Redis-url]: https://redis.io/

[ElementUI.com]: https://img.shields.io/badge/Element_UI-4FC08D?style=for-the-badge&logo=elementdotio&logoColor=white

[ElementUI-url]: https://element.eleme.io/

[JDK8.com]: https://img.shields.io/badge/Java_8-ED8B00?style=for-the-badge&logo=java&logoColor=white

[JDK8-url]: https://www.oracle.com/java/technologies/javase/javase-jdk8-downloads.html

[IntelliJ IDEA.com]: https://img.shields.io/badge/IntelliJ_IDEA-000000?style=for-the-badge&logo=intellij-idea&logoColor=white

[IntelliJ IDEA-url]: https://www.jetbrains.com/idea/
