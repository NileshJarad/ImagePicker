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

#### 2.2.1) Launch the option dialog from Activity
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

#### 2.2.2) Launch the option dialog from Fragment
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

# 3) Full Configuration attributes

```js
 DialogConfiguration configuration = new DialogConfiguration()
                .setTitle("Choose Options")
                .setOptionOrientation(LinearLayoutCompat.HORIZONTAL)
                .setBackgroundColor(Color.GRAY)
                .setNegativeText("No")
                .setResultImageDimension(200,200)
                .setNegativeTextColor(Color.WHITE)
                .setTitleTextColor(Color.WHITE);
```
# 4) Change log 
## [1.0.3](https://github.com/skywinder/ActionSheetPicker-3.0/tree/2.2.0) (2017-03-01)
 [Full Changelog](https://github.com/skywinder/ActionSheetPicker-3.0/compare/2.1.0...2.2.0)

**Implemented enhancements:**

Closed issue of image from Camera for devices running Android L (Lollipop)
Added configurations for the setting background color, text color, dimension of result image
Removed unused library [\#1](https://github.com/NileshJarad/ImagePicker/pull/1)
 
**Merged pull requests:**

Removed unused library [\#1](https://github.com/NileshJarad/ImagePicker/pull/1)([Sagar Gangawane](https://github.com/SagarGang))

**Closed issues:**

Closed issue Camera Image is not showing [\#2](https://github.com/NileshJarad/ImagePicker/issues/2)


## Author's [Portfolio](https://nileshjarad.github.io/)





