# CircularProgressbar-Android

[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html)

<img src="/Screens/preview.gif" width="300" vspace="10" alt="preview" align="right"  />

Usage (Gradle dependency)
-----
  - Make sure you have jcenter() as a repository in your project  `build.gradle`
  - Add the following dependencies to your app `build.gradle`:
 
```gradle
dependencies {
	compile 'net.futuredrama.jomaceld:circular-progressbar:0.2'
}
```

XML
-----

```xml
<net.futuredrama.jomaceld.circularpblib.CircularProgressBarView
    android:id="@+id/pbar"    
    android:layout_width="match_parent"    
    android:layout_height="wrap_content"       
    app:backgroundThickness="20dp"    
    app:barThickness="16dp"    
    app:barCapStyle="butt"/>
```

You can use the following attributes in your XML declaration to tweak the ProgressBar.

##### XML Properties:

* `app:backgroundColor`: Background color
	* type: Color 
	* default: Color.DKGRAY
* `app:backgroundThickness`: Background thickness
	* type: dimension 
	* default: 10
* `app:barThickness`: Bar thickness 
	* type: dimension 
	* default: 8
* `app:startAngle`: Angle at witch the progress is 0 (-90 => 12 o'clock) 
	* type: integer 
	* default: -90
* `app:maxValue`: The maximum value of the progressbar range
	* type: float 
	* default: 1
* `app:minValue`: The minimum value of the progressbar range 
	* type: float 
	* default: 0
* `app:barCapStyle`  Bar cap style. See: [Android Paint.Cap](https://developer.android.com/reference/android/graphics/Paint.Cap.html) 
	* type: enum: {butt, round, square} 
	* default: round

Examples
-----
<img src="/Screens/loading_animation.gif" alt="loading animation" title="screenshot1" width="400" height="400"  />


Clone Github repository
-----
  1. Use git to download a copy of the whole repository to your computer (this includes the folder of the library and an example project)
  2. Import the library folder (`CircularProgressbarLib`) into Android Studio as a dependency to your project: 
   - [Add your library as a dependency](https://developer.android.com/studio/projects/android-library.html#AddDependency)
   
#License

Copyright 2016 Jose Antonio Maestre Celdran

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
