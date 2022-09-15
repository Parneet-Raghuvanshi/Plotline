![](https://github.com/Parneet-Raghuvanshi/Plotline/blob/main/readme/plotline-banner.jpg?raw=true "Plotline")

## Short Summary (Edge Detection)

Edge detection is an image processing technique for finding the boundaries of objects within 
images. This can be achieved by a variety of computer vision algorithms on all kinds of images.
Edge detection is a technique used in a variety of real-life applications like medical imaging,
finger-print scanning, and of course those snappy filters on social media.

<p align="center">
  <img src="https://github.com/Parneet-Raghuvanshi/Plotline/blob/main/readme/1.jpg?raw=true" title="Raw Image" width="32%">
  <img src="https://github.com/Parneet-Raghuvanshi/Plotline/blob/main/readme/2.jpg?raw=true" title="Processed Image" width="32%">
  <img src="https://github.com/Parneet-Raghuvanshi/Plotline/blob/main/readme/DASHBOARD.jpg?raw=true" title="Dashboard" width="32%">
</p>

## Implementation

- Add the JitPack repository to your build file / or inside settings.gradle if new gradle system.

```gradle
allprojects {
  repositories {
    ...
	maven { url 'https://jitpack.io' }
  }
}
```

- Now add the dependency to the app level gradle file.

```gradle
dependencies {
  implementation 'com.github.Parneet-Raghuvanshi:Plotline:1.0.0'
}
```

## Usage

- First of all cratre an Instance object of Plotline, here just Give you main activity context instead of MainActivity.this.
- implements the PlotlineHandler interface to your class.
- After implementing ovverride the `plotlineHandler()` function in your class as shown below.

```java
// This is your Activity in which you want to use Edge Detection
public class MainActivity extends AppCompatActivity implements PlotlineHandler {
    
    // inside a class (for example here --> MainActivity.java)
    Plotline plotline = new Plotline(MainActivity.this);
    
    /**
    * Here is your class body
    */
    
    // this function will recieve all the results from pltoline
    @Override
    public void plotlineHandler(Bitmap preImage,Bitmap postImage,String TYPE_CODE) {
}
```

## Functions

Plotline have **one main** Image processing function.

- `processImage()` This will take a bitmap and give back you the edge detected bitmap.
```java
{
    public Bitmap processImage(Bitmap bitmap) {...}
}
```

There are **4 types** of helper/utility functions for camera/gallery to avoid boiler-plate code, these functions are void and you can get result images from plotlineHandler function.

- `openGalleryWithDetection()` this function open gallery and then process image.
- `openCameraWithDetection()` this function open camera and then process image.
- `openGallery()` this function will only open gallery and will give you the choosen btimap.
- `openCamera()` this function will only open camera and will give you the clicked bitmap.
```java
{
    public void openGalleryWithDetection() {...}

    public void openCameraWithDetection() {...}

    public void openGallery() {...}

    public void openCamera() {...}
}
```

There are also 2 types of similar function for url processing, these functions needs the url to be passed, and you can get result images from plotlineHandler function.

- `openUrlWithDetection()` this function opens a url and then process the image.
- `openUrl()` this function will only return the bitmap.
```java
{
    public void openUrlWithDetection(String urlString) {...}
    
    public void openUrl(String urlString) {...}
}
```

## How to get Result

To get result in `plotlineHandler()` function, there are two types of response based on the String parameter `TYPE_CODE` where :-

- if `TYPE_CODE = "DETECTION"` then the result is of a function with Edge Detection and you will get bot Bitmap non-null
  - `preImage` = unprocessed raw image Bitmap. 
  - `postImage` = processed imge Bitmap (Edge Detected).


- else `TYPE_CODE = "BROWSE"` then the result is of utility like to open camera/gallerry/url and get raw image bitmap only.
  - `preImage` = unprocessed raw image Bitmap.
  - `postImage` = null this time.

## How it works internally

This is the most interesting part to know :) believe me.

Edge Detection can be achieved by a series of operations performed on the image bitmap in the form of integer byte arrays.

Such of these operations are mentioned below.
- [Thresholding](https://homepages.inf.ed.ac.uk/rbf/HIPR2/threshld.htm)
- [Sobel Edge Detector](https://homepages.inf.ed.ac.uk/rbf/HIPR2/sobel.htm)
- [Gaussian Smoothing/Convolution](https://homepages.inf.ed.ac.uk/rbf/HIPR2/gsmooth.htm)
- [Canny Edge Detector](https://homepages.inf.ed.ac.uk/rbf/HIPR2/canny.htm)

Always open for Discussions : 😊 [Parneet Raghuvanshi](mailto:parneetraghuvanshi@gmail.com)

***

<p align="center">
  <img src="https://github.com/Parneet-Raghuvanshi/EScanner-Project/blob/master/readmeresources/android-studio.png?raw=true" title="Android Studio" height="30"/>
  <img src="https://github.com/Parneet-Raghuvanshi/EScanner-Project/blob/master/readmeresources/Git-Icon.png?raw=true" title="Git" height="30"/>
  <img src="https://github.com/Parneet-Raghuvanshi/EScanner-Project/blob/master/readmeresources/java.png?raw=true" title="Java" height="30"/>
  <img src="https://github.com/Parneet-Raghuvanshi/EScanner-Project/blob/master/readmeresources/xml.png?raw=true" title="XML" height="30"/>
</p>
