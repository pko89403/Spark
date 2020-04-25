## pre-requirements
~~~sh
brew install sbt
~~~

## build.sbt 라는 빌드 파일을 생성한다.
- name은 프로젝트의 이름을 정의한다
- organization은 비슷한 이름을 가진 프로젝트 간의 충돌을 방지하기 위해 사용되는 Namespace 다
- scalaVersion은 빌드하려는 스칼라 버전을 설정한다
- Version은 프로젝트의 현재 빌드 버전을 지정한다

## sbt 커맨드를 입력한다.
- compile ( 스칼라 또는 자바 소스 파일을 compile )
- package ( target 폴더에 존재하는 .jar 파일을 생성 )

## spark-submit 
~~~sh
spark-submit --master local --name test_sbt --class UsingGenericsForLikedList sbt-build-tutorials_2.13-0.0.1-SNAPSHOT.jar
~~~

### sbt-assembly
> sbt-assembly creates a fat JAR - a single JAR file containing all class files from your code and libraries. By evolution, it also contains ways of resolving conflicts when multiple JARs provide the same file path (like config or README file). It involves unzipping of all library JARs, so it's a bit slow, but these are heavily cached.

### sbt-package
> sbt-pack keeps all the library JARs intact, moves them into target/pack directory (as opposed to ivy cache where they would normally live), and makes a shell script for you to run them.

### sbt-native-packager
> sbt-native-packager is similar to sbt-pack but it was started by a sbt committer Josh Suereth, and now maintained by highly capable Nepomuk Seiler (also known as muuki88). The plugin supports a number of formats like Windows msi file and Debian deb file. The recent addition is a support for Docker images.

> All are viable means of creating deployment images. In certain cases like deploying your application to a web framework etc., it might make things easier if you're dealing with one file as opposed to a dozen.