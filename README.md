[![](https://jitpack.io/v/NileshJarad/ImagePicker.svg)](https://jitpack.io/#NileshJarad/ImagePicker)

Android Library that helps to easily integrate image choose option.
It shows camera and gallery option.


# 1) Setup

#### 1.1) _Add it in your root(Project) build.gradle at the end of repositories_
```js
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```
#### 1.2)  _Add the dependency to you app build.gradle_
```js
dependencies {
	        compile 'com.github.NileshJarad:ImagePicker:v1.0.2'
	}
```

# 2) Implementation
#### 2.1) Add below line to you manifest 
```js
 <application
       ...>

        <provider
            android:name="android.support.v4.content.FileProvider"
            android:authorities="${applicationId}.com.nj.imagepicker.fileprovider"
            tools:replace="android:authorities">
            <meta-data
                android:name="android.support.FILE_PROVIDER_PATHS"
                android:resource="@xml/file_paths" />
        </provider>
       ...
    </application>
```

#### 2.1) Launch the option dialog from Activity
```js
ImagePicker.build(new DialogConfiguration()
                .setTitle("Choose")
                .setOptionOrientation(LinearLayoutCompat.HORIZONTAL),
                 new ImageResultListener() {
                    @Override
                    public void onImageResult(ImageResult imageResult) {
                        ivImage.setImageBitmap(imageResult.getBitmap());
                    }
        }).show(getSupportFragmentManager());
```

#### or

```js
ImagePicker.build(new DialogConfiguration()
                , new ImageResultListener() {
                    @Override
                    public void onImageResult(ImageResult imageResult) {
                        ivImage.setImageBitmap(imageResult.getBitmap());
                    }
                }).show(getSupportFragmentManager());
```

#### 2.1) Launch the option dialog from Fragment
```js
 ImagePicker.build(new DialogConfiguration()
                .setTitle("Choose")
                .setOptionOrientation(LinearLayoutCompat.HORIZONTAL), new ImageResultListener() {
            @Override
            public void onImageResult(ImageResult imageResult) {
                ivImage.setImageBitmap(imageResult.getBitmap());
            }
        }).show(getChildFragmentManager());
```

# 3) Image result 
You can get Image Uri , path and Bitmap from ImageResult object in callback. 


## Author's [Portfolio](https://nileshjarad.github.io/)





