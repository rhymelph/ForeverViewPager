# ForeverViewPager[![](https://jitpack.io/v/rhylme/ForeverViewPager.svg)](https://jitpack.io/#rhylme/ForeverViewPager)
[demo](https://fir.im/y5z7)
## How to use
### 1.gradle
#### Step 1. Add the JitPack repository to your build file
##### Add it in your root build.gradle at the end of repositories:
    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
#### Step 2. Add the dependency last version:[![](https://jitpack.io/v/rhylme/ForeverViewPager.svg)](https://jitpack.io/#rhylme/ForeverViewPager)
    dependencies {
	        compile 'com.github.rhylme:ForeverViewPager:LastVersion'
	}
### 2.xml
    <com.rhyme.foreverviewpager.ForeverViewPager
        android:id="@+id/forever_vp"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:dot_visible="true"
        app:carousel="true"
        app:dot_align="center"
        app:dot_size="50px"
        app:dot_normal="@drawable/ic_fiber_black"
        app:dot_select="@drawable/ic_fiber_while"
        app:interval="3000"
        />
#### 1.Do dot dots display? 
    app:dot_visible default true
#### 2.Carousel?
    app:carousel default true
#### 3.Dot align?
    app:dot_align default bottom center 
    other right or left or center
#### 4.Dot size?
    app:dot_size default 50px 
#### 5.Dot normal form?
    app:dot_normal defalut Black dot
#### 6.Dot select form?
    app:dot_select defalut While dot
#### 7.Carousel interval?
    app:interval default 2000mi
 
 ### 2.load data
    forever_vp.setAdapter(parameter)
#### parameter:
##### 1.int[] resList
##### 2.String[] urlList , int resHolder , int resError
##### 3.Object[] objectList , int resHolder , int resError
###### support: res , file , url , bitmap ,drawable
##### 4.List<View> viewList
  
 ### 3.other:
#### 1.Carousel start
    forever_vp.start()
#### 2.Carousel stop
    forever_vp.stop()
 
 ### If you need to connect network,please add the permission in AndroidManifest.xml:
    <uses-permission android:name="android.permission.INTERNET"/>
 ### If you need to read and write storege,please add the permission in AndroidManifest.xml:
    <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"/>
    
    //if os version >=6.0 Add the following code to where you want to use it
    if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
    //request permission ,override onRequestPermissionsResult() Complete related operations
                }else {
    //to do
                }
### version:1.19
#### 1.add onItemClickListener,you neet to put this in front of setAdapter()
        forever_vp.setOnItemClickListener(new ForeverViewPager.OnItemClickListener() {
            @Override
            public void ClickItem(View view, int position) {
                
            }
        });
	
#### 2.add PageScrollerListener,you neet to put this in front of setAdapter()
        forever_vp.setPageScrollListener(new ForeverViewPager.PageScrollListener() {
            @Override
            public void onPageScrollStateChanged(int state) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }
        });
