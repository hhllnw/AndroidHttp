# AndroidHttp
Android http
```Java
  POST:
        String url = "";
        HashMap<String, Object> map = new HashMap<>();
        map.put("username", "name");
        map.put("password", "123456");
        new Request.Builder()
                .with(this)
                .url(url)
                .paramMap(map)//请求参数
                .requestMethod(Request.RequestMethod.POST)//默认GET请求
                .tag(toString())//必须添加tag,不一定用toString(),用于绑定activity生命周期
                .callBack(new JsonCallBack<AccountEntity>() {
                    @Override
                    public void onSuccess(AccountEntity result) {

                    }

                    @Override
                    public void onFailure(AppException e) {

                    }
                }).build();
 ```
                
  ```Java
  GET：
        String url = "";
        HashMap<String, Object> map = new HashMap<>();
        map.put("page", 1);
        map.put("size", 100);
        new Request.Builder()
                .with(this)
                .url(url)
                .paramMap(map)
                .requestMethod(Request.RequestMethod.GET)//默认GET请求
                .isEnableProgressUpdate(true)//进度UI更新,和下面onProgressUpdated(int type, int curLen, int totalLen)连用
                .tag(toString())
                .callBack(new StringCallBack() {
                    @Override
                    public void onProgressUpdated(int type, int curLen, int totalLen) {//进度UI更新，如果有需求可重写此方法
                        mProgressBar.setMax(curLen);
                        mProgressBar.setProgress(curLen);
                    }

                    @Override
                    public void onSuccess(String result) {

                    }

                    @Override
                    public void onFailure(AppException e) {

                    }
                }).build();
  ```
说明：此library用于Android HTTP 网络请求，有GET、POST请求，用tag绑定Activity生命周期。这个是我在听了大神stay讲的Http框架后写的，<br>
加入了自己的想法，和常用习惯，后续还会添加新内容。
