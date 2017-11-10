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
	        compile 'com.github.NileshJarad:ImagePicker:v1.0.5'
	}
```

# 2) Implementation
#### 2.1) Add below line to you manifest 
```js
 <application
       ...>

        <provider
            android:name="com.nj.imagepicker.provider.ImageProvider"
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



## [1.0.5](https://github.com/NileshJarad/ImagePicker/tree/1669bda278294ea16a1d2967118562401c7bb59f) (2017-11-10)
 [Full Changelog](https://github.com/NileshJarad/ImagePicker/tree/f67696ef021ce9eb6fdbad1b09c758848a103c66)

## [1.0.4](https://github.com/NileshJarad/ImagePicker/tree/1669bda278294ea16a1d2967118562401c7bb59f) (2017-07-27)
 [Full Changelog](https://github.com/NileshJarad/ImagePicker/commit/1669bda278294ea16a1d2967118562401c7bb59f)


## [1.0.3](https://github.com/NileshJarad/ImagePicker/tree/7df01b8043dfa4fc2264cffea8b43db9768abe91) (2017-03-01)
 [Full Changelog](https://github.com/NileshJarad/ImagePicker/commit/7df01b8043dfa4fc2264cffea8b43db9768abe91)

**Implemented enhancements:**

1. Closed issue of image from Camera for devices running Android L (Lollipop)
2. Added configurations for the setting background color, text color, dimension of result image
3. Removed unused library [\#1](https://github.com/NileshJarad/ImagePicker/pull/1)
 
**Merged pull requests:**

Removed unused library [\#1](https://github.com/NileshJarad/ImagePicker/pull/1)([Sagar Gangawane](https://github.com/SagarGang))

**Closed issues:**

Closed issue Camera Image is not showing [\#2](https://github.com/NileshJarad/ImagePicker/issues/2)


## Author's [Portfolio](https://nileshjarad.github.io/)





