# AndroidHttp
 ```Java
说明：此library用于Android HTTP 网络请求，有GET、POST，DELETE,PUT网络请求

 ```
 ```Java
##一：添加依赖：
     1、Add it in your root build.gradle at the end of repositories:
   allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

    2、Add the dependency
    dependencies {
	        compile 'com.github.hhllnw:AndroidHttp:v1.2.3'
	     }
```
```Java
二：使用方法：
1、在你的项目里新建名为BaseActivity的父Activity,BaseActivity继承AppActivity；

2、   //重写此方法，设置缓冲UI中的文字
      protected void setProgressDialogMessage() {
          mRequestManager.setProgressDialogListener(this, "请稍后...");
      }
3、 //在BaseActivity中写异常那个处理逻辑
    public boolean handleExpection(AppException e) {
            if (e.getStatusCode() == 401) {
                //such as token inavlid,need to relogin
                return true;
            }
            return false;
        }
4、//设置缓冲UI样式设置
   public void showDialog(String message) {

       }
 ```

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
                .tag(toString())//必须添加tag,参数必须为toString(),用于绑定activity生命周期
                .OnGlobalExpectionListener(this)//异常统一处理，必选项
                .callBack(new JsonCallBack<AccountEntity>() {
                    @Override
                    public void onSuccess(AccountEntity result) {

                    }

                    @Override
                    public void onFailure(AppException e) {

                    }
                }).build();
                

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
                .OnGlobalExpectionListener(this)
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
