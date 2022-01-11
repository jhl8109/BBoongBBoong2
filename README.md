# BBongBBong2
MadCamp week2 project
### 팀원
이제호, 강현희
## Abstract

카카오 맵 API를 이용해 화장실, 쓰레기통의 위치를 찾아주는 어플리케이션.

Tab 1: 화장실
Floating action button : 화장실 및 쓰레기통 추가 기능
Tab 2: 쓰레기통


## Main Features

### 1. 위치 찾기 기능

- 마커를 사용해서 지도 내에서 화장실/쓰레기통의 위치를 보여준다.
- 사용자가 바라보는 방향에 따라서 화면이 돌아간다.

### 2. 위치 및 리뷰 추가 기능 
- +버튼을 누르면 특정 종류에 대해 리뷰 추가가 가능하다.
- 이미 있는 위치라면 해당 위치 리뷰에 추가되고, 없는 위치라면 해당 위치에 새로운 마커가 추가된다.

### 3. 특정 거리 내 리뷰순 표시 기능
- 화면 우측 상단의 버튼을 누르면 특정 거리 내의 화장실/쓰레기통 리뷰를 볼 수 있다. 
- 평점이 높은 순으로 정렬되어 있으며, 해당 화장실까지의 경로가 line으로 표시되어 있다.
- 각 아이템 클릭시 해당 화장실/쓰레기통의 리뷰 목록으로 이동한다.

### 3. 에티켓 기능
- 화면 우측 상단의 재생 버튼으로 에티켓 소리를 재생할 수 있다.
- 점 아이콘을 눌러 소리를 선택할 수 있다.

## Implementation

### 지도
카카오 맵 API사용
카카오 좌표-주소 변환 API사용

### DB, Server
MongoDB, express framework 사용
서버 연결의 경우 Retrofit2 사용

## 시행착오와 개선방안
- 카카오 API가이드에 적힌 내용과 일치하지 않는 내용이 많아 어려움이 많았다.
- 만들어진 SDK에 커스텀하기가 힘들었다.
- 서버에서 받은 정보를 이용하여 비동기적으로 화면을 처리하는 것이 힘들었다.
