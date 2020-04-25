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

## spark-submit --master local --name test_sbt --class UsingGenericsForLikedList sbt-build-tutorials_2.13-0.0.1-SNAPSHOT.jar