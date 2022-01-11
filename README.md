# BBongBBong2

### MadCamp week2 project
<img width = "200" height="200" src=https://user-images.githubusercontent.com/77828537/148908103-bed89842-9271-4102-8724-88ff75271993.png >


### 팀원
이제호, 강현희
## Abstract

카카오 맵 API를 이용해 화장실, 쓰레기통의 위치를 찾아주는 어플리케이션.

Tab 1: 화장실 <br>
Floating action button : 화장실 및 쓰레기통 추가 기능 <br>
Tab 2: 쓰레기통 <br>


## Main Features


|<img width = "200" height="400" src=https://user-images.githubusercontent.com/78259314/148903546-f644458d-e149-45d3-a66b-b5508c862df7.gif >|<img width = "200" height="400" src=https://user-images.githubusercontent.com/78259314/148902633-421f78fa-4d45-48d6-8157-bb0e26899192.gif>|<img width = "200" height="400" src=https://user-images.githubusercontent.com/78259314/148902539-c5894973-0a08-4821-94e2-4261a046cadf.gif>|
|:---:|:---:|:---:|
|위치 찾기|위치 추가|리뷰 추가|


|<img width = "200" height="400" src=https://user-images.githubusercontent.com/78259314/148903513-f8a13d86-680e-404a-b67a-bfd2b4ea92ca.gif>|<img  width = "200" height="400" src=https://user-images.githubusercontent.com/78259314/148902602-db18d906-10ce-45fd-80ea-fdaa8e248a36.gif>|
|:---:|:---:|
|거리 내 목적지 표시|에티켓 기능|

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

### 4. 에티켓 기능
- 화면 우측 상단의 재생 버튼으로 에티켓 소리를 재생할 수 있다.
- 점 아이콘을 눌러 소리를 선택할 수 있다.

## Implementation

### 지도
카카오 맵 API사용 <br>
카카오 좌표-주소 변환 API사용

### DB, Server
MongoDB, express framework 사용 <br>
서버 연결의 경우 Retrofit2 사용

```javascript
const express = require("express");
const app = express();
const path = require("path");
const port = process.env.port || 443
const mongoose = require('mongoose');

app.use(express.json());
app.use(express.urlencoded({extended: true}));

mongoose.connect('mongodb+srv://hyunhee:rkdgusgml2@cluster0.u2vfc.mongodb.net/Cluster0?retryWrites=true&w=majority');

var db = mongoose.connection; //데이터 베이스 연결 
db.on('error',console.error); // 연동 실패시 에러 메세지 출력
db.once('open', () =>{
    console.log('connected to mongoDB server'); //연동 성공시 성공 메세지 출력
}) 

const trashRouter = require('./routes/trash');
const toiletRouter = require('./routes/toilet');

app.use('/trash',trashRouter);
app.use('/toilet',toiletRouter); //라우팅 설정

app.get('/', function(req, res){
    res.send('Please specify what to get.');
});

app.listen(port, ()=>console.log(`server is running at port ${port}`));
```
- Fragment별로 라우터를 설정하여 관리했다.

```javascript

const mongoose = require('mongoose');
//화장실 스키마
var PostSchema = new mongoose.Schema({
    lat: Number,
    lng: Number,
    review: [{score:Number, comment: String}]
  });

  module.exports = mongoose.model("Toilet", PostSchema);

```
- Toilet Collection 관리를 위한 Schema

```java
    public void connectingServer() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://ip주소")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RetrofitService retrofitService = retrofit.create(RetrofitService.class);
        Call<ArrayList<Result>> res = retrofitService.getToilet();
        res.enqueue(new Callback<ArrayList<Result>>() {
            @Override
            public void onResponse(Call<ArrayList<Result>> call, Response<ArrayList<Result>> response) {
                ArrayList<Result> result = response.body();
                if (response.isSuccessful()) {
                    Log.e("test", String.valueOf(result.get(0).getId()));
                    ((AppTest) getActivity().getApplication()).setToiletList(result);
                }
                else {
                    try {
                        Log.e("err",response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                for(int i = 0; i<result.size(); i++) {
                    makeMarker(result.get(i).getId(),result.get(i).getLat(),result.get(i).getLng());
                    Log.e("id", String.valueOf(result.get(i).getLat())+ " "+String.valueOf(result.get(i).getLng()));
                }
            }
            @Override
            public void onFailure(Call<ArrayList<Result>> call, Throwable t) {
                Log.e("failed","failed");
                t.printStackTrace();
            }
        });
    }

```
- Client가 Server에서 화장실 정보를 받아오는 코드이다. 

## 시행착오와 개선방안
- 카카오 API가이드에 적힌 내용과 일치하지 않는 내용이 많아 어려움이 많았다.
- 만들어진 SDK에 커스텀하기가 힘들었다.
- 서버에서 받은 정보를 이용하여 비동기적으로 화면을 처리하는 것이 힘들었다.
