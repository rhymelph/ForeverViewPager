# ForeverViewPager[![](https://jitpack.io/v/rhylme/ForeverViewPager.svg)](https://jitpack.io/#rhylme/ForeverViewPager)
[demo](https://github.com/rhylme/ForeverViewPager/raw/master/app/demo.apk)
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
#### Step 2. Add the dependency
    dependencies {
	        compile 'com.github.rhylme:ForeverViewPager:1.12'
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
##### 3.List<View> viewList
  
 ### 3.other:
#### 1.Carousel start
    forever_vp.start()
#### 2.Carousel stop
    forever_vp.stop()
 
 ### If you need to connect network,pleace add the permissions in AndroidManifest.xml:
    <uses-permission android:name="android.permission.INTERNET"/>
